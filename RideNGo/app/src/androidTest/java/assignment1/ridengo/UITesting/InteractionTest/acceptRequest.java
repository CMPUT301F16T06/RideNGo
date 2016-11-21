package assignment1.ridengo.UITesting.InteractionTest;

import android.test.ActivityInstrumentationTestCase2;
import android.widget.Button;
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

    public void testAcceptByDriver(){
        solo.assertCurrentActivity("Wrong Activity", MainActivity.class);
        solo.enterText((EditText) solo.getView(R.id.usernameMain), "IamDriver");
        solo.clickOnView(solo.getView(R.id.button_MainSignIn));
        assertTrue(solo.waitForActivity(RoleSelectActivity.class));

        solo.clickOnView(solo.getView(R.id.button_Driver));
        assertTrue(solo.waitForActivity(DriverMainActivity.class));

        final ListView listView = (ListView)solo.getView(R.id.DriverRequestListView);
        final int count = listView.getCount();
        for(int i = 0; i<count; i++){
            solo.clickInList(i);
            assertTrue(solo.waitForActivity(DriverRequestDetailActivity.class));
            String riderName = solo.clickInList(3).get(0).getText().toString().trim();
            Button button = (Button) solo.getView(R.id.AcceptButton);
            String buttonStr = button.getText().toString().trim();
            if(riderName.equals("Name: IamRider") && buttonStr.equals("Give him a ride!")){
                solo.clickOnView(solo.getView(R.id.AcceptButton));
                continue;
            }
            solo.goBack();
        }
    }

    @Override
    public void tearDown() throws Exception{
        solo.finishOpenedActivities();
    }

}
