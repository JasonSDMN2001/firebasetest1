package com.unipi.boidanis.firebasetest1;

import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link sleepingFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class sleepingFragment extends Fragment {

    View[] views = new View[5];
    int res[] = {R.id.button_1, R.id.button_2, R.id.button_3};
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    public sleepingFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment sleepingFragment.
     */
    public static sleepingFragment newInstance(String param1, String param2) {
        sleepingFragment fragment = new sleepingFragment();
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
        View view= inflater.inflate(R.layout.fragment_sleeping, container, false);
        for (int i = 0; i < res.length; i++) {
            views[i] = view.findViewById(res[i]);
        }
        views[0].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showMessage("good sleeping habits","Help Your" +
                        " Child Sleep Better\n" +
                        "Feed your baby right before bedtime so they are not hungry when you put" +
                        " them in their crib.\n" +
                        "Put your child in their crib when they are sleepy but not yet asleep." +
                        " This lets them learn to fall asleep on their back \n Always place your baby" +
                        " in bed on their back\n" +
                        "Always put your baby in their crib on their back. Do this until they" +
                        " are 1 year old (Picture 1).\n" +
                        "Have a regular sleep schedule and a nighttime routine.\n" +
                        "Your child should go to sleep at the same time each night.\n" +
                        "Nighttime routines can include feeding, bathing, stories, " +
                        "soft music, etc.\n" +
                        "You can give your baby a pacifier while they sleep.\n" +
                        "A pacifier may lower the risk of sudden unexplained infant death " +
                        "syndrome (SUIDS).\n" +
                        "If your baby is breastfeeding, be sure they can put their mouth " +
                        "around the nipple and latch on before starting a pacifier. This is" +
                        " usually around 3 to 4 weeks of age.\n" +
                        "Do not put your child in their crib with a bottle or cup. Sleeping " +
                        "with milk or juice in their mouth can lead to tooth decay (cavities).\n" +
                        "Prevent Bad Habits\n" +
                        "Put your baby in their crib on their back for safety and so they " +
                        "learn to sleep alone.\n" +
                        "Safe sleep for infants up to 1 year of age also includes:\n" +
                        "Sharing a room but NOT a bed\n" +
                        "Nothing in the crib but your baby; no blankets, stuffed animals, " +
                        "or bumpers\n" +
                        "Night feedings\n" +
                        "When babies are 2 times their birth weight they may not need to be" +
                        " fed at night.\n" +
                        "Ask your baby’s health care provider when to start cutting down " +
                        "night feedings.\n" +
                        "When your baby is about 6 months old, try this if they wake up " +
                        "and fuss at night.\n" +
                        "Check on them, but don’t let them see you. If you do, they will" +
                        " expect you to keep coming back when they fuss.\n" +
                        "At first, fussing lasts about 10 minutes. The next night, let" +
                        " fussing last a bit longer.\n" +
                        "For this to work, you must keep doing it over and over again" +
                        " the same way.\n" +
                        "Remember, you are helping your child learn to go back to sleep " +
                        "on their own.\n" +
                        "If this does not work after a few weeks, you can stop. Try" +
                        " again in 4 to 6 weeks.");
            }
        });
        views[1].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showMessage("How much sleep does my baby need","2-3 months: what to expect from baby sleep\n" +
                        "At this age, babies sleep on and off during the day and night. Most babies sleep for 14-17 hours in every 24 hours.\n" +
                        "\n" +
                        "Young babies sleep in cycles that last 50-60 minutes. In young babies, each cycle is made up of active sleep and quiet sleep. Babies move around and grunt during active sleep, and sleep deeply during quiet sleep.\n" +
                        "\n" +
                        "At the end of each cycle, babies wake up for a little while. They might grizzle or cry. They might need help to settle for the next sleep cycle.\n" +
                        "\n" +
                        "At 2-3 months, babies start developing night and day sleep patterns. This means they tend to start sleeping more during the night.\n" +
                        "\n" +
                        "Around 3 months: what to expect from baby sleep\n" +
                        "Babies keep developing night and day sleep patterns.\n" +
                        "\n" +
                        "Their sleep cycles consist of:\n" +
                        "\n" +
                        "light sleep, when baby wakes easily\n" +
                        "deep sleep, when baby is sound asleep and very still\n" +
                        "dream sleep, when baby is dreaming.\n" +
                        "Sleep cycles also get longer, which might mean less waking and resettling during sleep. At this age, some babies might regularly be having longer sleeps at night – for example, 4-5 hours.\n" +
                        "\n" +
                        "Most babies still sleep for 14-17 hours in every 24 hours.\n" +
                        "\n" +
                        "3-6 months: what to expect from baby sleep\n" +
                        "At this age, most babies sleep for 12-15 hours every 24 hours.\n" +
                        "\n" +
                        "Babies might start moving towards a pattern of 2-3 daytime sleeps of up to two hours each.\n" +
                        "\n" +
                        "And night-time sleeps get longer at this age. For example, some babies might be having long sleeps of six hours at night by the time they’re six months old.\n" +
                        "\n" +
                        "But you can expect that your baby will still wake at least once each night.\n" +
                        "\n" +
                        "6-12 months: what to expect from baby sleep\n" +
                        "Babies sleep less as they get older. By the time your baby is one year old, baby will probably sleep for 11-14 hours every 24 hours.\n" +
                        "\n" +
                        "Sleep during the night\n" +
                        "From about six months, most babies have their longest sleeps at night.\n" +
                        "\n" +
                        "Most babies are ready for bed between 6 pm and 10 pm. They usually take less than 40 minutes to get to sleep, but some babies take longer.\n" +
                        "\n" +
                        "At this age, baby sleep cycles are closer to those of grown-up sleep – which means less waking at night. So your baby might not wake you during the night, or waking might happen less often.\n" +
                        "\n" +
                        "But many babies do wake during the night and need an adult to settle them back to sleep. Some babies do this 3-4 times a night.\n" +
                        "\n" +
                        "Sleep during the day\n" +
                        "At this age, most babies are still having 2-3 daytime naps that last for between 30 minutes and 2 hours.\n" +
                        "\n" +
                        "6-12 months: other developments that affect sleep\n" +
                        "From around six months, babies develop many new abilities that can affect their sleep or make them more difficult to settle:\n" +
                        "\n" +
                        "Babies learn to keep themselves awake, especially if something interesting is happening, or they’re in a place with a lot of light and noise.\n" +
                        "Settling difficulties can happen at the same time as crawling. You might notice your baby’s sleep habits changing when baby starts moving around more.\n" +
                        "Babies learn that things exist, even when they’re out of sight. Now that your baby knows you exist when you leave the bedroom, baby might call or cry out for you.\n" +
                        "Separation anxiety is when babies get upset because you’re not around. It might mean your baby doesn’t want to go to sleep and wakes up more often in the night. As babies mature they gradually overcome this worry.\n");
            }
        });
        return view;
    }
    void showMessage(String title, String message) {
        new AlertDialog.Builder(getContext()).setTitle(title).setMessage(message).setCancelable(true).show();
    }
}