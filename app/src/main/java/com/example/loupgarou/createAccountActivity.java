package com.example.loupgarou;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class createAccountActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);
    }


    public void createAccount(View view) {
        //create account here
        Intent intent = new Intent(this,MainActivity.class);
        startActivity(intent);
    }
}