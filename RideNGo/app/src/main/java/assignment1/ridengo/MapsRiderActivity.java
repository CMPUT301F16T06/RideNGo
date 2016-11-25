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
 * http://stackoverflow.com/questions/14710744/how-to-draw-road-directions-between-two-geocodes-in-android-google-map-v2
 * The above link is where the code for getting the route between two points was taken and that
 * encompasses getDirectionsUrl, downloadUrl, DownloadTask, and ParserTask
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

    /**
     * Called when the activity is first created. Initializes all buttons and
     * the autocomplete fragments
     *
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps_rider);

        UserController.loadUserListFromServer();
        RideRequestController.loadRequestListFromServer();

        startAndEndPoints = new ArrayList<LatLng>();
        addressesToReturn = new ArrayList<String>();
        geocoder = new Geocoder(this, Locale.getDefault());
        final Button optionButton = (Button) findViewById(R.id.locationSearcher);
        Button doneButton = (Button) findViewById(R.id.doneButton);

        PlaceAutocompleteFragment autocompleteFragment = (PlaceAutocompleteFragment)
                getFragmentManager().findFragmentById(R.id.place_autocomplete_fragment);

        // The second autocomplete fragment for the end location
        PlaceAutocompleteFragment autocompleteFragmentTo = (PlaceAutocompleteFragment)
                getFragmentManager().findFragmentById(R.id.place_autocomplete_fragment_from);

        /**
         * Wait for user to click on the autocomplete fragment and place markers depending
         * on the location picked
         */
        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                // Check if the search place option has been picked
                if (menuLocationOption == 2) {
                    fromLocation = place.getLatLng();
                    fromLocationName = place.getName().toString();
                    if (startMarker == null) {
                        startMarker = mMap.addMarker(new MarkerOptions().position(fromLocation).title("Start Location"));
                    } else {
                        startMarker.remove();
                        startMarker = mMap.addMarker(new MarkerOptions().position(fromLocation).title("Start Location"));
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

        /**
         * Wait for user to click on the autocomplete fragment and place markers depending
         * on the location picked. This fragment when complete will also draw a line
         * between the start and end locations
         */
        autocompleteFragmentTo.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                // Check if the search option has been picked
                if (menuLocationOption == 2) {
                    if (startMarker != null) {
                        toLocation = place.getLatLng();
                        toLocationName = place.getName().toString();
                        if (endMarker == null) {
                            endMarker = mMap.addMarker(new MarkerOptions().position(toLocation).title("End Location"));
                        } else {
                            endMarker.remove();
                            endMarker = mMap.addMarker(new MarkerOptions().position(toLocation).title("End Location"));
                        }
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(toLocation, 15));
                        Location.distanceBetween(fromLocation.latitude, fromLocation.longitude, toLocation.latitude, toLocation.longitude, distanceResult);

                        // If there is already a line on the map, clear the map first
                        if (polyLine != null){
                            polyLine.remove();
                        }

                        // Get route based on start and end location
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


        /**
         * This android method is used to return the data from the picking of points
         * back to the previous activity
         * @see RiderPostRequestActivity
         */
        doneButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                ArrayList<LatLng> searchedReturnAddresses = new ArrayList<LatLng>();
                Intent intent = new Intent(MapsRiderActivity.this, RiderPostRequestActivity.class);
                Bundle extras = new Bundle();

                // Checks if both start and end location exist when using search option
                if (addressesToReturn.size() == 2){
                    extras.putStringArrayList("ARRAY_LIST_ADDRESS_MARKER", addressesToReturn);
                    searchedReturnAddresses.add(startAndEndPoints.get(0));
                    searchedReturnAddresses.add(startAndEndPoints.get(1));
                    extras.putParcelableArrayList("MARKER_LAT_LNG", searchedReturnAddresses);
                }

                //  Check if both click markers are there if marker method is used
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

        /**
         * Android method used to open context menu to pick options when button is clicked
         */
        optionButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                registerForContextMenu(optionButton);
                openContextMenu(optionButton);
            }
        });

        // Obtain the SupportMapFragment and get notified when the map is ready to be used
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

    /**
     * This method changes information based on which of the two options are picked in the menu
     * @param item This parameter is the two different menu option items
     * @return A boolean to signal the end of the switch case
     */
    public boolean onContextItemSelected(MenuItem item){
        switch (item.getItemId()){
            case R.id.use_markers:
                Toast.makeText(getApplicationContext(),"Marker option selected", Toast.LENGTH_SHORT).show();
                item.setChecked(true);
                resetMap();
                menuLocationOption = 1;
                return true;
            case R.id.use_searcher:
                Toast.makeText(getApplicationContext(),"Searcher option selected", Toast.LENGTH_SHORT).show();
                item.setChecked(true);
                resetMap();
                menuLocationOption = 2;
                return true;
        }
        return super.onContextItemSelected(item);
    }

    /**
     * Function used to return all map conditions to the original form
     */
    private void resetMap(){
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
        mMap.setMyLocationEnabled(true);


        /**
         * When the location button is clicked, make the map move to the lat/lng co-ordinates
         * of Edmonton
         */
        mMap.setOnMyLocationButtonClickListener(new GoogleMap.OnMyLocationButtonClickListener() {
            @Override
            public boolean onMyLocationButtonClick() {
                LatLng loc = new LatLng(53.5444,-113.4909);
                mMap.moveCamera(CameraUpdateFactory.newLatLng(loc));
                return true;
            }
        });


        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {

            /**
             * This function waits for the user to click on the map and adds markers based
             * on the position clicked
             * @param latLng this parameter is the co-ordinates clicked on the map
             */
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
                        // Distance is in metres
                        Location.distanceBetween(startDestination.latitude, startDestination.longitude, endDestination.latitude, endDestination.longitude, distanceResult);
                        Toast.makeText(getBaseContext(), "Distance between points in metres " + distanceResult[0], Toast.LENGTH_SHORT).show();
                        // If line exists on the map already, delete it
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

    /**
     * This method gets the name of a address based on lat/lng co-ordinates
     * @param addressCoordinate this parameter is the lat/ln co-ordinates of a point
     * Some of the code was taken from the android developer tutorial located here
     * https://developer.android.com/training/location/display-address.html
     */
    // https://developer.android.com/training/location/display-address.html
    private void getAddressInfo(LatLng addressCoordinate){
        String errorMessage = "";
        double latitudePoint = addressCoordinate.latitude;
        double longitudePoint = addressCoordinate.longitude;
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

/**
 * This function gets the google directions url information from two points
 * @param origin this parameter is the starting point co-ordinates
 * @param dest   this parameter is the end point co-ordinates
 * @return An url address taken from google api
 *
 * Code taken from
 * http://stackoverflow.com/questions/14710744/how-to-draw-road-directions-between-two-geocodes-in-android-google-map-v2
 */
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

/**
 * This function downloads the data from the url gotten from getDirectionsUrl function
 * @param strUrl this parameter is the url gotten from getDirectionsUrl function
 * @return data from the url address
 *
 */
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

/**
 * This class fetches the data with the help of the downloadUrl class
 * @return the fetched data
 *
 */
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

/**
 * This class parses the data with the help of the DirectionsJSONParser class and adds
 * the route to the map. A small edit to the source code was made so that the polyline
 * could be saved to be deleted in the future
 * @see DirectionsJSONParser
 *
 *
 */
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

