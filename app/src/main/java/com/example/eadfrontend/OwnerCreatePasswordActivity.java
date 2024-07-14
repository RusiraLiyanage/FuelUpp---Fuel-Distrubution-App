package com.example.eadfrontend;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

public class OwnerCreatePasswordActivity extends AppCompatActivity {
    EditText editTextOwnerEmail;
    EditText editTextOwnerPassword;
    EditText editTextOwnerConfPassword;
    Button buttonOwnerCreatePassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_owner_create_password);
        editTextOwnerEmail = findViewById(R.id.editTextOwnerEmail);
        editTextOwnerPassword = findViewById(R.id.editTextOwnerPassword);
        editTextOwnerConfPassword = findViewById(R.id.editTextOwnerConfPassword);
        buttonOwnerCreatePassword = findViewById(R.id.buttonOwnerCreatePassword);

        DBHelperOwner dbHelperOwner = new DBHelperOwner(this);

        //Creates new password - shed owner
        buttonOwnerCreatePassword.setOnClickListener(v -> {
            Boolean res = dbHelperOwner.createNewPassword(editTextOwnerEmail.getText().toString(), editTextOwnerPassword.getText().toString(), editTextOwnerConfPassword.getText().toString());
            if(res){
                Intent intent = new Intent(OwnerCreatePasswordActivity.this, ShedOwnerLoginActivity.class);
                OwnerCreatePasswordActivity.this.startActivity(intent);
            }
        });
    }
}