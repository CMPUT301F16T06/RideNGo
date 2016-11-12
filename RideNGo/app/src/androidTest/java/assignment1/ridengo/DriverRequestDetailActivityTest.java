package assignment1.ridengo;

import android.test.ActivityInstrumentationTestCase2;

import com.robotium.solo.Solo;

/**
 * Test for DriverRequestDetailActivity
 * Created by Rui on 2016-11-11.
 */
public class DriverRequestDetailActivityTest extends ActivityInstrumentationTestCase2<DriverRequestDetailActivity> {
    private Solo solo;

    public DriverRequestDetailActivityTest(){
        super(DriverRequestDetailActivity.class);
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
