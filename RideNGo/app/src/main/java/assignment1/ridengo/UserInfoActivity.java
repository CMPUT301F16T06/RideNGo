package assignment1.ridengo;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

/**
 * The type User info activity.
 * Able to let user to update his/her information
 */
public class UserInfoActivity extends Activity {
    private String user;
    private Vehicle vehicle;
    final Activity activity = this;
    final List<String> colorList = Arrays.asList("Black","White","Silver","Brown","Grey","Red","Blue","Yellow","Green","Other");
    private User currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);

        RideRequestController.loadRequestListFromServer("{\"from\": 0, \"size\": 10000}");

        user = getIntent().getStringExtra("username");
        UserController.loadUserListFromServer("{\"from\":0,\"size\":10000,\"query\": { \"match\": { \"username\": \"" + user + "\"}}}");

        final TextView usernameTextView = (TextView) findViewById(R.id.UserNameTextView);
        final EditText usernameText = (EditText) findViewById(R.id.editText_EnterUsername);
        final EditText emailText = (EditText) findViewById(R.id.editText_EnterEmail);
        final EditText phoneNumText = (EditText) findViewById(R.id.editText_EnterPhoneNum);
        final TextView vehicleInfoText = (TextView) findViewById(R.id.vehicleInfoTextView);
        final Button addVehicleButton = (Button) findViewById(R.id.button_add_vehicle);
        Button signUpButton = (Button) findViewById(R.id.button_SignUpMain);

        phoneNumText.addTextChangedListener(new PhoneNumberFormattingTextWatcher());

        if(!user.isEmpty()){

            currentUser = UserController.getUserList().getUserByUsername(user);
            usernameTextView.setText("Username:");
            usernameText.setText(currentUser.getUsername());
            usernameText.setEnabled(false);
            emailText.setText(currentUser.getEmail());
            phoneNumText.setText(currentUser.getPhoneNum());
            if(currentUser.haveVehicle()) {
                vehicle = currentUser.getVehicle();
                vehicleInfoText.setText(currentUser.getVehicle().toString());
                addVehicleButton.setText("Edit Vehicle");
            }

            RideRequestController.notifyUser(user, this);
        }

        addVehicleButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                final Dialog dialog = new Dialog(UserInfoActivity.this);
                dialog.setTitle("Vehicle Information");
                dialog.setContentView(R.layout.dialog_user_vehicle_info);

                final Spinner yearSpinner = (Spinner) dialog.findViewById(R.id.YearSpinner);
                final Spinner colorSpinner = (Spinner) dialog.findViewById(R.id.ColorSpinner);
                final EditText makeText = (EditText) dialog.findViewById(R.id.MakeEditText);
                final EditText modelText = (EditText) dialog.findViewById(R.id.ModelEditText);
                final EditText pNumText = (EditText) dialog.findViewById(R.id.PNumEditText);
                Button saveButton = (Button) dialog.findViewById(R.id.SaveButton);
                Button deleteButton = (Button) dialog.findViewById(R.id.DeleteButton);

                if(!(vehicle == null)){
                    yearSpinner.setSelection(2017-vehicle.getYear());
                    colorSpinner.setSelection(colorList.indexOf(vehicle.getColor()));
                    makeText.setText(vehicle.getMake());
                    modelText.setText(vehicle.getModel());
                    pNumText.setText(vehicle.getPlateNum());
                    deleteButton.setEnabled(true);
                }
                else{
                    deleteButton.setEnabled(false);
                }
                /*
                if(!user.isEmpty()){
                    if(currentUser.haveVehicle()){
                        Vehicle userVehicle = currentUser.getVehicle();
                        yearSpinner.setSelection(2017-userVehicle.getYear());
                        colorSpinner.setSelection(colorList.indexOf(userVehicle.getColor()));
                        makeText.setText(userVehicle.getMake());
                        modelText.setText(userVehicle.getModel());
                        pNumText.setText(userVehicle.getPlateNum());
                        deleteButton.setEnabled(true);
                    }
                    else{
                        deleteButton.setEnabled(false);
                    }

                }*/

                saveButton.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View view){
                        String pNum = pNumText.getText().toString().trim();
                        String year = yearSpinner.getSelectedItem().toString();
                        int y;
                        if(year.equals("2005/before")){
                            y = 2005;
                        }
                        else{
                            y = Integer.parseInt(year);
                        }
                        String color = colorSpinner.getSelectedItem().toString();
                        String make = makeText.getText().toString().trim();
                        String model = modelText.getText().toString().trim();
                        vehicle = new Vehicle(pNum,y,make,model,color);
                        vehicleInfoText.setText(vehicle.toString());
                        dialog.cancel();
                    }
                });

                deleteButton.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View view){
                        AlertDialog.Builder adb = new AlertDialog.Builder(activity);
                        adb.setMessage("Do you want to delete this vehicle?");
                        adb.setCancelable(true);
                        adb.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                vehicle = null;
                                dialog.cancel();
                                vehicleInfoText.setText("No vehicle information\nPlease add vehicle if you want to be a driver");
                            }
                        });
                        adb.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {}
                        });
                        adb.show();
                    }
                });

                dialog.show();
            }
        });


        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String username = usernameText.getText().toString().trim();
                String email = emailText.getText().toString().trim();
                String phoneNum = phoneNumText.getText().toString();
                UserController.loadUserListFromServer("{\"from\":0,\"size\":10000,\"query\": { \"match\": { \"username\": \"" + username + "\"}}}");

                if(!signUp(username,email,phoneNum)){
                    return;
                }
                Intent intent = new Intent(activity,RoleSelectActivity.class);
                intent.putExtra("username",username);
                startActivity(intent);
                finish();
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
                if(user.isEmpty()){
                    Intent intent = new Intent(UserInfoActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }
                else {
                    finish();
                }
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

        if(username.equals("")||email.equals("")||phoneNum.equals("")){
            Toast.makeText(UserInfoActivity.this,"Username, phone number, and email cannot be empty",Toast.LENGTH_SHORT).show();
        }

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

        if(currentUser == null) {
            currentUser = new User(username, email, phoneNum);
            currentUser.setVehicle(vehicle);
            try{
                UserController.addUser(currentUser);
            }catch (RuntimeException e) {
                Toast.makeText(activity, "User Already Exists.", Toast.LENGTH_SHORT).show();
                return false;
            }
        } else if(user.isEmpty()) {
            Toast.makeText(activity, "User Already Exists.", Toast.LENGTH_SHORT).show();
            return false;
        }
        else{
            currentUser.setEmail(email);
            currentUser.setPhoneNum(phoneNum);
            currentUser.setVehicle(vehicle);
        }
        return true;
    }
}
