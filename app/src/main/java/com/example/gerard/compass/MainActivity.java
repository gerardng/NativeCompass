package com.example.gerard.compass;

import android.content.Intent;
import android.hardware.GeomagneticField;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.GoogleMap;

import java.util.Date;

import Utilities.GPS;

public class MainActivity extends AppCompatActivity implements SensorEventListener {

    // define the display assembly compass picture
    private ImageView image;

    // record the compass picture angle turned
    private float currentAzimuth = 0f;

    // alpha variable to use in low pass filtering of the compass
    public static final float ALPHA = 0.2f;

    // compass sensor managers
    private SensorManager compassSensorManager;
    private Sensor accelerometer;
    private Sensor magnetometer;

    // variables to hold orientation
    float azimut;
    float pitch;
    float roll;

    // variable to hold altitude
    private float altitude = 0;

    private float declination = 0;

    // variables to hold TextViews
    TextView degreeLabel;
    TextView pitchLabel;
    TextView rollLabel;
    TextView altitudeLabel;
    TextView speedLabel;
    TextView gpsLabel;
    TextView declinationLabel;
    TextView dateTimeLabel;
    TextView sunriseLabel;
    TextView sunsetLabel;
    TextView latlng;
    TextView txtUTM;
    TextView gpsUpdateInterval;
    Location location;
    Date date;

    float[] accelerometerData = new float[3]; // accelerometer
    float[] magnetometerData = new float[3]; // magnetometer
    float[] rMat = new float[9];
    float[] iMat = new float[9];
    float orientation[] = new float[3];
    private int mAzimuth = 0; // degree

    GPS gps;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        gps = new GPS(MainActivity.this);


        // our compass image
        image = (ImageView) findViewById(R.id.imageViewCompass);

        // Initialize all the labels
        degreeLabel = (TextView) findViewById(R.id.degreeLabel);
        pitchLabel = (TextView) findViewById(R.id.pitchLabel);
        rollLabel = (TextView) findViewById(R.id.rollLabel);


        // initialize android device sensor for compass
        compassSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        accelerometer = compassSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        magnetometer = compassSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);

    }


    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();

        // for the system's orientation sensor registered listeners
        compassSensorManager.registerListener((SensorEventListener) this, compassSensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION),
                SensorManager.SENSOR_DELAY_NORMAL);
        compassSensorManager.registerListener((SensorEventListener) this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
        compassSensorManager.registerListener((SensorEventListener) this, magnetometer, SensorManager.SENSOR_DELAY_NORMAL);

    }

    @Override
    protected void onPause() {
        super.onPause();

        // to stop the listener/s and save battery
        compassSensorManager.unregisterListener(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        switch ( event.sensor.getType() ) {
            case Sensor.TYPE_ACCELEROMETER:
                accelerometerData = lowPass( event.values.clone(), accelerometerData );
                break;
            case Sensor.TYPE_MAGNETIC_FIELD:
                magnetometerData = event.values.clone();
                break;
            default: return;
        }

        if ( SensorManager.getRotationMatrix( rMat, iMat, accelerometerData, magnetometerData) ) {
            mAzimuth = (int) ( Math.toDegrees( SensorManager.getOrientation( rMat, orientation )[0] ) + 360 ) % 360;
            mAzimuth += declination;
            if (mAzimuth >= 360) {
                mAzimuth -= 360;
            }
        }

        // get the angle around the z-axis rotated
        degreeLabel.setText("Heading\n" + Integer.toString(mAzimuth) + " (Deg T)");

        // create a rotation animation (reverse turn degree degrees)
        RotateAnimation ra = new RotateAnimation(
                -mAzimuth,
                -mAzimuth,
                Animation.RELATIVE_TO_SELF,
                0.5f,
                Animation.RELATIVE_TO_SELF,
                0.5f);

        // how long the animation will take place
        ra.setDuration(210);

        // set the animation after the end of the reservation status
        ra.setFillAfter(true);

        // Start the animation
        image.startAnimation(ra);

        //currentDegree = -degree;
        currentAzimuth = -mAzimuth + declination;

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // nothing to do
    }

    /**
     * Low pass filter to smoothen out the compass
     * @param input
     * @param output
     * @return
     */
    protected float[] lowPass( float[] input, float[] output ) {
        if ( output == null ) return input;

        for ( int i=0; i<input.length; i++ ) {
            output[i] = output[i] + ALPHA * (input[i] - output[i]);
        }
        return output;
    }

    private float getDeclination(Location location)
    {
        GeomagneticField geomagneticField = new GeomagneticField(
                Double.valueOf(location.getLatitude()).floatValue(),
                Double.valueOf(location.getLongitude()).floatValue(),
                Double.valueOf(location.getAltitude()).floatValue(),
                System.currentTimeMillis());
        declination = geomagneticField.getDeclination();
        return geomagneticField.getDeclination();
    }

    /**
     * Navigation method for about button
     * @param view
     */
    public void toAbout(View view) {
        Intent intent = new Intent(this, AboutActivity.class);
        startActivity(intent);
    }

    /**
     * Navigation method for map button
     * @param view
     */
    public void toMap(View view) {
        Intent intent = new Intent(this, MapActivity.class);
        startActivity(intent);
    }
}
