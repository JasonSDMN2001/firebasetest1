package com.unipi.boidanis.firebasetest1;

import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaCodec;
import android.media.MediaCodecInfo;
import android.media.MediaFormat;
import android.media.MediaMetadataRetriever;
import android.media.MediaMuxer;
import android.net.Uri;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.Surface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.VideoView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FaceFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FaceFragment extends Fragment {
    VideoView videoView;
    String babyname;
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
        videoView = view.findViewById(R.id.videoView);
        Spinner getbabyname = (Spinner) getActivity().findViewById(R.id.spinner);
        babyname = getbabyname.getSelectedItem().toString();
        Button button = view.findViewById(R.id.button9);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!babyname.matches("")) {
                    Intent intent = new Intent(getActivity(),MainActivity8.class);
                    intent.putExtra("babyname",String.valueOf( babyname));
                    startActivity(intent);
                }else{
                    showMessage("oops","try again ");
                }
            }
        });
        return view;
    }
    private void createVideoFromImages() {
        // Create a list of URIs for the images
        List<Uri> imageUris = new ArrayList<>();
        // Retrieve image URIs from Firebase Storage
        // ...

        try {
            // Create a MediaCodec encoder
            MediaFormat format = MediaFormat.createVideoFormat("video/avc", 640, 480);
            format.setInteger(MediaFormat.KEY_COLOR_FORMAT, MediaCodecInfo.CodecCapabilities.COLOR_FormatSurface);
            format.setInteger(MediaFormat.KEY_BIT_RATE, 2000000);
            format.setInteger(MediaFormat.KEY_FRAME_RATE, 30);
            format.setInteger(MediaFormat.KEY_I_FRAME_INTERVAL, 5);
            MediaCodec encoder = MediaCodec.createEncoderByType("video/avc");
            encoder.configure(format, null, null, MediaCodec.CONFIGURE_FLAG_ENCODE);
            Surface inputSurface = encoder.createInputSurface();
            encoder.start();

            // Create a VideoWriter
            String outputPath = getOutputFilePath();
            MediaMuxer muxer = new MediaMuxer(outputPath, MediaMuxer.OutputFormat.MUXER_OUTPUT_MPEG_4);
            int trackIndex = -1;
            boolean muxerStarted = false;

            // Encode each image into a video frame and write it to the video file
            for (int i = 0; i < imageUris.size(); i++) {
                Uri uri = imageUris.get(i);

                // Extract the bitmap from the image using MediaMetadataRetriever
                MediaMetadataRetriever retriever = new MediaMetadataRetriever();
                retriever.setDataSource(getContext(), uri);
                Bitmap bitmap = retriever.getFrameAtTime();

                // Compress the bitmap data into a byte array
                ByteArrayOutputStream outStream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, outStream);
                byte[] byteBuffer = outStream.toByteArray();

                // Get an input buffer from the encoder
                ByteBuffer[] inputBuffers = encoder.getInputBuffers();
                int inputBufferIndex = encoder.dequeueInputBuffer(-1);
                if (inputBufferIndex >= 0) {
                    ByteBuffer inputBuffer = inputBuffers[inputBufferIndex];
                    inputBuffer.clear();
                    inputBuffer.put(byteBuffer);
                    encoder.queueInputBuffer(inputBufferIndex, 0, byteBuffer.length, i * 1000000 / 30, 0);
                }

                // Get an output buffer from the encoder and write it to the video file
                ByteBuffer[] outputBuffers = encoder.getOutputBuffers();
                MediaCodec.BufferInfo bufferInfo = new MediaCodec.BufferInfo();
                int outputBufferIndex = encoder.dequeueOutputBuffer(bufferInfo, 0);
                while (outputBufferIndex >= 0) {
                    ByteBuffer outputBuffer = outputBuffers[outputBufferIndex];
                    if (!muxerStarted) {
                        trackIndex = muxer.addTrack(encoder.getOutputFormat());
                        muxer.start();
                        muxerStarted = true;
                    }
                    outputBuffer.position(bufferInfo.offset);
                    outputBuffer.limit(bufferInfo.offset + bufferInfo.size);
                    muxer.writeSampleData(trackIndex, outputBuffer, bufferInfo);
                    encoder.releaseOutputBuffer(outputBufferIndex, false);
                    outputBufferIndex = encoder.dequeueOutputBuffer(bufferInfo, 0);
                }
            }

            // Release resources
            encoder.stop();
            encoder.release();
            if (muxerStarted) {
                muxer.stop();
                muxer.release();
            }

            // Show the video in a VideoView

            videoView.setVideoURI(Uri.parse(outputPath));
            videoView.start();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private String getOutputFilePath() {
        File file = new File(getActivity().getExternalFilesDir(null), "FaceADay.mp4");
        return file.getAbsolutePath();
    }
    void showMessage(String title, String message) {
        new AlertDialog.Builder(getActivity()).setTitle(title).setMessage(message).setCancelable(true).show();
    }

}