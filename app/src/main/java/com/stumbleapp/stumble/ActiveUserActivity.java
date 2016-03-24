package com.stumbleapp.stumble;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.stumbleapp.me.stumble.R;

public class ActiveUserActivity extends AppCompatActivity {

    DatabaseHelper database;
    String Name;
    String location;

    String names[];
    String locations[];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_active_user);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ListView listview = (ListView) findViewById(R.id.listView);

        database = new DatabaseHelper(this);
        Cursor result = database.getAllData();

        names = new String[result.getCount()];
        locations = new String[result.getCount()];
        int i = 0;
        if(result.getCount() > 0){
            if (result.moveToFirst()) {
                do {
                    Name = result.getString(1);
                    names[i] = Name;
                    location = result.getString(2);
                    locations[i] = location;
                    Log.i("Database Query : ", Name);
                    Log.i("Database Query : ", location);
                    String Password = result.getString(1);
                    i++;
                } while (result.moveToNext());
            }
        }

        ArrayAdapter adapter = new ArrayAdapter(this, R.layout.list_item_streams,R.id.list_item_date_textview,names);

        listview.setAdapter(adapter);




        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent mapIntent = new Intent(getApplicationContext(), AddNewStreamActivity.class);
                startActivity(mapIntent);
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

}
