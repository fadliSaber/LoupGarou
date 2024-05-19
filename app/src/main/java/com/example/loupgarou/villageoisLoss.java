package com.example.loupgarou;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class villageoisLoss extends AppCompatActivity {

    private Button backBtn;
    private DatabaseReference roomRef;
    private String roomCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_villageois_loss);

        backBtn = findViewById(R.id.backBtn);
        roomRef = FirebaseDatabase.getInstance().getReference("rooms");
        roomCode = getIntent().getStringExtra("ROOM_CODE");

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backFun(v);
            }
        });


    }

    private void backFun(View v) {
        Intent intent = new Intent("FINISH_ALL_ACTIVITIES");
        sendBroadcast(intent);
        startActivity(new Intent(villageoisLoss.this,MainActivity.class));
    }
}