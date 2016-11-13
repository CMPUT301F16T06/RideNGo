package assignment1.ridengo;

import android.test.ActivityInstrumentationTestCase2;

import com.robotium.solo.Solo;

/**
 * Test for DriverMainActivity
 * Created by Rui on 2016-11-11.
 */
public class DriverMainActivityTest extends ActivityInstrumentationTestCase2<DriverMainActivity> {
    private Solo solo;

    public DriverMainActivityTest(){
        super(DriverMainActivity.class);
    }

    public void setUp() throws Exception{
        solo = new Solo(getInstrumentation(), getActivity());
    }

    public void testViewAccepted() throws Exception{
        solo.assertCurrentActivity("Wrong Activity", DriverMainActivity.class);

        solo.clickOnView(solo.getView(R.id.SearchButton));

        solo.clickOnView(solo.getView(R.id.FindNearbyButton));

        solo.clickOnView(solo.getView(R.id.ViewAcceptedButton));
        assertTrue(solo.waitForActivity(DriverAcceptedListActivity.class));


    }

    public void testViewRequest() throws Exception{
        solo.assertCurrentActivity("Wrong Activity", DriverMainActivity.class);

    }

    @Override
    public void tearDown() throws Exception{
        solo.finishOpenedActivities();
    }
}
