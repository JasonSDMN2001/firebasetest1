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
import java.util.concurrent.TimeUnit;

public class MainActivity4 extends AppCompatActivity {
    EditText editText;
    CalendarView calendarView;
    private String weight;
    Date date,date1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main4);
        editText = findViewById(R.id.editTextTextPersonName8);
        calendarView = findViewById(R.id.calendarView);
        //date1 = Long.parseLong(getIntent().getStringExtra("last date"));
        date1 = (Date) getIntent().getExtras().getSerializable("last date");
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {

            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month,
                                            int dayOfMonth) {
                Calendar c = Calendar.getInstance();
                c.set(year,month,dayOfMonth);
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
            boolean check = validateinfo(weight);
            if(check){

                int difference = WeekCalculation(date1,date);
                if(date.getTime()>date1.getTime()&&difference<=1){
                    Intent intent = new Intent();
                    intent.putExtra("weight",weight);
                    intent.putExtra("date",date);
                    setResult(Activity.RESULT_OK, intent);
                    finish();
                }else{
                    //Toast.makeText(this, "Please enter a later date than your last recorded:"+date1 + "and no more than 7 days away from the last", Toast.LENGTH_SHORT).show();
                    showMessage("Please enter a later date than your last recorded:"+date1,"and no more than 7 days away from the last.Also keep in mind that during the first week your child's weight will drop,so feel free" +
                            "to check again in a couple of days and input it in the app");
                }
            }else{
                Toast.makeText(this, "Check your parameters or remove space", Toast.LENGTH_SHORT).show();

            }
        }else {
            showMessage("Please","Enter weight");
        }

    }
    public int WeekCalculation(Date date1,Date date) {
        long i = date1.getTime();
        long j = date.getTime();
        long daysDiff = TimeUnit.DAYS.convert(j - i, TimeUnit.MILLISECONDS);//604800//1 week
        long k = (long) Math.floor(daysDiff / 7.0);
        return (int) k;
    }
    private boolean validateinfo(String weight) {
        if(!weight.matches("[0-9]\\.[0-9]{1,2}$")){
            editText.requestFocus();
            editText.setError("weight between 0.1-10kg");
            return false;
        }else{
            return true;
        }
    }

    void showMessage(String title, String message){
        new AlertDialog.Builder(this).setTitle(title).setMessage(message).setCancelable(true).show();
    }
}