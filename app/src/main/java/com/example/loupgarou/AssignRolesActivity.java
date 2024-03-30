package com.example.loupgarou;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;

public class AssignRolesActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ArrayList<RecyclerData> recyclerDataArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assign_roles);

        recyclerView = findViewById(R.id.rvUserList2);

        recyclerDataArrayList=new ArrayList<RecyclerData>();

        //users who joins the room using the code, gets added in this list
        //example
        recyclerDataArrayList.add(new RecyclerData("Name",R.drawable.loup,"loup"));
        recyclerDataArrayList.add(new RecyclerData("Name",R.drawable.loup,"loup"));
        recyclerDataArrayList.add(new RecyclerData("Name",R.drawable.loup,"loup"));
        recyclerDataArrayList.add(new RecyclerData("Name",R.drawable.villageois,"villageois"));
        recyclerDataArrayList.add(new RecyclerData("Name",R.drawable.voyante,"voyante"));
        recyclerDataArrayList.add(new RecyclerData("Name",R.drawable.sorciere,"sorcière"));

        RecyclerViewAdapter adapter=new RecyclerViewAdapter(recyclerDataArrayList,this);

        GridLayoutManager layoutManager=new GridLayoutManager(this,3);

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
    }

    public void startGameActivity(View view) {
        Intent intent = new Intent(this, GameActivity.class);
        startActivity(intent);
    }
}