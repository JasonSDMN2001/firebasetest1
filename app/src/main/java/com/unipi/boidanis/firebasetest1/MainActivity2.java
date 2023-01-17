package com.unipi.boidanis.firebasetest1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
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
        ImageView mImageView ;
        mImageView = (ImageView)findViewById(R.id.imageView); //this is your imageView
        mImageView .setImageDrawable(getResources().getDrawable( R.drawable.blink));
        AnimationDrawable frameAnimation = (AnimationDrawable) mImageView.getDrawable(); frameAnimation.start();
    }
    void showMessage(String title, String message){
        new AlertDialog.Builder(this).setTitle(title).setMessage(message).setCancelable(true).show();
    }
    public void signup(View view){
        mAuth.createUserWithEmailAndPassword(email.getText().toString(),password.getText().toString())
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            showMessage("Success!","User authenticated");
                            showMessage("Success!",mAuth.getUid());
                            user = mAuth.getCurrentUser();
                            changeUserProfile("Electra",
                                    "https://thumbs.gfycat.com/SharpCookedGiraffe-size_restricted.gif",user);

                        }else {
                            showMessage("Error",task.getException().getLocalizedMessage());
                        }
                    }
                });

    }
    public void signin(View view){
        if(!email.getText().toString().matches("")&&!password.getText().toString().matches("")) {
            mAuth.signInWithEmailAndPassword(email.getText().toString(), password.getText().toString())
                    .addOnCompleteListener((task) -> {
                        if (task.isSuccessful()) {
                            showMessage("Success!", "Ok");

                            Intent intent = new Intent(this, MainActivity3.class);
                            startActivity(intent);
                            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                        } else {
                            showMessage("Error", task.getException().getLocalizedMessage());
                        }
                    });

        }else {
            showMessage("Please fill","the remaining fields");
        }

    }
    private void changeUserProfile(String displayName, String imageUrl, FirebaseUser user){
        UserProfileChangeRequest profileChangeRequest = new UserProfileChangeRequest.Builder()
                .setDisplayName(displayName)
                .setPhotoUri(Uri.parse(imageUrl))
                .build();
        //user.updatePhoneNumber();
        user.updateProfile(profileChangeRequest).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful())
                    Toast.makeText(MainActivity2.this, "User profile updated!", Toast.LENGTH_SHORT).show();
                else showMessage("Error",task.getException().getLocalizedMessage());
            }
        });
    }
}