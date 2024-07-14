package com.example.eadfrontend;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class ShedOwnerLoginActivity extends AppCompatActivity {
    TextView btnGotoRegister;
    EditText editTextOwnerEmail;
    EditText editTextOwnerPassword;
    Button buttonOwnerGotoCreatePassword;
    Button buttonOwnerLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shed_owner_login);

        btnGotoRegister = findViewById(R.id.textView6);
        buttonOwnerGotoCreatePassword = findViewById(R.id.buttonOwnerGotoCreatePassword);
        buttonOwnerLogin = findViewById(R.id.buttonOwnerLogin);
        editTextOwnerEmail = findViewById(R.id.editTextOwnerEmail);
        editTextOwnerPassword = findViewById(R.id.editTextOwnerPassword);

        //Navigate to register
        btnGotoRegister.setOnClickListener(v -> {
            Intent intent = new Intent(ShedOwnerLoginActivity.this, ShedOwnerRegisterActivity.class);
            ShedOwnerLoginActivity.this.startActivity(intent);
        });

        //Navigate to create password
        buttonOwnerGotoCreatePassword.setOnClickListener(v -> {
            Intent intent = new Intent(ShedOwnerLoginActivity.this, OwnerCreatePasswordActivity.class);
            ShedOwnerLoginActivity.this.startActivity(intent);
        });

        //Validate login - shed owner
        buttonOwnerLogin.setOnClickListener(v -> {
            DBHelperOwner dbHelperOwner = new DBHelperOwner(this);
            Boolean res = dbHelperOwner.checkLoginHasPassword(editTextOwnerEmail.getText().toString());
            if(res){
                Boolean loginRes = dbHelperOwner.validateLoginCredentials(editTextOwnerEmail.getText().toString(), editTextOwnerPassword.getText().toString());
                if(loginRes){
                    Intent intent = new Intent(ShedOwnerLoginActivity.this, ShedOwnerMainActivity.class);
                    intent.putExtra("owneremail", editTextOwnerEmail.getText().toString());
                    ShedOwnerLoginActivity.this.startActivity(intent);
                }
            }
        });
    }
}