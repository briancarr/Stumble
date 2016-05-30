package com.stumbleapp.stumble;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.stumbleapp.me.stumble.R;

import java.util.Map;

//import com.firebase.client.Firebase;

public class RegisterActivity extends AppCompatActivity {
    private String _username = "";
    private String _password = "";
    private String _password_confirmation = "";
    private String _email = "";
    private EditText user;
    private EditText pass;
    private EditText pass_confirmation;
    private EditText email;
    private ProgressDialog pDialog;

    private static String url_create_stream = "http://192.168.1.14/my-site/registerUser.php";

    Firebase fb = new Firebase("https://projecttest.firebaseio.com/");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        Button register = (Button) findViewById(R.id.conferm_register_button);
        email = (EditText) findViewById(R.id.register_email_editText);
        pass = (EditText) findViewById(R.id.register_password_editText);
        pass_confirmation = (EditText) findViewById(R.id.register_password_conferm_editText);

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                _email = email.getText().toString();
                _password = pass.getText().toString();
                _password_confirmation = pass_confirmation.getText().toString();

                //Basic error check
                Log.i("pass",_password);
                Log.i("pass Confirmation", _password_confirmation);
                if (_password.equals(_password_confirmation)) {
                    //Create the user
                    registerUser(_email, _password);
                } else {
                    pass.getBackground().setColorFilter(Color.RED, PorterDuff.Mode.MULTIPLY);
                    pass_confirmation.getBackground().setColorFilter(Color.RED, PorterDuff.Mode.MULTIPLY);
                }
            }
        });

        pass.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                pass.getBackground().clearColorFilter();
                pass_confirmation.getBackground().clearColorFilter();
            }
        });

        pass_confirmation.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                pass.getBackground().clearColorFilter();
                pass_confirmation.getBackground().clearColorFilter();
            }
        });
    }

    public void dialog(){
        AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.setTitle("Register Successful");
        alertDialog.setMessage("Thank you for registering.");
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "Login",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                        startActivity(intent);
                    }
                });
        alertDialog.show();
    }
    public void errorDialog(){
        AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.setTitle("Register Unsuccessful");
        alertDialog.setMessage("There was a problem with the registration, Please Try Again Later.");
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "Close",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        alertDialog.show();
    }

    public String get_username() {
        return _username;
    }

    public String get_password() {
        return _password;
    }

    public String get_password_confirmation() {
        return _password_confirmation;
    }

    public String get_email() {
        return _email;
    }

    public void registerUser(String... params){
        fb.createUser(params[0], params[1 ], new Firebase.ValueResultHandler<Map<String, Object>>() {
            @Override
            public void onSuccess(Map<String, Object> result) {
                System.out.println("Successfully created user account with uid: " + result.get("uid"));
                dialog();
            }
            @Override
            public void onError(FirebaseError firebaseError) {
                errorDialog();
            }
        });
    }


}
