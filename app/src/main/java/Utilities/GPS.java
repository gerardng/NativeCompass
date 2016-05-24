package Utilities;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

/**
 * Created by gerard on 2016-05-21.
 */
public class GPS extends Service implements LocationListener {

    private final Context context;

    // Tag for logs
    private static final String TAG = "GPS Class";


    // flag for GPS status
    boolean isGPSEnabled = false;

    // flag for GPS status
    boolean canGetLocation = false;


    Location location; // gps location

    double latitude; // latitude
    double longitude; // longitude
    int altitude; //altitude
    int accuracy;
    long time;

    // Declaring a Location Manager
    protected LocationManager locationManager;

    public GPS(Context context) {
        this.context = context;
    }

    public Location getLocation() {

        locationManager = (LocationManager) context.getSystemService(LOCATION_SERVICE);

        // if GPS_PROVIDER returns something
        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            if (location == null) {
                // GPS_PROVIDER, minimum distance between updates in meters, minimum time in milliseconds, current instance
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1, 1, this);
                Log.d(TAG, "GPS is enabled");
                // else get the last known location
                if (locationManager != null) {
                    location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                }
            }
        }
        // grab information
        if(location != null) {
            latitude = location.getLatitude();
            longitude = location.getLongitude();
            altitude = (int) location.getAltitude();
            accuracy = (int) location.getAccuracy();
            time = location.getTime();
        }
        return location;
    }

    /**
     * Resets Location;
     */
    public void resetLocation()
    {
        if(location != null) {
            location.setLatitude(0.0);
            location.setLongitude(0.0);
            location.setAltitude(0.0);
            location.setAccuracy(0);
            location.setTime(0);
        }
    }



    @Override
    public void onLocationChanged(Location location) {
    }

    @Override
    public void onProviderDisabled(String provider) {
    }

    @Override
    public void onProviderEnabled(String provider) {
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
    }

    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }

}
