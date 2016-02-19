package com.example.joe.locationtest;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.widget.TextView;
import android.widget.Toast;

import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;
import java.util.logging.Handler;
import java.util.logging.LogRecord;


public class MainActivity extends Activity {
    public static final int SHOW_LOCATION = 1;

    private TextView positionTextView;
    private LocationManager locationManager;
    private String provider;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        positionTextView = (TextView)findViewById(R.id.position_text_view);
        locationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
        //获得所有可用的位置提供器
        List<String> providerList = locationManager.getProviders(true);
        if(providerList.contains(LocationManager.GPS_PROVIDER)) {
            provider = LocationManager.GPS_PROVIDER;
        }else if(providerList.contains(LocationManager.NETWORK_PROVIDER)) {
            provider = LocationManager.NETWORK_PROVIDER;
        }else {
            Toast.makeText(this, "No provider to use",Toast.LENGTH_SHORT).show();
            return;
        }
        /*try {
            Location location = locationManager.getLastKnownLocation(provider);
            if (location != null) {
                showLocation(location);
            }
            locationManager.requestLocationUpdates(provider, 5000, 10, locationListener);
        }catch (SecurityException e) {
            Log.d("PermissionException", "permission not granted");
        }*/

        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED){
            Location location = locationManager.getLastKnownLocation(provider);
            if (location != null) {
                showLocation(location);
            }
            locationManager.requestLocationUpdates(provider, 5000, 10, locationListener);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
       /* try {
            if (locationManager != null) {
                locationManager.removeUpdates(locationListener);
            }
        }catch(SecurityException e) {
            Log.e("PermissionException", "permission not granted");
        }*/
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED){
            if (locationManager != null) {
                locationManager.removeUpdates(locationListener);
            }
        }
    }
    LocationListener locationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {

            showLocation(location);
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {

        }
    };

    private void showLocation(final Location location) {
        /*String currentPosition = "latitude is "+location.getLatitude()+"\n"
                +"longitude is "+location.getLongitude();
        positionTextView.setText(currentPosition);*/

        new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    //组装反向编码的接口地址
                    StringBuilder url = new StringBuilder();
                    url.append("http://maps.googleapis.com/maps/api/geocode/json?latlng=");
                    url.append(location.getLatitude()).append(",");
                    url.append(location.getLongitude());
                    url.append("&sensor=false");

                    HttpURLConnection httpURLConnection;
                    URL realUrl = new URL(url);
                    httpURLConnection = (HttpURLConnection)realUrl.openConnection();

                }
            }
        });
    }


        @Override
        public void flush() {

        }

        @Override
        public void publish(LogRecord record) {

        }
    }

    private Handler handler = new Handler() {
        @Override
        public void close() {
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case SHOW_LOCATION:
                        String currentPosition = (String)msg.obj;
                        positionTextView.setText(currentPosition);
                        break;
                    default:
                        break;
                }
            }
        }


}
