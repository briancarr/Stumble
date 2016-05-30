package com.stumbleapp.stumble;

import android.app.DialogFragment;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import com.firebase.client.AuthData;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.stumbleapp.me.stumble.R;

public class LoginActivity extends AppCompatActivity {

    private String _username = "";
    private String _password = "";
    private EditText user;
    private EditText pass;
    private int mStackLevel;

    DialogFragment newFragment;

    GoogleCloudMessaging gcm;
    String regid;
    String PROJECT_NUMBER = "816514718419";
    private String url = "http://192.168.1.14/my-site/login.php";

    Firebase fb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
        getSupportActionBar().hide();

        Firebase.setAndroidContext(this);
        fb = new Firebase("https://projecttest.firebaseio.com/");

        Window window = this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.setStatusBarColor(this.getResources().getColor(R.color.colorPrimary));
        }

        setContentView(R.layout.activity_login);

        //getRegId();

        user = (EditText) findViewById(R.id.username_editText);
        pass = (EditText) findViewById(R.id.password_editText);

        Button login = (Button) findViewById(R.id.login_button);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                _username = user.getText().toString();
                _password = pass.getText().toString();

                fb.authWithPassword(_username, _password, new Firebase.AuthResultHandler() {
                    @Override
                    public void onAuthenticated(AuthData authData) {
                        //System.out.println("User ID: " + authData.getUid() + ", Provider: " + authData.getProvider());
                        Intent intent = new Intent(getApplicationContext(), ActiveUserActivity.class);
                        startActivity(intent);
                    }

                    @Override
                    public void onAuthenticationError(FirebaseError firebaseError) {

                    }
                });
            }
        });

        Button register = (Button) findViewById(R.id.register_button);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), RegisterActivity.class);
                startActivity(intent);
            }
        });

        startService(new Intent(this, MyService.class));


    }

//    public void getRegId(){
//        new AsyncTask<Void, Void, String>() {
//            @Override
//            protected String doInBackground(Void... params) {
//                String msg = "";
//                try {
//                    if (gcm == null) {
//                        gcm = GoogleCloudMessaging.getInstance(getApplicationContext());
//                    }
//                    regid = gcm.register(PROJECT_NUMBER);
//                    msg = "Device registered, registration ID=" + regid;
//                    Log.i("GCM",  msg);
//
//                } catch (IOException ex) {
//                    msg = "Error :" + ex.getMessage();
//
//                }
//                return msg;
//            }
//
//            @Override
//            protected void onPostExecute(String msg) {
//                //etRegId.setText(msg + "\n");
//            }
//        }.execute(null, null, null);
//    }

//    public void login(String username, String password){
//        final JSONParser login = new JSONParser();
//        final ContentValues values = new ContentValues();
//        new AsyncTask<Void, Void, String>() {
//            @Override
//            protected String doInBackground(Void... params) {
//
//                values.put("username", params[0].toString());
//                values.put("password", params[1].toString());
//                String msg = "";
//                login.makeHttpRequest(url,"POST",values);
//
//                return msg;
//            }
//
//            @Override
//            protected void onPostExecute(String msg) {
//
//            }
//        }.execute(null, null, null);
//    }
//
//    public void showDialog(){
//        mStackLevel++;
//
//        // DialogFragment.show() will take care of adding the fragment
//        // in a transaction.  We also want to remove any currently showing
//        // dialog, so make our own transaction and take care of that here.
//        FragmentTransaction ft = getFragmentManager().beginTransaction();
//        Fragment prev = getFragmentManager().findFragmentByTag("dialog");
//        if (prev != null) {
//            ft.remove(prev);
//        }
//        ft.addToBackStack(null);
//
//        // Create and show the dialog.
//        newFragment = MyDialogFragment.newInstance(mStackLevel);
//        newFragment.show(ft, "dialog");
//    }
}
