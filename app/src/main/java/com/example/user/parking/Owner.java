package com.example.user.parking;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
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

import java.util.HashMap;
import java.util.Map;

/**
 * Created by USER on 4/8/2018.
 */
public class Owner extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.owner,container,false);
       final EditText owner_name=(EditText)view.findViewById(R.id.owner_name);
       final EditText owner_phone=(EditText)view.findViewById(R.id.owner_phone);
       final EditText owner_address=(EditText)view.findViewById(R.id.owner_address);
       final EditText owner_password=(EditText)view.findViewById(R.id.owner_password);
        Button owner_save=(Button)view.findViewById(R.id.save);
        owner_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String name=owner_name.getText().toString();
                final String phone=owner_phone.getText().toString();
                final String address=owner_address.getText().toString();
                final String password=owner_password.getText().toString();

                RequestQueue queue= Volley.newRequestQueue(getActivity());
                StringRequest request=new StringRequest(Request.Method.POST, "https://shamelsheriff.000webhostapp.com/RegisterOwner.php", new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        if(response.equals("User Added Successfuly!")){
                                    Toast.makeText(getActivity(), "User Added Successfuly!", Toast.LENGTH_SHORT).show();
                                    getActivity().startActivity(new Intent(getActivity(),LoGin.class));

                        }
                        else {

                                    Toast.makeText(getActivity(), "register not done", Toast.LENGTH_SHORT).show();

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
                        params.put("address",address);
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
