package assignment1.ridengo;

import android.app.Activity;

import android.app.Dialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;
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

        final ListView listView = (ListView) findViewById(R.id.RequestDetailListView);

        final List<UserDriver> driverList = rideRequest.getAcceptions();
        adapter = new ArrayAdapter<UserDriver>(activity, android.R.layout.simple_list_item_1, driverList);
        listView.setAdapter(adapter);

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

            @Override
            public boolean onItemLongClick(AdapterView<?> adapter, View view, int pos, long arg) {
                Dialog dialog = new Dialog(RiderRequestDetailActivity.this);
                dialog.setTitle("Driver Information");
                dialog.setContentView(R.layout.dialog_driver_info);

                // get info on that driver at that position
                // fill in username, phone, email
                TextView driverUsername = (TextView) dialog.findViewById(R.id.driverUsername);
                TextView driverPhone = (TextView) dialog.findViewById(R.id.driverPhone);
                TextView driverEmail = (TextView) dialog.findViewById(R.id.driverEmail);

                driverUsername.setText(driverList.get(pos).getUser().getUsername());
                driverPhone.setText(driverList.get(pos).getUser().getPhoneNum());
                driverEmail.setText(driverList.get(pos).getUser().getEmail());


                // make this the confirmation button instead?
                Button okButton = (Button) dialog.findViewById(R.id.okButton);
                okButton.setOnClickListener(new View.OnClickListener(){
                    public void onClick(View v){
                        onBackPressed();
                    }
                });
                dialog.show();
                return true;
            }

        });

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
