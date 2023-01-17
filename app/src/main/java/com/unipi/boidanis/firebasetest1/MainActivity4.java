package com.unipi.boidanis.firebasetest1;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.EditText;

public class MainActivity4 extends AppCompatActivity {
    EditText editText;
    private String weight;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main4);
        editText = findViewById(R.id.editTextTextPersonName8);
    }

    public void go2(View view){
        if(!editText.getText().toString().matches("")) {
            weight = editText.getText().toString();
            Intent intent = new Intent();
            intent.putExtra("weight",weight);
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