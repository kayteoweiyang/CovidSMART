package com.anf.covid_smart;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class ConfirmBooking extends AppCompatActivity {

    TextView type, date, time;
    Button btnConfirm, btnBack;
    String selecteddate, selectedtype;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_booking);

        btnConfirm = findViewById(R.id.bookConfirm);
        btnBack = findViewById(R.id.backConfirm);
        type = findViewById(R.id.txtTypeConfirm);
        date = findViewById(R.id.txtDateConfirm);
        time = findViewById(R.id.txtTimeConfirm);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            selecteddate = extras.getString("date");
            selectedtype = extras.getString("type");
        }

        type.setText(selectedtype);
        date.setText(selecteddate);

        btnConfirm.setOnClickListener(buttonsOnClickListener);
    }
    private View.OnClickListener buttonsOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.bookConfirm:
                    Intent bookedIntent = new Intent(ConfirmBooking.this, HomeActivity.class);
                    Toast.makeText(ConfirmBooking.this, "Successfully Booked", Toast.LENGTH_LONG).show();
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