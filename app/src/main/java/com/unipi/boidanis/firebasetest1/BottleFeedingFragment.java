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
 * Use the {@link BottleFeedingFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BottleFeedingFragment extends Fragment {
    View[] views = new View[2];
    int res[] = {R.id.button_1, R.id.button_2};
    ScrollView scrollView;
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public BottleFeedingFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment BottleFeedingFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static BottleFeedingFragment newInstance(String param1, String param2) {
        BottleFeedingFragment fragment = new BottleFeedingFragment();
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
        View view=  inflater.inflate(R.layout.fragment_bottle_feeding, container, false);
        scrollView = (ScrollView) view.findViewById(R.id.scrollview4);
        for (int i = 0; i < res.length; i++) {
            views[i] = view.findViewById(res[i]);
        }
        views[0].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showMessage("3 positions for easy feeding","Over the shoulder: Drape your baby over your shoulder and firmly pat or rub their back.\n" +
                        "On the lap: Sit your baby upright, lean them forward against the heel of your hand, and firmly pat or rub their back.\n" +
                        "Lying down: Place baby stomach-down on your lap and firmly rub or pat their back.");
            }
        });
        views[1].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showMessage("how much milk does my baby need","By the end of their first week, most will need around 150 to 200ml per kilo of their weight a day until they're 6 months old. This amount will vary from baby to baby.");
            }
        });
        return view;
    }
    void showMessage(String title, String message) {
        new AlertDialog.Builder(getContext()).setTitle(title).setMessage(message).setCancelable(true).show();
    }
}