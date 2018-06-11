package com.example.user.parking;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.PersistableBundle;
import android.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Garage_Sign extends AppCompatActivity {
    EditText Garage_name;
    EditText Garage_address;
    EditText Garage_places;
    Spinner Garage_city;
    TextView Garage_location;
    Button save;
    Intent intent;
    String lat = null;
    String lon = null;
    String name,places, address, city;
    String owner_name;
    Database database;
    Cursor cursor;
    ActionBar actionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        intent = getIntent();

        setContentView(R.layout.activity_garage__sign);
        actionBar = getActionBar();
//        ColorDrawable colorDrawable = new ColorDrawable(
//                Color.parseColor("#373836"));
//        actionBar.setBackgroundDrawable(colorDrawable);
        Garage_name=(EditText)findViewById(R.id.garage_name) ;
        Garage_places=(EditText)findViewById(R.id.garage_palces);
        Garage_address=(EditText)findViewById(R.id.garage_adderss);
        Garage_city=(Spinner) findViewById(R.id.spin_city);
        Garage_location=(TextView) findViewById(R.id.garage_location);
        save=(Button)findViewById(R.id.save);

        database = new Database( this );
        cursor = database.ShowData();
        while (cursor.moveToNext()){
            owner_name = cursor.getString( 1 );
        }

        if (intent.getStringExtra( "lat" ) != null){
            name = intent.getStringExtra( "name");
            places = intent.getStringExtra( "place");
            address = intent.getStringExtra( "address");
            Garage_name.setText( name );
            Garage_address.setText( address );
            Garage_places.setText( places );
            lat = intent.getStringExtra("lat");
            lon = intent.getStringExtra("lon");
            Toast.makeText( this,"Lat: "+lat+"\nLon: "+lon,Toast.LENGTH_LONG ).show();

        }

        if (savedInstanceState != null){
            name = savedInstanceState.getString( "name");
            places = savedInstanceState.getString( "place");
            address = savedInstanceState.getString( "address");
            Garage_name.setText( name );
            Garage_address.setText( address );
            Garage_places.setText( places );
        }
        Garage_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name = Garage_name.getText().toString();
                places = Garage_places.getText().toString();
                address = Garage_address.getText().toString();
                Intent intent = new Intent(Garage_Sign.this,MapsActivity1.class);
                intent.putExtra( "name",name );
                intent.putExtra( "place",places );
                intent.putExtra( "address",address );
                startActivity( intent );
            }
        });



        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name=Garage_name.getText().toString();

                places=Garage_places.getText().toString();
                address=Garage_address.getText().toString();
                city = Garage_city.getSelectedItem().toString();


                if (lat!=null && lon!=null) {
                    RequestQueue queue = Volley.newRequestQueue(Garage_Sign.this);
                    StringRequest request = new StringRequest(Request.Method.POST, "https://shamelsheriff.000webhostapp.com/InsertGarage.php", new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                            Toast.makeText(Garage_Sign.this, response, Toast.LENGTH_SHORT).show();

                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {

                        }
                    }) {

                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            Map<String, String> params = new HashMap<String, String>();
                            params.put("name", name);
                            params.put("address", address);
                            params.put("available_places", places);
                            params.put("lan", lon);
                            params.put("owner_name", owner_name);
                            params.put("lat", lat);
                            params.put("city", city);
                            return params;
                        }
                    };
                    queue.add(request);
                }
                Intent intent=new Intent(Garage_Sign.this,Owner_Profile.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState( outState, outPersistentState );
        outState.putString( "name",name );
        outState.putString( "place",places );
        outState.putString( "address",address );
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState( savedInstanceState );
        name = savedInstanceState.getString( "name");
        places = savedInstanceState.getString( "place");
        address = savedInstanceState.getString( "address");
        Garage_name.setText( name );
        Garage_address.setText( address );
        Garage_places.setText( places );
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
                Intent intent = new Intent( Garage_Sign.this,LoGin.class );
                intent.setFlags( Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK );
                database.UpdateData( "1","","" ,"");
                startActivity( intent );
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
