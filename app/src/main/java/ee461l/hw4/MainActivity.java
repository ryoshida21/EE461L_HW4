package ee461l.hw4;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.ImageRequest;
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
        }, null);
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
                    int min_temp = (int) object.getJSONObject("main").getDouble("temp_min");
                    int max_temp = (int) object.getJSONObject("main").getDouble("temp_max");
                    int humidity = object.getJSONObject("main").getInt("humidity");
                    int wind = (int) object.getJSONObject("wind").getDouble("speed");
                    String icon_url = "http://openweathermap.org/img/w/" + weather.getString("icon") + ".png";
                    TextView descriptionText = findViewById(R.id.description_text);
                    descriptionText.setText(description);
                    TextView tempText = findViewById(R.id.temp_text);
                    tempText.setText(String.format("Temperature: %d°", temp));
                    TextView tempMinMaxText = findViewById(R.id.temp_max_min_text);
                    tempMinMaxText.setText(String.format("Max: %d° Min: %d°", max_temp, min_temp));
                    TextView humidityText = findViewById(R.id.humidity_text);
                    humidityText.setText(String.format("Humidity: %d%%", humidity));
                    TextView windText = findViewById(R.id.wind_text);
                    windText.setText(String.format("Wind: %d mph", wind));
                    getWeatherIcon(icon_url);


                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, null);
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(request);

    }

    public void getWeatherIcon(String url) {
        final ImageView view = findViewById(R.id.weathericon);
        ImageRequest request = new ImageRequest(url, new Response.Listener<Bitmap>() {
            @Override
            public void onResponse(Bitmap response) {
                view.setImageBitmap(response);
            }
        }, 0, 0, ImageView.ScaleType.CENTER, null, null);
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(request);
    }

}
