package assignment1.ridengo;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.AsyncTask;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

/**
 * This is the map activity for the riders where they pick their route
 * @see RiderPostRequestActivity
 * @see DirectionsJSONParser
 */


public class MapsRiderActivity extends FragmentActivity implements OnMapReadyCallback
{
    private static final String TAG = "MapsActivity";
    private int menuLocationOption = 0;

    private GoogleMap mMap;
    private ArrayList<LatLng> startAndEndPoints;
    private Geocoder geocoder;
    private List<Address> addresses = null;
    private Polyline polyLine = null;
    private LatLng fromLocation;
    private LatLng toLocation;
    private ArrayList<String> addressesToReturn;
    private float[] distanceResult = new float[1];
    private Marker startMarker;
    private Marker endMarker;
    private String fromLocationName = null;
    private String toLocationName = null;

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
                    fromLocation = place.getLatLng();
                    fromLocationName = place.getName().toString();


                    if (startMarker == null) {
                        startMarker = mMap.addMarker(new MarkerOptions().position(fromLocation).title("Start Location"));
                    } else {
                        startMarker.remove();
                        startMarker = mMap.addMarker(new MarkerOptions().position(fromLocation).title("Start Location"));
                        //startAndEndPoints.clear();
                    }
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(fromLocation, 15));

                    Log.i(TAG, "Place: " + place.getName());
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
                    if (startMarker != null) {
                        toLocation = place.getLatLng();
                        toLocationName = place.getName().toString();
                        //startAndEndPoints.add(toLocation);
                        if (endMarker == null) {
                            endMarker = mMap.addMarker(new MarkerOptions().position(toLocation).title("End Location"));
                        } else {
                            endMarker.remove();
                            endMarker = mMap.addMarker(new MarkerOptions().position(toLocation).title("End Location"));
                            //startAndEndPoints.remove(endMarker);
                        }
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(toLocation, 15));
                        Location.distanceBetween(fromLocation.latitude, fromLocation.longitude, toLocation.latitude, toLocation.longitude, distanceResult);
                        if (polyLine != null){
                            polyLine.remove();
                        }
                        String url = getDirectionsUrl(toLocation,fromLocation);
                        DownloadTask downloadTask = new DownloadTask();
                        downloadTask.execute(url);
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
                //ArrayList<String> searchedReturnAddresses = new ArrayList<String>();
                ArrayList<LatLng> searchedReturnAddresses = new ArrayList<LatLng>();
                Intent intent = new Intent(MapsRiderActivity.this, RiderPostRequestActivity.class);
                Bundle extras = new Bundle();
                if (addressesToReturn.size() == 2){
                    extras.putStringArrayList("ARRAY_LIST_ADDRESS_MARKER", addressesToReturn);
                    searchedReturnAddresses.add(startAndEndPoints.get(0));
                    searchedReturnAddresses.add(startAndEndPoints.get(1));
                    extras.putParcelableArrayList("MARKER_LAT_LNG", searchedReturnAddresses);
                }

                if (startMarker != null && endMarker != null){
                    searchedReturnAddresses.add(fromLocation);
                    searchedReturnAddresses.add(toLocation);
                    extras.putParcelableArrayList("ARRAY_LIST_ADDRESS_SEARCHED", searchedReturnAddresses);
                    extras.putString("FROM_LOCATION", fromLocationName);
                    extras.putString("TO_LOCATION", toLocationName);
                }
                extras.putFloat("DISTANCE_FROM_POINTS",distanceResult[0]);
                intent.putExtras(extras);
                setResult(RESULT_OK,intent);
                finish();
            }
        });

        optionButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                registerForContextMenu(optionButton);
                openContextMenu(optionButton);
            }
        });

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
    }

    public boolean onContextItemSelected(MenuItem item){
        switch (item.getItemId()){
            case R.id.use_markers:
                Toast.makeText(getApplicationContext(),"marker", Toast.LENGTH_SHORT).show();
                item.setChecked(true);
                resetMap();
                menuLocationOption = 1;
                return true;
            case R.id.use_searcher:
                Toast.makeText(getApplicationContext(),"searcher", Toast.LENGTH_SHORT).show();
                item.setChecked(true);
                resetMap();
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
        fromLocation = null;
        fromLocationName = null;
        toLocation = null;
        toLocationName = null;
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
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Disable the toolbar on bottom right of screen
        mMap.getUiSettings().setMapToolbarEnabled(false);

        // Add zoom buttons
        mMap.getUiSettings().setZoomControlsEnabled(true);
        LatLng edmonton = new LatLng(53.5444,-113.4909);
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

//        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
//                == PackageManager.PERMISSION_GRANTED) {
//            mMap.setMyLocationEnabled(true);
//        } else {
//            ActivityCompat.requestPermissions(MapsRiderActivity.this,
//                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},REQUEST_CODE);
//        }

//        int permissionCheck = ContextCompat.checkSelfPermission(this,
//                Manifest.permission.WRITE_CALENDAR);

        mMap.setMyLocationEnabled(true);


        //http://stackoverflow.com/questions/35348604/location-button-doesnt-work-googlemaps-v2
        mMap.setOnMyLocationButtonClickListener(new GoogleMap.OnMyLocationButtonClickListener() {
            @Override
            public boolean onMyLocationButtonClick() {
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
                    if (startAndEndPoints.size() >= 2 || startMarker != null) {
                        resetMap();
                    }

                    startAndEndPoints.add(latLng);
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
                        if (polyLine != null){
                            polyLine.remove();
                        }
                        String url = getDirectionsUrl(startDestination,endDestination);
                        DownloadTask downloadTask = new DownloadTask();
                        downloadTask.execute(url);
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

        } else {
            String locationAddress = addresses.get(0).getAddressLine(0);
            addressesToReturn.add(locationAddress);
        }
    }

private String getDirectionsUrl(LatLng origin,LatLng dest){

    // Origin of route
    String str_origin = "origin="+origin.latitude+","+origin.longitude;

    // Destination of route
    String str_dest = "destination="+dest.latitude+","+dest.longitude;

    // Sensor enabled
    String sensor = "sensor=false";

    // Building the parameters to the web service
    String parameters = str_origin+"&"+str_dest+"&"+sensor;

    // Output format
    String output = "json";

    String url = "https://maps.googleapis.com/maps/api/directions/"+output+"?"+parameters;

    return url;
}

private String downloadUrl(String strUrl) throws IOException{
    String data = "";
    InputStream iStream = null;
    HttpURLConnection urlConnection = null;
    try{
        URL url = new URL(strUrl);

        urlConnection = (HttpURLConnection) url.openConnection();

        // Connecting to url
        urlConnection.connect();

        // Reading data from url
        iStream = urlConnection.getInputStream();

        BufferedReader br = new BufferedReader(new InputStreamReader(iStream));

        StringBuffer sb  = new StringBuffer();

        String line = "";
        while( ( line = br.readLine())  != null){
            sb.append(line);
        }

        data = sb.toString();

        br.close();

    }catch(Exception e){
        Log.d("Exception url", e.toString());
    }finally{
        iStream.close();
        urlConnection.disconnect();
    }
    return data;
}

// Fetches data from url passed
private class DownloadTask extends AsyncTask<String, Void, String> {

    // Downloading data in non-ui thread
    @Override
    protected String doInBackground(String... url) {

        // For storing data from web service
        String data = "";

        try{
            // Fetching the data from web service
            data = downloadUrl(url[0]);
        }catch(Exception e){
            Log.d("Background Task",e.toString());
        }
        return data;
    }

    // Executes in UI thread, after the execution of
    // doInBackground()
    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);

        ParserTask parserTask = new ParserTask();

        // Invokes the thread for parsing the JSON data
        parserTask.execute(result);
    }
}


private class ParserTask extends AsyncTask<String, Integer, List<List<HashMap<String,String>>>> {

    @Override
    protected List<List<HashMap<String, String>>> doInBackground(String... jsonData) {

        JSONObject jObject;
        List<List<HashMap<String, String>>> routes = null;

        try {
            jObject = new JSONObject(jsonData[0]);
            DirectionsJSONParser parser = new DirectionsJSONParser();

            // Starts parsing data
            routes = parser.parse(jObject);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return routes;
    }

    @Override
    protected void onPostExecute(List<List<HashMap<String, String>>> result) {
        ArrayList<LatLng> points = null;
        PolylineOptions lineOptions = null;
        MarkerOptions markerOptions = new MarkerOptions();

        // Traversing through all the routes
        for (int i = 0; i < result.size(); i++) {
            points = new ArrayList<LatLng>();
            lineOptions = new PolylineOptions();

            // Fetching i-th route
            List<HashMap<String, String>> path = result.get(i);

            // Fetching all the points in i-th route
            for (int j = 0; j < path.size(); j++) {
                HashMap<String, String> point = path.get(j);

                double lat = Double.parseDouble(point.get("lat"));
                double lng = Double.parseDouble(point.get("lng"));
                LatLng position = new LatLng(lat, lng);

                points.add(position);
            }

            // Adding all the points in the route to LineOptions
            lineOptions.addAll(points);
            lineOptions.width(2);
            lineOptions.color(Color.RED);
        }

        // Drawing polyline in the Google Map for the i-th route
        polyLine = mMap.addPolyline(lineOptions);
    }
}


}

