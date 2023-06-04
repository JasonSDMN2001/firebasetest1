package com.unipi.boidanis.firebasetest1;

import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link safetyFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class safetyFragment extends Fragment {

    View[] views = new View[2];
    int res[] = {R.id.button_1, R.id.button_2};
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    public safetyFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment safetyFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static safetyFragment newInstance(String param1, String param2) {
        safetyFragment fragment = new safetyFragment();
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
        View view=inflater.inflate(R.layout.fragment_safety, container, false);
        for (int i = 0; i < res.length; i++) {
            views[i] = view.findViewById(res[i]);
        }
        views[0].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showMessage("Infant Safety Tips","As you hold your new bundle of joy in the hospital, you promise to always love and keep your baby safe. You know he/she depends on you for everything, and you want to give your baby your best. Besides following general home safety tips and childproofing your home, below are safety tips especially for baby:\n" +
                        "\n" +
                        "Bathing\n" +
                        "Bathe baby in a specially designed, slip-resistant infant bathtub.\n" +
                        "Fill tub with 2-3 inches of warm—not hot—water (check temperature with your wrist or tub thermometer. The water should be between 96-100 degrees Fahrenheit).\n" +
                        "Stay within arm's reach of your baby while he or she is in the tub.\n" +
                        "Nursery\n" +
                        "Bassinet and/or crib should meet current safety standards.\n" +
                        "Keep items such as pillows, comforters, quilts, and stuffed toys out of the bed.\n" +
                        "Mattress should be firm and fit snugly within the bassinet/crib.\n" +
                        "Remove mobiles when baby can sit on his or her own.\n" +
                        "Remove bumper pads and shift crib mattress to lowest position when baby can pull to a standing position.\n" +
                        "Keep bassinet/crib positioned away from windows, heaters, lamps, and other furniture.\n" +
                        "Do not leave baby unattended on a changing table, and be sure to use the safety strap.\n" +
                        "Avoid walking away from baby by keeping all changing supplies within arm's reach of changing table.\n" +
                        "Always put your baby to sleep on his or her back.\n" +
                        "Feeding\n" +
                        "During the first year of life, eating is constantly an adventure. Baby can rapidly move from breast milk/formula to trying his/her first table foods. Plus, babies use their mouths to learn about their world, so many non-food items also find their way into little mouths. Due to all of the action these mouths see, choking is an inherent danger. Following are a few tips for keeping your baby safe:\n" +
                        "\n" +
                        "Until age 4, avoid foods that can block the airways such as: peanut butter, hot dogs, popcorn, whole grapes, raw carrots, raisins, nuts, hard candies or toffees and chewing gum.\n" +
                        "Provide safe finger foods such as bananas, well-cooked pasta and vegetables, o-shaped low-sugar cereals (such as Cheerios).\n" +
                        "Keep items such as coins, buttons, balloons, safety pins, barrettes, and rocks out of your child's reach.\n" +
                        "Follow age recommendations on toys, especially those with small parts, and make sure toys are in good repair.\n" +
                        "Be vigilant. Small children put many things in their mouths. A watchful" +
                        " adult is often the best defense.\n" +

                        "\n" +
                        "Other\n" +
                        "Purchase and correctly install an infant car safety seat.\n" +
                        "Avoid burns by not holding your baby while cooking or holding hot food or beverages.\n" +
                        "Never leave baby unattended on beds, sofas, chairs, or any place where he or she may fall.\n" +
                        "Install baby gates at the top and bottom of stairways.\n" +
                        "Never leave baby alone with other young children or with pets.\n" +
                        "Before baby begins crawling, childproof your home.");
            }
        });
        views[1].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showMessage("If Your Child is Choking","Hold the baby face down on your forearm, with the infant's head in your hand.\n" +
                        "Give up to five blows to the back with the heel of your free hand.\n" +
                        "Turn the baby over and give up to five chest thrusts, placing the heel of your free hand on the lower half of the breastbone.\n" +
                        "Alternate between the five back blows and five chest thrusts until the object in the baby's throat comes out or the baby becomes unconscious.\n" +
                        "If the baby becomes unconscious, have someone call the ambulance, and begin giving CPR.\n" +
                        "If you are alone, call 911 after you have given CPR for about a minute.");
            }
        });
        return view;
    }
    void showMessage(String title, String message) {
        new AlertDialog.Builder(getContext()).setTitle(title).setMessage(message).setCancelable(true).show();
    }
}