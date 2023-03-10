package com.unipi.boidanis.firebasetest1;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;
import java.util.Date;

public class MainActivity5 extends AppCompatActivity {
    FirebaseAuth mAuth;
    FirebaseUser user;
    FirebaseDatabase database;
    DatabaseReference reference;
    EditText editText,editText2;
    CalendarView calendarView;
    Date date;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main5);
        database = FirebaseDatabase.getInstance();
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        if (user!=null){
            reference=database.getReference("Users").child(user.getUid());
        }
        editText = findViewById(R.id.editTextTextPersonName9);
        editText2 = findViewById(R.id.editTextTextPersonName10);
        calendarView = findViewById(R.id.calendarView2);
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {

            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month,
                                            int dayOfMonth) {
                Calendar c = Calendar.getInstance();
                c.set(year,month,dayOfMonth);
                date=c.getTime();
            }
        });
        Button button = (Button) findViewById(R.id.button13);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!editText.getText().toString().matches("")&&!editText2.getText().toString().matches("")){
                    ChildInfo childInfo = new ChildInfo(editText.getText().toString(),date);
                    WeightData weightData = new WeightData(date,0,Float.parseFloat(editText2.getText().toString()));
                    String key1 = reference.push().getKey();
                    reference.child(childInfo.name).child("weightData").child(key1).setValue(weightData);
                    String key = reference.push().getKey();
                    reference.child(childInfo.name).child(key).setValue(childInfo);
                }else{
                    Toast.makeText(getApplicationContext(), "Please enter your baby's name", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    public void registerbabybirthdata(){

    }
}