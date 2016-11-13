package assignment1.ridengo;

import android.content.Intent;
import android.test.ActivityInstrumentationTestCase2;

import com.robotium.solo.Solo;

/**
 * Test for RiderRequestDetailActivity
 * Created by Rui on 2016-11-11.
 */
public class RiderRequestDetailActivityTest extends ActivityInstrumentationTestCase2<RiderRequestDetailActivity> {
    private Solo solo;

    public RiderRequestDetailActivityTest(){
        super(RiderRequestDetailActivity.class);
    }

    public void setUp() throws Exception{
        super.setUp();
        solo = new Solo(getInstrumentation(), getActivity());
    }

    public void testCancelRequest() throws Exception{
        solo.assertCurrentActivity("Wrong Activity", RiderRequestDetailActivity.class);

        solo.clickOnView(solo.getView(R.id.RequestDetailCancelRequestButton));
        assertTrue(solo.waitForActivity(RiderMainActivity.class));
    }

    public void testCompleteRequest() throws Exception{
        solo.assertCurrentActivity("Wrong Activity", RiderRequestDetailActivity.class);
        solo.clickOnView(solo.getView(R.id.RequestDetailConfirmButton));
        assertTrue(solo.waitForActivity(RiderMainActivity.class));
    }

    @Override
    public void tearDown() throws Exception{
        solo.finishOpenedActivities();
    }
}
