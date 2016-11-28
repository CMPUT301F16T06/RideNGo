package assignment1.ridengo.UITesting.InteractionTest;

import android.test.ActivityInstrumentationTestCase2;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.robotium.solo.Solo;

import assignment1.ridengo.MainActivity;
import assignment1.ridengo.R;
import assignment1.ridengo.RiderMainActivity;
import assignment1.ridengo.RiderRequestDetailActivity;
import assignment1.ridengo.RoleSelectActivity;

/**
 * Created by Rui on 2016-11-20.
 */
public class confirmDriver extends ActivityInstrumentationTestCase2<MainActivity> {

    private Solo solo;

    public confirmDriver() {
        super(MainActivity.class);
    }

    public void setUp() throws Exception{
        super.setUp();
        solo = new Solo(getInstrumentation(), getActivity());
    }

    public void testConfirmDriver(){
        solo.assertCurrentActivity("Wrong Activity", MainActivity.class);
        solo.enterText((EditText) solo.getView(R.id.usernameMain), "IamRider");
        solo.clickOnView(solo.getView(R.id.button_MainSignIn));
        assertTrue(solo.waitForActivity(RoleSelectActivity.class));

        assertTrue(solo.searchText(""));

        solo.clickOnView(solo.getView(R.id.button_Rider));
        assertTrue(solo.waitForActivity(RiderMainActivity.class));

        ListView listView = (ListView)solo.getView(R.id.RiderRequestListView);
        for(int i = 0; i<listView.getCount(); i++) {
            solo.clickInList(i);
            assertTrue(solo.waitForActivity(RiderRequestDetailActivity.class));

            TextView textView = (TextView) solo.getView(R.id.RequestDetailCurrentStatusTextView);
            if (textView.getText().toString().equals("Accepted By Driver")) {
                solo.clickInList(1);
                solo.clickOnButton("Confirm Driver");
            }else {
                solo.goBack();
            }
        }
    }
    @Override
    public void tearDown() throws Exception{
        solo.finishOpenedActivities();
    }

}

