package com.example.EyeU;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.AsyncTask;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import androidx.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.net.URL;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

public class membership_lookup extends AppCompatActivity {

    ImageButton ba;
    Button fi,passfi;
    EditText na,em;
    View.OnClickListener cl;
    AlertDialog dialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.membership_lookup);

        ba = (ImageButton) findViewById(R.id.back);
        fi = (Button) findViewById(R.id.find);
        passfi = (Button) findViewById(R.id.passwordfind);
        na = (EditText) findViewById(R.id.name);
        em = (EditText) findViewById(R.id.email);

        cl = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.back :
                        Intent i = new Intent(membership_lookup.this, login_screen.class);
                        startActivity(i);
                        finish();
                        break;
                    case  R.id.find :
                        if(na.getText().toString().length()>0 && em.getText().toString().length()>0) {
                            new IdFind().execute(na.getText().toString(),em.getText().toString());
                        }else if (na.getText().toString().length() == 0){
                            Toast.makeText(membership_lookup.this,"이름을 입력해 주세요!",Toast.LENGTH_SHORT).show();
                        }else if (em.getText().toString().length() == 0){
                            Toast.makeText(membership_lookup.this,"이메일을 입력해 주세요!",Toast.LENGTH_SHORT).show();
                        }
                        break;
                    case R.id.passwordfind :
                        Intent i3 = new Intent(membership_lookup.this, membership_lookup2.class);
                        startActivity(i3);
                        finish();
                        break;
                }

            }
        };
        ba.setOnClickListener(cl);
        fi.setOnClickListener(cl);
        passfi.setOnClickListener(cl);
    }

    public class IdFind extends AsyncTask<String, Void, Document> {

        @Override
        protected Document doInBackground(String... params) {
            Document doc = null;
            try {
                String Name = (String) params[0];
                String Email = (String) params[1];
                String link = "http://shingu403.cafe24.com/007_id_find.php?um_name="+Name+"&um_email="+Email;

                URL url = new URL(link);

                DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
                DocumentBuilder db = dbf.newDocumentBuilder();
                doc = db.parse(url.openStream());
                doc.getDocumentElement().normalize();

                return doc;
            } catch (Exception e) {
                Log.e("에러", e.getMessage());

            }
            return doc;
        }

        @Override
        protected void onPostExecute(Document s) {
            super.onPostExecute(s);
            String value = "빔";
            String  idvalue = "null";
            try {
                NodeList itemNodeList = s.getElementsByTagName("item");
                for (int i = 0; i < itemNodeList.getLength(); i++) {


                    Node node = itemNodeList.item(0);
                    Element element = (Element) node;

                    NodeList command = element.getElementsByTagName("command");
                    value = command.item(0).getChildNodes().item(0).getNodeValue();

                    NodeList id = element.getElementsByTagName("um_id");
                    idvalue = id.item(0).getChildNodes().item(0).getNodeValue();
                    //Xml파싱


                }

                if (value.equals("Y")) {
                    // 회원 정보 일치
                    AlertDialog.Builder builder = new AlertDialog.Builder(membership_lookup.this);
                    dialog = builder.setMessage(String.format("귀하의 아이디는 %s 입니다",idvalue))
                            .setPositiveButton("확인",null)
                            .create();
                    dialog.show();
                }else{
                    // 불일치
                    AlertDialog.Builder builder = new AlertDialog.Builder(membership_lookup.this);
                    dialog = builder.setMessage("회원 정보가 올바르지 않습니다!")
                            .setNegativeButton("확인",null)
                            .create();
                    dialog.show();
                    return;
                }


            } catch (Exception e) {
                Log.e("리얼익셉션", e+"");
                e.printStackTrace();
            }

        }



    }



}
