package assignment1.ridengo;

import android.content.Intent;
import android.test.ActivityInstrumentationTestCase2;

import com.robotium.solo.Solo;

/**
 * Test for RoleSelectActivity
 * Created by Rui on 2016-11-11.
 */
public class RoleSelectActivityTest extends ActivityInstrumentationTestCase2<RoleSelectActivity> {
    private Solo solo;
    Intent intent;

    public RoleSelectActivityTest(){
        super(RoleSelectActivity.class);

    }

    public void setUp() throws Exception{
        intent = new Intent();
        intent.putExtra("username", "testUser");
        setActivityIntent(intent);
        solo = new Solo(getInstrumentation(), getActivity());
    }

    public void testActivity() throws Exception{
        solo.assertCurrentActivity("Wrong Activity", RoleSelectActivity.class);

        solo.clickOnView(solo.getView(R.id.button_Driver));

        solo.clickOnView(solo.getView(R.id.button_Rider));

        //solo.clickOnView(solo.getView(R.id.button_EditInfo));

    }

    @Override
    public void tearDown() throws Exception{
        solo.finishOpenedActivities();
    }
}
