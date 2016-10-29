package assignment1.ridengo;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class UserInfoActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);

        final EditText usernameText = (EditText) findViewById(R.id.editText_EnterUsername);
        final EditText emailText = (EditText) findViewById(R.id.editText_EnterEmail);
        final EditText phoneNumText = (EditText) findViewById(R.id.editText_EnterPhoneNum);

        String username = getIntent().getStringExtra("username");
        if(!username.isEmpty()){
            usernameText.setText(username);
            usernameText.setEnabled(false);
        }

        Button signupButton = (Button) findViewById(R.id.button_SignUp);
        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = usernameText.getText().toString();
                String email = emailText.getText().toString();
                String phoneNum = phoneNumText.getText().toString();
                User user = new User(username, email, phoneNum);
                UserController.getUserList().addUser(user);
            }
        });
    }
}
