package com.example.ecko.spots;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;

import com.anychart.AnyChart;
import com.anychart.AnyChartView;
import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.chart.common.dataentry.ValueDataEntry;
import com.anychart.charts.Pie;
import com.example.ecko.spots.model.Spot;
import com.example.ecko.spots.model.SpotsManager;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.example.ecko.spots.model.UserManager;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StatisticsActivity extends AppCompatActivity {

    public static final String inputFormat = "HH:mm";
    private RadioButton general, byPark, betweenHours;
    private EditText startHour, endHour;
    private TextView to, piorSpot, melhorSpot, numberRegisted, tempoMedioSugestao;
    private Button ver;
    private Date dateCompareOne;
    private Date dateCompareTwo;
    private Date date;
    private Pattern pattern;
    private Matcher matcher;
    private static final String TIME24HOURS_PATTERN =
            "([01]?[0-9]|2[0-3]):[0-5][0-9]";

    SimpleDateFormat inputParser = new SimpleDateFormat(inputFormat, Locale.UK);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);
        ver = findViewById(R.id.ver);

        //final AnyChartView anyChartView = (AnyChartView) findViewById(R.id.viewChart);
        //final Pie pie2 = AnyChart.pie();

        pattern = Pattern.compile(TIME24HOURS_PATTERN);

        ver=findViewById(R.id.ver);

        general = findViewById(R.id.general);
        byPark = findViewById(R.id.byPark);
        betweenHours = findViewById(R.id.betweenHours);
        final AnyChartView anyChartView = (AnyChartView) findViewById(R.id.viewChart);
        final Pie pie2 = AnyChart.pie();

        startHour = findViewById(R.id.startHour);
        startHour.setEnabled(false);
        endHour = findViewById(R.id.endHour);
        endHour.setEnabled(false);
        to = findViewById(R.id.to);
        to.setEnabled(false);

        piorSpot=findViewById(R.id.piorSpot);
        piorSpot.setVisibility(View.INVISIBLE);
        melhorSpot=findViewById(R.id.melhorSpot);
        melhorSpot.setVisibility(View.INVISIBLE);

        numberRegisted=findViewById(R.id.numberRegisted);
        numberRegisted.setVisibility(View.INVISIBLE);
        tempoMedioSugestao=findViewById(R.id.tempoMedioSugestao);
        tempoMedioSugestao.setVisibility(View.INVISIBLE);

        final ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.statistics_type, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        final Spinner spinnerStats = findViewById(R.id.spinnerStatisticsType);
        spinnerStats.setAdapter(adapter);

        final ArrayAdapter<CharSequence> adapterParques = ArrayAdapter.createFromResource(this, R.array.park_array, android.R.layout.simple_spinner_item);
        adapterParques.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        final Spinner spinnerFilterParques = findViewById(R.id.spinnerParks);
        spinnerFilterParques.setAdapter(adapterParques);
        spinnerFilterParques.setEnabled(false);

        final List<DataEntry> dataParqueA = new ArrayList<>();
        final List<DataEntry> dataParqueD = new ArrayList<>();
        final List<DataEntry> dataParqueESSLEI = new ArrayList<>();
        final List<DataEntry> dataGeneralParque = new ArrayList<>();
        final List<DataEntry> dataParqueByHour = new ArrayList<>();

        FirebaseDatabase.getInstance().getReference("TaxaOcupacao").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                Iterable<DataSnapshot> parques = snapshot.getChildren();
                for (DataSnapshot spotData : parques) {
                    if (spotData.getKey().equals("ParqueA")) {
                        dataParqueA.add(new ValueDataEntry("Nao Ocupado", spotData.child("Nao Ocupado").getValue(Float.class)));
                        dataParqueA.add(new ValueDataEntry("Ocupado", spotData.child("Ocupado").getValue(Float.class)));
                        dataGeneralParque.add(new ValueDataEntry("ParqueA Nao Ocupado", spotData.child("Nao Ocupado").getValue(Float.class)));
                        dataGeneralParque.add(new ValueDataEntry("ParqueA Ocupado", spotData.child("Ocupado").getValue(Float.class)));

                    } else if (spotData.getKey().equals("ParqueD")) {
                        dataParqueD.add(new ValueDataEntry("Nao Ocupado", spotData.child("Nao Ocupado").getValue(Float.class)));
                        dataParqueD.add(new ValueDataEntry("Ocupado", spotData.child("Ocupado").getValue(Float.class)));
                        dataGeneralParque.add(new ValueDataEntry("ParqueD Nao Ocupado", spotData.child("Nao Ocupado").getValue(Float.class)));
                        dataGeneralParque.add(new ValueDataEntry("ParqueD Ocupado", spotData.child("Ocupado").getValue(Float.class)));

                    } else if (spotData.getKey().equals("ParqueESSLEI")) {
                        dataParqueESSLEI.add(new ValueDataEntry("Nao Ocupado", spotData.child("Nao Ocupado").getValue(Float.class)));
                        dataParqueESSLEI.add(new ValueDataEntry("Ocupado", spotData.child("Ocupado").getValue(Float.class)));
                        dataGeneralParque.add(new ValueDataEntry("ParqueESSLEI Nao Ocupado", spotData.child("Nao Ocupado").getValue(Float.class)));
                        dataGeneralParque.add(new ValueDataEntry("ParqueESSLEI Ocupado", spotData.child("Ocupado").getValue(Float.class)));
                    }


                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });



        //CALCULAR ESTATISTICAS GERAIS
        general.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (spinnerFilterParques.isEnabled()) {
                    spinnerFilterParques.setEnabled(false);
                }
                if (startHour.isEnabled()) {
                    startHour.setEnabled(false);
                    endHour.setEnabled(false);
                    to.setEnabled(false);
                }
            }
        });

        //CALCULAR ESTATISTICAS POR PARQUE
        byPark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (startHour.isEnabled()) {
                    startHour.setEnabled(false);
                    endHour.setEnabled(false);
                    to.setEnabled(false);
                }
                spinnerFilterParques.setEnabled(true);
            }
        });

        //CALCULAR ESTATISTICAS ENTRE HORAS
        betweenHours.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (spinnerFilterParques.isEnabled()) {
                    spinnerFilterParques.setEnabled(false);
                }
                startHour.setEnabled(true);
                endHour.setEnabled(true);
                to.setEnabled(true);

            }
        });

        ver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nomeEstatistica = spinnerStats.getSelectedItem().toString();
                if (!general.isChecked() && !byPark.isChecked() && !betweenHours.isChecked()) {
                    showErrorMessage(R.string.selectAType);
                    return;
                }
                //GENERAL
                if(general.isChecked()){
                    if(nomeEstatistica.equals("Rate")){

                        //ESTATISTICAS GERAIS DE RATE: best and worst spot with rate

                        List<Spot> listaSpotD = SpotsManager.INSTANCE.getListSpotD();
                        List<Spot> listaSpotEsslei = SpotsManager.INSTANCE.getListSpotESSLEI();

                        Spot piorSpotGeral = new Spot();

                        Spot piorSpotD = new Spot();
                        Spot piorSpotESSLEI = new Spot();

                        Spot melhorSpotGeral = new Spot();

                        Spot melhorSpotD = new Spot();
                        Spot melhorSpotESSLEI = new Spot();


                        piorSpotD.setRating(6);
                        piorSpotESSLEI.setRating(6);


                        melhorSpotD.setRating(-1);
                        melhorSpotESSLEI.setRating(-1);

                        List<Spot> listaSpotA = SpotsManager.INSTANCE.getListSpotA();
                        Spot piorSpotA = new Spot();
                        Spot melhorSpotA = new Spot();
                        melhorSpotA.setRating(-1);
                        piorSpotA.setRating(6);
                        for (Spot spot : listaSpotA) {
                            if (spot.getRating() < piorSpotA.getRating()) {
                                piorSpotA = spot;
                            }
                            if (spot.getRating() > melhorSpotA.getRating()) {
                                melhorSpotA = spot;
                            }
                        }

                        for (Spot spot : listaSpotD) {
                            if (spot.getRating() < piorSpotD.getRating()) {
                                piorSpotD = spot;
                            }
                            if (spot.getRating() > melhorSpotD.getRating()) {
                                melhorSpotD = spot;
                            }
                        }

                        for (Spot spot : listaSpotEsslei) {
                            if (spot.getRating() < piorSpotESSLEI.getRating()) {
                                piorSpotESSLEI = spot;
                            }
                            if (spot.getRating() > melhorSpotESSLEI.getRating()) {
                                melhorSpotESSLEI = spot;
                            }
                        }

                        if (melhorSpotESSLEI.getRating() >= melhorSpotA.getRating() && melhorSpotESSLEI.getRating() >= melhorSpotD.getRating()) {
                            melhorSpotGeral = melhorSpotESSLEI;
                        } else if (melhorSpotA.getRating() >= melhorSpotESSLEI.getRating() && melhorSpotA.getRating() >= melhorSpotD.getRating()) {
                            melhorSpotGeral = melhorSpotA;
                        } else {
                            melhorSpotGeral = melhorSpotD;
                        }
                        melhorSpot.setText("Melhor spot do campus:" + melhorSpotGeral.toString());
                        melhorSpot.setVisibility(View.VISIBLE);

                        if (piorSpotESSLEI.getRating() <= piorSpotA.getRating() && piorSpotESSLEI.getRating() <= piorSpotD.getRating()) {
                            piorSpotGeral = piorSpotESSLEI;
                        } else if (piorSpotA.getRating() <= piorSpotESSLEI.getRating() && piorSpotA.getRating() <= piorSpotD.getRating()) {
                            piorSpotGeral = piorSpotA;
                        } else {
                            piorSpotGeral = piorSpotA;
                        }
                        piorSpot.setText("Pior spot do campus:" + piorSpotGeral.toString());
                        piorSpot.setVisibility(View.VISIBLE);
                    } else if (nomeEstatistica.equals("Ranking")) {
                        //TO DO: ESTATISTICAS GERAIS DE RANKING
                        pie2.data(dataGeneralParque);
                        pie2.title("General Ranking Statistics");
                    }else if (nomeEstatistica.equals("App")){
                        //ESTATISTICAS GERAIS DE APP

                        long numeroUsers = UserManager.INSTANCE.getNumeroUsers();

                        numberRegisted.setText("Número de users registados na app: " + numeroUsers);
                        numberRegisted.setVisibility(View.VISIBLE);

                        final List<DataEntry> dataMediaApp = new ArrayList<>();
                        FirebaseDatabase.getInstance().getReference("TempoMedioApp").addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                final long[] countTotalRegistosClosest = {0};
                                final long[] countTotalQualification = {0};
                                final long[] countTotalFavourites = {0};
                                final long[] countClosest = {0};
                                final long[] countQualification = {0};
                                final long[] countFavourites = {0};
                                Iterable<DataSnapshot> preferencias = dataSnapshot.getChildren();
                                for (DataSnapshot prefData : preferencias) {
                                    Iterable<DataSnapshot> registos = prefData.getChildren();
                                    if (prefData.getKey().equals("Closest")) {
                                        countTotalRegistosClosest[0] = countTotalRegistosClosest[0] + prefData.getChildrenCount();
                                    }
                                    if (prefData.getKey().equals("Favourites")) {
                                        countTotalFavourites[0] = countTotalFavourites[0] + prefData.getChildrenCount();
                                    }
                                    if (prefData.getKey().equals("Qualification")) {
                                        countTotalQualification[0] = countTotalQualification[0] + prefData.getChildrenCount();
                                    }
                                    for (DataSnapshot registo : registos) {
                                        System.out.println(prefData.getKey());
                                        System.out.println(registo.getValue());
                                        if (prefData.getKey().equals("Closest")) {
                                            countClosest[0] =registo.getValue(Long.class) +  countClosest[0];
                                        }
                                        if (prefData.getKey().equals("Qualification")) {
                                            countQualification[0]= registo.getValue(Long.class) + countQualification[0];
                                        }
                                        if (prefData.getKey().equals("Favourites")) {
                                            countFavourites[0]=registo.getValue(Long.class) + countFavourites[0];
                                        }
                                    }
                                }
                                long mediaClosest = countClosest[0]/countTotalRegistosClosest[0];
                                long mediaQualification = countQualification[0]/ countTotalQualification[0];
                                long mediaFavourites = 0;

                                dataMediaApp.add(new ValueDataEntry("Closest", mediaClosest));
                                dataMediaApp.add(new ValueDataEntry("Qualification", mediaQualification));
                                dataMediaApp.add(new ValueDataEntry("Favourites", mediaFavourites));
                                //pie2.data(dataMediaApp);
                                long mediaTotal = (mediaClosest+ mediaQualification+mediaFavourites)/3;
                                tempoMedioSugestao.setText("Tempo médio que a app leva a sugerir spot: "+mediaTotal+" segundos");
                                tempoMedioSugestao.setVisibility(View.VISIBLE);
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    }else{
                        //TO DO: ESTATISTICAS GERAIS DE REPORT
                    }
                } else if (byPark.isChecked()) {
                    //BY PARK
                    String nomeParque = spinnerFilterParques.getSelectedItem().toString();
                    if(nomeEstatistica.equals("Rate")){
                        if(nomeParque.equals("Parque D")){
                            //STATISTICAS DE RATE BY PARQUE D: best and worst spot with rate
                            List<Spot> listaSpotD = SpotsManager.INSTANCE.getListSpotD();
                            Spot piorSpotD = new Spot();
                            Spot melhorSpotD = new Spot();
                            melhorSpotD.setRating(-1);
                            piorSpotD.setRating(6);
                            for (Spot spot : listaSpotD) {
                                if (spot.getRating() < piorSpotD.getRating()) {
                                    piorSpotD = spot;
                                }
                                if (spot.getRating() > melhorSpotD.getRating()) {
                                    melhorSpotD = spot;
                                }
                            }

                            melhorSpot.setText("Melhor spot do Parque D:" + melhorSpotD.toString());
                            melhorSpot.setVisibility(View.VISIBLE);

                            piorSpot.setText("Pior spot do Parque D:" + piorSpotD.toString());
                            piorSpot.setVisibility(View.VISIBLE);

                        } else if (nomeParque.equals("Parque A")) {
                            //STATISTICAS DE RATE BY PARQUE A: best and worst spot with rate
                            List<Spot> listaSpotA = SpotsManager.INSTANCE.getListSpotA();
                            Spot piorSpotA = new Spot();
                            Spot melhorSpotA = new Spot();
                            melhorSpotA.setRating(-1);
                            piorSpotA.setRating(6);
                            for (Spot spot : listaSpotA) {
                                if (spot.getRating() < piorSpotA.getRating()) {
                                    piorSpotA = spot;
                                }
                                if (spot.getRating() > melhorSpotA.getRating()) {
                                    melhorSpotA = spot;
                                }
                            }

                            melhorSpot.setText("Melhor spot do Parque ESSLEI:" + melhorSpotA.toString());
                            melhorSpot.setVisibility(View.VISIBLE);

                            piorSpot.setText("Pior spot do Parque ESSLEI:" + piorSpotA.toString());
                            piorSpot.setVisibility(View.VISIBLE);

                        } else {
                            //STATISTICAS DE RATE BY PARQUE ESSLEI: best and worst spot with rate
                            List<Spot> listaSpotESSLEI = SpotsManager.INSTANCE.getListSpotESSLEI();
                            Spot piorSpotESSLEI = new Spot();
                            Spot melhorSpotESSLEI = new Spot();
                            melhorSpotESSLEI.setRating(-1);
                            piorSpotESSLEI.setRating(6);
                            for (Spot spot : listaSpotESSLEI) {
                                if (spot.getRating() < piorSpotESSLEI.getRating()) {
                                    piorSpotESSLEI = spot;
                                }
                                if (spot.getRating() > melhorSpotESSLEI.getRating()) {
                                    melhorSpotESSLEI = spot;
                                }
                            }

                            melhorSpot.setText("Melhor spot do Parque A:" + melhorSpotESSLEI.toString());
                            melhorSpot.setVisibility(View.VISIBLE);

                            piorSpot.setText("Pior spot do Parque A:" + piorSpotESSLEI.toString());
                            piorSpot.setVisibility(View.VISIBLE);
                        }
                    } else if (nomeEstatistica.equals("Ranking")) {
                        if (nomeParque.equals("Parque D")) {
                            //TO DO: ESTATISTICAS DE RANKING BY PARQUE D
                            pie2.data(dataParqueD);
                            pie2.title("Parque D");
                        } else if (nomeParque.equals("Parque A")) {
                            //TO DO: ESTATISTICAS DE RANKING BY PARQUE A
                            pie2.data(dataParqueA);
                            pie2.title("Parque A");
                        } else {
                            //TO DO: ESTATISTICAS DE RANKING BY PARQUE ESSLEI
                            pie2.data(dataParqueESSLEI);
                            pie2.title("Parque Esslei");
                        }
                    }else if (nomeEstatistica.equals("App")){
                            //ESTATISTICAS DE APP BY PARQUE
                        showErrorMessage(R.string.naoSeAplicaFiltro);
                        return;
                    }else{
                        if(nomeParque.equals("Parque D")){
                            //TO DO: ESTATISTICAS DE REPORT BY PARQUE D
                        } else if (nomeParque.equals("Parque A")) {
                            //TO DO: ESTATISTICAS DE REPORT BY PARQUE A
                        } else {
                            //TO DO: ESTATISTICAS DE REPORT BY PARQUE ESSLEI
                        }
                    }
                } else {
                    //BETWEEN HOURS
                    final String start = startHour.getText().toString();
                    final String end = endHour.getText().toString();

                    if (start.isEmpty()) {
                        showErrorMessage(R.string.introduceStartHour);
                        return;
                    }
                    if (end.isEmpty()) {
                        showErrorMessage(R.string.introduceEndHour);
                        return;
                    }
                    if (!validate(start) || !validate(end)) {
                        showErrorMessage(R.string.wrongHourFormart);
                        return;
                    }

                    if (nomeEstatistica.equals("Rate")) {
                        //TO DO: ESTATISTICAS DE RATE ENTRE START E END
                    } else if (nomeEstatistica.equals("Ranking")) {
                        //TO DO: ESTATISTICAS DE RANKING ENTRE START E END
                        dataParqueByHour.clear();
                        FirebaseDatabase.getInstance().getReference("Spots").addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                final long[] countTotalRegistosA = {0};
                                final long[] countRegistosA = {0};
                                final long[] countTotalRegistosD = {0};
                                final long[] countRegistosD = {0};
                                final long[] countTotalRegistosESSLEI = {0};
                                final long[] countRegistosESSLEI = {0};
                                Iterable<DataSnapshot> parques = dataSnapshot.getChildren();
                                for (DataSnapshot parquesData : parques) {
                                    if (parquesData.getKey() != "DEFAULTPARK") {
                                        Iterable<DataSnapshot> spots = parquesData.getChildren();
                                        for (DataSnapshot spotData : spots) {
                                            if (parquesData.getKey().equals("ParqueA")) {
                                                countTotalRegistosA[0] = countTotalRegistosA[0] + spotData.child("Registo de Ocupacao").getChildrenCount();
                                            }
                                            if (parquesData.getKey().equals("ParqueD")) {
                                                countTotalRegistosD[0] = countTotalRegistosD[0] + spotData.child("Registo de Ocupacao").getChildrenCount();
                                            }
                                            if (parquesData.getKey().equals("ParqueESSLEI")) {
                                                countTotalRegistosESSLEI[0] = countTotalRegistosESSLEI[0] + spotData.child("Registo de Ocupacao").getChildrenCount();
                                            }

                                            Iterable<DataSnapshot> registos = spotData.child("Registo de Ocupacao").getChildren();
                                            for (DataSnapshot registo : registos) {
                                                date = parseDate(registo.getValue(String.class));
                                                dateCompareOne = parseDate(start);
                                                dateCompareTwo = parseDate(end);
                                                if (dateCompareOne.before(date) && dateCompareTwo.after(date)) {
                                                    System.out.println(parquesData.getKey());
                                                    System.out.println(registo.getValue());
                                                    if (parquesData.getKey().equals("ParqueA")) {
                                                        countRegistosA[0]++;
                                                    }
                                                    if (parquesData.getKey().equals("ParqueD")) {
                                                        countRegistosD[0]++;
                                                    }
                                                    if (parquesData.getKey().equals("ParqueESSLEI")) {
                                                        countRegistosESSLEI[0]++;
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                                long percParqueA = (countRegistosA[0] * 100) / countTotalRegistosA[0];
                                long percParqueD = (countRegistosD[0] * 100) / countTotalRegistosD[0];
                                long percParqueESSLEI = (countRegistosESSLEI[0] * 100) / countTotalRegistosESSLEI[0];
                                dataParqueByHour.add(new ValueDataEntry("Parque A", percParqueA));
                                dataParqueByHour.add(new ValueDataEntry("Parque D", percParqueD));
                                dataParqueByHour.add(new ValueDataEntry("Parque ESSLEI", percParqueESSLEI));
                                pie2.data(dataParqueByHour);
                            }
                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    } else if (nomeEstatistica.equals("App")) {
                        //TO DO: ESTATISTICAS DE APP ENTRE START E END
                    } else {
                        //TO DO: ESTATISTICAS DE REPORT ENTRE START E END
                    }
                }
            }
        });
        anyChartView.setChart(pie2);
    }

    private Date parseDate(String date) {
        try {
            return inputParser.parse(date);
        } catch (java.text.ParseException e) {
            return new Date(0);
        }
    }

    private void showErrorMessage(int message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(message);
        builder.setNeutralButton(R.string.OK, null);
        builder.show();
    }

    public boolean validate(String time){
        matcher = pattern.matcher(time);
        return matcher.matches();

    }
}
