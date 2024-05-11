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
    private TextView gameDesc,phaseDesc,NightDesc;
    private ScrollView scrollView;



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
        phaseDesc = findViewById(R.id.textView11);
        gameDesc = findViewById(R.id.textView12);
        NightDesc = findViewById(R.id.textView4);
        scrollView = findViewById(R.id.scrollView2);

        listUsers();
        roomRef.child(roomCode).child("gameStep").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                gameStep = snapshot.getValue(Integer.class);
                int x = gameStep/4 + 1;
                switch (gameStep%4) {
                    case 0:
                        listUsers();
                        phaseDesc.setText("Phase 1:");
                        NightDesc.setText("Nuit "+x+":");
                        gameDesc.setText("Votez pour la cible que\n" +
                                "vous voulez eliminer");
                        scrollView.setVisibility(View.VISIBLE);
                        break;
                    case 1:
                        phaseDesc.setText("Phase 2:");
                        NightDesc.setText("Nuit "+x+":");
                        gameDesc.setText("La Sorcière se réveille. Va-t-elle \n" +
                                "utiliser sa potion de guérison, \n" +
                                "ou d’empoisonnement ?");
                        scrollView.setVisibility(View.INVISIBLE);
                        break;
                    case 2:
                        phaseDesc.setText("Phase 3:");
                        NightDesc.setText("Nuit "+x+":");
                        gameDesc.setText("La Voyante se réveille, et désigne \n" +
                                "un joueur dont elle veut sonder \n" +
                                "la véritable personnalité");
                        scrollView.setVisibility(View.INVISIBLE);
                        break;
                    case 3:
                        Intent intent = new Intent(loup_game.this,generalVoteActivity.class);
                        intent.putExtra("ROOM_CODE",roomCode);
                        intent.putExtra("USER_ROLE","loup");
                        startActivity(intent);
                        break;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



    }

    public void listUsers() {
        roomRef.child(roomCode).child("users").addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                userList.clear();
                count = 0;
                for(DataSnapshot snapshot1:snapshot.getChildren()) {
                    User user = snapshot1.getValue(User.class);
                    users.add(user);
                    if(user.getRole().equals("loup")) {
                        count++;
                    }else userList.add(user);
                }
                count2 = users.size();
                adapter = new RecyclerViewAdapter((ArrayList<User>) userList, loup_game.this, "red",loup_game.this);
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
        Integer val = count;
        if (activity.equals("loup_game")) {
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
                                Log.w("highestVote","highest: "+user.getVote());
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
                    Log.w("TheVal","val: "+val);
                    Log.w("TheCount2","val: "+count2);
                    Log.w("TheCount","val: "+count);
                    if(nbStarts+1==val) {
                        User user = userList2.get(posMax);
                        user.setState("killed");
                        userList2.set(posMax,user);
                        Log.w("userVote","vote: "+user.getVote());
                        roomRef.child(roomCode).child("users").setValue(userList2);
                        roomRef.child(roomCode).child("gameStep").setValue(gameStep+1);
                        roomRef.child(roomCode).child("nbStarts").setValue(0);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });




        }
    }
}