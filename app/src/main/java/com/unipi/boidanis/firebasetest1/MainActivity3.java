package com.unipi.boidanis.firebasetest1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;


import android.content.Context;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.unipi.boidanis.firebasetest1.databinding.ActivityMain3Binding;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;

public class MainActivity3 extends AppCompatActivity {
    FirebaseAuth mAuth;
    FirebaseDatabase database;
    DatabaseReference myRef,reference;
    FirebaseUser user;
    Toolbar myToolbar;
    ActivityMain3Binding binding;
    Deque<Integer> integerDeque = new ArrayDeque<>(3);
    boolean flag =true;
    String babyname;
    Spinner spinner;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMain3Binding.inflate(getLayoutInflater());
        //bottomNavigationView = findViewById(R.id.bottomNavigationView);
        //setContentView(R.layout.activity_main3);
        setContentView(binding.getRoot());
        integerDeque.push(R.id.home);
        replaceFragment(new HomeFragment());
        binding.bottomNavigationView.setSelectedItemId(R.id.home);
        binding.bottomNavigationView.setOnItemSelectedListener(
                new NavigationBarView.OnItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        int id = item.getItemId();
                        if (integerDeque.contains(id)){
                            if(id==R.id.home){
                                if(integerDeque.size()!=1){
                                    if(flag){
                                        integerDeque.addFirst(R.id.home);
                                        flag=false;
                                    }
                                }
                            }
                            integerDeque.remove(id);
                        }
                        integerDeque.push(id);
                        replaceFragment(getFragment(item.getItemId()));
                        return true;
                    }
                }

        );
        /*binding.bottomNavigationView.setOnItemSelectedListener(item -> {
            switch (item.getItemId()){
                case R.id.home:
                    replaceFragment(new HomeFragment(),"FRAGMENT_HOME");
                    break;
                case R.id.growth:
                    replaceFragment(new growthFragment(),"FRAGMENT_GROWTH");
                    break;
                case R.id.other:
                    replaceFragment(new otherFragment(),"FRAGMENT_OTHER");
                    break;
            }
            return true;
        });*/

        myToolbar = findViewById(R.id.myToolbar);
        setSupportActionBar(myToolbar);
        database = FirebaseDatabase.getInstance();
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        ArrayList<String> list = new ArrayList<String>();
        spinner = findViewById(R.id.spinner);
        DatabaseReference reference =  database.getReference("Users").child(mAuth.getUid());
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot: snapshot.getChildren()){
                    babyname = dataSnapshot.getKey();
                    if(babyname!=null){
                        list.add(babyname);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        list.add("Add child");
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                ((TextView) adapterView.getChildAt(0)).setTextColor(Color.WHITE);
                replaceFragment(getFragment(integerDeque.peek()));
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        ArrayAdapter<String> adapter= new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, list);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

    }

    private Fragment getFragment(int itemId) {
        switch (itemId){
            case R.id.home:
                binding.bottomNavigationView.getMenu().getItem(0).setChecked(true);
                return new HomeFragment();
            case R.id.growth:
                binding.bottomNavigationView.getMenu().getItem(1).setChecked(true);
                return new growthFragment();
            case R.id.other:
                binding.bottomNavigationView.getMenu().getItem(2).setChecked(true);
                return new otherFragment();
        }
        binding.bottomNavigationView.getMenu().getItem(0).setChecked(true);
        return new HeadFragment();
    }

    private void replaceFragment(Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout,fragment);
        fragmentTransaction.commit();
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
    @Override
    public void onBackPressed() {
        integerDeque.pop();
        if(!integerDeque.isEmpty()){
            replaceFragment(getFragment(integerDeque.peek()));
        }else{
            finish();
        }
    }

    void showMessage(String title, String message){
        new AlertDialog.Builder(this).setTitle(title).setMessage(message).setCancelable(true).show();
    }
}