package com.stumbleapp.stumble;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.TimePicker;

import com.firebase.client.Firebase;
import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.stumbleapp.me.stumble.R;

import java.util.Calendar;

public class AddNewStreamActivity extends AppCompatActivity {

    EditText location;
    EditText name;
    DatabaseHelper database;
    SQLiteDatabase db;
    String   mName;
    String  mLocation;
    Double Lat;
    Double Lng;

    //variables for date and time picker
    public static int _year;
    public static int _month;
    public static int _day;
    public static int _hour;
    public static int _minute;

    public static TextView time;
    public static TextView date;

    //base domain for streaming server
    private String baseDomain;

    Firebase fb;
    GeoFire geoFire;

    // url to create new product
    private static String url_create_stream = "http://192.168.1.14/my-site/createStream.php";

    // JSON Node names
    private static final String TAG_SUCCESS = "success";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_stream);

        /**
         * Setup references to Firebase database.
         * The GeoFire object uses the GeoFire library with a reference
         * to the Firebase Project.
         */
        fb = new Firebase("https://projecttest.firebaseio.com/");

        //uncomment when login is reactivated
        final String userId = fb.getAuth().getUid();

        baseDomain = "rtsp://192.168.1.14:1935/live/";

        database = new DatabaseHelper(this);

        location = (EditText) findViewById(R.id.location_editText);
        name = (EditText) findViewById(R.id.stream_name_editText);

        time = (TextView) findViewById(R.id.time_selection_textView);
        date = (TextView) findViewById(R.id.date_selection_textView);

        final Button startStream = (Button) findViewById(R.id.start_stream_button);
        final Button date = (Button) findViewById(R.id.date_button);
        final Button time = (Button) findViewById(R.id.time_button);

        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment newFragment = new DatePickerFragment();
                newFragment.show(getSupportFragmentManager(), "datePicker");
                //date.append(getDate());
            }
        });

        time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment newFragment = new TimePickerFragment();
                newFragment.show(getSupportFragmentManager(), "timePicker");
            }
        });

        /**
         * Sets up the button to launce the map activity.
         * Gets the selected geo point as a result.
         */
        ImageButton mapLauncher;
        mapLauncher = (ImageButton) findViewById(R.id.imageButton);
        mapLauncher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MapsActivity.class);
                startActivityForResult(intent, 1);
            }
        });


        /**
         * OnClick for the start stream button.
         * This creates the Firebase database object for the new stream.
         */
        startStream.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mName = name.getText().toString();
                mLocation = location.getText().toString();
                GeoLocation loc;
                loc = new GeoLocation(Lat, Lng);
                geoFire = new GeoFire(fb.child("locations"));

                // Constructor Stream(String name, String user, String location, String url, String date, String time)
                Stream stream = new Stream(mName,userId,mLocation,createStreamURI(),getDate(),getTime());
                fb = fb.child("streams").child(stream.getUser());//.push().setValue(stream);
                fb.child("name").setValue(stream.getName());
                fb.child("location").setValue(stream.getLocation());
                fb.child("url").setValue(stream.getUrl());
                fb.child("date").setValue(stream.getDate());
                fb.child("time").setValue(stream.getTime());
                fb.child("user").setValue(stream.getUser());
                createGeoFence(userId, loc);
            }
        });


    }

    private static String getTime() {
        String time = null;
        if(_minute < 10){
            time = _hour + ":" +"0"+ _minute;
        }else {
            time = _hour + ":" + _minute;
        }
        return  time;
    }

    private static String getDate() {
        String date = _day +"/"+_month+"/"+_year;
        return  date;
    }

    /**
     * Appends the date and time
     * to the textviews within the view.
     */
    public static void appendTime(){
        time.setText(getTime());
    }

    public static void appendDate(){
        date.setText(getDate());
    }

    /**
     * Creates the geofence using the
     * returned latitude and longitude
     * from the MapsActivity.
     * @param name
     * @param loc
     */
    public void createGeoFence(String name, GeoLocation loc){
        geoFire.setLocation(name, new GeoLocation(loc.latitude,loc.longitude));
    }

    private String getName() {
        String nameStr = name.getText().toString();
        return nameStr;
    }

    private String getLocation() {
        String locationStr = location.getText().toString();
        return locationStr;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                String address = data.getStringExtra("Address");
                Lat = data.getDoubleExtra("Lat",0.00);
                Lng = data.getDoubleExtra("Lng",0.00);
                if(address != null) {
                    location.setText(address);
                }
                Log.i("Address :",address);
                //Log.i("Lng",Lng.toString());
            }
        }
    }

    //Returns the URL for the stream using the name entered.
    public String createStreamURI(){
        String URI;
        URI  = baseDomain + mName;
        return URI;
    }


    /**
     * Classes for date and time selection fragments
     */
    public static class TimePickerFragment extends DialogFragment
            implements TimePickerDialog.OnTimeSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current time as the default values for the picker
            final Calendar c = Calendar.getInstance();
            int hour = c.get(Calendar.HOUR_OF_DAY);
            int minute = c.get(Calendar.MINUTE);

            // Create a new instance of TimePickerDialog and return it
            return new TimePickerDialog(getActivity(), this, hour, minute,
                    DateFormat.is24HourFormat(getActivity()));
        }

        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            _hour = hourOfDay;
            _minute = minute;
            appendTime();
        }
    }

    public static class DatePickerFragment extends DialogFragment
            implements DatePickerDialog.OnDateSetListener {


        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current date as the default date in the picker
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            // Create a new instance of DatePickerDialog and return it
            return new DatePickerDialog(getActivity(), this, year, month, day);
        }

        public void onDateSet(DatePicker view, int year, int month, int day) {
            _year = year;
            _month = month;
            _day = day;
            appendDate();
        }
    }

}
