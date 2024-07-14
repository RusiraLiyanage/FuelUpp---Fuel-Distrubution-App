package com.example.eadfrontend;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class ShedOwnerUpdateFuelStatusActivity extends AppCompatActivity {
    AutoCompleteTextView fuelStatusDropdown;
    TextView textViewShedName;
    TextView textViewFuelStatus;
    Button btnUpdateFuelStatus;
    String userid;
    String shedname;
    String ownerName;
    String ownerEmail;
    String shedLocation;
    String fuelArrivalTime;
    String fuelFinishTime;
    String queueLength;
    String fuelAvailability;
    String url_getByShedname = "http://192.168.8.159:5198/api/ShedOwners/";
    JSONObject jsonObjectShedData;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shed_owner_update_fuel_status);
        Intent intent = getIntent();
        shedname = intent.getStringExtra("shedname");
        ownerEmail = intent.getStringExtra("owneremail");
        textViewShedName = findViewById(R.id.textViewShedName);
        textViewFuelStatus = findViewById(R.id.textView14);
        btnUpdateFuelStatus = findViewById(R.id.btnUpdateFuelStatus);
        fuelStatusDropdown = findViewById(R.id.availability_dropdown);
        textViewShedName.setText(shedname);
        String[] fuelStatusArray = getResources().getStringArray(R.array.availability);
        ArrayAdapter arrayAdapter = new ArrayAdapter(getApplicationContext(), R.layout.dropdown_item, fuelStatusArray);
        fuelStatusDropdown.setAdapter(arrayAdapter);
        jsonObjectShedData = new JSONObject();

        RequestQueue queue = Volley.newRequestQueue(this);

        //GET request to get all shed names
        JsonObjectRequest jsonObjectRequestShedName = new JsonObjectRequest(Request.Method.GET, url_getByShedname.concat(shedname), null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    if(response.getString("fuelAvaliability").equals("Avaliable") || response.getString("fuelAvaliability").equals("AVAILABLE")){
                        textViewFuelStatus.setText("AVAILABLE");
                        textViewFuelStatus.setTextColor(Color.parseColor("#4CAF50"));

                    }else{
                        textViewFuelStatus.setText("NOT AVAILABLE");
                        textViewFuelStatus.setTextColor(Color.RED);
                    }
                    userid = response.getString("id");
                    ownerName = response.getString("ownerName");
                    shedname = response.getString("shedName");
                    shedLocation = response.getString("shedLocation");
                    fuelArrivalTime = response.getString("fuelArrivalTime");
                    fuelFinishTime = response.getString("fuelFinishTime");
                    queueLength = response.getString("queueLength");
                    fuelAvailability = fuelStatusDropdown.getText().toString();

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        queue.add(jsonObjectRequestShedName);

        //fuel status dropdown to select fuel status
        fuelStatusDropdown.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String selectedAvailability = parent.getItemAtPosition(position).toString();
                try {
                    jsonObjectShedData.put("id", userid);
                    jsonObjectShedData.put("ownerName", ownerName);
                    jsonObjectShedData.put("shedName", shedname);
                    jsonObjectShedData.put("shedLocation", shedLocation);
                    jsonObjectShedData.put("fuelArrivalTime", fuelArrivalTime);
                    jsonObjectShedData.put("fuelFinishTime", "");
                    jsonObjectShedData.put("queueLength", Integer.parseInt(queueLength));
                    jsonObjectShedData.put("fuelAvaliability", selectedAvailability);

                    Log.d("wefwefwef", jsonObjectShedData.toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        });

        //Button to update fuel status
        btnUpdateFuelStatus.setOnClickListener(v -> {
            Log.d("wefwefwef", jsonObjectShedData.toString());
            JsonObjectRequest jsonObjectRequestUpdateStatus = new JsonObjectRequest(Request.Method.PUT, url_getByShedname.concat("updateFuelData/").concat(shedname), jsonObjectShedData, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    Intent intent2 = new Intent(ShedOwnerUpdateFuelStatusActivity.this, ShedOwnerMainActivity.class);
                    intent2.putExtra("owneremail", ownerEmail);
                    ShedOwnerUpdateFuelStatusActivity.this.startActivity(intent2);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                }
            });
            queue.add(jsonObjectRequestUpdateStatus);

        });



    }
}