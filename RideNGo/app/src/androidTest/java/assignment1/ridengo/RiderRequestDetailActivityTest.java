package assignment1.ridengo;

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
        solo = new Solo(getInstrumentation(), getActivity());
        solo.assertCurrentActivity("Wrong Activity", MainActivity.class);
    }

    @Override
    public void tearDown() throws Exception{
        solo.finishOpenedActivities();
    }
}
