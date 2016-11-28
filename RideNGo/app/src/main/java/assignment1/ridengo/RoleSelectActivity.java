package assignment1.ridengo;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.Button;
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
import java.util.List;

/**
 * The type Role select activity.
 * The user can select to be a driver or to be a rider
 */
public class RoleSelectActivity extends Activity {
    final Activity activity = this;
    private int mBackKeyPressedTimes = 0;
    private String username;
    private List<RideRequest> offlinePostedRequests;
    private RideRequest offlineAcceptedRequest;
    private static final String AR_FILE = "offlineAcceptedRequest";
    private static final String PR_FILE = "offlinePostedRequest";
    private static final String DRL_FILE = "driverOfflineRequestList";
    private static final String RRL_FILE = "riderOfflineRequestList";
    private static final String T = ".sav";
    private List<RideRequest> driverRideRequestList;
    private List<RideRequest> riderRideRequestList;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_role_select);

        username = getIntent().getStringExtra("username");
        RideRequestController.notifyUser(username, this);

        RideRequestController.loadRequestListFromServer("{\"from\":0,\"size\":10000,\"query\": { \"match\": { \"username\": \"" + username + "\"}}}");

        if(isConnected()) {
            UserController.loadUserListFromServer("{\"from\":0,\"size\":10000,\"query\": { \"match\": { \"username\": \"" + username + "\"}}}");
            RideRequestController.loadRequestListFromServer("{\"from\": 0, \"size\": 10000}");
            driverRideRequestList = RideRequestController.getRequestList().getRequestsWithDriver(username);
            riderRideRequestList = RideRequestController.getRequestList().getRequestsWithRider(username);
            saveAcceptedRequestList();
            savePostedRequestList();
        }

        Button editInfoButton = (Button) findViewById(R.id.button_EditInfo);
        editInfoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isConnected()) {
                    Intent intent = new Intent(activity, UserInfoActivity.class);
                    intent.putExtra("username", username);
                    startActivity(intent);
                }
                else {
                    Toast.makeText(activity,"You are offline now, please check your network status.",Toast.LENGTH_SHORT).show();
                }
            }
        });

        Button riderButton = (Button) findViewById(R.id.button_Rider);
        riderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(activity, RiderMainActivity.class);
                intent.putExtra("username", username);
                startActivity(intent);
            }
        });

        Button driverButton = (Button) findViewById(R.id.button_Driver);
        driverButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RideRequestController.loadRequestListFromServer("{\"from\":0,\"size\":10000,\"query\": { \"match\": { \"username\": \"" + username + "\"}}}");
                if(UserController.getUserList().getUserByUsername(username).haveVehicle()){
                    Intent intent = new Intent(activity, DriverMainActivity.class);
                    intent.putExtra("username", username);
                    startActivity(intent);
                }
                else{
                    AlertDialog.Builder adb = new AlertDialog.Builder(RoleSelectActivity.this);
                    adb.setMessage("Please add a vehicle before proceeding.");
                    adb.setCancelable(true);
                    adb.setPositiveButton("Add a vehicle", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Intent intent = new Intent(activity,UserInfoActivity.class);
                            intent.putExtra("username",username);
                            startActivity(intent);
                            finish();
                        }
                    });
                    adb.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {}
                    });
                    adb.show();
                }
            }
        });
    }

    public void onResume(){
        super.onResume();
        User user = UserController.getUserList().getUserByUsername(username);
        if(isConnected()) {
            checkOfflinePostRequested(username);
            if (offlinePostedRequests != null) {
                for(RideRequest offlinePostedRequest:offlinePostedRequests) {
                    user.postRideRequest(offlinePostedRequest);
                    Toast.makeText(activity, "Offline request Added, from " + offlinePostedRequest.getStartPoint() + " to " + offlinePostedRequest.getEndPoint(), Toast.LENGTH_SHORT).show();
                }
                offlinePostedRequests = null;
            }

            checkOfflineAcceptedRequest(username);
            if(offlineAcceptedRequest != null){
                int offlineRequestId = offlineAcceptedRequest.getId();
                RideRequest refreshedOfflineRequest = RideRequestController.getRequestList().getRequestById(offlineRequestId);
                if(refreshedOfflineRequest.getStatus().equals("Waiting for Driver") || refreshedOfflineRequest.getStatus().equals("Waiting for Confirmation") ) {
                    user.acceptRequest(refreshedOfflineRequest);
                    offlineAcceptedRequest = null;
                    Toast.makeText(this, "The request you accepted while offline is now accepted. You can check it in VIEW ACCEPTED.", Toast.LENGTH_SHORT).show();
                }
                else{
                    offlineAcceptedRequest = null;
                    Toast.makeText(this, "Sorry, the request you accepted while offline is no longer available.", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    @Override
    public void onBackPressed() {
        if (mBackKeyPressedTimes == 0) {
            Toast.makeText(this, "Press again to sign out.", Toast.LENGTH_SHORT).show();
            mBackKeyPressedTimes = 1;
            new Thread() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } finally {
                        mBackKeyPressedTimes = 0;
                    }
                }
            }.start();
            return;
        }
        else{
            Intent intent = new Intent(activity,MainActivity.class);
            startActivity(intent);
            finish();
        }
    }

    public boolean isConnected(){
        ConnectivityManager cm = (ConnectivityManager) this.getSystemService(this.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return ((activeNetwork != null) && activeNetwork.isConnectedOrConnecting());
    }

    public void checkOfflinePostRequested(String username){
        String FILENAME = PR_FILE+username+T;
        try {
            FileInputStream fis = openFileInput(FILENAME);
            BufferedReader in = new BufferedReader(new InputStreamReader(fis));

            Gson gson = new Gson();
            Type rideRequestType = new TypeToken<List<RideRequest>>(){}.getType();

            offlinePostedRequests = gson.fromJson(in, rideRequestType);
            deleteFile(FILENAME);
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            offlinePostedRequests = null;
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

    private void saveAcceptedRequestList(){
        String FILENAME = DRL_FILE+username+T;
        try {
            deleteFile(FILENAME);
            FileOutputStream fos = openFileOutput(FILENAME, Context.MODE_PRIVATE);

            BufferedWriter out = new BufferedWriter(new OutputStreamWriter(fos));

            Gson gson = new Gson();
            gson.toJson(driverRideRequestList, out);
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

    private void savePostedRequestList(){
        String FILENAME = RRL_FILE+username+T;
        try {
            deleteFile(FILENAME);
            FileOutputStream fos = openFileOutput(FILENAME, Context.MODE_PRIVATE);

            BufferedWriter out = new BufferedWriter(new OutputStreamWriter(fos));

            Gson gson = new Gson();
            gson.toJson(riderRideRequestList, out);
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
}
