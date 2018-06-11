package com.example.user.parking;

import android.content.SharedPreferences;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

public class Rgisteration extends AppCompatActivity {
Spinner spinner;
    String names[]={"User","owner"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rgisteration);
        spinner=(Spinner)findViewById(R.id.spinner);
        ArrayAdapter<String> arr=new ArrayAdapter<String>(this,R.layout.spinner,R.id.text,names);
        spinner.setAdapter(arr);
        final FragmentManager manager=getSupportFragmentManager();
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //String name=names[position];


                if(position==0){
                    User user=new User();
                    FragmentTransaction transaction=manager.beginTransaction();
                    transaction.replace(R.id.frame,user);
                    transaction.commit();

                }
                else if(position==1){
                    Owner owner=new Owner();
                    FragmentTransaction ft=manager.beginTransaction();
                    ft.replace(R.id.frame,owner);
                    ft.commit();
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }
}
