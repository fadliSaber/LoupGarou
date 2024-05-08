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
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class loup_game extends AppCompatActivity implements RecyclerViewAdapter.OnUserClickListener{

    private RecyclerView recyclerView;
    private ArrayList<User> recyclerDataArrayList;
    private DatabaseReference roomRef;
    private String roomCode,userRole;
    private int gameStep;
    private List<User> users;
    private RecyclerViewAdapter adapter;
    private List<User> userList,userList2,userList3;
    private String userSelectedId,userIdMax;
    private int count = 0,max_ = 0,count2 = 0;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loup_game);
        recyclerView=findViewById(R.id.rvUserList);

        Intent intent = getIntent();
        roomCode = intent.getStringExtra("ROOM_CODE");
        userRole = intent.getStringExtra("USER_ROLE");
        gameStep = intent.getIntExtra("gameStep",1);
        users = new ArrayList<>();
        roomRef = FirebaseDatabase.getInstance().getReference("rooms");
        userList = new ArrayList<>();
        userList2 = new ArrayList<>();
        userList3 = new ArrayList<>();

        roomRef.child(roomCode).child("users").addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                userList.clear();
                for(DataSnapshot snapshot1:snapshot.getChildren()) {
                    User user = snapshot1.getValue(User.class);
                    if(user.getRole().equals("loup")) {
                        count++;
                    }

                    userList.add(user);
                }
                users = userList;
                count2 = users.size();
                adapter = new RecyclerViewAdapter((ArrayList<User>) users, loup_game.this, "blue",loup_game.this);
                GridLayoutManager layoutManager = new GridLayoutManager(loup_game.this, 3);
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.w("fetch users per room", "fetchUserPerRoom:onCancelled", error.toException());
            }
        });





    }

    @Override
    public void onUserClick(String userId, String activity) {
        if (activity.equals("loup_game")) {
            userSelectedId = userId;
            roomRef.child(roomCode).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    Integer nbStarts = snapshot.child("nbStarts").getValue(Integer.class);
                    DataSnapshot snapshot2 = snapshot.child("users");
                    userList2.clear();
                    for(DataSnapshot snapshot1:snapshot2.getChildren()) {
                        User user = snapshot1.getValue(User.class);
                        if(user.getId().equals(userSelectedId)){
                            user.setVote(user.getVote()+1);
                            if(user.getVote()>max_){
                                max_ = user.getVote();
                                userIdMax = user.getId();
                            }
                        }
                        userList2.add(user);
                    }
                    roomRef.child(roomCode).child("users").setValue(userList2);
                    roomRef.child(roomCode).child("nbStarts").setValue(nbStarts+1);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });


            roomRef.child(roomCode).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    Integer nbStarts = snapshot.child("nbStarts").getValue(Integer.class);
                    if(nbStarts-2*count2==count){
                        DataSnapshot snapshot2 = snapshot.child("users");
                        userList3.clear();
                        for(DataSnapshot snapshot1:snapshot2.getChildren()) {
                            User user = snapshot1.getValue(User.class);
                            if(user.getId().equals(userIdMax)){
                                user.setState("killed");
                            }
                            userList3.add(user);
                        }
                        roomRef.child(roomCode).child("users").setValue(userList3);
                        Intent intent = new Intent(loup_game.this,villageois_game.class);
                        intent.putExtra("ROOM_CODE",roomCode);
                        intent.putExtra("USER_ROLE",userRole);
                        intent.putExtra("gameStep",gameStep+1);
                        startActivity(intent);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
    }
}