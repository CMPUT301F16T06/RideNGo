package assignment1.ridengo.UITesting.InteractionTest;

import android.content.Intent;
import android.os.Bundle;
import android.test.ActivityInstrumentationTestCase2;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.robotium.solo.Solo;

import java.util.ArrayList;
import java.util.List;

import assignment1.ridengo.DriverMainActivity;
import assignment1.ridengo.DriverRequestDetailActivity;
import assignment1.ridengo.MainActivity;
import assignment1.ridengo.R;
import assignment1.ridengo.RoleSelectActivity;
import assignment1.ridengo.ShowPointsOnMapActivity;

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

        solo.enterText((EditText)solo.getView(R.id.SearchTextView), "TESTESTEST");
        solo.clickOnView(solo.getView(R.id.SearchButton));
        ListView listView = (ListView) solo.getView(R.id.DriverRequestListView);
        assertTrue(listView.getCount() == 1);

        solo.clickInList(1);
        solo.clickOnView(solo.getView(R.id.AcceptButton));

    }

    @Override
    public void tearDown() throws Exception{
        solo.finishOpenedActivities();
    }

}
