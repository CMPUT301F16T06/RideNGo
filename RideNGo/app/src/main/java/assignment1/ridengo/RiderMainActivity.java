package assignment1.ridengo;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.List;

/**
 * The type Rider main activity.
 * Main activity for Rider. Rider able to see/update a list of requests he/she posted, and/or post a new request
 */
public class RiderMainActivity extends AppCompatActivity {

    private String username;
    private RideRequest offlinePostedRequest;
    private List<RideRequest> rideRequestList;
    private static final String PR_FILE = "offlinePostedRequest";
    private static final String RRL_FILE = "riderOfflineRequestList";
    private static final String T = ".sav";
    /**
     * The Activity.
     */
    private final Activity activity = this;
    private ArrayAdapter<RideRequest> adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rider_main);

        username = getIntent().getStringExtra("username");
        RideRequestController.notifyUser(username, this);

        if(isConnected()) {
            UserController.loadUserListFromServer();
            RideRequestController.loadRequestListFromServer();
            checkOfflinePostedRequest();
            if (offlinePostedRequest != null) {
                User r = offlinePostedRequest.getRider();
                r.postRideRequest(offlinePostedRequest);
                Toast.makeText(activity, "Offline request Added, from " + offlinePostedRequest.getStartPoint() + " to " + offlinePostedRequest.getEndPoint(), Toast.LENGTH_SHORT).show();
                offlinePostedRequest = null;
            }
            rideRequestList = RideRequestController.getRequestList().getRequestsWithRider(username);
            savePostedRequestList();
        }
        else{
            offlineLoadRiderRequestList();
        }



        ListView requestListView = (ListView) findViewById(R.id.RiderRequestListView);

        adapter = new ArrayAdapter<RideRequest>(activity, android.R.layout.simple_list_item_1, rideRequestList);
        requestListView.setAdapter(adapter);

        Button addRequestButton = (Button) findViewById(R.id.AddRequestButton);
        addRequestButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isConnected()) {
                    Intent intent = new Intent(activity, RiderPostRequestActivity.class);
                    intent.putExtra("username", username);
                    startActivity(intent);
                }
                else{
                    Toast.makeText(RiderMainActivity.this,"You are offline now, please check your network status.",Toast.LENGTH_SHORT).show();
                }

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
        ListView requestListView = (ListView) findViewById(R.id.RiderRequestListView);
        final List<RideRequest> rideRequestList = RideRequestController.getRequestList().getRequestsWithRider(username);
        adapter = new ArrayAdapter<RideRequest>(activity, android.R.layout.simple_list_item_1, rideRequestList);
        requestListView.setAdapter(adapter);
    }

    public boolean isConnected(){
        ConnectivityManager cm = (ConnectivityManager) this.getSystemService(this.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return ((activeNetwork != null) && activeNetwork.isConnectedOrConnecting());
    }

    public void checkOfflinePostedRequest(){
        String FILENAME = PR_FILE+username+T;
        try {
            FileInputStream fis = openFileInput(FILENAME);
            BufferedReader in = new BufferedReader(new InputStreamReader(fis));

            Gson gson = new Gson();
            Type rideRequestType = new TypeToken<RideRequest>(){}.getType();

            offlinePostedRequest = gson.fromJson(in, rideRequestType);
            deleteFile(FILENAME);
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            offlinePostedRequest = null;
        } catch (IOException e) {
            // TODO Auto-generated catch block
            throw new RuntimeException();
        }
    }

    private void savePostedRequestList(){
        String FILENAME = RRL_FILE+username+T;
        try {
            deleteFile(FILENAME);
            FileOutputStream fos = openFileOutput(FILENAME, Context.MODE_PRIVATE);

            BufferedWriter out = new BufferedWriter(new OutputStreamWriter(fos));

            Gson gson = new Gson();
            gson.toJson(rideRequestList, out);
            out.flush();

            fos.close();
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            throw new RuntimeException();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            throw new RuntimeException();
        }
    }

    public void offlineLoadRiderRequestList(){
        String FILENAME = RRL_FILE+username+T;
        try {
            FileInputStream fis = openFileInput(FILENAME);
            BufferedReader in = new BufferedReader(new InputStreamReader(fis));

            Gson gson = new Gson();
            Type rideRequestListType = new TypeToken<List<RideRequest>>(){}.getType();

            rideRequestList = gson.fromJson(in, rideRequestListType);
        } catch (FileNotFoundException e) {
            //rideRequestList = null;
            rideRequestList = null;
        } catch (IOException e) {
            // TODO Auto-generated catch block
            throw new RuntimeException();
        }
    }

}
