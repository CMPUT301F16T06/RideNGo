package assignment1.ridengo.UITesting.InteractionTest;

import android.test.ActivityInstrumentationTestCase2;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.robotium.solo.Solo;

import java.util.ArrayList;
import java.util.List;

import assignment1.ridengo.MainActivity;
import assignment1.ridengo.R;
import assignment1.ridengo.RiderMainActivity;
import assignment1.ridengo.RiderPostRequestActivity;
import assignment1.ridengo.RiderRequestDetailActivity;
import assignment1.ridengo.RoleSelectActivity;

/**
 * Created by Rui on 2016-11-20.
 */
public class postRequest extends ActivityInstrumentationTestCase2<MainActivity> {

    private Solo solo;
    public postRequest(){
        super(MainActivity.class);
    }

    public void setUp() throws Exception{
        super.setUp();
        solo = new Solo(getInstrumentation(), getActivity());
    }

    public void testPostRequest(){
        solo.assertCurrentActivity("Wrong Activity", MainActivity.class);
        solo.enterText((EditText) solo.getView(R.id.usernameMain), "IamRider");
        solo.clickOnView(solo.getView(R.id.button_MainSignIn));
        assertTrue(solo.waitForActivity(RoleSelectActivity.class));

        solo.clickOnView(solo.getView(R.id.button_Rider));
        assertTrue(solo.waitForActivity(RiderMainActivity.class));

        solo.clickOnView(solo.getView(R.id.AddRequestButton));
        assertTrue(solo.waitForActivity(RiderPostRequestActivity.class));

        solo.clickOnView(solo.getView(R.id.FindPointOnMapButton));
        solo.clickOnView(solo.getView(R.id.locationSearcher));
        solo.clickOnText("Use address searcher");

        solo.clickOnView(solo.getView(R.id.place_autocomplete_fragment));
        assertTrue(solo.waitForFragmentById(R.id.place_autocomplete_fragment));
        ArrayList<View> v = solo.getViews();
        solo.enterText((EditText)v.get(0), "University of Alberta");
        solo.clickInList(1);

                solo.clickOnView(solo.getView(R.id.place_autocomplete_fragment_from));
        solo.enterText(0, "Edmonton International Airport");
        solo.clickInList(1);

        solo.clickOnView(solo.getView(R.id.postRequestButton));
        assertTrue(solo.waitForActivity(RiderMainActivity.class));


    }
    @Override
    public void tearDown() throws Exception{
        solo.finishOpenedActivities();
    }

}