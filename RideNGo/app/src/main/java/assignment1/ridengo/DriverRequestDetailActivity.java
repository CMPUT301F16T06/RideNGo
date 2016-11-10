package assignment1.ridengo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class DriverRequestDetailActivity extends AppCompatActivity {

    private String username;
    private int hash;
    private ArrayAdapter<String> adapter;
    private RideRequest rideRequest;
    private ArrayList<String> info = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_request_detail);

        username = getIntent().getStringExtra("username");
        hash = getIntent().getIntExtra("hash",0);
        final UserDriver driver = UserController.getUserList().getUserByUsername(username).getDriver();
        rideRequest = RideRequestController.getRequestList().getRequestWithHash(hash);
        getInfo();

        ListView requestDetailListView = (ListView)findViewById(R.id.RequestDetailListView);
        Button acceptButton = (Button)findViewById(R.id.AcceptButton);

        adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,info);
        requestDetailListView.setAdapter(adapter);

        if(rideRequest.isAccepted(driver)){
            acceptButton.setText("Accepted");
            acceptButton.setEnabled(false);
        }
        else {
            acceptButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(DriverRequestDetailActivity.this, "Give him/her a ride!", Toast.LENGTH_SHORT).show();
                    RideRequestController.getRequestList().getRequestWithHash(hash).addAcception(driver);
                    finish();
                }
            });
        }
        requestDetailListView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id){
                if(id == 3){
                    Toast.makeText(DriverRequestDetailActivity.this,"Email rider",Toast.LENGTH_SHORT).show();
                }
                else if(id == 4) {
                    Toast.makeText(DriverRequestDetailActivity.this, "Call rider", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void getInfo(){
        info.clear();
        User rider = rideRequest.getRider().getUser();
        info.add("Start: " + rideRequest.getStartPoint().toString());
        info.add("End: " + rideRequest.getEndPoint().toString());
        info.add("Name: " + rider.getUsername());
        info.add("Email: " + rider.getEmail());
        info.add("Phone: " + rider.getPhoneNum());
    }
}
