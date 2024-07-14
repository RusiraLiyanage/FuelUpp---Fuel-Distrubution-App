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
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;

public class ClientMainActivity extends AppCompatActivity {
    AutoCompleteTextView dropdownSelectPetrolStation;
    TextView textViewQueueLength;
    TextView textFuelAvailability;
    ScrollView scrollLayout;
    LinearLayout linearLayoutFillScreen;
    Button btnJoinTheQueue;
    Button btnExitBeforePump;
    Button btnExitAfterPump;
    Button buttonGoBack;
    ArrayList<String> shedNamesArray = new ArrayList<>();
    ArrayList<String> vehicleTypeArray = new ArrayList<>(Arrays.asList("Car", "Van", "Bike", "Three-wheeler"));
    ArrayList<String> lengthByVehicleType = new ArrayList<>();
    String userEmail;
    String selectedShed;
    String name;
    String arrivalTime;
    String vehicleType;
    String petrolShedName;
    String departTime;
    String exitTimeBeforePumping;
    String url = "http://192.168.8.159:5198/api/ShedOwners";
    String url_shedname = "http://192.168.8.159:5198/api/ShedOwners/";
    String url_joinQueue = "http://192.168.8.159:5198/api/Users";
    String url_exitWithoutPumping = "http://192.168.8.159:5198/api/Users/existingWithoutPumping/";
    String url_exitAfterPumping = "http://192.168.8.159:5198/api/Users/pumpingDone/";
    JSONObject userDataExitWithoutPumping;
    JSONObject userDataExitAfterPumping;
    RequestQueue queue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_main);
        Intent intent = getIntent();
        userEmail = intent.getStringExtra("useremail");
        dropdownSelectPetrolStation = findViewById(R.id.shed_dropdown);
        textViewQueueLength = findViewById(R.id.textViewQueueLength);
        textFuelAvailability = findViewById(R.id.textView14);
        scrollLayout = findViewById(R.id.scrollLayout);
        linearLayoutFillScreen = findViewById(R.id.linearLayoutFillScreen);
        btnJoinTheQueue = findViewById(R.id.button2);
        btnExitBeforePump = findViewById(R.id.buttonExitBeforePump);
        btnExitAfterPump = findViewById(R.id.buttonExitAfterPump);
        buttonGoBack = findViewById(R.id.buttonGoBack);
        scrollLayout.setVisibility(View.GONE);
        btnJoinTheQueue.setVisibility(View.VISIBLE);
        linearLayoutFillScreen.setVisibility(View.VISIBLE);
        btnExitBeforePump.setVisibility(View.GONE);
        btnExitAfterPump.setVisibility(View.GONE);
        queue = Volley.newRequestQueue(this);

        //Http GET request to get all shed names
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                for(int i = 0; i < response.length(); i++){
                    try {
                        shedNamesArray.add(response.getJSONObject(i).getString("shedName"));
                        ArrayAdapter arrayAdapter = new ArrayAdapter(getApplicationContext(), R.layout.dropdown_item, shedNamesArray);
                        dropdownSelectPetrolStation.setAdapter(arrayAdapter);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
        queue.add(request);

        //Retrieving queue length of selected petrol shed
        dropdownSelectPetrolStation.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                scrollLayout.setVisibility(View.VISIBLE);
                linearLayoutFillScreen.setVisibility(View.GONE);
                btnJoinTheQueue.setVisibility(View.VISIBLE);
                btnExitBeforePump.setVisibility(View.GONE);
                btnExitAfterPump.setVisibility(View.GONE);
                selectedShed = parent.getItemAtPosition(position).toString();

                getLoadData(selectedShed);

            }

        });



        //Button "Join to the Queue"
        btnJoinTheQueue.setOnClickListener(v -> {
            btnJoinTheQueue.setVisibility(View.GONE);
            btnExitBeforePump.setVisibility(View.VISIBLE);
            btnExitAfterPump.setVisibility(View.VISIBLE);


            DBHelperClient dbHelperClient = new DBHelperClient(this);
            name = dbHelperClient.getUserFullname(userEmail);
            vehicleType = dbHelperClient.getUserVehicleType(userEmail);
            petrolShedName = selectedShed;
            departTime = "null";
            exitTimeBeforePumping = "null";

            int hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
            int minute = Calendar.getInstance().get(Calendar.MINUTE);

            if(minute < 10){
                arrivalTime = hour+":0"+minute;
            }else{
                arrivalTime = hour+":"+minute;
            }

            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("name", name);
                jsonObject.put("arrivalTime", arrivalTime);
                jsonObject.put("vehicleType", vehicleType);
                jsonObject.put("petrolShedName", petrolShedName);
                jsonObject.put("departTime", departTime);
                jsonObject.put("exitTimeBeforePumping", exitTimeBeforePumping);

                JsonObjectRequest jsonObjectRequest2 = new JsonObjectRequest(Request.Method.POST, url_joinQueue, jsonObject, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        getLoadData(petrolShedName);
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
        });

        btnExitBeforePump.setOnClickListener(v -> {
            DBHelperClient dbHelperClient = new DBHelperClient(this);
            userDataExitWithoutPumping = new JSONObject();
            JsonArrayRequest requestUsers = new JsonArrayRequest(Request.Method.GET, url_joinQueue, null, new Response.Listener<JSONArray>() {
                @Override
                public void onResponse(JSONArray response) {
                    for(int i = 0; i < response.length(); i++){
                        try {
                            if(response.getJSONObject(i).getString("name").equals(dbHelperClient.getUserFullname(userEmail)) && response.getJSONObject(i).getString("petrolShedName").equals(selectedShed)){
                                int hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
                                int minute = Calendar.getInstance().get(Calendar.MINUTE);
                                if(minute < 10){
                                    exitTimeBeforePumping = hour+":0"+minute;
                                }else{
                                    exitTimeBeforePumping = hour+":"+minute;
                                }

                                try {
                                    userDataExitWithoutPumping.put("id", response.getJSONObject(i).getString("id"));
                                    userDataExitWithoutPumping.put("name", name);
                                    userDataExitWithoutPumping.put("arrivalTime", arrivalTime);
                                    userDataExitWithoutPumping.put("vehicleType", vehicleType);
                                    userDataExitWithoutPumping.put("petrolShedName", petrolShedName);
                                    userDataExitWithoutPumping.put("departTime", "null");
                                    userDataExitWithoutPumping.put("exitTimeBeforePumping", exitTimeBeforePumping);

                                    Log.d("objs", userDataExitWithoutPumping.toString());
                                    JsonObjectRequest requestExitWithoutPump = new JsonObjectRequest(Request.Method.PUT, url_exitWithoutPumping.concat(response.getJSONObject(i).getString("id")), userDataExitWithoutPumping, new Response.Listener<JSONObject>() {
                                        @Override
                                        public void onResponse(JSONObject response) {
                                            btnJoinTheQueue.setVisibility(View.VISIBLE);
                                            btnExitBeforePump.setVisibility(View.GONE);
                                            btnExitAfterPump.setVisibility(View.GONE);
                                            getLoadData(selectedShed);
                                        }
                                    }, new Response.ErrorListener() {
                                        @Override
                                        public void onErrorResponse(VolleyError error) {

                                        }
                                    });
                                    queue.add(requestExitWithoutPump);

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                break;
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    error.printStackTrace();
                }
            });
            queue.add(requestUsers);
        });


        btnExitAfterPump.setOnClickListener(v -> {
            DBHelperClient dbHelperClient = new DBHelperClient(this);
            userDataExitAfterPumping = new JSONObject();
            JsonArrayRequest requestUsers = new JsonArrayRequest(Request.Method.GET, url_joinQueue, null, new Response.Listener<JSONArray>() {
                @Override
                public void onResponse(JSONArray response) {
                    for(int i = 0; i < response.length(); i++){
                        try {
                            if(response.getJSONObject(i).getString("name").equals(dbHelperClient.getUserFullname(userEmail)) && response.getJSONObject(i).getString("petrolShedName").equals(selectedShed)){
                                int hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
                                int minute = Calendar.getInstance().get(Calendar.MINUTE);
                                if(minute < 10){
                                    departTime = hour+":0"+minute;
                                }else{
                                    departTime = hour+":"+minute;
                                }

                                try {
                                    userDataExitAfterPumping.put("id", response.getJSONObject(i).getString("id"));
                                    userDataExitAfterPumping.put("name", name);
                                    userDataExitAfterPumping.put("arrivalTime", arrivalTime);
                                    userDataExitAfterPumping.put("vehicleType", vehicleType);
                                    userDataExitAfterPumping.put("petrolShedName", petrolShedName);
                                    userDataExitAfterPumping.put("departTime", departTime);
                                    userDataExitAfterPumping.put("exitTimeBeforePumping", "null");

                                    JsonObjectRequest requestExitAfterPump = new JsonObjectRequest(Request.Method.PUT, url_exitAfterPumping.concat(response.getJSONObject(i).getString("id")), userDataExitAfterPumping, new Response.Listener<JSONObject>() {
                                        @Override
                                        public void onResponse(JSONObject response) {
                                            btnJoinTheQueue.setVisibility(View.VISIBLE);
                                            btnExitBeforePump.setVisibility(View.GONE);
                                            btnExitAfterPump.setVisibility(View.GONE);
                                            getLoadData(selectedShed);
                                        }
                                    }, new Response.ErrorListener() {
                                        @Override
                                        public void onErrorResponse(VolleyError error) {

                                        }
                                    });
                                    queue.add(requestExitAfterPump);

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                break;
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    error.printStackTrace();
                }
            });
            queue.add(requestUsers);
        });

        buttonGoBack.setOnClickListener(v -> {
            Intent intentGoBack = new Intent(ClientMainActivity.this, LoginActivity.class);
            ClientMainActivity.this.startActivity(intentGoBack);
        });
    }

    public void getLoadData(String selectedShed){
        //http GET request to obtain queue length
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url_shedname.concat(selectedShed), null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    textViewQueueLength.setText(response.getString("queueLength"));
                    if(response.getString("fuelAvaliability").equals("Avaliable") || response.getString("fuelAvaliability").equals("AVAILABLE")){
                        textFuelAvailability.setText("AVAILABLE");
                        textFuelAvailability.setTextColor(Color.parseColor("#4CAF50"));
                        btnJoinTheQueue.setEnabled(true);
                        btnJoinTheQueue.setBackgroundColor(Color.parseColor("#4CAF50"));
                    }else{
                        textFuelAvailability.setText("NOT AVAILABLE");
                        textFuelAvailability.setTextColor(Color.RED);
                        btnJoinTheQueue.setEnabled(false);
                        btnJoinTheQueue.setBackgroundColor(Color.GRAY);
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
        queue.add(jsonObjectRequest);

    }

}