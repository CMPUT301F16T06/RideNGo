package assignment1.ridengo;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
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
import java.math.RoundingMode;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static android.R.attr.delay;

/**
 * The type Rider post request activity.
 * Rider able to post a new request.
 * Rider need to enter a starting point and a end point.
 * @see MapsRiderActivity
 */
public class RiderPostRequestActivity extends AppCompatActivity {

    private static final int RESULT_SUCCESS = 1;
    private String startPoint = "";
    private String endPoint = "";
    private String description = "";
    private Double fare = 0.0;
    private LatLng startCoord = new LatLng(0,0);
    private LatLng endCoord = new LatLng(0,0);
    private float returnedDistance;
    final private Activity activity = this;
    private List<RideRequest> offlinePostedRequests;
    private static final String PR_FILE = "offlinePostedRequest";
    private static final String T = ".sav";
    private String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rider_post_request);

        username = getIntent().getStringExtra("username");
        RideRequestController.notifyUser(username, this);

        if(isConnected()) {
            checkOfflinePostedRequest();
            if (offlinePostedRequests != null) {
                User r = UserController.getUserList().getUserByUsername(username);
                for(RideRequest offlinePostedRequest:offlinePostedRequests) {
                    r.postRideRequest(offlinePostedRequest);
                    Toast.makeText(activity, "Offline request Added, from " + offlinePostedRequest.getStartPoint() + " to " + offlinePostedRequest.getEndPoint(), Toast.LENGTH_SHORT).show();
                }
                offlinePostedRequests = null;
            }
        }

        UserController.loadUserListFromServer("{\"from\":0,\"size\":10000,\"query\": { \"match\": { \"username\": \"" + username + "\"}}}");
        final User rider = UserController.getUserList().getUserByUsername(username);
        final EditText start = (EditText) findViewById(R.id.StartPointEditText);
        final EditText end = (EditText) findViewById(R.id.EndPointEditText);
        final EditText descText = (EditText) findViewById(R.id.DescriptionEditText);
        final TextView estimatedFare = (TextView) findViewById(R.id.estimatedFareTextView);

        estimatedFare.setText("$"+0.00);
        Button postRequestButton = (Button) findViewById(R.id.postRequestButton);
        Button findPointsOnMapButton = (Button) findViewById(R.id.FindPointOnMapButton);

        postRequestButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startPoint = start.getText().toString();
                endPoint = end.getText().toString();
                //if(startPoint.isEmpty() || endPoint.isEmpty()) {
                //    Toast.makeText(activity, "You Must Choose Two Points first.", Toast.LENGTH_SHORT).show();
                //    return;
                //}
                description = descText.getText().toString().toLowerCase().trim();
                RideRequest rideRequest = new RideRequest(startCoord, endCoord, startPoint, endPoint, description, rider, returnedDistance);
                rideRequest.setFare(fare);
                if(isConnected()) {
                    rider.postRideRequest(rideRequest);
                    delay(300);
                    Toast.makeText(activity, "Request Added, from " + startPoint + " to " + endPoint, Toast.LENGTH_SHORT).show();
                }
                else{
                    checkOfflinePostedRequest();
                    if(offlinePostedRequests == null){
                        offlinePostedRequests = new ArrayList<RideRequest>();
                    }
                    offlinePostedRequests.add(rideRequest);
                    saveOffLinePostedRequests();
                    offlinePostedRequests = null;
                    Toast.makeText(activity, "You are offline now, your request(s) will be post once your device get online again.", Toast.LENGTH_SHORT).show();
                }
                Intent intent = new Intent(activity, RiderMainActivity.class);
                intent.putExtra("username", username);
                startActivity(intent);
                finish();
            }
        });

        findPointsOnMapButton.setOnClickListener(new View.OnClickListener(){

            public void onClick(View v){
                if(isConnected()) {
                    Intent intent = new Intent(RiderPostRequestActivity.this, MapsRiderActivity.class);
                    startActivityForResult(intent, RESULT_SUCCESS);
                }
                else{
                    Toast.makeText(activity, "You are offline now, please check your network status.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    /**
     * This method takes the results from the MapsRiderActivity and stores those results(Start and
     * end points, their LatLng coordinates, distance from start and end points) in order to be
     * added to the ride requests.
     * https://developer.android.com/training/basics/intents/result.html
     * http://stackoverflow.com/questions/5195837/format-float-to-n-decimal-places
     * Accessed on November 8, 2016
     * @param requestCode This parameter is the request code sent from startActivityForResult()
     *                    which allows us to know where the result came from
     * @param resultCode  This parameter is used to determine if the results returned are good for
     *                    use
     * @param data        This parameter holds the data gotten from MapsRiderActivity
     *
     * @see RideRequest
     * @see MapsRiderActivity
     */

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RESULT_SUCCESS) {
            if(resultCode == RESULT_OK) {
                ArrayList<String> returnedAddresses = data.getStringArrayListExtra("ARRAY_LIST_ADDRESS_MARKER");
                ArrayList<LatLng> markerLatLng = data.getParcelableArrayListExtra("MARKER_LAT_LNG");

                final EditText start = (EditText) findViewById(R.id.StartPointEditText);
                final EditText end = (EditText) findViewById(R.id.EndPointEditText);
                final TextView estimatedFare = (TextView) findViewById(R.id.estimatedFareTextView);

                // Then search option used when picking points
                if (returnedAddresses == null) {
                    ArrayList<LatLng> searchedReturnAddressLatLng = data.getParcelableArrayListExtra("ARRAY_LIST_ADDRESS_SEARCHED");
                    startCoord = searchedReturnAddressLatLng.get(0);
                    endCoord = searchedReturnAddressLatLng.get(1);
                    String fromLocationName = data.getStringExtra("FROM_LOCATION");
                    String toLocationName = data.getStringExtra("TO_LOCATION");
                    start.setText(fromLocationName);
                    end.setText(toLocationName);
                } else {
                    startCoord = markerLatLng.get(0);
                    endCoord = markerLatLng.get(1);
                    start.setText(returnedAddresses.get(0));
                    end.setText(returnedAddresses.get(1));
                }
                returnedDistance = data.getFloatExtra("DISTANCE_FROM_POINTS", 0);
                NumberFormat fareFormat = NumberFormat.getInstance(Locale.CANADA);
                fareFormat.setMaximumFractionDigits(2);
                fareFormat.setMinimumFractionDigits(2);
                fareFormat.setRoundingMode(RoundingMode.HALF_UP);

                fare = RideRequest.calculateFare(returnedDistance);

                estimatedFare.setText("$" + fareFormat.format(fare));
            }
        }
    }

    public boolean isConnected(){
        ConnectivityManager cm = (ConnectivityManager) this.getSystemService(this.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return ((activeNetwork != null) && activeNetwork.isConnectedOrConnecting());
    }

    private void saveOffLinePostedRequests(){
        String FILENAME = PR_FILE+username+T;
        try {
            FileOutputStream fos = openFileOutput(FILENAME,Context.MODE_PRIVATE);

            BufferedWriter out = new BufferedWriter(new OutputStreamWriter(fos));

            Gson gson = new Gson();
            gson.toJson(offlinePostedRequests, out);
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

    public void checkOfflinePostedRequest(){
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

    @Override
    public void onBackPressed(){
        Intent intent = new Intent(this,RiderMainActivity.class);
        intent.putExtra("username",username);
        startActivity(intent);
    }

    public void delay(final int ms) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        System.out.println("XXX");                 //add your code here
                    }
                }, ms);
            }
        });
    }
}
