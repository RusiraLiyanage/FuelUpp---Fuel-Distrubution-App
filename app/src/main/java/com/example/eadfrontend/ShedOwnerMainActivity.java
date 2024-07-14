package com.example.eadfrontend;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
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

public class ShedOwnerMainActivity extends AppCompatActivity {
    Button buttonUpdateFuelStatus;
    Button buttonUpdateTime;
    Button buttonGoBack;
    TextView textViewShedName;
    TextView textViewQueueLength;
    TextView textViewFuelAvailability;
    TextView ownerFuelArrivalTime;
    TextView ownerFuelFinishTime;
    String ownerEmail;
    String shedName;
    String url_getByShedname = "http://192.168.8.159:5198/api/ShedOwners/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shed_owner_main);
        Intent intent = getIntent();
        ownerEmail = intent.getStringExtra("owneremail");
        textViewShedName = findViewById(R.id.textViewShedName);
        textViewQueueLength = findViewById(R.id.textViewQueueLength);
        textViewFuelAvailability = findViewById(R.id.textViewFuelAvailability);
        ownerFuelArrivalTime = findViewById(R.id.ownerFuelArrivalTime);
        ownerFuelFinishTime = findViewById(R.id.ownerFuelDepartTime);
        buttonUpdateFuelStatus = findViewById(R.id.buttonUpdateFuelStatus);
        buttonUpdateTime = findViewById(R.id.buttonUpdateTime);
        buttonGoBack = findViewById(R.id.buttonGoBack);
        RequestQueue queue = Volley.newRequestQueue(this);
        DBHelperOwner dbHelperOwner = new DBHelperOwner(this);
        shedName = dbHelperOwner.getUserShedName(ownerEmail);
        textViewShedName.setText(shedName.concat(" Fuel Station"));

        JsonObjectRequest jsonObjectRequestShedName = new JsonObjectRequest(Request.Method.GET, url_getByShedname.concat(shedName), null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    textViewQueueLength.setText(response.getString("queueLength"));
                    if(response.getString("fuelArrivalTime").equals("")){
                        ownerFuelArrivalTime.setText("-");
                    }else{
                        ownerFuelArrivalTime.setText(response.getString("fuelArrivalTime"));
                    }

                    if(response.getString("fuelFinishTime").equals("")){
                        ownerFuelFinishTime.setText("-");
                    }else{
                        ownerFuelFinishTime.setText(response.getString("fuelFinishTime"));
                    }

                    if(response.getString("fuelAvaliability").equals("Avaliable") || response.getString("fuelAvaliability").equals("AVAILABLE")){
                        textViewFuelAvailability.setText("AVAILABLE");
                        textViewFuelAvailability.setTextColor(Color.parseColor("#4CAF50"));

                    }else{
                        textViewFuelAvailability.setText("NOT AVAILABLE");
                        textViewFuelAvailability.setTextColor(Color.RED);

                    }
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

        buttonUpdateFuelStatus.setOnClickListener(v -> {
            Intent intent2 = new Intent(ShedOwnerMainActivity.this, ShedOwnerUpdateFuelStatusActivity.class);
            intent2.putExtra("shedname", shedName);
            intent2.putExtra("owneremail", ownerEmail);
            ShedOwnerMainActivity.this.startActivity(intent2);
        });

        buttonUpdateTime.setOnClickListener(v -> {
            Intent intent3 = new Intent(ShedOwnerMainActivity.this, ShedOwnerUpdateTimeActivity.class);
            intent3.putExtra("shedname", shedName);
            intent3.putExtra("owneremail", ownerEmail);
            ShedOwnerMainActivity.this.startActivity(intent3);
        });

        buttonGoBack.setOnClickListener(v -> {
            Intent intentGoback = new Intent(ShedOwnerMainActivity.this, LoginActivity.class);
            ShedOwnerMainActivity.this.startActivity(intentGoback);
        });
    }
}