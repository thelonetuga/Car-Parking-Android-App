package com.example.ecko.spots;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ecko.spots.model.Parque;
import com.example.ecko.spots.model.ParquesManager;
import com.example.ecko.spots.model.Spot;
import com.example.ecko.spots.model.SpotsManager;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

public class DashboardActivity extends FragmentActivity implements OnMapReadyCallback {

    Integer[] places;
    private GoogleMap mMap;
    private SharedPreferences preferences;
    Date currentTime = Calendar.getInstance().getTime();
    Parque parqueSpot;
    private List<Parque> listadefaultParques;
    private List<Spot> listaSpotsDefaultParque;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        setTitle(R.string.myAccountTitle);

        if (FirebaseAuth.getInstance().getCurrentUser() != null && getSharedPreferencesData()) {
            startActivity(AuthenticatedDashboardActivity.getIntent(DashboardActivity.this, 1));
            finish();
        }

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        listaSpotsDefaultParque = ParquesManager.INSTANCE.getListaSpotsDefaultParque();
        final int wel = getIntent().getIntExtra("WELCOME",-1);
        final int bye = getIntent().getIntExtra("BYE",-1);



        TextView textViewUpdate = findViewById(R.id.txtData);
        textViewUpdate.setText("Last update: " + currentTime.toString());

        Parque defaultParque = ParquesManager.INSTANCE.getDefaultParque();
        TextView textView = findViewById(R.id.txtViewNomeParque);
        textView.append(" " + ParquesManager.INSTANCE.getDefaultParque().getNome());

        int tamanhoParque = ParquesManager.INSTANCE.getTamanhoParque(defaultParque);

        // Initializing a new String Array
        places = new Integer[tamanhoParque];
        for (int i = 0; i < tamanhoParque; i++) {
            places[i] = i + 1;
        }
        // Populate a List from Array elements
         List<Integer> placesList = new ArrayList<>(Arrays.asList(places));

