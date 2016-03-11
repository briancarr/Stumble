package com.stumbleapp.stumble;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import com.stumbleapp.me.stumble.R;

public class AddNewStreamActivity extends AppCompatActivity {
    EditText location;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_stream);

        location = (EditText) findViewById(R.id.location_editText);

        ImageButton mapLauncher;
        mapLauncher = (ImageButton) findViewById(R.id.imageButton);
        mapLauncher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MapsActivity.class);
                startActivityForResult(intent, 1);

            }

        });
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                location.setText(data.getStringExtra("Location"));
                Log.i("",""+data.getStringExtra("Location"));
            }
        }
    }


}
