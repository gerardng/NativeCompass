package com.example.gerard.compass;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;

/**
 * Created by gerard on 2016-05-21.
 */
public class MapActivity extends FragmentActivity {

    GoogleMap map;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        if(initMap()) {
            Toast.makeText(this, "Ready to map!", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Map not available", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean initMap() {
        if(map == null) {
            SupportMapFragment mapFrag = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
            map = mapFrag.getMap();
        }

        return(map != null);
    }
}
