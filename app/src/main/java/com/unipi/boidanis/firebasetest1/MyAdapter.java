package com.unipi.boidanis.firebasetest1;

import static java.security.AccessController.getContext;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DateFormat;
import java.util.ArrayList;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {


    Context context;
    ArrayList<WeightData>list;

    public MyAdapter(Context context, ArrayList<WeightData> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_weightdata,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        WeightData weightData=list.get(position);
        DateFormat dateFormat = android.text.format.DateFormat.getDateFormat(context);
        holder.date.setText(dateFormat.format(weightData.getDate()).toString());
        holder.week.setText(String.valueOf(weightData.getWeek()));
        holder.weight.setText(String.valueOf(weightData.getWeight()));
        holder.key=weightData.getKey();
        holder.babyName=weightData.getBabyName();
        holder.intweek = weightData.getWeek();
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{
        TextView date,week,weight;
        Button button;
        String key,babyName;
        int intweek;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            date=itemView.findViewById(R.id.textView18);
            week=itemView.findViewById(R.id.textView19);
            weight=itemView.findViewById(R.id.textView20);
            button=itemView.findViewById(R.id.button);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                    FirebaseAuth mAuth;
                    mAuth = FirebaseAuth.getInstance();
                    if(intweek!=0){
                        DatabaseReference reference = database.getReference().child("Users").child(mAuth.getUid()).child(babyName).child("weightData").child(key);
                        reference.removeValue();
                    }else{
                        Toast.makeText(itemView.getContext(), "Cant delete birthdate", Toast.LENGTH_SHORT).show();
                    }
                }
            });

        }
    }
}
