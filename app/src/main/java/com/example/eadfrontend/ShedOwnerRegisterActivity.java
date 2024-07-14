package com.example.eadfrontend;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class ShedOwnerRegisterActivity extends AppCompatActivity {
    AutoCompleteTextView dropdownGender;
    EditText ownerFullName;
    EditText ownerUsername;
    EditText ownerEmail;
    EditText ownerMobileNo;
    EditText ownerShedLocationName;
    Button btnOwnerRegister;
    String url_shedowner = "http://192.168.8.159:5198/api/ShedOwners";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shed_owner_register);

        ownerFullName = findViewById(R.id.editTextTextPersonName3);
        ownerUsername = findViewById(R.id.editTextTextPersonName4);
        ownerEmail = findViewById(R.id.editTextTextPersonName5);
        dropdownGender = findViewById(R.id.gender_dropdown_owner);
        ownerMobileNo = findViewById(R.id.editTextTextPersonName7);
        ownerShedLocationName = findViewById(R.id.editTextTextPersonName8);
        btnOwnerRegister = findViewById(R.id.buttonOwnerRegister);
        RequestQueue queue = Volley.newRequestQueue(this);

        //Dropdown to select gender value
        String[] genderArray = getResources().getStringArray(R.array.gender);
        ArrayAdapter arrayAdapter = new ArrayAdapter(getApplicationContext(), R.layout.dropdown_item, genderArray);
        dropdownGender.setAdapter(arrayAdapter);

        //Button to register owner in SQLlite db
        btnOwnerRegister.setOnClickListener(v -> {
            DBHelperOwner dbHelperOwner = new DBHelperOwner(this);
            Boolean res = dbHelperOwner.insertData(
                    ownerFullName.getText().toString(),
                    ownerUsername.getText().toString(),
                    ownerEmail.getText().toString(),
                    dropdownGender.getText().toString(),
                    ownerMobileNo.getText().toString(),
                    ownerShedLocationName.getText().toString()
            );
            if(res){
                String ownerName = ownerFullName.getText().toString();
                String shedName = ownerShedLocationName.getText().toString();
                String shedLocation = ownerShedLocationName.getText().toString();
                String fuelArrivalTime = "";
                String fuelFinishTime = "";
                int queueLength = 0;
                String fuelAvailability = "NOT AVAILABLE";

                JSONObject jsonObject2 = new JSONObject();
                try {
                    jsonObject2.put("ownerName", ownerName);
                    jsonObject2.put("shedName", shedName);
                    jsonObject2.put("shedLocation", shedLocation);
                    jsonObject2.put("fuelArrivalTime", fuelArrivalTime);
                    jsonObject2.put("fuelFinishTime", fuelFinishTime);
                    jsonObject2.put("queueLength", queueLength);
                    jsonObject2.put("fuelAvaliability", fuelAvailability);

                    JsonObjectRequest jsonObjectRequest2 = new JsonObjectRequest(Request.Method.POST, url_shedowner, jsonObject2, new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            Intent intent = new Intent(ShedOwnerRegisterActivity.this, ShedOwnerLoginActivity.class);
                            ShedOwnerRegisterActivity.this.startActivity(intent);
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {

                        }
                    });
                    queue.add(jsonObjectRequest2);
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }else{
                ownerFullName.setText("");
                ownerUsername.setText("");
                ownerEmail.setText("");
                dropdownGender.setText("");
                ownerMobileNo.setText("");
                ownerShedLocationName.setText("");
            }
        });
    }
}