package com.stumbleapp.stumble;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.stumbleapp.me.stumble.R;

import java.text.BreakIterator;
import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    public Circle circle;
    private Location mLastLocation;
    public GoogleApiClient mGoogleApiClient;
    private BreakIterator mLatitudeText;
    private BreakIterator mLongitudeText;
    public Location coord;
    private LocationManager mLocationManager;
    private int MY_LOCATION_REQUEST_CODE;
    private Marker mMarker;
    private List lstLatLngs;
    private LatLng loc;
    View.OnClickListener mOnClickListener;
    private Location location;
    private static final String TAG = "MyActivity";
    private boolean clicked = false;
    LatLng latLng = null;
    String lat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mMap.setMyLocationEnabled(true);
        GoogleMap.OnMyLocationChangeListener myLocationChangeListener = new GoogleMap.OnMyLocationChangeListener() {
            @Override
            public void onMyLocationChange(Location location) {
                loc = new LatLng(location.getLatitude(), location.getLongitude());
                mMarker = mMap.addMarker(new MarkerOptions().position(loc));
                if(mMap != null){
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(loc, 16.0f));
                }
            }
        };

        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {


            @Override
            public void onMapClick(LatLng point) {
                // TODO Auto-generated method stub
                // Instantiates a new CircleOptions object and defines the center and radius

                if (!clicked) {
                    mMap.clear();
                    CircleOptions circleOptions;
                    circleOptions = new CircleOptions()
                            .center(new LatLng(point.latitude, point.longitude))
                            .fillColor(0x5500ff00)
                            .strokeColor(0x5500ff00)
                            .radius(100);// In meters

                    circle = mMap.addCircle(circleOptions);

                    latLng = new LatLng(point.latitude, point.longitude);
                    CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 17);
                    mMap.animateCamera(cameraUpdate);

                    lat = latLng.latitude + " " + latLng.longitude;
                    //checkForUser();
                    Snackbar.make(findViewById(android.R.id.content), "Save Marker", Snackbar.LENGTH_INDEFINITE)
                            .setAction("Save", mOnClickListener)
                            .setActionTextColor(Color.WHITE)
                            .show();

                    //clicked = true;
                }
            }
        });

        mOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), lat, Toast.LENGTH_LONG).show();
                //Send back latlng to addStream activity
                locationInfo();
                //mMap.clear();
            }
        };
    }

    private void locationInfo() {
        Intent intent = new Intent();
        intent.putExtra("Location", lat);
        setResult(RESULT_OK, intent);
        finish();
    }
}
