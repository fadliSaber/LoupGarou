package com.example.loupgarou;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.Button;

import java.util.ArrayList;

public class voyante_game extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ArrayList<RecyclerData> recyclerDataArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_voyante_game);
        recyclerView=findViewById(R.id.rvUserList);


        recyclerDataArrayList=new ArrayList<RecyclerData>();

        //users who joins the room using the code, gets added in this list
        //example
        recyclerDataArrayList.add(new RecyclerData("Name",R.drawable.front));
        recyclerDataArrayList.add(new RecyclerData("Name",R.drawable.front));
        recyclerDataArrayList.add(new RecyclerData("Name",R.drawable.front));
        recyclerDataArrayList.add(new RecyclerData("Name",R.drawable.front));
        recyclerDataArrayList.add(new RecyclerData("Name",R.drawable.front));
        recyclerDataArrayList.add(new RecyclerData("Name",R.drawable.front));

        RecyclerViewAdapter adapter=new RecyclerViewAdapter(recyclerDataArrayList,this,"blue");

        GridLayoutManager layoutManager=new GridLayoutManager(this,3);

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);



    }
}