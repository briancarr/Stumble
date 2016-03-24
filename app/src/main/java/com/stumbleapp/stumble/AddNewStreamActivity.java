package com.stumbleapp.stumble;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.stumbleapp.me.stumble.R;

public class AddNewStreamActivity extends AppCompatActivity {

    EditText location;
    EditText name;
    DatabaseHelper database;
    SQLiteDatabase db;
    String   mName;
    String  mLocation;

    private String baseDomain;

    // Progress Dialog
    private ProgressDialog pDialog;

    JSONParser jsonParser = new JSONParser();

    // url to create new product
    private static String url_create_stream = "http://192.168.1.14/my-site/createStream.php";

    // JSON Node names
    private static final String TAG_SUCCESS = "success";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_stream);

        baseDomain = "rtsp://192.168.1.19:1935/live/";

        database = new DatabaseHelper(this);

        location = (EditText) findViewById(R.id.location_editText);
        name = (EditText) findViewById(R.id.stream_name_editText);

        location = (EditText) findViewById(R.id.location_editText);
        name = (EditText) findViewById(R.id.stream_name_editText);

        Button startStream = (Button) findViewById(R.id.start_stream_button);
        ImageButton mapLauncher;
        mapLauncher = (ImageButton) findViewById(R.id.imageButton);
        mapLauncher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MapsActivity.class);
                startActivityForResult(intent, 1);

            }

        });

        startStream.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mName = name.getText().toString();
                mLocation = location.getText().toString();
                Log.i("before createNewProduct"," done ");
                new CreateNewProduct().execute();
                Log.i("After createNewProduct", " done ");
                //insert(getName(), getLocation(),createStreamURI(getName()));
            }
        });
    }

    //Returns the name entered into the "Stream Name" textbox
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
                location.setText(data.getStringExtra("Location"));
                Log.i("",""+data.getStringExtra("Location"));
            }
        }
    }


    public String createStreamURI(String name){
        String URI;
        URI  = baseDomain + name;
        return URI;
    }

    /**
     * Background Async Task to Create new product
     * */
    class CreateNewProduct extends AsyncTask<String, String, String> {

        /**
         * Before starting background thread Show Progress Dialog
         * */
        @Override
        protected void onPreExecute() {
            Log.i("on pre execute", " done ");
            super.onPreExecute();
            pDialog = new ProgressDialog(AddNewStreamActivity.this);
            pDialog.setMessage("Creating Product..");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        /**
         * Creating product
         * */
        protected String doInBackground(String... args) {

            // Building Parameters
            ContentValues values = new ContentValues();

            if(getName() != null && getLocation() != null) {
                /**
                 * Add values to ContentValues object
                 */
                values.put("name", getName());
                values.put("location", getLocation());
                values.put("url", createStreamURI(mName).toString());

                /**
                 * Pass variables to jsonparser for POST request.
                 * Returns String result from database server.
                 * Result gets passed to onPostExecute.
                 */
                return jsonParser.makeHttpRequest(url_create_stream, "POST", values);
            }else{
                return null;
            }
        }

        /**
         * After completing background task Dismiss the progress dialog
         * **/
        protected void onPostExecute(String result) {
            // dismiss the dialog once done
            pDialog.dismiss();
            Log.i("on post execute", result);
            //Check result and if not null launch intent
            if(result != "") {
                Intent intent = new Intent(AddNewStreamActivity.this, StreamActivity.class);
                startActivity(intent);
            }
        }

    }

}
