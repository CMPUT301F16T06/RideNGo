package assignment1.ridengo;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MapsRiderActivity extends FragmentActivity implements OnMapReadyCallback //GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {
{
    private static final String TAG = "MapsActivity";
    private int menuLocationOption = 0;

    private GoogleMap mMap;
    private ArrayList<LatLng> startAndEndPoints;
    private ArrayList<LatLng> startAndEndPointsSearcher;
    private Geocoder geocoder;
    private List<Address> addresses = null;
    private List<Address> startSearchAddressReturn = null;
    private List<Address> endSearchAddressReturn = null;
    private ArrayList<String> addressesToReturn;
    private float[] distanceResult = new float[1];
    private Marker startMarker;
    private Marker endMarker;
    private String fromLocationName = null;
    private String toLocationName = null;
    //private double latitudePoint = 0;
    //private double longitudePoint = 0;
//    private GoogleApiClient mGoogleApiClient;
//    private LocationRequest mLocationRequest;
//    public static final String TAG = MapsActivity.class.getSimpleName();
    //private static final int LOCATION_REQUEST_CODE = 101;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps_rider);
        startAndEndPoints = new ArrayList<LatLng>();
        addressesToReturn = new ArrayList<String>();
        geocoder = new Geocoder(this, Locale.getDefault());
        final Button optionButton = (Button) findViewById(R.id.locationSearcher);
        Button doneButton = (Button) findViewById(R.id.doneButton);

        // https://developers.google.com/places/android-api/autocomplete
        PlaceAutocompleteFragment autocompleteFragment = (PlaceAutocompleteFragment)
                getFragmentManager().findFragmentById(R.id.place_autocomplete_fragment);

        PlaceAutocompleteFragment autocompleteFragmentTo = (PlaceAutocompleteFragment)
                getFragmentManager().findFragmentById(R.id.place_autocomplete_fragment_from);

        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                // TODO: Get info about the selected place.
                if (menuLocationOption == 2) {
                    //Toast.makeText(getBaseContext(), "Past habit completion deleted." + place.getName(), Toast.LENGTH_SHORT).show();
                    if (startAndEndPoints.size() >= 2) {
                        mMap.clear();
                        startAndEndPoints.clear();
                        addressesToReturn.clear();
                        startSearchAddressReturn = null;
                        fromLocationName = null;
                    }

                    if (startAndEndPoints.size() <= 1) {
                        try {
                            startSearchAddressReturn = geocoder.getFromLocationName(place.getName().toString(), 1);
                            LatLng fromLocation = place.getLatLng();
                            fromLocationName = place.getName().toString();
                            startAndEndPoints.add(fromLocation);


                            if (startMarker == null) {
                                startMarker = mMap.addMarker(new MarkerOptions().position(fromLocation).title("Start Location"));
                            } else {
                                startMarker.remove();
                                startMarker = mMap.addMarker(new MarkerOptions().position(fromLocation).title("Start Location"));
                                startAndEndPoints.clear();
                            }
                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(fromLocation, 15));

                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        //startSearchAddressReturn = geocoder.getFromLocationName(place.getName().toString(),1);
                        Log.i(TAG, "Place: " + place.getName());
                    }
                }
            }

            @Override
            public void onError(Status status) {
                // TODO: Handle the error.
                Toast.makeText(getBaseContext(),"Error getting location",Toast.LENGTH_SHORT).show();
                Log.i(TAG, "An error occurred: " + status);
            }
        });

        autocompleteFragmentTo.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                if (menuLocationOption == 2) {
                    // TODO: Get info about the selected place.
                    //Toast.makeText(getBaseContext(), "Past habit completion deleted." + place.getName(), Toast.LENGTH_SHORT).show();
                    if (startAndEndPoints.size() >= 2) {
                        mMap.clear();
                        startAndEndPoints.clear();
                        addressesToReturn.clear();
                        endSearchAddressReturn = null;
                        toLocationName = null;
                    }
                    if (startMarker != null) {
                        try {
                            endSearchAddressReturn = geocoder.getFromLocationName(place.getName().toString(), 1);
                            LatLng toLocation = place.getLatLng();
                            toLocationName = place.getName().toString();
                            startAndEndPoints.add(toLocation);
                            if (endMarker == null) {
                                endMarker = mMap.addMarker(new MarkerOptions().position(toLocation).title("End Location"));
                            } else {
                                endMarker.remove();
                                endMarker = mMap.addMarker(new MarkerOptions().position(toLocation).title("End Location"));
                                startAndEndPoints.remove(endMarker);
                            }
                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(toLocation, 15));
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        //startSearchAddressReturn = geocoder.getFromLocationName(place.getName().toString(),1);
                        Log.i(TAG, "Place: " + place.getName());

                    }
                }
            }

            @Override
            public void onError(Status status) {
                // TODO: Handle the error.
                Toast.makeText(getBaseContext(),"Error getting location",Toast.LENGTH_SHORT).show();
                Log.i(TAG, "An error occurred: " + status);
            }
        });

        doneButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                // Add things to save the start and end location data. Doing this for now as
                // I don't know how to save to elastic search yet.
                ArrayList<String> searchedReturnAddresses = new ArrayList<String>();
                Intent intent = new Intent(MapsRiderActivity.this, RiderPostRequestActivity.class);
                Bundle extras = new Bundle();
                if (addressesToReturn.size() == 2){
                    extras.putStringArrayList("ARRAY_LIST_ADDRESS_MARKER", addressesToReturn);
                }

                if (startSearchAddressReturn != null && endSearchAddressReturn != null){
                    searchedReturnAddresses.add(startSearchAddressReturn.get(0).toString());
                    searchedReturnAddresses.add(endSearchAddressReturn.get(0).toString());
                    extras.putStringArrayList("ARRAY_LIST_ADDRESS_SEARCHED", searchedReturnAddresses);
                    extras.putString("FROM_LOCATION", fromLocationName);
                    extras.putString("TO_LOCATION", toLocationName);
                }
                extras.putFloat("DISTANCE_FROM_POINTS",distanceResult[0]);
                intent.putExtras(extras);
                setResult(RESULT_OK,intent);
                finish();
                //startActivity(intent);
            }
        });

        optionButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                registerForContextMenu(optionButton);
                openContextMenu(optionButton);

            }
        });


