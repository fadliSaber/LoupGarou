package com.example.loupgarou;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class villageois_game extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ArrayList<User> recyclerDataArrayList;
    private String roomCode,userRole;
    private DatabaseReference roomRef;
    private Integer gameStep;
    private TextView gameDesc,phaseDesc,NightDesc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_villageois_game);

        Intent intent = getIntent();
        roomCode = intent.getStringExtra("ROOM_CODE");
        userRole = intent.getStringExtra("USER_ROLE");
        phaseDesc = findViewById(R.id.textView11);
        gameDesc = findViewById(R.id.textView12);
        NightDesc = findViewById(R.id.textView4);

        roomRef = FirebaseDatabase.getInstance().getReference("rooms");

        roomRef.child(roomCode).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                gameStep = snapshot.child("gameStep").getValue(Integer.class);
                switch (gameStep%5) {
                    case 1:
                        phaseDesc.setText("Phase 2:");
                        gameDesc.setText("La Sorcière se réveille. Va-t-elle \n" +
                                "utiliser sa potion de guérison, \n" +
                                "ou d’empoisonnement ?");
                        break;
                    case 2:
                        phaseDesc.setText("Phase 3:");
                        gameDesc.setText("La Voyante se réveille, et désigne \n" +
                                "un joueur dont elle veut sonder \n" +
                                "la véritable personnalité");
                        break;
                    case 3:
                        phaseDesc.setText("Phase 4:");
                        NightDesc.setText("Jour 1:");
                        gameDesc.setText("C’est le matin, le village se réveille.\n" +
                                "Discutez et votez un joueur\n" +
                                "pour l’éliminer");
                        break;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}