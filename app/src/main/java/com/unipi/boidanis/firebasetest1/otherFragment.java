package com.unipi.boidanis.firebasetest1;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link otherFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class otherFragment extends Fragment implements View.OnClickListener {
    FirebaseAuth mAuth;
    FirebaseUser user;
    ImageButton imageButton;
    FirebaseDatabase database;
    SharedPreferences sharedPreferences;


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public otherFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment otherFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static otherFragment newInstance(String param1, String param2) {
        otherFragment fragment = new otherFragment();
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
        View view=inflater.inflate(R.layout.fragment_other, container, false);
        database = FirebaseDatabase.getInstance();
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        imageButton = (ImageButton) view.findViewById(R.id.imageButton2);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        imageButton.setOnClickListener(this);
        imageButton.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Toast.makeText(getContext(), "Sign-Out", Toast.LENGTH_SHORT).show();

                return false;
            }
        });
        ImageButton imageButton2 = (ImageButton) view.findViewById(R.id.imageButton4);
        imageButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction fragmentTransaction = getActivity()
                        .getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.frame_layout,new GuidesFragment());
                fragmentTransaction.commit();
            }
        });
        imageButton2.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Toast.makeText(getContext(), "Guides", Toast.LENGTH_SHORT).show();

                return false;
            }
        });
        ImageButton imageButton3 = (ImageButton) view.findViewById(R.id.imageButton5);
        imageButton3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction fragmentTransaction = getActivity()
                        .getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.frame_layout,new SettingsFragment());
                fragmentTransaction.commit();
            }
        });
        imageButton3.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Toast.makeText(getContext(), "Settings", Toast.LENGTH_SHORT).show();

                return false;
            }
        });
        ImageButton imageButton4 = (ImageButton) view.findViewById(R.id.imageButton6);
        imageButton4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction fragmentTransaction = getActivity()
                        .getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.frame_layout,new QyestionsFragment());
                fragmentTransaction.commit();
            }
        });
        imageButton4.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Toast.makeText(getContext(), "Questions", Toast.LENGTH_SHORT).show();

                return false;
            }
        });
        ImageButton imageButton5 = (ImageButton) view.findViewById(R.id.imageButton7);
        imageButton5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction fragmentTransaction = getActivity()
                        .getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.frame_layout,new RateFragment());
                fragmentTransaction.commit();
            }
        });
        imageButton5.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Toast.makeText(getContext(), "Rate us", Toast.LENGTH_SHORT).show();

                return false;
            }
        });
        return view;
    }
    void showMessage(String title, String message){
        new AlertDialog.Builder(getActivity()).setTitle(title).setMessage(message).setCancelable(true).show();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.imageButton2:
                if (mAuth.getCurrentUser() != null) {
                    FirebaseAuth.getInstance().signOut();
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("current user","");
                    editor.putString("password","");
                    editor.apply();
                    Toast.makeText(getContext(), "Signed out successfully", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getActivity(), MainActivity2.class);
                    startActivity(intent);
                    getActivity().finish();
                }
                break;

        }
    }
}