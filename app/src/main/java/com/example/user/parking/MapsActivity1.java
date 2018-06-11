package com.example.user.parking;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMarkerDragListener;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsActivity1 extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    Button button;
    Double lat = 0d;
    Double lon = 0d;
    String name, address, place;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps1);
        intent = getIntent();
        name = intent.getStringExtra("name");
        place = intent.getStringExtra("place");
        address = intent.getStringExtra("address");
        button = (Button) findViewById(R.id.done);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (lat != 0 && lon != 0) {
                    Intent intent = new Intent(MapsActivity1.this, Garage_Sign.class);
                    intent.putExtra("lat", lat + "");
                    intent.putExtra("lon", lon + "");
                    intent.putExtra("name", name);
                    intent.putExtra("place", place);
                    intent.putExtra("address", address);
                    startActivity(intent);
                }
            }
        });
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        GooglePlayServicesUtil
                .isGooglePlayServicesAvailable(getApplicationContext());
        ((SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map)).getMapAsync(this);
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

    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
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
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        mMap.addMarker(new MarkerOptions()
                .position(new LatLng(0, 0))
                .title("Marker")
                .draggable(true)
                .snippet("Hello")
                .icon(BitmapDescriptorFactory
                        .defaultMarker(BitmapDescriptorFactory.HUE_YELLOW)));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(0, 0), 12));
        // Add a marker in Sydney and move the camera
        // LatLng sydney = new LatLng(-34, 151);
        // mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        // mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));

        mMap.setOnMarkerDragListener(new OnMarkerDragListener() {

            @Override
            public void onMarkerDragStart(Marker marker) {
                // TODO Auto-generated method stub
                // Here your code
                Toast.makeText(MapsActivity1.this, "Dragging Start",
                        Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onMarkerDragEnd(Marker marker) {
                // TODO Auto-generated method stub
                LatLng latLng = marker.getPosition();
                lat = latLng.latitude;
                lon = latLng.longitude;
                Toast.makeText(
                        MapsActivity1.this,
                        "Lat " + latLng.latitude + " "
                                + "Long " + latLng.longitude,
                        Toast.LENGTH_LONG).show();
            }

            @Override
            public void onMarkerDrag(Marker marker) {
                // TODO Auto-generated method stub
                // Toast.makeText(MainActivity.this, "Dragging",
                // Toast.LENGTH_SHORT).show();
                System.out.println("Draagging");
            }
        });

        LocationManager manager = (LocationManager) getSystemService(LOCATION_SERVICE);
        LocationListener listener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                //code when location changed
                LatLng latLng=new LatLng(location.getLatitude(),location.getLongitude());
                lat=location.getLatitude();
                lon=location.getLatitude();

                mMap.addMarker(new MarkerOptions().position(latLng).title("Marker in user location"));
                mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
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
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION},1);
            return;
        }
        else {
            manager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, listener);
            mMap.setMyLocationEnabled(true);
        }

    }

    public boolean onMarkerClick(final Marker marker) {

        if (marker.equals(mMap)) {
            // handle click here
            // map.getMyLocation();
            System.out.println("Clicked");
            LatLng position = marker.getPosition(); //
            Toast.makeText(
                    MapsActivity1.this,
                    "Lat " + position.latitude + " "
                            + "Long " + position.longitude,
                    Toast.LENGTH_LONG).show();
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode==1){
            if(grantResults.length>0&&grantResults[0]==PackageManager.PERMISSION_GRANTED&&grantResults[1]==PackageManager.PERMISSION_GRANTED){
                onMapReady(mMap);
            }
        }
    }


}

