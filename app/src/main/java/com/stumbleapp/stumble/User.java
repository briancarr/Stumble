package com.stumbleapp.stumble;

//import com.firebase.client.AuthData;
//import com.firebase.client.Firebase;
//import com.firebase.client.FirebaseError;

/**
 * Created by Me on 03/01/2016.
 */
public class User {

    //static Firebase ref = new Firebase("https://projecttest.firebaseio.com");

    private boolean logged_in;

    public boolean isLogged_in() {
        return logged_in;
    }

    public void setLogged_in(boolean logged_in) {
        this.logged_in = logged_in;
    }

//    public static void login(String... params){
//        ref.authWithPassword(params[0], params[1], new Firebase.AuthResultHandler() {
//            @Override
//            public void onAuthenticated(AuthData authData) {
//                System.out.println("User ID: " + authData.getUid() + ", Provider: " + authData.getProvider());
//            }
//            @Override
//            public void onAuthenticationError(FirebaseError firebaseError) {
//                // there was an error
//            }
//        });
//
//    }
//
//    public static void register(String... params){
//        ref.createUser(params[0], params[1], new Firebase.ValueResultHandler<Map<String, Object>>() {
//            @Override
//            public void onSuccess(Map<String, Object> result) {
//                System.out.println("Successfully created user account with uid: " + result.get("uid"));
//            }
//
//            @Override
//            public void onError(FirebaseError firebaseError) {
//                // there was an error
//            }
//        });


    //}
}
