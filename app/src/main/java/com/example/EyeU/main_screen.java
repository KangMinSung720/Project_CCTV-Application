package com.example.EyeU;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

public class main_screen extends AppCompatActivity {

    Button log;
    ImageButton live,rec;
    View.OnClickListener cl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_screen);

        log = (Button) findViewById(R.id.logout);
        live = (ImageButton) findViewById(R.id.live);
        rec = (ImageButton) findViewById(R.id.record);

        cl = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.logout :
                        Intent i = new Intent(main_screen.this, login_screen.class);
                        startActivity(i);
                        finish();
                        break;
                    case R.id.live :
                        Intent i2 = new Intent(main_screen.this, live_screen.class);
                        startActivity(i2);
                        break;
                    case R.id.record :
                        Intent i3 = new Intent(main_screen.this, record_screen.class);
                        startActivity(i3);
                        break;
                }

            }
        };
        log.setOnClickListener(cl);
        live.setOnClickListener(cl);
        rec.setOnClickListener(cl);
    }
}
