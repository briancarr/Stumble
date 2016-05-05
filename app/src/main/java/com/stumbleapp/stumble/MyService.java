package com.stumbleapp.stumble;

import android.Manifest;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.firebase.geofire.GeoQuery;
import com.firebase.geofire.GeoQueryEventListener;
import com.stumbleapp.me.stumble.R;

public class MyService extends Service {

    Firebase fb = new Firebase("https://projecttest.firebaseio.com/locations");

    GeoFire geoFire = new GeoFire(fb);
    Bundle extras;
    private int mId;
    private String locationKey;

    boolean notificationDisplayed = false;

    Stream stream;
    String streamName = null;

    Firebase ref = new Firebase("https://projecttest.firebaseio.com/streams");

    public MyService() {
    }

    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {


        LocationListener locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {

                //Toast.makeText(getApplicationContext(), "Location Changed", Toast.LENGTH_LONG).show();
                //Toast.makeText(getApplicationContext(), ""+location.getLatitude(), Toast.LENGTH_LONG).show();
                GeoQuery geoQuery = geoFire.queryAtLocation(new GeoLocation(location.getLatitude(), location.getLongitude()), 0.6);

                geoQuery.addGeoQueryEventListener(new GeoQueryEventListener() {
                    @Override
                    public void onKeyEntered(String key, GeoLocation location) {
                        if(!notificationDisplayed){
                            notificationDisplayed = true;
                            //key represents the user id used to create the location
                            //pass key to notification and use to look up stream entry.
                            locationKey = key;
                            getStreamInfo();
                        }
                        Log.i("Key Entered",location.toString());
                        Log.i("Key ",key);
                    }

                    @Override
                    public void onKeyExited(String key) {
                        notificationDisplayed  = false;
                    }

                    @Override
                    public void onKeyMoved(String key, GeoLocation location) {

                    }

                    @Override
                    public void onGeoQueryReady() {

                    }

                    @Override
                    public void onGeoQueryError(FirebaseError error) {

                    }
                });

            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {
                ;
            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        };

        LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return 0;
        }
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);

        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Toast.makeText(this, "Service Destroyed", Toast.LENGTH_LONG).show();
    }


    public  void createNotification(){

        getStreamInfo();

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.ic_launcher)
                        .setAutoCancel(true)
                        .setContentTitle("Stumble Stream")
                        .setContentText(streamName);


        mBuilder.setDefaults(Notification.DEFAULT_SOUND);
        mBuilder.setVibrate(new long[]{500,500});


        //Creates an explicit intent for the streamActivity
        //Pass the URI for the stream
        Intent resultIntent = new Intent(this, PlayStreamActivity.class);
        Log.i("Service ", locationKey);
        resultIntent.putExtra("userID",locationKey);

        resultIntent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);


        // The stack builder object will contain an artificial back stack for the
        // started Activity.
        // This ensures that navigating backward from the Activity leads out of
        // your application to the Home screen.
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        // Adds the back stack for the Intent (but not the Intent itself)
        stackBuilder.addParentStack(ActiveUserActivity.class);
        // Adds the Intent that starts the Activity to the top of the stack
        stackBuilder.addNextIntent(resultIntent);

        PendingIntent resultPendingIntent =  stackBuilder.getPendingIntent( 0,PendingIntent.FLAG_UPDATE_CURRENT);

        mBuilder.setContentIntent(resultPendingIntent);

        NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        // mId allows you to update the notification later on.
        mNotificationManager.notify(mId, mBuilder.build());
    }

    private void getStreamInfo() {
        ref.child(locationKey).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                stream = snapshot.getValue(Stream.class);
                streamName = stream.getName().toString();
                createNotification();
            }
            @Override
            public void onCancelled(FirebaseError firebaseError) {
                Log.e("streams", "The read failed: " + firebaseError.getMessage());
            }
        });
    }
}
