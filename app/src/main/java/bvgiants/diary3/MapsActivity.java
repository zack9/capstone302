package bvgiants.diary3;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by Alex on 10/05/2016.
 */
public class MapsActivity extends FragmentActivity implements GoogleApiClient.ConnectionCallbacks,
        OnMapReadyCallback, GoogleApiClient.OnConnectionFailedListener {

    GoogleApiClient mGoogleApiClient;
    Location mLastLocation;

    Context mContext;

    private static GoogleMap mMap;
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        startBackgroundProcess(this.findViewById(android.R.id.content), mContext);

        if(mGoogleApiClient!= null){
            mGoogleApiClient.connect();
        }

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    public void startBackgroundProcess(View view, Context c){
        startService(new Intent(getBaseContext(), BackgroundService.class));
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMyLocationEnabled(true);
        Log.e("Google Maps", "Maps is ready for use");
        // Render Pins
        checkRunPins();
        checkFoodPins();

        // Zoom the map into Brisbane
        LatLng startLatLng = new LatLng(-27.4769, 153.0270);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(startLatLng));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(11));

    } // End onMapReady

    private void checkRunPins(){
        // Access the Run Pins
        SharedPreferences mapReferences = getSharedPreferences("DropPins", MODE_PRIVATE);
        int locationCount = mapReferences.getInt("locationCount", 1);
        // Drop run pins if there are any
        if (locationCount > 0) {
            dropRunPins();
        } else {
            Log.e("Google Maps", "No RUN Pins to drop");
        }
    } // End checkRunPins

    private void checkFoodPins(){
        // Access the food pins
        SharedPreferences mapReferences = getSharedPreferences("FoodPins", MODE_PRIVATE);
        int foodLocationCount = mapReferences.getInt("foodLocationCount", 1);
        // Drop food pins if there are any
        if (foodLocationCount > 0) {
            dropFoodPins();
        } else {
            Log.e("Google Maps", "No FOOD Pins to drop");
        }
    } // End checkFoodPins


    private void dropRunPins(){
        Log.e("Google Maps", "-----------------------------< START RUN PINS >------------------------------");
        // Access the Run Pins
        SharedPreferences mapReferences = getSharedPreferences("DropPins", MODE_PRIVATE);
        int locationCount = mapReferences.getInt("locationCount", 1);
        Log.e("Google Maps", "Found a Total of " + locationCount + " recorded RUN locations");

        // Loop through all the Run Pins
        for (int i=0; i<locationCount; i++){
            String lat = mapReferences.getString("lat"+i, "No Lat Recorded");
            String lng = mapReferences.getString("lng"+i, "No Long Recorded");
            LatLng loc = new LatLng(Double.parseDouble(lat), Double.parseDouble(lng));
            int numSteps = mapReferences.getInt("steps"+i, 0);

            // Ensure first pin drops, so the next pin can compare it
            if (i == 0) {
                Log.e("Google Maps", "---");
                Log.e("Google Maps", "Run Pin No: " + i);
                Log.e("Google Maps", "Dropped Pin " + i + " at " + loc);
                Marker mMarker = mMap.addMarker(new MarkerOptions().position(loc).title("Number of Steps: "+ numSteps));
            }

            // Loop through the rest of the pins
            if (i > 0) {
                String oldlatitude = mapReferences.getString("lat"+(i-1), "No Lat Recorded");
                String oldlongitude = mapReferences.getString("lng"+(i-1), "No Long Recorded");
                Double oldlat = Double.parseDouble(oldlatitude);
                Double oldlng = Double.parseDouble(oldlongitude);

                // Find the distance difference between the new point and the previous point
                Double distanceDif = distance(Double.parseDouble(lat), Double.parseDouble(lng), oldlat, oldlng);

                // If the distance has not changed by more than 100m do nothing, otherwise place a pin
                if (distanceDif < .10){
                    Log.e("Google Maps", "---");
                    Log.e("Google Maps", "Run Pin No: " + i);
                    Log.e("Google Maps", "Cannot Drop Pin - Under 100m - " + loc);
                } else {
                    // Place a Pin
                    Log.e("Google Maps", "---");
                    Log.e("Google Maps", "Run Pin No: " + i);
                    Log.e("Google Maps", "Dropped Pin " + i + " at " + loc + " | Which is " + distanceDif + " since the previous pin" );
                    Marker mMarker = mMap.addMarker(new MarkerOptions().position(loc).title("Number of Steps: "+ numSteps));
                }
            } // End Loop Through Rest
        } // End Loop All Locations
        Log.e("Google Maps", "-----------------------------< END RUN PINS >------------------------------");
    } // End dropRunPins

    private void dropFoodPins(){
        Log.e("Google Maps", "-----------------------------< START DROPPING FOOD PINS >------------------------------");
        // Access Food Pins
        SharedPreferences mapReferences = getSharedPreferences("FoodPins", MODE_PRIVATE);
        int foodLocationCount = mapReferences.getInt("foodLocationCount", 0);
        Log.e("Google Maps", "Found a total of " + foodLocationCount + " recorded FOOD locations");
        // Loop through each location and place a pin
        for (int i=0; i<foodLocationCount; i++){
            String lat = mapReferences.getString("lat"+i, "No Lat Recorded"); // Second string is a placeholder if no value is found
            String lng = mapReferences.getString("lng"+i, "No Long Recorded");
            LatLng loc = new LatLng(Double.parseDouble(lat), Double.parseDouble(lng));
            Set<String> foodEaten = mapReferences.getStringSet("foods"+i, new HashSet<String>());

            Log.e("Google Maps", "---");
            Log.e("Google Maps", "Food Pin No: " + i);
            Log.e("Google Maps", "Dropped Pin "+ i +" at " + loc);
            Marker mMarker = mMap.addMarker(new MarkerOptions().position(loc).title("Food Eaten Here: " + foodEaten).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
        }
        Log.e("Google Maps", "-----------------------------< END DROPPING FOOD PINS >------------------------------");
    } // End dropFoodPins

    // Calculate the distance between two 'locs' (locations)
    private double distance(double lat1, double lon1, double lat2, double lon2) {
        double theta = lon1 - lon2;
        double dist = Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2)) + Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) * Math.cos(deg2rad(theta));
        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist = dist * 60 * 1.1515;
        return (dist);
    } // End Distance

    private double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }
    private double rad2deg(double rad) {
        return (rad * 180.0 / Math.PI);
    }

    @Override
    public void onStart() {
        super.onStart();
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Maps Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://bvgiants.diary3/http/host/path")
        );
        AppIndex.AppIndexApi.start(client, viewAction);
        Log.e("Google Maps", "Map has started");
    } // End onStart

    @Override
    public void onStop() {
        super.onStop();
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Maps Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://bvgiants.diary3/http/host/path")
        );
        AppIndex.AppIndexApi.end(client, viewAction);
        client.disconnect();
    } // End onStop

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Log.e("Google Maps", "Map has connected!");
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.e("Google Maps", "Connection Failed!");
    }
}
