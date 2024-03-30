package com.example.loupgarou;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.loupgarou.R;
import com.example.loupgarou.RecyclerData;

import java.util.ArrayList;

public class CreateRoomActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ArrayList<RecyclerData> recyclerDataArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_room);
        recyclerView=findViewById(R.id.rvUserList);

        TextView codefield = findViewById(R.id.textView4);

        String codeRoom = RandomCodeGenerator.generateCode(10);

        codefield.setText(codeRoom);

        recyclerDataArrayList=new ArrayList<RecyclerData>();

        //users who joins the room using the code, gets added in this list
        //example
        recyclerDataArrayList.add(new RecyclerData("Name",R.drawable.front));
        recyclerDataArrayList.add(new RecyclerData("Name",R.drawable.front));
        recyclerDataArrayList.add(new RecyclerData("Name",R.drawable.front));
        recyclerDataArrayList.add(new RecyclerData("Name",R.drawable.front));
        recyclerDataArrayList.add(new RecyclerData("Name",R.drawable.front));
        recyclerDataArrayList.add(new RecyclerData("Name",R.drawable.front));

        RecyclerViewAdapter adapter=new RecyclerViewAdapter(recyclerDataArrayList,this);

        GridLayoutManager layoutManager=new GridLayoutManager(this,3);

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
    }

    public void openAssignRolesActivity(View view) {
        Intent intent = new Intent(this, AssignRolesActivity.class);
        startActivity(intent);
    }
}
