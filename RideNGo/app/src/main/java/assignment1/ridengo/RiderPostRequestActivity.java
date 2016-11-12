package assignment1.ridengo;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

/**
 * The type Rider post request activity.
 */
public class RiderPostRequestActivity extends AppCompatActivity {

    private static final int RESULT_SUCCESS = 1;
    private String startPoint;
    private String endPoint;
    private String description;
    private Double fare;
    final private Activity activity = this;

    /**
     * The Ride request controller.
     */
    RideRequestController rideRequestController = new RideRequestController();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rider_post_request);

        final String username = getIntent().getStringExtra("username");
        final User rider = UserController.getUserList().getUserByUsername(username);
        final EditText start = (EditText) findViewById(R.id.StartPointEditText);
        final EditText end = (EditText) findViewById(R.id.EndPointEditText);

        final TextView estimatedFare = (TextView) findViewById(R.id.estimatedFareTextView);
        description="Test";
        fare = 50.00;

        estimatedFare.setText("$"+fare);

        Button postRequestButton = (Button) findViewById(R.id.postRequestButton);
        Button findPointsOnMapButton = (Button) findViewById(R.id.FindPointOnMapButton);

        postRequestButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startPoint = start.getText().toString();
                endPoint = end.getText().toString();
                RideRequest rideRequest = new RideRequest(startPoint, endPoint, description, rider,fare);
                rideRequestController.getRequestList().addRequest(rideRequest);
                Toast.makeText(activity, "Request Added, from " + startPoint + " to " + endPoint, Toast.LENGTH_SHORT).show();
                finish();
            }
        });

        findPointsOnMapButton.setOnClickListener(new View.OnClickListener(){

            public void onClick(View v){
                Intent intent = new Intent(RiderPostRequestActivity.this, MapsRiderActivity.class);
                startActivityForResult(intent,RESULT_SUCCESS);
            }
        });
    }

    //https://developer.android.com/training/basics/intents/result.html
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == RESULT_SUCCESS) {
            //Toast.makeText(getBaseContext(),"Error getting location",Toast.LENGTH_SHORT).show();
            //if(resultCode == RESULT_OK){
            ArrayList<String> returnedAddresses = data.getStringArrayListExtra("ARRAY_LIST_ADDRESS_MARKER");
                //String result=data.getStringExtra("result");
            if(returnedAddresses == null){
                returnedAddresses = data.getStringArrayListExtra("ARRAY_LIST_ADDRESS_SEARCHED");
                String fromLocationName = data.getStringExtra("FROM_LOCATION");
                String toLocationName = data.getStringExtra("TO_LOCATION");
                final EditText start = (EditText) findViewById(R.id.StartPointEditText);
                final EditText end = (EditText) findViewById(R.id.EndPointEditText);
                start.setText(fromLocationName);
                end.setText(toLocationName);
            } else {
                final EditText start = (EditText) findViewById(R.id.StartPointEditText);
                final EditText end = (EditText) findViewById(R.id.EndPointEditText);
                start.setText(returnedAddresses.get(0));
                end.setText(returnedAddresses.get(1));
            }
            float returnedDistance = data.getFloatExtra("DISTANCE_FROM_POINTS",0);
            //Toast.makeText(getBaseContext(),"asdasd" + returnedDistance,Toast.LENGTH_SHORT).show();
            final TextView estimatedFare = (TextView) findViewById(R.id.estimatedFareTextView);
            estimatedFare.setText("$"+ returnedDistance);


            //}
           // if (resultCode == Activity.RESULT_CANCELED) {
                //Write your code if there's no result
            //}
        }
    }//onActivityResult

}
