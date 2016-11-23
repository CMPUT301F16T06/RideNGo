package assignment1.ridengo;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.regex.Pattern;

/**
 * The type User info activity.
 * Able to let user to update his/her information
 */
public class UserInfoActivity extends Activity {
    protected String user;
    final Activity activity = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);

        UserController.loadUserListFromServer();
        RideRequestController.loadRequestListFromServer();

        final TextView usernameTextView = (TextView) findViewById(R.id.UserNameTextView);
        final EditText usernameText = (EditText) findViewById(R.id.editText_EnterUsername);
        final EditText emailText = (EditText) findViewById(R.id.editText_EnterEmail);
        final EditText phoneNumText = (EditText) findViewById(R.id.editText_EnterPhoneNum);
        final TextView vehicleInfoText = (TextView) findViewById(R.id.vehicleInfoTextView);
        final Button addVehicleButton = (Button) findViewById(R.id.button_add_vehicle);

        phoneNumText.addTextChangedListener(new PhoneNumberFormattingTextWatcher());

        user = getIntent().getStringExtra("username");
        if(!user.isEmpty()){

            User currentUser = null;
            currentUser = UserController.getUserList().getUserByUsername(user);
            usernameTextView.setText("Username:");
            usernameText.setText(currentUser.getUsername());
            usernameText.setEnabled(false);
            emailText.setText(currentUser.getEmail());
            phoneNumText.setText(currentUser.getPhoneNum());
            if(currentUser.haveVehicle()) {
                vehicleInfoText.setText(currentUser.getVehicle().toString());
                addVehicleButton.setText("Edit Vehicle");
            }

            RideRequestController.notifyUser(user, this);
        }


        addVehicleButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                String username = usernameText.getText().toString().trim();
                String email = emailText.getText().toString().trim();
                String phoneNum = phoneNumText.getText().toString();

                if(!signUp(username,email,phoneNum)){
                    return;
                }

                Intent intent = new Intent(activity, UserVehicleInfoActivity.class);
                intent.putExtra("username", username);
                startActivity(intent);
                finish();
            }
        });

        Button signUpButton = (Button) findViewById(R.id.button_SignUpMain);
        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UserController.loadUserListFromServer();
                RideRequestController.loadRequestListFromServer();

                String username = usernameText.getText().toString().trim();
                String email = emailText.getText().toString().trim();
                String phoneNum = phoneNumText.getText().toString();

                if(!signUp(username,email,phoneNum)){
                    return;
                }

                Intent intent = new Intent(activity, RoleSelectActivity.class);
                intent.putExtra("username", username);
                startActivity(intent);
                finish();
//
//                try{
//                    UserController.addUser(currentUser);
//
//                }
//                catch (RuntimeException e){
//                    if(user.isEmpty()) {
//                        Toast.makeText(getApplicationContext(), "User Already Exists.", Toast.LENGTH_SHORT).show();
//                    }
//                    else {
//                        if(currentUser.getId() != null)
//                            Toast.makeText(activity, currentUser.getId(), Toast.LENGTH_SHORT).show();
//                        UserController.updateUser(currentUser);
//
//                    }
//                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder adb = new AlertDialog.Builder(this);
        adb.setMessage("Are you sure to get back to the previous page? Information updated may be lost.");
        adb.setCancelable(true);
        adb.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent intent = new Intent(UserInfoActivity.this,RoleSelectActivity.class);
                intent.putExtra("username",user);
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

    public boolean signUp(String username, String email, String phoneNum){

        String emailPattern = "\\b[\\w.%-]+@[-.\\w]+\\.[A-Za-z]{2,4}\\b";

        if(username.contains(" ")){
            Toast.makeText(UserInfoActivity.this,"Invalid username, please try again",Toast.LENGTH_SHORT).show();
            return false;
        }
        if(!Pattern.matches(emailPattern,email)){
            Toast.makeText(UserInfoActivity.this,"Invalid email address, please try again",Toast.LENGTH_SHORT).show();
            return false;
        }
        if(phoneNum.length()<14){
            Toast.makeText(UserInfoActivity.this,"Invalid phone number, please try again",Toast.LENGTH_SHORT).show();
            return false;
        }

        User currentUser = UserController.getUserList().getUserByUsername(username);
        if(currentUser == null) {
            currentUser = new User(username, email, phoneNum);
            UserController.addUser(currentUser);
        } else if(user.isEmpty()) {
            Toast.makeText(activity, "User Already Exists.", Toast.LENGTH_SHORT).show();
            return false;
        }
        else{
//            currentUser.setEmail(email);
//            currentUser.setPhoneNum(phoneNum);
        }
        return true;
    }
}
