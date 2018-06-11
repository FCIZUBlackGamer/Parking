package com.example.user.parking;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
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

/**
 * Created by USER on 4/8/2018.
 */
public class User extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view =inflater.inflate(R.layout.user,container,false);
       final EditText user_name=(EditText)view.findViewById(R.id.user_name);
        final EditText user_phone=(EditText)view.findViewById(R.id.user_phone);
        final EditText use_password=(EditText)view.findViewById(R.id.user_password);
       Button user_save=(Button) view.findViewById(R.id.save);
        user_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String name=user_name.getText().toString();
                final String phone=user_phone.getText().toString();
                final String password=use_password.getText().toString();
                RequestQueue queue= Volley.newRequestQueue(getActivity());
                StringRequest request=new StringRequest(Request.Method.POST, "https://shamelsheriff.000webhostapp.com/RegisterUser.php", new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                            if(response.equals("User Added Successfuly!")){
                                Toast.makeText(getActivity(), "register done!", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(getActivity(),LoGin.class));
                            }
                            else {
                                Toast.makeText(getActivity(), "Something went wrong!", Toast.LENGTH_SHORT).show();
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
                        params.put("name",name);

                        params.put("phone",phone);
                        params.put("password",password);

                        return params;
                    }
                };
                queue.add(request);

            }
        });
        return view;
    }
}
