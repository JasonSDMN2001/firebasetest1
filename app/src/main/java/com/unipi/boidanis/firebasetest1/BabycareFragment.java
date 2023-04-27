package com.unipi.boidanis.firebasetest1;

import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.ScrollView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link BabycareFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BabycareFragment extends Fragment {
    View[] views = new View[5];
    int res[] = {R.id.button_1, R.id.button_2, R.id.button_3, R.id.button_4, R.id.button_5};
    ScrollView scrollView;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public BabycareFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment BabycareFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static BabycareFragment newInstance(String param1, String param2) {
        BabycareFragment fragment = new BabycareFragment();
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
        View view = inflater.inflate(R.layout.fragment_babycare, container, false);
        scrollView = (ScrollView) view.findViewById(R.id.scrollview4);
        for (int i = 0; i < res.length; i++) {
            views[i] = view.findViewById(res[i]);
        }
        views[0].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showMessage("This is how you'll learn the burping basics","Hold your baby on you with its face facing you and gentle " +
                        "tap on its back");
            }
        });
        views[1].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showMessage("How to change a baby's nappy","" +
                        "Wash your hands before removing the nappy.\n" +
                        "Clean your baby's genitals and bottom with cotton wool and water, or with an unscented wipe.\n" +
                        "Gently lift your baby’s legs by holding their ankles. This lets you clean underneath.\n" +
                        "Always wipe from front to back.\n" +
                        "For a baby boy, there is no need to retract the foreskin. Point his penis downwards before replacing the nappy.\n" +
                        "Let the area dry. There is no need to use powders.\n" +
                        "Slide a new, open nappy under your baby by gently lifting their legs at the ankles. The new nappy should be snug but not tight.\n" +
                        "Wash your hands after changing the nappy.");
            }
        });
        views[2].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        views[3].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        views[4].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        return view;
    }
    void showMessage(String title, String message) {
        new AlertDialog.Builder(getContext()).setTitle(title).setMessage(message).setCancelable(true).show();
    }
}