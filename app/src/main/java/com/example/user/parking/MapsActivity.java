package com.example.user.parking;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {
    TextView name;
    TextView address;
    TextView position;

    public static final int REQUEST_GOOGLE_PLAY_SERVICES = 1972;

    Button increment, decrement;

    private GoogleMap mMap;
    String name1;
    String adderss1;

    double lat, lan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        if (savedInstanceState == null) {
            startRegistrationService();
        }

        increment = (Button) findViewById(R.id.increament);
        decrement = (Button) findViewById(R.id.decreament);

        name = (TextView) findViewById(R.id.name);
        address = (TextView) findViewById(R.id.address);
        position = (TextView) findViewById(R.id.position);
        //bundle from Garags in list

        Bundle bundle = getIntent().getExtras();

        name1 = bundle.getString("names");
        adderss1 = bundle.getString("address");
        lan = Double.parseDouble(bundle.getString("lan"));
        lat = Double.parseDouble(bundle.getString("lat"));
        final String position1 = bundle.getString("position");
        name.setText(name1);
        address.setText(adderss1);
        position.setText(position1);
        increment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RequestQueue queue = Volley.newRequestQueue(MapsActivity.this);
                StringRequest request = new StringRequest(Request.Method.POST, "https://shamelsheriff.000webhostapp.com/IncrementAvaliblePlaces.php", new Response.Listener<String>() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onResponse(String response) {

                        Toast.makeText(MapsActivity.this, "Place Increased by 1!", Toast.LENGTH_LONG).show();

                        position.setText("" + (Integer.parseInt(position.getText().toString()) + 1));

                        Log.e("TAAAAAAG", response);

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(MapsActivity.this, error.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }) {
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> params = new HashMap<String, String>();
                        params.put("place_name", name1);
                        params.put("place_address", adderss1);
                        return params;
                    }
                };
                queue.add(request);
            }
        });
        decrement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Integer.parseInt(position.getText().toString()) > 0) {
                    Toast.makeText(MapsActivity.this, name1 + "  " + adderss1 + "    " + position1, Toast.LENGTH_LONG).show();
                    RequestQueue queue = Volley.newRequestQueue(MapsActivity.this);
                    StringRequest request = new StringRequest(Request.Method.POST, "https://shamelsheriff.000webhostapp.com/DecrementPlace.php", new Response.Listener<String>() {
                        @SuppressLint("SetTextI18n")
                        @Override
                        public void onResponse(String response) {

                            Toast.makeText(MapsActivity.this, "Place Decreased by 1!", Toast.LENGTH_LONG).show();

                            position.setText("" + (Integer.parseInt(position.getText().toString()) - 1));

                            Log.e("TAAAAAAG", response);
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(final VolleyError error) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(MapsActivity.this, error.getMessage(), Toast.LENGTH_LONG).show();
                                }
                            });
                        }
                    }) {
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            Map<String, String> params = new HashMap<String, String>();
                            params.put("place_name", name1);
                            params.put("place_address", adderss1);
                            return params;
                        }
                    };
                    queue.add(request);
                }
            }
        });
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        //GooglePlayServicesUtil.isGooglePlayServicesAvailable( getApplicationContext() );
        GoogleApiAvailability googleAPI = GoogleApiAvailability.getInstance();
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


        mapFragment.getMapAsync(this);
    }

    private void startRegistrationService() {
        GoogleApiAvailability api = GoogleApiAvailability.getInstance();
        int code = api.isGooglePlayServicesAvailable(this);
        if (code == ConnectionResult.SUCCESS) {
            onActivityResult(REQUEST_GOOGLE_PLAY_SERVICES, Activity.RESULT_OK, null);
        } else if (api.isUserResolvableError(code) &&
                api.showErrorDialogFragment(this, code, REQUEST_GOOGLE_PLAY_SERVICES)) {
            // wait for onActivityResult call (see below)
        } else {
            Toast.makeText(this, api.getErrorString(code), Toast.LENGTH_LONG).show();
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
        mMap.setMapType( GoogleMap.MAP_TYPE_NORMAL );
//        mMap.addMarker( new MarkerOptions()
//                .position( new LatLng( lat, lan ) )
//                .title( name1 )
//                .draggable( true )
//                .snippet( adderss1 )
//                .icon( BitmapDescriptorFactory
//                        .defaultMarker( BitmapDescriptorFactory.HUE_BLUE ) ) );
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lat, lan), 12));
        // Add a marker in Sydney and move the camera
        // LatLng sydney = new LatLng(-34, 151);
        // mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        // mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
        LocationManager manager = (LocationManager) getSystemService( LOCATION_SERVICE );
        LocationListener listener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                //code when location changed
                LatLng latLng = new LatLng( lat, lan );

                mMap.addMarker( new MarkerOptions().position( latLng ).title( "Marker in user location" ) );
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

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult( requestCode, permissions, grantResults );
        if (requestCode == 1) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                onMapReady( mMap );
            }
        }
    }


}
