package com.unipi.boidanis.firebasetest1;

import static java.security.AccessController.getContext;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DateFormat;
import java.util.ArrayList;





public class MomentsAdapter extends RecyclerView.Adapter<MomentsAdapter.MyViewHolder2>{

    Context context;
    ArrayList<MomentImages>list;

    public MomentsAdapter(Context context, ArrayList<MomentImages> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public MomentsAdapter.MyViewHolder2 onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_momentimages,parent,false);
        return new MomentsAdapter.MyViewHolder2(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MomentsAdapter.MyViewHolder2 holder, int position) {
        MomentImages momentImages=list.get(position);
        holder.momentName.setText(momentImages.getMomentName());
        holder.key=momentImages.getKey();
        holder.babyName=momentImages.getBabyName();
        Glide.with(context).load(list.get(position).getImageUrl()).into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class MyViewHolder2 extends RecyclerView.ViewHolder{
        TextView momentName;
        Button button;
        ImageView imageView;
        String key,babyName;
        public MyViewHolder2(@NonNull View itemView) {
            super(itemView);
            momentName=itemView.findViewById(R.id.textView18);
            imageView=itemView.findViewById(R.id.imageView3);
            button=itemView.findViewById(R.id.button);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                    FirebaseAuth mAuth;
                    mAuth = FirebaseAuth.getInstance();
                    DatabaseReference reference = database.getReference().child("Users").child(mAuth.getUid()).child(babyName).child("moments").child(key);
                    reference.removeValue();
                }
            });

        }
    }
}