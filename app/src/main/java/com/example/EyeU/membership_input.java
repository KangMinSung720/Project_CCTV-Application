package com.example.EyeU;

import android.app.AlertDialog;
import android.app.ProgressDialog;
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
import android.widget.Toast;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.net.URL;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

public class membership_input extends AppCompatActivity {

    Button ov, com;
    ImageButton ba;
    EditText id, pw, pw2, name, email;
    View.OnClickListener cl;
    AlertDialog dialog;
    String resultId="";


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.membership_input);

        ov = (Button) findViewById(R.id.overlap);
        com = (Button) findViewById(R.id.complete);
        ba = (ImageButton) findViewById(R.id.back);
        id = (EditText) findViewById(R.id.id);
        pw = (EditText) findViewById(R.id.pw);
        pw2 = (EditText) findViewById(R.id.pw2);
        name = (EditText) findViewById(R.id.name);
        email = (EditText) findViewById(R.id.email);

        cl = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.back:
                        Intent i = new Intent(membership_input.this, login_screen.class);
                        startActivity(i);
                        finish();
                        break;
                        case R.id.overlap:
                        if (id.getText().toString().length() > 0) {
                            new IdCheck().execute(id.getText().toString());
                        } else {
                            AlertDialog.Builder builder = new AlertDialog.Builder(membership_input.this);
                            dialog = builder.setMessage("아이디 입력을 확인해 주세요!")
                                    .setPositiveButton("확인",null)
                                    .create();
                            dialog.show();
                            return;
                        }

                        break;
                    case R.id.complete:

                        //비밀번호확인 조건문 필요
                        if(id.getText().toString().length()>0&&pw.getText().toString().length()>0&&pw.getText().toString().equals(pw2.getText().toString())&&name.getText().toString().length()>0&&email.getText().toString().length()>0){
                            if(resultId.equals(id.getText().toString())){
                                new InsertData().execute(id.getText().toString(), pw.getText().toString(), name.getText().toString(), email.getText().toString());

                            }else{
                                AlertDialog.Builder builder = new AlertDialog.Builder(membership_input.this);
                                dialog = builder.setMessage("아이디 중복확인을 해주세요!")
                                        .setPositiveButton("확인",null)
                                        .create();
                                dialog.show();
                                return;
                            }
                        }else if(id.getText().toString().length() == 0){
                            Toast.makeText(membership_input.this, "아이디를 입력해 주세요!", Toast.LENGTH_SHORT).show();
                        }else if(pw.getText().toString().length() == 0){
                            Toast.makeText(membership_input.this, "비밀번호를 입력해 주세요!", Toast.LENGTH_SHORT).show();
                        }else if(!pw2.getText().toString().equals(pw.getText().toString())){
                            Toast.makeText(membership_input.this, "비밀번호가 일치하지 않습니다!", Toast.LENGTH_SHORT).show();
                        }else if(name.getText().toString().length() == 0){
                            Toast.makeText(membership_input.this, "이름을 입력해 주세요!", Toast.LENGTH_SHORT).show();
                        }else if(email.getText().toString().length() == 0){
                            Toast.makeText(membership_input.this, "이메일을 입력해 주세요!", Toast.LENGTH_SHORT).show();
                        }
//
//                        Toast.makeText(getApplicationContext(),"회원가입이 정상적으로 완료되었습니다! 로그인을 해주세요.",Toast.LENGTH_LONG).show();
//                        Intent i3 = new Intent(membership_input.this, login_screen.class);
//                        startActivity(i3);
//                        finish();
                        break;
                }

            }
        };
        ba.setOnClickListener(cl);
        ov.setOnClickListener(cl);
        com.setOnClickListener(cl);


    }
    private class IdCheck extends  AsyncTask<String, Void, Document> {

        @Override
        protected Document doInBackground(String... params) {
            Document doc = null;
            try {
                String Id = (String) params[0];
                String link = "http://shingu403.cafe24.com/007_id_check.php?um_id="+Id;

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
                    // 이미  아이디가있음
                    AlertDialog.Builder builder = new AlertDialog.Builder(membership_input.this);
                    dialog = builder.setMessage("사용 불가능한 아이디 입니다. 다시 입력해 주세요!")
                            .setNegativeButton("확인",null)
                            .create();
                    dialog.show();
                    return;
                }else{
                    AlertDialog.Builder builder = new AlertDialog.Builder(membership_input.this);
                    dialog = builder.setMessage("사용 가능한 아이디 입니다. 계속 진행해 주세요!")
                            .setPositiveButton("확인",null)
                            .create();
                    dialog.show();
                    resultId= id.getText().toString();
                }


            } catch (Exception e) {
                Log.e("리얼익셉션", e+"");
                e.printStackTrace();
            }

        }



    }


    private class InsertData extends AsyncTask<String, Void, Document> {
        ProgressDialog loading;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            loading = ProgressDialog.show(membership_input.this, "회원정보 확인중..", null, true, true);
        }

        @Override
        protected Document doInBackground(String... params) {
            Document doc = null;
            try {
                String Id = (String) params[0];
                String Pw = (String) params[1];
                String Name = (String) params[2];
                String Email = (String) params[3];

                String link = "http://shingu403.cafe24.com/007_member_insert.php?um_id="+Id+"&um_pw="+Pw+"&um_name="+Name+"&um_email="+Email;

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
            loading.dismiss();
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
                    Toast.makeText(getApplicationContext(), "회원가입이 정상적으로 완료되었습니다! 로그인을 해주세요.", Toast.LENGTH_LONG).show();
                    Intent i3 = new Intent(membership_input.this, login_screen.class);
                    startActivity(i3);
                    finish();
                } else {

                }


            } catch (Exception e) {
                Log.e("리얼익셉션", e.toString());
                e.printStackTrace();
            }

        }
    }

}


