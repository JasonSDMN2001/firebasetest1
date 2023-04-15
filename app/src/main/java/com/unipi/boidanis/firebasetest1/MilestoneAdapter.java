package com.unipi.boidanis.firebasetest1;

import static java.security.AccessController.getContext;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.util.ArrayList;



public class MilestoneAdapter extends RecyclerView.Adapter<MilestoneAdapter.MyViewHolder3> {
    Context context;
    ArrayList<Milestones>list;

    public MilestoneAdapter(Context context, ArrayList<Milestones> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public MilestoneAdapter.MyViewHolder3 onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_milestones,parent,false);
        return new MilestoneAdapter.MyViewHolder3(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MilestoneAdapter.MyViewHolder3 holder, int position) {
        Milestones milestones = list.get(position);
        holder.name.setText(milestones.milestoneName);
        holder.range.setText(milestones.lowerbound +"-"+milestones.upperbound+" weeks");
        holder.key = milestones.getKey();
        holder.babyName = milestones.getBabyName();
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class MyViewHolder3 extends RecyclerView.ViewHolder{
        TextView name,range;
        Button button,button2,button3;
        String key,babyName;
        public MyViewHolder3(@NonNull View itemView) {
            super(itemView);
            name=itemView.findViewById(R.id.textView19);
            range=itemView.findViewById(R.id.textView20);
            button=itemView.findViewById(R.id.button);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                    FirebaseAuth mAuth;
                    mAuth = FirebaseAuth.getInstance();
                    DatabaseReference reference = database.getReference().child("Users").child(mAuth.getUid()).child(babyName).child("milestones").child(key);
                    reference.child("completed").setValue(true);
                }
            });
            button2=itemView.findViewById(R.id.button2);
            button2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                    FirebaseAuth mAuth;
                    mAuth = FirebaseAuth.getInstance();
                    DatabaseReference reference = database.getReference().child("Users").child(mAuth.getUid()).child(babyName).child("milestones").child(key);
                    reference.child("completed").setValue(false);
                }
            });
            button3=itemView.findViewById(R.id.button3);
            button3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                    FirebaseAuth mAuth;
                    mAuth = FirebaseAuth.getInstance();
                    DatabaseReference reference = database.getReference().child("Users").child(mAuth.getUid()).child(babyName).child("milestones").child(key);
                    reference.removeValue();
                }
            });
        }
    }
}
