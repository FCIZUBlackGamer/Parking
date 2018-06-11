package com.example.user.parking;

import android.Manifest;
import android.app.ActionBar;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class garage_all extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    double lat = 0;
    double lan = 0;
    ArrayList<MarkerData> markersArray;
    Button make_list;
    ActionBar actionBar;
    Spinner citys;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        getWindow().requestFeature( Window.FEATURE_ACTION_BAR);
        setContentView( R.layout.activity_garage_all );
//        actionBar = getActionBar();
//        ColorDrawable colorDrawable = new ColorDrawable(
//                Color.parseColor("#373836"));
//        actionBar.setBackgroundDrawable(colorDrawable);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById( R.id.map );
        mapFragment.getMapAsync( this );
        markersArray = new ArrayList<MarkerData>();
        make_list = (Button)findViewById( R.id.make_list );
        citys = (Spinner) findViewById(R.id.spin_city);
        make_list.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent( garage_all.this,ViewInList.class );
                intent.putExtra( "city",citys.getSelectedItem().toString() );
                Toast.makeText(garage_all.this,citys.getSelectedItem().toString(),Toast.LENGTH_SHORT).show();
                startActivity( intent );
            }
        } );

        citys.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                mMap.clear();

                RequestQueue queue = Volley.newRequestQueue( garage_all.this );
                StringRequest request = new StringRequest( Request.Method.POST, "https://shamelsheriff.000webhostapp.com/all_garage.php", new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            final JSONObject object = new JSONObject( response );
                            JSONArray jsonArray = object.getJSONArray( "garage_data" );
                            for (int x = 0; x < jsonArray.length(); x++) {
                                final JSONObject object1 = jsonArray.getJSONObject( x );
                                runOnUiThread( new Runnable() {
                                    @Override
                                    public void run() {
                                        try {
//                                        MarkerData x1 = new MarkerData( Double.parseDouble( object1.getString( "lat" ) ),
//                                                Double.parseDouble( object1.getString( "lan" ) ),
//                                                object1.getString( "name" ) );

                                            //markersArray.add( x1 );

                                            createMarker( Double.parseDouble( object1.getString( "lat" )), Double.parseDouble(object1.getString( "lan" )), object1.getString( "name" ) );

                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                } );
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }){
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        HashMap<String,String> stringStringHashMap = new HashMap<>();
                        stringStringHashMap.put("city",citys.getSelectedItem().toString());
                        return stringStringHashMap;
                    }
                };
                queue.add( request );
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        if (lat == 0 || lan == 0) {
            RequestQueue queue = Volley.newRequestQueue( garage_all.this );
            StringRequest request = new StringRequest( Request.Method.POST, "https://shamelsheriff.000webhostapp.com/all_garage.php", new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    markersArray.clear();
                    mMap.clear();
                    try {
                        final JSONObject object = new JSONObject( response );
                        JSONArray jsonArray = object.getJSONArray( "garage_data" );
                        for (int x = 0; x < jsonArray.length(); x++) {
                            final JSONObject object1 = jsonArray.getJSONObject( x );
                            runOnUiThread( new Runnable() {
                                @Override
                                public void run() {
                                    try {
//                                        MarkerData x1 = new MarkerData( Double.parseDouble( object1.getString( "lat" ) ),
//                                                Double.parseDouble( object1.getString( "lan" ) ),
//                                                object1.getString( "name" ) );

                                        //markersArray.add( x1 );

                                        createMarker( Double.parseDouble( object1.getString( "lat" )), Double.parseDouble(object1.getString( "lan" )), object1.getString( "name" ) );

                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            } );
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                }
            }){
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    HashMap<String,String> stringStringHashMap = new HashMap<>();
                    stringStringHashMap.put("city",citys.getSelectedItem().toString());
                    return stringStringHashMap;
                }
            };
            queue.add( request );
        }
    }

    protected Marker createMarker(double latitude, double longitude, String title) {

        return mMap.addMarker( new MarkerOptions()
                .position( new LatLng( latitude, longitude ) )
                .anchor( 0.5f, 0.5f )
                .title( title )
                .icon( BitmapDescriptorFactory
                        .defaultMarker( BitmapDescriptorFactory.HUE_RED ) ) );
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

        // Add a marker in Sydney and move the camera
        // LatLng sydney = new LatLng(-34, 151);
        // mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        // mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
        LocationManager manager = (LocationManager) getSystemService( LOCATION_SERVICE );
        LocationListener listener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                //code when location changed
                LatLng latLng = new LatLng( location.getLatitude(), location.getLongitude() );
                lat = location.getLatitude();
                lan = location.getLatitude();

                mMap.addMarker( new MarkerOptions().position( latLng ).title( "That's You!" )
                        .icon( BitmapDescriptorFactory
                                .defaultMarker( BitmapDescriptorFactory.HUE_CYAN ) ));
                mMap.moveCamera( CameraUpdateFactory.newLatLng( latLng ) );
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
        if (ActivityCompat.checkSelfPermission( this, Manifest.permission.ACCESS_FINE_LOCATION ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission( this, Manifest.permission.ACCESS_COARSE_LOCATION ) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            ActivityCompat.requestPermissions( this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 1 );
            return;
        } else {
            manager.requestLocationUpdates( LocationManager.NETWORK_PROVIDER, 0, 0, listener );
            mMap.setMyLocationEnabled( true );
        }
    }

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult( requestCode, permissions, grantResults );
        if (requestCode == 1) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                onMapReady( mMap );
            }
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.switch__nav, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case R.id.logout_user:
                Database database = new Database( garage_all.this );
                Intent intent = new Intent( garage_all.this,LoGin.class );
                intent.setFlags( Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK );
                database.UpdateData( "1","","" ,"");
                startActivity( intent );
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
