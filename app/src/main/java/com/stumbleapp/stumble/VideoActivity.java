package com.stumbleapp.stumble;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.VideoView;

import com.github.pedrovgs.DraggableListener;
import com.github.pedrovgs.DraggableView;
import com.squareup.picasso.Picasso;
import com.stumbleapp.me.stumble.R;

import butterknife.ButterKnife;
import butterknife.BindView;
import butterknife.OnClick;

/**
 * Sample activity created to show a video using a VideoView.
 *
 * @author Pedro Vicente Gómez Sánchez.
 */
public class VideoActivity extends FragmentActivity {

    private static final String APPLICATION_RAW_PATH =
            "android.resource://com.github.pedrovgs.sample/";
    private static final String VIDEO_POSTER =
            "http://wac.450f.edgecastcdn.net/80450F/screencrush.com/files/2013/11/the-amazing-spider-"
                    + "man-2-poster-rhino.jpg";
    private static final String VIDEO_THUMBNAIL =
            "http://wac.450f.edgecastcdn.net/80450F/screencrush.com/files/2013/11/the-amazing-spider-"
                    + "man-2-poster-green-goblin.jpg";
    private static final String VIDEO_TITLE = "The Amazing Spider-Man 2: Rise of Electro";

    @BindView(R.id.draggable_view) DraggableView draggableView;
    @BindView(R.id.video_view) VideoView videoView;
    @BindView(R.id.iv_thumbnail) ImageView thumbnailImageView;
    @BindView(R.id.iv_poster) ImageView posterImageView;
    private String streamUrl;

    /**
     * Initialize the Activity with some injected data.
     */
    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);

        Intent intent = getIntent();
        streamUrl = intent.getStringExtra("url");

        ButterKnife.bind(this);
        initializeVideoView();
        initializePoster();
        hookDraggableViewListener();
    }

    /**
     * Method triggered when the iv_thumbnail widget is clicked. This method shows a toast with the
     * video title.
     */
    @OnClick(R.id.iv_thumbnail) void onThubmnailClicked() {
        Toast.makeText(this, VIDEO_TITLE, Toast.LENGTH_SHORT).show();
    }

    /**
     * Method triggered when the iv_poster widget is clicked. This method maximized the draggableView
     * widget.
     */
    @OnClick(R.id.iv_poster) void onPosterClicked() {
        draggableView.maximize();
    }

    /**
     * Hook DraggableListener to draggableView to pause or resume VideoView.
     */
    private void hookDraggableViewListener() {
        draggableView.setDraggableListener(new DraggableListener() {
            @Override public void onMaximized() {
                startVideo();
            }

            //Empty
            @Override public void onMinimized() {
                //Empty
            }

            @Override public void onClosedToLeft() {
                pauseVideo();
            }

            @Override public void onClosedToRight() {
                pauseVideo();
            }
        });
    }

    /**
     * Pause the VideoView content.
     */
    private void pauseVideo() {
        if (videoView.isPlaying()) {
            videoView.pause();
        }
    }

    /**
     * Resume the VideoView content.
     */
    private void startVideo() {
        if (!videoView.isPlaying()) {
            videoView.start();
        }
    }

    /**
     * Initialize ViedeoView with a video by default.
     */
    private void initializeVideoView() {
        Uri path = Uri.parse(streamUrl);
        videoView.setVideoURI(path);
        videoView.start();
    }

    /**
     * Initialize some ImageViews with a video poster and a video thumbnail.
     */
    private void initializePoster() {
        Picasso.with(this)
                .load(VIDEO_POSTER)
                .placeholder(R.drawable.bg)
                .into(posterImageView);
        Picasso.with(this)
                .load(VIDEO_THUMBNAIL)
                .placeholder(R.drawable.bg)
                .into(thumbnailImageView);
    }
}