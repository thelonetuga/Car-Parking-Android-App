package com.example.ecko.spots;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.ecko.spots.model.Parque;
import com.example.ecko.spots.model.ParquesManager;
import com.example.ecko.spots.model.Spot;
import com.example.ecko.spots.model.SpotsManager;
import com.example.ecko.spots.model.UserManager;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class AuthenticatedDashboardActivity extends FragmentActivity implements OnMapReadyCallback, AdapterView.OnItemSelectedListener {
    Integer[] places;
    private GoogleMap mMap;
    Date currentTime = Calendar.getInstance().getTime();
    private LocationManager locationManager;
    private LocationListener locationListener;
    private Button btnFindMeASpot;
    private Button btnMySpot;
    private List<Parque> listaParques;
    private List<Spot> listaSpotsD;
    private List<Spot> listaSpotsDakar;
    private List<Spot> listaSpotsA;
    private List<Spot> listaSpotsESSLEI;
    private DatabaseReference mDatabase;// = FirebaseDatabase.getInstance().getReference("Users");
    private FirebaseUser auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authenticated_dashboard);
        setTitle(R.string.myAccountTitle);
        btnMySpot = findViewById(R.id.btnMySpot);
        btnFindMeASpot = findViewById(R.id.btnFindMeASpot);

        mDatabase = FirebaseDatabase.getInstance().getReference("Users");
        auth = FirebaseAuth.getInstance().getCurrentUser();

        //Atualixação do campo autenticado no Firebase
        mDatabase.child(auth.getUid()).child("autenticado").setValue(true);

        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                UserManager.INSTANCE.setNumeroUsers(dataSnapshot.getChildrenCount());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        //Atribuicao do update time
        TextView textViewUpdate = findViewById(R.id.txtData);
        textViewUpdate.setText("Last update: " + currentTime.toString());
        final int msg = getIntent().getIntExtra("WELCOME", -1);
        if (msg == 10){
            showMessage(R.string.thanksMSG);
        }

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        //Criacao do fragmento para o mapa
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapAuth);
        mapFragment.getMapAsync(this);
        //Evento do spinner onde acontece, o update da grid consoante o parque Escolhido

        final ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.park_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        final Spinner spinner1 = findViewById(R.id.availableSpotsSpinner);
        spinner1.setAdapter(adapter);
        spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, final View view, int position, long id) {

                int indexSpinner = spinner1.getSelectedItemPosition();
                final String nomeParqueSelecionado = ParquesManager.INSTANCE.getParque(indexSpinner).getNome();

                final GridView gridView = findViewById(R.id.gridviewAuthParque);
                TextView textView = findViewById(R.id.txtViewNomeAuthParque);
                final List<Spot> listaSpotsParqueSelecionando = ParquesManager.INSTANCE.getListaSpotsNomeParque(nomeParqueSelecionado);
                textView.setText("Parque: " + nomeParqueSelecionado);
                Parque parqueSelected = ParquesManager.INSTANCE.getParque(indexSpinner);
                int tamanhoParque = ParquesManager.INSTANCE.getTamanhoParque(parqueSelected);

                // Initializing a new String Array
                places = new Integer[tamanhoParque];

                for (int i = 0; i < tamanhoParque; i++) {
                    places[i] = i;
                }

                // Populate a List from Array elements
                final List<Integer> placesList = new ArrayList<>(Arrays.asList(places));
                if (ParquesManager.INSTANCE.hasAvailableSpots(listaSpotsParqueSelecionando, parqueSelected).equals("no")) {
                    showMessage(R.string.noavailablespots);
                }

                /*if (!ParquesManager.INSTANCE.hasSpot(nomeParqueSelecionado)) {
                    showMessage(R.string.carNotFound);
                }*/
                // Create a new ArrayAdapter
                final ArrayAdapter<Integer> gridViewArrayAdapter = new ArrayAdapter<Integer>(AuthenticatedDashboardActivity.this, android.R.layout.simple_list_item_1, placesList) {
                    @Override
                    public View getView(final int position, final View convertView, final ViewGroup parent) {
                        Spinner spinner1 = findViewById(R.id.availableSpotsSpinner);
                        int indexSpinner = spinner1.getSelectedItemPosition();
                        final String nomeParqueSelecionado = ParquesManager.INSTANCE.getParque(indexSpinner).getNome();
                        final List<Spot> listaSpotsParqueSelecionando = new ArrayList<>();
                        final View view = super.getView(position, convertView, parent);
                        FirebaseDatabase.getInstance().getReference("Spots").addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot snapshot) {
                                Iterable<DataSnapshot> parques = snapshot.child(nomeParqueSelecionado).getChildren();
                                for (DataSnapshot spotData : parques) {
                                    Double cordX = spotData.child("coordenadaX").getValue(Double.class);
                                    Double cordY = spotData.child("coordenadaY").getValue(Double.class);
                                    String parqueNome = spotData.child("parqueNome").getValue(String.class);
                                    Boolean isOcupado = spotData.child("ocupado").getValue(Boolean.class);
                                    int estado = spotData.child("estado").getValue(Integer.class);
                                    float rating = spotData.child("rating").getValue(Float.class);
                                    Spot spot = new Spot(cordX, cordY, parqueNome, isOcupado, estado, rating);
                                    listaSpotsParqueSelecionando.add(spot);
                                }

                                if (ParquesManager.INSTANCE.isLugarOcupado(listaSpotsParqueSelecionando.get(position))) {
                                    view.setBackgroundColor(Color.rgb(175, 34, 12)); //dark red
                                } else {
                                    view.setBackgroundColor(Color.rgb(5, 142, 21)); //dark green
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                            }
                        });


                        final FirebaseUser userAuth = FirebaseAuth.getInstance().getCurrentUser();
                        FirebaseDatabase.getInstance().getReference("Users/" + userAuth.getUid() + "/MySpot").addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot snapshot) {
                                Iterable<DataSnapshot> nomeMyspot = snapshot.getChildren();
                                for (DataSnapshot spotData : nomeMyspot) {
                                    Integer mySpotEstado = spotData.child("estado").getValue(Integer.class);
                                    String[] locationMySpotFirebase = spotData.getKey().split("\\s+");
                                    if (mySpotEstado != null && mySpotEstado == 2 && Integer.parseInt(locationMySpotFirebase[1]) == position) {
                                        view.setBackgroundColor(Color.GRAY);
                                        return;
                                    }
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                            }
                        });
                        return view;
                    }
                };
                // Data bind GridView with ArrayAdapter (String Array elements)
                gridView.setAdapter(gridViewArrayAdapter);
                gridView.invalidateViews();
                gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    public void onItemClick(AdapterView<?> parent, View v, final int position, long id) {
                        final FirebaseUser userAuth = FirebaseAuth.getInstance().getCurrentUser();
                        FirebaseDatabase.getInstance().getReference("Users/" + userAuth.getUid() + "/MySpot").addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot snapshot) {
                                Spinner spinner1 = findViewById(R.id.availableSpotsSpinner);
                                int indexSpinner = spinner1.getSelectedItemPosition();
                                final String nomeParqueSelecionado = ParquesManager.INSTANCE.getParque(indexSpinner).getNome();
                                String[] spotEscolhido = snapshot.child(nomeParqueSelecionado + " " + position).getKey().split("\\s+");
                                Spot spot = SpotsManager.INSTANCE.getSpot(position, nomeParqueSelecionado);
                                //spot não ocupado nem é o meu prórprio spot
                                if (!spot.getOcupado() && spot.getEstado() != 2) {
                                    if (snapshot.exists()) {
                                        //ja tenho spot em my spot
                                        showMessage(R.string.alreadyHaveSpot);
                                        return;
                                    }
                                    //nao tenho spot
                                    Calendar rightNow = Calendar.getInstance();
                                    int currentHour = rightNow.get(Calendar.HOUR_OF_DAY);
                                    int currentMinute = rightNow.get(Calendar.MINUTE);
                                    int currentSeconds = rightNow.get(Calendar.SECOND);
                                    FirebaseDatabase.getInstance().getReference("Spots").child(nomeParqueSelecionado).child(spotEscolhido[1]).child("ocupado").setValue(true);
                                    FirebaseDatabase.getInstance().getReference("Spots").child(nomeParqueSelecionado).child(spotEscolhido[1]).child("estado").setValue(2);
                                    FirebaseDatabase.getInstance().getReference("Spots").child(nomeParqueSelecionado).child(spotEscolhido[1]).child("Registo de Ocupacao").push().setValue(currentHour +":"+currentMinute);
                                    FirebaseDatabase.getInstance().getReference("Users").child(userAuth.getUid()).child("MySpot").child(nomeParqueSelecionado + " " + position).setValue(spot);
                                    FirebaseDatabase.getInstance().getReference("Users").child(userAuth.getUid()).child("MySpot").child(nomeParqueSelecionado + " " + position).child("estado").setValue(2);
                                    FirebaseDatabase.getInstance().getReference("Users").child(userAuth.getUid()).child("MySpot").child(nomeParqueSelecionado + " " + position).child("ocupado").setValue(true);
                                    gridViewArrayAdapter.notifyDataSetChanged();
                                    showMessage(R.string.spotAdded);
                                } else {
                                    showMessage(R.string.OcupadoPorOutroCarro);
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                            }
                        });
                    }
                });

                gridViewArrayAdapter.notifyDataSetChanged();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        final int wel = getIntent().getIntExtra("WELCOME", -1);

        //Show welcome message
        if (wel != 6 && wel != 10) {
            showMessage(R.string.welcomeMessage);
        }

        btnMySpot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final FirebaseUser userAuth = FirebaseAuth.getInstance().getCurrentUser();

                FirebaseDatabase.getInstance().getReference("Users/" + userAuth.getUid() + "/MySpot").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            Iterable<DataSnapshot> nomeMyspot = snapshot.getChildren();
                            for (DataSnapshot spotData : nomeMyspot) {
                                String spotAntigo = spotData.getKey();
                                String[] locationMySpotFirebase = snapshot.child(spotAntigo).getKey().split("\\s+");
                                startActivity(MySpotActivity.getIntent(AuthenticatedDashboardActivity.this, locationMySpotFirebase[0], Integer.parseInt(locationMySpotFirebase[1])));
                                finish();
                            }
                        }else {
                            showMessage(R.string.notHaveSavedSpot);
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });
            }
        });
    }

    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String textSelected = parent.getItemAtPosition(position).toString();
        TextView tv2 = findViewById(R.id.txtViewNomeAuthParque);
        tv2.setText(textSelected);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        listaParques = ParquesManager.INSTANCE.getParques();
        for (Parque parque : listaParques) {
            LatLng locationParque = new LatLng(parque.getCoordenadaX(), parque.getCoordenadaY());
            mMap.addMarker(new MarkerOptions().position(locationParque).title(parque.getNome()));
        }
        mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(listaParques.get(0).getCoordenadaX(), listaParques.get(0).getCoordenadaY())));
        CameraUpdate yourLocation = CameraUpdateFactory.newLatLngZoom(new LatLng(listaParques.get(0).getCoordenadaX(), listaParques.get(0).getCoordenadaY()), 18);
        mMap.animateCamera(yourLocation);
        // Add a marker in Sydney and move the camera
    }

    public static Intent getIntent(Context context, int wel) {
        Intent intent = new Intent(context, AuthenticatedDashboardActivity.class);
        intent.putExtra("WELCOME", wel);
        return intent;
    }


    private void showMessage(int message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(message);
        builder.setNeutralButton(R.string.OK, null);
        builder.show();
    }

    //Acao quando clico no "Logout"
    public void makeLogout(View view) throws InterruptedException {
        mDatabase.child(auth.getUid()).child("autenticado").setValue(false);
        FirebaseAuth.getInstance().signOut();
        startActivity(DashboardActivity.getIntentLogout(AuthenticatedDashboardActivity.this, 3));
        getSharedPreferences(LoginActivity.PREFS_NAME, MODE_PRIVATE).edit().clear().apply();
        finish();
    }

    public void onClickMoreInfo(View view) {
        startActivity(AuthenticatedMoreInfoActivity.getIntent(AuthenticatedDashboardActivity.this));
    }

    public void onClickPerfil(View view) {
        startActivity(AuthenticatedProfileActivity.getIntent(AuthenticatedDashboardActivity.this));
    }

    public void onClickReport(View view){
        startActivity(ReportActivity.getIntent(AuthenticatedDashboardActivity.this));
        finish();
    }

    public void findMeASpot(View view) {
        Spinner spinner = findViewById(R.id.availableSpotsSpinner);
        String parqueSelectedStr = ParquesManager.INSTANCE.getParque(spinner.getSelectedItemPosition()).getNome();
        Parque parqueSelected = ParquesManager.INSTANCE.getParqueByName(parqueSelectedStr);
        List<Spot> listaSpots = ParquesManager.INSTANCE.getListaSpotsNomeParque(parqueSelectedStr);

        if (ParquesManager.INSTANCE.hasAvailableSpots(listaSpots, parqueSelected).equals("no")) {
            showMessage(R.string.noavailablespots_inpark);
        } else {
            //show error message se localizaçao desligada
            if(isLocationServicesAvailable(getApplicationContext())){
                //Comecar activityFindMeASpot
                Intent intent = new Intent(AuthenticatedDashboardActivity.this, FindMeASpotActivity.class);
                intent.putExtra("PARQUE", parqueSelectedStr);
                startActivity(intent);
                finish();
            }else{
                showMessage(R.string.location_disabled);
            }
        }
    }

    public static boolean isLocationServicesAvailable(Context context) {
        int locationMode = 0;
        String locationProviders;
        boolean isAvailable = false;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT){
            try {
                locationMode = Settings.Secure.getInt(context.getContentResolver(), Settings.Secure.LOCATION_MODE);
            } catch (Settings.SettingNotFoundException e) {
                e.printStackTrace();
            }

            isAvailable = (locationMode != Settings.Secure.LOCATION_MODE_OFF);
        } else {
            locationProviders = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
            isAvailable = !TextUtils.isEmpty(locationProviders);
        }

        boolean coarsePermissionCheck = (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED);
        boolean finePermissionCheck = (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED);

        return isAvailable && (coarsePermissionCheck || finePermissionCheck);
    }


    public void onStatistics(View view) {
        Intent intent = new Intent(AuthenticatedDashboardActivity.this, StatisticsActivity.class);
        startActivity(intent);
    }
}
