package com.example.myguard1;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class LocActivity extends FragmentActivity {
    private GoogleMap mMap;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loc);
        double lat=6.927078600000000000;
        double lng=79.861243000000060000;
        LatLng loc=new LatLng(lat, lng);
        ViewLocation(loc);
    }

    private void ViewLocation(LatLng loc) {
        if (mMap != null) {
            return;
        }
        mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map)).getMap();
        if (mMap == null) {
            return;
        }
        mMap.addMarker(new MarkerOptions().position(loc));
        CameraUpdate cu = CameraUpdateFactory.newLatLngZoom(loc, 10);
        mMap.moveCamera(cu);
    }
}
