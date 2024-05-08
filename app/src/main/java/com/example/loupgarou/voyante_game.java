package com.example.loupgarou;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
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

public class voyante_game extends AppCompatActivity implements RecyclerViewAdapter.OnUserClickListener{

    private RecyclerView recyclerView;
    private ArrayList<User> recyclerDataArrayList;
    private DatabaseReference roomRef;
    private String roomCode,userRole;
    private int gameStep;
    private List<User> users;
    private RecyclerViewAdapter adapter;
    private TextView revealUser,uRevealUser,uuRevealUser;
    private List<User> userList;
    private String userSelectedId;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_voyante_game);
        recyclerView=findViewById(R.id.rvUserList);

        Intent intent = getIntent();
        roomCode = intent.getStringExtra("ROOM_CODE");
        userRole = intent.getStringExtra("USER_ROLE");
        gameStep = intent.getIntExtra("gameStep",1);
        revealUser = findViewById(R.id.textView4);
        uRevealUser = findViewById(R.id.textView11);
        uuRevealUser = findViewById(R.id.textView12);
        users = new ArrayList<>();
        roomRef = FirebaseDatabase.getInstance().getReference("rooms");
        userList = new ArrayList<>();

        roomRef.child(roomCode).child("users").addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                userList = snapshot.getValue(new GenericTypeIndicator<List<User>>() {
                });
                users = userList;
                adapter = new RecyclerViewAdapter((ArrayList<User>) users, voyante_game.this, "blue",voyante_game.this);
                GridLayoutManager layoutManager = new GridLayoutManager(voyante_game.this, 3);
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
        if (activity.equals("voyante_game")) {
            userSelectedId = userId;
            roomRef.child(roomCode).child("users").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for(DataSnapshot snapshot1:snapshot.getChildren()) {
                        User user = snapshot1.getValue(User.class);
                        if(user.getId().equals(userSelectedId)){
                            uRevealUser.setText("");
                            uuRevealUser.setText("");
                            revealUser.setText(user.getRole());
                            int textColor = ContextCompat.getColor(voyante_game.this, R.color.black);
                            revealUser.setTextColor(textColor);
                            Intent intent = new Intent(voyante_game.this,villageois_game.class);
                            intent.putExtra("ROOMD_CODE",roomCode);
                            intent.putExtra("USER_ROLE",userRole);
                            intent.putExtra("gameStep",gameStep+1);
                            startActivity(intent);
                            finish();
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