package assignment1.ridengo;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.sql.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * The type Rider main activity.
 * Main activity for Rider. Rider able to see/update a list of requests he/she posted, and/or post a new request
 */
public class RiderMainActivity extends AppCompatActivity {

    private String username;
    /**
     * The Activity.
     */
    private final Activity activity = this;
    private ArrayAdapter<RideRequest> adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rider_main);

        UserController.loadUserListFromServer();
        RideRequestController.loadRequestListFromServer();

        username = getIntent().getStringExtra("username");
        RideRequestController.notifyUser(username, this);

        ListView requestListView = (ListView) findViewById(R.id.RiderRequestListView);
        final List<RideRequest> rideRequestList = RideRequestController.getRequestList().getRequestsWithRider(username);
        adapter = new ArrayAdapter<RideRequest>(activity, android.R.layout.simple_list_item_1, rideRequestList);
        requestListView.setAdapter(adapter);
        Button addRequestButton = (Button) findViewById(R.id.AddRequestButton);
        addRequestButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, RiderPostRequestActivity.class);
                intent.putExtra("username", username);
                startActivity(intent);

            }
        });

        requestListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(activity, RiderRequestDetailActivity.class);
                intent.putExtra("username", username);
                intent.putExtra("position", position);
                startActivity(intent);
            }
        });

        RideRequestController.getRequestList().addListener(new Listener() {
            @Override
            public void update() {
                rideRequestList.clear();
                Collection<RideRequest> rideRequests = RideRequestController.getRequestList().getRequestsWithRider(username);
                rideRequestList.addAll(rideRequests);
                adapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    protected void onStart(){
        super.onStart();
        UserController.loadUserListFromServer();
        RideRequestController.loadRequestListFromServer();

        username = getIntent().getStringExtra("username");
        RideRequestController.notifyUser(username, this);

        ListView requestListView = (ListView) findViewById(R.id.RiderRequestListView);
        final List<RideRequest> rideRequestList = RideRequestController.getRequestList().getRequestsWithRider(username);
        adapter = new ArrayAdapter<RideRequest>(activity, android.R.layout.simple_list_item_1, rideRequestList);
        requestListView.setAdapter(adapter);
        Button addRequestButton = (Button) findViewById(R.id.AddRequestButton);
        addRequestButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, RiderPostRequestActivity.class);
                intent.putExtra("username", username);
                startActivity(intent);

            }
        });

        requestListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(activity, RiderRequestDetailActivity.class);
                intent.putExtra("username", username);
                intent.putExtra("position", position);
                startActivity(intent);
            }
        });

        RideRequestController.getRequestList().addListener(new Listener() {
            @Override
            public void update() {
                rideRequestList.clear();
                Collection<RideRequest> rideRequests = RideRequestController.getRequestList().getRequestsWithRider(username);
                rideRequestList.addAll(rideRequests);
                adapter.notifyDataSetChanged();
            }
        });
    }
}
