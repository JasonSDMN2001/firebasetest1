package com.unipi.boidanis.firebasetest1;

import android.app.Activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
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
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.LegendRenderer;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.unipi.boidanis.firebasetest1.databinding.ActivityMain3Binding;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Deque;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;


public class WeightFragment extends Fragment {
    FirebaseAuth mAuth;
    FirebaseUser user;
    FirebaseDatabase database;
    DatabaseReference ref, ref2, reference;
    ActivityResultLauncher<Intent> resultLauncher;
    private Date date, birthdate;
    private int week;
    private float weight;
    private String key,gender;
    RecyclerView recyclerView;
    MyAdapter myAdapter;
    ArrayList<WeightData> list;
    GraphView graphView;
    LineGraphSeries series, series2,series3,series4,series5,series6,series7,series8,series9,series10,series11;
    //String[] babyName = new String[2];
    Button button;
    String babyname = "";
    DataPoint[] weightdp = new DataPoint[14];
    Date[] temp_date;
    TextView textView9,textView10,textView15,textView16,textView23;
    BottomNavigationView bottomNavigationView;
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
        View view = inflater.inflate(R.layout.fragment_weight, container, false);


        database = FirebaseDatabase.getInstance();
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        bottomNavigationView = (BottomNavigationView) getActivity().findViewById(R.id.bottomNavigationView);
        Spinner getbabyname = (Spinner) getActivity().findViewById(R.id.spinner);
        babyname = getbabyname.getSelectedItem().toString();
        whichBaby();



