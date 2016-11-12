package assignment1.ridengo;

import android.test.ActivityInstrumentationTestCase2;

import com.robotium.solo.Solo;

/**
 * Test for DriverAcceptRequestActivity
 * Created by Rui on 2016-11-11.
 */
public class DriverAcceptRequestActivityTest extends ActivityInstrumentationTestCase2<DriverAcceptedListActivity> {

    private Solo solo;

    public DriverAcceptRequestActivityTest(){
        super(DriverAcceptedListActivity.class);
    }

    public void setUp() throws Exception{
        solo = new Solo(getInstrumentation(), getActivity());
    }

    public void testActivity() throws Exception{
        solo.assertCurrentActivity("Wrong Activity", MainActivity.class);
    }

    @Override
    public void tearDown() throws Exception{
        solo.finishOpenedActivities();
    }
}
