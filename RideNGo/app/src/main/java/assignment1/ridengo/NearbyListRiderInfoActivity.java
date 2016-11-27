package assignment1.ridengo;

import android.content.Intent;
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

public class NearbyListRiderInfoActivity extends AppCompatActivity {

    private String username;
    private Integer indexOfRequest = null;
    private ArrayList<String> info = new ArrayList<String>();
    private List<RideRequest> rideRequests = RideRequestController.getRequestList().getRequests();
    //private ArrayList<LatLng> listOfPoints = null;
    private LatLng startPoint;
    private LatLng endPoint;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nearby_list_rider_info);
        UserController.loadUserListFromServer("{\"from\":0,\"size\":10000,\"query\": { \"match\": { \"username\": \"" + username + "\"}}}");
        RideRequestController.loadRequestListFromServer("{\"from\": 0, \"size\": 10000}");
        Toast.makeText(getBaseContext(), "TestRIDER:  " + rideRequests.get(5).getRider(), Toast.LENGTH_LONG).show();

        username = getIntent().getStringExtra("username");
        Toast.makeText(getBaseContext(), "Index of request:  " + username, Toast.LENGTH_SHORT).show();
        //RideRequestController.notifyUser(username, this);

        indexOfRequest = getIntent().getIntExtra("INDEX_OF_SEARCHED",0);
        Toast.makeText(getBaseContext(), "Index of request:  " + indexOfRequest, Toast.LENGTH_SHORT).show();
        Toast.makeText(getBaseContext(), "INDEX TEST:  " + rideRequests.get(indexOfRequest).getRider(), Toast.LENGTH_LONG).show();

        //final int id = getIntent().getIntExtra("id", 0);
        final User driver = UserController.getUserList().getUserByUsername(username);
        //rideRequest = RideRequestController.getRequestList().getRequestById(id);
        getInfo(indexOfRequest);


        //username = getIntent().getStringExtra("username");
        ListView nearbyRequestsInfoListView = (ListView)findViewById(R.id.NearbyRequestDetailListView);
        Button acceptButton = (Button)findViewById(R.id.AcceptButton);
        Button showOnMapButton = (Button) findViewById(R.id.mapShowButton);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, info);
        nearbyRequestsInfoListView.setAdapter(adapter);

        if(rideRequests.get(indexOfRequest).isAccepted(username)){
            acceptButton.setText("You've Accepted");
            acceptButton.setEnabled(false);
        } else if (rideRequests.get(indexOfRequest).getStatus().equals("Driver Confirmed")||rideRequests.get(indexOfRequest).getStatus().equals("Trip Completed")){
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
                if(id == 3){
                    Toast.makeText(NearbyListRiderInfoActivity.this,"Email rider",Toast.LENGTH_SHORT).show();
                }
                else if(id == 4) {
                    Toast.makeText(NearbyListRiderInfoActivity.this, "Call rider", Toast.LENGTH_SHORT).show();
                }
            }
        });

//        showOnMapButton.setOnClickListener(new View.OnClickListener(){
//            @Override
//            public void onClick(View v){
//                ArrayList<LatLng> listOfPoints = new ArrayList<LatLng>();
//                Bundle extras = new Bundle();
//                startPoint = rideRequests.get(indexOfRequest).getStartCoord();
//                endPoint = rideRequests.get(indexOfRequest).getEndCoord();
//                listOfPoints.add(startPoint);
//                listOfPoints.add(endPoint);
//                Intent intent = new Intent(NearbyListRiderInfoActivity.this, ShowPointsOnMapActivity.class);
//                extras.putParcelableArrayList("SHOW_POINTS",listOfPoints);
//                intent.putExtras(extras);
//                startActivity(intent);
//
//            }
//        });

    }

    /**
     * Get info.
     */
    private void getInfo(int index){
        info.clear();
        //info.add("Rider" + rideRequests.get(index).getRider());
        //Toast.makeText(getBaseContext(), "RIDER:  " + rideRequests.get(index).getRider(), Toast.LENGTH_LONG).show();
        User rider = rideRequests.get(index).getRider();
        info.add("Start: " + rideRequests.get(index).getStartPoint());
        info.add("End: " + rideRequests.get(index).getEndPoint());
        info.add("Name: " + rider.getUsername());
        info.add("Email: " + rider.getEmail());
        info.add("Phone: " + rider.getPhoneNum());
        info.add("Status: " + rideRequests.get(index).getStatus());
        info.add("LatLng Start: " + rideRequests.get(index).getStartCoord());
        info.add("LatLng End: " + rideRequests.get(index).getEndCoord());
    }
}
