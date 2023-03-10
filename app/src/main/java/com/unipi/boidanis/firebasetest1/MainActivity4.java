package com.unipi.boidanis.firebasetest1;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Date;

public class MainActivity4 extends AppCompatActivity {
    EditText editText;
    CalendarView calendarView;
    private String weight;
    Date date;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main4);
        editText = findViewById(R.id.editTextTextPersonName8);
        calendarView = findViewById(R.id.calendarView);
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {

            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month,
                                            int dayOfMonth) {
                Calendar c = Calendar.getInstance();
                c.set(year,month,dayOfMonth);

                //Toast.makeText(getApplicationContext(),year +"+" + month+"+"+dayOfMonth, Toast.LENGTH_SHORT).show();

                if(year==0||dayOfMonth==0||month==0){
                    c=Calendar.getInstance();
                }
                date=c.getTime();
            }
        });
    }

    public void go2(View view){
        if(!editText.getText().toString().matches("")) {
            weight = editText.getText().toString();
            Intent intent = new Intent();
            intent.putExtra("weight",weight);
            intent.putExtra("date",date);
            setResult(Activity.RESULT_OK, intent);
            finish();
        }else {
            showMessage("Please","Enter weight");
        }

    }
    void showMessage(String title, String message){
        new AlertDialog.Builder(this).setTitle(title).setMessage(message).setCancelable(true).show();
    }
}