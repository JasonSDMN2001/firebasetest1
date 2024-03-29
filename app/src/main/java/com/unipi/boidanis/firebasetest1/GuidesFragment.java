package com.unipi.boidanis.firebasetest1;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link GuidesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class GuidesFragment extends Fragment {
    View[] views = new View[7];
    int res[] = {R.id.button_1, R.id.button_2, R.id.button_3, R.id.button_4, R.id.button_5, R.id.button_6, R.id.button_7};
    HorizontalScrollView horizontalScrollView;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public GuidesFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment GuidesFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static GuidesFragment newInstance(String param1, String param2) {
        GuidesFragment fragment = new GuidesFragment();
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
        View view = inflater.inflate(R.layout.fragment_guides, container, false);
        horizontalScrollView = (HorizontalScrollView) view.findViewById(R.id.horizon);
        for (int i = 0; i < res.length; i++) {
            views[i] = view.findViewById(res[i]);
            /*views[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int[] loc = new int[2];
                    views[3].getLocationInWindow(loc);
                    horizontalScrollView.scrollTo(loc[0], 0);
                    ReplaceFragment(new MilestoneFragment());
                }
            });*/
        }
        views[0].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ReplaceFragment(new BabycareFragment());
            }
        });
        views[1].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ReplaceFragment(new BottleFeedingFragment());
            }
        });
        views[2].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ReplaceFragment(new cryingFragment());
            }
        });
        views[3].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ReplaceFragment(new weight_guideFragment());
            }
        });
        views[4].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ReplaceFragment(new IllnessFragment());
            }
        });
        views[5].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ReplaceFragment(new sleepingFragment());
            }
        });
        views[6].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ReplaceFragment(new safetyFragment());
            }
        });
        ReplaceFragment(new BabycareFragment());
        return view;
    }
    private void ReplaceFragment(Fragment fragment){
        FragmentTransaction fragmentTransaction = getActivity()
                .getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout_guides, fragment);
        fragmentTransaction.commit();
    }
}