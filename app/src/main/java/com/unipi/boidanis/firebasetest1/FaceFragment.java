package com.unipi.boidanis.firebasetest1;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.Manifest;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.media.MediaCodec;
import android.media.MediaCodecInfo;
import android.media.MediaFormat;
import android.media.MediaMuxer;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Surface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.Spinner;
import android.widget.VideoView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FaceFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FaceFragment extends Fragment {
    ImageView imageView;
    String babyname;
    FirebaseDatabase database;
    DatabaseReference ref;
    FirebaseUser user;
    RecyclerView recyclerView;
    FirebaseAuth mAuth;
    FaceADayAdapter faceADayAdapter ;
    FacePicture facePicture;
    ArrayList<FacePicture>list;
    ArrayList<String>facesDayList;
    List<Uri> imageUris;
    List<Bitmap> bitmaps;
    private int currentImageIndex = 0;
    private Handler handler;
    private Runnable updateImageRunnable;
    private MediaController mediaController;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public FaceFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FaceFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static FaceFragment newInstance(String param1, String param2) {
        FaceFragment fragment = new FaceFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_face, container, false);
        /*if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1234);
        }*/

        imageView = view.findViewById(R.id.imageview11);

        Spinner getbabyname = (Spinner) getActivity().findViewById(R.id.spinner);
        babyname = getbabyname.getSelectedItem().toString();

        database = FirebaseDatabase.getInstance();
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        ref = database.getReference().child("Users").child(user.getUid()).child(babyname).child("Face A Day");
        recyclerView = (RecyclerView)view.findViewById(R.id.recyclerView5);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        list =new ArrayList<>();
        imageUris = new ArrayList<>();
        facesDayList = new ArrayList<>();
        bitmaps = new ArrayList<>();
        faceADayAdapter =new FaceADayAdapter(getContext(),list);
        recyclerView.setAdapter(faceADayAdapter);
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list.clear();
                facesDayList.clear();
                imageUris.clear();
                for(DataSnapshot dataSnapshot: snapshot.getChildren()) {
                    facePicture = dataSnapshot.getValue(FacePicture.class);
                    list.add(facePicture);
                    facesDayList.add(String.valueOf( facePicture.getDay()));
                    imageUris.add(Uri.parse( facePicture.getImageUrl()));
                }
                faceADayAdapter.notifyDataSetChanged();


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        Button button = view.findViewById(R.id.button9);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!babyname.matches("")&&list!=null) {
                    Intent intent = new Intent(getActivity(),MainActivity8.class);
                    intent.putExtra("babyname",String.valueOf( babyname));
                    intent.putExtra("facelist", facesDayList);
                    startActivity(intent);
                }else{
                    showMessage("oops","try again ");
                }
            }
        });
        Button button2 = view.findViewById(R.id.button6);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                handler = new Handler();
                updateImageRunnable = new Runnable() {
                    @Override
                    public void run() {
                        // Update the ImageView with the next image
                        Glide.with(getContext()).load(imageUris.get(currentImageIndex))
                                //.apply(new RequestOptions().diskCacheStrategy(DiskCacheStrategy.ALL).centerCrop())
                                .transition(DrawableTransitionOptions.withCrossFade())
                                .into(imageView);
                        // Increment the currentImageIndex
                        currentImageIndex = (currentImageIndex + 1) % imageUris.size();
                        if (currentImageIndex == 0) {
                            // Remove the callback to stop the execution
                            handler.removeCallbacks(updateImageRunnable);
                            button2.setEnabled(true);
                        } else {
                            // Post the updateImageRunnable to run again in 5 seconds
                            handler.postDelayed(updateImageRunnable, 5000);
                        }
                    }
                };
                handler.postDelayed(updateImageRunnable, 0); // Start the first update after 30 seconds
                button2.setEnabled(false);

            }
        });

        return view;
    }
    @Override
    public void onDestroy() {
        // Remove the updateImageRunnable callbacks when the activity is destroyed to prevent memory leaks
        handler.removeCallbacks(updateImageRunnable);
        super.onDestroy();
    }

    void showMessage(String title, String message) {
        new AlertDialog.Builder(getActivity()).setTitle(title).setMessage(message).setCancelable(true).show();
    }

}