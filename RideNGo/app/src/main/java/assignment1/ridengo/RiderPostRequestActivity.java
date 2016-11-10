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

public class RiderPostRequestActivity extends AppCompatActivity {

    private String startPoint;
    private String endPoint;
    private String description;
    private Double fare;
    final private Activity activity = this;

    RideRequestController rideRequestController = new RideRequestController();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rider_post_request);

        final String username = getIntent().getStringExtra("username");
        final UserRider rider = UserController.getUserList().getUserByUsername(username).getRider();
        final EditText start = (EditText) findViewById(R.id.StartPointEditText);
        final EditText end = (EditText) findViewById(R.id.EndPointEditText);

        final TextView estimatedFare = (TextView) findViewById(R.id.estimatedFareTextView);
        description="Test";
        fare = 50.00;

        estimatedFare.setText("$"+fare);

        Button postRequestButton = (Button) findViewById(R.id.postRequestButton);
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



    }
}
