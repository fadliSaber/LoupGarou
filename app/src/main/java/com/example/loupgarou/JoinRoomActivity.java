package com.example.loupgarou;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class JoinRoomActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join_room);
    }

    public void openJoinGameActivity(View view) {
        //show a loading screen to wait other players
        //determine what role is given
        //Intent intent = new Intent(this, RevealRoleActivity.class);
        //startActivity(intent);
    }
}