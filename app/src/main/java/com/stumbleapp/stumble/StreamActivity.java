package com.stumbleapp.stumble;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.stumbleapp.me.stumble.R;

public class StreamActivity extends AppCompatActivity {

    private String streamId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stream);

        Intent intent = this.getIntent();
        if(intent.getStringExtra(Intent.EXTRA_TEXT) != null ) {
            streamId = intent.getStringExtra(Intent.EXTRA_TEXT);
        }
    }
}
