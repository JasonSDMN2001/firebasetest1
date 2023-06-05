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
public class HeightAdapter extends RecyclerView.Adapter<HeightAdapter.MyViewHolder>{

    Context context;
    ArrayList<HeightData>list;

    public HeightAdapter(Context context, ArrayList<HeightData> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public HeightAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_heightdata,parent,false);
        return new HeightAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HeightAdapter.MyViewHolder holder, int position) {
        HeightData heightData=list.get(position);
        DateFormat dateFormat = android.text.format.DateFormat.getDateFormat(context);
        holder.date.setText(dateFormat.format(heightData.getDate()).toString());
        holder.week.setText(String.valueOf(heightData.getWeek()));
        holder.height.setText(String.valueOf(heightData.getHeight()));
        holder.key=heightData.getKey();
        holder.babyName=heightData.getBabyName();
        holder.intweek = heightData.getWeek();
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

public static class MyViewHolder extends RecyclerView.ViewHolder{
    TextView date,week,height;
    Button button;
    String key,babyName;
    int intweek;
    public MyViewHolder(@NonNull View itemView) {
        super(itemView);
        date=itemView.findViewById(R.id.textView18);
        week=itemView.findViewById(R.id.textView19);
        height=itemView.findViewById(R.id.textView20);
        button=itemView.findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                FirebaseAuth mAuth;
                mAuth = FirebaseAuth.getInstance();
                if(intweek!=0){
                    DatabaseReference reference = database.getReference().child("Users").child(mAuth.getUid()).child(babyName).child("heightData").child(key);
                    reference.removeValue();
                }else{
                    Toast.makeText(itemView.getContext(), "Cant delete birthdate", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}
}
