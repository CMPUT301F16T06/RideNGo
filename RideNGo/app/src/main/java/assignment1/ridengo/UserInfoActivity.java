package assignment1.ridengo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class UserInfoActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);

        final Activity activity = this;

        final EditText usernameText = (EditText) findViewById(R.id.editText_EnterUsername);
        final EditText emailText = (EditText) findViewById(R.id.editText_EnterEmail);
        final EditText phoneNumText = (EditText) findViewById(R.id.editText_EnterPhoneNum);

        String username = getIntent().getStringExtra("username");
        if(!username.isEmpty()){
            User currentUser = UserController.getUserList().getUserByUsername(username);
            usernameText.setText(currentUser.getUsername());
            usernameText.setEnabled(false);
            emailText.setText(currentUser.getEmail());
            phoneNumText.setText(currentUser.getPhoneNum());
        }

        Button signupButton = (Button) findViewById(R.id.button_SignUp);
        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = usernameText.getText().toString();
                String email = emailText.getText().toString();
                String phoneNum = phoneNumText.getText().toString();
                User user = new User(username, email, phoneNum);
                try{
                    UserController.getUserList().addUser(user);
                    Intent intent = new Intent(activity, RoleSelectActivity.class);
                    intent.putExtra("username", username);
                    startActivity(intent);
                }
                catch (RuntimeException e){
                    Toast.makeText(getApplicationContext(), "User Already Exits.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
