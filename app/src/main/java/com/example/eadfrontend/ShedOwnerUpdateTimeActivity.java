package com.example.eadfrontend;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TimePicker;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class ShedOwnerUpdateTimeActivity extends AppCompatActivity {
    TimePicker arrivalTimePicker;
    TimePicker finishTimePicker;
    Button updateArrivalTimeBtn;
    Button updateFinishTimeBtn;
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
        setContentView(R.layout.activity_shed_owner_update_time);
        Intent intent = getIntent();
        shedname = intent.getStringExtra("shedname");
        ownerEmail = intent.getStringExtra("owneremail");
        arrivalTimePicker = findViewById(R.id.arrivalTimePicker);
        finishTimePicker = findViewById(R.id.finishTimePicker);
        updateArrivalTimeBtn = findViewById(R.id.btnUpdateFuelArrivalTime);
        updateFinishTimeBtn = findViewById(R.id.btnUpdateFinishTime);
        arrivalTimePicker.setIs24HourView(true);
        finishTimePicker.setIs24HourView(true);
        RequestQueue queue = Volley.newRequestQueue(this);
        jsonObjectShedData = new JSONObject();

        JsonObjectRequest jsonObjectRequestShedName = new JsonObjectRequest(Request.Method.GET, url_getByShedname.concat(shedname), null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    userid = response.getString("id");
                    ownerName = response.getString("ownerName");
                    shedname = response.getString("shedName");
                    shedLocation = response.getString("shedLocation");
                    fuelArrivalTime = response.getString("fuelArrivalTime");
                    fuelFinishTime = response.getString("fuelFinishTime");
                    queueLength = response.getString("queueLength");
                    fuelAvailability = response.getString("fuelAvaliability");

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


        updateArrivalTimeBtn.setOnClickListener(v -> {
            String arrivalTimeMinutes;

            if (arrivalTimePicker.getMinute() < 10){
                arrivalTimeMinutes = "0"+arrivalTimePicker.getMinute();
            }else{
                arrivalTimeMinutes = String.valueOf(arrivalTimePicker.getMinute());
            }

            fuelArrivalTime = arrivalTimePicker.getHour()+":"+arrivalTimeMinutes;
            try {
                jsonObjectShedData.put("id", userid);
                jsonObjectShedData.put("ownerName", ownerName);
                jsonObjectShedData.put("shedName", shedname);
                jsonObjectShedData.put("shedLocation", shedLocation);
                jsonObjectShedData.put("fuelArrivalTime", fuelArrivalTime);
                jsonObjectShedData.put("fuelFinishTime", fuelFinishTime);
                jsonObjectShedData.put("queueLength", Integer.parseInt(queueLength));
                jsonObjectShedData.put("fuelAvaliability", fuelAvailability);

                JsonObjectRequest jsonObjectRequestUpdateArrivalTime = new JsonObjectRequest(Request.Method.PUT, url_getByShedname.concat("updateFuelData/").concat(shedname), jsonObjectShedData, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Intent intent2 = new Intent(ShedOwnerUpdateTimeActivity.this, ShedOwnerMainActivity.class);
                        intent2.putExtra("owneremail", ownerEmail);
                        ShedOwnerUpdateTimeActivity.this.startActivity(intent2);
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });
                queue.add(jsonObjectRequestUpdateArrivalTime);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        });

        updateFinishTimeBtn.setOnClickListener(v -> {
            String finishTimeMinutes;

            if (finishTimePicker.getMinute() < 10){
                finishTimeMinutes = "0"+finishTimePicker.getMinute();
            }else{
                finishTimeMinutes = String.valueOf(finishTimePicker.getMinute());
            }

            fuelFinishTime = finishTimePicker.getHour()+":"+finishTimeMinutes;
            try {
                jsonObjectShedData.put("id", userid);
                jsonObjectShedData.put("ownerName", ownerName);
                jsonObjectShedData.put("shedName", shedname);
                jsonObjectShedData.put("shedLocation", shedLocation);
                jsonObjectShedData.put("fuelArrivalTime", fuelArrivalTime);
                jsonObjectShedData.put("fuelFinishTime", fuelFinishTime);
                jsonObjectShedData.put("queueLength", Integer.parseInt(queueLength));
                jsonObjectShedData.put("fuelAvaliability", fuelAvailability);

                JsonObjectRequest jsonObjectRequestUpdateArrivalTime = new JsonObjectRequest(Request.Method.PUT, url_getByShedname.concat("updateFuelData/").concat(shedname), jsonObjectShedData, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Intent intent2 = new Intent(ShedOwnerUpdateTimeActivity.this, ShedOwnerMainActivity.class);
                        intent2.putExtra("owneremail", ownerEmail);
                        ShedOwnerUpdateTimeActivity.this.startActivity(intent2);
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });
                queue.add(jsonObjectRequestUpdateArrivalTime);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        });
    }
}