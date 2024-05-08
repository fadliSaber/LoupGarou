package com.example.loupgarou;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class sorciere_game extends AppCompatActivity implements RecyclerViewAdapter.OnUserClickListener{

    private RecyclerView recyclerView;
    private ArrayList<User> recyclerDataArrayList;
    private DatabaseReference roomRef;
    private String roomCode,userRole;
    private Integer gameStep;
    private List<User> users;
    private RecyclerViewAdapter adapter;
    private ImageButton killBtn,ReviveBtn;
    private List<User> userList,userList1;
    private String userSelectedId;
    private TextView gameDesc,phaseDesc,NightDesc;
    private ScrollView scrollView;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sorciere_game);
        recyclerView = findViewById(R.id.rvUserList);

        Intent intent = getIntent();
        roomCode = intent.getStringExtra("ROOM_CODE");
        userRole = intent.getStringExtra("USER_ROLE");
        killBtn = findViewById(R.id.button10);
        ReviveBtn = findViewById(R.id.button11);
        scrollView = findViewById(R.id.scrollView2);
        users = new ArrayList<>();
        roomRef = FirebaseDatabase.getInstance().getReference("rooms");
        userList = new ArrayList<>();
        userList1 = new ArrayList<>();

        phaseDesc = findViewById(R.id.textView11);
        gameDesc = findViewById(R.id.textView12);
        NightDesc = findViewById(R.id.textView4);

        roomRef.child(roomCode).child("users").addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                userList = snapshot.getValue(new GenericTypeIndicator<List<User>>() {
                });
                users = userList;
                adapter = new RecyclerViewAdapter((ArrayList<User>) users, sorciere_game.this, "blue", sorciere_game.this);
                GridLayoutManager layoutManager = new GridLayoutManager(sorciere_game.this, 3);
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.w("fetch users per room", "fetchUserPerRoom:onCancelled", error.toException());
            }
        });

        roomRef.child(roomCode).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                gameStep = snapshot.child("gameStep").getValue(Integer.class);
                switch (gameStep % 4) {
                    case 0:
                        phaseDesc.setText("Phase 1:");
                        gameDesc.setText("Les Loups-Garous se réveillent \n" +
                                "et désignent une nouvelle victime");
                        killBtn.setVisibility(View.INVISIBLE);
                        ReviveBtn.setVisibility(View.INVISIBLE);
                        scrollView.setVisibility(View.INVISIBLE);
                        break;
                    case 1:
                        phaseDesc.setText("Phase 2:");
                        gameDesc.setText("Choisissez votre cible et\n" +
                                "la potion à utiliser");
                        killBtn.setVisibility(View.VISIBLE);
                        ReviveBtn.setVisibility(View.VISIBLE);
                        scrollView.setVisibility(View.VISIBLE);
                        ReviveBtn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                revive(v);
                            }
                        });

                        killBtn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                killPlayer(v);
                            }
                        });
                        break;
                    case 2:
                        phaseDesc.setText("Phase 3:");
                        gameDesc.setText("La Voyante se réveille, et désigne \n" +
                                "un joueur dont elle veut sonder \n" +
                                "la véritable personnalité");
                        killBtn.setVisibility(View.INVISIBLE);
                        ReviveBtn.setVisibility(View.INVISIBLE);
                        scrollView.setVisibility(View.INVISIBLE);
                        break;
                    case 3:
                        phaseDesc.setText("Phase 4:");
                        NightDesc.setText("Jour 1:");
                        gameDesc.setText("C’est le matin, le village se réveille.\n" +
                                "Discutez et votez un joueur\n" +
                                "pour l’éliminer");
                        killBtn.setVisibility(View.INVISIBLE);
                        ReviveBtn.setVisibility(View.INVISIBLE);
                        scrollView.setVisibility(View.INVISIBLE);
                        break;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void killPlayer(View v) {
        roomRef.child(roomCode).child("users").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                userList.clear();
                for(DataSnapshot snapshot1:snapshot.getChildren()) {
                    User user = snapshot1.getValue(User.class);
                    if(user.getId().equals(userSelectedId)){
                        user.setState("killed");
                    }
                    userList.add(user);
                }
                roomRef.child(roomCode).child("users").setValue(userList);
                roomRef.child(roomCode).child("gameStep").setValue(gameStep+1);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void revive(View v) {
        roomRef.child(roomCode).child("users").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                userList1.clear();
                for(DataSnapshot snapshot1:snapshot.getChildren()) {
                    User user = snapshot1.getValue(User.class);
                    if(user.getState().equals("killed")){
                        user.setState("actif");
                    }
                    userList1.add(user);
                }
                roomRef.child(roomCode).child("users").setValue(userList1);
                roomRef.child(roomCode).child("gameStep").setValue(gameStep+1);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }



    @Override
    public void onUserClick(String userId, String activity) {
        if (activity.equals("sorciere_game")) {
            userSelectedId = userId;
        }
    }
}