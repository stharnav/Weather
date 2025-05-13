package com.example.weatherapp;

import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {
    private ImageView weatherIcon;
    private TextView temperature;
    private TextView city;
    private TextView weatherDescription;
    private TextView temperatureHighLow;
    private TextView feelsLike;
    private RequestQueue requestQueue;;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        requestQueue = Volley.newRequestQueue(this);
        fetchWeather("Kathmandu");
    }

    protected void fetchWeather(@Nullable String city){
        String API_KEY = "978b2627174542f035cb91ce61ed936a";
        String URL ="https://api.openweathermap.org/data/2.5/weather?q=" + city + "&appid="+ API_KEY +"&units=metric";

        JsonObjectRequest request =  new JsonObjectRequest
                (Request.Method.GET, URL, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("api",response.toString());

                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }

                });
        requestQueue.add(request);
    }
}
