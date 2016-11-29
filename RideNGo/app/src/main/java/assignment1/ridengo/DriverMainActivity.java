package assignment1.ridengo;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * The type Driver main activity.
 */
public class DriverMainActivity extends Activity {
    private String username;

    private final Activity activity = this;
    private RideRequest offlineAcceptedRequest;
    private static final String AR_FILE = "offlineAcceptedRequest";
    private static final String T = ".sav";
    private static String filter = null;
    private static float filter_value;
    private static String operator = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_main);

        username = getIntent().getStringExtra("username");
        RideRequestController.notifyUser(username, this);

        if(isConnected()){
            UserController.loadUserListFromServer("{\"from\":0,\"size\":10000,\"query\": { \"match\": { \"username\": \"" + username + "\"}}}");
            //RideRequestController.loadRequestListFromServer();
            checkOfflineAcceptedRequest(username);
            if(offlineAcceptedRequest != null){
                int offlineRequestId = offlineAcceptedRequest.getId();
                RideRequest refreshedOfflineRequest = RideRequestController.getRequestList().getRequestById(offlineRequestId);
                if(refreshedOfflineRequest.getStatus().equals("Waiting for Driver") || refreshedOfflineRequest.getStatus().equals("Waiting for Confirmation") ) {
                    User r = UserController.getUserList().getUserByUsername(username);
                    r.acceptRequest(refreshedOfflineRequest);
                    offlineAcceptedRequest = null;
                    Toast.makeText(this, "The request you accepted while offline is now accepted. You can check it in VIEW ACCEPTED.", Toast.LENGTH_SHORT).show();
                }
                else{
                    offlineAcceptedRequest = null;
                    Toast.makeText(this, "Sorry, the request you accepted while offline is no longer available.", Toast.LENGTH_SHORT).show();
                }
            }
        }

        Button filterButton = (Button) findViewById(R.id.FilterButton);
        filterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Dialog dialog = new Dialog(DriverMainActivity.this);
                dialog.setTitle("Filter");
                dialog.setContentView(R.layout.dialog_filter);

                final RadioButton rideRadioButton = (RadioButton) dialog.findViewById(R.id.RideRadioButton);
                final RadioButton kmRadioButton = (RadioButton) dialog.findViewById(R.id.KmRadioButton);
                final Spinner optionSpinner = (Spinner) dialog.findViewById(R.id.OptionSpinner);
                final EditText valueEditText = (EditText) dialog.findViewById(R.id.ValueEditText);
                final Button applyButton = (Button) dialog.findViewById(R.id.AppleButton);
                final Button removeButton = (Button) dialog.findViewById(R.id.buttonRemoveFilter);

                ArrayList<String> arrayOp = new ArrayList<String>();
                arrayOp.add("greater than");
                arrayOp.add("equal to");
                arrayOp.add("less than");
                ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(activity, R.layout.support_simple_spinner_dropdown_item, arrayOp);
                optionSpinner.setAdapter(spinnerAdapter);

                applyButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(!rideRadioButton.isChecked() && !kmRadioButton.isChecked()){
                            Toast.makeText(activity,"Please select a method of filter",Toast.LENGTH_SHORT).show();
                            return;
                        }
                        else if(valueEditText.getText().toString().equals("")){
                            Toast.makeText(activity,"Please specify a range",Toast.LENGTH_SHORT).show();
                            return;
                        }
                        else {
                            if (rideRadioButton.isChecked()) {
                                filter = "filter per ride";
                            } else if (kmRadioButton.isChecked()) {
                                filter = "filter per km";
                            }
                            filter_value = Float.valueOf(valueEditText.getText().toString());
                            operator = optionSpinner.getSelectedItem().toString();
                            dialog.cancel();
                        }
                    }
                });

                removeButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        filter = null;
                        dialog.cancel();
                    }
                });
                dialog.show();
            }
        });


        /*
        Set up all buttons, ListView, and adapter here
        When the activity starts, a suggested request list is shown in list view. The suggested list
        should be the same thing as 'findNearbyButton' returns [except those has been accepted (optional)].
         */
        Button searchButton = (Button) findViewById(R.id.SearchButton);
        Button findNearbyButton = (Button) findViewById(R.id.FindNearbyButton);
        Button viewAcceptedButton = (Button) findViewById(R.id.ViewAcceptedButton);
        final EditText searchText = (EditText) findViewById(R.id.SearchTextView);
        final ListView requestListView = (ListView) findViewById(R.id.DriverRequestListView);
        final List<RideRequest> rideRequestList = new ArrayList<>();
        final ArrayAdapter<RideRequest> adapter = new ArrayAdapter<RideRequest>(activity, android.R.layout.simple_list_item_1, rideRequestList);
        requestListView.setAdapter(adapter);

        viewAcceptedButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent = new Intent(activity, DriverAcceptedListActivity.class);
                intent.putExtra("username", username);
                startActivity(intent);
            }
        });

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rideRequestList.clear();
                String search = searchText.getText().toString().trim().toLowerCase();
                String query;
                if(search.isEmpty()) {
                    query = "{\n" +
                            "   \"query\" : {\n" +
                            "      \"filtered\" : { \n" +
                            "         \"query\" : {\n" +
                            "            \"bool\" : {\n" +
                            "          \t  \"must\" :[{\"match\":{\"status\":\"Waiting\"}}\n" +
                            "          \t  ]\n" +
                            "           }\n" +
                            "         }\n" +
                            "      }\n" +
                            "   }\n" +
                            "}";
                } else {
                    query = "{\n" +
                            "   \"query\" : {\n" +
                            "      \"filtered\" : { \n" +
                            "         \"query\" : {\n" +
                            "            \"bool\" : {\n" +
                            "          \t  \"must\" :[{\"match\":{\"status\":\"Waiting\"}},\n" +
                            "          \t  \t{\"wildcard\":{\"description\":\""+ search + "\"}}\n" +
                            "          \t  ]\n" +
                            "           }\n" +
                            "         }\n" +
                            "      }\n" +
                            "   }\n" +
                            "}";
                }
                RideRequestController.loadRequestListFromServer(query);
                if(filter == null) {
                    rideRequestList.addAll(RideRequestController.getRequestList().getRequests());
                } else if(filter.equals("filter per ride")) {
                    for(RideRequest request : RideRequestController.getRequestList().getRequests()) {
                        if(operator.equals("greater than")) {
                            if(request.getFare() > filter_value) {
                                rideRequestList.add(request);
                            }
                        } else if(operator.equals("equal to")) {
                            if(request.getFare() == filter_value) {
                                rideRequestList.add(request);
                            }
                        } else if(operator.equals("less than")) {
                            if(request.getFare() < filter_value) {
                                rideRequestList.add(request);
                            }
                        }
                    }
                } else if(filter.equals("filter per km")) {
                    for(RideRequest request : RideRequestController.getRequestList().getRequests()) {
                        if(operator.equals("greater than")) {
                            if(request.getFare() / (request.getDistance() / 1000) > filter_value) {
                                rideRequestList.add(request);
                            }
                        } else if(operator.equals("equal to")) {
                            if(request.getFare() / (request.getDistance() / 1000) == filter_value) {
                                rideRequestList.add(request);
                            }
                        } else if(operator.equals("less than")) {
                            if(request.getFare() / (request.getDistance() / 1000) < filter_value) {
                                rideRequestList.add(request);
                            }
                        }
                    }
                }

                adapter.notifyDataSetChanged();
            }
        });

        findNearbyButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                if(isConnected()) {
                    Intent intent = new Intent(DriverMainActivity.this, MapsDriverSearchActivity.class);
                    intent.putExtra("username", username);
                    startActivity(intent);
                }
                else {
                    Toast.makeText(activity,"You are offine now, please check your network status.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        requestListView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id){
                Intent intent = new Intent(activity, DriverRequestDetailActivity.class);
                intent.putExtra("username",username);
                RideRequest request = (RideRequest) requestListView.getItemAtPosition(position);
                intent.putExtra("id", request.getId());
                startActivity(intent);
                finish();
            }
        });

    }

    /**
     * Is connected boolean.
     *
     * @return the boolean
     */
    public boolean isConnected(){
        ConnectivityManager cm = (ConnectivityManager) this.getSystemService(this.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return ((activeNetwork != null) && activeNetwork.isConnectedOrConnecting());
    }

    /**
     * Check offline accepted request.
     *
     * @param username the username
     */
    public void checkOfflineAcceptedRequest(String username){
        String FILENAME = AR_FILE+username+T;
        try {
            FileInputStream fis = openFileInput(FILENAME);
            BufferedReader in = new BufferedReader(new InputStreamReader(fis));

            Gson gson = new Gson();
            Type rideRequestType = new TypeToken<RideRequest>(){}.getType();

            offlineAcceptedRequest = gson.fromJson(in, rideRequestType);
            deleteFile(FILENAME);
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            offlineAcceptedRequest = null;
        } catch (IOException e) {
            // TODO Auto-generated catch block
            throw new RuntimeException();
        }
    }
}