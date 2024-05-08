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
    private ArrayList<User> usersDataArrayList;
    private Context context;
    private String color;
    private OnUserClickListener clickListener;

    public RecyclerViewAdapter(ArrayList<User> recyclerDataArrayList, Context context, String color,OnUserClickListener clickListener) {
        this.usersDataArrayList = recyclerDataArrayList;
        this.context = context;
        this.color = color;
        this.clickListener = clickListener;
    }

    public interface OnUserClickListener {
        void onUserClick(String userId,String activity);
    }

    @NonNull
    @Override
    public RecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if(this.color.equals("blue")) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardusers,
                    parent, false);
        }
        else{
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardusersred,
                    parent, false);
        }
        return new RecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewHolder holder, int position) {
        User user = usersDataArrayList.get(position);
        holder.name.setText(user.getState());
        //holder.imgid.setImageResource(recyclerData.getImgid());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickListener.onUserClick(user.getId(),getActivityName(context));
            }
        });
    }

    private String getActivityName(Context context) {
        // Determine the source activity based on the context
        if (context instanceof sorciere_game) {
            return "sorciere_game";
        } else if (context instanceof loup_game) {
            return "loup_game";
        } else if (context instanceof voyante_game){
            return "voyante_game";
        }else {
            return "unknown";
        }
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
