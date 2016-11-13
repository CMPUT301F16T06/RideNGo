package assignment1.ridengo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 * The type User info activity.
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

        final String user = getIntent().getStringExtra("username");
        if(!user.isEmpty()){
            User currentUser = UserController.getUserList().getUserByUsername(user);
            usernameText.setText(currentUser.getUsername());
            usernameText.setEnabled(false);
            emailText.setText(currentUser.getEmail());
            phoneNumText.setText(currentUser.getPhoneNum());
        }

        Button signupButton = (Button) findViewById(R.id.button_SignUpMain);
        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = usernameText.getText().toString();
                String email = emailText.getText().toString();
                String phoneNum = phoneNumText.getText().toString();



                User currentUser = UserController.getUserList().getUserByUsername(username);
                if(currentUser == null) {
                    currentUser = new User(username, email, phoneNum);
                    UserController.addUser(currentUser);
                } else {
                    currentUser.setEmail(email);
                    currentUser.setPhoneNum(phoneNum);
                }
                Intent intent = new Intent(activity, RoleSelectActivity.class);
                intent.putExtra("username", username);
                startActivity(intent);
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
