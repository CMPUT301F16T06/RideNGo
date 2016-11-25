package assignment1.ridengo;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.util.Pair;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class MapsDriverSearchActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    private Marker driverMarker;
    //private CircleOptions circleAroundMarker;
    private Circle circleForMap;
    private List<RideRequest> rideRequests = RideRequestController.getRequestList().getRequests();
    //PairForSearch[] pairRequests = new PairForSearch[rideRequests.size()];
    private List<PairForSearch> pairRequests = new ArrayList<PairForSearch>();
    private PairForSearch pairCoord;
    private LatLng startPoint;
    private LatLng endPoint;

    private float[] distanceResult = new float[1];
    private List<Float> distanceList = new ArrayList<Float>();
    private float distanceFloat = 0;
    private List<Integer> positionArray =  new ArrayList<Integer>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps_driver_search);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        Button findLocations = (Button) findViewById(R.id.findNearby);

        findLocations.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Collections.sort(distanceList);
                Collections.sort(pairRequests);
                //Toast.makeText(getBaseContext(), "End:  " + positionArray, Toast.LENGTH_SHORT).show();
                //Toast.makeText(getBaseContext(), "End:  " + distanceList, Toast.LENGTH_SHORT).show();
                //Toast.makeText(getBaseContext(), "End:  " + pairRequests.get(1).getIndex(), Toast.LENGTH_LONG).show();
                //Toast.makeText(getBaseContext(), "End:  " + pairRequests.get(1).getValue(), Toast.LENGTH_LONG).show();
                for(int i = 0; i < pairRequests.size(); i++) {
                    Toast.makeText(getBaseContext(), "End:  " + pairRequests.get(i).getIndex(), Toast.LENGTH_SHORT).show();
                    Toast.makeText(getBaseContext(), "End:  " + pairRequests.get(i).getValue(), Toast.LENGTH_SHORT).show();
                }
                Intent intent = new Intent(MapsDriverSearchActivity.this, NearbyListActivity.class);
                Bundle extras = new Bundle();
                //extras.putParcelableArrayList("NEARBY_LOCATIONS",pairRequests);
                extras.putSerializable("NEARBY_LOCATIONS", (Serializable)pairRequests);
                //extras.putStringArrayList("NEARBY_LOCATIONS", (ArrayList<String>)pairRequests);
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
        mMap.getUiSettings().setMapToolbarEnabled(false);
        mMap.getUiSettings().setZoomControlsEnabled(true);

        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
        // Add a marker in Sydney and move the camera
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        //mMap.addMarker(new MarkerOptions().position(rideRequests.get(1).getStartCoord()).title("test"));
        //mMap.addMarker(new MarkerOptions().position(rideRequests.get(1).getEndCoord()).title("test2"));
        //mMap.addMarker(new MarkerOptions().position(rideRequests.get(4).getStartCoord()).title("test3"));
        //mMap.addMarker(new MarkerOptions().position(rideRequests.get(4).getEndCoord()).title("test4"));
//        for(int i = 0; i < rideRequests.size(); i++){
//            Toast.makeText(getBaseContext(), "Start:  " + rideRequests.get(i).getStartCoord(), Toast.LENGTH_SHORT).show();
//            if(rideRequests.get(i).getStartCoord() == null){
//
//            } else {
//                mMap.addMarker(new MarkerOptions().position(rideRequests.get(i).getStartCoord()).title("Start Point"));
//            }
//            Toast.makeText(getBaseContext(), "End:  " + rideRequests.get(i).getEndCoord(), Toast.LENGTH_SHORT).show();
//            if(rideRequests.get(i).getEndCoord() == null){
//
//            } else {
//                mMap.addMarker(new MarkerOptions().position(rideRequests.get(i).getEndCoord()).title("End Point"));
//            }
//
//
//        }
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));

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
            @Override
            public void onMapClick(LatLng latLng) {
                pairRequests.clear();
                if (driverMarker != null){
                    driverMarker.remove();
                }
                LatLng driverCoordinates = new LatLng(latLng.latitude, latLng.longitude);
                driverMarker = mMap.addMarker(new MarkerOptions().position(driverCoordinates).title("Point to search around"));
                if (circleForMap != null){
                    circleForMap.remove();
                }

                for(int i = 0; i < rideRequests.size(); i++){
                    //Toast.makeText(getBaseContext(), "Start:  " + rideRequests.get(i).getStartCoord(), Toast.LENGTH_SHORT).show();
                    if(rideRequests.get(i).getStartCoord() == null || rideRequests.get(i).getEndCoord() == null || rideRequests.get(i).getStartCoord() == rideRequests.get(i).getEndCoord()){

                    } else {
                        mMap.addMarker(new MarkerOptions().position(rideRequests.get(i).getStartCoord()).title("Start Point"));
                        Location.distanceBetween(driverCoordinates.latitude,
                                driverCoordinates.longitude,
                                rideRequests.get(i).getStartCoord().latitude,
                                rideRequests.get(i).getStartCoord().longitude, distanceResult);

                        distanceFloat = distanceResult[0];
                        //distanceList.add(distanceFloat);
                        //positionArray.add(i);
                        pairCoord = new PairForSearch(i,distanceFloat);
                        pairRequests.add(pairCoord);
                        //Toast.makeText(getBaseContext(), "Start:  " + i, Toast.LENGTH_SHORT).show();
                    }
                    //Toast.makeText(getBaseContext(), "End:  " + rideRequests.get(i).getEndCoord(), Toast.LENGTH_SHORT).show();
                    if(rideRequests.get(i).getEndCoord() == null){

                    } else {
                        mMap.addMarker(new MarkerOptions().position(rideRequests.get(i).getEndCoord()).title("End Point"));

                    }


                }
                CircleOptions circleAroundMarker = new CircleOptions();
                circleAroundMarker.center(driverCoordinates);
                circleAroundMarker.radius(200);
                circleAroundMarker.strokeColor(Color.RED);
                circleAroundMarker.fillColor(Color.parseColor("#500084d3"));
                circleForMap = mMap.addCircle(circleAroundMarker);


                // Filter out all requests by their geolocation and then add them to map. Distance between, RideRequestController needs points things

            }
        });
    }
}
