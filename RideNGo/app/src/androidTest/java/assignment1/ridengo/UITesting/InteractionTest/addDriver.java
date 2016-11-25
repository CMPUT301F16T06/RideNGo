package assignment1.ridengo.UITesting.InteractionTest;

import android.test.ActivityInstrumentationTestCase2;
import android.widget.EditText;
import android.widget.TextView;

import com.robotium.solo.Solo;

import assignment1.ridengo.MainActivity;
import assignment1.ridengo.R;
import assignment1.ridengo.RoleSelectActivity;

/**
 * Created by Rui on 2016-11-20.
 */
public class addDriver extends ActivityInstrumentationTestCase2<MainActivity> {
    private Solo solo;

    public addDriver(){
        super(MainActivity.class);
    }


    public void setUp() throws Exception{
        super.setUp();
        solo = new Solo(getInstrumentation(), getActivity());
    }

    public void testAddDriver(){
        solo.assertCurrentActivity("Wrong Activity", MainActivity.class);
        solo.enterText((EditText) solo.getView(R.id.usernameMain), "IamDriver");
        solo.clickOnView(solo.getView(R.id.button_MainSignIn));
        if(solo.searchText("User does not exist.")){
            solo.clickOnView(solo.getView(R.id.button_SignUpMain));

            solo.enterText((EditText) solo.getView(R.id.editText_EnterUsername), "IamDriver");
            solo.enterText((EditText) solo.getView(R.id.editText_EnterEmail), "driver@gmail.com");
            solo.enterText((EditText) solo.getView(R.id.editText_EnterPhoneNum), "18007654321");
            solo.clickOnView(solo.getView(R.id.button_SignUpMain));
        }
        assertTrue(solo.waitForActivity(RoleSelectActivity.class));

        solo.clickOnView(solo.getView(R.id.button_EditInfo));
        TextView textView = (TextView)solo.getView(R.id.vehicleInfoTextView);

        if(textView.getText().equals("No vehicle information\nPlease add vehicle if you want to be a driver")) {
            solo.clickOnButton("Add Vehicle");

            solo.clickOnView(solo.getView(R.id.YearSpinner));
            solo.clickOnText("2016");

            solo.enterText((EditText) solo.getView(R.id.MakeEditText), "Rolls Royce");
            solo.enterText((EditText) solo.getView(R.id.ModelEditText), "Ghost");
            solo.clickOnView(solo.getView(R.id.ColorSpinner));
            solo.clickOnText("Black");

            solo.enterText((EditText) solo.getView(R.id.PNumEditText), "001");
        }
        solo.clickOnButton("Confirm");
        assertTrue(solo.waitForActivity(RoleSelectActivity.class));
    }

    @Override
    public void tearDown() throws Exception{
        solo.finishOpenedActivities();
    }

}
