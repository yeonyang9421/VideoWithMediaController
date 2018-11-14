package com.example.edu.videowithmediacontroller;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    VideoView mVideoView;
    Button mVideoButton, mTakeButton;

    public static final int REQUEST_CODE_VEDIO = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mVideoView = (VideoView) findViewById(R.id.videoView);
        mVideoButton = (Button) findViewById(R.id.videoViewFromGalleryButton);
        mTakeButton = (Button) findViewById(R.id.takeButton);
        mTakeButton.setOnClickListener(this);
        mVideoButton.setOnClickListener(this);


        int permission = ContextCompat.checkSelfPermission(this, Manifest.permission.INTERNET);
        if (permission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.INTERNET}, REQUEST_CODE_VEDIO);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_CODE_VEDIO:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED
                        && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                    Log.i("", "Permission has been granted by user");
                }
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.videoViewFromGalleryButton:
                Intent intent = new Intent();
                intent.setType("video/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent, 100);
                break;
            case R.id.takeButton:
                if (getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_ANY)) {
                    Intent intent1 = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
                    startActivityForResult(intent1, 200);
                    break;
                }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        switch (requestCode) {
            case 100:
                if (data != null) {
                    String videoPath = data.getData().toString();
                    mVideoView.setVideoPath(videoPath);
                    mVideoView.start();
                }
            case 200:
                if (requestCode == RESULT_OK) {
                    Uri videoUri = data.getData();
                    Toast.makeText(this, "" + videoUri, Toast.LENGTH_SHORT).show();
                    mVideoView.setVideoURI(videoUri);
                    mVideoView.start();
                }
        }

    }

}


