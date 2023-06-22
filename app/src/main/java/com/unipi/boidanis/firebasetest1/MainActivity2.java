package com.unipi.boidanis.firebasetest1;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Rect;
import android.graphics.drawable.AnimationDrawable;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Random;

public class MainActivity2 extends AppCompatActivity {
    EditText email,password;
    FirebaseAuth mAuth;
    FirebaseDatabase database;
    DatabaseReference myRef,reference;
    FirebaseUser user;
    ProgressBar progressBar;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        email= findViewById(R.id.editTextTextPersonName5);
        password=findViewById(R.id.editTextTextPersonName6);
        database = FirebaseDatabase.getInstance();
        mAuth = FirebaseAuth.getInstance();
        myRef = database.getReference("myMessage");
        reference = database.getReference("message");
        progressBar = findViewById(R.id.progressBar5);
        progressBar.setVisibility(View.INVISIBLE);
        ImageView mImageView ;
        mImageView = (ImageView)findViewById(R.id.imageView);
        //mImageView .setImageDrawable(getResources().getDrawable( R.drawable.blink));
        mImageView.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.blink,null));
        AnimationDrawable frameAnimation = (AnimationDrawable) mImageView.getDrawable(); frameAnimation.start();
        CheckBox checkBox = (CheckBox) findViewById(R.id.checkBox);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        Button button = (Button) findViewById(R.id.button8);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                button.setClickable(false);
                progressBar.setVisibility(View.VISIBLE);
                if(!email.getText().toString().matches("")&&!password.getText().toString().matches("")) {
                    mAuth.signInWithEmailAndPassword(email.getText().toString(), password.getText().toString())
                            .addOnCompleteListener((task) -> {
                                if (task.isSuccessful()) {
                                    if(checkBox.isChecked()){
                                        SharedPreferences.Editor editor = sharedPreferences.edit();
                                        editor.putString("current user",email.getText().toString());
                                        editor.putString("password",password.getText().toString());
                                        editor.apply();
                                    }
                                    Toast.makeText(MainActivity2.this, "Success", Toast.LENGTH_SHORT).show();
                                    finish();
                                    Intent intent = new Intent(getApplication(), MainActivity3.class);
                                    startActivity(intent);
                                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);

                                } else {
                                    showMessage("Error", task.getException().getLocalizedMessage());
                                    button.setClickable(true);
                                    progressBar.setVisibility(View.INVISIBLE);
                                }
                            });

                }else {
                    Toast.makeText(MainActivity2.this, "Please fill the remaining fields", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }
    void showMessage(String title, String message){
        new AlertDialog.Builder(this).setTitle(title).setMessage(message).setCancelable(true).show();
    }
    public void signup(View view){
        if(!email.getText().toString().matches("")&&!password.getText().toString().matches("")) {
        mAuth.createUserWithEmailAndPassword(email.getText().toString(),password.getText().toString())
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){

                            showMessage("Success!","User authenticated");
                            user = mAuth.getCurrentUser();

                        }else {
                            showMessage("Error",task.getException().getLocalizedMessage());
                        }
                    }
                });
        }else {
            Toast.makeText(MainActivity2.this, "Please fill the remaining fields", Toast.LENGTH_SHORT).show();
        }


    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if ( v instanceof EditText) {
                Rect outRect = new Rect();
                v.getGlobalVisibleRect(outRect);
                if (!outRect.contains((int)event.getRawX(), (int)event.getRawY())) {
                    v.clearFocus();
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
            }
        }
        return super.dispatchTouchEvent( event );
    }

}