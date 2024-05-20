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
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class generalVoteActivity extends AppCompatActivity implements RecyclerViewAdapter.OnUserClickListener{

    private RecyclerView recyclerView;
    private ArrayList<User> recyclerDataArrayList;
    private DatabaseReference roomRef;
    private String roomCode,userRole;
    private Integer gameStep;
    private List<User> users;
    private RecyclerViewAdapter adapter;
    private List<User> userList,userList2,userList3;
    private String userSelectedId,userIdMax;
    private int max_ = 0,count2 = 0,loups = 0;
    private TextView gameDesc,phaseDesc,NightDesc;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_general_vote);
        recyclerView=findViewById(R.id.rvUserList);

        Intent intent = getIntent();
        roomCode = intent.getStringExtra("ROOM_CODE");
        userRole = intent.getStringExtra("USER_ROLE");
        users = new ArrayList<>();
        roomRef = FirebaseDatabase.getInstance().getReference("rooms");
        userList = new ArrayList<>();
        userList2 = new ArrayList<>();
        userList3 = new ArrayList<>();
        phaseDesc = findViewById(R.id.textView11);
        gameDesc = findViewById(R.id.textView12);
        NightDesc = findViewById(R.id.textView4);

        loups = 0;

        listUsers();

    }

    public void listUsers() {
        roomRef.child(roomCode).addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                users.clear();
                count2 = 0;
                for(DataSnapshot snapshot1:snapshot.child("users").getChildren()) {
                    User user = snapshot1.getValue(User.class);
                    if(user.getState().equals("actif")){
                        users.add(user);
                        if(user.getRole().equals("loup")) loups++;
                    }
                }
                gameStep = snapshot.child("gameStep").getValue(Integer.class);
                int x = gameStep/4 + 1;
                NightDesc.setText("Night "+x+":");
                gameDesc.setText("C’est le matin, le village se réveille.\n" +
                        "Discutez et votez un joueur\n" +
                        "pour l’éliminer");
                phaseDesc.setText("Phase 4:");
                count2 = users.size();
                adapter = new RecyclerViewAdapter((ArrayList<User>) users, generalVoteActivity.this, "blue",generalVoteActivity.this);
                GridLayoutManager layoutManager = new GridLayoutManager(generalVoteActivity.this, 3);
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
        if (activity.equals("generalVoteActivity")) {
            userSelectedId = userId;
            roomRef.child(roomCode).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    Integer nbStarts = snapshot.child("nbStarts").getValue(Integer.class);
                    userList2.clear();
                    max_ = 0;
                    int i = 0;
                    int posMax = 0;
                    for(DataSnapshot snapshot1:snapshot.child("users").getChildren()) {
                        User user = snapshot1.getValue(User.class);
                        if(user.getId().equals(userSelectedId)) {
                            user.setVote(user.getVote()+1);
                            if(user.getVote()>max_) {
                                max_ = user.getVote();
                                userIdMax = user.getId();
                                posMax = i;
                            }
                        }
                        i++;
                        userList2.add(user);
                    }
                    roomRef.child(roomCode).child("users").setValue(userList2);
                    roomRef.child(roomCode).child("nbStarts").setValue(nbStarts+1);
                    if(nbStarts+1==count2) {
                        Log.w("counttttt","count: "+count2);
                        User user = userList2.get(posMax);
                        user.setState("inactif");
                        if(user.getRole().equals("loup")) loups--;
                        Log.w("loups","n :"+posMax);
                        userList2.set(posMax,user);
                        for(User user1:userList2){
                            user1.setVote(0);
                        }
                        if(2*loups>=count2) {
                            roomRef.child(roomCode).child("users").setValue(userList2);
                            roomRef.child(roomCode).child("nbStarts").setValue(0);
                            roomRef.child(roomCode).child("gameStep").setValue(gameStep + 1);
                            Toast.makeText(generalVoteActivity.this, "The role was : " + user.getRole(), Toast.LENGTH_LONG).show();
                        }else if(loups==0) {
                            roomRef.child(roomCode).child("users").setValue(userList2);
                            roomRef.child(roomCode).child("nbStarts").setValue(0);
                            roomRef.child(roomCode).child("gameStep").setValue(gameStep + 2);
                            Toast.makeText(generalVoteActivity.this, "The role was : " + user.getRole(), Toast.LENGTH_LONG).show();
                        }else {
                            roomRef.child(roomCode).child("users").setValue(userList2);
                            roomRef.child(roomCode).child("nbStarts").setValue(0);
                            roomRef.child(roomCode).child("gameStep").setValue(gameStep + 3);
                            Toast.makeText(generalVoteActivity.this, "The role was : " + user.getRole(), Toast.LENGTH_LONG).show();
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });




        }
    }
}