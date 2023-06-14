package com.unipi.boidanis.firebasetest1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {
    SharedPreferences sharedPreferences;
    FirebaseAuth mAuth;

    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String currentUser = sharedPreferences.getString("current user", "");
        String password = sharedPreferences.getString("password", "");
        mAuth = FirebaseAuth.getInstance();
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (!NetworkUtils.isNetworkAvailable(MainActivity.this)) {
                    Toast.makeText(MainActivity.this, "No internet connection", Toast.LENGTH_SHORT).show();
                    onPause();
                }else{
                if(!currentUser.matches("")&&!password.matches("")){
                    try{
                        mAuth.signInWithEmailAndPassword(currentUser,password).addOnCompleteListener((task) -> {
                            if (task.isSuccessful()) {
                                finish();
                                Intent intent = new Intent(getApplication(), MainActivity3.class);
                                startActivity(intent);
                                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                            } else {
                                startActivity(new Intent(MainActivity.this,MainActivity2.class));
                                finish();                            }
                        });
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }else{
                    startActivity(new Intent(MainActivity.this,MainActivity2.class));
                    finish();
                }

            }}
        },2000);
    }
}