//        locationSearch.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                String location = startTextLocation.getText().toString();
//                try {
//                    startSearchAddress = geocoder.getFromLocationName(location, 1);
//                    Address address1 = startSearchAddress.get(0);
//                    Toast.makeText(getBaseContext(),"Past habit completion deleted." + address1,Toast.LENGTH_SHORT).show();
//                    double lat = address1.getLatitude();
//                    double lng = address1.getLongitude();
//                    LatLng bleh = new LatLng(lat,lng);
//                    CameraUpdate update = CameraUpdateFactory.newLatLngZoom(bleh, 15);
//                    mMap.moveCamera(update);
//                } catch(IOException e){
//                    e.printStackTrace();
//                }
//
//            }
//        });

//        if(mGoogleApiClient == null){
//            mGoogleApiClient = new GoogleApiClient.Builder(this)
//                    .addConnectionCallbacks(this)
//                    .addOnConnectionFailedListener(this)
//                    .addApi(LocationServices.API)
//                    .build();
//        }
//        requestPermission(Manifest.permission.ACCESS_FINE_LOCATION,
//                LOCATION_REQUEST_CODE);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo){
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.map_options, menu);
        //MenuItem useMarkers = menu.findItem(R.id.use_markers);
        //MenuItem useSearcheer = menu.findItem(R.id.use_searcher);
    }

    public boolean onContextItemSelected(MenuItem item){
        switch (item.getItemId()){
            case R.id.use_markers:
                Toast.makeText(getApplicationContext(),"marker", Toast.LENGTH_SHORT).show();
                item.setChecked(true);
                menuLocationOption = 1;
                return true;
            case R.id.use_searcher:
                Toast.makeText(getApplicationContext(),"searcher", Toast.LENGTH_SHORT).show();
                item.setChecked(true);
                menuLocationOption = 2;
                return true;
        }
        return super.onContextItemSelected(item);
    }

    public void resetMap(){
        mMap.clear();
        startAndEndPoints.clear();
        addressesToReturn.clear();
        startMarker = null;
        endMarker = null;
        startSearchAddressReturn = null;
        fromLocationName = null;
        endSearchAddressReturn = null;
        toLocationName = null;

//        private GoogleMap mMap;
//        private ArrayList<LatLng> startAndEndPoints;
//        private ArrayList<LatLng> startAndEndPointsSearcher;
//        private Geocoder geocoder;
//        private List<Address> addresses = null;
//        private List<Address> startSearchAddressReturn = null;
//        private List<Address> endSearchAddressReturn = null;
//        private ArrayList<String> addressesToReturn;
//        private float[] distanceResult = new float[1];
//        private Marker startMarker;
//        private Marker endMarker;
//        private String fromLocationName = null;
//        private String toLocationName = null;
    }
