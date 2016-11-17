package assignment1.ridengo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);

        final Activity activity = this;

        final EditText usernameText = (EditText) findViewById(R.id.editText_EnterUsername);
        final EditText emailText = (EditText) findViewById(R.id.editText_EnterEmail);
        final EditText phoneNumText = (EditText) findViewById(R.id.editText_EnterPhoneNum);
        final TextView vehicleInfoText = (TextView) findViewById(R.id.vehicleInfoTextView);

        phoneNumText.addTextChangedListener(new PhoneNumberFormattingTextWatcher());

        final String user = getIntent().getStringExtra("username");
        if(!user.isEmpty()){
            User currentUser = UserController.getUserList().getUserByUsername(user);
            usernameText.setText(currentUser.getUsername());
            usernameText.setEnabled(false);
            emailText.setText(currentUser.getEmail());
            phoneNumText.setText(currentUser.getPhoneNum());
            if(!(currentUser.getVehicle() == null)) {
                vehicleInfoText.setText(currentUser.getVehicle().toString());
            }
        }

        Button addVehicleButton = (Button) findViewById(R.id.button_add_vehicle);
        addVehicleButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Toast.makeText(UserInfoActivity.this,"addVehicleActivity",Toast.LENGTH_SHORT).show();
            }
        });

        Button signupButton = (Button) findViewById(R.id.button_SignUpMain);
        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = usernameText.getText().toString().trim();
                String email = emailText.getText().toString().trim();
                String phoneNum = phoneNumText.getText().toString();

                String emailPattern = "\\b[\\w.%-]+@[-.\\w]+\\.[A-Za-z]{2,4}\\b";

                if(username.contains(" ")){
                    Toast.makeText(UserInfoActivity.this,"Invalid username, please try again",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(!Pattern.matches(emailPattern,email)){
                    Toast.makeText(UserInfoActivity.this,"Invalid email address, please try again",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(phoneNum.length()<14){
                    Toast.makeText(UserInfoActivity.this,"Invalid phone number, please try again",Toast.LENGTH_SHORT).show();
                    return;
                }


                User currentUser = UserController.getUserList().getUserByUsername(username);
                if(currentUser == null) {
                    currentUser = new User(username, email, phoneNum);
                    UserController.addUser(currentUser);
                } else if(user.isEmpty()) {
                    Toast.makeText(activity, "User Already Exists.", Toast.LENGTH_SHORT).show();
                    return;
                }
                else{
                    currentUser.setEmail(email);
                    currentUser.setPhoneNum(phoneNum);
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
}
