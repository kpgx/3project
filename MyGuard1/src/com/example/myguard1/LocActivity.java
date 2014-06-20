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
	private static final String TAG = LocActivity.class.getSimpleName();
	String location;
    private GoogleMap mMap;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loc);
        Bundle b = getIntent().getExtras();
	    location = b.getString("location");
	    String latNlon[]=location.split(",");
        double lat=Double.parseDouble(latNlon[1]);
        double lng=Double.parseDouble(latNlon[0]);
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
