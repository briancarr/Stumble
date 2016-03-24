package com.stumbleapp.stumble;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;

import com.stumbleapp.me.stumble.R;

public class PlayStreamActivity extends AppCompatActivity {
    String streamId;
    TextView title;
    VideoView video;
    private MediaController mController;
    private boolean paused;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_stream);
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        video = (VideoView) findViewById(R.id.videoView);

        Intent intent = this.getIntent();
        if(intent.getStringExtra(Intent.EXTRA_TEXT) != null ) {
            streamId = intent.getStringExtra(Intent.EXTRA_TEXT);
        }

        title = (TextView) findViewById(R.id.textView5);
        title.setText(streamId);

        MediaController mediaController = new MediaController(PlayStreamActivity.this);
        mediaController.setAnchorView(video);
        mediaController.setMediaPlayer(video);

        Uri videoURI = Uri.parse("rtsp://192.168.1.19:1935/live/myStream");
        video.setMediaController(mediaController);
        video.setVideoURI(videoURI);
        ViewGroup.LayoutParams params=video.getLayoutParams();
        params.width = getWindowManager().getDefaultDisplay().getWidth();
        video.setLayoutParams(params);
        video.start();

        video.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(paused) {
                    video.resume();
                    paused = false;
                }
                else{
                    video.pause();
                    paused = true;
                }
            }
        });

    }
}
