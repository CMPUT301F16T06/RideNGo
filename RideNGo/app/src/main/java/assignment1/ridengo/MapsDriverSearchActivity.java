package assignment1.ridengo;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * This activity allows the driver to pick a location on the map and to return all locations
 * near that point to sort the locations based on distance. The locations are stored as
 * PairForSearch objects which allow the distance and original index to be saved
 * @see RideRequest
 * @see PairForSearch
 * @see NearbyListActivity
 */
public class MapsDriverSearchActivity extends FragmentActivity implements OnMapReadyCallback {

    private static final int REQUEST_CODE = 1;
    private String username;
    private GoogleMap mMap;
    private Marker driverMarker;
    private Circle circleForMap;
    private List<RideRequest> rideRequests = RideRequestController.getRequestList().getRequests();
    private List<PairForSearch> pairRequests = new ArrayList<PairForSearch>();
    private PairForSearch pairCoord;
    private float[] distanceResult = new float[1];
    private float distanceFloat = 0;

    /**
     * Called upon when activity is first created. Used to set up the user, ride request
     * controller, the map and buttons.
     * @see RideRequestController
     * @see UserController
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps_driver_search);
        UserController.loadUserListFromServer("{\"from\":0,\"size\":10000,\"query\": { \"match\": { \"username\": \"" + username + "\"}}}");
        RideRequestController.loadRequestListFromServer("{\"from\": 0, \"size\": 10000}");
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        username = getIntent().getStringExtra("username");
        Button findLocations = (Button) findViewById(R.id.findNearby);

        findLocations.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Collections.sort(pairRequests);
                Intent intent = new Intent(MapsDriverSearchActivity.this, NearbyListActivity.class);
                Bundle extras = new Bundle();
                extras.putSerializable("NEARBY_LOCATIONS", (Serializable)pairRequests);
                extras.putString("username", username);
                intent.putExtras(extras);
                startActivity(intent);

            }
        });
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
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE);
        } else {
            mMap.setMyLocationEnabled(true);
        }
        mMap.getUiSettings().setMapToolbarEnabled(false);
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.getUiSettings().setZoomControlsEnabled(true);
        LatLng edmonton = new LatLng(53.5444,-113.4909);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(edmonton,10));

        /**
         * This method is used to place a marker on the map and then detect all locations that fit
         * the criteria to be sorted from shortest to longest distance.
         * @see PairForSearch
         */
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                pairRequests.clear();
                if (driverMarker != null){
                    driverMarker.remove();
                }
                LatLng driverCoordinates = new LatLng(latLng.latitude, latLng.longitude);
                driverMarker = mMap.addMarker(new MarkerOptions().position(driverCoordinates)
                        .title("Point to search around")
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
                if (circleForMap != null){
                    circleForMap.remove();
                }

                for(int i = 0; i < rideRequests.size(); i++){
                    if(rideRequests.get(i).getStartCoord() == null || rideRequests.get(i).getEndCoord() == null || rideRequests.get(i).getStartCoord() == rideRequests.get(i).getEndCoord()){
                        // Filter out invalid request
                    } else {
                        mMap.addMarker(new MarkerOptions().position(rideRequests.get(i).getStartCoord()).title("Start Point"));
                        Location.distanceBetween(driverCoordinates.latitude,
                                driverCoordinates.longitude,
                                rideRequests.get(i).getStartCoord().latitude,
                                rideRequests.get(i).getStartCoord().longitude, distanceResult);

                        if(rideRequests.get(i).getStatus().equals("Driver Confirmed") || rideRequests.get(i).getStatus().equals("Trip Completed")){
                            // Do not add to nearby requests list if the request is already confirmed by other drivers
                        } else {
                            distanceFloat = distanceResult[0];
                            pairCoord = new PairForSearch(i,distanceFloat);
                            pairRequests.add(pairCoord);
                        }
                    }
                }

                CircleOptions circleAroundMarker = new CircleOptions();
                circleAroundMarker.center(driverCoordinates);
                circleAroundMarker.radius(200);
                circleAroundMarker.strokeColor(Color.RED);
                circleAroundMarker.fillColor(Color.parseColor("#500084d3"));
                circleForMap = mMap.addCircle(circleAroundMarker);
            }
        });
    }

    /**
     * This function checks the result after requesting permissions
     * @param requestCode This parameter is from the request code in requestPermissions method
     * @param permissions This string contains the permissions requested
     * @param grantResults This parameter is either PERMISSION_GRANTED or PERMISSION_DENIED
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
                        // Permissions granted
                        return;
                    }
                    mMap.setMyLocationEnabled(true);
                } else {
                    Toast.makeText(MapsDriverSearchActivity.this, "You do not have the desired permissions", Toast.LENGTH_LONG).show();
                }
            }
        }
    }
}