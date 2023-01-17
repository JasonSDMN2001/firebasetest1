package com.unipi.boidanis.firebasetest1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
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
    EditText email,password,data,editText;
    FirebaseAuth mAuth;
    FirebaseDatabase database;
    DatabaseReference myRef,reference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        editText = findViewById(R.id.editTextTextPersonName);
        database = FirebaseDatabase.getInstance();
        email= findViewById(R.id.editTextTextPersonName4);
        password=findViewById(R.id.editTextTextPersonName2);
        data = findViewById(R.id.editTextTextPersonName3);
        mAuth = FirebaseAuth.getInstance();
        myRef = database.getReference("myMessage");
        reference = database.getReference("message");
    }
    void showMessage(String title, String message){
        new AlertDialog.Builder(this).setTitle(title).setMessage(message).setCancelable(true).show();
    }
    public void write(View view){
        myRef.setValue(editText.getText().toString());
        Toast.makeText(this, "Saved!", Toast.LENGTH_SHORT).show();
    }
    public void signup(View view){
        mAuth.createUserWithEmailAndPassword(email.getText().toString(),password.getText().toString())
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            showMessage("Success!","User authenticated");
                            showMessage("Success!",mAuth.getUid());
                        }else {
                            showMessage("Error",task.getException().getLocalizedMessage());
                        }
                    }
                });
    }
    public void signin(View view){
        mAuth.signInWithEmailAndPassword(email.getText().toString(),email.getText().toString())
                .addOnCompleteListener((task)->{
                    if(task.isSuccessful()){
                        showMessage("Success!","Ok");
                    }else {
                        showMessage("Error",task.getException().getLocalizedMessage());
                    }
                });
    }
    public void write2(View view){
        reference.setValue(data.getText().toString());
    }
    public void read(View view) {
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                showMessage("DB data change", snapshot.getValue().toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    public void write3(View view){
        DatabaseReference ref2 = database.getReference("Users");
        ref2.child(mAuth.getUid()).setValue(data.getText().toString());
    }
    public void go3(View view){
        Intent intent = new Intent(this, MainActivity2.class);
        startActivity(intent);
    }
}