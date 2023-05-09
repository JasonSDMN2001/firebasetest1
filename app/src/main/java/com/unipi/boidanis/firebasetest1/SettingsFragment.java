package com.unipi.boidanis.firebasetest1;

import static android.content.Context.MODE_PRIVATE;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.SwitchCompat;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.fragment.app.Fragment;

import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessaging;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SettingsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SettingsFragment extends Fragment {
    FirebaseAuth mAuth;
    FirebaseDatabase database;
    DatabaseReference myRef,reference;
    FirebaseUser user;
    SharedPreferences sharedPreferences;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public SettingsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SettingsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SettingsFragment newInstance(String param1, String param2) {
        SettingsFragment fragment = new SettingsFragment();
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
        View view = inflater.inflate(R.layout.fragment_settings, container, false);
        database = FirebaseDatabase.getInstance();
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        EditText editText = (EditText) view.findViewById(R.id.editTextTextPersonName);
        try{
            editText.setText(user.getDisplayName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        Button button = (Button) view.findViewById(R.id.button5);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!editText.getText().toString().matches("")){
                    changeUserProfile(editText.getText().toString(),
                            null,user);
                }else{
                    showMessage("Attention","Please enter a non empty username");
                }
            }
        });
        SwitchCompat simpleSwitch2 = (SwitchCompat) view.findViewById(R.id.switch2);
        simpleSwitch2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean notificationcheck = simpleSwitch2.isChecked();
                if(simpleSwitch2.isChecked()){
                    FirebaseMessaging.getInstance().getToken()
                            .addOnCompleteListener(new OnCompleteListener<String>() {
                                @Override
                                public void onComplete(@NonNull Task<String> task) {
                                    if (!task.isSuccessful()) {
                                        Log.w(TAG, "Fetching FCM registration token failed", task.getException());
                                        return;
                                    }

                                    // Get new FCM registration token
                                    String token = task.getResult();

                                    // Log and toast
                                    Log.d(TAG, token);
                                    Toast.makeText(getContext(), "You will now be able to receive face a day notifications", Toast.LENGTH_SHORT).show();

                                }
                            });
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putBoolean("notifications2", notificationcheck);
                    editor.apply();
                }else{
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putBoolean("notifications2", notificationcheck);
                    editor.apply();
                }
            }
        });

        SwitchCompat simpleSwitch = (SwitchCompat) view.findViewById(R.id.switch1);
        simpleSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean notificationcheck = simpleSwitch.isChecked();
                if(simpleSwitch.isChecked()){
                    if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_NOTIFICATION_POLICY)
                            != PackageManager.PERMISSION_GRANTED ) {
                        ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_NOTIFICATION_POLICY},
                                123);
                    }
                    NotificationChannel channel = new NotificationChannel("1345", "notifications",
                            NotificationManager.IMPORTANCE_DEFAULT);
                    NotificationManager notificationManager =
                            (NotificationManager) getActivity().getSystemService(Context.NOTIFICATION_SERVICE);
                    notificationManager.createNotificationChannel(channel);
                    NotificationCompat.Builder builder =
                            new NotificationCompat.Builder(getContext(), "1345");
                    builder.setContentTitle("Ready to")
                            .setSmallIcon(R.mipmap.ic_launcher)
                            .setContentText("receive notifications")
                            .setPriority(NotificationCompat.PRIORITY_HIGH)
                            .setAutoCancel(true);
                    notificationManager.notify(3, builder.build());
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putBoolean("notifications", notificationcheck);
                    editor.putString("user",mAuth.getUid());
                    editor.apply();

                }else{
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putBoolean("notifications", notificationcheck);
                    editor.putString("user",mAuth.getUid());
                    editor.apply();
                }
            }
        });
        boolean notificationpreference = sharedPreferences.getBoolean("notifications", false);
        String userpref = sharedPreferences.getString("user", "");
        boolean notificationpreference2 = sharedPreferences.getBoolean("notifications2",false);
        if(userpref.matches(user.getUid())){
            simpleSwitch.setChecked(notificationpreference);
            simpleSwitch2.setChecked(notificationpreference2);
        }
        return view;
    }
    private void changeUserProfile(String displayName, @Nullable String imageUrl, FirebaseUser user){
        UserProfileChangeRequest profileChangeRequest = new UserProfileChangeRequest.Builder()
                .setDisplayName(displayName)
                //.setPhotoUri(Uri.parse(imageUrl))
                .build();
        //user.updatePhoneNumber();
        user.updateProfile(profileChangeRequest).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful())
                    Toast.makeText(getContext(), "User profile updated!", Toast.LENGTH_SHORT).show();
                else showMessage("Error",task.getException().getLocalizedMessage());
            }
        });
    }
    void showMessage(String title, String message){
        new AlertDialog.Builder(getContext()).setTitle(title).setMessage(message).setCancelable(true).show();
    }
}