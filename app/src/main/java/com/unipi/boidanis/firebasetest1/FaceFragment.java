package com.unipi.boidanis.firebasetest1;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.Manifest;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.media.MediaCodec;
import android.media.MediaCodecInfo;
import android.media.MediaFormat;
import android.media.MediaMetadataRetriever;
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

import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Surface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.VideoView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FaceFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FaceFragment extends Fragment {
    VideoView videoView;
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
    List<String> imageUris;
    List<Bitmap> bitmaps;
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
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1234);
        }

        videoView = view.findViewById(R.id.videoView);
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
                    imageUris.add(facePicture.getImageUrl());
                }
                faceADayAdapter.notifyDataSetChanged();
                if(!imageUris.isEmpty()){
                createVideoFromImages(imageUris);}

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

                videoView.setVideoURI(getOutputMediaFileUri());
                videoView.start();
            }
        });

        return view;
    }
    private void createVideoFromImages(List<String> imageUris) {
        try {

            MediaCodec codec = MediaCodec.createEncoderByType("video/avc");

            MediaFormat format = MediaFormat.createVideoFormat("video/avc", 480, 640);

            format.setInteger(MediaFormat.KEY_BIT_RATE, 200000);


            format.setInteger(MediaFormat.KEY_FRAME_RATE, 30);


            format.setInteger(MediaFormat.KEY_COLOR_FORMAT, MediaCodecInfo.CodecCapabilities.COLOR_FormatSurface);


            format.setInteger(MediaFormat.KEY_I_FRAME_INTERVAL, 5);


            codec.configure(format, null, null, MediaCodec.CONFIGURE_FLAG_ENCODE);


            Surface surface = codec.createInputSurface();


            codec.start();

            MediaMuxer muxer = new MediaMuxer(String.valueOf(getOutputMediaFileUri()), MediaMuxer.OutputFormat.MUXER_OUTPUT_MPEG_4);
            showMessage("10","");

            // Load images using Glide and convert them to bitmaps
            List<Bitmap> bitmaps = new ArrayList<>();
            showMessage("11","");

            for (String uri : imageUris) {
                showMessage("12","");

                Bitmap bitmap = Glide.with(getContext()).asBitmap().load(uri).submit().get();
                showMessage("13","");

                bitmaps.add(bitmap);
            }

            // Encode bitmaps to video frames
            for (int i = 0; i < bitmaps.size(); i++) {
                Bitmap bitmap = bitmaps.get(i);
                if (bitmap != null) {
                    Canvas canvas = surface.lockCanvas(null);
                    canvas.drawBitmap(bitmap, 0, 0, null);
                    surface.unlockCanvasAndPost(canvas);
                    codec.queueInputBuffer(codec.dequeueInputBuffer(-1), 0, 1, i * 1000000 / 30, 0);
                    MediaCodec.BufferInfo info = new MediaCodec.BufferInfo();
                    int outputBufferIndex = codec.dequeueOutputBuffer(info, 0);
                    while (outputBufferIndex >= 0) {
                        ByteBuffer outputBuffer = codec.getOutputBuffer(outputBufferIndex);
                        muxer.writeSampleData(0, outputBuffer, info);
                        codec.releaseOutputBuffer(outputBufferIndex, false);
                        outputBufferIndex = codec.dequeueOutputBuffer(info, 0);
                    }
                }
            }

            // Release resources
            codec.stop();
            codec.release();
            surface.release();
            muxer.stop();
            muxer.release();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    /*private String getOutputPath() {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String outputFileName = "VIDEO_" + timeStamp + ".mp4";
        return getActivity().getExternalFilesDir(null) + "/" + outputFileName;
    }*/
    private Uri getOutputMediaFileUri() {
        Uri videoUri = null;
        try {


            // Create a media file name
            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            String mediaFileName = "VID_" + timeStamp + ".mp4";

            // Get the directory for storing the video
            File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MOVIES), "MyAppVideos");

            // Create the directory if it doesn't exist
            if (!mediaStorageDir.exists()) {
                if (!mediaStorageDir.mkdirs()) {
                    Log.d(TAG, "Failed to create directory");
                    return null;
                }
            }

            // Create the file object
            File mediaFile = new File(mediaStorageDir.getPath() + File.separator + mediaFileName);

            // Save the video to the gallery
            ContentValues values = new ContentValues();
            values.put(MediaStore.Video.Media.TITLE, mediaFileName);
            values.put(MediaStore.Video.Media.DESCRIPTION, "My video description");
            values.put(MediaStore.Video.Media.MIME_TYPE, "video/mp4");
            values.put(MediaStore.Video.Media.DATA, mediaFile.getAbsolutePath());
            videoUri = requireContext().getContentResolver().insert(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, values);
            showMessage(videoUri.toString(), "");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return videoUri;
    }


    void showMessage(String title, String message) {
        new AlertDialog.Builder(getActivity()).setTitle(title).setMessage(message).setCancelable(true).show();
    }

}