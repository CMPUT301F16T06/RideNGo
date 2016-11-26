package assignment1.ridengo;

import android.app.Activity;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * The type Driver main activity.
 * Main activity for driver, he can search a request by keyword, search by geolocation. And he can view a requests he accepted.
 */
public class DriverMainActivity extends Activity {
    private String username;

    private final Activity activity = this;
    private RideRequest offlineAcceptedRequest;
    private static final String AR_FILE = "offlineAcceptedRequest";
    private static final String T = ".sav";

    private List<RideRequest> rideRequestList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_main);

        username = getIntent().getStringExtra("username");
        RideRequestController.notifyUser(username, this);

        if(isConnected()){
            UserController.loadUserListFromServer();
            RideRequestController.loadRequestListFromServer();
            checkOfflineAcceptedRequest(username);
            if(offlineAcceptedRequest != null){
                int offlineRequestId = offlineAcceptedRequest.getId();
                RideRequest refreshedOfflineRequest = RideRequestController.getRequestList().getRequestById(offlineRequestId);
                if(refreshedOfflineRequest.getStatus().equals("Waiting for Driver") || refreshedOfflineRequest.getStatus().equals("Waiting for Confirmation") ) {
                    User r = UserController.getUserList().getUserByUsername(username);
                    r.acceptRequest(refreshedOfflineRequest);
                    offlineAcceptedRequest = null;
                    Toast.makeText(this, "The request you accepted while offline is now accepted. You can check it in VIEW ACCEPTED.", Toast.LENGTH_SHORT).show();
                }
                else{
                    offlineAcceptedRequest = null;
                    Toast.makeText(this, "Sorry, the request you accepted while offline is no longer available.", Toast.LENGTH_SHORT).show();
                }
            }
        }

        /*
        Set up all buttons, ListView, and adapter here
        When the activity starts, a suggested request list is shown in list view. The suggested list
        should be the same thing as 'findNearbyButton' returns [except those has been accepted (optional)].
         */
        Button searchButton = (Button) findViewById(R.id.SearchButton);
        Button findNearbyButton = (Button) findViewById(R.id.FindNearbyButton);
        Button viewAcceptedButton = (Button) findViewById(R.id.ViewAcceptedButton);
        EditText 
        final ListView requestListView = (ListView) findViewById(R.id.DriverRequestListView);
        rideRequestList = RideRequestController.getRequestList().getRequests(); //.getTestRequests();
        ArrayAdapter<RideRequest> adapter = new ArrayAdapter<RideRequest>(activity, android.R.layout.simple_list_item_1, rideRequestList);
        requestListView.setAdapter(adapter);

        viewAcceptedButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){

            }
        });

        /*Search part:
        searchButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
            */
          findNearbyButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent = new Intent(DriverMainActivity.this, MapsDriverSearchActivity.class);
                startActivity(intent);
            }
         });

        requestListView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id){
                Intent intent = new Intent(activity, DriverRequestDetailActivity.class);
                intent.putExtra("username",username);
                RideRequest request = (RideRequest) requestListView.getItemAtPosition(position);
                intent.putExtra("id", request.getId());
                startActivity(intent);
                finish();
            }
        });

    }

    public boolean isConnected(){
        ConnectivityManager cm = (ConnectivityManager) this.getSystemService(this.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return ((activeNetwork != null) && activeNetwork.isConnectedOrConnecting());
    }

    public void checkOfflineAcceptedRequest(String username){
        String FILENAME = AR_FILE+username+T;
        try {
            FileInputStream fis = openFileInput(FILENAME);
            BufferedReader in = new BufferedReader(new InputStreamReader(fis));

            Gson gson = new Gson();
            Type rideRequestType = new TypeToken<RideRequest>(){}.getType();

            offlineAcceptedRequest = gson.fromJson(in, rideRequestType);
            deleteFile(FILENAME);
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            offlineAcceptedRequest = null;
        } catch (IOException e) {
            // TODO Auto-generated catch block
            throw new RuntimeException();
        }
    }
}
