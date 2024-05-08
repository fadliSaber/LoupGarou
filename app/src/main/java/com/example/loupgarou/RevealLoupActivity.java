package com.example.loupgarou;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class RevealLoupActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private DatabaseReference roomRef;
    private String roomCode;
    private Button continueBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reveal_loup);

        roomRef = FirebaseDatabase.getInstance().getReference("rooms");
        Intent intent = getIntent();
        if(intent!=null && intent.hasExtra("ROOM_CODE")) {
            roomCode = intent.getStringExtra("ROOM_CODE");
        }else{
            Toast.makeText(this, "No room CODE provided", Toast.LENGTH_SHORT).show();
            finish();
        }

        continueBtn = findViewById(R.id.button5);

        continueBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startGame(v);
            }
        });
    }

    private void startGame(View v) {
        roomRef.child(roomCode).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Integer nbStarts = snapshot.child("nbStarts").getValue(Integer.class);
                roomRef.child(roomCode).child("nbStarts").setValue(nbStarts+1);
                if(nbStarts+1==2*snapshot.child("users").getValue(new GenericTypeIndicator<List<User>>() {
                }).size()){
                    Intent intent = new Intent(RevealLoupActivity.this,loup_game.class);
                    intent.putExtra("ROOM_CODE",roomCode);
                    startActivity(intent);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.w("add nbStarts","countNb:onCancelled",error.toException());
            }
        });

        roomRef.child(roomCode).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Integer nbStarts = snapshot.child("nbStarts").getValue(Integer.class);
                List<User> users = snapshot.child("users").getValue(new GenericTypeIndicator<List<User>>() {
                });
                if(nbStarts==2*users.size()){
                    Intent intent = new Intent(RevealLoupActivity.this,loup_game.class);
                    intent.putExtra("ROOM_CODE",roomCode);
                    startActivity(intent);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.w("add nbStarts","countNb:onCancelled",error.toException());
            }
        });
    }
}