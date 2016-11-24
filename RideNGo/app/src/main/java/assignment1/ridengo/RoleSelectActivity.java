package assignment1.ridengo;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;

/**
 * The type Role select activity.
 * The user can select to be a driver or to be a rider
 */
public class RoleSelectActivity extends Activity {
    final Activity activity = this;
    private int mBackKeyPressedTimes = 0;
    private RideRequest offlinePostedRequest;
    private static final String PR_FILE = "offlinePostedRequest";
    private static final String T = ".sav";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_role_select);

        UserController.loadUserListFromServer();
        RideRequestController.loadRequestListFromServer();

        final String username = getIntent().getStringExtra("username");
        RideRequestController.notifyUser(username, this);

        if(isConnected()) {
            checkOfflinePostRequest(username);
            if (offlinePostedRequest != null) {
                User r = offlinePostedRequest.getRider();
                r.postRideRequest(offlinePostedRequest);
                Toast.makeText(activity, "Offline request Added, from " + offlinePostedRequest.getStartPoint() + " to " + offlinePostedRequest.getEndPoint(), Toast.LENGTH_SHORT).show();
            }
        }


        Button editInfoButton = (Button) findViewById(R.id.button_EditInfo);
        editInfoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(activity, UserInfoActivity.class);
                intent.putExtra("username", username);
                startActivity(intent);
            }
        });

        Button riderButton = (Button) findViewById(R.id.button_Rider);
        riderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(activity, RiderMainActivity.class);
                intent.putExtra("username", username);
                startActivity(intent);
            }
        });

        Button driverButton = (Button) findViewById(R.id.button_Driver);
        driverButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(UserController.getUserList().getUserByUsername(username).haveVehicle()){
                    Intent intent = new Intent(activity, DriverMainActivity.class);
                    intent.putExtra("username", username);
                    startActivity(intent);
                }
                else{
                    AlertDialog.Builder adb = new AlertDialog.Builder(RoleSelectActivity.this);
                    adb.setMessage("Please add a vehicle before proceeding.");
                    adb.setCancelable(true);
                    adb.setPositiveButton("Add a vehicle", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Intent intent = new Intent(activity,UserInfoActivity.class);
                            intent.putExtra("username",username);
                            startActivity(intent);
                            finish();
                        }
                    });
                    adb.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {}
                    });
                    adb.show();
                }
            }
        });
    }

    @Override
    public void onBackPressed() {

        if (mBackKeyPressedTimes == 0) {
            Toast.makeText(this, "Press again to sign out.", Toast.LENGTH_SHORT).show();
            mBackKeyPressedTimes = 1;
            new Thread() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } finally {
                        mBackKeyPressedTimes = 0;
                    }
                }
            }.start();
            return;
        }
        else{
            Intent intent = new Intent(activity,MainActivity.class);
            startActivity(intent);
            finish();
        }

    }

    public boolean isConnected(){
        ConnectivityManager cm = (ConnectivityManager) this.getSystemService(this.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return ((activeNetwork != null) && activeNetwork.isConnectedOrConnecting());
    }

    public void checkOfflinePostRequest(String username){
        String FILENAME = PR_FILE+username+T;
        try {
            FileInputStream fis = openFileInput(FILENAME);
            BufferedReader in = new BufferedReader(new InputStreamReader(fis));

            Gson gson = new Gson();
            Type rideRequestType = new TypeToken<RideRequest>(){}.getType();

            offlinePostedRequest = gson.fromJson(in, rideRequestType);
            deleteFile(FILENAME);
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            offlinePostedRequest = null;
        } catch (IOException e) {
            // TODO Auto-generated catch block
            throw new RuntimeException();
        }
    }
}
