package com.unipi.boidanis.firebasetest1;

import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link cryingFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class cryingFragment extends Fragment {
    View[] views = new View[2];
    int res[] = {R.id.button_1, R.id.button_2};
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public cryingFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment cryingFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static cryingFragment newInstance(String param1, String param2) {
        cryingFragment fragment = new cryingFragment();
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
        View view=inflater.inflate(R.layout.fragment_crying, container, false);
        for (int i = 0; i < res.length; i++) {
            views[i] = view.findViewById(res[i]);
        }
        views[0].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showMessage("how much crying is normal","All newborns cry and get fussy " +
                        "sometimes. It's normal for a baby to cry for 2–3 hours a day for the first" +
                        " 6 weeks. During the first 3 months of life, they cry more than at any " +
                        "other time. New parents often are low on sleep and getting used to life" +
                        " with their little one.");
            }
        });
        views[1].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showMessage("How to soothe a crying baby","Try stroking your " +
                        "baby's back firmly and rhythmically, holding them against you or lying" +
                        " face downwards on your lap. Undress your baby and massage them gently " +
                        "and firmly. Avoid using any oils or lotions until your baby's at least a " +
                        "month old. Talk soothingly as you do it and keep the room warm enough.");
            }
        });
        return view;
    }
    void showMessage(String title, String message) {
        new AlertDialog.Builder(getContext()).setTitle(title).setMessage(message).setCancelable(true).show();
    }
}