//    @Override
//    protected void onResume() {
//        super.onResume();
//        //setUpMapIfNeeded();
//        mGoogleApiClient.connect();
//    }

//    @Override
//    protected void onStart() {
//        mGoogleApiClient.connect();
//        //Toast.makeText(getBaseContext(),"Past habit completion deleted.",Toast.LENGTH_SHORT).show();
//        super.onStart();
//    }
//
//    @Override
//    protected void onStop() {
//        mGoogleApiClient.disconnect();
//        super.onStop();
//    }

//    @Override
//    protected void onPause() {
//        super.onPause();
//        if (mGoogleApiClient.isConnected()) {
//            mGoogleApiClient.disconnect();
//        }
//    }


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

        // Disable the toolbar on bottom right of screen
        mMap.getUiSettings().setMapToolbarEnabled(false);
        // Add zoom buttons
        mMap.getUiSettings().setZoomControlsEnabled(true);

        // Add a marker in Sydney and move the camera
        //LatLng sydney = new LatLng(-34, 151);
        LatLng edmonton = new LatLng(53.5444,-113.4909);
        //mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(edmonton,10));


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

//        int permissionCheck = ContextCompat.checkSelfPermission(this,
//                Manifest.permission.WRITE_CALENDAR);

        mMap.setMyLocationEnabled(true);

//        LocationManager service = (LocationManager) getSystemService(LOCATION_SERVICE);
//        boolean gpsReady = service.isProviderEnabled(LocationManager.GPS_PROVIDER);
//        if (gpsReady == false){
//            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
//            startActivity(intent);
//        }

        //http://stackoverflow.com/questions/35348604/location-button-doesnt-work-googlemaps-v2
        mMap.setOnMyLocationButtonClickListener(new GoogleMap.OnMyLocationButtonClickListener() {
            @Override
            public boolean onMyLocationButtonClick() {
//                LatLng loc = new LatLng(mMap.getMyLocation().getLatitude(),mMap.getMyLocation().getLongitude());
                LatLng loc = new LatLng(53.5444,-113.4909);
                mMap.moveCamera(CameraUpdateFactory.newLatLng(loc));
                return true;
            }
        });


        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                if (menuLocationOption == 1) {
                    // Need to check if there is already a start and end point
                    //Toast.makeText(getBaseContext(),"Past habit completion deleted." + startAndEndPoints.size(),Toast.LENGTH_SHORT).show();
                    if (startAndEndPoints.size() >= 2 || startMarker != null) {
                        resetMap();
                    }

                    startAndEndPoints.add(latLng);
                    //Toast.makeText(getBaseContext(),"Past habit completion deleted." + startAndEndPoints,Toast.LENGTH_SHORT).show();
                    if (startAndEndPoints.size() == 1) {
                        mMap.addMarker(new MarkerOptions().position(startAndEndPoints.get(0)).title("Start Location"));
                    } else {
                        mMap.addMarker(new MarkerOptions().position(startAndEndPoints.get(1)).title("End Location"));
                        LatLng startDestination = startAndEndPoints.get(0);
                        LatLng endDestination = startAndEndPoints.get(1);
                        getAddressInfo(startDestination);
                        getAddressInfo(endDestination);
                        // Metres
                        Location.distanceBetween(startDestination.latitude, startDestination.longitude, endDestination.latitude, endDestination.longitude, distanceResult);
                        Toast.makeText(getBaseContext(), "Distance between points in metres " + distanceResult[0], Toast.LENGTH_SHORT).show();
                    }

                }
            }
        });



    }

    // https://developer.android.com/training/location/display-address.html
    public void getAddressInfo(LatLng startPoint){
        String errorMessage = "";
        double latitudePoint = startPoint.latitude;
        double longitudePoint = startPoint.longitude;
        try {
            addresses = geocoder.getFromLocation(
                    latitudePoint,
                    longitudePoint,
                    // In this sample, get just a single address.
                    1);
        } catch (IOException ioException) {
            // Catch network or other I/O problems.
            errorMessage = "The service is unavailable.";
            Log.e(TAG, errorMessage, ioException);
        } catch (IllegalArgumentException illegalArgumentException) {
            // Catch invalid latitude or longitude values.
            errorMessage = "The latitude or longitude values are invalid";
            Log.e(TAG, errorMessage + ". " +
                    "Latitude = " + latitudePoint +
                    ", Longitude = " +
                    longitudePoint, illegalArgumentException);
        }

        // Handle case where no address was found.
        if (addresses == null || addresses.size()  == 0) {
            if (errorMessage.isEmpty()) {
                errorMessage = "No address found.";
                Log.e(TAG, errorMessage);
            }

            //deliverResultToReceiver(Constants.FAILURE_RESULT, errorMessage);
//        } else {
//            Address address = addresses.get(0);
//            ArrayList<String> addressFragments = new ArrayList<String>();
//
//            // Fetch the address lines using getAddressLine,
//            // join them, and send them to the thread.
//            for(int i = 0; i < address.getMaxAddressLineIndex(); i++) {
//                addressFragments.add(address.getAddressLine(i));
//            }
//            Log.i(TAG, getString(R.string.address_found));
//            deliverResultToReceiver(Constants.SUCCESS_RESULT,
//                    TextUtils.join(System.getProperty("line.separator"),
//                            addressFragments));
//        }

        } else {
            String locationAddress = addresses.get(0).getAddressLine(0);
            addressesToReturn.add(locationAddress);
            //Toast.makeText(getBaseContext(),"Past habit completion deleted." + locationAddress,Toast.LENGTH_SHORT).show();
        }
    }


