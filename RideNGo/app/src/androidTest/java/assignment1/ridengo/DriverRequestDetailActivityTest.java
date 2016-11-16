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
        //solo = new Solo(getInstrumentation(), getActivity());
    }

    public void testActivity() throws Exception{
//        solo.assertCurrentActivity("Wrong Activity", DriverRequestDetailActivity.class);
//
//        solo.clickInList(3);
//        assertTrue(solo.searchText("Email Rider"));
//
//        solo.clickInList(4);
//        assertTrue(solo.searchText("Call Rider"));
    }

    @Override
    public void tearDown() throws Exception{
        solo.finishOpenedActivities();
    }
}
