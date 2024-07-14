package com.example.eadfrontend;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;

public class ClientCreatePasswordActivity extends AppCompatActivity {
    EditText editTextClientEmail;
    EditText editTextClientPassword;
    EditText editTextClientConfPassword;
    Button buttonClientCreatePassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_create_password);
        editTextClientEmail = findViewById(R.id.editTextClientEmail);
        editTextClientPassword = findViewById(R.id.editTextClientPassword);
        editTextClientConfPassword = findViewById(R.id.editTextClientConfPassword);
        buttonClientCreatePassword = findViewById(R.id.buttonClientCreatePassword);

        DBHelperClient dbHelperClient = new DBHelperClient(this);

        //Creates new password for the new user.
        buttonClientCreatePassword.setOnClickListener(v -> {
            Boolean res = dbHelperClient.createNewPassword(editTextClientEmail.getText().toString(), editTextClientPassword.getText().toString(), editTextClientConfPassword.getText().toString());
            if(res){
                Intent intent = new Intent(ClientCreatePasswordActivity.this, ClientLoginActivity.class);
                ClientCreatePasswordActivity.this.startActivity(intent);
            }
        });
    }
}