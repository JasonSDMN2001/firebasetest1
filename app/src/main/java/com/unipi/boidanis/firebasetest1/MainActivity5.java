package com.unipi.boidanis.firebasetest1;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.Calendar;
import java.util.Date;

public class MainActivity5 extends AppCompatActivity implements AdapterView.OnItemSelectedListener{
    FirebaseAuth mAuth;
    FirebaseUser user;
    FirebaseDatabase database;
    DatabaseReference reference;
    EditText editText,editText2;
    CalendarView calendarView;
    Date date;
    Button uploadBtn;
    ImageView imageview;
    private Uri imageUri;
    StorageReference storreference = FirebaseStorage.getInstance().getReference();
    private final int GALLERY_REQ_CODE = 1000;
    ProgressBar progressBar;
    String gender;
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
        imageview = findViewById(R.id.imageView);
        progressBar = findViewById(R.id.progressBar2);
        progressBar.setVisibility(View.INVISIBLE);
        imageview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent galleryIntent = new Intent();
                galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");
                someActivityResultLauncher.launch(galleryIntent);
                //startActivityForResult(galleryIntent, 2);

            }
        });
        Spinner spinner = (Spinner) findViewById(R.id.spinner2);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.gender_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);
        Button button = (Button) findViewById(R.id.button13);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!editText.getText().toString().matches("")&&!editText2.getText().toString().matches("")&&imageUri != null){
                    String weight = editText2.getText().toString();
                    boolean check = validateinfo(weight);
                    if(check){
                        StorageReference fileRef = storreference.child(System.currentTimeMillis() + "." + getFileExtension(imageUri));
                        fileRef.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        ChildInfo childInfo = new ChildInfo(editText.getText().toString(),date,uri.toString(),gender);
                                        String key1 = reference.push().getKey();
                                        WeightData weightData = new WeightData(key1,date,0,Float.parseFloat(editText2.getText().toString()),childInfo.name);
                                        reference.child(childInfo.name).child("weightData").child(key1).setValue(weightData);
                                        String key = reference.push().getKey();
                                        reference.child(childInfo.name).child(key).setValue(childInfo);
                                        progressBar.setVisibility(View.INVISIBLE);
                                        imageview.setImageResource(R.drawable.ic_baseline_add_photo_alternate_24);
                                        /*DatabaseReference reference = database.getReference("All Weight Data");
                                        String key2 = reference.push().getKey();
                                        reference.child(key2).setValue(weightData);*/
                                        StatisticsCalculation(Float.parseFloat(weight),0);
                                        milestonePreperation(childInfo.name);
                                        finish();
                                        Intent intent = new Intent(getApplicationContext(), MainActivity3.class);
                                        startActivity(intent);
                                    }
                                });
                            }
                        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                                progressBar.setVisibility(View.VISIBLE);
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                progressBar.setVisibility(View.INVISIBLE);
                            }
                        });
                    }else{
                        Toast.makeText(getApplicationContext(), "Check your parameters", Toast.LENGTH_SHORT).show();

                    }
                }else{
                    Toast.makeText(getApplicationContext(), "Incorrect arguments", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void milestonePreperation(@NonNull String babyname) {
        DatabaseReference ref = database.getReference("Users").child(user.getUid()).child(babyname).child("milestones");
        String key = ref.push().getKey();
        Milestones milestones = new Milestones("Follows object to midline",1,5,false,key,babyname);
        ref.child(key).setValue(milestones);
        String key2 = ref.push().getKey();
        Milestones milestones2 = new Milestones("Follows object past midline",5,10,false,key2,babyname);
        ref.child(key2).setValue(milestones2);
        String key3 = ref.push().getKey();
        Milestones milestones3 = new Milestones("Lifts head 45 degrees",7,16,false,key3,babyname);
        ref.child(key3).setValue(milestones3);
        String key4 = ref.push().getKey();
        Milestones milestones4 = new Milestones("Sits supported",11,19,false,key4,babyname);
        ref.child(key4).setValue(milestones4);
        String key5 = ref.push().getKey();
        Milestones milestones5 = new Milestones("Lifts chest with arm support",9,15,false,key5,babyname);
        ref.child(key5).setValue(milestones5);
        String key6 = ref.push().getKey();
        Milestones milestones6 = new Milestones("Rolls over",12,20,false,key6,babyname);
        ref.child(key6).setValue(milestones6);
        String key7 = ref.push().getKey();
        Milestones milestones7 = new Milestones("Reaches for an object",13,16,false,key7,babyname);
        ref.child(key7).setValue(milestones7);
        String key8 = ref.push().getKey();
        Milestones milestones8 = new Milestones("Gramp noises",13,16,false,key8,babyname);
        ref.child(key8).setValue(milestones8);
        String key9 = ref.push().getKey();
        Milestones milestones9 = new Milestones("Puts hands together",15,18,false,key9,babyname);
        ref.child(key9).setValue(milestones9);
        String key10 = ref.push().getKey();
        Milestones milestones10 = new Milestones("Looks in direction of voice",1,7,false,key10,babyname);
        ref.child(key10).setValue(milestones10);
        String key11 = ref.push().getKey();
        Milestones milestones11 = new Milestones("Smiles spontaneously",1,3,false,key11,babyname);
        ref.child(key11).setValue(milestones11);
        String key12 = ref.push().getKey();
        Milestones milestones12 = new Milestones("Ooo/Aah sounds",4,12,false,key12,babyname);
        ref.child(key12).setValue(milestones12);
        String key13 = ref.push().getKey();
        Milestones milestones13 = new Milestones("Social smile",4,8,false,key13,babyname);
        ref.child(key13).setValue(milestones13);
    }

    ActivityResultLauncher<Intent> someActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        // There are no request codes
                        Intent data = result.getData();
                        imageUri = data.getData();
                        imageview.setImageURI(imageUri);
                    }
                }
            });
    private void StatisticsCalculation(float weight,int week) {
        DatabaseReference statreference;
        if(week<10){
            statreference = database.getReference("All weight data").child(gender).child("week 0"+week).child("weight");
        }else{
            statreference = database.getReference("All weight data").child(gender).child("week "+week).child("weight");
        }
        statreference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                float stat_weight = Float.parseFloat(snapshot.getValue().toString());
                statreference.setValue(stat_weight+weight);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        DatabaseReference reference2;
        if(week<10){
            reference2 = database.getReference("All weight data").child(gender).child("week 0"+week).child("babies");
        }else{
            reference2 = database.getReference("All weight data").child(gender).child("week "+week).child("babies");
        }

        reference2.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int babies = Integer.parseInt(snapshot.getValue().toString());
                reference2.setValue(babies+1);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
    private boolean validateinfo(String weight) {
        if(!weight.matches("[0-9]\\.[0-9]{1,2}$")){
            editText2.requestFocus();
            editText2.setError("weight between 0.1-10kg");
            return false;
        }else{
            return true;
        }
    }
    private String getFileExtension(Uri mUri){

        ContentResolver cr = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cr.getType(mUri));
    }
    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        gender = (String) adapterView.getItemAtPosition(i);

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}