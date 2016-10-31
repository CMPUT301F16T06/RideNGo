package assignment1.ridengo;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.List;

public class RiderRequestDetailActivity extends AppCompatActivity {

    private ArrayAdapter<UserDriver> adapter;
    private String username;
    private int position;
    private RideRequest rideRequest;

    final Activity activity = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_detail);

        username = getIntent().getStringExtra("username");
        position = getIntent().getIntExtra("position", 0);

        rideRequest = RideRequestController.getRequestList().getRequestsWithRider(username).get(position);

        TextView startPoint = (TextView) findViewById(R.id.RequestDetailStartPointTextView);
        startPoint.setText(rideRequest.getStartPoint());

        TextView endPoint = (TextView) findViewById(R.id.RequestDetailEndPointTextView);
        endPoint.setText(rideRequest.getEndPoint());

        TextView status = (TextView) findViewById(R.id.RequestDetailCurrentStatusTextView);
        status.setText(rideRequest.getStatus().toString());

        ListView listView = (ListView) findViewById(R.id.RequestDetailListView);

        List<UserDriver> driverList = rideRequest.getAcceptions();
        adapter = new ArrayAdapter<UserDriver>(activity, android.R.layout.simple_list_item_1, driverList);
        listView.setAdapter(adapter);

        Button cancelRequestButton = (Button) findViewById(R.id.RequestDetailCancelRequestButton);
        cancelRequestButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RideRequestController.getRequestList().removeRequest(position);
                finish();
            }
        });

        Button confirmButton = (Button) findViewById(R.id.RequestDetailConfirmButton);
        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


    }
}
