package com.unipi.boidanis.firebasetest1;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.icu.util.Calendar;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */

public class HomeFragment extends Fragment {
    FirebaseAuth mAuth;
    FirebaseDatabase database;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        TextView dateTimeDisplay = (TextView) view.findViewById(R.id.textView2);
        dateTimeDisplay.setText("" + DateFormat.format("EEE,d MMM", System.currentTimeMillis()));
        ShapeableImageView shapeableImageView = (ShapeableImageView) view.findViewById(R.id.profile_button3);
        shapeableImageView.setScaleType(ImageView.ScaleType.CENTER);
        shapeableImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
                Intent intent = new Intent(getContext(), MainActivity5.class);
                startActivity(intent);
            }
        });


        //shapeableImageView.setImageURI(null);
        database = FirebaseDatabase.getInstance();
        mAuth = FirebaseAuth.getInstance();
        Spinner getbabyname = (Spinner) getActivity().findViewById(R.id.spinner);
        String babyname = getbabyname.getSelectedItem().toString();
        DatabaseReference reference = database.getReference("Users").child(mAuth.getUid()).child(babyname);
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot: snapshot.getChildren()) {
                    if (!dataSnapshot.getKey().matches("weightData")&&
                            !dataSnapshot.getKey().matches("milestones")&&
                            !dataSnapshot.getKey().matches("moments")&&
                    !dataSnapshot.getKey().matches("Face A Day")&&
                            !dataSnapshot.getKey().matches("heightData")&&
                            !dataSnapshot.getKey().matches("headData")) {
                        ChildInfo childInfo = dataSnapshot.getValue(ChildInfo.class);
                        if(getActivity()!=null) {
                            Glide.with(getContext()).load(childInfo.getImageUrl()).into(shapeableImageView);
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        ShapeableImageView shapeableImageView2 = (ShapeableImageView) view.findViewById(R.id.profile_button2);
        shapeableImageView2.setScaleType(ImageView.ScaleType.CENTER);
        shapeableImageView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getContext(), MainActivity6.class);
                startActivity(intent);
            }
        });
        DatabaseReference ref2 = database.getReference("Users").child(mAuth.getUid()).child("parent info and settings");
        ref2.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot:snapshot.getChildren()){
                    ParentInfo parentInfo = dataSnapshot.getValue(ParentInfo.class);
                    String s = parentInfo.getImageUrl();
                    if(getActivity()!=null){
                        Glide.with(getContext()).load(s).into(shapeableImageView2);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        ShapeableImageView shapeableImageView3 = (ShapeableImageView) view.findViewById(R.id.profile_button4);
        shapeableImageView3.setScaleType(ImageView.ScaleType.CENTER);
        shapeableImageView3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                CustomDialog2 dialog = new CustomDialog2();
                dialog.show(getChildFragmentManager(), "custom_dialog2");

            }
        });
        DatabaseReference ref3 = database.getReference("Users").child(mAuth.getUid()).child("togetherpicture").child("image");
        ref3.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot:snapshot.getChildren()){
                    String s = dataSnapshot.toString();
                    if(getActivity()!=null){
                        Glide.with(getContext()).load(s).into(shapeableImageView3);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        return view;
    }

}