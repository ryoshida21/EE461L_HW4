package ee461l.hw4;

import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private LatLng m;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        Intent intent = getIntent();
        double lat = intent.getDoubleExtra(MainActivity.EXTRA_LAT, 0);
        double lng = intent.getDoubleExtra(MainActivity.EXTRA_LNG, 0);

        LatLng marker = new LatLng(lat, lng);
        m = marker;
        String title = "Coordinates";
        String snippet = "(" + lat + "°, " + lng + "°)";
        mMap.addMarker(new MarkerOptions().position(marker).title(title).snippet(snippet));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(marker, 15.0f));


    }

    public void recenter(View view) {
        //mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(m, 15.0f));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(m, 15.0f));
    }
}
