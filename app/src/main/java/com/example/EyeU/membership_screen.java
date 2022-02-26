package com.example.EyeU;

import androidx.appcompat.app.AppCompatActivity;

import androidx.annotation.Nullable;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;


public class membership_screen extends AppCompatActivity {

    Button con, can;
    CheckBox all, ch1, ch2, ch3;
    TextView ch1i,ch2i,ch3i;
    ScrollView ch1c,ch2c,ch3c;
    View.OnClickListener cl;
    CheckBox.OnCheckedChangeListener ccl;
    Boolean allB=false, ch1b=false, ch2b=false, ch3b=false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.membership_screen);

        con = (Button) findViewById(R.id.confirm);
        can = (Button) findViewById(R.id.cancel);
        ch1i = (TextView) findViewById(R.id.check1info);
        ch2i = (TextView) findViewById(R.id.check2info);
        ch3i = (TextView) findViewById(R.id.check3info);
        all = (CheckBox) findViewById(R.id.allcheck);
        ch1 = (CheckBox) findViewById(R.id.check1);
        ch2 = (CheckBox) findViewById(R.id.check2);
        ch3 = (CheckBox) findViewById(R.id.check3);
        ch1c = (ScrollView) findViewById(R.id.check1content);
        ch2c = (ScrollView) findViewById(R.id.check2content);
        ch3c = (ScrollView) findViewById(R.id.check3content);

        cl = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.confirm :
                        if(all.isChecked()) {
                            Intent i = new Intent(membership_screen.this, membership_input.class);
                            startActivity(i);
                        }else {
                            Toast.makeText(getApplicationContext(),"약관동의에 대한 체크여부를 확인해 주세요!",Toast.LENGTH_SHORT).show();
                        }
                        break;
                    case R.id.cancel :
                        Intent i2 = new Intent(membership_screen.this, login_screen.class);
                        startActivity(i2);
                        finish();
                        break;
                    case R.id.check1info :
                        if (ch1c.getVisibility() == v.GONE ) {
                            ch1c.setVisibility(v.VISIBLE);
                            ch1i.setText("간략히");
                        }else {
                            ch1c.setVisibility(v.GONE);
                            ch1i.setText("보기");
                        }
                        break;
                    case R.id.check2info :
                        if (ch2c.getVisibility() == v.GONE ) {
                            ch2c.setVisibility(v.VISIBLE);
                            ch2i.setText("간략히");
                        }else {
                            ch2c.setVisibility(v.GONE);
                            ch2i.setText("보기");
                        }
                        break;
                    case R.id.check3info :
                        if (ch3c.getVisibility() == v.GONE ) {
                            ch3c.setVisibility(v.VISIBLE);
                            ch3i.setText("간략히");
                        }else {
                            ch3c.setVisibility(v.GONE);
                            ch3i.setText("보기");
                        }
                        break;
                }

            }
        };
        con.setOnClickListener(cl);
        can.setOnClickListener(cl);
        ch1i.setOnClickListener(cl);
        ch2i.setOnClickListener(cl);
        ch3i.setOnClickListener(cl);

        ccl = new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean b) {
                switch (buttonView.getId()) {
                    case R.id.allcheck:
                        allB = b;
                        if (b) {
                            if (!ch1.isChecked())
                                ch1.setChecked(b);
                            if (!ch2.isChecked())
                                ch2.setChecked(b);
                            if (!ch3.isChecked())
                                ch3.setChecked(b);
                        } else {
                            if (all.isPressed()) { //전체동의 체크박스를 직접 체크 했는지?
                                ch1.setChecked(b);
                                ch2.setChecked(b);
                                ch3.setChecked(b);
                            }
                        }
                        break;
                    case R.id.check1:
                        ch1b = b;
                        if (ch1b && ch2b && ch3b) {
                            all.setChecked(true);
                        } else {
                            all.setChecked(false);
                        }
                        break;
                    case R.id.check2:
                        ch2b = b;
                        if (ch1b && ch2b && ch3b) {
                            all.setChecked(true);
                        } else {
                            all.setChecked(false);
                        }
                        break;
                    case R.id.check3:
                        ch3b = b;
                        if (ch1b && ch2b && ch3b) {
                            all.setChecked(true);
                        } else {
                            all.setChecked(false);
                        }
                        break;
                }

            }
        };
        all.setOnCheckedChangeListener(ccl);
        ch1.setOnCheckedChangeListener(ccl);
        ch2.setOnCheckedChangeListener(ccl);
        ch3.setOnCheckedChangeListener(ccl);


    }
}
