package com.example.loupgarou;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void openCreateRoomActivity(View view) {
        Intent intent = new Intent(this, CreateRoomActivity.class);
        startActivity(intent);
    }
    public void openJoinRoomActivity(View view) {
        Intent intent = new Intent(this, JoinRoomActivity.class);
        startActivity(intent);
    }
}