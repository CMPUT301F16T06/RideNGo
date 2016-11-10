package assignment1.ridengo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class DriverMainActivity extends Activity {
    private String username;
    final Activity activity = this;
    private ArrayAdapter<RideRequest> adapter;
    private ArrayList<RideRequest> tempTestList = new ArrayList<RideRequest>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_main);

        username = getIntent().getStringExtra("username");

        /*
        Set up all buttons, ListView, and adapter here
        When the activity starts, a suggested request list is shown in list view. The suggested list
        should be the same thing as 'findNearbyButton' returns [except those has been accepted (optional)].
         */
        Button searchButton = (Button) findViewById(R.id.SearchButton);
        Button findNearbyButton = (Button) findViewById(R.id.FindNearbyButton);
        Button viewAcceptedButton = (Button) findViewById(R.id.ViewAcceptedButton);
        ListView requestListView = (ListView) findViewById(R.id.DriverRequestListView);
        final List<RideRequest> rideRequestList = RideRequestController.getRequestList().getTestRequests();
        adapter = new ArrayAdapter<RideRequest>(activity,android.R.layout.simple_list_item_1,rideRequestList);
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
         });
         */

        requestListView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id){
                Intent intent = new Intent(activity, DriverRequestDetailActivity.class);
                int hash = rideRequestList.get((int)id).hashCode();
                intent.putExtra("hash", hash);
                startActivity(intent);
            }
        });

    }
}
