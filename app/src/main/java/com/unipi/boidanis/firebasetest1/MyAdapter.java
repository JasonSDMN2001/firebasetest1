package com.unipi.boidanis.firebasetest1;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

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
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{
        TextView date,week,weight;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            date=itemView.findViewById(R.id.textView18);
            week=itemView.findViewById(R.id.textView19);
            weight=itemView.findViewById(R.id.textView20);
        }
    }
}
