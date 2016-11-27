package assignment1.ridengo;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.TwoLineListItem;

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
import java.util.List;

import static android.graphics.Color.BLACK;
import static android.graphics.Color.GRAY;

/**
 * The type Driver accepted list activity.
 */
public class DriverAcceptedListActivity extends AppCompatActivity {

    /**
     * The Activity.
     */
    private final Activity activity = this;
    private RideRequest offlineAcceptedRequest;
    private static final String AR_FILE = "offlineAcceptedRequest";
    private static final String DRL_FILE = "driverOfflineRequestList";
    private static final String T = ".sav";
    private String username;
    private List<RideRequest> rideRequestList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_accepted_list);

        username = getIntent().getStringExtra("username");
        RideRequestController.notifyUser(username, this);

        if(isConnected()){
            UserController.loadUserListFromServer();
            RideRequestController.loadRequestListFromServer("{\"from\":0,\"size\":10000,\"query\": { \"match\": { \"acceptions.username\": \"" + username + "\"}}}");
            checkOfflineAcceptedRequest();
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
            rideRequestList = RideRequestController.getRequestList().getRequestsWithDriver(username);
            saveAcceptedRequestList();
        }
        else{
            offlineLoadDriverRequestList();
        }

        final ListView acceptedListView = (ListView) findViewById(R.id.AcceptedListView);


        ArrayAdapter<RideRequest> adapter = new ArrayAdapter<RideRequest>(this, android.R.layout.simple_list_item_1, rideRequestList) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                TwoLineListItem row;
                if (convertView == null) {
                    LayoutInflater inflater = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    row = (TwoLineListItem) inflater.inflate(android.R.layout.simple_list_item_2, null);
                } else {
                    row = (TwoLineListItem) convertView;
                }
                RideRequest request = rideRequestList.get(position);
                row.getText1().setText(request.toString());
                if (!request.isDriver(username)) {
                    row.getText1().setTextColor(GRAY);
                    Toast.makeText(DriverAcceptedListActivity.this, request.getDriver() + "," + request.isDriver(username), Toast.LENGTH_SHORT).show();
                    row.getText2().setText("Request on trip, customer has picked another driver.");
                    row.getText2().setTextColor(GRAY);
                } else {
                    row.getText1().setTextColor(BLACK);
                    row.getText2().setText(request.getStatus());
                    row.getText2().setTextColor(GRAY);
                }
                return row;
            }
        };
        acceptedListView.setAdapter(adapter);

        acceptedListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                RideRequest request = (RideRequest) acceptedListView.getItemAtPosition(position);
                Intent intent = new Intent(activity, DriverRequestDetailActivity.class);
                intent.putExtra("username", username);
                intent.putExtra("id", request.getId());
                startActivity(intent);
            }
        });
    }

    public boolean isConnected(){
        ConnectivityManager cm = (ConnectivityManager) this.getSystemService(this.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return ((activeNetwork != null) && activeNetwork.isConnectedOrConnecting());
    }

    public void checkOfflineAcceptedRequest(){
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

    private void saveAcceptedRequestList(){
        String FILENAME = DRL_FILE+username+T;
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

    public void offlineLoadDriverRequestList(){
        String FILENAME = DRL_FILE+username+T;
        try {
            FileInputStream fis = openFileInput(FILENAME);
            BufferedReader in = new BufferedReader(new InputStreamReader(fis));

            Gson gson = new Gson();
            Type rideRequestListType = new TypeToken<List<RideRequest>>(){}.getType();

            rideRequestList = gson.fromJson(in, rideRequestListType);
        } catch (FileNotFoundException e) {
            //rideRequestList = null;
            throw new RuntimeException();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            throw new RuntimeException();
        }
    }
}
