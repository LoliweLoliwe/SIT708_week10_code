package com.loliwe.lfmapapp;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    Button createBtn, displayBtn, mapBtn;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        createBtn = findViewById(R.id.button1);
        displayBtn = findViewById(R.id.button2);
        mapBtn = findViewById(R.id.btn_geo);

        createBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent newIntent = new Intent(MainActivity.this, NewAdActivity.class);
                startActivity(newIntent);
            }
        });

        displayBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent newIntent = new Intent(MainActivity.this, DisplayActivity.class);
                startActivity(newIntent);
            }
        });

        mapBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, MapActivity.class);
                startActivity(intent);
            }
        });
    }
}