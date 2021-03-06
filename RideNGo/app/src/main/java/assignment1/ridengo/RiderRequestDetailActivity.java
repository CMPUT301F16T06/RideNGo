package assignment1.ridengo;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.List;

/**
 * The type Rider request detail activity.
 */
public class RiderRequestDetailActivity extends AppCompatActivity {

    private int position;
    private RideRequest rideRequest;
    private List<RideRequest> offlinePostedRequests;
    private static final String PR_FILE = "offlinePostedRequest";
    private static final String T = ".sav";
    private String username;
    /**
     * The Activity.
     */
    private final Activity activity = this;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rider_request_detail);

        username = getIntent().getStringExtra("username");
        RideRequestController.notifyUser(username, this);
        position = getIntent().getIntExtra("position", 0);

        RideRequestController.loadRequestListFromServer("{\"from\":0,\"size\":10000,\"query\": { \"match\": { \"rider.username\": \"" + username + "\"}}}");
        if(isConnected()) {
            checkOfflinePostRequest();
            if (offlinePostedRequests != null) {
                User r = UserController.getUserList().getUserByUsername(username);
                for(RideRequest offlinePostedRequest:offlinePostedRequests) {
                    r.postRideRequest(offlinePostedRequest);
                    Toast.makeText(activity, "Offline request Added, from " + offlinePostedRequest.getStartPoint() + " to " + offlinePostedRequest.getEndPoint(), Toast.LENGTH_SHORT).show();
                }
                offlinePostedRequests = null;
            }
        }

        rideRequest = RideRequestController.getRequestList().getRequestsWithRider(username).get(position);
        //final UserList userList = UserController.getUserList();

        TextView startPoint = (TextView) findViewById(R.id.RequestDetailStartPointTextView);
        startPoint.setText(rideRequest.getStartPoint());

        TextView endPoint = (TextView) findViewById(R.id.RequestDetailEndPointTextView);
        endPoint.setText(rideRequest.getEndPoint());

        final TextView status = (TextView) findViewById(R.id.RequestDetailCurrentStatusTextView);
        status.setText(rideRequest.getStatus().toString());

        final ListView listView = (ListView) findViewById(R.id.RequestDetailListView);

        final List<User> driverList = rideRequest.getAcceptions();
        final ArrayAdapter<User> adapter = new ArrayAdapter<User>(activity, android.R.layout.simple_list_item_1, driverList);
        listView.setAdapter(adapter);


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapter, View view, int pos, long arg) {

                if(isConnected()) {
                    final int id = (int) arg;
                    Dialog dialog = new Dialog(RiderRequestDetailActivity.this);
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialog.setContentView(R.layout.dialog_driver_info);

                    TextView driverUsername = (TextView) dialog.findViewById(R.id.driverUsername);
                    TextView driverPhone = (TextView) dialog.findViewById(R.id.driverPhone);
                    TextView driverEmail = (TextView) dialog.findViewById(R.id.driverEmail);
                    TextView driverVehicle = (TextView) dialog.findViewById(R.id.driverVehicle);
                    TextView driverRate = (TextView) dialog.findViewById(R.id.driverRating);

                    String driverUserName = driverList.get(pos).getUsername();
                    UserController.loadUserListFromServer("{\"from\":0,\"size\":10000,\"query\": { \"match\": { \"username\": \"" + driverUserName + "\"}}}");
                    User driverUser = UserController.getUserList().getUserByUsername(driverUserName);

                    driverUsername.setText(driverUserName);
                    driverPhone.setText(driverUser.getPhoneNum());
                    driverEmail.setText(driverUser.getEmail());
                    driverVehicle.setText(driverUser.getVehicle().toString());

                    int numRatings = driverUser.getNumRatings();
                    driverRate.setText(String.format("%.2f", driverUser.getRating()) + "/5 from " + numRatings + " users");

                    Button okButton = (Button) dialog.findViewById(R.id.okButton);
                    if (!rideRequest.getStatus().equals("Waiting for Confirmation")) {
                        okButton.setEnabled(false);
                    } else {
                        okButton.setOnClickListener(new View.OnClickListener() {
                            public void onClick(View v) {

                                User driver = driverList.get(id);
                                rideRequest.getRider().confirmAcception(rideRequest, driver);
                                finish();
                            }
                        });
                    }
                    dialog.show();
                }
                else{
                    Toast.makeText(RiderRequestDetailActivity.this,"You are offline now, please check your network status.",Toast.LENGTH_SHORT).show();
                }
            }

        });

        Button cancelRequestButton = (Button) findViewById(R.id.RequestDetailCancelRequestButton);
        if (rideRequest.getStatus().equals("Driver Confirmed") || rideRequest.getStatus().equals("Trip Completed")) {
            cancelRequestButton.setEnabled(false);
        } else {
            cancelRequestButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(isConnected()) {
                        rideRequest.getRider().cancelRequest(rideRequest);
                        Intent intent = new Intent(activity,RiderMainActivity.class);
                        intent.putExtra("username",username);
                        startActivity(intent);
                        finish();
                    }
                    else{
                        Toast.makeText(RiderRequestDetailActivity.this,"You are offline now, please check your network status.",Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }

        Button confirmButton = (Button) findViewById(R.id.RequestDetailConfirmButton);

        if (!rideRequest.getStatus().equals("Driver Confirmed")) {
            confirmButton.setEnabled(false);
        } else {
            confirmButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(isConnected()) {
                        final Dialog rateDialog = new Dialog(RiderRequestDetailActivity.this);
                        rateDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                        rateDialog.setContentView(R.layout.dialog_rate_driver);

                        final TextView driverUser = (TextView) rateDialog.findViewById(R.id.driverUser);
                        driverUser.setText(rideRequest.getDriver().getUsername());

                        final Button rateButton = (Button) rateDialog.findViewById(R.id.rateButton);

                        rateButton.setOnClickListener(new View.OnClickListener() {
                            public void onClick(View v) {
                                RatingBar rateDriver = (RatingBar) rateDialog.findViewById(R.id.rateDriverBar);
                                float userRating = rateDriver.getRating();
                                UserController.getUserList().getUserByUsername(rideRequest.getDriver().getUsername()).setRating(userRating);

                                rideRequest.getRider().riderCompleteRide(rideRequest);
                                rateButton.setEnabled(false);
                                finish();
                            }
                        });
                        rateDialog.show();
                    }
                    else{
                        Toast.makeText(RiderRequestDetailActivity.this,"You are offline now, please check your network status.",Toast.LENGTH_SHORT).show();
                    }
                }

            });
        }
        ImageButton refreshButton = (ImageButton) findViewById(R.id.button_Refresh);
        refreshButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RideRequestController.loadRequestListFromServer("{\"from\": 0, \"size\": 10000}");
                rideRequest = RideRequestController.getRequestList().getRequestsWithRider(username).get(position);
                driverList.clear();
                driverList.addAll(rideRequest.getAcceptions());
                adapter.notifyDataSetChanged();
                Toast.makeText(RiderRequestDetailActivity.this,"Refreshed",Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * Is connected boolean.
     *
     * @return the boolean
     */
    public boolean isConnected(){
        ConnectivityManager cm = (ConnectivityManager) this.getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return ((activeNetwork != null) && activeNetwork.isConnectedOrConnecting());
    }

    /**
     * Check offline post request.
     */
    public void checkOfflinePostRequest(){
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
        }
    }

    @Override
    public void onBackPressed(){
        Intent intent = new Intent(this,RiderMainActivity.class);
        intent.putExtra("username",username);
        startActivity(intent);
        finish();
    }
}
