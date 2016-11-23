package assignment1.ridengo;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

/**
 * The type Role select activity.
 * The user can select to be a driver or to be a rider
 */
public class RoleSelectActivity extends Activity {
    final Activity activity = this;
    private int mBackKeyPressedTimes = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_role_select);

        UserController.loadUserListFromServer();
        RideRequestController.loadRequestListFromServer();

        final String username = getIntent().getStringExtra("username");
        RideRequestController.notifyUser(username, this);


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
}
