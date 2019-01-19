package com.example.ecko.spots;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.drawable.Icon;
import android.location.Location;
import android.location.LocationManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ecko.spots.model.Parque;
import com.example.ecko.spots.model.ParquesManager;
import com.example.ecko.spots.model.Spot;
import com.example.ecko.spots.model.SpotsManager;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.maps.android.ui.IconGenerator;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;


public class FindMeASpotActivity extends FragmentActivity {

    private static final String PARQUE = "PARQUE";

    private Boolean locationPermissionGranted = false;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private static final  String TAG = "AuthenticatedDashboardActivity";
    private static final String FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    private static final String COURSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;
    private static  final int LOCATION_PERMISSION_REQUEST_CODE = 1234;
    private double myLat;
    private double myLong;
    private SharedPreferences preferences;
    protected static final String PREFS_POS = "currentloc";
    private DatabaseReference mDatabase;

    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_me_aspot);

        preferences = getSharedPreferences(PREFS_POS, MODE_PRIVATE);
        mDatabase = FirebaseDatabase.getInstance().getReference();

        final String nomeParque = getIntent().getStringExtra(PARQUE);


        getLocationPermission();
        if (locationPermissionGranted){
            getDeviceLocation();
        }

        final FirebaseUser userAuth = FirebaseAuth.getInstance().getCurrentUser();

        FirebaseDatabase.getInstance().getReference("Users").child(userAuth.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot){

                String preferecias = snapshot.child("preference").getValue().toString();
                LinkedList<Spot> favourites = new LinkedList<>();

                DataSnapshot favoriteSnapshot = snapshot.child("favouriteSpots");
                Iterable<DataSnapshot> favoriteChildren = favoriteSnapshot.getChildren();
                for (DataSnapshot spotData : favoriteChildren) {
                    Spot spot = spotData.getValue(Spot.class);
                    favourites.add(spot);
                }

                //Atribuir Spots
                Spot spot1 = escolherSpot(preferecias, nomeParque, favourites);
                Spot spot2 = escolherSpot(preferecias, nomeParque, favourites);
                Spot spot3 = escolherSpot(preferecias, nomeParque, favourites);

                //Tornar Spots final
                final Spot spot1final = spot1;
                final Spot spot2final = spot2;
                final Spot spot3final = spot3;

                //Cancel
                Button btnCancel = findViewById(R.id.btnCancel);
                btnCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (spot2final == null){
                            spot1final.setEstado(0);
                        }else if(spot3final == null){
                            spot1final.setEstado(0);
                            spot2final.setEstado(0);
                        }else{
                            spot1final.setEstado(0);
                            spot2final.setEstado(0);
                            spot3final.setEstado(0);
                        }

                        startActivity(AuthenticatedDashboardActivity.getIntent(FindMeASpotActivity.this, 6));
                        finish();
                    }
                });

                //Spot1
                Button btnSpot1 = findViewById(R.id.btnSpot1);
                btnSpot1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        preferences = getSharedPreferences(PREFS_POS, MODE_PRIVATE);
                        double longit = 0;
                        double lat = 0;
                        if (preferences.contains("prefs_lat") && preferences.contains("prefs_long")){
                            lat = Double.parseDouble(preferences.getString("prefs_lat", "not found"));
                            longit = Double.parseDouble(preferences.getString("prefs_long", "not found"));
                        }
                        if(spot3final == null){
                            spot2final.setEstado(0);
                        }else if(spot3final!=null && spot2final!=null){
                            spot2final.setEstado(0);
                            spot3final.setEstado(0);
                        }

                        startActivity(NavigationActivity.getIntent(FindMeASpotActivity.this, lat, longit,spot1final.getCoordenadaX(), spot1final.getCoordenadaY(), nomeParque));
                        finish();
                    }
                });

                //Spot2
                final Button btnSpot2 = findViewById(R.id.btnSpot2);
                btnSpot2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        preferences = getSharedPreferences(PREFS_POS, MODE_PRIVATE);
                        double longit = 0;
                        double lat = 0;
                        if (preferences.contains("prefs_lat") && preferences.contains("prefs_long")){
                            lat = Double.parseDouble(preferences.getString("prefs_lat", "not found"));
                            longit = Double.parseDouble(preferences.getString("prefs_long", "not found"));
                        }
                        if(spot3final == null){
                            spot1final.setEstado(0);
                        }else{
                            spot1final.setEstado(0);
                            spot3final.setEstado(0);
                        }

                        startActivity(NavigationActivity.getIntent(FindMeASpotActivity.this, lat, longit,spot2final.getCoordenadaX(), spot2final.getCoordenadaY(), nomeParque));
                        finish();
                    }
                });

                //Spot3
                final Button btnSpot3 = findViewById(R.id.btnSpot3);
                btnSpot3.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        preferences = getSharedPreferences(PREFS_POS, MODE_PRIVATE);
                        double longit = 0;
                        double lat = 0;
                        if (preferences.contains("prefs_lat") && preferences.contains("prefs_long")){
                            lat = Double.parseDouble(preferences.getString("prefs_lat", "not found"));
                            longit = Double.parseDouble(preferences.getString("prefs_long", "not found"));
                        }
                        if (spot2final == null){
                            spot1final.setEstado(0);
                        }else{
                            spot1final.setEstado(0);
                            spot2final.setEstado(0);
                        }

                        startActivity(NavigationActivity.getIntent(FindMeASpotActivity.this, lat, longit,spot3final.getCoordenadaX(), spot3final.getCoordenadaY(), nomeParque));
                        finish();
                    }
                });


                //Funcao para Inicializar o Mapa Consoante os Spots
                SupportMapFragment mapFragment1 = (SupportMapFragment) getSupportFragmentManager()
                        .findFragmentById(R.id.mapFindMeASpot);
                mapFragment1.getMapAsync(new OnMapReadyCallback() {
                    @Override
                    public void onMapReady(GoogleMap googleMap) {
                        mMap = googleMap;

                        TextView textParque = findViewById(R.id.textParque);

                        //Mostrar Parque selecionado
                        textParque.setText("Parque: " + nomeParque);

                        //Posicionar camera no parque
                        LatLng markerParque = new LatLng(ParquesManager.INSTANCE.getParqueByName(spot1final.getParqueNome()).getCoordenadaX(),
                                ParquesManager.INSTANCE.getParqueByName(spot1final.getParqueNome()).getCoordenadaY());
                        mMap.moveCamera(CameraUpdateFactory.newLatLng(markerParque));
                        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(markerParque,19));

                        //Posicionar marcador da minha posiçao
                        preferences = getSharedPreferences(PREFS_POS, MODE_PRIVATE);
                        double longit = 0;
                        double lat = 0;
                        if (preferences.contains("prefs_lat") && preferences.contains("prefs_long")){
                            lat = Double.parseDouble(preferences.getString("prefs_lat", "not found"));
                            longit = Double.parseDouble(preferences.getString("prefs_long", "not found"));
                        }
                        LatLng markerCurrentLocation = new LatLng(lat, longit);
                        mMap.addMarker(new MarkerOptions().position(markerCurrentLocation).title("You Are Here!").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));


                        //Verificar Quantos Spots Foram Passados E Mostrar Informaçao
                        if(spot2final == null){ //So Existe 1 Spot Livre
                            //Obter localizacao do spot e do parque
                            LatLng markerSpot1 = new LatLng(spot1final.getCoordenadaX()
                                    , spot1final.getCoordenadaY());

                            //Meter marcador no spot
                            Marker marker1 =mMap.addMarker(new MarkerOptions().position(markerSpot1).title("Spot 1"));
                            marker1.showInfoWindow();
                            
                            //Atualizar Mensagem
                            Toast.makeText(getApplicationContext(), "So existe um spot livre", Toast.LENGTH_SHORT).show();
                            btnSpot2.setVisibility(View.INVISIBLE);
                            btnSpot3.setVisibility(View.INVISIBLE);

                        }else if(spot3final == null){ //So Existem 2 Spots Livres
                            //Obter localizacao dos 2 spots e do parque
                            LatLng markerSpot1 = new LatLng(spot1final.getCoordenadaX()
                                    , spot1final.getCoordenadaY());
                            LatLng markerSpot2 = new LatLng(spot2final.getCoordenadaX()
                                    , spot2final.getCoordenadaY());

                            //Meter marcadores nos 2 spots
                            Marker marker1 =mMap.addMarker(new MarkerOptions().position(markerSpot1).title("Spot 1"));
                            marker1.showInfoWindow();
                            Marker marker2 =mMap.addMarker(new MarkerOptions().position(markerSpot2).title("Spot 2"));
                            //Atualizar Mensagem
                            Toast.makeText(getApplicationContext(), "So existem dois spots livres", Toast.LENGTH_SHORT).show();
                            btnSpot3.setVisibility(View.INVISIBLE);

                        }else{
                            //Obter localizacao dos 3 spots e do parque
                            LatLng markerSpot1 = new LatLng(spot1final.getCoordenadaX()
                                    , spot1final.getCoordenadaY());
                            LatLng markerSpot2 = new LatLng(spot2final.getCoordenadaX()
                                    , spot2final.getCoordenadaY());
                            LatLng markerSpot3 = new LatLng(spot3final.getCoordenadaX()
                                    , spot3final.getCoordenadaY());

                            //Meter marcadores nos 3 spots
                            Marker marker1 =mMap.addMarker(new MarkerOptions().position(markerSpot1).title("Spot 1"));
                            marker1.showInfoWindow();
                            Marker marker2 =mMap.addMarker(new MarkerOptions().position(markerSpot2).title("Spot 2"));
                            Marker marker3 =mMap.addMarker(new MarkerOptions().position(markerSpot3).title("Spot 3"));

                        }

                        mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);

                    }
                });

            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

    }


    private void showMessage(int message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(message);
        builder.setNeutralButton(R.string.OK, null);
        builder.show();
    }


    //Escolher 3 spots conforme preferencias
    public Spot escolherSpot(String preferencia, String parqueName, LinkedList<Spot> favourites){
        //Comeco da contagem de tempo
        long startTime = System.currentTimeMillis();

        long total = 0;
        for (int i = 0; i < 10000000; i++) {
            total += i;
        }

        Parque parqueSelected = ParquesManager.INSTANCE.getParqueByName(parqueName);
        List<Spot> listaSpots = ParquesManager.INSTANCE.getListaSpotsNomeParque(parqueName);

        if(ParquesManager.INSTANCE.hasAvailableSpots(listaSpots, parqueSelected).equals("no")) {
            showMessage(R.string.noavailablespots_inpark);
        }

        if(preferencia.equals("Qualification")){
            Spot maiorRating = null;
            for (Spot spotp : listaSpots){   //Percorre spots do parque
                if (spotp.getEstado() != 0){
                    continue;
                }else if (maiorRating == null){
                    maiorRating = spotp;
                }
                if (spotp.getRating() > maiorRating.getRating()){
                    maiorRating = spotp;
                    if (maiorRating.getRating() == 5){  //Se rating for maximo devolve logo
                        maiorRating.setEstado(2);
                        long stopTime = System.currentTimeMillis();
                        long elapsedTime = stopTime - startTime;
                        mDatabase.child("TempoMedioApp").child("Qualification").push().setValue(elapsedTime);
                        System.out.println("Qualification Demorou: "+elapsedTime);
                        return  maiorRating;
                    }
                }
            }
            if (maiorRating != null){
                maiorRating.setEstado(2);
                long stopTime = System.currentTimeMillis();
                long elapsedTime = stopTime - startTime;
                mDatabase.child("TempoMedioApp").child("Qualification").push().setValue(elapsedTime);
                System.out.println("Qualification Demorou: "+elapsedTime);
                return maiorRating;
            }
        }else if (preferencia.equals("Favourites")){
            for (Spot spotf : favourites){    //Percorre favoritos
                if (spotf.getEstado() != 0){
                    continue;
                }
                for (Spot spotp : listaSpots){   //Percorre spots do parque
                    if (spotf == spotp){    //Se coincidirem devolve
                        spotf.setEstado(2);
                        long stopTime = System.currentTimeMillis();
                        long elapsedTime = stopTime - startTime;
                        mDatabase.child("TempoMedioApp").child("Favourites").push().setValue(elapsedTime);
                        System.out.println("Favourites Demorou: "+elapsedTime);
                        return spotf;
                    }
                }
            }
        }

        //default faz por proximidade
        double menorDist = 0;
        Spot spotMenorDist = null;
        double dist = 0;

        preferences = getSharedPreferences(PREFS_POS, MODE_PRIVATE);
        double longit = 0;
        double lat = 0;
        if (preferences.contains("prefs_lat") && preferences.contains("prefs_long")){
            lat = Double.parseDouble(preferences.getString("prefs_lat", "not found"));
            longit = Double.parseDouble(preferences.getString("prefs_long", "not found"));
        }

        for (Spot spotp : listaSpots){   //Percorre spots do parque
            if (spotp.getEstado() == 0){
                dist = Math.sqrt((longit - spotp.getCoordenadaY()) * (longit - spotp.getCoordenadaY()) + (lat - spotp.getCoordenadaX()) * (lat - spotp.getCoordenadaX()));
                if (spotMenorDist == null){
                    spotMenorDist = spotp;
                    menorDist = dist;
                }else{
                    if (dist < menorDist){
                        spotMenorDist = spotp;
                        menorDist = dist;
                    }
                }
            }
        }

        spotMenorDist.setEstado(2);
        Log.d(TAG, "Distancia = " + menorDist);

        //contar tempo da app
        long stopTime = System.currentTimeMillis();
        long elapsedTime = stopTime - startTime;
        mDatabase.child("TempoMedioApp").child("Closest").push().setValue(elapsedTime);

        System.out.println("Distancia Demorou: "+elapsedTime);

        return spotMenorDist;
    }


    private void getLocationPermission(){
        Log.d(TAG, "getLocationPermission: getting location permission");
        String[] permissions = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};

        if (ContextCompat.checkSelfPermission(this, FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
            if (ContextCompat.checkSelfPermission(this, COURSE_LOCATION) == PackageManager.PERMISSION_GRANTED){
                locationPermissionGranted = true;
                Log.d(TAG, "getLocationPermission: got permissions");

                //init
            }else{
                ActivityCompat.requestPermissions(this, permissions, LOCATION_PERMISSION_REQUEST_CODE);
                Log.d(TAG, "getLocationPermission: failed to get permissions on first else");
            }
        }else{
            ActivityCompat.requestPermissions(this, permissions, LOCATION_PERMISSION_REQUEST_CODE);
            Log.d(TAG, "getLocationPermission: failed to get permissions on second else");
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResult){
        super.onRequestPermissionsResult(requestCode, permissions, grantResult);
        locationPermissionGranted = false;

        switch (requestCode){
            case LOCATION_PERMISSION_REQUEST_CODE:{
                if (grantResult.length > 0){
                    for (int i = 0; i < grantResult.length; i++){
                        if (grantResult[i] != PackageManager.PERMISSION_GRANTED){
                            locationPermissionGranted = false;
                            Log.d(TAG, "onRequestPermissionsResult: permission failed");
                            return;
                        }
                    }
                    Log.d(TAG, "onRequestPermissionsResult: permission granted");
                    locationPermissionGranted = true;
                }
            }
        }
    }


    private void getDeviceLocation(){
        Log.d(TAG, "getDeviceLocation: getting devices location");
        final TextView textViewLat = findViewById(R.id.textMyLat);
        final TextView textViewLong = findViewById(R.id.textMyLong);

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        try {
            if (locationPermissionGranted){
                final Task location = fusedLocationProviderClient.getLastLocation();
                location.addOnCompleteListener(new OnCompleteListener(){
                    @Override
                    public void onComplete(@NonNull Task task){
                        if (task.isSuccessful()){
                            Location currentLocation =(Location) task.getResult();
                            myLat = currentLocation.getLatitude();
                            myLong = currentLocation.getLongitude();
                            textViewLat.setText(String.valueOf(myLat));
                            textViewLong.setText(String.valueOf(myLong));
                            preferences.edit().clear().apply();
                            SharedPreferences.Editor editor = preferences.edit();
                            editor.putString("prefs_lat", String.valueOf(myLat));
                            editor.putString("prefs_long", String.valueOf(myLong));
                            editor.apply();

                            Log.d(TAG, "getDeviceLocation: got device location");
                        }else{
                            Log.d(TAG, "getDeviceLocation: task not successful");
                        }
                    }
                });
            }else{
                Log.d(TAG, "getDeviceLocation: unable to get device location");
            }
        }catch (SecurityException e){
            Log.e(TAG, "getDeviceLocation: SecurityException: " + e.getMessage());
        }
    }



}
