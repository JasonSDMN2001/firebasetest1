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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
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

import java.util.ArrayList;

public class MainActivity8 extends AppCompatActivity {
    FirebaseAuth mAuth;
    FirebaseUser user;
    FirebaseDatabase database;
    TextView textview;
    DatabaseReference reference;
    ImageView imageview;
    private Uri imageUri;
    String babyname;
    EditText editText;
    StorageReference storreference = FirebaseStorage.getInstance().getReference();
    private final int GALLERY_REQ_CODE = 1000;
    ProgressBar progressBar;
    ArrayList<String> list;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main8);

        database = FirebaseDatabase.getInstance();
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        if (user!=null){
            reference=database.getReference("Users").child(user.getUid());
            babyname = getIntent().getStringExtra("babyname");
        }else{
            Toast.makeText(MainActivity8.this, "Please check your internet connection", Toast.LENGTH_SHORT).show();
            finish();
            startActivity(getIntent());
        }
        textview = findViewById(R.id.textView3);
        textview.setText(user.getDisplayName());
        imageview = findViewById(R.id.imageView3);
        progressBar = findViewById(R.id.progressBar3);
        progressBar.setVisibility(View.INVISIBLE);
        editText = findViewById(R.id.editTextTextPersonName2);
        imageview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent galleryIntent = new Intent();
                galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");
                someActivityResultLauncher.launch(galleryIntent);
            }
        });
        list = (ArrayList<String>) getIntent().getExtras().getSerializable("facelist");
        Button button = (Button) findViewById(R.id.button2);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!editText.getText().toString().matches("")) {
                    boolean check = validateinfo(editText.getText().toString());
                    boolean check2=true;
                    for(String s : list){
                        if(editText.getText().toString().matches(s)){
                            check2=false;
                        }
                    }
                    if (check && check2) {
                        if (imageUri != null) {
                            StorageReference fileRef = storreference.child(System.currentTimeMillis() + "." + getFileExtension(imageUri));
                            fileRef.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                    fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                        @Override
                                        public void onSuccess(Uri uri) {
                                            String key1 = reference.push().getKey();
                                            FacePicture facePicture = new FacePicture(uri.toString(), key1, Integer.parseInt(editText.getText().toString()),babyname);
                                            reference.child(babyname).child("Face A Day").child(key1).setValue(facePicture);
                                            progressBar.setVisibility(View.INVISIBLE);
                                            imageview.setImageResource(R.drawable.ic_baseline_add_photo_alternate_24);
                                            finish();
                                            //Intent intent = new Intent(getApplicationContext(), MainActivity3.class);
                                            //startActivity(intent);
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
                        } else {
                            Toast.makeText(MainActivity8.this, "Please select picture", Toast.LENGTH_SHORT).show();
                        }
                    }else{
                        Toast.makeText(getApplicationContext(), "You've already submitted a picture for this day", Toast.LENGTH_SHORT).show();

                    }
                }else {
                    Toast.makeText(getApplicationContext(), "Please enter the day", Toast.LENGTH_SHORT).show();
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
    private boolean validateinfo(String weight) {
        if(!weight.matches("^(?:[1-9]|[1-9][0-9]|[12][0-9]{2}|3[0-5][0-9]|36[0-5])$")){
            editText.requestFocus();
            editText.setError("day between 1-365");
            return false;
        }else{
            return true;
        }
    }
}