        button = view.findViewById(R.id.button10);
        resultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == Activity.RESULT_OK) {
                            Intent intent = result.getData();
                            weight = Float.parseFloat(intent.getStringExtra("weight"));
                            date = (Date) intent.getExtras().getSerializable("date");
                            week = WeekCalculation(date);
                            key = ref2.push().getKey();
                            WeightData weightData = new WeightData(key,date, week, weight,babyname);
                            ref2.child(key).setValue(weightData);
                            StatisticsCalculation(weight,week);
                        }
                    }
                });
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!NetworkUtils.isNetworkAvailable(getActivity())) {
                    Toast.makeText(getActivity(), "No internet connection", Toast.LENGTH_SHORT).show();
                    onPause();
                }else{
                if(!babyname.matches("Select child")) {
                    Intent intent = new Intent(getActivity(), MainActivity4.class);
                    if(temp_date.length>0){
                        if (temp_date[temp_date.length - 1] != null) {
                            intent.putExtra("last date", temp_date[temp_date.length - 1]);
                        } else {
                            intent.putExtra("last date", birthdate);
                        }
                    } else {
                        intent.putExtra("last date", birthdate);
                    }
                    resultLauncher.launch(intent);
                }else{
                    Toast.makeText(getContext(), "Please select a child", Toast.LENGTH_SHORT).show();

                }
            }
            }
        });

        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        list = new ArrayList<>();
        myAdapter = new MyAdapter(getContext(), list);
        recyclerView.setAdapter(myAdapter);


        graphView = (GraphView) view.findViewById(R.id.graphview);
        series = new LineGraphSeries();
        series.setColor(Color.CYAN);
        series.setTitle(babyname+"'s weight");
        series.setThickness(14);

        graphView.addSeries(series);
        graphView.setTitle("Weight History");
        graphView.getGridLabelRenderer().setVerticalAxisTitle("Weight in kg");
        graphView.getGridLabelRenderer().setHorizontalAxisTitle("Week");
        graphView.getViewport().setScalable(true);
        graphView.getViewport().setScrollable(true);
        graphView.getViewport().setScalableY(true);
        graphView.getViewport().setScrollableY(true);
        series3 = new LineGraphSeries();
        series3.setColor(Color.YELLOW);
        series3.setTitle("Users average");
        series3.setThickness(14);
        graphView.addSeries(series3);
        series4 = new LineGraphSeries();
        series4.setTitle("3rd percentile");
        graphView.addSeries(series4);
        series5 = new LineGraphSeries();
        series5.setTitle("5th percentile");
        graphView.addSeries(series5);

        series6 = new LineGraphSeries();
        series6.setTitle("10th percentile");
        graphView.addSeries(series6);
        series7 = new LineGraphSeries();
        series7.setTitle("25th percentile");
        series7.setColor(Color.LTGRAY);
        graphView.addSeries(series7);

        series2 = new LineGraphSeries();
        series2.setTitle("50th percentile");
        graphView.addSeries(series2);

        series8 = new LineGraphSeries();
        series8.setTitle("75th percentile");
        series8.setColor(Color.LTGRAY);
        graphView.addSeries(series8);
        series9 = new LineGraphSeries();
        series9.setTitle("90th percentile");
        graphView.addSeries(series9);
        series10 = new LineGraphSeries();
        series10.setTitle("95th percentile");
        graphView.addSeries(series10);
        series11 = new LineGraphSeries();
        series11.setTitle("97th percentile");
        graphView.addSeries(series11);
        graphView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Toast.makeText(getContext(), "Pinch to zoom in/out", Toast.LENGTH_SHORT).show();

                return false;
            }
        });


        textView9 = view.findViewById(R.id.textView9);
        textView10 = view.findViewById(R.id.textView10);
        textView15=view.findViewById(R.id.textView15);
        textView16=view.findViewById(R.id.textView16);
        textView23 = view.findViewById(R.id.textView23);
        ImageButton imageButton = (ImageButton) view.findViewById(R.id.imageButton4);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showMessage("How to use growth chart:","press the button to add your " +
                        "baby's data,remember to add not only each week but twice in the first few " +
                        "days,as your baby's dimensions will change shortly after birth");
            }
        });
        if(!babyname.matches("Select child")){
            graphView.getLegendRenderer().setVisible(true);
            graphView.getLegendRenderer().setAlign(LegendRenderer.LegendAlign.TOP);
        }
        return view;
    }

    private void whichBaby() {
        if (!babyname.matches("")) {
            BirthDayFind(babyname);
            RecyclerUpdate(babyname);
            ref2 = database.getReference("Users").child(mAuth.getUid()).child(babyname).child("weightData");

        }
    }

    private void StatisticsCalculation(float weight,int week) {
        DatabaseReference statreference;
        if(week<10){
            statreference = database.getReference("All weight data").child(gender).child("week 0"+week).child("weight");
        }else{
            statreference = database.getReference("All weight data").child(gender).child("week "+week).child("weight");
        }
        statreference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                float stat_weight = Float.parseFloat(snapshot.getValue().toString());
                statreference.setValue(stat_weight+weight);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        DatabaseReference reference2;
        if(week<10){
            reference2 = database.getReference("All weight data").child(gender).child("week 0"+week).child("babies");
        }else{
            reference2 = database.getReference("All weight data").child(gender).child("week "+week).child("babies");
        }

        reference2.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int babies = Integer.parseInt(snapshot.getValue().toString());
                reference2.setValue(babies+1);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
    public void StatsGraph(String gender){
        DatabaseReference graphReference = database.getReference("All weight data").child(gender);
        graphReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                DataPoint[] dp =new DataPoint[(int)snapshot.getChildrenCount()];
                int index=0;
                for(DataSnapshot dataSnapshot:snapshot.getChildren()){
                    float found_weight =Float.parseFloat(dataSnapshot.child("weight").getValue().toString());
                    int baby_population = Integer.parseInt(dataSnapshot.child("babies").getValue().toString());
                    WeightGraphPoints points = new WeightGraphPoints(index,found_weight/baby_population);
                    dp[index] = new DataPoint(points.x, points.y);
                    index++;
                }
                series3.resetData(dp);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void RecyclerUpdate(String s) {
        try {
            reference = database.getReference("Users").child(mAuth.getUid()).child(s).child("weightData");
            reference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    list.clear();
                    DataPoint[] dp = new DataPoint[(int) snapshot.getChildrenCount()];
                    int index = 0;
                    temp_date = new Date[(int) snapshot.getChildrenCount()];
                    float[] temp_weight = new float[(int) snapshot.getChildrenCount()];
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        WeightData weightData = dataSnapshot.getValue(WeightData.class);
                        list.add(weightData);
                        temp_date[index] = weightData.getDate();
                        temp_weight[index] = weightData.getWeight();
                        if (dp.length > 2) {

                            WeightGraphPoints points = new WeightGraphPoints(weightData.getWeek(), weightData.getWeight());
                            dp[index] = new DataPoint(points.x, points.y);
                            index++;
                        }
                    }
                    if (dp.length > 2) {
                        if (weightdp[(int) (Math.floor((int) snapshot.getChildrenCount() / 4.0))] != null) {
                            if (Math.abs(dp[((int) snapshot.getChildrenCount()) - 1].getY()
                                    - weightdp[(int) (Math.floor((int) snapshot.getChildrenCount() / 4.0))].getY()) > 1.2) {
                                try{
                                WeightNotification();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                        if (dp[0] != null && temp_date[(int) snapshot.getChildrenCount() - 1] != null) {
                            try {
                                textView9.setText(String.format("%.2f", dp[0].getY()) + " kg");
                                textView15.setText(temp_weight[(int) snapshot.getChildrenCount() - 1] + " kg");
                                java.text.DateFormat dateFormat = android.text.format.DateFormat.getDateFormat(getContext());
                                textView16.setText(dateFormat.format(temp_date[(int) snapshot.getChildrenCount() - 1]));
                                textView23.setText(String.format("%.2f", temp_weight[(int) snapshot.getChildrenCount() - 1] - dp[0].getY()) + " kg");
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }

                        series.resetData(dp);
                    }
                    myAdapter.notifyDataSetChanged();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void WeightNotification() {
        buildAlertMessage("Would you like to learn ways to manage your child's weight?");
    }
    private void buildAlertMessage(String s) {
        final android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(getContext());
        builder.setMessage(s)
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        bottomNavigationView.setSelectedItemId(R.id.other);
                        FragmentTransaction fragmentTransaction = getActivity()
                                .getSupportFragmentManager().beginTransaction();
                        fragmentTransaction.replace(R.id.frame_layout, new GuidesFragment());
                        fragmentTransaction.commit();
                        /*FragmentTransaction fragmentTransaction2 = getActivity()
                                .getSupportFragmentManager().beginTransaction();
                        fragmentTransaction2.replace(R.id.frame_layout_guides, new weight_guideFragment());
                        fragmentTransaction2.commit();*/
                        showMessage("To access the weight guides","please press the weight button");
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        dialog.cancel();
                    }
                });
        final android.app.AlertDialog alert = builder.create();
        alert.show();
    }
    public void BirthDayFind(String s) {
        try {
            DatabaseReference reference2 = database.getReference("Users").child(mAuth.getUid()).child(s);
            reference2.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        if (!dataSnapshot.getKey().matches("weightData") &&
                                !dataSnapshot.getKey().matches("moments") &&
                                !dataSnapshot.getKey().matches("milestones") &&
                                !dataSnapshot.getKey().matches("Face A Day") &&
                                !dataSnapshot.getKey().matches("heightData")&&
                                !dataSnapshot.getKey().matches("headData")) {
                            ChildInfo childInfo = dataSnapshot.getValue(ChildInfo.class);
                            birthdate = childInfo.getbirthDate();
                            textView10.setText(DateFormat.format("dd/MM/yyyy",birthdate));
                            gender = childInfo.getGender();
                            StatsGraph(gender);
                            if (Objects.equals(gender, "Boy")) {
                                series2.setColor(Color.GREEN);
                                DataPoint[] dp = new DataPoint[14];
                                dp[0] = new DataPoint(0, 3.530203);
                                dp[1] = new DataPoint(4, 4.879525);
                                dp[2] = new DataPoint(8, 5.672889);
                                dp[3] = new DataPoint(12, 6.391392);
                                dp[4] = new DataPoint(16, 7.041836);
                                dp[5] = new DataPoint(20, 7.630425);
                                dp[6] = new DataPoint(24, 8.162951);
                                dp[7] = new DataPoint(28, 8.644832);
                                dp[8] = new DataPoint(32, 9.08112);
                                dp[9] = new DataPoint(36, 9.4765);
                                dp[10] = new DataPoint(40, 9.835308);
                                dp[11] = new DataPoint(44, 10.16154);
                                dp[12] = new DataPoint(48, 10.45885);
                                dp[13] = new DataPoint(52, 10.73063);
                                weightdp = dp;
                                series2.resetData(dp);
                                series4.setColor(Color.RED);
                                Paint dottedLinePaint = new Paint();
                                dottedLinePaint.setColor(Color.RED);
                                dottedLinePaint.setStyle(Paint.Style.STROKE);
                                dottedLinePaint.setStrokeWidth(11);
                                dottedLinePaint.setPathEffect(new DashPathEffect(new float[]{10, 20}, 0));
                                series4.setCustomPaint(dottedLinePaint);
                                DataPoint[] dp4 = new DataPoint[14];
                                dp4[0] = new DataPoint(0, 2.35);
                                dp4[1] = new DataPoint(4, 3.5);
                                dp4[2] = new DataPoint(8, 4.3);
                                dp4[3] = new DataPoint(12, 4.9);
                                dp4[4] = new DataPoint(16, 5.57);
                                dp4[5] = new DataPoint(20, 6.09);
                                dp4[6] = new DataPoint(24, 6.5);
                                dp4[7] = new DataPoint(28, 6.98);
                                dp4[8] = new DataPoint(32, 7.36);
                                dp4[9] = new DataPoint(36, 7.7);
                                dp4[10] = new DataPoint(40, 8.0);
                                dp4[11] = new DataPoint(44, 8.2);
                                dp4[12] = new DataPoint(48, 8.53);
                                dp4[13] = new DataPoint(52, 8.76);
                                series4.resetData(dp4);
                                series5.setColor(Color.MAGENTA);
                                Paint dottedLinePaint2 = new Paint();
                                dottedLinePaint2.setColor(Color.MAGENTA);
                                dottedLinePaint2.setStyle(Paint.Style.STROKE);
                                dottedLinePaint2.setStrokeWidth(11);
                                dottedLinePaint2.setPathEffect(new DashPathEffect(new float[]{10, 20}, 0));
                                series5.setCustomPaint(dottedLinePaint2);
                                DataPoint[] dp5 = new DataPoint[14];
                                dp5[0] = new DataPoint(0, 2.52);
                                dp5[1] = new DataPoint(4, 3.77);
                                dp5[2] = new DataPoint(8, 4.5);
                                dp5[3] = new DataPoint(12, 5.1);
                                dp5[4] = new DataPoint(16, 5.7);
                                dp5[5] = new DataPoint(20, 6.2);
                                dp5[6] = new DataPoint(24, 6.74);
                                dp5[7] = new DataPoint(28, 7.17);
                                dp5[8] = new DataPoint(32, 7.55);
                                dp5[9] = new DataPoint(36, 7.9);
                                dp5[10] = new DataPoint(40, 8.2);
                                dp5[11] = new DataPoint(44, 8.4);
                                dp5[12] = new DataPoint(48, 8.75);
                                dp5[13] = new DataPoint(52, 8.9);
                                series5.resetData(dp5);
                                series6.setColor(Color.BLUE);
                                Paint dottedLinePaint3 = new Paint();
                                dottedLinePaint3.setColor(Color.BLUE);
                                dottedLinePaint3.setStyle(Paint.Style.STROKE);
                                dottedLinePaint3.setStrokeWidth(11);
                                dottedLinePaint3.setPathEffect(new DashPathEffect(new float[]{10, 20}, 0));
                                series6.setCustomPaint(dottedLinePaint3);
                                DataPoint[] dp6 = new DataPoint[14];
                                dp6[0] = new DataPoint(0, 2.773802);
                                dp6[1] = new DataPoint(4, 4.020561);
                                dp6[2] = new DataPoint(8, 4.754479);
                                dp6[3] = new DataPoint(12, 5.416803);
                                dp6[4] = new DataPoint(16, 6.013716);
                                dp6[5] = new DataPoint(20, 6.551379);
                                dp6[6] = new DataPoint(24, 7.035656);
                                dp6[7] = new DataPoint(28, 7.472021);
                                dp6[8] = new DataPoint(32, 7.865533);
                                dp6[9] = new DataPoint(36, 8.220839);
                                dp6[10] = new DataPoint(40, 8.542195);
                                dp6[11] = new DataPoint(44, 8.833486);
                                dp6[12] = new DataPoint(48, 9.098246);
                                dp6[13] = new DataPoint(52, 9.339688);
                                series6.resetData(dp6);
                                Paint dottedLinePaint4 = new Paint();
                                dottedLinePaint4.setColor(Color.LTGRAY);
                                dottedLinePaint4.setStyle(Paint.Style.STROKE);
                                dottedLinePaint4.setStrokeWidth(11);
                                dottedLinePaint4.setPathEffect(new DashPathEffect(new float[]{10, 20}, 0));
                                series7.setCustomPaint(dottedLinePaint4);
                                DataPoint[] dp7 = new DataPoint[14];
                                dp7[0] = new DataPoint(0, 3.150611);
                                dp7[1] = new DataPoint(4, 4.428873);
                                dp7[2] = new DataPoint(8, 5.183378);
                                dp7[3] = new DataPoint(12, 5.866806);
                                dp7[4] = new DataPoint(16, 6.484969);
                                dp7[5] = new DataPoint(20, 7.043627);
                                dp7[6] = new DataPoint(24, 7.548346);
                                dp7[7] = new DataPoint(28, 8.004399);
                                dp7[8] = new DataPoint(32, 8.416719);
                                dp7[9] = new DataPoint(36, 8.789882);
                                dp7[10] = new DataPoint(40, 9.12811);
                                dp7[11] = new DataPoint(44, 9.435279);
                                dp7[12] = new DataPoint(48, 9.714942);
                                dp7[13] = new DataPoint(52, 9.970338);
                                series7.resetData(dp7);
                                Paint dottedLinePaint6 = new Paint();
                                dottedLinePaint6.setColor(Color.LTGRAY);
                                dottedLinePaint6.setStyle(Paint.Style.STROKE);
                                dottedLinePaint6.setStrokeWidth(11);
                                dottedLinePaint6.setPathEffect(new DashPathEffect(new float[]{10, 20}, 0));
                                series8.setCustomPaint(dottedLinePaint6);
                                DataPoint[] dp8 = new DataPoint[14];
                                dp8[0] = new DataPoint(0, 3.979077);
                                dp8[1] = new DataPoint(4, 5.427328);
                                dp8[2] = new DataPoint(8, 6.275598);
                                dp8[3] = new DataPoint(12, 7.042217);
                                dp8[4] = new DataPoint(16, 7.735323);
                                dp8[5] = new DataPoint(20, 8.262033);
                                dp8[6] = new DataPoint(24, 8.828786);
                                dp8[7] = new DataPoint(28, 9.34149);
                                dp8[8] = new DataPoint(32, 9.805593);
                                dp8[9] = new DataPoint(36, 10.22612);
                                dp8[10] = new DataPoint(40, 10.60772);
                                dp8[11] = new DataPoint(44, 10.95466);
                                dp8[12] = new DataPoint(48, 11.27087);
                                dp8[13] = new DataPoint(52, 11.55996);
                                series8.resetData(dp8);
                                series9.setColor(Color.BLUE);
                                Paint dottedLinePaint5 = new Paint();
                                dottedLinePaint5.setColor(Color.BLUE);
                                dottedLinePaint5.setStyle(Paint.Style.STROKE);
                                dottedLinePaint5.setStrokeWidth(11);
                                dottedLinePaint5.setPathEffect(new DashPathEffect(new float[]{10, 20}, 0));
                                series9.setCustomPaint(dottedLinePaint5);
                                DataPoint[] dp9 = new DataPoint[14];
                                dp9[0] = new DataPoint(0, 4.172493);
                                dp9[1] = new DataPoint(4, 5.728153);
                                dp9[2] = new DataPoint(8, 6.638979);
                                dp9[3] = new DataPoint(12, 7.460702);
                                dp9[4] = new DataPoint(16, 8.202193);
                                dp9[5] = new DataPoint(20, 8.871384);
                                dp9[6] = new DataPoint(24, 9.475466);
                                dp9[7] = new DataPoint(28, 10.02101);
                                dp9[8] = new DataPoint(32, 10.51406);
                                dp9[9] = new DataPoint(36, 10.96017);
                                dp9[10] = new DataPoint(40, 11.36445);
                                dp9[11] = new DataPoint(44, 11.7316);
                                dp9[12] = new DataPoint(48, 12.06595);
                                dp9[13] = new DataPoint(52, 12.37145);
                                series9.resetData(dp9);
                                series10.setColor(Color.MAGENTA);
                                Paint dottedLinePaint7 = new Paint();
                                dottedLinePaint7.setColor(Color.MAGENTA);
                                dottedLinePaint7.setStyle(Paint.Style.STROKE);
                                dottedLinePaint7.setStrokeWidth(11);
                                dottedLinePaint7.setPathEffect(new DashPathEffect(new float[]{10, 20}, 0));
                                series10.setCustomPaint(dottedLinePaint7);
                                DataPoint[] dp10 = new DataPoint[14];
                                dp10[0] = new DataPoint(0, 4.340293);
                                dp10[1] = new DataPoint(4, 5.967102);
                                dp10[2] = new DataPoint(8, 6.921119);
                                dp10[3] = new DataPoint(12, 7.781401);
                                dp10[4] = new DataPoint(16, 8.556813);
                                dp10[5] = new DataPoint(20, 9.255615);
                                dp10[6] = new DataPoint(24, 9.885436);
                                dp10[7] = new DataPoint(28, 10.45331);
                                dp10[8] = new DataPoint(32, 10.96574);
                                dp10[9] = new DataPoint(36, 11.42868);
                                dp10[10] = new DataPoint(40, 11.84763);
                                dp10[11] = new DataPoint(44, 12.22766);
                                dp10[12] = new DataPoint(48, 12.5734);
                                dp10[13] = new DataPoint(52, 12.88911);
                                series10.resetData(dp10);
                                series11.setColor(Color.RED);
                                Paint dottedLinePaint8 = new Paint();
                                dottedLinePaint8.setColor(Color.RED);
                                dottedLinePaint8.setStyle(Paint.Style.STROKE);
                                dottedLinePaint8.setStrokeWidth(11);
                                dottedLinePaint8.setPathEffect(new DashPathEffect(new float[]{10, 20}, 0));
                                series11.setCustomPaint(dottedLinePaint8);
                                DataPoint[] dp11 = new DataPoint[14];
                                dp11[0] = new DataPoint(0, 4.446488);
                                dp11[1] = new DataPoint(4, 6.121929);
                                dp11[2] = new DataPoint(8, 7.10625);
                                dp11[3] = new DataPoint(12, 7.993878);
                                dp11[4] = new DataPoint(16, 8.793444);
                                dp11[5] = new DataPoint(20, 9.513307);
                                dp11[6] = new DataPoint(24, 10.16135);
                                dp11[7] = new DataPoint(28, 10.74492);
                                dp11[8] = new DataPoint(32, 11.27084);
                                dp11[9] = new DataPoint(36, 11.74538);
                                dp11[10] = new DataPoint(40, 12.17436);
                                dp11[11] = new DataPoint(44, 12.56308);
                                dp11[12] = new DataPoint(48, 12.91645);
                                dp11[13] = new DataPoint(52, 13.23893);
                                series11.resetData(dp11);
                            } else if (Objects.equals(gender, "Girl")) {
                                series2.setColor(Color.MAGENTA);
                                DataPoint[] dp = new DataPoint[14];
                                dp[0] = new DataPoint(0, 3.399186);
                                dp[1] = new DataPoint(4, 4.544777);
                                dp[2] = new DataPoint(8, 5.230584);
                                dp[3] = new DataPoint(12, 5.859961);
                                dp[4] = new DataPoint(16, 6.437588);
                                dp[5] = new DataPoint(20, 6.96785);
                                dp[6] = new DataPoint(24, 7.454854);
                                dp[7] = new DataPoint(28, 7.902436);
                                dp[8] = new DataPoint(32, 8.314178);
                                dp[9] = new DataPoint(36, 8.693418);
                                dp[10] = new DataPoint(40, 9.043262);
                                dp[11] = new DataPoint(44, 9.366594);
                                dp[12] = new DataPoint(48, 9.666089);
                                dp[13] = new DataPoint(52, 9.944226);
                                weightdp = dp;
                                series2.resetData(dp);
                                series4.setColor(Color.RED);
                                Paint dottedLinePaint8 = new Paint();
                                dottedLinePaint8.setColor(Color.RED);
                                dottedLinePaint8.setStyle(Paint.Style.STROKE);
                                dottedLinePaint8.setStrokeWidth(11);
                                dottedLinePaint8.setPathEffect(new DashPathEffect(new float[]{10, 20}, 0));
                                series4.setCustomPaint(dottedLinePaint8);
                                DataPoint[] dp4 = new DataPoint[14];
                                dp4[0] = new DataPoint(0, 2.414112);
                                dp4[1] = new DataPoint(4, 3.402293);
                                dp4[2] = new DataPoint(8, 3.997806);
                                dp4[3] = new DataPoint(12, 4.547383);
                                dp4[4] = new DataPoint(16, 5.054539);
                                dp4[5] = new DataPoint(20, 5.5225);
                                dp4[6] = new DataPoint(24, 5.954272);
                                dp4[7] = new DataPoint(28, 6.352668);
                                dp4[8] = new DataPoint(32, 6.720328);
                                dp4[9] = new DataPoint(36, 7.059732);
                                dp4[10] = new DataPoint(40, 7.373212);
                                dp4[11] = new DataPoint(44, 7.662959);
                                dp4[12] = new DataPoint(48, 7.93103);
                                dp4[13] = new DataPoint(52, 8.179356);
                                series4.resetData(dp4);
                                series5.setColor(Color.BLUE);
                                Paint dottedLinePaint1 = new Paint();
                                dottedLinePaint1.setColor(Color.BLUE);
                                dottedLinePaint1.setStyle(Paint.Style.STROKE);
                                dottedLinePaint1.setStrokeWidth(11);
                                dottedLinePaint1.setPathEffect(new DashPathEffect(new float[]{10, 20}, 0));
                                series5.setCustomPaint(dottedLinePaint1);
                                DataPoint[] dp5 = new DataPoint[14];
                                dp5[0] = new DataPoint(0, 2.547905);
                                dp5[1] = new DataPoint(4, 3.54761);
                                dp5[2] = new DataPoint(8, 4.150639);
                                dp5[3] = new DataPoint(12, 4.707123);
                                dp5[4] = new DataPoint(16, 5.220488);
                                dp5[5] = new DataPoint(20, 5.693974);
                                dp5[6] = new DataPoint(24, 6.130641);
                                dp5[7] = new DataPoint(28, 6.533373);
                                dp5[8] = new DataPoint(32, 6.904886);
                                dp5[9] = new DataPoint(36, 7.247736);
                                dp5[10] = new DataPoint(40, 7.564327);
                                dp5[11] = new DataPoint(44, 7.856916);
                                dp5[12] = new DataPoint(48, 8.127621);
                                dp5[13] = new DataPoint(52, 8.378425);
                                series5.resetData(dp5);
                                series6.setColor(Color.LTGRAY);
                                Paint dottedLinePaint2 = new Paint();
                                dottedLinePaint2.setColor(Color.LTGRAY);
                                dottedLinePaint2.setStyle(Paint.Style.STROKE);
                                dottedLinePaint2.setStrokeWidth(11);
                                dottedLinePaint2.setPathEffect(new DashPathEffect(new float[]{10, 20}, 0));
                                series6.setCustomPaint(dottedLinePaint2);
                                DataPoint[] dp6 = new DataPoint[14];
                                dp6[0] = new DataPoint(0, 2.747222);
                                dp6[1] = new DataPoint(4, 3.770157);
                                dp6[2] = new DataPoint(8, 4.387042);
                                dp6[3] = new DataPoint(12, 4.955926);
                                dp6[4] = new DataPoint(16, 5.480295);
                                dp6[5] = new DataPoint(20, 5.96351);
                                dp6[6] = new DataPoint(24, 6.408775);
                                dp6[7] = new DataPoint(28, 6.819122);
                                dp6[8] = new DataPoint(32, 7.197414);
                                dp6[9] = new DataPoint(36, 7.546342);
                                dp6[10] = new DataPoint(40, 7.868436);
                                dp6[11] = new DataPoint(44, 8.166069);
                                dp6[12] = new DataPoint(48, 8.44146);
                                dp6[13] = new DataPoint(52, 8.696684);
                                series6.resetData(dp6);
                                series7.setColor(Color.GREEN);
                                Paint dottedLinePaint3 = new Paint();
                                dottedLinePaint3.setColor(Color.GREEN);
                                dottedLinePaint3.setStyle(Paint.Style.STROKE);
                                dottedLinePaint3.setStrokeWidth(11);
                                dottedLinePaint3.setPathEffect(new DashPathEffect(new float[]{10, 20}, 0));
                                series7.setCustomPaint(dottedLinePaint3);
                                DataPoint[] dp7 = new DataPoint[14];
                                dp7[0] = new DataPoint(0, 3.064865);
                                dp7[1] = new DataPoint(4, 4.138994);
                                dp7[2] = new DataPoint(8, 4.78482);
                                dp7[3] = new DataPoint(12, 5.379141);
                                dp7[4] = new DataPoint(16, 5.925888);
                                dp7[5] = new DataPoint(20, 6.428828);
                                dp7[6] = new DataPoint(24, 6.891533);
                                dp7[7] = new DataPoint(28, 7.317373);
                                dp7[8] = new DataPoint(32, 7.709516);
                                dp7[9] = new DataPoint(36, 8.070932);
                                dp7[10] = new DataPoint(40, 8.4044);
                                dp7[11] = new DataPoint(44, 8.712513);
                                dp7[12] = new DataPoint(48, 8.997692);
                                dp7[13] = new DataPoint(52, 9.262185);
                                series7.resetData(dp7);
                                series8.setColor(Color.GREEN);
                                Paint dottedLinePaint4 = new Paint();
                                dottedLinePaint4.setColor(Color.GREEN);
                                dottedLinePaint4.setStyle(Paint.Style.STROKE);
                                dottedLinePaint4.setStrokeWidth(11);
                                dottedLinePaint4.setPathEffect(new DashPathEffect(new float[]{10, 20}, 0));
                                series8.setCustomPaint(dottedLinePaint4);
                                DataPoint[] dp8 = new DataPoint[14];
                                dp8[0] = new DataPoint(0, 3.717519);
                                dp8[1] = new DataPoint(4, 4.946766);
                                dp8[2] = new DataPoint(8, 5.680083);
                                dp8[3] = new DataPoint(12, 6.351512);
                                dp8[4] = new DataPoint(16, 6.966524);
                                dp8[5] = new DataPoint(20, 7.53018);
                                dp8[6] = new DataPoint(24, 8.047178);
                                dp8[7] = new DataPoint(28, 8.521877);
                                dp8[8] = new DataPoint(32, 8.958324);
                                dp8[9] = new DataPoint(36, 9.360271);
                                dp8[10] = new DataPoint(40, 9.731193);
                                dp8[11] = new DataPoint(44, 10.07431);
                                dp8[12] = new DataPoint(48, 10.39258);
                                dp8[13] = new DataPoint(52, 10.68874);
                                series8.resetData(dp8);
                                series9.setColor(Color.LTGRAY);
                                Paint dottedLinePaint5 = new Paint();
                                dottedLinePaint5.setColor(Color.LTGRAY);
                                dottedLinePaint5.setStyle(Paint.Style.STROKE);
                                dottedLinePaint5.setStrokeWidth(11);
                                dottedLinePaint5.setPathEffect(new DashPathEffect(new float[]{10, 20}, 0));
                                series9.setCustomPaint(dottedLinePaint5);
                                DataPoint[] dp9 = new DataPoint[14];
                                dp9[0] = new DataPoint(0, 3.992572);
                                dp9[1] = new DataPoint(4, 5.305632);
                                dp9[2] = new DataPoint(8, 6.087641);
                                dp9[3] = new DataPoint(12, 6.80277);
                                dp9[4] = new DataPoint(16, 7.457119);
                                dp9[5] = new DataPoint(20, 8.056331);
                                dp9[6] = new DataPoint(24, 8.605636);
                                dp9[7] = new DataPoint(28, 9.109878);
                                dp9[8] = new DataPoint(32, 9.573546);
                                dp9[9] = new DataPoint(36, 10.00079);
                                dp9[10] = new DataPoint(40, 10.39545);
                                dp9[11] = new DataPoint(44, 10.76106);
                                dp9[12] = new DataPoint(48, 11.10089);
                                dp9[13] = new DataPoint(52, 11.41792);
                                series9.resetData(dp9);
                                series10.setColor(Color.BLUE);
                                Paint dottedLinePaint6 = new Paint();
                                dottedLinePaint6.setColor(Color.BLUE);
                                dottedLinePaint6.setStyle(Paint.Style.STROKE);
                                dottedLinePaint6.setStrokeWidth(11);
                                dottedLinePaint6.setPathEffect(new DashPathEffect(new float[]{10, 20}, 0));
                                series10.setCustomPaint(dottedLinePaint6);
                                DataPoint[] dp10 = new DataPoint[14];
                                dp10[0] = new DataPoint(0, 4.152637);
                                dp10[1] = new DataPoint(4, 5.519169);
                                dp10[2] = new DataPoint(8, 6.332837);
                                dp10[3] = new DataPoint(12, 7.076723);
                                dp10[4] = new DataPoint(16, 7.757234);
                                dp10[5] = new DataPoint(20, 8.38033);
                                dp10[6] = new DataPoint(24, 8.951544);
                                dp10[7] = new DataPoint(28, 9.476009);
                                dp10[8] = new DataPoint(32, 9.95848);
                                dp10[9] = new DataPoint(36, 10.40335);
                                dp10[10] = new DataPoint(40, 10.8147);
                                dp10[11] = new DataPoint(44, 11.19625);
                                dp10[12] = new DataPoint(48, 11.55145);
                                dp10[13] = new DataPoint(52, 11.88348);
                                series10.resetData(dp10);
                                series11.setColor(Color.RED);
                                Paint dottedLinePaint7 = new Paint();
                                dottedLinePaint7.setColor(Color.RED);
                                dottedLinePaint7.setStyle(Paint.Style.STROKE);
                                dottedLinePaint7.setStrokeWidth(11);
                                dottedLinePaint7.setPathEffect(new DashPathEffect(new float[]{10, 20}, 0));
                                series11.setCustomPaint(dottedLinePaint7);
                                DataPoint[] dp11 = new DataPoint[14];
                                dp11[0] = new DataPoint(0, 4.254922);
                                dp11[1] = new DataPoint(4, 5.657379);
                                dp11[2] = new DataPoint(8, 6.492574);
                                dp11[3] = new DataPoint(12, 7.256166);
                                dp11[4] = new DataPoint(16, 7.95473);
                                dp11[5] = new DataPoint(20, 8.594413);
                                dp11[6] = new DataPoint(24, 9.180938);
                                dp11[7] = new DataPoint(28, 9.719621);
                                dp11[8] = new DataPoint(32, 10.21539);
                                dp11[9] = new DataPoint(36, 10.6728);
                                dp11[10] = new DataPoint(40, 11.09607);
                                dp11[11] = new DataPoint(44, 11.48908);
                                dp11[12] = new DataPoint(48, 11.85539);
                                dp11[13] = new DataPoint(52, 12.19829);
                                series11.resetData(dp11);
                            }
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public int WeekCalculation(Date weightDate) {
        long i = birthdate.getTime();
        long j = weightDate.getTime();
        long daysDiff = TimeUnit.DAYS.convert(j - i, TimeUnit.MILLISECONDS);//604800//1 week
        long k = (long) Math.floor(daysDiff / 7.0);
        return (int) k;
    }
    void showMessage(String title, String message) {
        new AlertDialog.Builder(getActivity()).setTitle(title).setMessage(message).setCancelable(true).show();
    }


}