package com.example.ecko.spots;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.RatingBar;
import android.widget.Toast;

import com.example.ecko.spots.model.Parque;
import com.example.ecko.spots.model.ParquesManager;
import com.example.ecko.spots.model.Spot;
import com.example.ecko.spots.model.SpotsManager;
import com.example.ecko.spots.model.User;
import com.example.ecko.spots.model.UserManager;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.FirebaseError;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import static android.icu.lang.UCharacter.GraphemeClusterBreak.T;
import static android.widget.Toast.*;
import static com.google.maps.android.SphericalUtil.computeDistanceBetween;


public class MySpotActivity extends FragmentActivity implements OnMapReadyCallback {
    private GoogleMap mMap;
    private Button btnLeave;
    private LocationManager locationManager;
    private LocationListener locationListener;
    private Marker currentPositionMarker = null;
    private double destLat = 0;
    private double destLong = 0;
    private boolean stop = false;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_spot);

        //Verificar quando nos afastamos do spot
        locationManager = (LocationManager) this.getSystemService(LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                double latitude = location.getLatitude();
                double longitude = location.getLongitude();
                LatLng markerCurrent = new LatLng(latitude, longitude);
                LatLng markerDest = new LatLng(destLat, destLong); //mudar para destino

                currentPositionMarker.setPosition(markerCurrent);

                if (!stop) {
                    int dist = (int) computeDistanceBetween(markerCurrent, markerDest); //Nao ta a funcionar
                    Toast.makeText(getApplicationContext(), dist + " metros", Toast.LENGTH_SHORT).show();

                    if (dist > 20) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(MySpotActivity.this);
                        builder.setMessage("Do you want to leave this Spot?")
                                .setCancelable(false)
                                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        stop = true;
                                    }
                                })
                                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        locationManager.removeUpdates(locationListener);
                                        String wel = getIntent().getStringExtra("parque");
                                        startActivity(PopUpActivity.getIntent(MySpotActivity.this, wel));
                                        finish();
                                    }
                                });
                        builder.show();
                    }
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

        //get parque escolhido de outra atividade autenticada
        btnLeave = findViewById(R.id.btnLeaveMySpot);
        btnLeave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String wel = getIntent().getStringExtra("parque");
                startActivity(PopUpActivity.getIntent(MySpotActivity.this, wel));
                locationManager.removeUpdates(locationListener);
                finish();
            }
        });

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.mapMySpot);
        mapFragment.getMapAsync(this);
    }

    public static Intent getIntent(Context context, String nomeParqueSelecionado, int posicao) {
        Intent intent = new Intent(context, MySpotActivity.class);
        intent.putExtra("parque", nomeParqueSelecionado);
        intent.putExtra("posicao", posicao);
        return intent;
    }

    public void cancelSpot(View view) {
        startActivity(AuthenticatedDashboardActivity.getIntent(MySpotActivity.this, 6));
        locationManager.removeUpdates(locationListener);
        finish();
    }

    public void checkHasSpot(User user, String parqueName) {
        if (user.getLugarOcupado() == null) {
            showErrorMessage(R.string.notHaveSavedSpot);
        } else {
            Spot spot = SpotsManager.INSTANCE.getSpot(user.getIndexMySpot(), parqueName);
            // Obtain the SupportMapFragment and get notified when the map is ready to be used.
            SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.mapMySpot);
            mapFragment.getMapAsync(this);
        }
    }

    private void showErrorMessage(int message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(message);
        builder.setNeutralButton(R.string.OK, null);
        builder.show();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        FirebaseUser userAuth = FirebaseAuth.getInstance().getCurrentUser();
        FirebaseDatabase.getInstance().getReference("Users/" + userAuth.getUid() + "/MySpot").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                List<Spot> mySpot = new ArrayList<>();
                Iterable<DataSnapshot> mySpotChildren = snapshot.getChildren();
                for (DataSnapshot spotData : mySpotChildren) {
                    if (spotData.exists()) {
                        Spot spot = spotData.getValue(Spot.class);
                        LatLng locationSpot = new LatLng(spot.getCoordenadaX(), spot.getCoordenadaY());
                        mMap.addMarker(new MarkerOptions().position(locationSpot).title("My Spot"));
                        destLat = spot.getCoordenadaX();
                        destLong = spot.getCoordenadaY();
                        mMap.moveCamera(CameraUpdateFactory.newLatLng(locationSpot));
                        CameraUpdate yourLocation = CameraUpdateFactory.newLatLngZoom(locationSpot, 18);
                        mMap.animateCamera(yourLocation);
                        mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);

                        //My position market
                        currentPositionMarker = mMap.addMarker(new MarkerOptions().position(new LatLng(0,0)).title("You are here!"));

                    } else {
                        showErrorMessage(R.string.notHaveSavedSpot);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

    }
}
