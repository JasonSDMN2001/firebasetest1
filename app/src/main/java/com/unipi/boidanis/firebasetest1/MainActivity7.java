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
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
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

public class MainActivity7 extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    String moment,babyname;
    FirebaseAuth mAuth;
    FirebaseUser user;
    FirebaseDatabase database;
    private Uri imageUri;
    DatabaseReference reference;
    ImageView imageview;
    StorageReference storreference = FirebaseStorage.getInstance().getReference();
    private final int GALLERY_REQ_CODE = 1000;
    ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main7);
        Spinner spinner = (Spinner) findViewById(R.id.spinner2);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.moment_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);
        database = FirebaseDatabase.getInstance();
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        if (user!=null){
            reference=database.getReference("Users").child(user.getUid());
            babyname = getIntent().getStringExtra("babyname");
        }else{
            Toast.makeText(MainActivity7.this, "Please check your internet connection", Toast.LENGTH_SHORT).show();
            finish();
            startActivity(getIntent());
        }
        //"moments"->"ταδε μομεντ ονομα"->"imageurl"
        imageview = findViewById(R.id.imageView5);
        progressBar = findViewById(R.id.progressBar4);
        progressBar.setVisibility(View.INVISIBLE);
        imageview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent galleryIntent = new Intent();
                galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");
                someActivityResultLauncher.launch(galleryIntent);
            }
        });
        Button button = (Button) findViewById(R.id.button3);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(imageUri != null) {
                    StorageReference fileRef = storreference.child(System.currentTimeMillis() + "." + getFileExtension(imageUri));
                    fileRef.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    String key1 = reference.push().getKey();
                                    MomentImages momentImages = new MomentImages(key1,moment,uri.toString(),babyname);
                                    reference.child(babyname).child("moments").child(key1).setValue(momentImages);
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
                }else{
                    Toast.makeText(MainActivity7.this, "Please select picture", Toast.LENGTH_SHORT).show();
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

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        moment = (String)adapterView.getItemAtPosition(i);

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}