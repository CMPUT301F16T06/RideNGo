package assignment1.ridengo;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.TwoLineListItem;

import java.util.List;

import static android.graphics.Color.BLACK;
import static android.graphics.Color.GRAY;

/**
 * The type Driver accepted list activity.
 */
public class DriverAcceptedListActivity extends AppCompatActivity {

    private String username;
    private ArrayAdapter<RideRequest> adapter;
    /**
     * The Activity.
     */
    final Activity activity = this;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_accepted_list);

        username = getIntent().getStringExtra("username");

        ListView acceptedListView = (ListView) findViewById(R.id.AcceptedListView);
        final List<RideRequest> requestList = RideRequestController.getRequestList().getRequestsWithDriver(username);
        adapter = new ArrayAdapter<RideRequest>(this,android.R.layout.simple_list_item_1,requestList){
            @Override
            public View getView(int position, View convertView, ViewGroup parent){
                TwoLineListItem row;
                if(convertView == null){
                    LayoutInflater inflater = (LayoutInflater)getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    row = (TwoLineListItem)inflater.inflate(android.R.layout.simple_list_item_2, null);
                }else{
                    row = (TwoLineListItem)convertView;
                }
                RideRequest request = requestList.get(position);
                row.getText1().setText(request.toString());
                row.getText1().setTextColor(BLACK);
                row.getText2().setText(request.getStatus());
                row.getText2().setTextColor(GRAY);
                return row;
            }
        };
        acceptedListView.setAdapter(adapter);

        acceptedListView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id){
                Intent intent = new Intent(activity, DriverRequestDetailActivity.class);
                int hash = requestList.get((int)id).hashCode();
                intent.putExtra("username",username);
                intent.putExtra("hash", hash);
                startActivity(intent);
            }
        });
    }
}
