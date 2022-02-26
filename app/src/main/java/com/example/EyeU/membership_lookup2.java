package com.example.EyeU;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.net.URL;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

public class membership_lookup2 extends AppCompatActivity {

    ImageButton ba;
    Button fi,idfi,ch;
    EditText id,em,pw,pw2;
    LinearLayout pwch;
    View.OnClickListener cl;
    AlertDialog dialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.membership_lookup2);

        ba = (ImageButton) findViewById(R.id.back);
        fi = (Button) findViewById(R.id.find);
        idfi = (Button) findViewById(R.id.idfind);
        ch = (Button) findViewById(R.id.change);
        id = (EditText) findViewById(R.id.id);
        em = (EditText) findViewById(R.id.email);
        pw = (EditText) findViewById(R.id.newpw);
        pw2 = (EditText) findViewById(R.id.newpw2);
        pwch = (LinearLayout) findViewById(R.id.pwch);

        cl = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.back :
                        Intent i = new Intent(membership_lookup2.this, login_screen.class);
                        startActivity(i);
                        finish();
                        break;
                    case  R.id.find :
                        if(id.getText().toString().length()>0 && em.getText().toString().length()>0) {
                            new PwFind().execute(id.getText().toString(),em.getText().toString());
                        }else if (id.getText().toString().length() == 0){
                            Toast.makeText(membership_lookup2.this,"아이디를 입력해 주세요!",Toast.LENGTH_SHORT).show();
                        }else if (em.getText().toString().length() == 0){
                            Toast.makeText(membership_lookup2.this,"이메일을 입력해 주세요!",Toast.LENGTH_SHORT).show();
                        }
                        break;
                    case R.id.change :
                        if(pw.getText().toString().equals(pw2.getText().toString())){
                            new PwChange().execute(id.getText().toString(),pw.getText().toString());

                        } else {
                            AlertDialog.Builder builder = new AlertDialog.Builder(membership_lookup2.this);
                            dialog = builder.setMessage("비밀번호가 일치하지 않습니다!")
                                    .setPositiveButton("확인", null )
                                    .create();
                            dialog.show();
                        }

                        break;
                    case R.id.idfind :
                        Intent i3 = new Intent(membership_lookup2.this, membership_lookup.class);
                        startActivity(i3);
                        finish();
                        break;
                }

            }
        };
        ba.setOnClickListener(cl);
        fi.setOnClickListener(cl);
        ch.setOnClickListener(cl);
        idfi.setOnClickListener(cl);
    }

    private class PwFind extends AsyncTask<String, Void, Document> {

        @Override
        protected Document doInBackground(String... params) {
            Document doc = null;
            try {
                String Id = (String) params[0];
                String Email = (String) params[1];
                String link = "http://shingu403.cafe24.com/007_pw_find.php?um_id="+Id+"&um_email="+Email;

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
        //    String  idvalue = "null";
            try {
                NodeList itemNodeList = s.getElementsByTagName("item");
                for (int i = 0; i < itemNodeList.getLength(); i++) {


                    Node node = itemNodeList.item(0);
                    Element element = (Element) node;

                    NodeList command = element.getElementsByTagName("command");
                    value = command.item(0).getChildNodes().item(0).getNodeValue();

                  //  NodeList id = element.getElementsByTagName("um_id");
                  //  idvalue = id.item(0).getChildNodes().item(0).getNodeValue();
                    //Xml파싱


                }


                if (value.equals("Y")) {
                    // 회원 정보 일치
                   pwch.setVisibility(View.VISIBLE);
                }else{ // 회원 정보 불일치
                    AlertDialog.Builder builder = new AlertDialog.Builder(membership_lookup2.this);
                    dialog = builder.setMessage("회원 정보가 올바르지 않습니다!")
                            .setPositiveButton("확인",null)
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

    private class PwChange extends AsyncTask<String, Void, Document> {

        @Override
        protected Document doInBackground(String... params) {
            Document doc = null;
            try {
                String Id = (String) params[0];
                String Pw = (String) params[1];

                String link = "http://shingu403.cafe24.com/007_pw_change.php?um_id="+Id+"&um_pw="+Pw;

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
            //    String  idvalue = "null";
            try {
                NodeList itemNodeList = s.getElementsByTagName("item");
                for (int i = 0; i < itemNodeList.getLength(); i++) {


                    Node node = itemNodeList.item(0);
                    Element element = (Element) node;

                    NodeList command = element.getElementsByTagName("command");
                    value = command.item(0).getChildNodes().item(0).getNodeValue();

                    //  NodeList id = element.getElementsByTagName("um_id");
                    //  idvalue = id.item(0).getChildNodes().item(0).getNodeValue();
                    //Xml파싱


                }


                if (value.equals("Y")) {
                    // 회원 정보 일치
                    AlertDialog.Builder builder = new AlertDialog.Builder(membership_lookup2.this);
                    dialog = builder.setMessage("성공적으로 변경되었습니다")
                            .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    finish();
                                }
                            })
                            .create();
                    dialog.show();
                    return;
                }else{ // 회원 정보 불일치
                    Toast.makeText(membership_lookup2.this, "변경에실패하였습니다.", Toast.LENGTH_SHORT).show();
                }


            } catch (Exception e) {
                Log.e("리얼익셉션", e+"");
                e.printStackTrace();
            }

        }



    }
}
