package com.googlmaptesting.kun.demo_map;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tagmanager.Container;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback , LocationListener ,GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private GoogleMap mMap;
    private AlertDialog Map_Category_Object;
    private AlertDialog Add_Marker_Object;
    private Location mylocation;
    public GoogleApiClient googleApiClient;
    private LocationRequest myLocationRequest;
    private Marker mycurrentlocation;
    private Location mLastLocation;
    private Button camera;
    private static final int CAMERA_REQUEST = 1888;
    private View mCustomMarkerView;
    private ImageView mMarkerImageView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        // check if the app has grant the user permission
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkLocationPermission();
        }
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(MapsActivity.this);

        //initiate location request
        myLocationRequest = new LocationRequest();
        myLocationRequest.setInterval(1000);
        myLocationRequest.setFastestInterval(1000);
        myLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);

        // initiate google api client
        buildGoogleApiClient();

        //build dialogs
        buildMapCategoryDialog();
        bulidAddMarkerDialog();
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
    public void onMapReady(final GoogleMap googleMap) {
        mMap = googleMap;

        mMap.setOnMyLocationButtonClickListener(new GoogleMap.OnMyLocationButtonClickListener() {
            @Override
            public boolean onMyLocationButtonClick() {
                LocationRequestUpdate();
                if(mylocation == null){
                    mylocation = mLastLocation;
                    Double latitude = mylocation.getLatitude();
                    Double Longitude = mylocation.getLongitude();
                    LatLng latLng = new LatLng(latitude,Longitude);
                    // Creating a marker
                    MarkerOptions markerOptions = new MarkerOptions();

                    // Setting the position for the marker
                    markerOptions.position(latLng);

                    mMap.addMarker(markerOptions);
                    mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
                    mMap.animateCamera(CameraUpdateFactory.zoomTo(11));
                }else {
                    Double latitude = mylocation.getLatitude();
                    Double Longitude = mylocation.getLongitude();
                    LatLng latLng = new LatLng(latitude, Longitude);
                    // Creating a marker
                    MarkerOptions markerOptions = new MarkerOptions();

                    // Setting the position for the marker
                    markerOptions.position(latLng);

                    mMap.addMarker(markerOptions);
                    mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
                    mMap.animateCamera(CameraUpdateFactory.zoomTo(11));
                }
                return true;
            }
        });



        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                LatLng latLng = marker.getPosition();
                Toast toast =  Toast.makeText(MapsActivity.this, latLng.latitude+":"+latLng.longitude+"   "+marker.getTitle(),Toast.LENGTH_LONG);
                Add_Marker_Object.show();
                toast.show();

                return true;
            }
        });

        Button MarkMyLocation = (Button)findViewById(R.id.mylocation);
        MarkMyLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mylocation == null){
                    mylocation = mLastLocation;
                    Double latitude = mylocation.getLatitude();
                    Double Longitude = mylocation.getLongitude();
                    LatLng latLng = new LatLng(latitude,Longitude);
                    // Creating a marker
                    MarkerOptions markerOptions = new MarkerOptions();

                    // Setting the position for the marker
                    markerOptions.position(latLng);

                    mMap.addMarker(markerOptions);
                    mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
                    mMap.animateCamera(CameraUpdateFactory.zoomTo(11));
                }else {
                    Double latitude = mylocation.getLatitude();
                    Double Longitude = mylocation.getLongitude();
                    LatLng latLng = new LatLng(latitude, Longitude);
                    // Creating a marker
                    MarkerOptions markerOptions = new MarkerOptions();

                    // Setting the position for the marker
                    markerOptions.position(latLng);

                    mMap.addMarker(markerOptions);
                    mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
                    mMap.animateCamera(CameraUpdateFactory.zoomTo(11));
                }


            }
        });

        camera = (Button)findViewById(R.id.btn_camera);
        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, CAMERA_REQUEST);
            }
        });

        mMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(LatLng latLng) {
                // Creating a marker
                MarkerOptions markerOptions = new MarkerOptions();

                // Setting the position for the marker
                markerOptions.position(latLng);

                // Setting the title for the marker.
                // This will be displayed on taping the marker
                //markerOptions.title(latLng.latitude + " : " + latLng.longitude);

                // Clears the previously touched position
                mMap.clear();

                // Animating to the touched position
                mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));

                // Placing a marker on the touched positio
                mMap.addMarker(markerOptions);
            }
        });

        Populate_Maps(mMap);

        //Initialize Google Play Services
        Initialize_Google_Play_Services(mMap);
    }

    //Initialize Google Play Services
    public void Initialize_Google_Play_Services(GoogleMap googleMap){
        mMap = googleMap;
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this,
                    android.Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                //buildGoogleApiClient();
                mMap.setMyLocationEnabled(true);
            }
        }else {
            //buildGoogleApiClient();
            mMap.setMyLocationEnabled(true);
        }
    }

    protected synchronized void buildGoogleApiClient() {
        googleApiClient= new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        googleApiClient.connect();
    }

    //populating the map with existing data
    public void Populate_Maps(GoogleMap googleMap){
        ArrayList<AddressObject> addressObjects = new ArrayList<>();
        AddressObject a1 = new AddressObject(49.1415,-122.8673,"1","");
        AddressObject a2 = new AddressObject(49.2573,-123.1019,"2","");
        addressObjects.add(a1);
        addressObjects.add(a2);
        AddressObject holder;
        PolylineOptions options = new PolylineOptions().width(5).color(Color.BLUE).geodesic(true);
        while(!addressObjects.isEmpty()){
            int i = addressObjects.size()-1;
            holder = addressObjects.remove(i);
            LatLng latLng = new LatLng(holder.getLatitude(),holder.getLongitude90());
            MarkerOptions markerOptions = new MarkerOptions();
            options.add(latLng);
            markerOptions.position(latLng);
            markerOptions.title(holder.getAddress());
            googleMap.addMarker(markerOptions);
        }
        googleMap.addPolyline(options);



    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
       // View marker = ((LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.customized_marker, null);

        mCustomMarkerView = ((LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.view_custom_marker, null);
        mMarkerImageView = (ImageView) mCustomMarkerView.findViewById(R.id.profile_image);


        if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK) {
            Bitmap thumbnail = (Bitmap)data.getExtras().get("data");
            LatLng newone = new LatLng(mylocation.getLatitude(),mLastLocation.getLongitude());
            MarkerOptions markerOptions1 = new MarkerOptions();
            markerOptions1.icon(BitmapDescriptorFactory.fromBitmap(getMarkerBitmapFromView(mCustomMarkerView,thumbnail)));


            markerOptions1.position(newone);
            mMap.addMarker(markerOptions1);
        }else{
            Toast.makeText(MapsActivity.this,"condition is wrong",Toast.LENGTH_SHORT).show();
        }
    }


    private Bitmap getMarkerBitmapFromView(View view, Bitmap bitmap) {
        mMarkerImageView.setImageBitmap(bitmap);
        view.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());
        view.buildDrawingCache();
        Bitmap returnedBitmap = Bitmap.createBitmap(view.getMeasuredWidth(), view.getMeasuredHeight(),
                Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(returnedBitmap);
        canvas.drawColor(Color.WHITE, PorterDuff.Mode.SRC_IN);
        Drawable drawable = view.getBackground();
        if (drawable != null)
            drawable.draw(canvas);
        view.draw(canvas);
        return returnedBitmap;
    }



    /**
     * check the user location permission
     * if the permission is not granted then sent the permission request
     */

    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;

    public boolean checkLocationPermission(){
        if (ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Asking user if explanation is needed
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    android.Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

                //Prompt the user once explanation has been shown
                ActivityCompat.requestPermissions(this,
                        new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);


            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this,
                        new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }
            return false;
        } else {
            return true;
        }
    }


    /**
     * verify the permission result and establish the google api client
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    @Override
    public void onRequestPermissionsResult(
            int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults){

        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted. Do the
                    // contacts-related task you need to do.
                    if (ContextCompat.checkSelfPermission(this,
                            android.Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {

                        if (googleApiClient == null) {
                            buildGoogleApiClient();
                        }
                        mMap.setMyLocationEnabled(true);
                    }

                } else {

                    // Permission denied, Disable the functionality that depends on this permission.
                    Toast.makeText(this, "permission denied", Toast.LENGTH_LONG).show();
                }
                return;
            }

            // other 'case' lines to check for other permissions this app might request.
            // You can add here other case statements according to your requirement.
        }
    }





    /**
     * Search botton event handle. it's used to pass the input address to mMap function and relocate the marker to intend address
     * @param view
     */
    public void ClickSearch(View view) {
        // get input text
        EditText Taget_Location = (EditText)findViewById(R.id.editText);
        String New_Location = Taget_Location.getText().toString();
        List<android.location.Address> addressList = null;
        // check the input text if its empty
        if (!New_Location.equals("") ){
            // do  location search
            Geocoder geocoder =  new Geocoder(this);
            try {
                addressList = geocoder.getFromLocationName(New_Location,3);

            } catch (IOException e) {
                e.printStackTrace();
            }

            Address address = addressList.get(0);
            LatLng latLng = new LatLng(address.getLatitude(),address.getLongitude());
            mMap.addMarker(new MarkerOptions().position(latLng).title("Marker"));
            mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));

        }
    }

    /**
     * switch map type between normal and satellite
     * @param v
     */
    public void MAPTYPE (View v){
        /**check current Map type and change to another
        if (mMap.getMapType() == GoogleMap.MAP_TYPE_NORMAL){
            mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
        }else if (mMap.getMapType() == GoogleMap.MAP_TYPE_SATELLITE){
            mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        }
         **/

        Map_Category_Object.show();

    }

    /**
     * event handler for zoom_out and zoom_in button
     * @param v
     */
    public void onZoom (View v){
        if(v.getId() == R.id.BZoom_Out){
            mMap.animateCamera(CameraUpdateFactory.zoomOut());
        }else if (v.getId() == R.id.BZoomIn){
            mMap.animateCamera(CameraUpdateFactory.zoomIn());
        }
    }

    /**
     * method to create map category dialog
     */
    private void buildMapCategoryDialog(){
        final String[] MapCategory = new String[]{"normal","Satellite","Hybrid","Terrain"};
        final AlertDialog.Builder categorybuilder = new AlertDialog.Builder(this);
        categorybuilder.setTitle("MapType");
        categorybuilder.setSingleChoiceItems(MapCategory, 0, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                Map_Category_Object.cancel();
                switch (item){
                    case 0:
                        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                        break;
                    case 1:
                        mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
                        break;
                    case 2:
                        mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
                        break;
                    case 3:
                        mMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
                        break;
                }
            }
        });
        Map_Category_Object = categorybuilder.create();
    }

    /**
     * display a alter dialog to show add marker to database
     */
    private void bulidAddMarkerDialog(){
        final AlertDialog.Builder AddMarker = new AlertDialog.Builder(this);
        AddMarker.setTitle("Add Marker");
        AddMarker.setMessage("Do you want to add maker to database");
        AddMarker.setPositiveButton("Add to DB", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // call other Activity
            }
        });
        AddMarker.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // nothing
            }
        });
        Add_Marker_Object = AddMarker.create();
    }



    @Override
    public void onLocationChanged(Location location) {
        mylocation = location;


        double lat = location.getLatitude();
        double longi =  location.getLongitude();

        if(location != null){
            Toast.makeText(MapsActivity.this,lat+":"+longi,Toast.LENGTH_LONG).show();
        }else{
            Toast.makeText(MapsActivity.this,"method is caled,no provider",Toast.LENGTH_LONG).show();
        }

        if(mycurrentlocation != null){
            mycurrentlocation.remove();
        }

            LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        /**
            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.position(latLng);
            markerOptions.title("my current location");
            mycurrentlocation = mMap.addMarker(markerOptions);
         **/
            mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
            mMap.animateCamera(CameraUpdateFactory.zoomTo(11));


        if (googleApiClient != null) {
            LocationServices.FusedLocationApi.removeLocationUpdates(googleApiClient, this);
        }
    }


    @Override
    public void onConnected(@Nullable Bundle bundle) {
        LocationRequestUpdate();
    }

    //update the location request
    public void LocationRequestUpdate(){
        if (ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, myLocationRequest, this);
            mLastLocation = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
        }
    }

    @Override
    public void onConnectionSuspended(int i) {}

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {}

    @Override
    protected void onResume() {
        super.onResume();
        if(googleApiClient.isConnected()){
            LocationRequestUpdate();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        LocationServices.FusedLocationApi.removeLocationUpdates(googleApiClient, this);
    }

    @Override
    protected void onStart(){
        super.onStart();
        googleApiClient.connect();
    }

    @Override
    protected void onStop(){
        super.onStop();
        googleApiClient.disconnect();
    }

}
