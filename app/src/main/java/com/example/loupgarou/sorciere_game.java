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
    private int gameStep;
    private List<User> users;
    private RecyclerViewAdapter adapter;
    private ImageButton killBtn,ReviveBtn;
    private List<User> userList,userList1;
    private String userSelectedId;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sorciere_game);
        recyclerView=findViewById(R.id.rvUserList);

        Intent intent = getIntent();
        roomCode = intent.getStringExtra("ROOM_CODE");
        userRole = intent.getStringExtra("USER_ROLE");
        gameStep = intent.getIntExtra("gameStep",1);
        killBtn = findViewById(R.id.button10);
        ReviveBtn = findViewById(R.id.button11);
        users = new ArrayList<>();
        roomRef = FirebaseDatabase.getInstance().getReference("rooms");
        userList = new ArrayList<>();
        userList1 = new ArrayList<>();

        roomRef.child(roomCode).child("users").addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                userList = snapshot.getValue(new GenericTypeIndicator<List<User>>() {
                });
                users = userList;
                adapter = new RecyclerViewAdapter((ArrayList<User>) users, sorciere_game.this, "blue",sorciere_game.this);
                GridLayoutManager layoutManager = new GridLayoutManager(sorciere_game.this, 3);
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.w("fetch users per room", "fetchUserPerRoom:onCancelled", error.toException());
            }
        });

        ReviveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                roomRef.child(roomCode).child("users").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for(DataSnapshot snapshot1:snapshot.getChildren()) {
                            User user = snapshot1.getValue(User.class);
                            if(user.getState().equals("killed")){
                                user.setState("actif");
                            }
                            userList1.add(user);
                        }
                        //roomRef.child(roomCode).child("users").setValue(userList1);

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
                Intent intent = new Intent(sorciere_game.this,villageois_game.class);
                intent.putExtra("ROOM_CODE",roomCode);
                intent.putExtra("USER_ROLE",userRole);
                intent.putExtra("gameStep",gameStep);
                startActivity(intent);
                finish();
            }
        });

        killBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
                        //roomRef.child(roomCode).child("users").setValue(userList);

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
                Intent intent = new Intent(sorciere_game.this,villageois_game.class);
                intent.putExtra("ROOM_CODE",roomCode);
                intent.putExtra("USER_ROLE",userRole);
                intent.putExtra("gameStep",gameStep);
                startActivity(intent);
                finish();
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