//    @Override
//    public void onConnected(@Nullable Bundle bundle) {
//        Log.i(TAG,"Connected to services");
//        Location mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
////        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
////                mGoogleApiClient);
//        //Toast.makeText(getBaseContext(),"Past habit completion deleted.",Toast.LENGTH_SHORT).show();
//        if (mLastLocation != null) {
//            //Toast.makeText(getBaseContext(),"Past habit completion deleted.",Toast.LENGTH_SHORT).show();
//            if (ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
//                    || ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED){
//                double mLatitude = mLastLocation.getLatitude();
//                double mLongitude = mLastLocation.getLongitude();
//                LatLng latLng = new LatLng(mLatitude, mLongitude);
//                MarkerOptions options = new MarkerOptions()
//                        .position(latLng)
//                        .title("I am here!");
//                mMap.addMarker(options);
//                mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
//                Toast.makeText(getBaseContext(),"Stuff." + mLatitude ,Toast.LENGTH_SHORT).show();
//            }
////            mLatitudeText.setText(String.valueOf(mLastLocation.getLatitude()));
////            mLongitudeText.setText(String.valueOf(mLastLocation.getLongitude()));
//        } else {
//            //LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
//        }
//
//    }

//    @Override
//    public void onConnectionSuspended(int i) {
//        Log.i(TAG, "Reconnect.");
//    }
//
//    @Override
//    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
//
//    }
//
//    @Override
//    public void onLocationChanged(Location location) {
//
//    }

//    protected void requestPermission(String permissionType, int requestCode) {
//        int permission = ContextCompat.checkSelfPermission(this,
//                permissionType);
//
//        if (permission != PackageManager.PERMISSION_GRANTED) {
//            ActivityCompat.requestPermissions(this,
//                    new String[]{permissionType}, requestCode
//            );
//        }
//    }

//    @Override
//    public void onRequestPermissionsResult(int requestCode, String[] permissions,
//                                           int[] grantResults){
//        switch (requestCode) {
//            case LOCATION_REQUEST_CODE: {
//
//                if (grantResults.length == 0
//                        || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
//                    Toast.makeText(this, "Unable to show location - permission required", Toast.LENGTH_LONG).show();
//                }
//                return;
//            }
//        }
//    }
}

