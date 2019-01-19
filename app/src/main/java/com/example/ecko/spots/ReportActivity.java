package com.example.ecko.spots;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.ecko.spots.model.IncidentReport;
import com.example.ecko.spots.model.ParquesManager;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class ReportActivity extends AppCompatActivity {

    private RadioButton naturalCause, wrongUse;
    private EditText decription;
    private ImageView imageView;
    private String typeStr = "";
    private String spot;
    private String parque;
    private String descriprionStr = null;
    private Uri filePath;
    private byte[] data;
    private int cameraType; //0 -> camera & 1 -> galeria

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);

        naturalCause = findViewById(R.id.radioBtnNaturalIncident);
        wrongUse = findViewById(R.id.radioBtnWrongUse);
        decription = findViewById(R.id.txtIncidentDescription);

        cameraType = 2;

        final ArrayAdapter<CharSequence> adapterParques = ArrayAdapter.createFromResource(this, R.array.park_array, android.R.layout.simple_spinner_item);
        adapterParques.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        final Spinner spinnerParques = findViewById(R.id.spinnerParques);
        spinnerParques.setAdapter(adapterParques);

        final Spinner spinnerSpot = findViewById(R.id.spinnerSpot);

        spinnerParques.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int pos, long l) {
                adapterView.getItemAtPosition(pos);
                if (pos == 0) {
                    ArrayAdapter<CharSequence> adapterSpots = ArrayAdapter.createFromResource(ReportActivity.this, R.array.parqueD_spots, android.R.layout.simple_spinner_item);
                    adapterParques.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinnerSpot.setAdapter(adapterSpots);
                } else if (pos == 1) {
                    ArrayAdapter<CharSequence> adapterSpots = ArrayAdapter.createFromResource(ReportActivity.this, R.array.parqueA_spots, android.R.layout.simple_spinner_item);
                    adapterParques.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinnerSpot.setAdapter(adapterSpots);
                } else if (pos == 2) {
                    ArrayAdapter<CharSequence> adapterSpots = ArrayAdapter.createFromResource(ReportActivity.this, R.array.parqueESSLEI_spots, android.R.layout.simple_spinner_item);
                    adapterParques.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinnerSpot.setAdapter(adapterSpots);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        ArrayAdapter<CharSequence> adapterSpots = ArrayAdapter.createFromResource(this, R.array.parqueA_spots, android.R.layout.simple_spinner_item);
        adapterParques.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerSpot.setAdapter(adapterSpots);


        //Upload Photo
        Button btnUploadPhoto = findViewById(R.id.btnUploadPhoto);
        btnUploadPhoto.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {

                AlertDialog.Builder builder = new AlertDialog.Builder(ReportActivity.this);
                builder.setMessage("Choose a photo from")
                        .setCancelable(true)
                        .setNegativeButton("Gallery", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Intent pickPhoto = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                                startActivityForResult(pickPhoto , 1);
                            }
                        })
                        .setPositiveButton("Camera", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Intent takePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                startActivityForResult(takePicture, 0);
                            }
                        });
                builder.show();
            }
        });


        //Submit Report
        Button btnReportSubmmit = findViewById(R.id.btnReport);
        btnReportSubmmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Type
                if (naturalCause.isChecked()){
                    typeStr = "Natural Incident";

                    //Descriprion
                    if (decription.getText().length() < 1){
                        showErrorMessage(R.string.insertDescription);
                    }else{
                        descriprionStr = decription.getText().toString();

                        //Parque
                        parque = spinnerParques.getSelectedItem().toString();

                        //Spot
                        spot = spinnerSpot.getSelectedItem().toString();

                        IncidentReport report = new IncidentReport(spot, parque, descriprionStr, typeStr);

                        String ref =  FirebaseDatabase.getInstance().getReference("Reports").push().getKey();

                        FirebaseDatabase.getInstance().getReference("Reports").child(ref).setValue(report);

                        if (cameraType == 0){
                            FirebaseStorage.getInstance().getReference(ref).putBytes(data);
                        }else if (cameraType == 1){
                            FirebaseStorage.getInstance().getReference(ref).putFile(filePath);
                        }

                        showErrorMessage(R.string.reportSubmitted);
                        startActivity(AuthenticatedDashboardActivity.getIntent(ReportActivity.this, 6));
                        finish();
                    }
                }else if (wrongUse.isChecked()){
                    typeStr = "Wrong use of Spot";

                    //Descriprion
                    if (decription.getText().length() < 1){
                        showErrorMessage(R.string.insertDescription);
                    }else{
                        descriprionStr = decription.getText().toString();

                        //Parque
                        parque = spinnerParques.getSelectedItem().toString();

                        //Spot
                        spot = spinnerSpot.getSelectedItem().toString();

                        IncidentReport report = new IncidentReport(spot, parque, descriprionStr, typeStr);

                        String ref =  FirebaseDatabase.getInstance().getReference("Reports").push().getKey();

                        FirebaseDatabase.getInstance().getReference("Reports").child(ref).setValue(report);

                        if (cameraType == 0){
                            FirebaseStorage.getInstance().getReference(ref).putBytes(data);
                        }else if (cameraType == 1){
                            FirebaseStorage.getInstance().getReference(ref).putFile(filePath);
                        }

                        showErrorMessage(R.string.reportSubmitted);
                        startActivity(AuthenticatedDashboardActivity.getIntent(ReportActivity.this, 6));
                        finish();
                    }
                }else{
                    showErrorMessage(R.string.insertType);
                }
            }
        });

        //Cancel
        Button btnCancelReport = findViewById(R.id.btnCancelReport);
        btnCancelReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(AuthenticatedDashboardActivity.getIntent(ReportActivity.this, 6));
                finish();
            }
        });
    }

    public static Intent getIntent(Context context) {
        Intent intent = new Intent(context, ReportActivity.class);
        return intent;
    }

    private void showErrorMessage(int message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(message);
        builder.setNeutralButton(R.string.OK, null);
        builder.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);
        imageView = findViewById(R.id.imageView);

        if(resultCode == RESULT_OK){
            switch(requestCode) {
                case 0:
                    Bitmap bitmap = (Bitmap) imageReturnedIntent.getExtras().get("data");
                    imageView.setImageBitmap(bitmap);

                    cameraType = 0;
                    bitmap = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                    data = baos.toByteArray();
                    break;
                case 1:
                    cameraType = 1;
                    filePath = imageReturnedIntent.getData();
                    Uri selectedImage = imageReturnedIntent.getData();
                    Bitmap bitmapg = null;
                    try {
                        bitmapg = android.provider.MediaStore.Images.Media.getBitmap(getContentResolver(), selectedImage);
                        imageView.setImageURI(selectedImage);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;
            }
        }

    }




}
