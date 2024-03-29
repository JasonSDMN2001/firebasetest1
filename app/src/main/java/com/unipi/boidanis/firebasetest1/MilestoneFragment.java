package com.unipi.boidanis.firebasetest1;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MilestoneFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MilestoneFragment extends Fragment {
    String babyname;
    FirebaseDatabase database;
    DatabaseReference ref,ref2,ref3;
    FirebaseUser user;
    RecyclerView recyclerView2,recyclerView3,recyclerView4;
    FirebaseAuth mAuth;
    MilestoneAdapter milestoneAdapter,milestoneAdapter2,milestoneAdapter3;
    Milestones milestones;
    ArrayList<Milestones>list,list2,list3;
    private Date currentDate,birthdate;
    TextView textView24;
    int weekDifference;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public MilestoneFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MilestoneFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MilestoneFragment newInstance(String param1, String param2) {
        MilestoneFragment fragment = new MilestoneFragment();
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
        View view = inflater.inflate(R.layout.fragment_milestone, container, false);
         textView24 = (TextView) view.findViewById(R.id.textView24);
        if (!NetworkUtils.isNetworkAvailable(getActivity())) {
            Toast.makeText(getActivity(), "No internet connection", Toast.LENGTH_SHORT).show();
            onPause();
        }
        Spinner getbabyname = (Spinner) getActivity().findViewById(R.id.spinner);
        babyname = getbabyname.getSelectedItem().toString();
        database = FirebaseDatabase.getInstance();
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        BirthDayFind(babyname);
        currentDate = Calendar.getInstance().getTime();
        recyclerView2 = (RecyclerView)view.findViewById(R.id.recyclerView2);
        recyclerView3 = (RecyclerView)view.findViewById(R.id.recyclerView3);
        recyclerView4 = (RecyclerView)view.findViewById(R.id.recyclerView4);

        ImageButton imageButton = (ImageButton) view.findViewById(R.id.imageButton4);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showMessage("How to use milestones:","You'll probably be wondering how" +
                        "your little one is developing based on our milestone schedule" +
                        "Keep in mind, there are several reasons why your little one may not be " +
                        "developing as quickly as some babies. Babies who are premature need a little" +
                        " time to catch up. If your baby had a medical problem or illness, some of the" +
                        " development milestones might be delayed. A baby’s temperament may also play " +
                        "a role in how fast he or she develops.\n" +
                        "The milestone schedule is provided to you on a general information basis " +
                        "only and is not a substitute for personalized medical advice. Remember," +
                        " every baby is different, and if you have any concerns about your baby's " +
                        "development always contact your healthcare provider.\n" +
                        "\n" +
                        "To encourage your baby’s development, provide her with plenty of opportunities " +
                        "to play and learn. Get down on the floor and play simple games, read books " +
                        "and talk with your little one. Be careful, not to go overboard and over" +
                        " stimulate your baby. Lastly, encourage your baby to do some things for" +
                        " himself when possible.\n" +
                        "You can view your child's already completed milestones, edit them,and delete them.\n" +
                        "Based on the current week there are some on going milestones that your child can complete." +
                        "there are also the next near milestones that you can prepare for ");
            }
        });
        return view;
    }
    public void RecyclerUpdate(){
        ref = database.getReference().child("Users").child(user.getUid()).child(babyname).child("milestones");
        recyclerView2.setHasFixedSize(true);
        recyclerView2.setLayoutManager(new LinearLayoutManager(getContext()));
        list =new ArrayList<>();
        milestoneAdapter =new MilestoneAdapter(getContext(),list);
        recyclerView2.setAdapter(milestoneAdapter);
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list.clear();
                for(DataSnapshot dataSnapshot: snapshot.getChildren()) {
                    milestones = dataSnapshot.getValue(Milestones.class);
                    if(milestones.getCompleted().toString().equals("false")) {
                        list.add(milestones);
                    }
                }
                milestoneAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        recyclerView3.setHasFixedSize(true);
        recyclerView3.setLayoutManager(new LinearLayoutManager(getContext()));
        list2 =new ArrayList<>();
        milestoneAdapter2 =new MilestoneAdapter(getContext(),list2);
        recyclerView3.setAdapter(milestoneAdapter2);
        ref2 = database.getReference().child("Users").child(user.getUid()).child(babyname).child("milestones");
        ref2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list2.clear();
                for(DataSnapshot dataSnapshot: snapshot.getChildren()) {
                    milestones = dataSnapshot.getValue(Milestones.class);
                    if(milestones.getCompleted().toString().equals("true")){
                        list2.add(milestones);
                    }
                }
                milestoneAdapter2.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        recyclerView4.setHasFixedSize(true);
        recyclerView4.setLayoutManager(new LinearLayoutManager(getContext()));
        list3 =new ArrayList<>();
        milestoneAdapter3 =new MilestoneAdapter(getContext(),list3);
        recyclerView4.setAdapter(milestoneAdapter3);
        ref3 = database.getReference().child("Users").child(user.getUid()).child(babyname).child("milestones");
        ref3.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list3.clear();
                for(DataSnapshot dataSnapshot: snapshot.getChildren()) {
                    milestones = dataSnapshot.getValue(Milestones.class);
                    if(milestones.getCompleted().toString().equals("false") && milestones.getLowerbound()<=weekDifference && milestones.getUpperbound()>=weekDifference) {
                        list3.add(milestones);
                    }
                }
                milestoneAdapter3.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    public int WeekCalculation(Date date1, Date date) {
        long i = date1.getTime();
        long j = date.getTime();
        long daysDiff = TimeUnit.DAYS.convert(j - i, TimeUnit.MILLISECONDS);//604800//1 week
        long k = (long) Math.floor(daysDiff / 7.0);
        return (int) k;
    }
    public void BirthDayFind(String s) {
        DatabaseReference reference2 = database.getReference("Users").child(mAuth.getUid()).child(s);
        reference2.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot:snapshot.getChildren()) {
                    if (!dataSnapshot.getKey().matches("weightData") &&
                            !dataSnapshot.getKey().matches("moments") &&
                            !dataSnapshot.getKey().matches("milestones")&&
                            !dataSnapshot.getKey().matches("Face A Day")&&
                            !dataSnapshot.getKey().matches("heightData")&&
                            !dataSnapshot.getKey().matches("headData")) {
                        ChildInfo childInfo = dataSnapshot.getValue(ChildInfo.class);
                        birthdate = childInfo.getbirthDate();
                        weekDifference = WeekCalculation(birthdate, currentDate);
                        textView24.setText(String.valueOf(weekDifference));
                        RecyclerUpdate();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    void showMessage(String title, String message) {
        new AlertDialog.Builder(getActivity()).setTitle(title).setMessage(message).setCancelable(true).show();
    }
}