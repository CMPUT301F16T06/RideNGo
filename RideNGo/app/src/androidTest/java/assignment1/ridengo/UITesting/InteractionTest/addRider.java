package assignment1.ridengo.UITesting.InteractionTest;

import android.test.ActivityInstrumentationTestCase2;
import android.widget.EditText;

import com.robotium.solo.Solo;

import assignment1.ridengo.MainActivity;
import assignment1.ridengo.R;
import assignment1.ridengo.RoleSelectActivity;

/**
 * Created by Rui on 2016-11-20.
 */
public class addRider extends ActivityInstrumentationTestCase2<MainActivity> {

    private Solo solo;
    public addRider(){
        super(MainActivity.class);
    }

    public void setUp() throws Exception{
        super.setUp();
        solo = new Solo(getInstrumentation(), getActivity());
    }

    public void testAddRider(){
        solo.assertCurrentActivity("Wrong Activity", MainActivity.class);
        solo.enterText((EditText) solo.getView(R.id.usernameMain), "IamRider");
        solo.clickOnView(solo.getView(R.id.button_MainSignIn));
        if(solo.searchText("User does not exist.")){
            solo.clickOnView(solo.getView(R.id.button_SignUpMain));

            solo.enterText((EditText) solo.getView(R.id.editText_EnterUsername), "IamRider");
            solo.enterText((EditText) solo.getView(R.id.editText_EnterEmail), "rider@gmail.com");
            solo.enterText((EditText) solo.getView(R.id.editText_EnterPhoneNum), "18001234567");

            solo.clickOnView(solo.getView(R.id.button_SignUpMain));
        }
        assertTrue(solo.waitForActivity(RoleSelectActivity.class));
    }
    @Override
    public void tearDown() throws Exception{
        solo.finishOpenedActivities();
    }

}
