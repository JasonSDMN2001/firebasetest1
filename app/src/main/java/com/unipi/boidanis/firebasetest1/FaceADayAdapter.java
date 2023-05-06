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
public class FaceADayAdapter extends RecyclerView.Adapter<FaceADayAdapter.MyViewHolder4>{
    Context context;
    ArrayList<FacePicture>list;
    public FaceADayAdapter(Context context, ArrayList<FacePicture> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public FaceADayAdapter.MyViewHolder4 onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_faces,parent,false);
        return new FaceADayAdapter.MyViewHolder4(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FaceADayAdapter.MyViewHolder4 holder, int position) {
        FacePicture facePicture = list.get(position);
        holder.day.setText(String.valueOf( facePicture.getDay()));
        holder.key=facePicture.getKey();
        holder.babyName=facePicture.getBabyname();
        Glide.with(context).load(list.get(position).getImageUrl()).into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class MyViewHolder4 extends RecyclerView.ViewHolder{
        TextView day;
        Button button;
        ImageView imageView;
        String key,babyName;
        public MyViewHolder4(@NonNull View itemView) {
            super(itemView);
            day=itemView.findViewById(R.id.textView18);
            imageView=itemView.findViewById(R.id.imageView3);
            button=itemView.findViewById(R.id.button);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                    FirebaseAuth mAuth;
                    mAuth = FirebaseAuth.getInstance();
                    DatabaseReference reference = database.getReference().child("Users").child(mAuth.getUid()).child(babyName).child("Face A Day").child(key);
                    reference.removeValue();
                }
            });

        }
    }
}
