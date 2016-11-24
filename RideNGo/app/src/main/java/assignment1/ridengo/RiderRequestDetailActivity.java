package assignment1.ridengo;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;

import java.util.List;

/**
 * The type Rider request detail activity.
 * Rider able to see the detail for the requests he/she posted.
 * The rider can select the driver, confirm the driver and/or cancel the request
 */
public class RiderRequestDetailActivity extends AppCompatActivity {

    private int position;
    private RideRequest rideRequest;

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

        String username = getIntent().getStringExtra("username");
        RideRequestController.notifyUser(username, this);
        position = getIntent().getIntExtra("position", 0);

        rideRequest = RideRequestController.getRequestList().getRequestsWithRider(username).get(position);

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
                final int id = (int)arg;
                Dialog dialog = new Dialog(RiderRequestDetailActivity.this);
                dialog.setContentView(R.layout.dialog_driver_info);

                TextView driverUsername = (TextView) dialog.findViewById(R.id.driverUsername);
                TextView driverPhone = (TextView) dialog.findViewById(R.id.driverPhone);
                TextView driverEmail = (TextView) dialog.findViewById(R.id.driverEmail);
                TextView driverVehicle = (TextView) dialog.findViewById(R.id.driverVehicle);
                TextView driverRate = (TextView) dialog.findViewById(R.id.driverRating);

                driverUsername.setText(driverList.get(pos).getUsername());
                driverPhone.setText(driverList.get(pos).getPhoneNum());
                driverEmail.setText(driverList.get(pos).getEmail());
                driverVehicle.setText(driverList.get(pos).getVehicle().toString());
                driverRate.setText(String.valueOf(driverList.get(pos).getRating()));

                Button okButton = (Button) dialog.findViewById(R.id.okButton);
                if (!rideRequest.getStatus().equals("Accepted By Driver")) {
                    okButton.setEnabled(false);
                }
                else{
                    okButton.setOnClickListener(new View.OnClickListener(){
                        public void onClick(View v){
                            User driver = driverList.get(id); //UserController.getUserList().getUserByUsername(username).getDriver();
                            rideRequest.getRider().confirmAcception(rideRequest,driver);
                            finish();
                        }
                    });
                }

                dialog.show();
            }

        });

        Button cancelRequestButton = (Button) findViewById(R.id.RequestDetailCancelRequestButton);
        if (rideRequest.getStatus().equals("Driver Confirmed")||rideRequest.getStatus().equals("Trip Completed")) {
            cancelRequestButton.setEnabled(false);
        }
        else {
            cancelRequestButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    rideRequest.getRider().cancelRequest(rideRequest);
                    finish();
                }
            });
        }

        Button confirmButton = (Button) findViewById(R.id.RequestDetailConfirmButton);

        if (!rideRequest.getStatus().equals("Driver Confirmed")){
            confirmButton.setEnabled(false);
        }
        else{
            confirmButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    final Dialog rateDialog = new Dialog(RiderRequestDetailActivity.this);
                    rateDialog.setContentView(R.layout.dialog_rate_driver);

                    TextView driverUser = (TextView) rateDialog.findViewById(R.id.driverUser);
                    driverUser.setText(rideRequest.getDriver().getUsername());

                    Button rateButton = (Button) rateDialog.findViewById(R.id.rateButton);

                    // Existing rating
                    final float driverRating = rideRequest.getDriver().getRating();
                    final int numRatings = rideRequest.getDriver().getNumRatings();


                    rateButton.setOnClickListener(new View.OnClickListener() {
                        public void onClick(View v) {
                            RatingBar rateDriver = (RatingBar) rateDialog.findViewById(R.id.rateDriverBar);
                            // User rates driver
                            float userRating = rateDriver.getRating();
                            float avgRating = (driverRating + userRating)/(float)(numRatings + 1);
                            //rideRequest.getDriver().setRating(avgRating);
                            //rideRequest.getDriver().setNumRatings(numRatings + 1);
                            //UserController.getUserList().getUserByUsername().setRating();


                            // Uncomment later
                            //rideRequest.getRider().riderCompleteRide(rideRequest);
                            rateDialog.cancel();
                        }
                    });

                    rateDialog.show();
                }

            });
        }


    }
}
