package com.example.eadfrontend;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class ClientLoginActivity extends AppCompatActivity {
    TextView gotoRegisterBtn;
    EditText editTextClientEmail;
    EditText editTextClientPassword;
    Button btnGotoCreatePassword;
    Button btnClientLogin;
    ProgressBar progressBarClientLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_login);
        gotoRegisterBtn = findViewById(R.id.gotoRegisterButton);
        btnGotoCreatePassword = findViewById(R.id.button3);
        btnClientLogin = findViewById(R.id.button2);
        editTextClientEmail = findViewById(R.id.editTextTextPersonName);
        editTextClientPassword = findViewById(R.id.editTextTextPersonName2);
        progressBarClientLogin = findViewById(R.id.progressBarClientLogin);
        progressBarClientLogin.setVisibility(View.INVISIBLE);
        btnClientLogin.setVisibility(View.VISIBLE);

        //Navigate to register screen
        gotoRegisterBtn.setOnClickListener(v -> {
            Intent intent = new Intent(ClientLoginActivity.this, ClientRegisterActivity.class);
            ClientLoginActivity.this.startActivity(intent);
        });

        //Navigate to create new password screen
        btnGotoCreatePassword.setOnClickListener(v -> {
            Intent intent = new Intent(ClientLoginActivity.this, ClientCreatePasswordActivity.class);
            ClientLoginActivity.this.startActivity(intent);
        });

        //Validate login credentials
        btnClientLogin.setOnClickListener(v -> {
            btnClientLogin.setVisibility(View.INVISIBLE);
            progressBarClientLogin.setVisibility(View.VISIBLE);
            DBHelperClient dbHelperClient = new DBHelperClient(this);

            Boolean res = dbHelperClient.checkLoginHasPassword(editTextClientEmail.getText().toString());
            if(res){
                Boolean loginRes = dbHelperClient.validateLoginCredentials(editTextClientEmail.getText().toString(), editTextClientPassword.getText().toString());
                if(loginRes){
                    Intent intent = new Intent(ClientLoginActivity.this, ClientMainActivity.class);
                    intent.putExtra("useremail", editTextClientEmail.getText().toString());
                    ClientLoginActivity.this.startActivity(intent);
                    progressBarClientLogin.setVisibility(View.INVISIBLE);
                    btnClientLogin.setVisibility(View.VISIBLE);
                }else{
                    btnClientLogin.setVisibility(View.VISIBLE);
                    progressBarClientLogin.setVisibility(View.INVISIBLE);
                    Toast.makeText(getApplicationContext(), "Login Failed. Try Again.", Toast.LENGTH_SHORT).show();
                }
            }else{
                btnClientLogin.setVisibility(View.VISIBLE);
                progressBarClientLogin.setVisibility(View.INVISIBLE);
                Toast.makeText(getApplicationContext(), "Please enter valid details!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}