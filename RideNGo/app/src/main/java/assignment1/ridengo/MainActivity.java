
package assignment1.ridengo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


/**
 * The type Main activity.
 * Able to let the user to select to sign in or sign up
 */
public class MainActivity extends Activity {
    private int mBackKeyPressedTimes = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Activity activity = this;

        RideRequestController.loadRequestListFromServer("{\"from\":0,\"size\":10000}");
        UserController.loadUserListFromServer();
        Toast.makeText(this, "Number of users: " + UserController.getUserList().getUsers().size(), Toast.LENGTH_LONG).show();

        Button signin = (Button) findViewById(R.id.button_MainSignIn);
        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText usernameText = (EditText) findViewById(R.id.usernameMain);
                String username = usernameText.getText().toString();
                if(UserController.getUserList().contains(username)){
                    Intent intent = new Intent(activity, RoleSelectActivity.class);
                    intent.putExtra("username", username);
                    startActivity(intent);
                    finish();
                }
                else{
                    Toast.makeText(getApplicationContext(), "User does not exist.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        Button signup = (Button) findViewById(R.id.button_SignUpMain);
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(activity, UserInfoActivity.class);
                intent.putExtra("username", "");
                startActivity(intent);
                finish();
            }
        });
    }

    //http://blog.csdn.net/caesardadi/article/details/8241305
    @Override
    public void onBackPressed() {
        if (mBackKeyPressedTimes == 0) {
            Toast.makeText(this, "Press again to exit.", Toast.LENGTH_SHORT).show();
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
            super.onBackPressed();
        }

    }
}
