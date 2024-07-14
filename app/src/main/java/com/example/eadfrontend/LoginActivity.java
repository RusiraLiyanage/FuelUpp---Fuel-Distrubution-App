package com.example.eadfrontend;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;

public class LoginActivity extends AppCompatActivity {
    ImageButton imageBtnClient;
    ImageButton imageBtnOwner;
    ImageButton imageButtonGoHome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        imageBtnClient = findViewById(R.id.imageButtonClient);
        imageBtnOwner = findViewById(R.id.imageButtonOwner);
        imageButtonGoHome = findViewById(R.id.imageButtonGoHome);

        //Navigate to client login
        imageBtnClient.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, ClientLoginActivity.class);
            LoginActivity.this.startActivity(intent);
        });

        //Navigate to shed owner login
        imageBtnOwner.setOnClickListener(v -> {
            Intent intent2 = new Intent(LoginActivity.this, ShedOwnerLoginActivity.class);
            LoginActivity.this.startActivity(intent2);
        });

        imageButtonGoHome.setOnClickListener(v -> {
            Intent intent3 = new Intent(LoginActivity.this, MainActivity.class);
            LoginActivity.this.startActivity(intent3);
        });
    }
}