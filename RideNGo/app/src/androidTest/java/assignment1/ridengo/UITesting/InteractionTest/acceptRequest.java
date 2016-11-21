package assignment1.ridengo.UITesting.InteractionTest;

import android.test.ActivityInstrumentationTestCase2;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.robotium.solo.Solo;

import java.util.List;

import assignment1.ridengo.DriverMainActivity;
import assignment1.ridengo.DriverRequestDetailActivity;
import assignment1.ridengo.MainActivity;
import assignment1.ridengo.R;
import assignment1.ridengo.RoleSelectActivity;

/**
 * Created by Rui on 2016-11-20.
 */
public class acceptRequest extends ActivityInstrumentationTestCase2<MainActivity> {
    private Solo solo;

    public acceptRequest(){
        super(MainActivity.class);
    }

    public void setUp() throws Exception{
        super.setUp();
        solo = new Solo(getInstrumentation(), getActivity());
    }

    public void testAddDriver(){
        solo.assertCurrentActivity("Wrong Activity", MainActivity.class);
        solo.enterText((EditText) solo.getView(R.id.usernameMain), "IamRich");
        solo.clickOnView(solo.getView(R.id.button_MainSignIn));
        assertTrue(solo.waitForActivity(RoleSelectActivity.class));

        solo.clickOnView(solo.getView(R.id.button_Driver));
        assertTrue(solo.waitForActivity(DriverMainActivity.class));

        ListView listView = (ListView)solo.getView(R.id.DriverRequestListView);
        for(int i = 0; i<listView.getCount(); i++){
            solo.clickInList(i + 1);
            assertTrue(solo.waitForActivity(DriverRequestDetailActivity.class));
            String riderName = listView.getItemAtPosition(2).toString();
            if(riderName.trim().equals("Name: IamRider")){
                solo.clickOnView(solo.getView(R.id.AcceptButton));
                fail();
            }
            solo.goBack();
        }

    }

    @Override
    public void tearDown() throws Exception{
        solo.finishOpenedActivities();
    }

}
