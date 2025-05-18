package com.example.weatherapp;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;

import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
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
import androidx.appcompat.widget.SearchView;
import java.util.zip.Inflater;

public class MainActivity extends AppCompatActivity {
    private ImageView weatherIcon;
    private TextView temperature;
    private TextView cityName;
    private TextView weatherDescription;
    private TextView temperatureHighLow;
    private TextView feelsLike;
    private RequestQueue requestQueue;
    private SearchView searchCity;

    private TextView visibility;
    private TextView pressure;
    private TextView humidity;
    private TextView wind;
    private TextView sunRise;
    private TextView sunSet;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        temperature = findViewById(R.id.temperature);
        feelsLike = findViewById(R.id.weatherFeel);
        temperatureHighLow = findViewById(R.id.temperatureHighLow);
        weatherDescription = findViewById(R.id.weatherDescription);
        cityName = findViewById(R.id.city);
        requestQueue = Volley.newRequestQueue(this);
        visibility = findViewById(R.id.visibility);
        pressure = findViewById(R.id.pressure);
        humidity = findViewById(R.id.humidity);
        wind = findViewById(R.id.wind);
        sunRise = findViewById(R.id.sunRise);
        sunSet = findViewById(R.id.sunSet);
        fetchWeather("Kathmandu");
    }

    protected void fetchWeather(@Nullable String city){
        String API_KEY = getString(R.string.weather_api_key);
        String URL ="https://api.openweathermap.org/data/2.5/weather?q=" + city + "&appid="+ API_KEY +"&units=metric";

        JsonObjectRequest request =  new JsonObjectRequest
                (Request.Method.GET, URL, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("api",response.toString());

                        try {
                            JSONObject main = response.getJSONObject("main");
                            JSONObject coord = response.getJSONObject("coord");
                            JSONObject weather = response.getJSONArray("weather").getJSONObject(0);
                            JSONObject windData = response.getJSONObject("wind");
                            JSONObject sys = response.getJSONObject("sys");
                            double visib = response.getDouble("visibility");
                            double visibilityInKm = visib/1000.0;

                            double temp = main.getDouble("temp");
                            double feel = main.getDouble("feels_like");
                            double min = main.getDouble("temp_min");
                            double max = main.getDouble("temp_max");
                            double press = main.getDouble("pressure");
                            double humid = main.getDouble("humidity");
                            double winds = windData.getDouble("speed");
                            double sunR = sys.getDouble("sunrise");
                            double sunS = sys.getDouble("sunset");

                            String weatherDesc = weather.getString("description");

                            String ct = response.getString("name");

                            temperature.setText(String.format("%.1f°C",temp));
                            feelsLike.setText(String.format("Feels like %.0f°C" ,feel));
                            temperatureHighLow.setText(String.format("%.1f/ ", min) + String.format("%.1f°C", max));
                            weatherDescription.setText(weatherDesc);
                            visibility.setText(String.format("%.01f km", visibilityInKm));
                            pressure.setText(String.format("%.0f hPa",press));
                            humidity.setText(String.format("%.0f %%", humid));
                            wind.setText(String.format("%.01f m/s", winds));
                            sunRise.setText(convertUnixToTime(sunR));
                            sunSet.setText(convertUnixToTime(sunS));
                            cityName.setText(ct);
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }

                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(MainActivity.this, "Error Loading weather data", Toast.LENGTH_SHORT).show();
                    }

                });
        requestQueue.add(request);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.top_menu, menu);

        MenuItem searchItem = menu.findItem(R.id.search);

        SearchView searchCity = (SearchView) searchItem.getActionView();
        searchCity.setQueryHint("Search City");

        searchCity.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // Trigger search
                Toast.makeText(MainActivity.this, "Search for city: " + query, Toast.LENGTH_SHORT).show();
                fetchWeather(query);
                hideKeyboard(MainActivity.this);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                // Handle live changes if needed
                return false;
            }
        });
        return true;
    }

    public void hideKeyboard(Activity activity) {
        View view = activity.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    private String convertUnixToTime(double unixTime) {
        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("hh:mm a");
        sdf.setTimeZone(java.util.TimeZone.getDefault());
        return sdf.format(new java.util.Date((long)unixTime * 1000));
    }

}
