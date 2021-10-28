package com.anf.covid_smart;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class ConfirmBooking extends AppCompatActivity {

    TextView type, date, time;
    Button btnConfirm, btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_booking);
        
        btnConfirm = findViewById(R.id.bookConfirm);
        btnBack = findViewById(R.id.backConfirm);
        type = findViewById(R.id.txtTypeConfirm);
        date = findViewById(R.id.txtDateConfirm);
        time = findViewById(R.id.txtTimeConfirm);

        btnConfirm.setOnClickListener(buttonsOnClickListener);
    }
    private View.OnClickListener buttonsOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.bookConfirm:
                    Intent bookedIntent = new Intent(ConfirmBooking.this, HomeActivity.class);
                    bookedIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(bookedIntent);
                    finish();
                    break;
                case R.id.backConfirm:
                    onBackPressed();
                    break;
                default:
                    throw new IllegalStateException("Unexpected value: " + v.getId());
            }
        }
    };
    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}