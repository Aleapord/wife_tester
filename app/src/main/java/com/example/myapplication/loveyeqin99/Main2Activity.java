package com.example.myapplication.loveyeqin99;

import androidx.appcompat.app.AppCompatActivity;

import android.net.Uri;
import android.os.Bundle;
import android.widget.VideoView;

public class Main2Activity extends AppCompatActivity {

    private VideoView videoView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);


        videoView = findViewById(R.id.videoView2);
        videoView.setVideoURI(Uri.parse("android.resource://"+getPackageName()+"/raw/love"));
        videoView.start();

    }
}
