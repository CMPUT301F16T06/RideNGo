package assignment1.ridengo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

/**
 * The type Driver main activity.
 * Main activity for driver, he can search a request by keyword, search by geolocation. And he can view a requests he accepted.
 */
public class DriverMainActivity extends Activity {
    private String username;

    private final Activity activity = this;

    private List<RideRequest> rideRequestList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_main);

        UserController.loadUserListFromServer();
        RideRequestController.loadRequestListFromServer();

        username = getIntent().getStringExtra("username");
        RideRequestController.notifyUser(username, this);

        /*
        Set up all buttons, ListView, and adapter here
        When the activity starts, a suggested request list is shown in list view. The suggested list
        should be the same thing as 'findNearbyButton' returns [except those has been accepted (optional)].
         */
        Button searchButton = (Button) findViewById(R.id.SearchButton);
        Button findNearbyButton = (Button) findViewById(R.id.FindNearbyButton);
        Button viewAcceptedButton = (Button) findViewById(R.id.ViewAcceptedButton);
        final ListView requestListView = (ListView) findViewById(R.id.DriverRequestListView);
        rideRequestList = RideRequestController.getRequestList().getRequests(); //.getTestRequests();
        ArrayAdapter<RideRequest> adapter = new ArrayAdapter<RideRequest>(activity, android.R.layout.simple_list_item_1, rideRequestList);
        requestListView.setAdapter(adapter);

        viewAcceptedButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent = new Intent(activity, DriverAcceptedListActivity.class);
                intent.putExtra("username", username);
                startActivity(intent);
            }
        });

        /*Search part:
        searchButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                //TODO
            }
         });

         findNearbyButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                //TODO
            }
         });*/

        requestListView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id){
                Intent intent = new Intent(activity, DriverRequestDetailActivity.class);
                intent.putExtra("username",username);
                RideRequest request = (RideRequest) requestListView.getItemAtPosition(position);
                intent.putExtra("id", request.getId());
                startActivity(intent);
            }
        });

    }
}