        // Create a new ArrayAdapter
        GridView gridView = findViewById(R.id.gridviewParque);
        ArrayAdapter<Integer> gridViewArrayAdapter = new ArrayAdapter<Integer>(this, android.R.layout.simple_list_item_1, placesList){
            @Override
            public View getView(final int position, View convertView, ViewGroup parent) {
                final View view = super.getView(position, convertView, parent);
                FirebaseDatabase.getInstance().getReference("Spots").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {
                        Iterable<DataSnapshot> parques = snapshot.child("DEFAULTPARK").getChildren();
                        for (DataSnapshot spotData : parques) {
                            Double cordX = spotData.child("coordenadaX").getValue(Double.class);
                            Double cordY = spotData.child("coordenadaY").getValue(Double.class);
                            String parqueNome = spotData.child("parqueNome").getValue(String.class);
                            Boolean isOcupado = spotData.child("ocupado").getValue(Boolean.class);
                            int estado = spotData.child("estado").getValue(Integer.class);
                            float rating = spotData.child("rating").getValue(Float.class);
                            Spot spot = new Spot(cordX,cordY,parqueNome,isOcupado,estado,rating);
                            listaSpotsDefaultParque.add(spot);
                        }

                        float taxaOcupacao = 0.0f;
                        int numeroOcupados = 0;
                        int tamanhoLista = listaSpotsDefaultParque.size();
                        for (int i = 0; i< listaSpotsDefaultParque.size(); i++){
                            Spot spot =listaSpotsDefaultParque.get(i);
                            if (spot.getOcupado() == true){
                                numeroOcupados++;
                            }
                        }
                        taxaOcupacao = (numeroOcupados*100)/tamanhoLista;
                        float naoOcupado = 100-taxaOcupacao;
                        FirebaseDatabase.getInstance().getReference("TaxaOcupacao").child("DEFAULTPARK").child("Nao Ocupado").setValue(naoOcupado);
                        FirebaseDatabase.getInstance().getReference("TaxaOcupacao").child("DEFAULTPARK").child("Ocupado").setValue(taxaOcupacao);
                        SpotsManager.INSTANCE.setDefaultParqueSpots(listaSpotsDefaultParque);
                        if(ParquesManager.INSTANCE.isLugarOcupado(listaSpotsDefaultParque.get(position))){
                            view.setBackgroundColor(Color.rgb(175, 34, 12));//dark red
                        } else {
                            view.setBackgroundColor(Color.rgb(5, 142, 21));//dark green
                        }
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });
                return view;
            }
        };
        gridView.setAdapter(gridViewArrayAdapter);

        //Firebase get spots data
        FirebaseDatabase.getInstance().getReference("Spots").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                List<Spot> listaSpotsParqueA = new ArrayList<>();
                Iterable<DataSnapshot> spotsA = snapshot.child("ParqueA").getChildren();
                for (DataSnapshot spotData : spotsA) {
                    Double cordX = spotData.child("coordenadaX").getValue(Double.class);
                    Double cordY = spotData.child("coordenadaY").getValue(Double.class);
                    String parqueNome = spotData.child("parqueNome").getValue(String.class);
                    Boolean isOcupado = spotData.child("ocupado").getValue(Boolean.class);
                    int estado = spotData.child("estado").getValue(Integer.class);
                    float rating = spotData.child("rating").getValue(Float.class);
                    Spot spot = new Spot(cordX,cordY,parqueNome,isOcupado,estado,rating);
                    listaSpotsParqueA.add(spot);
                }

                float taxaOcupacao = 0.0f;
                int numeroOcupados = 0;
                int tamanhoLista = listaSpotsParqueA.size();
                for (int i = 0; i< listaSpotsParqueA.size(); i++){
                    Spot spot =listaSpotsParqueA.get(i);
                    if (spot.getOcupado() == true){
                        numeroOcupados++;
                    }
                }
                taxaOcupacao = (numeroOcupados*100)/tamanhoLista;
                float naoOcupado = 100-taxaOcupacao;
                FirebaseDatabase.getInstance().getReference("TaxaOcupacao").child("ParqueA").child("Ocupado").setValue(taxaOcupacao);
                FirebaseDatabase.getInstance().getReference("TaxaOcupacao").child("ParqueA").child("Nao Ocupado").setValue(naoOcupado);
                System.out.println(listaSpotsParqueA);
                SpotsManager.INSTANCE.setListSpotsParques("ParqueA",listaSpotsParqueA);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        FirebaseDatabase.getInstance().getReference("Spots").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                List<Spot> listaSpotsParqueD = new ArrayList<>();
                Iterable<DataSnapshot> spotsD = snapshot.child("ParqueD").getChildren();
                for (DataSnapshot spotData : spotsD) {
                    Double cordX = spotData.child("coordenadaX").getValue(Double.class);
                    Double cordY = spotData.child("coordenadaY").getValue(Double.class);
                    String parqueNome = spotData.child("parqueNome").getValue(String.class);
                    Boolean isOcupado = spotData.child("ocupado").getValue(Boolean.class);
                    int estado = spotData.child("estado").getValue(Integer.class);
                    float rating = spotData.child("rating").getValue(Float.class);
                    Spot spot = new Spot(cordX,cordY,parqueNome,isOcupado,estado,rating);
                    listaSpotsParqueD.add(spot);
                }
                float taxaOcupacao = 0.0f;
                int numeroOcupados = 0;
                int tamanhoLista = listaSpotsParqueD.size();
                for (int i = 0; i< listaSpotsParqueD.size(); i++){
                    Spot spot =listaSpotsParqueD.get(i);
                    if (spot.getOcupado() == true){
                        numeroOcupados++;
                    }
                }
                taxaOcupacao = (numeroOcupados*100)/tamanhoLista;
                float naoOcupado = 100-taxaOcupacao;
                FirebaseDatabase.getInstance().getReference("TaxaOcupacao").child("ParqueD").child("Ocupado").setValue(taxaOcupacao);
                FirebaseDatabase.getInstance().getReference("TaxaOcupacao").child("ParqueD").child("Nao Ocupado").setValue(naoOcupado);
                SpotsManager.INSTANCE.setListSpotsParques("ParqueD",listaSpotsParqueD);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        FirebaseDatabase.getInstance().getReference("Spots").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                List<Spot> listaSpotsParqueESSLEI = new ArrayList<>();
                Iterable<DataSnapshot> spotsESSLEI = snapshot.child("ParqueESSLEI").getChildren();
                for (DataSnapshot spotData : spotsESSLEI) {
                    Double cordX = spotData.child("coordenadaX").getValue(Double.class);
                    Double cordY = spotData.child("coordenadaY").getValue(Double.class);
                    String parqueNome = spotData.child("parqueNome").getValue(String.class);
                    Boolean isOcupado = spotData.child("ocupado").getValue(Boolean.class);
                    int estado = spotData.child("estado").getValue(Integer.class);
                    float rating = spotData.child("rating").getValue(Float.class);
                    Spot spot = new Spot(cordX,cordY,parqueNome,isOcupado,estado,rating);
                    listaSpotsParqueESSLEI.add(spot);
                }
                float taxaOcupacao = 0.0f;
                int numeroOcupados = 0;
                int tamanhoLista = listaSpotsParqueESSLEI.size();
                for (int i = 0; i< listaSpotsParqueESSLEI.size(); i++){
                    Spot spot =listaSpotsParqueESSLEI.get(i);
                    if (spot.getOcupado() == true){
                        numeroOcupados++;
                    }
                }
                taxaOcupacao = (numeroOcupados*100)/tamanhoLista;
                float naoOcupado = 100-taxaOcupacao;
                FirebaseDatabase.getInstance().getReference("TaxaOcupacao").child("ParqueESSLEI").child("Ocupado").setValue(taxaOcupacao);
                FirebaseDatabase.getInstance().getReference("TaxaOcupacao").child("ParqueESSLEI").child("Nao Ocupado").setValue(naoOcupado);
                SpotsManager.INSTANCE.setListSpotsParques("ParqueESSLEI",listaSpotsParqueESSLEI);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
        // Data bind GridView with ArrayAdapter (String Array elements)

        if(wel == 2){
                showErrorMessage(R.string.welcomeMessage);
        }

        if(bye == 3){
            showErrorMessage(R.string.goodByeMessage);
        }
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                Toast.makeText(DashboardActivity.this, "" + (position + 1), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private Boolean getSharedPreferencesData() {
        preferences = getSharedPreferences(LoginActivity.PREFS_NAME, MODE_PRIVATE);
        Boolean b = false;
        if (preferences.contains("pref_check")) {
            b = preferences.getBoolean("pref_check", false);
        }
        return b;
    }


    public void makeLogin(View view) {
        startActivity(LoginActivity.getIntent(this,1));
        finish();
    }

    public void makeRegist(View view) {
        startActivity(RegisterActivity.getIntent(this));
        finish();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;
        listadefaultParques = ParquesManager.INSTANCE.getDefaultParques();
        for (Parque parque:listadefaultParques){
            LatLng locationParque = new LatLng(parque.getCoordenadaX(), parque.getCoordenadaY());
            mMap.addMarker(new MarkerOptions().position(locationParque).title(parque.getNome()));
        }
        // Add a marker in Sydney and move the camera

        mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(listadefaultParques.get(0).getCoordenadaX(),listadefaultParques.get(0).getCoordenadaY())));
        CameraUpdate yourLocation = CameraUpdateFactory.newLatLngZoom(new LatLng(listadefaultParques.get(0).getCoordenadaX(),listadefaultParques.get(0).getCoordenadaY()), 18);
        mMap.animateCamera(yourLocation);

    }

    public static Intent getIntent(Context context, int wel) {
        Intent intent = new Intent(context, DashboardActivity.class);
        intent.putExtra("WELCOME", 0);
        return intent;
    }

    public static Intent getIntentLogout(Context context, int wel) {
        Intent intent = new Intent(context, DashboardActivity.class);
        intent.putExtra("BYE", 3);
        return intent;
    }

    private void showErrorMessage(int message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle(message);

        builder.setNeutralButton(R.string.OK, null);

        builder.show();
    }

}