package com.example.ecko.spots;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.UserManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.example.ecko.spots.model.Parque;
import com.example.ecko.spots.model.ParquesManager;
import com.example.ecko.spots.model.SpotsManager;
import com.example.ecko.spots.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.regex.Pattern;

public class LoginActivity extends AppCompatActivity {

    private FirebaseAuth auth;
    private Button btnLogin, btnCancel;
    private EditText inputEmail, inputPassword;
    private CheckBox cbKeepMeSigned;

    private SharedPreferences preferences;
    public static final String PREFS_NAME = "PrefsFile";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        disableAutoFill();

        preferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);

        auth = FirebaseAuth.getInstance();
        btnLogin = findViewById(R.id.btnLogin);
        inputEmail = findViewById(R.id.inputEmailLogin);
        inputPassword = findViewById(R.id.inputPasswordLogin);
        cbKeepMeSigned = findViewById(R.id.cbKeepMeSigned);

        String msg = getIntent().getStringExtra("mensage");

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String email = inputEmail.getText().toString().trim();
                String password = inputPassword.getText().toString().trim();

                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(getApplicationContext(), "Enter email address!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(password)) {
                    Toast.makeText(getApplicationContext(), "Enter password!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (password.length() < 8) {
                    showErrorMessage(R.string.invalidCredentials);
                    return;
                }

                if (!validEmail(email)) {
                    showErrorMessage(R.string.wrongEmailFormat);
                    return;
                }


                auth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    if (cbKeepMeSigned.isChecked()) {
                                        Boolean boolIsChecked = cbKeepMeSigned.isChecked();
                                        SharedPreferences.Editor editor= preferences.edit();
                                        editor.putBoolean("pref_check", boolIsChecked);
                                        editor.apply();
                                    }else{
                                        preferences.edit().clear().apply();
                                    }
                                    //autenticado
                                    startActivity(AuthenticatedDashboardActivity.getIntent(LoginActivity.this, 2));
                                    finish();
                                } else {
                                    if (task.getException().getMessage().equals("There is no user record corresponding to this identifier. The user may have been deleted.")) {
                                        showErrorMessage(R.string.invalidCredentials);
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

    public static Intent getIntent(Context context, int wel) {
        Intent intent = new Intent(context, LoginActivity.class);
        intent.putExtra("LOGIN",wel);
        return intent;
    }

    public void cancelLogin(View view) {
        startActivity(DashboardActivity.getIntent(LoginActivity.this, -1));
        finish();
    }

    @TargetApi(Build.VERSION_CODES.O)
    private void disableAutoFill() {
        getWindow().getDecorView().setImportantForAutofill(View.IMPORTANT_FOR_AUTOFILL_NO_EXCLUDE_DESCENDANTS);
    }

}
