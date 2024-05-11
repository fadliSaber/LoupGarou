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

public class waitRoomActivity extends AppCompatActivity implements RecyclerViewAdapter.OnUserClickListener{

    private RecyclerViewAdapter adapter;
    private RecyclerView recyclerView;
    private List<User> users;
    private String roomCode;
    private DatabaseReference roomRef;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wait_room);

        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("ROOM_CODE")) {
            roomCode = intent.getStringExtra("ROOM_CODE");
        } else {
            Toast.makeText(this, "No room CODE provided", Toast.LENGTH_SHORT).show();
            finish();
        }
        recyclerView = findViewById(R.id.rvUserList);

        Button startGame = findViewById(R.id.button3);
        Button codefield = findViewById(R.id.button4);
        String codeRoom = "CODE\n";

        codeRoom += roomCode;
        codefield.setText(codeRoom);

        roomRef = FirebaseDatabase.getInstance().getReference("rooms");
        mAuth = FirebaseAuth.getInstance();

        users = new ArrayList<>();
        roomRef.child(roomCode).child("users").addListenerForSingleValueEvent(new ValueEventListener() {
            List<User> userList;

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                userList = snapshot.getValue(new GenericTypeIndicator<List<User>>() {
                });
                users = userList;
                adapter = new RecyclerViewAdapter((ArrayList<User>) users, waitRoomActivity.this, "blue",waitRoomActivity.this);
                GridLayoutManager layoutManager = new GridLayoutManager(waitRoomActivity.this, 3);
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.w("fetch users per room", "fetchUserPerRoom:onCancelled", error.toException());
            }
        });

        roomRef.child(roomCode).child("users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                users.clear();
                for (DataSnapshot snapshot1 : snapshot.getChildren()){
                    User user = snapshot1.getValue(User.class);
                    users.add(user);
                }
                adapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.w("fetch users per room","fetchUserPerRoom:onCancelled",error.toException());
            }
        });



    }



    public void startGame(View view) {
        roomRef.child(roomCode).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Integer nbStarts = snapshot.child("nbStarts").getValue(Integer.class);
                roomRef.child(roomCode).child("nbStarts").setValue(nbStarts+1);
                if(nbStarts+1==snapshot.child("users").getValue(new GenericTypeIndicator<List<User>>() {
                }).size()){
                    Intent intent = new Intent(waitRoomActivity.this,RevealLoupActivity.class);
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
                if(nbStarts==users.size()){
                    String userId = mAuth.getCurrentUser().getUid();
                    Log.w("usersUid","uid : "+userId);
                    Log.w("nbStarts","nbr : "+nbStarts);
                    String currentUserRole = "";
                    List<String> usersRoles = generateRoles(nbStarts);
                    Log.w("roleTag","role : "+usersRoles.get(0));
                    int i = 0;
                    List<User> userList = new ArrayList<>();
                    for(User user:users) {
                        Log.w("userLoop","uid : "+user.getId());
                        if(userId.equals(user.getId())) {
                            currentUserRole = usersRoles.get(i);
                        }
                        user.setRole(usersRoles.get(i));
                        userList.add(user);
                        i++;
                    }
                    roomRef.child(roomCode).child("users").setValue(userList);
                    roomRef.child(roomCode).child("nbStarts").setValue(0);
                    Log.w("currentRole","role : "+currentUserRole);
                    Log.w("currentI","id: "+i);
                    switch (currentUserRole) {
                        case "loup":
                            Intent intent = new Intent(waitRoomActivity.this, RevealLoupActivity.class);
                            intent.putExtra("ROOM_CODE",roomCode);
                            startActivity(intent);
                            break;
                        case "villageois":
                            Intent intent2 = new Intent(waitRoomActivity.this, RevealVillageoisActivity.class);
                            intent2.putExtra("ROOM_CODE",roomCode);
                            startActivity(intent2);
                            break;
                        case "sorciere":
                            Intent intent3 = new Intent(waitRoomActivity.this, RevealSorciereActivity.class);
                            intent3.putExtra("ROOM_CODE",roomCode);
                            startActivity(intent3);
                            break;
                        case "voyante":
                            Intent intent4 = new Intent(waitRoomActivity.this, RevealVoyanteActivity.class);
                            intent4.putExtra("ROOM_CODE",roomCode);
                            startActivity(intent4);
                            break;
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.w("add nbStarts","countNb:onCancelled",error.toException());
            }
        });
    }

    private List<String> generateRoles(Integer nbStarts) {
        List<String> answers = new ArrayList<>();
        Log.w("nbStartsFun","nbr : "+nbStarts);
        for(int i = 0;i<nbStarts;i++){
            if(i==0) answers.add("voyante");
            else if(i==1) answers.add("loup");
            else answers.add("villageois");
        }
        return answers;
    }


    @Override
    public void onUserClick(String userId, String activity) {
        return;
    }
}