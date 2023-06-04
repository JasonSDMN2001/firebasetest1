package com.unipi.boidanis.firebasetest1;

import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link weight_guideFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class weight_guideFragment extends Fragment {
    View[] views = new View[3];
    int res[] = {R.id.button_1, R.id.button_2, R.id.button_3};
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public weight_guideFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment feedingFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static weight_guideFragment newInstance(String param1, String param2) {
        weight_guideFragment fragment = new weight_guideFragment();
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
        View view=inflater.inflate(R.layout.fragment_weight_guide, container, false);
        for (int i = 0; i < res.length; i++) {
            views[i] = view.findViewById(res[i]);
        }
        views[0].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showMessage("Baby weight","Regular monitoring of your baby’s physical" +
                        " growth will provide a record of the amazing level of physical growth " +
                        "that occurs during the first year of life. Weight is the most commonly" +
                        " used measure of growth and health. However, avoid being too focused on" +
                        " the weight of your baby. Weight is only one measure of health.\n" +
                        "\n" +
                        "Weight, length and head circumference are three important areas of " +
                        "physical growth. Growth refers to an increase in size. At birth, the" +
                        " head is approximately one-quarter of the baby’s total body length, " +
                        "while the legs only take-up one-third of the total body length. " +
                        "By approximately five months most babies have doubled their birth weight." +
                        " By 12 months their length is more than 50% greater than at birth." +
                        " By two years, a baby’s head accounts for one-fifth and their legs " +
                        "for nearly half of the total body length.");
            }
        });
        views[1].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showMessage("Average Baby Length (Height)","In general, during the first six months, " +
                        "a baby grows about one inch per month.1 Between six months and one year, that growth" +
                        " slows down a bit to about a 1/2 inch per month.\n" +
                        "\n" +
                        "The average length of a baby boy at six months is approximately 26 1/2 inches (67.6 cm) " +
                        "and a baby girl is about 25 3/4 inches (65.7 cm). At one year, boys are around 29 3/4 " +
                        "inches (75.7 cm) and girls average 29 inches (74 cm).\n" +
                        "\n" +
                        "The factors that determine height are:\n" +
                        "\n" +
                        "Genetics: The height of a child's mother, father, and other family members have the " +
                        "most significant impact on how tall the child will be.\n" +
                        "Gender: Boys tend to be taller than girls.\n" +
                        "Nutrition: Good nutrition for both the parent during pregnancy and the baby after birth " +
                        "can ensure that the baby's body gets the proper vitamins, minerals, and protein for " +
                        "healthy bones and optimal growth.\n" +
                        "Sleep pattern: Studies show that infants grow in length after naps and long periods " +
                        "of sleep.6\n" +
                        "Physical activity: Body movement and physical activity help build strong muscles and" +
                        " bones.\n" +
                        "Overall health: Chronic illness and disease during childhood can affect growth and" +
                        " development.");
            }
        });
        views[2].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showMessage("Baby head circumference","During the first" +
                        " two years a baby’s head circumference will increase rapidly." +
                        " This is due to the rapid growth in brain size during this period." +
                        " A baby’s skull is structured to allow this growth. At birth, the" +
                        " bones of the skull are separated by six sutures or gaps, and form " +
                        "two fontanelles or ‘soft spots’. The anterior fontanelle is on the " +
                        "top of the baby’s head and easily felt. It closes over by a baby’s " +
                        "second year of life. The second smaller posterior fontanelle is at " +
                        "the back of the head and it closes more quickly. The sutures and " +
                        "fontanelles enable the bones of the skull to overlap during the " +
                        "birth process. At first your baby’s head might appear misshapen;" +
                        " this usually improves within a few weeks.");
            }
        });
        return view;
    }
    void showMessage(String title, String message) {
        new AlertDialog.Builder(getContext()).setTitle(title).setMessage(message).setCancelable(true).show();
    }
}