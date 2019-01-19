package com.example.ecko.spots;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.ecko.spots.model.DirectionsParser;
import com.example.ecko.spots.model.ParquesManager;
import com.example.ecko.spots.model.Spot;
import com.example.ecko.spots.model.SpotsManager;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.google.maps.android.SphericalUtil.computeDistanceBetween;

public class NavigationActivity extends FragmentActivity {

    private LocationManager locationManager;
    private LocationListener locationListener;
    private GoogleMap mMap;
    private Marker currentPositionMarker = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);

        final double myLat = getIntent().getDoubleExtra("MYLAT", 0);
        final double myLong = getIntent().getDoubleExtra("MYLONG", 0);
        final double destLat = getIntent().getDoubleExtra("DESTLAT", 0);
        final double destLong = getIntent().getDoubleExtra("DESTLONG", 0);
        final String nomeParque = getIntent().getStringExtra("PARQUE");

        final Spot spot = SpotsManager.INSTANCE.getSpotByCoords(destLat, destLong, nomeParque);
        final int pos = SpotsManager.INSTANCE.getPosicaoBySpot(nomeParque, destLat, destLong);

        locationManager = (LocationManager) this.getSystemService(LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                double latitude = location.getLatitude();
                double longitude = location.getLongitude();
                LatLng markerCurrent = new LatLng(latitude, longitude);
                LatLng markerDest = new LatLng(destLat, destLong);

                currentPositionMarker.setPosition(markerCurrent);
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(markerCurrent, 18));

                int dist = (int) computeDistanceBetween(markerCurrent, markerDest); //Nao ta a funcionar
                //Toast.makeText(getApplicationContext(), ""+dist, Toast.LENGTH_SHORT).show();

                if (dist < 20){
                    AlertDialog.Builder builder = new AlertDialog.Builder(NavigationActivity.this);
                    builder.setMessage("Do you want to add this Spot to MySpot?")
                            .setCancelable(false)
                            .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    locationManager.removeUpdates(locationListener);
                                    spot.setEstado(0);
                                    startActivity(AuthenticatedDashboardActivity.getIntent(NavigationActivity.this, 6));
                                    finish();
                                }
                            })
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    locationManager.removeUpdates(locationListener);
                                    FirebaseUser user =  FirebaseAuth.getInstance().getCurrentUser();
                                    FirebaseDatabase.getInstance().getReference("Spots").child(nomeParque).child(pos+"").child("ocupado").setValue(true);
                                    FirebaseDatabase.getInstance().getReference("Spots").child(nomeParque).child(pos+"").child("estado").setValue(2);
                                    FirebaseDatabase.getInstance().getReference("Users").child(user.getUid()).child("MySpot").child(nomeParque + " " +pos).setValue(spot);
                                    startActivity(MySpotActivity.getIntent(NavigationActivity.this, nomeParque,-1));//alterar find my spot
                                    finish();
                                }
                            });
                    builder.show();
                }
            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {

            }

            @Override
            public void onProviderEnabled(String s) {

            }

            @Override
            public void onProviderDisabled(String s) {

            }
        };

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        }else{
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
        }


        //Cancel
        Button btnCancelNav = findViewById(R.id.btnCancelNav);
        btnCancelNav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                spot.setEstado(0);
                locationManager.removeUpdates(locationListener);
                startActivity(AuthenticatedDashboardActivity.getIntent(NavigationActivity.this, 6));
                finish();
            }
        });


        SupportMapFragment mapFragment1 = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.navigation);
        mapFragment1.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                mMap = googleMap;

                LatLng markerStart = new LatLng(myLat, myLong);
                currentPositionMarker = mMap.addMarker(new MarkerOptions().position(markerStart).title("You are here!!"));

                LatLng markerDest = new LatLng(destLat, destLong);
                mMap.addMarker(new MarkerOptions().position(markerDest).title("Destination"));

                int dist = (int) computeDistanceBetween(markerStart, markerDest);
                Toast.makeText(getApplicationContext(), ""+dist+" metros", Toast.LENGTH_SHORT).show();

                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(markerStart,18));
                mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);

                String url = getDirectionsUrl(markerStart, markerDest);
                DownloadTask downloadTask = new DownloadTask();
                downloadTask.execute(url);
            }
        });
    }


    public static Intent getIntent(Context context, double myLat, double myLong, double destLat, double destLong, String nomeParque) {
        Intent intent = new Intent(context, NavigationActivity.class);
        intent.putExtra("MYLAT",myLat);
        intent.putExtra("MYLONG",myLong);
        intent.putExtra("DESTLAT",destLat);
        intent.putExtra("DESTLONG",destLong);
        intent.putExtra("PARQUE", nomeParque);
        return intent;
    }


    private class DownloadTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {

            String responseString = "";
            try {
                responseString = requestDirections(strings[0]);
            } catch (Exception e) {
                Log.d("Background Task", e.toString());
            }
            return responseString;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            TaskParser taskParser = new TaskParser();
            taskParser.execute(result);
        }
    }


    private class TaskParser extends AsyncTask<String, Void, List<List<HashMap<String, String>>>> {
        // Parsing the data in non-ui thread
        @Override
        protected List<List<HashMap<String, String>>> doInBackground(String... strings) {

            JSONObject jObject;
            List<List<HashMap<String, String>>> routes = null;

            try {
                jObject = new JSONObject(strings[0]);
                DirectionsParser parser = new DirectionsParser();

                routes = parser.parse(jObject);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return routes;
        }

        @Override
        protected void onPostExecute(List<List<HashMap<String, String>>> lists) {
            ArrayList points = null;
            PolylineOptions polylineOptions = null;
            MarkerOptions markerOptions = new MarkerOptions();

            for (List<HashMap<String, String>> path : lists) {
                points = new ArrayList();
                polylineOptions = new PolylineOptions();

                for (HashMap<String, String> point : path) {

                    double lat = Double.parseDouble(point.get("lat"));
                    double lng = Double.parseDouble(point.get("lng"));
                    LatLng position = new LatLng(lat, lng);

                    points.add(position);
                }

                polylineOptions.addAll(points);
                polylineOptions.width(12);
                polylineOptions.color(Color.RED);
                polylineOptions.geodesic(true);

            }

            // Drawing polyline in the Google Map for the i-th route
            mMap.addPolyline(polylineOptions);
        }
    }


    private String getDirectionsUrl(LatLng origin, LatLng dest) {
        // Origin of route
        String str_origin = "origin=" + origin.latitude + "," + origin.longitude;

        // Destination of route
        String str_dest = "destination=" + dest.latitude + "," + dest.longitude;

        // Sensor enabled
        String sensor = "sensor=false";
        String mode = "mode=driving";

        // Building the parameters to the web service
        String parameters = str_origin + "&" + str_dest + "&" + sensor + "&" + mode;

        // Output format
        String output = "json";

        // Building the url to the web service
        String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters + "&key=AIzaSyDMs5nmY0XmfgTbA52hwxXDiv1CaM0XOCk";

        return url;
    }


    private String requestDirections(String reqUrl) throws IOException{
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try {
            URL url = new URL(reqUrl);

            urlConnection = (HttpURLConnection) url.openConnection();

            urlConnection.connect();

            iStream = urlConnection.getInputStream();

            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));

            StringBuffer sb = new StringBuffer();

            String line = "";
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }

            data = sb.toString();

            br.close();

        } catch (Exception e) {
            Log.d("Exception", e.toString());
        } finally {
            iStream.close();
            urlConnection.disconnect();
        }
        return data;
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResult){
        super.onRequestPermissionsResult(requestCode, permissions, grantResult);

        if (grantResult.length > 0 && grantResult[0] == PackageManager.PERMISSION_GRANTED){
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
            }
        }
    }
}
