package com.example.loupgarou;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.RecyclerViewHolder> {
    private ArrayList<RecyclerData> usersDataArrayList;
    private Context context;

    public RecyclerViewAdapter(ArrayList<RecyclerData> recyclerDataArrayList, Context context) {
        this.usersDataArrayList = recyclerDataArrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardusers, parent, false);
        return new RecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewHolder holder, int position) {
        RecyclerData recyclerData = usersDataArrayList.get(position);
        holder.name.setText(recyclerData.getName());
        //holder.imgid.setImageResource(recyclerData.getImgid());
    }

    @Override
    public int getItemCount() {
        return usersDataArrayList.size();
    }

    public class RecyclerViewHolder extends RecyclerView.ViewHolder {
        private TextView name;
        private ImageView imgid;
        public RecyclerViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.idUserName);
            imgid = itemView.findViewById(R.id.idUserImg);
        }
    }
}
