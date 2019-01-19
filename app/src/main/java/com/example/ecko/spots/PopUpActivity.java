package com.example.ecko.spots;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.RatingBar;

import com.example.ecko.spots.model.ParquesManager;
import com.example.ecko.spots.model.Spot;
import com.example.ecko.spots.model.SpotsManager;
import com.google.android.gms.dynamic.IFragmentWrapper;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;

public class PopUpActivity extends AppCompatActivity {
    private RatingBar ratingBarPopUp;
    private CheckBox checkBoxFavorite;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pop_up);
         ratingBarPopUp = findViewById(R.id.ratingBarPopUp);
         checkBoxFavorite = findViewById(R.id.checkBoxPopUp);

        final FirebaseUser userAuth = FirebaseAuth.getInstance().getCurrentUser();
        FirebaseDatabase.getInstance().getReference("Users/" + userAuth.getUid() + "/MySpot").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                Iterable<DataSnapshot> nomeMyspot = snapshot.getChildren();
                for (final DataSnapshot spotData : nomeMyspot) {
                    String[] locationMySpotFirebase = spotData.getKey().split("\\s+");
                    FirebaseDatabase.getInstance().getReference("Users").child(userAuth.getUid()).child("favouriteSpots").child(locationMySpotFirebase[0]+" "+locationMySpotFirebase[1]).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot snapshot){
                            if (snapshot.exists() && checkBoxFavorite != null) {
                                checkBoxFavorite.setChecked(true);
                            }
                        }
                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                        }
                    });
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        if (checkBoxFavorite != null){
            checkBoxFavorite.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final FirebaseUser userAuth = FirebaseAuth.getInstance().getCurrentUser();
                    FirebaseDatabase.getInstance().getReference("Users/" + userAuth.getUid() + "/MySpot").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot snapshot) {
                            Iterable<DataSnapshot> nomeMyspot = snapshot.getChildren();
                            for (DataSnapshot spotData : nomeMyspot) {
                                String[] locationMySpotFirebase = spotData.getKey().split("\\s+");
                                Spot spot = spotData.getValue(Spot.class);
                                if (checkBoxFavorite.isChecked()){
                                    FirebaseDatabase.getInstance().getReference("Users").child(userAuth.getUid()).child("favouriteSpots").child(locationMySpotFirebase[0]+" "+locationMySpotFirebase[1]).setValue(spot);
                                }else {
                                    FirebaseDatabase.getInstance().getReference("Users").child(userAuth.getUid()).child("favouriteSpots").child(locationMySpotFirebase[0]+" "+locationMySpotFirebase[1]).removeValue();
                                }
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                        }
                    });
                }
            });
        }
        if (ratingBarPopUp != null){
            ratingBarPopUp.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
                public void onRatingChanged(final RatingBar ratingBar, float rating, boolean fromUser) {
                    final FirebaseUser userAuth = FirebaseAuth.getInstance().getCurrentUser();
                    FirebaseDatabase.getInstance().getReference("Users/" + userAuth.getUid() + "/MySpot").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot snapshot) {
                            Iterable<DataSnapshot> nomeMyspot = snapshot.getChildren();
                            for (DataSnapshot spotData : nomeMyspot) {
                                String[] locationMySpotFirebase = spotData.getKey().split("\\s+");
                                if (ratingBar.getRating() != 0.0){
                                    ratingBar.getRating();
                                    FirebaseDatabase.getInstance().getReference("Users/" + userAuth.getUid() + "/MySpot").child(locationMySpotFirebase[0]+" "+locationMySpotFirebase[1]).child("rating").setValue(ratingBar.getRating());
                                    FirebaseDatabase.getInstance().getReference("Spots").child(locationMySpotFirebase[0]).child(locationMySpotFirebase[1]).child("rating").setValue(ratingBar.getRating());
                                    if (checkBoxFavorite.isChecked()){
                                        FirebaseDatabase.getInstance().getReference("Users").child(userAuth.getUid()).child("favouriteSpots").child(locationMySpotFirebase[0]+" "+locationMySpotFirebase[1]).child("rating").setValue(ratingBar.getRating());
                                    }
                                }
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                        }
                    });
                }
            });
        }
    }

    public static Intent getIntent(Context context, String nomeParqueSelecionado) {
        Intent intent = new Intent(context, PopUpActivity.class);
        intent.putExtra("parque",nomeParqueSelecionado);
        return intent;
    }

    public void onCancel(View view) {
        startActivity(AuthenticatedDashboardActivity.getIntent(PopUpActivity.this, 6));
        finish();
    }

    public void onSave(View view) {
        ratingBarPopUp = findViewById(R.id.ratingBarPopUp);
        if (ratingBarPopUp.getRating() < 0.1){
            showMessage(R.string.ratePlease);
        }else{
            final FirebaseUser userAuth = FirebaseAuth.getInstance().getCurrentUser();
            FirebaseDatabase.getInstance().getReference("Users/" + userAuth.getUid() + "/MySpot").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot snapshot) {
                    Iterable<DataSnapshot> nomeMyspot = snapshot.getChildren();
                    for (DataSnapshot spotData : nomeMyspot) {
                        String[] locationMySpotFirebase = spotData.getKey().split("\\s+");
                        FirebaseDatabase.getInstance().getReference("Spots").child(locationMySpotFirebase[0]).child(locationMySpotFirebase[1]).child("estado").setValue(0);
                        FirebaseDatabase.getInstance().getReference("Spots").child(locationMySpotFirebase[0]).child(locationMySpotFirebase[1]).child("ocupado").setValue(false);
                        //FirebaseDatabase.getInstance().getReference("Users").child(userAuth.getUid()).child("favouriteSpots").child(locationMySpotFirebase[0]+" "+locationMySpotFirebase[1]).child("estado").setValue(0);
                        //FirebaseDatabase.getInstance().getReference("Users").child(userAuth.getUid()).child("favouriteSpots").child(locationMySpotFirebase[0]+" "+locationMySpotFirebase[1]).child("ocupado").setValue(false);
                        FirebaseDatabase.getInstance().getReference("Users/" + userAuth.getUid() + "/MySpot").removeValue();
                    }
                    startActivity(AuthenticatedDashboardActivity.getIntent(PopUpActivity.this, 10));
                    finish();
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                }
            });
        }
    }

    private void showMessage(int message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(message);
        builder.setNeutralButton(R.string.OK, null);
        builder.show();
    }
}
