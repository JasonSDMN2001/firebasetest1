package com.unipi.boidanis.firebasetest1;

import android.app.Activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.os.Bundle;
import android.graphics.Paint;
import android.graphics.DashPathEffect;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.LineGraphSeries;
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
 * Use the {@link HeightFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HeightFragment extends Fragment implements CustomDialog.CustomDialogListener {
    FirebaseAuth mAuth;
    FirebaseUser user;
    FirebaseDatabase database;
    DatabaseReference ref, ref2, reference;
    ActivityResultLauncher<Intent> resultLauncher;
    private Date date, birthdate;
    private int week;
    private float height;
    private String key,gender;
    RecyclerView recyclerView;
    HeightAdapter heightAdapter;
    ArrayList<HeightData> list;
    GraphView graphView;
    LineGraphSeries series, series2,series3,series4,series5,series6,series7,series8,series9,series10,series11;
    Button button;
    String babyname = "";
    DataPoint[] heightdp = new DataPoint[14];
    Date[] temp_date;
    TextView textView9,textView10,textView15,textView16,textView23;
    BottomNavigationView bottomNavigationView;
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    public HeightFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HeightFragment.
     */
    public static HeightFragment newInstance(String param1, String param2) {
        HeightFragment fragment = new HeightFragment();
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
        View view = inflater.inflate(R.layout.fragment_height, container, false);
        database = FirebaseDatabase.getInstance();
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        bottomNavigationView = (BottomNavigationView) getActivity().findViewById(R.id.bottomNavigationView);
        Spinner getbabyname = (Spinner) getActivity().findViewById(R.id.spinner);
        babyname = getbabyname.getSelectedItem().toString();
        whichBaby();



        button = view.findViewById(R.id.button10);
        /*resultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == Activity.RESULT_OK) {
                            Intent intent = result.getData();
                            height = Float.parseFloat(intent.getStringExtra("height"));
                            date = (Date) intent.getExtras().getSerializable("date");
                            week = WeekCalculation(date);
                            key = ref2.push().getKey();
                            HeightData heightData = new HeightData(key,date, week, height,babyname);
                            ref2.child(key).setValue(heightData);
                            StatisticsCalculation(height,week);
                        }
                    }
                });*/
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!NetworkUtils.isNetworkAvailable(getActivity())) {
                    Toast.makeText(getActivity(), "No internet connection", Toast.LENGTH_SHORT).show();
                    onPause();
                }else{
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
                    dialog.setCustomDialogListener(HeightFragment.this);
                    dialog.setHint("Height");
                    dialog.setCondition("^(4[4-9]\\.[0-9]|[5-7][0-9]\\.[0-9]|8[0-1]\\.[0-9]|82\\.0)$");
                    dialog.setError_message("height between 44.9-82.0 cm");
                    dialog.show(getChildFragmentManager(), "custom_dialog");

                }else{
                    Toast.makeText(getContext(), "Please select a child", Toast.LENGTH_SHORT).show();


                }
                /*if(!babyname.matches("Select child")) {
                    Intent intent = new Intent(getActivity(), MainActivity4.class);
                    if (temp_date[temp_date.length - 1] != null) {
                        intent.putExtra("last date", temp_date[temp_date.length - 1]);
                    } else {
                        intent.putExtra("last date", birthdate);
                    }
                    resultLauncher.launch(intent);
                    //resultLauncher.launch(new Intent(getActivity(), MainActivity4.class));
                }else{
                    Toast.makeText(getContext(), "Please select a child", Toast.LENGTH_SHORT).show();


                }*/
            }
            }
        });

        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        list = new ArrayList<>();
        heightAdapter = new HeightAdapter(getContext(), list);
        recyclerView.setAdapter(heightAdapter);


        graphView = (GraphView) view.findViewById(R.id.graphview);
        series = new LineGraphSeries();
        series.setColor(Color.CYAN);
        series.setTitle(babyname+"'s height");
        series.setThickness(14);
        graphView.addSeries(series);
        graphView.setTitle("height History");
        graphView.getGridLabelRenderer().setVerticalAxisTitle("height in cm");
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
            graphView.getLegendRenderer().setAlign(LegendRenderer.LegendAlign.BOTTOM);
        }
        return view;
    }
    @Override
    public void onDialogResult(Date selectedDate,String returned_height) {
        height = Float.parseFloat(returned_height);
        date = selectedDate;
        week = WeekCalculation(date);
        key = ref2.push().getKey();
        HeightData heightData = new HeightData(key,date, week, height,babyname);
        ref2.child(key).setValue(heightData);
        StatisticsCalculation(height,week);
    }
    private void whichBaby() {
        if (!babyname.matches("")) {
            BirthDayFind(babyname);
            RecyclerUpdate(babyname);
            ref2 = database.getReference("Users").child(mAuth.getUid()).child(babyname).child("heightData");

        }
    }

    private void StatisticsCalculation(float weight,int week) {
        try {
            DatabaseReference statreference;
            if (week < 10) {
                statreference = database.getReference("All height data").child(gender).child("week 0" + week).child("height");
            } else {
                statreference = database.getReference("All height data").child(gender).child("week " + week).child("height");
            }
            statreference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    float stat_height = Float.parseFloat(snapshot.getValue().toString());
                    statreference.setValue(stat_height + height);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
            DatabaseReference reference2;
            if (week < 10) {
                reference2 = database.getReference("All height data").child(gender).child("week 0" + week).child("babies");
            } else {
                reference2 = database.getReference("All height data").child(gender).child("week " + week).child("babies");
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
        DatabaseReference graphReference = database.getReference("All height data").child(gender);
        graphReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                DataPoint[] dp =new DataPoint[(int)snapshot.getChildrenCount()];
                int index=0;
                for(DataSnapshot dataSnapshot:snapshot.getChildren()){
                    float found_height =Float.parseFloat(dataSnapshot.child("height").getValue().toString());
                    int baby_population = Integer.parseInt(dataSnapshot.child("babies").getValue().toString());
                    WeightGraphPoints points = new WeightGraphPoints(index,found_height/baby_population);
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
            reference = database.getReference("Users").child(mAuth.getUid()).child(s).child("heightData");
            reference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    list.clear();
                    DataPoint[] dp = new DataPoint[(int) snapshot.getChildrenCount()];
                    int index = 0;
                    temp_date = new Date[(int) snapshot.getChildrenCount()];
                    float[] temp_height = new float[(int) snapshot.getChildrenCount()];
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        HeightData heightData = dataSnapshot.getValue(HeightData.class);
                        list.add(heightData);
                        temp_date[index] = heightData.getDate();
                        temp_height[index] = heightData.getHeight();
                        if (dp.length > 2) {

                            WeightGraphPoints points = new WeightGraphPoints(heightData.getWeek(), heightData.getHeight());
                            dp[index] = new DataPoint(points.x, points.y);
                            index++;
                        }
                    }
                    if (dp.length > 2) {
                        if (heightdp[(int) (Math.floor((int) snapshot.getChildrenCount() / 4.0))] != null) {
                            if (Math.abs(dp[((int) snapshot.getChildrenCount()) - 1].getY()
                                    - heightdp[(int) (Math.floor((int) snapshot.getChildrenCount() / 4.0))].getY()) > 2.5) {
                                HeightNotification();
                            }
                        }
                        if (dp[0] != null && temp_date[(int) snapshot.getChildrenCount() - 1] != null) {
                            try {
                                textView9.setText(String.format("%.2f", dp[0].getY()) + " cm");
                                textView15.setText(temp_height[(int) snapshot.getChildrenCount() - 1] + " cm");
                                java.text.DateFormat dateFormat = android.text.format.DateFormat.getDateFormat(getContext());
                                textView16.setText(dateFormat.format(temp_date[(int) snapshot.getChildrenCount() - 1]));
                                textView23.setText(String.format("%.2f", temp_height[(int) snapshot.getChildrenCount() - 1] - dp[0].getY()) + " cm");
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }

                        series.resetData(dp);
                    }
                    heightAdapter.notifyDataSetChanged();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void HeightNotification() {
        buildAlertMessage("Would you like to learn ways to manage your child's height?");
    }
    private void buildAlertMessage(String s) {
        try {
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
                            showMessage("To access the height guides", "please press the height button");
                        }
                    })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        public void onClick(final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                            dialog.cancel();
                        }
                    });
            final android.app.AlertDialog alert = builder.create();
            alert.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
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
                            dp[0] = new DataPoint(0, 49.98888);
                            dp[1] = new DataPoint(4, 52.69598);
                            dp[2] = new DataPoint(8, 56.62843);
                            dp[3] = new DataPoint(12, 59.60895);
                            dp[4] = new DataPoint(16, 62.077);
                            dp[5] = new DataPoint(20, 64.21686);
                            dp[6] = new DataPoint(24, 66.12531);
                            dp[7] = new DataPoint(28, 67.86018);
                            dp[8] = new DataPoint(32, 69.45908);
                            dp[9] = new DataPoint(36, 70.94804);
                            dp[10] = new DataPoint(40, 72.34586);
                            dp[11] = new DataPoint(44, 73.66665);
                            dp[12] = new DataPoint(48, 74.9213);
                            dp[13] = new DataPoint(52, 76.11838);
                            heightdp=dp;
                            series2.resetData(dp);
                            series4.setColor(Color.RED);
                            Paint dottedLinePaint = new Paint();
                            dottedLinePaint.setColor(Color.RED);
                            dottedLinePaint.setStyle(Paint.Style.STROKE);
                            dottedLinePaint.setStrokeWidth(11);
                            dottedLinePaint.setPathEffect(new DashPathEffect(new float[]{10, 20}, 0));
                            series4.setCustomPaint(dottedLinePaint);
                            DataPoint[] dp4 = new DataPoint[14];
                            dp4[0] = new DataPoint(0, 44.9251);
                            dp4[1] = new DataPoint(4, 47.97812);
                            dp4[2] = new DataPoint(8, 52.19859);
                            dp4[3] = new DataPoint(12, 55.26322);
                            dp4[4] = new DataPoint(16, 57.7304957);
                            dp4[5] = new DataPoint(20, 59.82569);
                            dp4[6] = new DataPoint(24, 61.66384);
                            dp4[7] = new DataPoint(28, 63.31224	);
                            dp4[8] = new DataPoint(32, 64.81395);
                            dp4[9] = new DataPoint(36, 66.19833);
                            dp4[10] = new DataPoint(40, 67.48635);
                            dp4[11] = new DataPoint(44, 68.6936);
                            dp4[12] = new DataPoint(48, 69.832);
                            dp4[13] = new DataPoint(52, 70.91088);
                            series4.resetData(dp4);
                            series5.setColor(Color.MAGENTA);
                            Paint dottedLinePaint2 = new Paint();
                            dottedLinePaint2.setColor(Color.MAGENTA);
                            dottedLinePaint2.setStyle(Paint.Style.STROKE);
                            dottedLinePaint2.setStrokeWidth(11);
                            dottedLinePaint2.setPathEffect(new DashPathEffect(new float[]{10, 20}, 0));
                            series5.setCustomPaint(dottedLinePaint2);
                            DataPoint[] dp5 = new DataPoint[14];
                            dp5[0] = new DataPoint(0, 45.56841);
                            dp5[1] = new DataPoint(4, 48.55809);
                            dp5[2] = new DataPoint(8, 52.72611);
                            dp5[3] = new DataPoint(12, 55.77345);
                            dp5[4] = new DataPoint(16, 58.23744);
                            dp5[5] = new DataPoint(20, 60.33647);
                            dp5[6] = new DataPoint(24, 62.18261);
                            dp5[7] = new DataPoint(28, 63.84166	);
                            dp5[8] = new DataPoint(32, 65.35584);
                            dp5[9] = new DataPoint(36, 66.75398);
                            dp5[10] = new DataPoint(40, 68.05675);
                            dp5[11] = new DataPoint(44, 69.27949);
                            dp5[12] = new DataPoint(48, 70.43397);
                            dp5[13] = new DataPoint(52, 71.52941);
                            series5.resetData(dp5);
                            series6.setColor(Color.BLUE);
                            Paint dottedLinePaint3 = new Paint();
                            dottedLinePaint3.setColor(Color.BLUE);
                            dottedLinePaint3.setStyle(Paint.Style.STROKE);
                            dottedLinePaint3.setStrokeWidth(11);
                            dottedLinePaint3.setPathEffect(new DashPathEffect(new float[]{10, 20}, 0));
                            series6.setCustomPaint(dottedLinePaint3);

                            DataPoint[] dp6 = new DataPoint[14];
                            dp6[0] = new DataPoint(0, 46.55429);
                            dp6[1] = new DataPoint(4, 49.4578);
                            dp6[2] = new DataPoint(8, 53.55365);
                            dp6[3] = new DataPoint(12, 56.57772);
                            dp6[4] = new DataPoint(16, 59.0383);
                            dp6[5] = new DataPoint(20, 61.1441);
                            dp6[6] = new DataPoint(24, 63.00296	);
                            dp6[7] = new DataPoint(28, 64.67854);
                            dp6[8] = new DataPoint(32, 66.21181);
                            dp6[9] = new DataPoint(36, 67.63088);
                            dp6[10] = new DataPoint(40, 68.95591);
                            dp6[11] = new DataPoint(44, 70.20192);
                            dp6[12] = new DataPoint(48, 71.38046);
                            dp6[13] = new DataPoint(52, 72.50055);
                            series6.resetData(dp6);
                            series7.setColor(Color.LTGRAY);
                            Paint dottedLinePaint4 = new Paint();
                            dottedLinePaint4.setColor(Color.LTGRAY);
                            dottedLinePaint4.setStyle(Paint.Style.STROKE);
                            dottedLinePaint4.setStrokeWidth(11);
                            dottedLinePaint4.setPathEffect(new DashPathEffect(new float[]{10, 20}, 0));
                            series7.setCustomPaint(dottedLinePaint4);
                            DataPoint[] dp7 = new DataPoint[14];
                            dp7[0] = new DataPoint(0, 48.18937);
                            dp7[1] = new DataPoint(4, 50.97919);
                            dp7[2] = new DataPoint(8, 54.9791);
                            dp7[3] = new DataPoint(12, 57.9744);
                            dp7[4] = new DataPoint(16, 60.43433);
                            dp7[5] = new DataPoint(20, 62.55409);
                            dp7[6] = new DataPoint(24, 64.43546);
                            dp7[7] = new DataPoint(28, 66.13896);
                            dp7[8] = new DataPoint(32, 67.70375);
                            dp7[9] = new DataPoint(36, 69.15682);
                            dp7[10] = new DataPoint(40, 70.51761);
                            dp7[11] = new DataPoint(44, 71.80065);
                            dp7[12] = new DataPoint(48, 73.01712);
                            dp7[13] = new DataPoint(52, 74.17581);
                            series7.resetData(dp7);
                            series8.setColor(Color.LTGRAY);
                            Paint dottedLinePaint5 = new Paint();
                            dottedLinePaint5.setColor(Color.LTGRAY);
                            dottedLinePaint5.setStyle(Paint.Style.STROKE);
                            dottedLinePaint5.setStrokeWidth(11);
                            dottedLinePaint5.setPathEffect(new DashPathEffect(new float[]{10, 20}, 0));
                            series8.setCustomPaint(dottedLinePaint5);
                            DataPoint[] dp8 = new DataPoint[14];
                            dp8[0] = new DataPoint(0, 51.77126);
                            dp8[1] = new DataPoint(4, 54.44054);
                            dp8[2] = new DataPoint(8, 58.35059);
                            dp8[3] = new DataPoint(12, 61.33788);
                            dp8[4] = new DataPoint(16, 63.82543);
                            dp8[5] = new DataPoint(20, 65.99131);
                            dp8[6] = new DataPoint(24, 67.92935);
                            dp8[7] = new DataPoint(28, 69.69579);
                            dp8[8] = new DataPoint(32, 71.32735);
                            dp8[9] = new DataPoint(36, 72.84947);
                            dp8[10] = new DataPoint(40, 74.2806);
                            dp8[11] = new DataPoint(44, 75.63462);
                            dp8[12] = new DataPoint(48, 76.92224);
                            dp8[13] = new DataPoint(52, 78.15196);
                            series8.resetData(dp8);
                            series9.setColor(Color.BLUE);
                            Paint dottedLinePaint6 = new Paint();
                            dottedLinePaint6.setColor(Color.BLUE);
                            dottedLinePaint6.setStyle(Paint.Style.STROKE);
                            dottedLinePaint6.setStrokeWidth(11);
                            dottedLinePaint6.setPathEffect(new DashPathEffect(new float[]{10, 20}, 0));
                            series9.setCustomPaint(dottedLinePaint6);
                            DataPoint[] dp9 = new DataPoint[14];
                            dp9[0] = new DataPoint(0, 53.36153);
                            dp9[1] = new DataPoint(4, 56.03444);
                            dp9[2] = new DataPoint(8, 59.9664);
                            dp9[3] = new DataPoint(12, 62.98158);
                            dp9[4] = new DataPoint(16, 65.49858);
                            dp9[5] = new DataPoint(20, 67.69405);
                            dp9[6] = new DataPoint(24, 69.66122);
                            dp9[7] = new DataPoint(28, 71.45609);
                            dp9[8] = new DataPoint(32, 73.11525);
                            dp9[9] = new DataPoint(36, 74.6641);
                            dp9[10] = new DataPoint(40, 76.1211);
                            dp9[11] = new DataPoint(44, 77.50016);
                            dp9[12] = new DataPoint(48, 78.81202);
                            dp9[13] = new DataPoint(52, 80.0652);
                            series9.resetData(dp9);
                            series10.setColor(Color.MAGENTA);
                            Paint dottedLinePaint7 = new Paint();
                            dottedLinePaint7.setColor(Color.MAGENTA);
                            dottedLinePaint7.setStyle(Paint.Style.STROKE);
                            dottedLinePaint7.setStrokeWidth(11);
                            dottedLinePaint7.setPathEffect(new DashPathEffect(new float[]{10, 20}, 0));
                            series10.setCustomPaint(dottedLinePaint7);
                            DataPoint[] dp10 = new DataPoint[14];
                            dp10[0] = new DataPoint(0, 54.30721);
                            dp10[1] = new DataPoint(4, 56.99908);
                            dp10[2] = new DataPoint(8, 60.96465);
                            dp10[3] = new DataPoint(12, 64.00789);
                            dp10[4] = new DataPoint(16, 66.54889);
                            dp10[5] = new DataPoint(20, 68.76538);
                            dp10[6] = new DataPoint(24, 70.75128);
                            dp10[7] = new DataPoint(28, 72.56307);
                            dp10[8] = new DataPoint(32, 74.23767);
                            dp10[9] = new DataPoint(36, 75.80074);
                            dp10[10] = new DataPoint(40, 77.27095);
                            dp10[11] = new DataPoint(44, 78.66234);
                            dp10[12] = new DataPoint(48, 79.98578);
                            dp10[13] = new DataPoint(52, 81.2499);
                            series10.resetData(dp10);
                            series11.setColor(Color.RED);
                            Paint dottedLinePaint8 = new Paint();
                            dottedLinePaint8.setColor(Color.RED);
                            dottedLinePaint8.setStyle(Paint.Style.STROKE);
                            dottedLinePaint8.setStrokeWidth(11);
                            dottedLinePaint8.setPathEffect(new DashPathEffect(new float[]{10, 20}, 0));
                            series11.setCustomPaint(dottedLinePaint8);
                            DataPoint[] dp11 = new DataPoint[14];
                            dp11[0] = new DataPoint(0, 54.919);
                            dp11[1] = new DataPoint(4, 57.62984);
                            dp11[2] = new DataPoint(8, 61.62591);
                            dp11[3] = new DataPoint(12, 64.69241);
                            dp11[4] = new DataPoint(16, 67.2519);
                            dp11[5] = new DataPoint(20, 69.48354);
                            dp11[6] = new DataPoint(24, 71.48218);
                            dp11[7] = new DataPoint(28, 73.30488);
                            dp11[8] = new DataPoint(32, 74.98899);
                            dp11[9] = new DataPoint(36, 76.56047);
                            dp11[10] = new DataPoint(40, 78.03819);
                            dp11[11] = new DataPoint(44, 79.43637);
                            dp11[12] = new DataPoint(48, 80.76602);
                            dp11[13] = new DataPoint(52, 82.03585);
                            series11.resetData(dp11);
                        }else if (Objects.equals(gender, "Girl")){
                            series2.setColor(Color.MAGENTA);
                            DataPoint[] dp = new DataPoint[14];
                            dp[0] = new DataPoint(0, 49.2864);
                            dp[1] = new DataPoint(4, 51.68358);
                            dp[2] = new DataPoint(8, 55.28613);
                            dp[3] = new DataPoint(12, 58.09382);
                            dp[4] = new DataPoint(16, 60.45981);
                            dp[5] = new DataPoint(20, 62.5367);
                            dp[6] = new DataPoint(24, 64.40633);
                            dp[7] = new DataPoint(28, 66.11842);
                            dp[8] = new DataPoint(32, 67.70574);
                            dp[9] = new DataPoint(36, 69.19124);
                            dp[10] = new DataPoint(40, 70.59164);
                            dp[11] = new DataPoint(44, 71.91962);
                            dp[12] = new DataPoint(48, 73.18501);
                            dp[13] = new DataPoint(52, 74.39564);
                            heightdp=dp;
                            series2.resetData(dp);
                            series4.setColor(Color.RED);
                            Paint dottedLinePaint = new Paint();
                            dottedLinePaint.setColor(Color.RED);
                            dottedLinePaint.setStyle(Paint.Style.STROKE);
                            dottedLinePaint.setStrokeWidth(11);
                            dottedLinePaint.setPathEffect(new DashPathEffect(new float[]{10, 20}, 0));
                            series4.setCustomPaint(dottedLinePaint);
                            DataPoint[] dp4 = new DataPoint[14];
                            dp4[0] = new DataPoint(0, 45.09488);
                            dp4[1] = new DataPoint(4, 47.46916);
                            dp4[2] = new DataPoint(8, 50.95701);
                            dp4[3] = new DataPoint(12, 53.62925);
                            dp4[4] = new DataPoint(16, 55.8594);
                            dp4[5] = new DataPoint(20, 57.8047);
                            dp4[6] = new DataPoint(24, 59.54799);
                            dp4[7] = new DataPoint(28, 61.13893);
                            dp4[8] = new DataPoint(32, 62.60993);
                            dp4[9] = new DataPoint(36, 63.98348);
                            dp4[10] = new DataPoint(40, 65.2759	);
                            dp4[11] = new DataPoint(44, 66.49948);
                            dp4[12] = new DataPoint(48, 67.66371);
                            dp4[13] = new DataPoint(52, 68.77613);
                            series4.resetData(dp4);
                            series5.setColor(Color.BLUE);
                            Paint dottedLinePaint2 = new Paint();
                            dottedLinePaint2.setColor(Color.BLUE);
                            dottedLinePaint2.setStyle(Paint.Style.STROKE);
                            dottedLinePaint2.setStrokeWidth(11);
                            dottedLinePaint2.setPathEffect(new DashPathEffect(new float[]{10, 20}, 0));
                            series5.setCustomPaint(dottedLinePaint2);
                            DataPoint[] dp5 = new DataPoint[14];
                            dp5[0] = new DataPoint(0, 45.57561);
                            dp5[1] = new DataPoint(4, 47.96324);
                            dp5[2] = new DataPoint(8, 51.47996);
                            dp5[3] = new DataPoint(12, 54.17907);
                            dp5[4] = new DataPoint(16, 56.43335);
                            dp5[5] = new DataPoint(20, 58.40032);
                            dp5[6] = new DataPoint(24, 60.16323);
                            dp5[7] = new DataPoint(28, 61.77208);
                            dp5[8] = new DataPoint(32, 63.25958);
                            dp5[9] = new DataPoint(36, 64.64845);
                            dp5[10] = new DataPoint(40, 65.9552);
                            dp5[11] = new DataPoint(44, 67.19226);
                            dp5[12] = new DataPoint(48, 68.36925);
                            dp5[13] = new DataPoint(52, 69.4938);
                            series5.resetData(dp5);
                            series6.setColor(Color.LTGRAY);
                            Paint dottedLinePaint3 = new Paint();
                            dottedLinePaint3.setColor(Color.LTGRAY);
                            dottedLinePaint3.setStyle(Paint.Style.STROKE);
                            dottedLinePaint3.setStrokeWidth(11);
                            dottedLinePaint3.setPathEffect(new DashPathEffect(new float[]{10, 20}, 0));
                            series6.setCustomPaint(dottedLinePaint3);
                            DataPoint[] dp6 = new DataPoint[14];
                            dp6[0] = new DataPoint(0, 46.33934);
                            dp6[1] = new DataPoint(4, 48.74248);
                            dp6[2] = new DataPoint(8, 52.29627);
                            dp6[3] = new DataPoint(12, 55.03144);
                            dp6[4] = new DataPoint(16, 57.31892);
                            dp6[5] = new DataPoint(20, 59.31633);
                            dp6[6] = new DataPoint(24, 61.10726);
                            dp6[7] = new DataPoint(28, 62.7421);
                            dp6[8] = new DataPoint(32, 64.25389);
                            dp6[9] = new DataPoint(36, 65.66559);
                            dp6[10] = new DataPoint(40, 66.99394);
                            dp6[11] = new DataPoint(44, 68.25154);
                            dp6[12] = new DataPoint(48, 69.44814);
                            dp6[13] = new DataPoint(52, 70.59149);
                            series6.resetData(dp6);
                            series7.setColor(Color.GREEN);
                            Paint dottedLinePaint4 = new Paint();
                            dottedLinePaint4.setColor(Color.GREEN);
                            dottedLinePaint4.setStyle(Paint.Style.STROKE);
                            dottedLinePaint4.setStrokeWidth(11);
                            dottedLinePaint4.setPathEffect(new DashPathEffect(new float[]{10, 20}, 0));
                            series7.setCustomPaint(dottedLinePaint4);
                            DataPoint[] dp7 = new DataPoint[14];
                            dp7[0] = new DataPoint(0, 47.68345);
                            dp7[1] = new DataPoint(4, 50.09686);
                            dp7[2] = new DataPoint(8, 53.69078);
                            dp7[3] = new DataPoint(12, 56.47125);
                            dp7[4] = new DataPoint(16, 58.80346);
                            dp7[5] = new DataPoint(20, 60.84386);
                            dp7[6] = new DataPoint(24, 62.6759);
                            dp7[7] = new DataPoint(28, 64.35005);
                            dp7[8] = new DataPoint(32, 65.89952);
                            dp7[9] = new DataPoint(36, 67.34745);
                            dp7[10] = new DataPoint(40, 68.7107);
                            dp7[11] = new DataPoint(44, 70.00202);
                            dp7[12] = new DataPoint(48, 71.23128);
                            dp7[13] = new DataPoint(52, 72.40633);
                            series7.resetData(dp7);
                            series8.setColor(Color.GREEN);
                            Paint dottedLinePaint5 = new Paint();
                            dottedLinePaint5.setColor(Color.GREEN);
                            dottedLinePaint5.setStyle(Paint.Style.STROKE);
                            dottedLinePaint5.setStrokeWidth(11);
                            dottedLinePaint5.setPathEffect(new DashPathEffect(new float[]{10, 20}, 0));
                            series8.setCustomPaint(dottedLinePaint5);
                            DataPoint[] dp8 = new DataPoint[14];
                            dp8[0] = new DataPoint(0, 51.0187);
                            dp8[1] = new DataPoint(4, 53.36362);
                            dp8[2] = new DataPoint(8, 56.93136);
                            dp8[3] = new DataPoint(12, 59.74045);
                            dp8[4] = new DataPoint(16, 62.1233);
                            dp8[5] = new DataPoint(20, 64.22507);
                            dp8[6] = new DataPoint(24, 66.12418);
                            dp8[7] = new DataPoint(28, 67.8685);
                            dp8[8] = new DataPoint(32, 69.48975);
                            dp8[9] = new DataPoint(36, 71.01019);
                            dp8[10] = new DataPoint(40, 72.44614);
                            dp8[11] = new DataPoint(44, 73.80997);
                            dp8[12] = new DataPoint(48, 75.11133);
                            dp8[13] = new DataPoint(52, 76.35791);
                            series8.resetData(dp8);
                            series9.setColor(Color.LTGRAY);
                            Paint dottedLinePaint6 = new Paint();
                            dottedLinePaint6.setColor(Color.LTGRAY);
                            dottedLinePaint6.setStyle(Paint.Style.STROKE);
                            dottedLinePaint6.setStrokeWidth(11);
                            dottedLinePaint6.setPathEffect(new DashPathEffect(new float[]{10, 20}, 0));
                            series9.setCustomPaint(dottedLinePaint6);
                            DataPoint[] dp9 = new DataPoint[14];
                            dp9[0] = new DataPoint(0, 52.7025);
                            dp9[1] = new DataPoint(4, 54.96222);
                            dp9[2] = new DataPoint(8, 58.45612);
                            dp9[3] = new DataPoint(12, 61.24306);
                            dp9[4] = new DataPoint(16, 63.62648);
                            dp9[5] = new DataPoint(20, 65.74096);
                            dp9[6] = new DataPoint(24, 67.65995);
                            dp9[7] = new DataPoint(28, 69.42868	);
                            dp9[8] = new DataPoint(32, 71.07731);
                            dp9[9] = new DataPoint(36, 72.62711);
                            dp9[10] = new DataPoint(40, 74.09378);
                            dp9[11] = new DataPoint(44, 75.48923);
                            dp9[12] = new DataPoint(48, 76.82282);
                            dp9[13] = new DataPoint(52, 78.10202);
                            series9.resetData(dp9);
                            series10.setColor(Color.BLUE);
                            Paint dottedLinePaint7 = new Paint();
                            dottedLinePaint7.setColor(Color.BLUE);
                            dottedLinePaint7.setStyle(Paint.Style.STROKE);
                            dottedLinePaint7.setStrokeWidth(11);
                            dottedLinePaint7.setPathEffect(new DashPathEffect(new float[]{10, 20}, 0));
                            series10.setCustomPaint(dottedLinePaint7);
                            DataPoint[] dp10 = new DataPoint[14];
                            dp10[0] = new DataPoint(0, 53.77291);
                            dp10[1] = new DataPoint(4, 55.96094);
                            dp10[2] = new DataPoint(8, 59.38911);
                            dp10[3] = new DataPoint(12, 62.15166);
                            dp10[4] = new DataPoint(16, 64.52875);
                            dp10[5] = new DataPoint(20, 66.64653);
                            dp10[6] = new DataPoint(24, 68.57452);
                            dp10[7] = new DataPoint(28, 70.35587);
                            dp10[8] = new DataPoint(32, 72.01952);
                            dp10[9] = new DataPoint(36, 73.58601);
                            dp10[10] = new DataPoint(40, 75.0705);
                            dp10[11] = new DataPoint(44, 76.4846);
                            dp10[12] = new DataPoint(48, 77.83742);
                            dp10[13] = new DataPoint(52, 79.13625);
                            series10.resetData(dp10);
                            series11.setColor(Color.RED);
                            Paint dottedLinePaint8 = new Paint();
                            dottedLinePaint8.setColor(Color.RED);
                            dottedLinePaint8.setStyle(Paint.Style.STROKE);
                            dottedLinePaint8.setStrokeWidth(11);
                            dottedLinePaint8.setPathEffect(new DashPathEffect(new float[]{10, 20}, 0));
                            series11.setCustomPaint(dottedLinePaint8);
                            DataPoint[] dp11 = new DataPoint[14];
                            dp11[0] = new DataPoint(0, 54.49527);
                            dp11[1] = new DataPoint(4, 56.62728);
                            dp11[2] = new DataPoint(8, 60.00338);
                            dp11[3] = new DataPoint(12, 62.74547);
                            dp11[4] = new DataPoint(16, 65.11577);
                            dp11[5] = new DataPoint(20, 67.23398);
                            dp11[6] = new DataPoint(24, 69.16668);
                            dp11[7] = new DataPoint(28, 70.95545);
                            dp11[8] = new DataPoint(32, 72.62835);
                            dp11[9] = new DataPoint(36, 74.20532);
                            dp11[10] = new DataPoint(40, 75.70118);
                            dp11[11] = new DataPoint(44, 77.12729);
                            dp11[12] = new DataPoint(48, 78.49257);
                            dp11[13] = new DataPoint(52, 79.80419);
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

    public int WeekCalculation(Date heightDate) {
        long i = birthdate.getTime();
        long j = heightDate.getTime();
        long daysDiff = TimeUnit.DAYS.convert(j - i, TimeUnit.MILLISECONDS);//604800//1 week
        long k = (long) Math.floor(daysDiff / 7.0);
        return (int) k;
    }
    void showMessage(String title, String message) {
        new AlertDialog.Builder(getActivity()).setTitle(title).setMessage(message).setCancelable(true).show();
    }




}