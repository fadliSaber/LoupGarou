package com.example.loupgarou;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class AssignRolesActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ArrayList<RecyclerData> userList;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assign_roles);



    }

    public void startGameActivity(View view) {
        Intent intent = new Intent(this, GameActivity.class);
        startActivity(intent);
    }
}