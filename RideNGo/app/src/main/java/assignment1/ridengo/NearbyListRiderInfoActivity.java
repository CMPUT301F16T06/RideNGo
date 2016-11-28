package assignment1.ridengo;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;

import static android.provider.CalendarContract.CalendarCache.URI;

/**
 * This activity displays all the information associated with a location. Drivers are allowed to
 * accept these locations and allow riders to confirm if they want these drivers. The location
 * index is passed from the previous activity NearbyListActivity
 * @see User
 * @see RideRequest
 * @see NearbyListActivity
 */
public class NearbyListRiderInfoActivity extends AppCompatActivity {

    private static final int REQUEST_CODE = 1;
    private String username;
    private Integer indexOfRequest = null;
    private ArrayList<String> info = new ArrayList<String>();
    private List<RideRequest> rideRequests = RideRequestController.getRequestList().getRequests();
    private LatLng startPoint;
    private LatLng endPoint;
    private String[] phoneNum;
    private String[] emailAddress;
    private Intent phoneIntent;

    /**
     * Activity that is called when activity is first created. Get the user and ride requests from
     * their respective controllers and populate the array adapter with the information of the
     * rider using the getInfo method.
     * @see UserController
     * @see RideRequestController
     *
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nearby_list_rider_info);
        username = getIntent().getStringExtra("username");
        UserController.loadUserListFromServer("{\"from\":0,\"size\":10000,\"query\": { \"match\": { \"username\": \"" + username + "\"}}}");

        //RideRequestController.notifyUser(username, this);

        indexOfRequest = getIntent().getIntExtra("INDEX_OF_SEARCHED",0);
        RideRequestController.loadRequestListFromServer("{\"from\": 0, \"size\": 10000}");
        final User driver = UserController.getUserList().getUserByUsername(username);
        getInfo(indexOfRequest);

        ListView nearbyRequestsInfoListView = (ListView)findViewById(R.id.NearbyRequestDetailListView);
        Button acceptButton = (Button)findViewById(R.id.AcceptButton);
        Button showOnMapButton = (Button) findViewById(R.id.mapShowButton);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, info);
        nearbyRequestsInfoListView.setAdapter(adapter);

        // Check to see if the request has already been accepted by the current driver or any other driver
        if(rideRequests.get(indexOfRequest).isAccepted(username)){
            acceptButton.setText("You've Accepted");
            acceptButton.setEnabled(false);
        } else if (rideRequests.get(indexOfRequest).getStatus().equals("Driver Confirmed")||
                rideRequests.get(indexOfRequest).getStatus().equals("Trip Completed")){
            acceptButton.setText("Not available");
            acceptButton.setEnabled(false);
        } else {
            acceptButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    driver.acceptRequest(rideRequests.get(indexOfRequest));
                    finish();
                }
            });
        }

        nearbyRequestsInfoListView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id){
                // Clicked on the email row, give option to email the rider
                if(id == 3){
                    Intent emailIntent = new Intent(Intent.ACTION_SEND);
                    emailIntent.putExtra(Intent.EXTRA_EMAIL, emailAddress);
                    emailIntent.putExtra(Intent.EXTRA_SUBJECT, "RideNGo");
                    emailIntent.setType("message/rfc822");
                    startActivity(Intent.createChooser(emailIntent, "Send Email"));
                    // Clicked on the email row, give option phone the rider
                } else if(id == 4) {
                    phoneIntent = new Intent(Intent.ACTION_CALL, URI.parse("tel: " + phoneNum[0]));
                    // Checking for phone permissions
                    if(ActivityCompat.checkSelfPermission(NearbyListRiderInfoActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(NearbyListRiderInfoActivity.this, new String[]{Manifest.permission.CALL_PHONE}, REQUEST_CODE);
                    } else {
                        startActivity(phoneIntent);
                    }
                }
            }
        });

        /**
         * When this button is clicked, the start and end point in LatLng format is given to
         * another map activity which displays the two points on the map.
         * @see ShowPointsOnMapActivity
         * @see RideRequest
         */
        showOnMapButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                ArrayList<LatLng> listOfPoints = new ArrayList<LatLng>();
                Bundle extras = new Bundle();
                startPoint = rideRequests.get(indexOfRequest).getStartCoord();
                endPoint = rideRequests.get(indexOfRequest).getEndCoord();
                listOfPoints.add(startPoint);
                listOfPoints.add(endPoint);
                Intent intent = new Intent(NearbyListRiderInfoActivity.this, ShowPointsOnMapActivity.class);
                extras.putParcelableArrayList("SHOW_POINTS",listOfPoints);
                intent.putExtras(extras);
                startActivity(intent);

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
    public void onRequestPermissionsResult(int requestCode, String [] permissions, int[] grantResults){
        switch(requestCode){
            case REQUEST_CODE:
            {
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    startActivity(phoneIntent);
                } else {
                    Toast.makeText(NearbyListRiderInfoActivity.this, "You do not have the desired permissions", Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    /**
     * This method gets the data for a rider for a ride request and stores it in an array to be used
     * later for an array adapter
     * @param index This parameter is the index of the ride request list for the respective rider
     *
     * @see User
     * @see RideRequest
     */
    private void getInfo(int index){
        info.clear();
        User rider = rideRequests.get(index).getRider();
        info.add("Start: " + rideRequests.get(index).getStartPoint());
        info.add("End: " + rideRequests.get(index).getEndPoint());
        info.add("Name: " + rider.getUsername());
        info.add("Email: " + rider.getEmail());
        emailAddress = new String[]{rider.getEmail()};
        info.add("Phone: " + rider.getPhoneNum());
        phoneNum = new String[]{ rider.getPhoneNum()};
        info.add("Status: " + rideRequests.get(index).getStatus());
    }
}
