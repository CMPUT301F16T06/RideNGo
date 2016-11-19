package assignment1.ridengo;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.Button;

/**
 * The type Role select activity.
 * The user can select to be a driver or to be a rider
 */
public class RoleSelectActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_role_select);

        final String username = getIntent().getStringExtra("username");
        final Activity activity = this;

        Button editInfoButton = (Button) findViewById(R.id.button_EditInfo);
        editInfoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(activity, UserInfoActivity.class);
                intent.putExtra("username", username);
                startActivity(intent);
                finish();
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
                            Intent intent = new Intent(activity,UserVehicleInfoActivity.class);
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
}
