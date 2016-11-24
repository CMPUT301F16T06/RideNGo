package assignment1.ridengo;

import android.content.Context;
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
import java.util.ArrayList;

/**
 * The type Driver request detail activity.
 * Driver able to see the details of the request including rider's username, phone number and email address
 */
public class DriverRequestDetailActivity extends AppCompatActivity {

    private RideRequest rideRequest;
    private ArrayList<String> info = new ArrayList<String>();
    private RideRequest offlineAcceptedRequest;
    private static final String AR_FILE = "offlineAcceptedRequest";
    private static final String T = ".sav";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_request_detail);

        UserController.loadUserListFromServer();
        RideRequestController.loadRequestListFromServer();

        final String username = getIntent().getStringExtra("username");
        RideRequestController.notifyUser(username, this);

        if(isConnected()){
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

        final int id = getIntent().getIntExtra("id", 0);
        final User driver = UserController.getUserList().getUserByUsername(username);
        rideRequest = RideRequestController.getRequestList().getRequestById(id);
        getInfo();

        ListView requestDetailListView = (ListView)findViewById(R.id.RequestDetailListView);
        Button acceptButton = (Button)findViewById(R.id.AcceptButton);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, info);
        requestDetailListView.setAdapter(adapter);

        if(rideRequest.isAccepted(username)){
            acceptButton.setText("You've Accepted");
            acceptButton.setEnabled(false);
        }
        else if(rideRequest.getStatus().equals("Driver Confirmed")||rideRequest.getStatus().equals("Trip Completed")){
            acceptButton.setText("Not available");
            acceptButton.setEnabled(false);
        }
        else {
            acceptButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(isConnected()) {
                        driver.acceptRequest(rideRequest);
                    }
                    else{
                        offlineAcceptedRequest = rideRequest;
                        saveOffLineAcceptedRequest(username);
                        offlineAcceptedRequest = null;
                        Toast.makeText(DriverRequestDetailActivity.this, "You are offline now, you will accept the request once you are online if the request is still available.", Toast.LENGTH_SHORT).show();
                    }
                    finish();
                }
            });
        }
        requestDetailListView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id){
                if(id == 3){
                    Toast.makeText(DriverRequestDetailActivity.this,"Email rider",Toast.LENGTH_SHORT).show();
                }
                else if(id == 4) {
                    Toast.makeText(DriverRequestDetailActivity.this, "Call rider", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    /**
     * Get info.
     */
    private void getInfo(){
        info.clear();
        User rider = rideRequest.getRider();
        info.add("Start: " + rideRequest.getStartPoint().toString());
        info.add("End: " + rideRequest.getEndPoint().toString());
        info.add("Name: " + rider.getUsername());
        info.add("Email: " + rider.getEmail());
        info.add("Phone: " + rider.getPhoneNum());
        info.add("Status: " + rideRequest.getStatus().toString());
    }

    public boolean isConnected(){
        ConnectivityManager cm = (ConnectivityManager) this.getSystemService(this.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return ((activeNetwork != null) && activeNetwork.isConnectedOrConnecting());
    }

    private void saveOffLineAcceptedRequest(String username){
        String FILENAME = AR_FILE+username+T;
        try {
            FileOutputStream fos = openFileOutput(FILENAME, Context.MODE_PRIVATE);

            BufferedWriter out = new BufferedWriter(new OutputStreamWriter(fos));

            Gson gson = new Gson();
            gson.toJson(offlineAcceptedRequest, out);
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
