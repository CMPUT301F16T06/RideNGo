package assignment1.ridengo;

import android.test.ActivityInstrumentationTestCase2;
import android.widget.EditText;

import com.robotium.solo.Solo;

/**
 * Test for RiderPostRequestActivity
 * Created by Rui on 2016-11-11.
 */
public class RiderPostRequestActivityTest extends ActivityInstrumentationTestCase2<RiderPostRequestActivity> {
    private Solo solo;

    public RiderPostRequestActivityTest(){
        super(RiderPostRequestActivity.class);
    }

    public void setUp() throws Exception{

        solo = new Solo(getInstrumentation(), getActivity());
    }

    public void testPostRequest() throws Exception{
        solo.assertCurrentActivity("Wrong Activity", RiderPostRequestActivity.class);

        solo.enterText((EditText) solo.getView(R.id.StartPointEditText), "Start");
        solo.enterText((EditText)solo.getView(R.id.EndPointEditText), "End");

        solo.clickOnView(solo.getView(R.id.postRequestButton));

        solo.assertCurrentActivity("Wrong", RiderMainActivity.class);
    }

    @Override
    public void tearDown() throws Exception{
        solo.finishOpenedActivities();
    }
}
