package assignment1.ridengo;

import android.test.ActivityInstrumentationTestCase2;
import android.test.UiThreadTest;
import android.view.View;
import android.widget.Adapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.robotium.solo.Solo;

import java.util.List;

/**
 * Test for RiderMainActivity
 * Created by Rui on 2016-11-11.
 */
public class RiderMainActivityTest extends ActivityInstrumentationTestCase2<RiderMainActivity> {
    private Solo solo;

    public RiderMainActivityTest(){
        super(RiderMainActivity.class);
    }

    public void setUp() throws Exception{
        solo = new Solo(getInstrumentation(), getActivity());
    }

    @UiThreadTest
    public void testViewRequest() throws Exception{
        solo.assertCurrentActivity("Wrong Activity", RiderMainActivity.class);

        ListView listView = (ListView) solo.getView(R.id.RiderRequestListView);
        if(listView.getCount() != 0){
            solo.clickInList(0);
            assertTrue(solo.waitForActivity(RiderRequestDetailActivity.class));
        }
    }

    public void testPostRequest() throws Exception{
        solo.assertCurrentActivity("Wrong Activity", RiderMainActivity.class);

        solo.clickOnView(solo.getView(R.id.AddRequestButton));
        assertTrue(solo.waitForActivity(RiderPostRequestActivity.class));

    }

    @Override
    public void tearDown() throws Exception{
        solo.finishOpenedActivities();
    }
}
