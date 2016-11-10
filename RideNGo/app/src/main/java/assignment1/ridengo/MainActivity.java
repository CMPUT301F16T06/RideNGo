package assignment1.ridengo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Activity activity = this;

        Button signin = (Button) findViewById(R.id.button_MainSignIn);
        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText usernameText = (EditText) findViewById(R.id.edit1);
                String username = usernameText.getText().toString();
                if(UserController.getUserList().contains(username)){
                    Intent intent = new Intent(activity, RoleSelectActivity.class);
                    intent.putExtra("username", username);
                    startActivity(intent);
                }
                else{
                    Toast.makeText(getApplicationContext(), "User does not exist.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        Button signup = (Button) findViewById(R.id.button_SignUp);
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(activity, UserInfoActivity.class);
                intent.putExtra("username", "");
                startActivity(intent);
            }
        });
    }
}
