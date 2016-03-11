package com.stumbleapp.stumble;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.firebase.client.Firebase;
import com.stumbleapp.me.stumble.R;

public class RegisterActivity extends AppCompatActivity {
    private String _username = "";
    private String _password = "";
    private String _password_confirmation = "";
    private String _email = "";
    private EditText user;
    private EditText pass;
    private EditText pass_confirmation;
    private EditText email;
    static Context baseContext;
    Firebase myFirebaseRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        //Reference to firebase database
        myFirebaseRef = new Firebase("https://projecttest.firebaseio.com/");

        Button register = (Button) findViewById(R.id.conferm_register_button);



        user = (EditText) findViewById(R.id.register_username_editText);
        pass = (EditText) findViewById(R.id.register_password_editText);
        pass_confirmation = (EditText) findViewById(R.id.register_password_conferm_editText);
        email = (EditText) findViewById(R.id.email_editText);

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                _username = user.getText().toString();
                _password = pass.getText().toString();
                _password_confirmation = pass_confirmation.getText().toString();
                _email = email.getText().toString();

                if(_password == _password_confirmation) {
                    //Create the user
                    User.register(_username, _password, _email);
                }else{
                    pass.setBackground(Drawable.createFromPath("@drawable/rounded_corner_error"));
                }
                dialog();


            }
        });
    }

    public void dialog(){
        AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.setTitle("Register Successfull");
        alertDialog.setMessage("Thank you for registeringn.");
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
}
