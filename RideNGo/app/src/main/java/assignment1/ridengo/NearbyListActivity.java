package assignment1.ridengo;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.util.Pair;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class NearbyListActivity extends AppCompatActivity {

    private String username;
    private RideRequest rideRequest;
    private ArrayList<Double> distanceArray = new ArrayList<Double>();
    private List<PairForSearch> pairRequests = new ArrayList<PairForSearch>();
    private List<RideRequest> rideRequests = RideRequestController.getRequestList().getRequests();
    private Integer index = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nearby_list);

        UserController.loadUserListFromServer("{\"from\":0,\"size\":10000,\"query\": { \"match\": { \"username\": \"" + username + "\"}}}");
        RideRequestController.loadRequestListFromServer("{\"from\": 0, \"size\": 10000}");
        Toast.makeText(getBaseContext(), "Test:  " + rideRequests.get(4).getRider(), Toast.LENGTH_SHORT).show();
        //User rider = rideRequests.get(index).getRider();
        pairRequests = (List<PairForSearch>)getIntent().getSerializableExtra("NEARBY_LOCATIONS");
        for(int i = 0; i < pairRequests.size(); i++){
            distanceArray.add(pairRequests.get(i).getValue());
        }

        username = getIntent().getStringExtra("username");
        ListView nearbyRequestsListView = (ListView)findViewById(R.id.NearbyRequestsListView);

        ArrayAdapter<Double> adapter = new ArrayAdapter<Double>(this, android.R.layout.simple_list_item_1, distanceArray);
        nearbyRequestsListView.setAdapter(adapter);

        nearbyRequestsListView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id){
                index = pairRequests.get(position).getIndex();
                Toast.makeText(getBaseContext(), "Index of searched:  " + index, Toast.LENGTH_SHORT).show();
                Toast.makeText(getBaseContext(), "Pair test:  " + pairRequests.get(position).getIndex(), Toast.LENGTH_LONG).show();
                Intent intent = new Intent(NearbyListActivity.this, NearbyListRiderInfoActivity.class);
                Bundle extras = new Bundle();
                //extras.putParcelableArrayList("NEARBY_LOCATIONS",pairRequests);
                //extras.putSerializable("NEARBY_LOCATIONS", (Serializable)pairRequests);
                extras.putInt("INDEX_OF_SEARCHED", index);
                extras.putString("username", username);
                //extras.putStringArrayList("NEARBY_LOCATIONS", (ArrayList<String>)pairRequests);
                intent.putExtras(extras);
                startActivity(intent);


//                AlertDialog.Builder builder = new AlertDialog.Builder(NearbyListActivity.this);
//                builder.setMessage("Hi")
//                        .setTitle("Rider information")
//                        .setPositiveButton("Accept request", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialogInterface, int i) {
//
//                            }
//                        })
//                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialogInterface, int i) {
//                                dialogInterface.cancel();
//                            }
//                        });
//
//                AlertDialog dialog = builder.create();
//                dialog.show();
            }
        });
    }
}
