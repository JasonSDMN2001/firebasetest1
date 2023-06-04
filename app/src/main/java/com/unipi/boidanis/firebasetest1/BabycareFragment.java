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
                showMessage("How to bathe your baby","You don't need to bathe your baby every day, but if they really enjoy it, there's no reason why you shouldn't.\n" +
                        "\n" +
                        "It's best not to bathe your baby straight after a feed or when they're hungry or tired. Make sure the room you're bathing them in is warm.\n" +
                        "\n" +
                        "Have everything you need at hand: a baby bath or clean washing-up bowl filled with warm water, 2 towels, a clean nappy, clean clothes and cotton wool.\n" +
                        "\n" +
                        "The water should be warm, not hot. Check it with your wrist or elbow and mix it well so there are no hot patches.\n" +
                        "Don't add any liquid cleansers to the bath water. Plain water is best for your baby's skin in the first month.\n" +
                        "Hold your baby on your knee and clean their face.\n" +
                        "Next, wash their hair with plain water, supporting them over the bowl.\n" +
                        "Once you've dried their hair gently, you can take off their nappy, wiping away any mess.\n" +
                        "Lower your baby gently into the bowl or bath using one hand to hold their upper arm and support their head and shoulders. Then use the other hand to gently swish the water over your baby without splashing.\n" +
                        "Keep your baby's head clear of the water.\n" +
                        "Never leave your baby alone in the bath, not even for a second.\n" +
                        "Lift your baby out and pat them dry, paying special attention to the creases in their skin.\n" +
                        "This is a good time to massage your baby. Massage can help them relax and sleep. Avoid using any oils or lotions until your baby is at least a month old.\n" +
                        "If your baby seems frightened of bathing and cries, try bathing together. Make sure the water isn't too hot. It's easier if someone else holds your baby while you get in and out of the bath.");
            }
        });
        views[3].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showMessage("How to cut Fingernails","Some babies are born with " +
                        "long nails and it's important to cut them in case they scratch themselves. " +
                        "You can buy special baby nail clippers or small, round-ended safety" +
                        " scissors. Or you could try filing them down with a fine emery board" +
                        " instead");
            }
        });
        views[4].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showMessage("Teeth","Most babies will develop teeth between 6 and 12" +
                        " months.\n" +
                        "There is a wide range of variability of when a first tooth may appear—some" +
                        " babies may not have any teeth by their first birthday! Around 3 months of" +
                        " age, babies will begin exploring the world with their mouth and have " +
                        "increased saliva and start to put their hands in their mouth. Many parents" +
                        " question whether or not this means that their baby is teething," +
                        " but a first tooth usually appears around 6 months old.\n" +
                        "\n" +
                        "Typically, the first teeth to come in are almost always the lower" +
                        " front teeth (the lower central incisors), and most children will " +
                        "usually have all of their baby teeth by age 3.\n" +
                        "\n"+"Fluoride should be added to your child's diet at 6 months of age.\n" +
                        "Fluoride is a mineral that helps prevent tooth decay by hardening" +
                        " the enamel of teeth. The good news is that fluoride is often added" +
                        " to tap water. Give your baby a few ounces of water in a sippy or " +
                        "straw cup when you begin them on solid foods (about 6 months of age)." +
                        " Speak with your pediatrician to see if your tap water contains " +
                        "fluoride or whether your child needs fluoride supplements. " +
                        "Fluoride is not typically found in most bottled water. ");
            }
        });
        return view;
    }
    void showMessage(String title, String message) {
        new AlertDialog.Builder(getContext()).setTitle(title).setMessage(message).setCancelable(true).show();
    }
}