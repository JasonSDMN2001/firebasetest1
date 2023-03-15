package com.unipi.boidanis.firebasetest1;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;


public class WeightFragment extends Fragment {
    FirebaseAuth mAuth;
    FirebaseUser user;
    FirebaseDatabase database;
    DatabaseReference ref,ref2,reference;
    ActivityResultLauncher<Intent> resultLauncher;
    private Date date,birthdate;
    private int week;
    private float weight;
    private String key;
    RecyclerView recyclerView;
    MyAdapter  myAdapter;
    ArrayList<WeightData>list;
    GraphView graphView;
    LineGraphSeries series,series2;
    //String[] babyName = new String[2];
    Button button;
    String babyname="";
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public WeightFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment WeightFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static WeightFragment newInstance(String param1, String param2) {
        WeightFragment fragment = new WeightFragment();
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
        View view =inflater.inflate(R.layout.fragment_weight, container, false);


        database = FirebaseDatabase.getInstance();
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();

        Spinner getbabyname = (Spinner) getActivity().findViewById(R.id.spinner);
        babyname = getbabyname.getSelectedItem().toString();
        whichBaby();

        /*ref = database.getReference("Users").child(mAuth.getUid());
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int index=0;
                for (DataSnapshot dataSnapshot: snapshot.getChildren()){
                    babyName[index] = dataSnapshot.getKey();

                    if(babyName[index]!=null){
                        RecyclerUpdate(index);
                        ref2 = database.getReference("Users").child(mAuth.getUid()).child(babyName[0]).child("weightData");
                        BirthDayFind(index);
                    }
                    index++;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });*/


        button = view.findViewById(R.id.button10);
        resultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode()== Activity.RESULT_OK){
                            Intent intent = result.getData();
                            weight = Float.parseFloat(intent.getStringExtra("weight"));
                            date = (Date)intent.getExtras().getSerializable("date");
                            week = WeekCalculation(date);
                            WeightData weightData = new WeightData(date,week,weight);
                            key = ref2.push().getKey();
                            ref2.child(key).setValue(weightData);
                            DatabaseReference reference = database.getReference("All Weight Data");
                            String key2 = reference.push().getKey();
                            reference.child(key2).setValue(weightData);
                        }
                    }
                });
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resultLauncher.launch(new Intent(getActivity(),MainActivity4.class));
            }
        });

        recyclerView = (RecyclerView)view.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        list =new ArrayList<>();
        myAdapter =new MyAdapter(getContext(),list);
        recyclerView.setAdapter(myAdapter);



        graphView=(GraphView) view.findViewById(R.id.graphview);
        series= new LineGraphSeries();
        graphView.addSeries(series);
        graphView.setTitle("Weight History");
        graphView.getGridLabelRenderer().setVerticalAxisTitle("Weight in kg");
        graphView.getGridLabelRenderer().setHorizontalAxisTitle("Week");
        graphView.getViewport().setScalable(true);
        graphView.getViewport().setScrollable(true);
        graphView.getViewport().setScalableY(true);
        graphView.getViewport().setScrollableY(true);
        return view;
    }

    private void whichBaby() {
        if(!babyname.matches("")){
            RecyclerUpdate(babyname);
            ref2 = database.getReference("Users").child(mAuth.getUid()).child(babyname).child("weightData");
            BirthDayFind(babyname);
        }
    }

    public void RecyclerUpdate(String s){
        reference = database.getReference("Users").child(mAuth.getUid()).child(s).child("weightData");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list.clear();
                DataPoint[] dp = new DataPoint[(int) snapshot.getChildrenCount()];
                int index = 0;

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    WeightData weightData = dataSnapshot.getValue(WeightData.class);
                    list.add(weightData);
                    if (dp.length > 2) {
                        WeightGraphPoints points = new WeightGraphPoints(weightData.getWeek(), weightData.getWeight());
                        dp[index] = new DataPoint(points.x, points.y);
                        index++;
                    }
                }
                if (dp.length > 2) {
                    series.resetData(dp);
                }
                myAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    public void BirthDayFind(String s){
        DatabaseReference reference2 = database.getReference("Users").child(mAuth.getUid()).child(s);
        reference2.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot: snapshot.getChildren()) {
                    if (!dataSnapshot.getKey().matches("weightData")) {
                        ChildInfo childInfo = dataSnapshot.getValue(ChildInfo.class);
                        birthdate = childInfo.birthDate;
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    public int WeekCalculation(Date weightDate){
            long i = birthdate.getTime();
            long j = weightDate.getTime();
            long daysDiff = TimeUnit.DAYS.convert(j-i, TimeUnit.MILLISECONDS);//604800//1 week
            long k = (long) Math.floor(daysDiff%7);

        return (int) k;
    }
    void showMessage(String title, String message){
        new AlertDialog.Builder(getActivity()).setTitle(title).setMessage(message).setCancelable(true).show();
    }
}