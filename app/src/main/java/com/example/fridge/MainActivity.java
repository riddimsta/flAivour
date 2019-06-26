package com.example.fridge;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;


// Start screen with logo and button

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void goToScanning(View v) {
        Intent myIntent = new Intent(getBaseContext(), Scanning.class);
        startActivity(myIntent);
    }
}
