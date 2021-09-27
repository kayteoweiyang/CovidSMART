package com.anf.covid_smart;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Logout extends AppCompatActivity {

    Button loginbtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logout);

        loginbtn = findViewById(R.id.loginbtnLogout);

        loginbtn.setOnClickListener(buttonsOnClickListener);
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
    }
    private View.OnClickListener buttonsOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.loginbtnLogout:
                    Intent backtoLogin = new Intent(Logout.this, MainActivity.class);
                    startActivity(backtoLogin);
                    finish();
                    break;
                default:
                    throw new IllegalStateException("Unexpected value: " + v.getId());
            }
        }
    };
}