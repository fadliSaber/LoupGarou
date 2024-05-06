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

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class waitRoomActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private List<User> users;
    private static long counter = 0;
    private String roomCode;
    private DatabaseReference roomRef;

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

        recyclerView=findViewById(R.id.rvUserList);

        Button startGame = findViewById(R.id.button3);
        Button codefield = findViewById(R.id.button4);
        String codeRoom = "CODE\n";

        codeRoom += roomCode;
        codefield.setText(codeRoom);
        users = new ArrayList<User>();
        RecyclerViewAdapter adapter=new RecyclerViewAdapter((ArrayList<User>) users,this,"blue");
        roomRef = FirebaseDatabase.getInstance().getReference("rooms");

        GridLayoutManager layoutManager=new GridLayoutManager(this,3);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        roomRef.child(roomCode).child("users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                users.clear();
                users = snapshot.getValue(new GenericTypeIndicator<List<User>>() {});
                adapter.notifyItemInserted(users.size()-1);
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
    }


}