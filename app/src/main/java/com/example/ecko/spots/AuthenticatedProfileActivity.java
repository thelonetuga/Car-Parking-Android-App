package com.example.ecko.spots;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.example.ecko.spots.model.Spot;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Pattern;

public class AuthenticatedProfileActivity extends AppCompatActivity {

    private FirebaseAuth base;
    private FirebaseUser auth;
    private Button saveProfile;
    private EditText inputMyEmail, inputNewPassword, inputNewPasswordConfirmation;
    private RadioGroup checkboxPreferences;
    private RadioButton closest, qualification, favs;
    private ListView favouriteSpots;
    private String preference;
    private TextView inputOldPassword;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authenticated_profile);


        //Get Firebase auth instance
        auth = FirebaseAuth.getInstance().getCurrentUser();
        mDatabase = FirebaseDatabase.getInstance().getReference("Users");

        inputMyEmail = findViewById(R.id.inputMyEmail);
        inputNewPassword = findViewById(R.id.inputNewPassword);
        inputNewPasswordConfirmation = findViewById(R.id.inputNewPasswordConfirmation);
        inputOldPassword = findViewById(R.id.inputOldPassword);
        checkboxPreferences = findViewById(R.id.checkboxPreferences);
        closest = findViewById(R.id.closest);
        qualification = findViewById(R.id.qualification);
        favs = findViewById(R.id.favs);

        final List<String> spots = new ArrayList<>();
        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, spots);
        favouriteSpots = findViewById(R.id.favouriteSpots);

        final List<String> emailsDatabase = new ArrayList<>();

        FirebaseDatabase.getInstance().getReference("Users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                DataSnapshot emailSnapshot = snapshot;
                Iterable<DataSnapshot> emailChildren = emailSnapshot.getChildren();

                for (DataSnapshot emailData : emailChildren) {
                    if (emailData.child("email").exists()) {
                        String email = emailData.child("email").getValue().toString();
                        emailsDatabase.add(email);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        FirebaseDatabase.getInstance().getReference("Users").child(auth.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                spots.clear();
                Iterable<DataSnapshot> favoriteChildren = dataSnapshot.child("favouriteSpots").getChildren();
                for (DataSnapshot spotData : favoriteChildren) {
                    String spot = spotData.getKey();
                    System.out.println(spot);
                    spots.add(spot);
                }

                favouriteSpots.setAdapter(arrayAdapter);

                favouriteSpots.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        //buscar que spot clicou
                        String selectedItem = spots.get(position);
                        //delete spot da listview e da lista
                        spots.remove(position);
                        arrayAdapter.notifyDataSetChanged();

                        mDatabase.child(auth.getUid()).child("favouriteSpots").child(selectedItem).removeValue();
                        //msg de exito
                        showErrorMessage(R.string.spotDeleted);
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        FirebaseDatabase.getInstance().getReference("Users").child(auth.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                inputMyEmail.setText(snapshot.child("email").getValue().toString());
                inputOldPassword.setText(snapshot.child("password").getValue().toString());

                String pref = snapshot.child("preference").getValue().toString();
                if (pref.equals(closest.getText().toString())) {
                    closest.setChecked(true);
                } else if (pref.equals(qualification.getText().toString())) {
                    qualification.setChecked(true);
                } else {
                    favs.setChecked(true);
                }


                saveProfile = findViewById(R.id.saveProfile);

                saveProfile.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        String email = inputMyEmail.getText().toString().trim();
                        String newPassword = inputNewPassword.getText().toString().trim();
                        String newPasswordConfirmation = inputNewPasswordConfirmation.getText().toString().trim();
                        if (closest.isChecked()) {
                            preference = closest.getText().toString();
                        } else if (qualification.isChecked()) {
                            preference = qualification.getText().toString();
                        } else {
                            preference = favs.getText().toString();
                        }

                        //email invalido
                        if (!validEmail(email) || email.isEmpty()) {
                            showErrorMessage(R.string.wrongEmailFormat);
                            return;
                        }

                        //not same password
                        if (!(newPassword.equals(newPasswordConfirmation)) && !newPasswordConfirmation.isEmpty()) {
                            showErrorMessage(R.string.insertSamePass);
                            return;
                        }

                        //dont introduce pass conf
                        if (newPassword.length() != 0 && newPasswordConfirmation.length() == 0) {
                            showErrorMessage(R.string.insertPassConf);
                            return;
                        }

                        //pass with wrong parameters
                        if (newPassword.length() != 0 && newPassword.length() < 8) {
                            showErrorMessage(R.string.wrongFormatPass);
                            return;
                        }
                        Log.w("myApp", "EMAILS " + emailsDatabase.toString());


                        //email em uso
                        emailsDatabase.remove(auth.getEmail());
                        //Log.w("myApp", "UPDATE " + emailsDatabase.toString());
                        for (String mail : emailsDatabase) {
                            if (mail.equals(email)) {
                                showErrorMessage(R.string.alreadyEmailExist);
                                return;
                            }
                        }

                        //update perfil
                        if (newPassword.length() > 7) { //se ele mudou a pass
                            auth.updatePassword(newPassword).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                }
                            });
                            mDatabase.child(auth.getUid()).child("password").setValue(newPassword);
                        }


                        auth.updateEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                            }


                        });

                        mDatabase.child(auth.getUid()).child("email").setValue(email);

                        mDatabase.child(auth.getUid()).child("preference").setValue(preference);

                        mDatabase.child(auth.getUid()).child("autenticado").setValue(true).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                showErrorMessage(R.string.profileUpdated);
                            }
                        });
                        finish();
                    }
                });
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }


    public void onClickLogout(View view) {
        mDatabase.child(auth.getUid()).child("autenticado").setValue(false);
        FirebaseAuth.getInstance().signOut();
        startActivity(DashboardActivity.getIntentLogout(AuthenticatedProfileActivity.this, 3));
        finish();
    }

    private boolean validEmail(String email) {
        Pattern pattern = Patterns.EMAIL_ADDRESS;
        return pattern.matcher(email).matches();
    }


    private void showErrorMessage(int message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(message);
        builder.setNeutralButton(R.string.OK, null);
        builder.show();
    }


    public static Intent getIntent(Context context) {
        Intent intent = new Intent(context, AuthenticatedProfileActivity.class);
        return intent;
    }
}