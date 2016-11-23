package assignment1.ridengo.UITesting;

import android.test.ActivityInstrumentationTestCase2;
import android.widget.EditText;
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

        solo.enterText((EditText) solo.getView(R.id.usernameMain), "IamRich");
        solo.clickOnView(solo.getView(R.id.button_MainSignIn));
        assertTrue(solo.waitForActivity(RoleSelectActivity.class));

        solo.clickOnView(solo.getView(R.id.button_Rider));
        assertTrue(solo.waitForActivity(RiderMainActivity.class));

        solo.clickOnView(solo.getView(R.id.AddRequestButton));
        assertTrue(solo.waitForActivity(RiderPostRequestActivity.class));
//
//        solo.clickOnView(solo.getView(R.id.FindPointOnMapButton));
//        assertTrue(solo.waitForActivity(MapsRiderActivity.class));
//
//        solo.clickOnButton("Enable map");
//        solo.clickInList(2);
//
//        solo.clickOnView(solo.getView(place_autocomplete_fragment));
//        solo.enterText(0, "University of Alberta");
//        solo.enterText(1, "University of Alberta");
//        solo.clickInList(1);
//
//        solo.clickOnView(solo.getView(R.id.place_autocomplete_fragment_from));
//        solo.enterText(0, "Edmonton International Airport");
//        solo.clickInList(1);
//
//        solo.clickOnView(solo.getView(R.id.doneButton));
//        assertTrue(solo.waitForActivity(RiderPostRequestActivity.class));

        solo.clickOnView(solo.getView(R.id.postRequestButton));
        assertTrue(solo.waitForActivity(RiderMainActivity.class));

        solo.clickInList(1);
        assertTrue(solo.waitForActivity(RiderRequestDetailActivity.class));

        TextView textView = (TextView) solo.getView(R.id.RequestDetailCurrentStatusTextView);
        assertTrue(textView.getText().toString().equals("Posted"));

        solo.goBack();

        //Cancel Request
        solo.clickInList(1);
        solo.clickOnView(solo.getView(R.id.RequestDetailCancelRequestButton));
        solo.waitForActivity(RiderMainActivity.class);

        try{
            solo.clickInList(1);
            fail("Should not reach here");
        }catch (Exception e){
            assertTrue(true);
        }
    }

    public void tearDown() throws Exception{
        solo.finishOpenedActivities();
    }
}
