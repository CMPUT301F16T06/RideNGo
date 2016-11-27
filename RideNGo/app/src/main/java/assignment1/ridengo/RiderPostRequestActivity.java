package assignment1.ridengo;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
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
import java.util.Locale;

/**
 * The type Rider post request activity.
 * Rider able to post a new request.
 * Rider need to enter a starting point and a end point.
 */
public class RiderPostRequestActivity extends AppCompatActivity {

    private static final int RESULT_SUCCESS = 1;
    private String startPoint;
    private String endPoint;
    private String description;
    private Double fare;
    private Float returnedDistance;
    private LatLng startCoord;
    private LatLng endCoord;
    final private Activity activity = this;
    private RideRequest offlinePostedRequest;
    private static final String PR_FILE = "offlinePostedRequest";
    private static final String T = ".sav";

    /**
     * The Ride request controller.
     */
    //RideRequestController rideRequestController = new RideRequestController();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rider_post_request);

        final String username = getIntent().getStringExtra("username");
        RideRequestController.notifyUser(username, this);

        if(isConnected()) {
            checkOfflinePostedRequest(username);
            if (offlinePostedRequest != null) {
                User r = offlinePostedRequest.getRider();
                r.postRideRequest(offlinePostedRequest);
                Toast.makeText(activity, "Offline request Added, from " + offlinePostedRequest.getStartPoint() + " to " + offlinePostedRequest.getEndPoint(), Toast.LENGTH_SHORT).show();
                offlinePostedRequest = null;
            }
        }

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
                description = descText.getText().toString().toLowerCase().trim();
                RideRequest rideRequest = new RideRequest(startCoord, endCoord, startPoint, endPoint, description, rider, returnedDistance);
                if(isConnected()) {
                    rider.postRideRequest(rideRequest);
                    Toast.makeText(activity, "Request Added, from " + startPoint + " to " + endPoint, Toast.LENGTH_SHORT).show();
                }
                else{
                    offlinePostedRequest = rideRequest;
                    saveOffLinePostedRequest(username);
                    offlinePostedRequest = null;
                    Toast.makeText(activity, "You are offline now, your request will be post once you get online again.", Toast.LENGTH_SHORT).show();
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

    //https://developer.android.com/training/basics/intents/result.html
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == RESULT_SUCCESS) {
            //Toast.makeText(getBaseContext(),"Error getting location",Toast.LENGTH_SHORT).show();
            if(resultCode == RESULT_OK) {
                ArrayList<String> returnedAddresses = data.getStringArrayListExtra("ARRAY_LIST_ADDRESS_MARKER");
                ArrayList<LatLng> markerLatLng = data.getParcelableArrayListExtra("MARKER_LAT_LNG");

                final EditText start = (EditText) findViewById(R.id.StartPointEditText);
                final EditText end = (EditText) findViewById(R.id.EndPointEditText);
                final TextView estimatedFare = (TextView) findViewById(R.id.estimatedFareTextView);
                //String result=data.getStringExtra("result");

                // Then search option used when picking points
                if (returnedAddresses == null) {
                    ArrayList<LatLng> searchedReturnAddressLatLng = data.getParcelableArrayListExtra("ARRAY_LIST_ADDRESS_SEARCHED");
                    startCoord = searchedReturnAddressLatLng.get(0);
                    endCoord = searchedReturnAddressLatLng.get(1);
                    String fromLocationName = data.getStringExtra("FROM_LOCATION");
                    String toLocationName = data.getStringExtra("TO_LOCATION");
                    //final EditText start = (EditText) findViewById(R.id.StartPointEditText);
                    //final EditText end = (EditText) findViewById(R.id.EndPointEditText);
                    start.setText(fromLocationName);
                    end.setText(toLocationName);
                } else {
                    //final EditText start = (EditText) findViewById(R.id.StartPointEditText);
                    //final EditText end = (EditText) findViewById(R.id.EndPointEditText);
                    startCoord = markerLatLng.get(0);
                    endCoord = markerLatLng.get(1);
                    start.setText(returnedAddresses.get(0));
                    end.setText(returnedAddresses.get(1));
                }
                returnedDistance = data.getFloatExtra("DISTANCE_FROM_POINTS", 0);

                //Toast.makeText(getBaseContext(),"asdasd" + returnedDistance,Toast.LENGTH_SHORT).show();
                // http://stackoverflow.com/questions/5195837/format-float-to-n-decimal-places
                NumberFormat fareFormat = NumberFormat.getInstance(Locale.CANADA);
                fareFormat.setMaximumFractionDigits(2);
                fareFormat.setMinimumFractionDigits(2);
                fareFormat.setRoundingMode(RoundingMode.HALF_UP);
                //Float roundedFare = new Float(fareFormat.format(((returnedDistance / 1000) * 2.00)));
                fare = RideRequest.getFare(returnedDistance);
                estimatedFare.setText("$" + fareFormat.format(fare));
            }
        }
    }//onActivityResult

    public boolean isConnected(){
        ConnectivityManager cm = (ConnectivityManager) this.getSystemService(this.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return ((activeNetwork != null) && activeNetwork.isConnectedOrConnecting());
    }

    private void saveOffLinePostedRequest(String username){
        String FILENAME = PR_FILE+username+T;
        try {
            FileOutputStream fos = openFileOutput(FILENAME,Context.MODE_PRIVATE);

            BufferedWriter out = new BufferedWriter(new OutputStreamWriter(fos));

            Gson gson = new Gson();
            gson.toJson(offlinePostedRequest, out);
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

    public void checkOfflinePostedRequest(String username){
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
