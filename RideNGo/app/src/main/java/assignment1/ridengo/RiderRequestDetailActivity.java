package assignment1.ridengo;

import android.app.Activity;
import android.app.Dialog;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.List;

/**
 * The type Rider request detail activity.
 * Rider able to see the detail for the requests he/she posted.
 * The rider can select the driver, confirm the driver and/or cancel the request
 */
public class RiderRequestDetailActivity extends AppCompatActivity {

    private int position;
    private RideRequest rideRequest;
    private RideRequest offlinePostedRequest;
    private static final String PR_FILE = "offlinePostedRequest";
    private static final String T = ".sav";
    private String username;
    /**
     * The Activity.
     */
    private final Activity activity = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_detail);

        UserController.loadUserListFromServer();
        RideRequestController.loadRequestListFromServer();

        username = getIntent().getStringExtra("username");
        RideRequestController.notifyUser(username, this);
        position = getIntent().getIntExtra("position", 0);

        if(isConnected()) {
            checkOfflinePostRequest();
            if (offlinePostedRequest != null) {
                User r = offlinePostedRequest.getRider();
                r.postRideRequest(offlinePostedRequest);
                Toast.makeText(activity, "Offline request Added, from " + offlinePostedRequest.getStartPoint() + " to " + offlinePostedRequest.getEndPoint(), Toast.LENGTH_SHORT).show();
                offlinePostedRequest = null;
            }
        }

        rideRequest = RideRequestController.getRequestList().getRequestsWithRider(username).get(position);
        final UserList userList = UserController.getUserList();

        TextView startPoint = (TextView) findViewById(R.id.RequestDetailStartPointTextView);
        startPoint.setText(rideRequest.getStartPoint());

        TextView endPoint = (TextView) findViewById(R.id.RequestDetailEndPointTextView);
        endPoint.setText(rideRequest.getEndPoint());

        final TextView status = (TextView) findViewById(R.id.RequestDetailCurrentStatusTextView);
        status.setText(rideRequest.getStatus().toString());

        final ListView listView = (ListView) findViewById(R.id.RequestDetailListView);

        final List<User> driverList = rideRequest.getAcceptions();
        ArrayAdapter<User> adapter = new ArrayAdapter<User>(activity, android.R.layout.simple_list_item_1, driverList);
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
                    User driverUser = userList.getUserByUsername(driverUserName);

                    driverUsername.setText(driverUserName);
                    driverPhone.setText(driverUser.getPhoneNum());
                    driverEmail.setText(driverUser.getEmail());
                    driverVehicle.setText(driverUser.getVehicle().toString());
                    driverRate.setText(String.valueOf(driverUser.getRating()));

                    Button okButton = (Button) dialog.findViewById(R.id.okButton);
                    if (!rideRequest.getStatus().equals("Accepted By Driver")) {
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

                        // Existing rating
                        final float driverRating = UserController.getUserList().getUserByUsername(rideRequest.getDriver().getUsername()).getTotalOfRating();
                        final int numRatings = UserController.getUserList().getUserByUsername(rideRequest.getDriver().getUsername()).getNumRatings();

                        rateButton.setOnClickListener(new View.OnClickListener() {
                            public void onClick(View v) {
                                RatingBar rateDriver = (RatingBar) rateDialog.findViewById(R.id.rateDriverBar);
                                // User rates driver
                                float userRating = rateDriver.getRating();
                                float avgRating = (driverRating + userRating) / (float) (numRatings + 1);

                                UserController.getUserList().getUserByUsername(rideRequest.getDriver().getUsername()).setRating(avgRating);
                                UserController.getUserList().getUserByUsername(rideRequest.getDriver().getUsername()).setTotalOfRating((int)(driverRating + userRating));
                                UserController.getUserList().getUserByUsername(rideRequest.getDriver().getUsername()).setNumRatings(numRatings + 1);

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
    }

    public boolean isConnected(){
        ConnectivityManager cm = (ConnectivityManager) this.getSystemService(this.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return ((activeNetwork != null) && activeNetwork.isConnectedOrConnecting());
    }

    public void checkOfflinePostRequest(){
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
}
