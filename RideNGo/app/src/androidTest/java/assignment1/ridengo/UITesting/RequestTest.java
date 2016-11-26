package assignment1.ridengo.UITesting;

import android.test.ActivityInstrumentationTestCase2;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.robotium.solo.Solo;

import assignment1.ridengo.MainActivity;
import assignment1.ridengo.R;
import assignment1.ridengo.RiderMainActivity;
import assignment1.ridengo.RiderPostRequestActivity;
import assignment1.ridengo.RiderRequestDetailActivity;
import assignment1.ridengo.RoleSelectActivity;

/**
 * Created by Rui on 2016-11-20.
 */
public class RequestTest extends ActivityInstrumentationTestCase2<MainActivity> {

    private Solo solo;

    public RequestTest(){
        super(MainActivity.class);
    }

    public void setUp() throws Exception{
        super.setUp();
        solo = new Solo(getInstrumentation(), getActivity());
    }

    public void testPostRequests() throws Exception{
        solo.assertCurrentActivity("Wrong Activiry", MainActivity.class);

        solo.enterText((EditText) solo.getView(R.id.usernameMain), "RequestTest");
        solo.clickOnView(solo.getView(R.id.button_MainSignIn));
        if(solo.searchText("User does not exist.")){
            solo.clickOnView(solo.getView(R.id.button_SignUpMain));

            solo.enterText((EditText) solo.getView(R.id.editText_EnterUsername), "RequestTest");
            solo.enterText((EditText) solo.getView(R.id.editText_EnterEmail), "RequestTest@gmail.com");
            solo.enterText((EditText) solo.getView(R.id.editText_EnterPhoneNum), "18001234567");

            solo.clickOnView(solo.getView(R.id.button_SignUpMain));
        }

        assertTrue(solo.waitForActivity(RoleSelectActivity.class));
        solo.clickOnView(solo.getView(R.id.button_Rider));
        assertTrue(solo.waitForActivity(RiderMainActivity.class));

        solo.clickOnView(solo.getView(R.id.AddRequestButton));
        assertTrue(solo.waitForActivity(RiderPostRequestActivity.class));

        solo.clickOnView(solo.getView(R.id.postRequestButton));
        assertTrue(solo.waitForActivity(RiderMainActivity.class));

        solo.clickInList(1);
        assertTrue(solo.waitForActivity(RiderRequestDetailActivity.class));

        TextView textView = (TextView) solo.getView(R.id.RequestDetailCurrentStatusTextView);
        assertTrue(textView.getText().toString().equals("Waiting for Driver"));

        solo.goBack();

        //Cancel Request
        solo.clickInList(1);
        solo.clickOnView(solo.getView(R.id.RequestDetailCancelRequestButton));
        solo.waitForActivity(RiderMainActivity.class);

        ListView listView = (ListView) solo.getView(R.id.RiderRequestListView);
        assertEquals(listView.getCount(), 0);
    }

    public void tearDown() throws Exception{
        solo.finishOpenedActivities();
    }
}
