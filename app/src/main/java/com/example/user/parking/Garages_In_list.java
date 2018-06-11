package com.example.user.parking;

import android.app.ActionBar;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Garages_In_list extends AppCompatActivity {
ListView list;
    ArrayList<String> names;
    ArrayList<String> addresses;
    ArrayList<String> positions;
    ArrayList<String> lat;
    ArrayList<String> lan;
    ArrayList<String> owner_name;

    String clat = "";
    String clon = "";
    ActionBar actionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature( Window.FEATURE_ACTION_BAR);
        setContentView(R.layout.activity_garages__in_list);
        actionBar = getActionBar();
//        ColorDrawable colorDrawable = new ColorDrawable(
//                Color.parseColor("#373836"));
//        actionBar.setBackgroundDrawable(colorDrawable);
        list=(ListView)findViewById(R.id.list);
        names=new ArrayList<>();
        addresses=new ArrayList<>();
        positions=new ArrayList<>();
        lat = new ArrayList<>();
        lan = new ArrayList<>();
        owner_name = new ArrayList<>();
        Database database = new Database(this);
        Cursor cursor = database.ShowData();
        while (cursor.moveToNext()){
            clon = cursor.getString(4);
            clat = cursor.getString(5);
        }

        RequestQueue queue= Volley.newRequestQueue(Garages_In_list.this);
        StringRequest request=new StringRequest(Request.Method.POST, "https://shamelsheriff.000webhostapp.com/getGaragesList.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject object = new JSONObject(response);
                    JSONArray jsonArray = object.getJSONArray("user_data");
                    for (int x=0; x<jsonArray.length(); x++){
                        final JSONObject  object1 = jsonArray.getJSONObject(x);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                try {

                                    names.add(object1.getString("name"));
                                    addresses.add(object1.getString("address"));
                                    lat.add(object1.getString("lat"));
                                    lan.add(object1.getString("lan"));
                                    positions.add(object1.getString("available_place"));
                                    owner_name.add(object1.getString("owner_name"));

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params=new HashMap<String, String>();
                params.put("city","Zagazig");
                return params;
            }
        };
        queue.add(request);
//        adapter adapter=new adapter(this,names,addresses,positions,lat,lan,owner_name);
//        list.setAdapter(adapter);
//        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                String name=names.get(position);
//                String address=addresses.get(position);
//                String positiones=positions.get(position);
//                String lats=lat.get(position);
//                String lans=lan.get(position);
//                String ownername=owner_name.get(position);
//                Intent intent = new Intent(Garages_In_list.this,MapsActivity.class);
//                intent.putExtra("names",name);
//                intent.putExtra("address",address);
//                intent.putExtra("position",positiones);
//                intent.putExtra("owner_name",ownername);
//                startActivity(intent);
//            }
//        });
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
                Database database;
                database = new Database( Garages_In_list.this );
                Intent intent = new Intent( Garages_In_list.this,LoGin.class );
                intent.setFlags( Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK );
                database.UpdateData( "1","","" ,"");
                startActivity( intent );
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /*
    * class  get_data extends AsyncTask<Void,Void,String> {
        @Override
        protected String doInBackground(Void... params) {
            String response="";
            try {String line="";
                URL url=new URL("");
                URLConnection connection=url.openConnection();
                connection.connect();
                InputStream input=url.openStream();
                BufferedReader reader=new BufferedReader(new InputStreamReader(input));
                StringBuffer s=new StringBuffer();
                while ((line=reader.readLine())!=null){
                    s.append(line);

                }
                response=s.toString();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return response;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            try {
                JSONObject obj=new JSONObject(s);
                JSONArray a=obj.getJSONArray("");
                for(int i=0;i<a.length();i++){
                    JSONObject obj2=a.getJSONObject(i);
                    String name=obj2.getString("");
                    String address=obj2.getString("");
                    String position=obj2.getString("");

                    names.add(name);
                    addresses.add(address);
                    positions.add(position);
                }
                adapter adapter=new adapter(Garages_In_list.this,names,addresses,positions);
                list.setAdapter(adapter);
                list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        String name=names.get(position);
                        String address=addresses.get(position);
                        String positiones=positions.get(position);
                        Intent i=new Intent(Garages_In_list.this,MapsActivity.class);
                        i.putExtra("name",name);
                        i.putExtra("address",address);
                        i.putExtra("position",positiones);
                        startActivity(i);
                    }
                });
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }
    * */
}
