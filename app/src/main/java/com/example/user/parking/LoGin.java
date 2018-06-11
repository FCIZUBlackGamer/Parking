package com.example.user.parking;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.location.LocationManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoGin extends AppCompatActivity {
    EditText User_name;
    EditText Password;
    TextView new_account;
    Button login_user;
    Button login_owner;
    Database database;
    Cursor cursor;
    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lo_gin);
        User_name=(EditText)findViewById(R.id.user_name);
        Password=(EditText)findViewById(R.id.password);
        new_account=(TextView)findViewById(R.id.new_account);
        login_user=(Button)findViewById(R.id.login_user);
        login_owner=(Button)findViewById(R.id.login_owner);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Connecting To Server ... ");

        database = new Database(this);
        cursor = database.ShowData();
        while (cursor.moveToNext()){
            if (!cursor.getString(0).equals("") && cursor.getString(3).equals("user") ){
                startActivity(new Intent(LoGin.this,User_Profile.class));
            }else if (!cursor.getString(0).equals("") && cursor.getString(3).equals("owner") ){
                startActivity(new Intent(LoGin.this,Owner_Profile.class));
            }
        }

        new_account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoGin.this,Rgisteration.class));
            }
        });

        login_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String name = User_name.getText().toString();
                final String password = Password.getText().toString();
                progressDialog.show();
                    RequestQueue queue = Volley.newRequestQueue(LoGin.this);
                    StringRequest request = new StringRequest(Request.Method.POST, "https://shamelsheriff.000webhostapp.com/LoginUser.php", new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                            if (response.equals("Welcome Home!")) {
                                progressDialog.dismiss();
                                Toast.makeText(LoGin.this, "Welcome Home!", Toast.LENGTH_SHORT).show();
                                database.UpdateData("1", name, password, "user");
                                startActivity(new Intent(LoGin.this, User_Profile.class));
                            } else {
                                progressDialog.dismiss();
                                Toast.makeText(LoGin.this, "username or password Wrong", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            progressDialog.dismiss();
                            if (error instanceof ServerError)
                                Toast.makeText(LoGin.this, "Server Error", Toast.LENGTH_SHORT).show();
                            else if (error instanceof TimeoutError)
                                Toast.makeText(LoGin.this, "Connection Timed Out", Toast.LENGTH_SHORT).show();
                            else if (error instanceof NetworkError)
                                Toast.makeText(LoGin.this, "Bad Network Connection", Toast.LENGTH_SHORT).show();
                        }
                    }) {

                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            Map<String, String> params = new HashMap<String, String>();
                            params.put("name", name);

                            params.put("password", password);

                            return params;
                        }
                    };
                    queue.add(request);

            }
        });

        login_owner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String name=User_name.getText().toString();
                final String password=Password.getText().toString();
                progressDialog.show();
                RequestQueue queue= Volley.newRequestQueue(LoGin.this);
                StringRequest request=new StringRequest(Request.Method.POST, "https://shamelsheriff.000webhostapp.com/LoginOwner.php", new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        if(response.equals("Welcome Home!")){
                            progressDialog.dismiss();
                            Toast.makeText(LoGin.this, "Welcome Home!", Toast.LENGTH_SHORT).show();
                            database.UpdateData("1",name,password,"owner");
                            startActivity(new Intent(LoGin.this,Owner_Profile.class));
                        }
                        else {
                            progressDialog.dismiss();
                            Toast.makeText(LoGin.this, "username or password Wrong", Toast.LENGTH_SHORT).show();
                        }

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();
                        if (error instanceof ServerError)
                            Toast.makeText(LoGin.this, "Server Error", Toast.LENGTH_SHORT).show();
                        else if (error instanceof TimeoutError)
                            Toast.makeText(LoGin.this, "Connection Timed Out", Toast.LENGTH_SHORT).show();
                        else if (error instanceof NetworkError)
                            Toast.makeText(LoGin.this, "Bad Network Connection", Toast.LENGTH_SHORT).show();
                    }
                }) {

                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> params=new HashMap<String, String>();
                        params.put("name",name);
                        params.put("password",password);
                        return params;
            }
                };
                queue.add(request);
            }
        });
    }
}
