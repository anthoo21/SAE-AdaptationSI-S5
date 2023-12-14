package com.example.saedolistocks5;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void clickCo(View Button){
        Intent intention = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(intention);
    }

    public void clickDeco(View Button){
        Intent intention = new Intent(MainActivity.this, ListeActivity.class);
        startActivity(intention);
    }
}