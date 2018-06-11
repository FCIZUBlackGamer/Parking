package com.example.user.parking;

import android.Manifest;
import android.app.ActionBar;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import net.gotev.uploadservice.MultipartUploadRequest;
import net.gotev.uploadservice.UploadNotificationConfig;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class Owner_Profile extends AppCompatActivity {
    ImageView owner_image;

    TextView owner_name, owner_phone, owner_address;
    RecyclerView list;
    Button add_newgarage;
    Database database;
    Cursor cursor;
    String name;
    List<owner_data> list_data;

    private int PICK_IMAGE_REQUEST = 1;

    //storage permission code
    private static final int STORAGE_PERMISSION_CODE = 123;

    //Bitmap to get image from gallery
    private Bitmap bitmap;

    //Uri to store the image uri
    private Uri filePath;
    ActionBar actionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        getWindow().requestFeature( Window.FEATURE_ACTION_BAR);
        setContentView( R.layout.activity_owner__profile );

        actionBar = getActionBar();
//        ColorDrawable colorDrawable = new ColorDrawable(
//                Color.parseColor("#373836"));
//        actionBar.setBackgroundDrawable(colorDrawable);

        owner_image = (ImageView) findViewById( R.id.owner_image );
        owner_name = (TextView) findViewById( R.id.owner_name );
        owner_phone = (TextView) findViewById( R.id.owner_phone );
        owner_address = (TextView) findViewById( R.id.owner_address );
        list = (RecyclerView) findViewById( R.id.list );
        list.setHasFixedSize(true);
        list.setLayoutManager(new LinearLayoutManager(this));
        add_newgarage = (Button) findViewById( R.id.add_garage );

        add_newgarage.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity( new Intent( Owner_Profile.this, Garage_Sign.class ) );
            }
        } );
        list_data = new ArrayList<>();
        database = new Database( this );
        cursor = database.ShowData();
        while (cursor.moveToNext()) {
            name = cursor.getString( 1 );
        }

        owner_image.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestStoragePermission();
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
            }
        } );

        RequestQueue queue = Volley.newRequestQueue( Owner_Profile.this );
        StringRequest request = new StringRequest( Request.Method.POST, "https://shamelsheriff.000webhostapp.com/OwnerProfile.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject object = new JSONObject( response );
                    JSONArray jsonArray = object.getJSONArray( "owner_data" );
                    for (int x = 0; x < jsonArray.length(); x++) {
                        final JSONObject object1 = jsonArray.getJSONObject( x );
                        runOnUiThread( new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    owner_name.setText( object1.getString( "name" ) );
                                    owner_phone.setText( object1.getString( "phone" ) );
                                    Picasso.with( Owner_Profile.this )
                                            .load( object1.getString( "img_url" ) )
                                            .into( owner_image );
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
        } ) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put( "name", name );
                return params;
            }
        };
        queue.add( request );

        RequestQueue queue1 = Volley.newRequestQueue( Owner_Profile.this );
        StringRequest request1 = new StringRequest( Request.Method.POST, "https://shamelsheriff.000webhostapp.com/Ownergarage.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject object = new JSONObject( response );
                    JSONArray jsonArray = object.getJSONArray( "owner_data" );
                    for (int x = 0; x < jsonArray.length(); x++) {
                        JSONObject object1 = jsonArray.getJSONObject( x );

                        owner_data owner_data = new owner_data( object1.getString( "address" )
                                , object1.getString( "available_place" )
                                , object1.getString( "name" ),
                                object1.getString( "lat" ),
                                object1.getString( "lan" ));
                        list_data.add( owner_data );

                    }
                    //adapter1 = new adapter1( list_data, Owner_Profile.this );
                    list.setAdapter( new adapter1(list_data, new adapter1.OnItemClickListener() {
                        @Override public void onItemClick(owner_data item) {
                            Toast.makeText(Owner_Profile.this, "Item Clicked", Toast.LENGTH_LONG).show();
                            Intent intent=new Intent(Owner_Profile.this,MapsActivity.class);
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
        } ) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put( "name", name );
                return params;
            }
        };
        queue1.add( request1 );

    }


    //Requesting permission
    private void requestStoragePermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
            return;

        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
            //If the user has denied the permission previously your code will come to this block
            //Here you can explain why you need this permission
            //Explain here why you need this permission
        }
        //And finally ask for the permission
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
    }

    //This method will be called when the user will tap on allow or deny
    @Override
    public void onRequestPermissionsResult(int requestCode,String[] permissions, int[] grantResults) {

        //Checking the request code of our request
        if (requestCode == STORAGE_PERMISSION_CODE) {

            //If permission is granted
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //Displaying a toast
                Toast.makeText(this, "Permission granted now you can read the storage", Toast.LENGTH_LONG).show();
            } else {
                //Displaying another toast if permission is not granted
                Toast.makeText(this, "Oops you just denied the permission", Toast.LENGTH_LONG).show();
            }
        }
    }


    //handling the image chooser activity result
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            filePath = data.getData();

            AlertDialog.Builder builder = new AlertDialog.Builder( Owner_Profile.this );
            builder.setTitle( "Are You Sure To Put This Image Your Profile Image?!" )
                    .setPositiveButton( "Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            try {
                                String path = getPath( filePath );
                                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                                owner_image.setImageBitmap(bitmap);
                                String uploadId = UUID.randomUUID().toString();
                                UploadNotificationConfig config = new UploadNotificationConfig();
                                config.setCompletedMessage( "Check Login!" );
                                config.setErrorMessage( "Something Went Wrong! ... please tyr again later" );
                                //Creating a multi part request
                                new MultipartUploadRequest( Owner_Profile.this, uploadId, "https://shamelsheriff.000webhostapp.com/UpdateOwnerProfilePic.php" )
                                        .addFileToUpload( path, "image" ) //Adding file
                                        .addParameter( "user_name", name ) //Adding text parameter to the request
                                        .setNotificationConfig( new UploadNotificationConfig().setCompletedMessage( "" ) )
                                        .setMaxRetries( 2 )

                                        .startUpload(); //Starting the upload
                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                        }
                    } )
                    .setNegativeButton( "No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    } ).setIcon( android.R.drawable.ic_dialog_alert )
                    .show();

        }
    }

    //method to get the file path from uri
    public String getPath(Uri uri) {
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        String document_id = cursor.getString(0);
        document_id = document_id.substring(document_id.lastIndexOf(":") + 1);
        cursor.close();

        cursor = getContentResolver().query(
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                null, MediaStore.Images.Media._ID + " = ? ", new String[]{document_id}, null);
        cursor.moveToFirst();
        String path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
        cursor.close();

        return path;
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
                Intent intent = new Intent( Owner_Profile.this,LoGin.class );
                intent.setFlags( Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK );
                database.UpdateData( "1","","" ,"");
                startActivity( intent );
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
