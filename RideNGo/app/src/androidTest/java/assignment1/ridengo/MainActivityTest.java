package assignment1.ridengo;

import android.content.Intent;
import android.test.ActivityInstrumentationTestCase2;
import android.widget.EditText;

import com.robotium.solo.Solo;

import java.util.ArrayList;

/**
 * Test for MainActivity
 * Created by Rui on 2016-11-11.
 */
public class MainActivityTest extends ActivityInstrumentationTestCase2<MainActivity> {

    private Solo solo;

    public MainActivityTest(){
        super(MainActivity.class);
    }

    public void setUp() throws Exception{
        super.setUp();
        solo = new Solo(getInstrumentation(), getActivity());
    }

    public void testSignUp() throws Exception {
        solo.assertCurrentActivity("Wrong Activity", MainActivity.class);

        Intent intent = new Intent();
        intent.putExtra("username", "");
        solo.clickOnView(solo.getView(R.id.button_SignUpMain));
    }

    public void testInvalidSignIn() throws Exception{
        solo.assertCurrentActivity("Wrong Activity", MainActivity.class);

        solo.clearEditText((EditText) solo.getView(R.id.usernameMain));
        solo.clickOnView(solo.getView(R.id.button_MainSignIn));
        assertTrue(solo.searchText("User does not exist."));
    }

    public void testValidSignIn() throws Exception{
        solo.assertCurrentActivity("Wrong Activity", MainActivity.class);

        UserList userList = UserController.getUserList();
        User newUser = new User("test", "", "");
        userList.addUser(newUser);

        solo.enterText((EditText) solo.getView(R.id.usernameMain), newUser.getUsername());
        Intent intent = new Intent();
        intent.putExtra("username", newUser.getUsername());
        solo.clickOnView(solo.getView(R.id.button_MainSignIn));
        assertTrue(solo.waitForActivity(RoleSelectActivity.class));
    }
    @Override
    public void tearDown() throws Exception{
        solo.finishOpenedActivities();
    }

}
