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
        phaseDesc = findViewById(R.id.textView11);
        gameDesc = findViewById(R.id.textView12);
        NightDesc = findViewById(R.id.textView4);

        roomRef = FirebaseDatabase.getInstance().getReference("rooms");

        roomRef.child(roomCode).child("gameStep").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                gameStep = snapshot.getValue(Integer.class);
                Integer x = gameStep/4 + 1;
                switch (gameStep%4) {
                    case 0:
                        phaseDesc.setText("Phase 1:");
                        NightDesc.setText("Nuit "+x+":");
                        gameDesc.setText("Les Loups-Garous se réveillent \n" +
                                "et désignent une nouvelle victime");
                        break;
                    case 1:
                        phaseDesc.setText("Phase 2:");
                        NightDesc.setText("Nuit "+x+":");
                        gameDesc.setText("La Sorcière se réveille. Va-t-elle \n" +
                                "utiliser sa potion de guérison, \n" +
                                "ou d’empoisonnement ?");
                        break;
                    case 2:
                        phaseDesc.setText("Phase 3:");
                        NightDesc.setText("Nuit "+x+":");
                        gameDesc.setText("La Voyante se réveille, et désigne \n" +
                                "un joueur dont elle veut sonder \n" +
                                "la véritable personnalité");
                        break;
                    case 3:
                        Intent intent = new Intent(villageois_game.this,generalVoteActivity.class);
                        intent.putExtra("ROOM_CODE",roomCode);
                        intent.putExtra("USER_ROLE","villageois");
                        startActivity(intent);
                        break;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}