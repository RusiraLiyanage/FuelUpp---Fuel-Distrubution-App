package com.example.eadfrontend;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;

public class ClientRegisterActivity extends AppCompatActivity {
    EditText editFullName;
    EditText editUserName;
    EditText editEmail;
    EditText editMobileNo;
    AutoCompleteTextView dropdownGender;
    AutoCompleteTextView dropdownVehicleType;
    Button registerBtnClient;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_register);
        dropdownGender = findViewById(R.id.gender_dropdown);
        dropdownVehicleType = findViewById(R.id.vehicletype_dropdown);
        editFullName = findViewById(R.id.editTextTextPersonName3);
        editUserName = findViewById(R.id.editTextTextPersonName4);
        editEmail = findViewById(R.id.editTextTextPersonName5);
        editMobileNo = findViewById(R.id.editTextTextPersonName7);
        registerBtnClient = findViewById(R.id.button4);

        String[] genderArray = getResources().getStringArray(R.array.gender);
        ArrayAdapter arrayAdapter = new ArrayAdapter(getApplicationContext(), R.layout.dropdown_item, genderArray);
        dropdownGender.setAdapter(arrayAdapter);

        String[] vehicleTypeArray = getResources().getStringArray(R.array.vehicleTypes);
        ArrayAdapter arrayAdapter2 = new ArrayAdapter(getApplicationContext(), R.layout.dropdown_item, vehicleTypeArray);
        dropdownVehicleType.setAdapter(arrayAdapter2);

        //Register new user
        registerBtnClient.setOnClickListener(v -> {
            DBHelperClient dbHelperClient = new DBHelperClient(this);
            Boolean res = dbHelperClient.insertData(
                    editFullName.getText().toString(),
                    editUserName.getText().toString(),
                    editEmail.getText().toString(),
                    dropdownGender.getText().toString(),
                    editMobileNo.getText().toString(),
                    dropdownVehicleType.getText().toString()
            );
            if(res){
                Intent intent = new Intent(ClientRegisterActivity.this, ClientLoginActivity.class);
                ClientRegisterActivity.this.startActivity(intent);
            }else{
                editFullName.setText("");
                editUserName.setText("");
                editEmail.setText("");
                dropdownGender.setText("");
                editMobileNo.setText("");
                dropdownVehicleType.setText("");
            }
        });
    }
}