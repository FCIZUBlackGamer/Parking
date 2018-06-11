package com.example.user.parking;

import android.app.ActionBar;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ViewInList extends AppCompatActivity {

    Intent intent;
    List<owner_data> markersArray;
    RecyclerView recyclerView;
    ActionBar actionBar;
    String city;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        getWindow().requestFeature( Window.FEATURE_ACTION_BAR);
        setContentView( R.layout.activity_view_in_list );
        intent = getIntent();
//        actionBar = getActionBar();
//        ColorDrawable colorDrawable = new ColorDrawable(
//                Color.parseColor("#373836"));
//        actionBar.setBackgroundDrawable(colorDrawable);
        markersArray = new ArrayList<>();
        city = intent.getStringExtra("city");
        //markersArray = (ArrayList<MarkerData>)intent.getSerializableExtra("data");
        recyclerView = (RecyclerView)findViewById( R.id.show_garage );
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        RequestQueue queue1 = Volley.newRequestQueue( ViewInList.this );
        StringRequest request1 = new StringRequest( Request.Method.POST, "https://shamelsheriff.000webhostapp.com/List_garage.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject object = new JSONObject( response );
                    JSONArray jsonArray = object.getJSONArray( "garage_data" );
                    for (int x = 0; x < jsonArray.length(); x++) {
                        JSONObject object1 = jsonArray.getJSONObject( x );

                        owner_data owner_data = new owner_data( object1.getString( "address" )
                                , object1.getString( "available_place" )
                                , object1.getString( "name" ),
                                object1.getString( "lat" ),
                                object1.getString( "lan" ));
                        markersArray.add( owner_data );

                    }
                    //adapter1 = new adapter1( list_data, Owner_Profile.this );
                    recyclerView.setAdapter( new adapter1(markersArray, new adapter1.OnItemClickListener() {
                        @Override public void onItemClick(owner_data item) {
                            Toast.makeText(ViewInList.this, "Item Clicked", Toast.LENGTH_LONG).show();
                            Intent intent=new Intent(ViewInList.this,MapsActivity.class);
                            intent.putExtra("names",item.getName());
                            intent.putExtra("address",item.getAddress());
                            intent.putExtra("position",item.getPosition());
                            intent.putExtra("lat",item.getLat());
                            intent.putExtra("lan",item.getLan());
                            startActivity(intent);
                        }
                    }) );
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        } ){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params=new HashMap<String, String>();
                params.put("city",city);
                return params;
            }
        };
        queue1.add( request1 );

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
                Intent intent = new Intent( ViewInList.this,LoGin.class );
                intent.setFlags( Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK );
                startActivity( intent );
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
