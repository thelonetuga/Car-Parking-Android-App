package com.example.ecko.spots;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class AuthenticatedMoreInfoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authenticated_more_info);
        setTitle(R.string.myAccountInfo);
    }

    public static Intent getIntent(Context context){
        Intent intent = new Intent(context, AuthenticatedMoreInfoActivity.class);
        return intent;
    }

    public void onClickCancel(View view) {
        startActivity(AuthenticatedDashboardActivity.getIntent(AuthenticatedMoreInfoActivity.this, 6));
    }

}
