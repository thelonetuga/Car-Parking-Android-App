package com.example.ecko.spots;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.ecko.spots.model.ParquesManager;
import com.example.ecko.spots.model.Spot;
import com.example.ecko.spots.model.SpotsManager;
import com.example.ecko.spots.model.User;
import com.example.ecko.spots.model.UserManager;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.LinkedList;
import java.util.regex.Pattern;

import static android.widget.Toast.LENGTH_LONG;


public class RegisterActivity extends AppCompatActivity {

    private FirebaseAuth auth;
    private Button btnRegist, btnCancel;
    private EditText inputEmail, inputPassword, inputPasswordConfirmation;
    private boolean userCreated;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        disableAutoFill();

        //Get Firebase auth instance
        auth = FirebaseAuth.getInstance();
        btnRegist = findViewById(R.id.btnRegist);
        inputPassword = findViewById(R.id.inputPassword);
        inputEmail = findViewById(R.id.inputEmail);
        btnCancel = findViewById(R.id.btnCancel);
        inputPasswordConfirmation = findViewById(R.id.inputPasswordConfirmation);

        btnRegist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String email = inputEmail.getText().toString().trim();
                final String password = inputPassword.getText().toString().trim();
                String passwordConfirmation = inputPasswordConfirmation.getText().toString().trim();

                //email vazio
                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(getApplicationContext(), "Enter email address!", Toast.LENGTH_SHORT).show();
                    return;
                }

                //password vazia
                if (TextUtils.isEmpty(password)) {
                    Toast.makeText(getApplicationContext(), "Enter password!", Toast.LENGTH_SHORT).show();
                    return;
                }

                //password e email invalido
                if (password.length() < 8 && !validEmail(email)) {
                    showErrorMessage(R.string.invalidCredentials);
                    return;
                }

                //password invalida
                if (password.length() < 8){
                    showErrorMessage(R.string.invalidPassword);
                    return;
                }

                //password e password de confirmaçao diferentes
                if (!password.equals(passwordConfirmation)){
                    showErrorMessage(R.string.differentPasswords);
                    return;
                }

                //email invalido
                if (!validEmail(email)){
                    showErrorMessage(R.string.wrongEmailFormat);
                    return;
                }


                auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            User user = new User(email, false, "closest", password ,null, null);
                            FirebaseDatabase.getInstance().getReference("Users").child(auth.getCurrentUser().getUid()).setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()){
                                        showErrorMessageOtherActivity(R.string.accountCreated);
                                    }
                                }
                            });
                        } else {
                            if(task.getException().getMessage().equals("The email address is already in use by another account.")) {
                                showErrorMessage(R.string.alreadyEmailExist);
                        }
                        }
                    }
                });
            }
        });
    }

    private void showErrorMessage(int message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(message);
        builder.setNeutralButton(R.string.OK, null);
        builder.show();
    }


    private boolean validEmail(String email) {
        Pattern pattern = Patterns.EMAIL_ADDRESS;
        return pattern.matcher(email).matches();
    }

    private void showErrorMessageOtherActivity(int errorMessage) {
        AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
        builder.setMessage(errorMessage)
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //do things
                        startActivity(LoginActivity.getIntent(RegisterActivity.this, -1));
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }

    public static Intent getIntent(Context context){
        Intent intent = new Intent(context, RegisterActivity.class);
        return intent;
    }


    public void cancelRegist(View view) {
        startActivity(DashboardActivity.getIntent(RegisterActivity.this, -1));
    }

    @TargetApi(Build.VERSION_CODES.O)
    private void disableAutoFill() {
        getWindow().getDecorView().setImportantForAutofill(View.IMPORTANT_FOR_AUTOFILL_NO_EXCLUDE_DESCENDANTS);
    }
}
