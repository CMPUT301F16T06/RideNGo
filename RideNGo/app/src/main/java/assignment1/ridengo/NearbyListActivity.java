package assignment1.ridengo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * This is the activity with an array adapter that shows nearby locations relative to the marker
 * placed on the map in the activity before this(MapsDriverSearchActivity). Locations can be picked
 * to see their information.
 * @see MapsDriverSearchActivity
 * @see NearbyListRiderInfoActivity
 */
public class NearbyListActivity extends AppCompatActivity {

    private String username;
    private ArrayList<Double> distanceArray = new ArrayList<Double>();
    private List<PairForSearch> pairRequests = new ArrayList<PairForSearch>();
    private Integer index = null;

    /**
     * Called upon when activity is first created. Load users and requests with their respective
     * controllers and takes the distance between the marker and start locations from the previous
     * map activity. Locations are sorted and displayed from shortest distance to largest distance.
     * @see UserController
     * @see RideRequestController
     * @see MapsDriverSearchActivity
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nearby_list);
        UserController.loadUserListFromServer("{\"from\":0,\"size\":10000,\"query\": { \"match\": { \"username\": \"" + username + "\"}}}");
        RideRequestController.loadRequestListFromServer("{\"from\": 0, \"size\": 10000}");

        pairRequests = (List<PairForSearch>)getIntent().getSerializableExtra("NEARBY_LOCATIONS");
        // Sort the requests from shortest to largest distance
        for(int i = 0; i < pairRequests.size(); i++){
            distanceArray.add(pairRequests.get(i).getValue());
        }

        username = getIntent().getStringExtra("username");
        ListView nearbyRequestsListView = (ListView)findViewById(R.id.NearbyRequestsListView);
        ArrayAdapter<Double> adapter = new ArrayAdapter<Double>(this, android.R.layout.simple_list_item_1, distanceArray);
        nearbyRequestsListView.setAdapter(adapter);

        /**
         * The listener is used to detect when any of the location distances are clicked on.
         * This will lead to another page with more details about the location
         * @see NearbyListRiderInfoActivity
         */
        nearbyRequestsListView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id){
                index = pairRequests.get(position).getIndex();
                Intent intent = new Intent(NearbyListActivity.this, NearbyListRiderInfoActivity.class);
                Bundle extras = new Bundle();
                extras.putInt("INDEX_OF_SEARCHED", index);
                extras.putString("username", username);
                intent.putExtras(extras);
                startActivity(intent);
            }
        });
    }
}
