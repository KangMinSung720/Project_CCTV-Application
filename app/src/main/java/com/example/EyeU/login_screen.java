package com.example.EyeU;

import android.app.AlertDialog;
import android.os.AsyncTask;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.net.URL;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

public class login_screen extends AppCompatActivity {

    Button log;
    TextView te4,te5;
    EditText id,pw;
    View.OnClickListener cl;
    AlertDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_screen);

        log = (Button) findViewById(R.id.login);
        te4 = (TextView) findViewById(R.id.te4);
        te5 = (TextView) findViewById(R.id.te5);
        id = (EditText) findViewById(R.id.id);
        pw = (EditText) findViewById(R.id.pw);

        cl = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.login :
                        if(id.getText().toString().length()>0 && pw.getText().toString().length()>0) {
                            new Login().execute(id.getText().toString(),pw.getText().toString());
                        }else if (id.getText().toString().length() == 0){
                            Toast.makeText(login_screen.this,"아이디를 입력해 주세요!",Toast.LENGTH_SHORT).show();
                        }else if (pw.getText().toString().length() == 0){
                            Toast.makeText(login_screen.this,"비밀번호를 입력해 주세요!",Toast.LENGTH_SHORT).show();
                        }
                        break;
                    case R.id.te4 :
                        Intent i2 = new Intent(login_screen.this, membership_screen.class);
                        startActivity(i2);
                        break;
                    case R.id.te5 :
                        Intent i3 = new Intent(login_screen.this, membership_lookup.class);
                        startActivity(i3);
                        break;

                }

            }
        };
        log.setOnClickListener(cl);
        te4.setOnClickListener(cl);
        te5.setOnClickListener(cl);


    }

    private class Login extends AsyncTask<String, Void, Document> {

        @Override
        protected Document doInBackground(String... params) {
            Document doc = null;
            try {
                String Id = (String) params[0];
                String Pw = (String) params[1];
                String link = "http://shingu403.cafe24.com/007_id_check.php?um_id="+Id+"&um_pw="+Pw;

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
            Log.e("결과",s.toString());
            String value = "빔";
            try {
                NodeList itemNodeList = s.getElementsByTagName("item");
                for (int i = 0; i < itemNodeList.getLength(); i++) {


                    Node node = itemNodeList.item(0);
                    Element element = (Element) node;

                    NodeList command = element.getElementsByTagName("command");
                    value = command.item(0).getChildNodes().item(0).getNodeValue();
                    //Xml파싱


                }

                if (value.equals("Y")) {
                    // 회원 정보 일치
                    Toast.makeText(login_screen.this,"환영합니다",Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(login_screen.this, main_screen.class);
                    startActivity(i);
                    finish();
                }else{ // 회원 정보 불일치
                    AlertDialog.Builder builder = new AlertDialog.Builder(login_screen.this);
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
}
