package com.unipi.boidanis.firebasetest1;

import android.app.Activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
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

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HeadFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HeadFragment extends Fragment implements CustomDialog.CustomDialogListener  {
    FirebaseAuth mAuth;
    FirebaseUser user;
    FirebaseDatabase database;
    DatabaseReference ref, ref2, reference;
    ActivityResultLauncher<Intent> resultLauncher;
    private Date date, birthdate;
    private int week;
    private float head;
    private String key,gender;
    RecyclerView recyclerView;
    HeadAdapter headAdapter;
    ArrayList<HeadData> list;
    GraphView graphView;
    LineGraphSeries series, series2,series3,series4,series5,series6,series7,series8,series9,series10,series11;
    Button button;
    String babyname = "";
    DataPoint[] headdp = new DataPoint[14];
    Date[] temp_date;
    TextView textView9,textView10,textView15,textView16,textView23;
    BottomNavigationView bottomNavigationView;
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    public HeadFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HeadFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HeadFragment newInstance(String param1, String param2) {
        HeadFragment fragment = new HeadFragment();
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
        View view=inflater.inflate(R.layout.fragment_head, container, false);
        database = FirebaseDatabase.getInstance();
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        bottomNavigationView = (BottomNavigationView) getActivity().findViewById(R.id.bottomNavigationView);
        Spinner getbabyname = (Spinner) getActivity().findViewById(R.id.spinner);
        babyname = getbabyname.getSelectedItem().toString();
        whichBaby();



        button = view.findViewById(R.id.button10);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if(!babyname.matches("Select child")) {

                    CustomDialog dialog = new CustomDialog();
                    if(temp_date.length>0){
                        if (temp_date[temp_date.length - 1] != null) {
                            dialog.setLastDate(temp_date[temp_date.length - 1]);
                        } else {
                            dialog.setLastDate(birthdate);
                        }
                    } else {
                        dialog.setLastDate(birthdate);
                    }
                    dialog.setCustomDialogListener(HeadFragment.this);
                    dialog.setHint("Head circumference");
                    dialog.setCondition("^(3[1-9]\\.[0-9]|4[0-8]\\.[0-9]|48\\.9)$");
                    dialog.setError_message("circumference must be between 31.4-48.9 cm");
                    dialog.show(getChildFragmentManager(), "custom_dialog");

                }else{
                    Toast.makeText(getContext(), "Please select a child", Toast.LENGTH_SHORT).show();


                }

            }
        });

        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        list = new ArrayList<>();
        headAdapter = new HeadAdapter(getContext(), list);
        recyclerView.setAdapter(headAdapter);


        graphView = (GraphView) view.findViewById(R.id.graphview);
        series = new LineGraphSeries();
        series.setColor(Color.CYAN);
        series.setTitle(babyname+"'s perimeter");
        series.setThickness(14);

        graphView.addSeries(series);
        graphView.setTitle("circumference History");
        graphView.getGridLabelRenderer().setVerticalAxisTitle("circumference in cm");
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
        graphView.addSeries(series7);

        series2 = new LineGraphSeries();
        series2.setTitle("50th percentile");
        graphView.addSeries(series2);

        series8 = new LineGraphSeries();
        series8.setTitle("75th percentile");
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
    @Override
    public void onDialogResult(Date selectedDate,String returned_head) {
        head = Float.parseFloat(returned_head);
        date = selectedDate;
        week = WeekCalculation(date);
        key = ref2.push().getKey();
        HeadData headData = new HeadData(key,date, week, head,babyname);
        ref2.child(key).setValue(headData);
        StatisticsCalculation(head,week);
    }
    private void whichBaby() {
        if (!babyname.matches("")) {
            BirthDayFind(babyname);
            RecyclerUpdate(babyname);
            ref2 = database.getReference("Users").child(mAuth.getUid()).child(babyname).child("headData");

        }
    }

    private void StatisticsCalculation(float weight,int week) {
        try {
            DatabaseReference statreference;
            if (week < 10) {
                statreference = database.getReference("All head data").child(gender).child("week 0" + week).child("head");
            } else {
                statreference = database.getReference("All head data").child(gender).child("week " + week).child("head");
            }
            statreference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    float stat_head = Float.parseFloat(snapshot.getValue().toString());
                    statreference.setValue(stat_head + head);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
            DatabaseReference reference2;
            if (week < 10) {
                reference2 = database.getReference("All head data").child(gender).child("week 0" + week).child("babies");
            } else {
                reference2 = database.getReference("All head data").child(gender).child("week " + week).child("babies");
            }

            reference2.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    int babies = Integer.parseInt(snapshot.getValue().toString());
                    reference2.setValue(babies + 1);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void StatsGraph(String gender){
        DatabaseReference graphReference = database.getReference("All head data").child(gender);
        graphReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                DataPoint[] dp =new DataPoint[(int)snapshot.getChildrenCount()];
                int index=0;
                for(DataSnapshot dataSnapshot:snapshot.getChildren()){
                    float found_head =Float.parseFloat(dataSnapshot.child("head").getValue().toString());
                    int baby_population = Integer.parseInt(dataSnapshot.child("babies").getValue().toString());
                    WeightGraphPoints points = new WeightGraphPoints(index,found_head/baby_population);
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
            reference = database.getReference("Users").child(mAuth.getUid()).child(s).child("headData");
            reference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    list.clear();
                    DataPoint[] dp = new DataPoint[(int) snapshot.getChildrenCount()];
                    int index = 0;
                    temp_date = new Date[(int) snapshot.getChildrenCount()];
                    float[] temp_head = new float[(int) snapshot.getChildrenCount()];
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        HeadData headData = dataSnapshot.getValue(HeadData.class);
                        list.add(headData);
                        temp_date[index] = headData.getDate();
                        temp_head[index] = headData.getHead();
                        if (dp.length > 2) {

                            WeightGraphPoints points = new WeightGraphPoints(headData.getWeek(), headData.getHead());
                            dp[index] = new DataPoint(points.x, points.y);
                            index++;
                        }
                    }
                    if (dp.length > 2) {
                        if (headdp[(int) (Math.floor((int) snapshot.getChildrenCount() / 4.0))] != null) {
                            if (Math.abs(dp[((int) snapshot.getChildrenCount()) - 1].getY()
                                    - headdp[(int) (Math.floor((int) snapshot.getChildrenCount() / 4.0))].getY()) > 2.5) {
                                try{HeadNotification();} catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                        if (dp[0] != null && temp_date[(int) snapshot.getChildrenCount() - 1] != null) {
                            try {
                                textView9.setText(String.format("%.2f", dp[0].getY()) + " kg");
                                textView15.setText(temp_head[(int) snapshot.getChildrenCount() - 1] + " kg");
                                java.text.DateFormat dateFormat = android.text.format.DateFormat.getDateFormat(getContext());
                                textView16.setText(dateFormat.format(temp_date[(int) snapshot.getChildrenCount() - 1]));
                                textView23.setText(String.format("%.2f", temp_head[(int) snapshot.getChildrenCount() - 1] - dp[0].getY()) + " cm");
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }

                        series.resetData(dp);
                    }
                    headAdapter.notifyDataSetChanged();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void HeadNotification() {
        buildAlertMessage("Would you like to learn ways to manage your child's head circumference?");
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
                        showMessage("To access the head guides","please press the head button");
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
        DatabaseReference reference2 = database.getReference("Users").child(mAuth.getUid()).child(s);
        reference2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    if (!dataSnapshot.getKey().matches("weightData")&&
                            !dataSnapshot.getKey().matches("moments")&&
                            !dataSnapshot.getKey().matches("milestones")&&
                            !dataSnapshot.getKey().matches("Face A Day")&&
                            !dataSnapshot.getKey().matches("heightData")&&
                            !dataSnapshot.getKey().matches("headData")) {
                        ChildInfo childInfo = dataSnapshot.getValue(ChildInfo.class);
                        birthdate = childInfo.getbirthDate();
                        textView10.setText(DateFormat.format("dd/MM/yyyy",birthdate));
                        gender = childInfo.getGender();
                        StatsGraph(gender);
                        if(Objects.equals(gender, "Boy")){
                            series2.setColor(Color.GREEN);
                            DataPoint[] dp = new DataPoint[14];
                            dp[0] = new DataPoint(0, 35.81367);
                            dp[1] = new DataPoint(4, 37.19361);
                            dp[2] = new DataPoint(8, 39.20743);
                            dp[3] = new DataPoint(12, 40.65233);
                            dp[4] = new DataPoint(16, 41.76517);
                            dp[5] = new DataPoint(20, 42.66116);
                            dp[6] = new DataPoint(24, 43.40489);
                            dp[7] = new DataPoint(28, 44.0361);
                            dp[8] = new DataPoint(32, 44.58097);
                            dp[9] = new DataPoint(36, 45.05761);
                            dp[10] = new DataPoint(40, 45.47908);
                            dp[11] = new DataPoint(44, 45.85506);
                            dp[12] = new DataPoint(48, 46.19295);
                            dp[13] = new DataPoint(52, 46.49853);
                            headdp=dp;
                            series2.resetData(dp);
                            series4.setColor(Color.RED);
                            Paint dottedLinePaint = new Paint();
                            dottedLinePaint.setColor(Color.RED);
                            dottedLinePaint.setStyle(Paint.Style.STROKE);
                            dottedLinePaint.setStrokeWidth(11);
                            dottedLinePaint.setPathEffect(new DashPathEffect(new float[]{10, 20}, 0));
                            series4.setCustomPaint(dottedLinePaint);
                            DataPoint[] dp4 = new DataPoint[14];
                            dp4[0] = new DataPoint(0, 31.48762);
                            dp4[1] = new DataPoint(4, 33.25006);
                            dp4[2] = new DataPoint(8, 35.78126);
                            dp4[3] = new DataPoint(12, 37.5588	);
                            dp4[4] = new DataPoint(16, 38.89944);
                            dp4[5] = new DataPoint(20, 39.95673);
                            dp4[6] = new DataPoint(24, 40.81642);
                            dp4[7] = new DataPoint(28, 41.53109);
                            dp4[8] = new DataPoint(32, 42.13521);
                            dp4[9] = new DataPoint(36, 42.65253);
                            dp4[10] = new DataPoint(40, 43.10009);
                            dp4[11] = new DataPoint(44, 43.49049);
                            dp4[12] = new DataPoint(48, 43.83332);
                            dp4[13] = new DataPoint(52, 44.136);
                            series4.resetData(dp4);
                            series5.setColor(Color.MAGENTA);
                            Paint dottedLinePaint2 = new Paint();
                            dottedLinePaint2.setColor(Color.MAGENTA);
                            dottedLinePaint2.setStyle(Paint.Style.STROKE);
                            dottedLinePaint2.setStrokeWidth(11);
                            dottedLinePaint2.setPathEffect(new DashPathEffect(new float[]{10, 20}, 0));
                            series5.setCustomPaint(dottedLinePaint2);
                            DataPoint[] dp5 = new DataPoint[14];
                            dp5[0] = new DataPoint(0, 32.14881);
                            dp5[1] = new DataPoint(4, 33.83392);
                            dp5[2] = new DataPoint(8, 36.26428	);
                            dp5[3] = new DataPoint(12, 37.97959);
                            dp5[4] = new DataPoint(16, 39.27893);
                            dp5[5] = new DataPoint(20, 40.30766);
                            dp5[6] = new DataPoint(24, 41.14714);
                            dp5[7] = new DataPoint(28, 41.84742);
                            dp5[8] = new DataPoint(32, 42.44134);
                            dp5[9] = new DataPoint(36, 42.95162);
                            dp5[10] = new DataPoint(40, 43.39458);
                            dp5[11] = new DataPoint(44, 43.7823);
                            dp5[12] = new DataPoint(48, 44.12399);
                            dp5[13] = new DataPoint(52, 44.42679);
                            series5.resetData(dp5);
                            series6.setColor(Color.BLUE);
                            Paint dottedLinePaint3 = new Paint();
                            dottedLinePaint3.setColor(Color.BLUE);
                            dottedLinePaint3.setStyle(Paint.Style.STROKE);
                            dottedLinePaint3.setStrokeWidth(11);
                            dottedLinePaint3.setPathEffect(new DashPathEffect(new float[]{10, 20}, 0));
                            series6.setCustomPaint(dottedLinePaint3);
                            DataPoint[] dp6 = new DataPoint[14];
                            dp6[0] = new DataPoint(0, 33.08389);
                            dp6[1] = new DataPoint(4, 34.67253);
                            dp6[2] = new DataPoint(8, 36.97377);
                            dp6[3] = new DataPoint(12, 38.60724);
                            dp6[4] = new DataPoint(16, 39.85123	);
                            dp6[5] = new DataPoint(20, 40.84114);
                            dp6[6] = new DataPoint(24, 41.65291);
                            dp6[7] = new DataPoint(28, 42.3333);
                            dp6[8] = new DataPoint(32, 42.91311);
                            dp6[9] = new DataPoint(36, 43.41365);
                            dp6[10] = new DataPoint(40, 43.85025);
                            dp6[11] = new DataPoint(44, 44.23432);
                            dp6[12] = new DataPoint(48, 44.57454);
                            dp6[13] = new DataPoint(52, 44.87767);
                            series6.resetData(dp6);
                            series7.setColor(Color.LTGRAY);
                            Paint dottedLinePaint4 = new Paint();
                            dottedLinePaint4.setColor(Color.LTGRAY);
                            dottedLinePaint4.setStyle(Paint.Style.STROKE);
                            dottedLinePaint4.setStrokeWidth(11);
                            dottedLinePaint4.setPathEffect(new DashPathEffect(new float[]{10, 20}, 0));
                            series7.setCustomPaint(dottedLinePaint4);
                            DataPoint[] dp7 = new DataPoint[14];
                            dp7[0] = new DataPoint(0, 34.46952);
                            dp7[1] = new DataPoint(4, 35.93987);
                            dp7[2] = new DataPoint(8, 38.07878);
                            dp7[3] = new DataPoint(12, 39.60637);
                            dp7[4] = new DataPoint(16, 40.77713);
                            dp7[5] = new DataPoint(20, 41.71483);
                            dp7[6] = new DataPoint(24, 42.48889);
                            dp7[7] = new DataPoint(28, 43.14204);
                            dp7[8] = new DataPoint(32, 43.70245);
                            dp7[9] = new DataPoint(36, 44.18964);
                            dp7[10] = new DataPoint(40, 44.61764);
                            dp7[11] = new DataPoint(44, 44.99694);
                            dp7[12] = new DataPoint(48, 45.33549);
                            dp7[13] = new DataPoint(52, 45.63952);
                            series7.resetData(dp7);
                            series8.setColor(Color.LTGRAY);
                            Paint dottedLinePaint5 = new Paint();
                            dottedLinePaint5.setColor(Color.LTGRAY);
                            dottedLinePaint5.setStyle(Paint.Style.STROKE);
                            dottedLinePaint5.setStrokeWidth(11);
                            dottedLinePaint5.setPathEffect(new DashPathEffect(new float[]{10, 20}, 0));
                            series8.setCustomPaint(dottedLinePaint5);
                            DataPoint[] dp8 = new DataPoint[14];
                            dp8[0] = new DataPoint(0, 37.00426);
                            dp8[1] = new DataPoint(4, 38.32125);
                            dp8[2] = new DataPoint(8, 40.24987);
                            dp8[3] = new DataPoint(12, 41.63968);
                            dp8[4] = new DataPoint(16, 42.71455);
                            dp8[5] = new DataPoint(20, 43.58358);
                            dp8[6] = new DataPoint(24, 44.30801);
                            dp8[7] = new DataPoint(28, 44.92555);
                            dp8[8] = new DataPoint(32, 45.46104);
                            dp8[9] = new DataPoint(36, 45.93166);
                            dp8[10] = new DataPoint(40, 46.34979	);
                            dp8[11] = new DataPoint(44, 46.72463);
                            dp8[12] = new DataPoint(48, 47.06318);
                            dp8[13] = new DataPoint(52, 47.37091);
                            series8.resetData(dp8);
                            series9.setColor(Color.BLUE);
                            Paint dottedLinePaint6 = new Paint();
                            dottedLinePaint6.setColor(Color.BLUE);
                            dottedLinePaint6.setStyle(Paint.Style.STROKE);
                            dottedLinePaint6.setStrokeWidth(11);
                            dottedLinePaint6.setPathEffect(new DashPathEffect(new float[]{10, 20}, 0));
                            series9.setCustomPaint(dottedLinePaint6);
                            DataPoint[] dp9 = new DataPoint[14];
                            dp9[0] = new DataPoint(0, 37.97379);
                            dp9[1] = new DataPoint(4, 39.24989);
                            dp9[2] = new DataPoint(8, 41.12605);
                            dp9[3] = new DataPoint(12, 42.48436);
                            dp9[4] = new DataPoint(16, 43.53902);
                            dp9[5] = new DataPoint(20, 44.39472);
                            dp9[6] = new DataPoint(24, 45.11034);
                            dp9[7] = new DataPoint(28, 45.72225);
                            dp9[8] = new DataPoint(32, 46.25443);
                            dp9[9] = new DataPoint(36, 46.72349);
                            dp9[10] = new DataPoint(40, 47.14142);
                            dp9[11] = new DataPoint(44, 47.51714);
                            dp9[12] = new DataPoint(48, 47.85744);
                            dp9[13] = new DataPoint(52, 48.16763);
                            series9.resetData(dp9);
                            series10.setColor(Color.MAGENTA);
                            Paint dottedLinePaint7 = new Paint();
                            dottedLinePaint7.setColor(Color.MAGENTA);
                            dottedLinePaint7.setStyle(Paint.Style.STROKE);
                            dottedLinePaint7.setStrokeWidth(11);
                            dottedLinePaint7.setPathEffect(new DashPathEffect(new float[]{10, 20}, 0));
                            series10.setCustomPaint(dottedLinePaint7);
                            DataPoint[] dp10 = new DataPoint[14];
                            dp10[0] = new DataPoint(0, 38.51574);
                            dp10[1] = new DataPoint(4, 39.77262);
                            dp10[2] = new DataPoint(8, 41.62581	);
                            dp10[3] = new DataPoint(12, 42.97189);
                            dp10[4] = new DataPoint(16, 44.01984);
                            dp10[5] = new DataPoint(20, 44.87197);
                            dp10[6] = new DataPoint(24, 45.58593);
                            dp10[7] = new DataPoint(28, 46.19736);
                            dp10[8] = new DataPoint(32, 46.72983);
                            dp10[9] = new DataPoint(36, 47.1997);
                            dp10[10] = new DataPoint(40, 47.6188);
                            dp10[11] = new DataPoint(44, 47.99592);
                            dp10[12] = new DataPoint(48, 48.33781);
                            dp10[13] = new DataPoint(52, 48.64972);
                            series10.resetData(dp10);
                            series11.setColor(Color.RED);
                            Paint dottedLinePaint8 = new Paint();
                            dottedLinePaint8.setColor(Color.RED);
                            dottedLinePaint8.setStyle(Paint.Style.STROKE);
                            dottedLinePaint8.setStrokeWidth(11);
                            dottedLinePaint8.setPathEffect(new DashPathEffect(new float[]{10, 20}, 0));
                            series11.setCustomPaint(dottedLinePaint8);
                            DataPoint[] dp11 = new DataPoint[14];
                            dp11[0] = new DataPoint(0, 38.85417);
                            dp11[1] = new DataPoint(4, 40.10028);
                            dp11[2] = new DataPoint(8, 41.94138);
                            dp11[3] = new DataPoint(12, 43.28181);
                            dp11[4] = new DataPoint(16, 44.32733);
                            dp11[5] = new DataPoint(20, 45.17877);
                            dp11[6] = new DataPoint(24, 45.893);
                            dp11[7] = new DataPoint(28, 46.50524);
                            dp11[8] = new DataPoint(32, 47.0388);
                            dp11[9] = new DataPoint(36, 47.5099);
                            dp11[10] = new DataPoint(40, 47.93027);
                            dp11[11] = new DataPoint(44, 48.30867);
                            dp11[12] = new DataPoint(48, 48.65181);
                            dp11[13] = new DataPoint(52, 48.96494);
                            series11.resetData(dp11);
                        }else if (Objects.equals(gender, "Girl")){
                            series2.setColor(Color.MAGENTA);
                            DataPoint[] dp = new DataPoint[14];
                            dp[0] = new DataPoint(0, 34.71156);
                            dp[1] = new DataPoint(4, 36.03454);
                            dp[2] = new DataPoint(8, 37.97672);
                            dp[3] = new DataPoint(12, 39.38013);
                            dp[4] = new DataPoint(16, 40.46774);
                            dp[5] = new DataPoint(20, 41.34841);
                            dp[6] = new DataPoint(24, 42.08335);
                            dp[7] = new DataPoint(28, 42.71034);
                            dp[8] = new DataPoint(32, 43.25429);
                            dp[9] = new DataPoint(36, 43.7325);
                            dp[10] = new DataPoint(40, 44.15743);
                            dp[11] = new DataPoint(44, 44.53837);
                            dp[12] = new DataPoint(48, 44.88241);
                            dp[13] = new DataPoint(52, 45.19508);
                            headdp=dp;
                            series2.resetData(dp);
                            series4.setColor(Color.RED);
                            Paint dottedLinePaint = new Paint();
                            dottedLinePaint.setColor(Color.RED);
                            dottedLinePaint.setStyle(Paint.Style.STROKE);
                            dottedLinePaint.setStrokeWidth(11);
                            dottedLinePaint.setPathEffect(new DashPathEffect(new float[]{10, 20}, 0));
                            series4.setCustomPaint(dottedLinePaint);
                            DataPoint[] dp4 = new DataPoint[14];
                            dp4[0] = new DataPoint(0, 31.9302);
                            dp4[1] = new DataPoint(4, 33.38071);
                            dp4[2] = new DataPoint(8, 35.48627);
                            dp4[3] = new DataPoint(12, 36.9855);
                            dp4[4] = new DataPoint(16, 38.13114);
                            dp4[5] = new DataPoint(20, 39.04619);
                            dp4[6] = new DataPoint(24, 39.7996);
                            dp4[7] = new DataPoint(28, 40.43379);
                            dp4[8] = new DataPoint(32, 40.97672);
                            dp4[9] = new DataPoint(36, 41.44768);
                            dp4[10] = new DataPoint(40, 41.86058	);
                            dp4[11] = new DataPoint(44, 42.22575);
                            dp4[12] = new DataPoint(48, 42.55105);
                            dp4[13] = new DataPoint(52, 42.8426);
                            series4.resetData(dp4);
                            series5.setColor(Color.BLUE);
                            Paint dottedLinePaint2 = new Paint();
                            dottedLinePaint2.setColor(Color.BLUE);
                            dottedLinePaint2.setStyle(Paint.Style.STROKE);
                            dottedLinePaint2.setStrokeWidth(11);
                            dottedLinePaint2.setPathEffect(new DashPathEffect(new float[]{10, 20}, 0));
                            series5.setCustomPaint(dottedLinePaint2);
                            DataPoint[] dp5 = new DataPoint[14];
                            dp5[0] = new DataPoint(0, 32.2509);
                            dp5[1] = new DataPoint(4, 33.68744);
                            dp5[2] = new DataPoint(8, 35.7756);
                            dp5[3] = new DataPoint(12, 37.26522);
                            dp5[4] = new DataPoint(16, 38.40561);
                            dp5[5] = new DataPoint(20, 39.31814);
                            dp5[6] = new DataPoint(24, 40.07086);
                            dp5[7] = new DataPoint(28, 40.70567);
                            dp5[8] = new DataPoint(32, 41.25016);
                            dp5[9] = new DataPoint(36, 41.7234);
                            dp5[10] = new DataPoint(40, 42.13913);
                            dp5[11] = new DataPoint(44, 42.50755);
                            dp5[12] = new DataPoint(48, 42.83643);
                            dp5[13] = new DataPoint(52, 43.13182);
                            series5.resetData(dp5);
                            series6.setColor(Color.LTGRAY);
                            Paint dottedLinePaint3 = new Paint();
                            dottedLinePaint3.setColor(Color.LTGRAY);
                            dottedLinePaint3.setStyle(Paint.Style.STROKE);
                            dottedLinePaint3.setStrokeWidth(11);
                            dottedLinePaint3.setPathEffect(new DashPathEffect(new float[]{10, 20}, 0));
                            series6.setCustomPaint(dottedLinePaint3);
                            DataPoint[] dp6 = new DataPoint[14];
                            dp6[0] = new DataPoint(0, 32.75949);
                            dp6[1] = new DataPoint(4, 34.17346);
                            dp6[2] = new DataPoint(8, 36.23326);
                            dp6[3] = new DataPoint(12, 37.70685);
                            dp6[4] = new DataPoint(16, 38.83814);
                            dp6[5] = new DataPoint(20, 39.74588);
                            dp6[6] = new DataPoint(24, 40.49672);
                            dp6[7] = new DataPoint(28, 41.13171);
                            dp6[8] = new DataPoint(32, 41.67787);
                            dp6[9] = new DataPoint(36, 42.15391);
                            dp6[10] = new DataPoint(40, 42.5733);
                            dp6[11] = new DataPoint(44, 42.94604);
                            dp6[12] = new DataPoint(48, 43.27977);
                            dp6[13] = new DataPoint(52, 43.58043);
                            series6.resetData(dp6);
                            series7.setColor(Color.GREEN);
                            Paint dottedLinePaint4 = new Paint();
                            dottedLinePaint4.setColor(Color.GREEN);
                            dottedLinePaint4.setStyle(Paint.Style.STROKE);
                            dottedLinePaint4.setStrokeWidth(11);
                            dottedLinePaint4.setPathEffect(new DashPathEffect(new float[]{10, 20}, 0));
                            series7.setCustomPaint(dottedLinePaint4);
                            DataPoint[] dp7 = new DataPoint[14];
                            dp7[0] = new DataPoint(0, 33.65187);
                            dp7[1] = new DataPoint(4, 35.02508);
                            dp7[2] = new DataPoint(8, 37.03282);
                            dp7[3] = new DataPoint(12, 38.47603);
                            dp7[4] = new DataPoint(16, 39.58905);
                            dp7[5] = new DataPoint(20, 40.48611);
                            dp7[6] = new DataPoint(24, 41.23136);
                            dp7[7] = new DataPoint(28, 41.86435);
                            dp7[8] = new DataPoint(32, 42.41113);
                            dp7[9] = new DataPoint(36, 42.88978);
                            dp7[10] = new DataPoint(40, 43.31329);
                            dp7[11] = new DataPoint(44, 43.69135);
                            dp7[12] = new DataPoint(48, 44.03133);
                            dp7[13] = new DataPoint(52, 44.33899);
                            series7.resetData(dp7);
                            series8.setColor(Color.GREEN);
                            Paint dottedLinePaint5 = new Paint();
                            dottedLinePaint5.setColor(Color.GREEN);
                            dottedLinePaint5.setStyle(Paint.Style.STROKE);
                            dottedLinePaint5.setStrokeWidth(11);
                            dottedLinePaint5.setPathEffect(new DashPathEffect(new float[]{10, 20}, 0));
                            series8.setCustomPaint(dottedLinePaint5);
                            DataPoint[] dp8 = new DataPoint[14];
                            dp8[0] = new DataPoint(0, 35.85124);
                            dp8[1] = new DataPoint(4, 37.11807);
                            dp8[2] = new DataPoint(8, 38.98533);
                            dp8[3] = new DataPoint(12, 40.34145);
                            dp8[4] = new DataPoint(16, 41.39732);
                            dp8[5] = new DataPoint(20, 42.25604);
                            dp8[6] = new DataPoint(24, 42.97566);
                            dp8[7] = new DataPoint(28, 43.59207);
                            dp8[8] = new DataPoint(32, 44.12897);
                            dp8[9] = new DataPoint(36, 44.60282);
                            dp8[10] = new DataPoint(40, 45.0255);
                            dp8[11] = new DataPoint(44, 45.40587);
                            dp8[12] = new DataPoint(48, 45.75072);
                            dp8[13] = new DataPoint(52, 46.06532);
                            series8.resetData(dp8);
                            series9.setColor(Color.LTGRAY);
                            Paint dottedLinePaint6 = new Paint();
                            dottedLinePaint6.setColor(Color.LTGRAY);
                            dottedLinePaint6.setStyle(Paint.Style.STROKE);
                            dottedLinePaint6.setStrokeWidth(11);
                            dottedLinePaint6.setPathEffect(new DashPathEffect(new float[]{10, 20}, 0));
                            series9.setCustomPaint(dottedLinePaint6);
                            DataPoint[] dp9 = new DataPoint[14];
                            dp9[0] = new DataPoint(0, 36.9535);
                            dp9[1] = new DataPoint(4, 38.16405);
                            dp9[2] = new DataPoint(8, 39.95459);
                            dp9[3] = new DataPoint(12, 41.26063);
                            dp9[4] = new DataPoint(16, 42.28153);
                            dp9[5] = new DataPoint(20, 43.11489);
                            dp9[6] = new DataPoint(24, 43.81575);
                            dp9[7] = new DataPoint(28, 44.41815);
                            dp9[8] = new DataPoint(32, 44.94461);
                            dp9[9] = new DataPoint(36, 45.41078);
                            dp9[10] = new DataPoint(40, 45.82799);
                            dp9[11] = new DataPoint(44, 46.20466);
                            dp9[12] = new DataPoint(48, 46.54726);
                            dp9[13] = new DataPoint(52, 46.86084);
                            series9.resetData(dp9);
                            series10.setColor(Color.BLUE);
                            Paint dottedLinePaint7 = new Paint();
                            dottedLinePaint7.setColor(Color.BLUE);
                            dottedLinePaint7.setStyle(Paint.Style.STROKE);
                            dottedLinePaint7.setStrokeWidth(11);
                            dottedLinePaint7.setPathEffect(new DashPathEffect(new float[]{10, 20}, 0));
                            series10.setCustomPaint(dottedLinePaint7);
                            DataPoint[] dp10 = new DataPoint[14];
                            dp10[0] = new DataPoint(0, 37.65138);
                            dp10[1] = new DataPoint(4, 38.82535);
                            dp10[2] = new DataPoint(8, 40.56517);
                            dp10[3] = new DataPoint(12, 41.83732);
                            dp10[4] = new DataPoint(16, 42.83396);
                            dp10[5] = new DataPoint(20, 43.64924);
                            dp10[6] = new DataPoint(24, 44.3363);
                            dp10[7] = new DataPoint(28, 44.92803);
                            dp10[8] = new DataPoint(32, 45.44619);
                            dp10[9] = new DataPoint(36, 45.90591);
                            dp10[10] = new DataPoint(40, 46.31815);
                            dp10[11] = new DataPoint(44, 46.69106);
                            dp10[12] = new DataPoint(48, 47.0309);
                            dp10[13] = new DataPoint(52, 47.34255);
                            series10.resetData(dp10);
                            series11.setColor(Color.RED);
                            Paint dottedLinePaint8 = new Paint();
                            dottedLinePaint8.setColor(Color.RED);
                            dottedLinePaint8.setStyle(Paint.Style.STROKE);
                            dottedLinePaint8.setStrokeWidth(11);
                            dottedLinePaint8.setPathEffect(new DashPathEffect(new float[]{10, 20}, 0));
                            series11.setCustomPaint(dottedLinePaint8);
                            DataPoint[] dp11 = new DataPoint[14];
                            dp11[0] = new DataPoint(0, 38.1211);
                            dp11[1] = new DataPoint(4, 39.27006);
                            dp11[2] = new DataPoint(8, 40.97482);
                            dp11[3] = new DataPoint(12, 42.22321);
                            dp11[4] = new DataPoint(16, 43.2026);
                            dp11[5] = new DataPoint(20, 44.00486);
                            dp11[6] = new DataPoint(24, 44.68183);
                            dp11[7] = new DataPoint(28, 45.26563);
                            dp11[8] = new DataPoint(32, 45.77751);
                            dp11[9] = new DataPoint(36, 46.23224);
                            dp11[10] = new DataPoint(40, 46.64053);
                            dp11[11] = new DataPoint(44, 47.01035);
                            dp11[12] = new DataPoint(48, 47.3478);
                            dp11[13] = new DataPoint(52, 47.65766);
                            series11.resetData(dp11);
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public int WeekCalculation(Date headDate) {
        long i = birthdate.getTime();
        long j = headDate.getTime();
        long daysDiff = TimeUnit.DAYS.convert(j - i, TimeUnit.MILLISECONDS);//604800//1 week
        long k = (long) Math.floor(daysDiff / 7.0);
        return (int) k;
    }
    void showMessage(String title, String message) {
        new AlertDialog.Builder(getActivity()).setTitle(title).setMessage(message).setCancelable(true).show();
    }

}