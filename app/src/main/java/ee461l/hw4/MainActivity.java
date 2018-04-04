package ee461l.hw4;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    public final static String EXTRA_LAT = "com.example.ee461l.hw4.LAT";
    public final static String EXTRA_LNG = "com.example.ee461l.hw4.LNG";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }

    public void buttonClicked(View view) {
        EditText editText = findViewById(R.id.edit_message);
        String location = editText.getText().toString();
        location = location.replaceAll("\\s+","+");
        if (view.getId() == R.id.weatherbutton) {
            handleRequest(location, true);
        } else {
            handleRequest(location, false);
        }
    }


    public void handleRequest(String location, final boolean showWeather) {
        String map_url = "https://maps.googleapis.com/maps/api/geocode/json?address=" + location + "&key=" + getString(R.string.google_maps_key);
        StringRequest stringRequest = new StringRequest(map_url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    Log.d("MAP", "Inside onResponse");
                    JSONObject jsonObject = new JSONObject(response);
                    JSONObject obj = jsonObject.getJSONArray("results").getJSONObject(0);
                    JSONObject location = obj.getJSONObject("geometry").getJSONObject("location");
                    double lat = location.getDouble("lat");
                    double lng = location.getDouble("lng");
                    if (showWeather) {
                        handleWeatherRequest(lat, lng);
                    } else {
                        Intent intent = new Intent(getBaseContext(), MapsActivity.class);
                        intent.putExtra(EXTRA_LAT, lat);
                        intent.putExtra(EXTRA_LNG, lng);
                        startActivity(intent);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("ERROR", "Error");
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);
    }

    public void handleWeatherRequest(double lat, double lng) {
        String weather_url = "https://api.openweathermap.org/data/2.5/weather?lat=" + lat + "&lon=" + lng + "&units=imperial&APPID=49988a2708ff0db78f28c4ad51158beb";
        StringRequest request = new StringRequest(weather_url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject object = new JSONObject(response);
                    JSONObject weather = object.getJSONArray("weather").getJSONObject(0);
                    String description = weather.getString("description");

                    int temp = (int) object.getJSONObject("main").getDouble("temp");
                    int humidity = object.getJSONObject("main").getInt("humidity");
                    int wind = (int) object.getJSONObject("wind").getDouble("speed");
                    TextView descriptionText = findViewById(R.id.description_text);
                    descriptionText.setText(description);
                    TextView tempText = findViewById(R.id.temp_text);
                    tempText.setText(String.format("Temperature: %d°", temp));
                    TextView humidityText = findViewById(R.id.humidity_text);
                    humidityText.setText(String.format("Humidity: %d%%", humidity));
                    TextView windText = findViewById(R.id.wind_text);
                    windText.setText(String.format("Wind: %d mph", wind));

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("ERROR", "Error");
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(request);

    }

}
