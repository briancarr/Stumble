package com.stumbleapp.stumble;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.firebase.ui.FirebaseListAdapter;
import com.stumbleapp.me.stumble.R;

public class ActiveUserActivity extends AppCompatActivity {

    DatabaseHelper database;
    String Name;
    String location;

    String names[];
    String locations[];

    DBAdapter myDb;

    int[] imageIDs = {
            R.drawable.ic_launcher,
            R.drawable.ic_launcher,
            R.drawable.ic_launcher
    };
    int nextImageIndex = 0;
    public FirebaseListAdapter<Stream> mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_active_user);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final TextView name = (TextView) findViewById(R.id.list_item_name_textview);
        final TextView location = (TextView) findViewById(R.id.list_item_location_textview);

        ListView listview = (ListView) findViewById(R.id.listView);

        Firebase ref = new Firebase("https://projecttest.firebaseio.com/streams");

        //setup database access
        myDb = new DBAdapter(this);
        myDb.open();

        ref.limitToLast(5).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                for (DataSnapshot msgSnapshot: snapshot.getChildren()) {
                    Stream stream = msgSnapshot.getValue(Stream.class);
                    //public long insertRow(String name, String userID, String location, String url, String date, String time)
                    myDb.insertRow(stream.getName(), stream.getUser(), stream.getLocation(), stream.getUrl(), stream.getDate(), stream.getTime());
                }
            }
            @Override
            public void onCancelled(FirebaseError firebaseError) {
                Log.e("streams", "The read failed: " + firebaseError.getMessage());
            }
        });

        int imageId = imageIDs[nextImageIndex];
        nextImageIndex = (nextImageIndex + 1) % imageIDs.length;

        Cursor cursor = myDb.getAllRows();

        //Map cursor to view
        String[] fromFieldNames = new String[]{
                DBAdapter.KEY_NAME,
                DBAdapter.KEY_LOCATION
        };
        int[] toViewIds = new int[]{
                R.id.list_item_name_textview,
                R.id.list_item_location_textview
        };

        String[] fromFieldLocations = new String[]{};

        SimpleCursorAdapter adapter = new SimpleCursorAdapter(this,R.layout.list_item_streams, cursor,fromFieldNames,toViewIds);

        listview.setAdapter(adapter);

        //Button for adding new stream
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent mapIntent = new Intent(getApplicationContext(), AddNewStreamActivity.class);
                startActivity(mapIntent);
                //nackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG).setAction("Action", null).show();
            }
        });
    }

    @Override
    protected void onStop() {
        super.onStop();
    }
}
