package com.unipi.boidanis.firebasetest1;

import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link IllnessFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class IllnessFragment extends Fragment {

    View[] views = new View[2];
    int res[] = {R.id.button_1, R.id.button_2};
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    public IllnessFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment IllnessFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static IllnessFragment newInstance(String param1, String param2) {
        IllnessFragment fragment = new IllnessFragment();
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
        View view = inflater.inflate(R.layout.fragment_illness, container, false);
        for (int i = 0; i < res.length; i++) {
            views[i] = view.findViewById(res[i]);
        }
        views[0].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showMessage("The immune system in babies","A baby’s immune system is" +
                        " not fully " +
                        "developed when they are born. It gets stronger as the baby gets older." +
                        " The " +
                        "immune system works throughout our lives fighting germs that can cause " +
                        "disease.\n" +
                        "\n" +
                        "A mother’s antibodies are shared with their baby through the placenta " +
                        "during the third trimester (last 3 months) of pregnancy. The mother’s " +
                        "antibodies help protect the baby from illnesses when the baby is born." +
                        " The type of antibodies passed from mother to baby depends on the mother’s" +
                        " own level of immunity.\n" +
                        "\n" +
                        "Good bacteria in our gut help our immune system to work well. During birth," +
                        " these good bacteria are in the vagina and are passed on to the baby. " +
                        "This helps good bacteria to start living in the baby’s gut.\n" +
                        "\n" +
                        "After birth, more antibodies are passed to your baby from the colostrum" +
                        " and in breast milk.");
            }
        });
        views[1].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showMessage("Fever","If your baby is younger than " +
                        "3 months old, contact your health care provider for any fever.\n" +
                        "\n" +
                        "If your baby is 3 to 6 months old and has a temperature up to " +
                        "102 F (38.9 C) and seems sick or has a temperature higher than 102 F" +
                        " (38.9 C), contact your health care provider.");
            }
        });
        return view;
    }
    void showMessage(String title, String message) {
        new AlertDialog.Builder(getContext()).setTitle(title).setMessage(message).setCancelable(true).show();
    }
}