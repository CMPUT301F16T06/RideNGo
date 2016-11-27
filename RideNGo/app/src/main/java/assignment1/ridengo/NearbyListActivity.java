package assignment1.ridengo;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.v4.util.Pair;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class NearbyListActivity extends AppCompatActivity {
    private RideRequest rideRequest;
    private ArrayList<Double> distanceArray = new ArrayList<Double>();
    private List<PairForSearch> pairRequests = new ArrayList<PairForSearch>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nearby_list);
        UserController.loadUserListFromServer();
        RideRequestController.loadRequestListFromServer("{\"from\": 0, \"size\": 10000}");
        pairRequests = (List<PairForSearch>)getIntent().getSerializableExtra("NEARBY_LOCATIONS");
        for(int i = 0; i < pairRequests.size(); i++){
            distanceArray.add(pairRequests.get(i).getValue());
        }

        ListView nearbyRequestsListView = (ListView)findViewById(R.id.NearbyRequestsListView);
        Button acceptButton = (Button)findViewById(R.id.AcceptButton);

        ArrayAdapter<Double> adapter = new ArrayAdapter<Double>(this, android.R.layout.simple_list_item_1, distanceArray);
        nearbyRequestsListView.setAdapter(adapter);

        nearbyRequestsListView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id){

                AlertDialog.Builder builder = new AlertDialog.Builder(NearbyListActivity.this);
                builder.setMessage("Hi")
                        .setTitle("Rider information")
                        .setPositiveButton("Accept request", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.cancel();
                            }
                        });

                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });
    }
}
