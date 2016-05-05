package com.stumbleapp.stumble;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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

        //Workaround to get the onNewIntent method to run
        PlayStreamActivity m = new PlayStreamActivity();
        m.onNewIntent(this.getIntent());

        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        video = (VideoView) findViewById(R.id.videoView);

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

        video.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mp, int what, int extra) {

                return false;
            }
        });

//        video.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if(paused) {
//                    video.resume();
//                    paused = false;
//                }
//                else{
//                    video.pause();
//                    paused = true;
//                }
//            }
//        });

    }

//    private void getURL(String streamId) {
//        Firebase stream = new Firebase("https://projecttest.firebaseio.com/streams");
//        stream.child(streamId).addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot snapshot) {
//                Stream stream = snapshot.getValue(Stream.class);
//                System.out.println(stream.getUrl());
//            }
//            @Override public void onCancelled(FirebaseError error) { }
//        });
//    }
//
//    @Override
//    protected void onNewIntent(Intent intent) {
//        super.onNewIntent(intent);
//        setIntent(intent);//must store the new intent unless getIntent() will return the old one
//        Intent data = getIntent();
//        streamId = data.getStringExtra("userID");
//        Log.i("UserID notification", streamId+"");
//        getURL(streamId);
//    }

}
