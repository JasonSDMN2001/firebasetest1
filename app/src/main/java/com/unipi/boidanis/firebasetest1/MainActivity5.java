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
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

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
    Button uploadBtn;
    ImageView imageview;
    private Uri imageUri;
    StorageReference storreference = FirebaseStorage.getInstance().getReference();
    private final int GALLERY_REQ_CODE = 1000;
    ProgressBar progressBar;
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
        Button button = (Button) findViewById(R.id.button13);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!editText.getText().toString().matches("")&&!editText2.getText().toString().matches("")&&imageUri != null){

                    StorageReference fileRef = storreference.child(System.currentTimeMillis() + "." + getFileExtension(imageUri));
                    fileRef.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    ChildInfo childInfo = new ChildInfo(editText.getText().toString(),date,uri.toString());
                                    WeightData weightData = new WeightData(date,0,Float.parseFloat(editText2.getText().toString()));
                                    String key1 = reference.push().getKey();
                                    reference.child(childInfo.name).child("weightData").child(key1).setValue(weightData);
                                    String key = reference.push().getKey();
                                    reference.child(childInfo.name).child(key).setValue(childInfo);
                                    progressBar.setVisibility(View.INVISIBLE);
                                    imageview.setImageResource(R.drawable.ic_baseline_add_photo_alternate_24);
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
                    Toast.makeText(getApplicationContext(), "Incorrect arguments", Toast.LENGTH_SHORT).show();
                }
            }
        });
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
    private String getFileExtension(Uri mUri){

        ContentResolver cr = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cr.getType(mUri));
    }
}