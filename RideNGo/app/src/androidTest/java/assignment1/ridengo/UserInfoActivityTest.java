package assignment1.ridengo;

import android.content.Context;
import android.content.Intent;
import android.test.ActivityInstrumentationTestCase2;
import android.widget.EditText;

import com.robotium.solo.Solo;

import org.apache.commons.lang3.ObjectUtils;

/**
 * Test for UserInfoActivity
 * Created by Rui on 2016-11-11.
 */
public class UserInfoActivityTest extends ActivityInstrumentationTestCase2<UserInfoActivity> {
    private Solo solo;
    Intent intent;

    public UserInfoActivityTest() throws Exception{
        super(UserInfoActivity.class);

    }

    public void setUp() throws Exception{
        super.setUp();
        //solo = new Solo(getInstrumentation(), getActivity());
    }


    public void testValidSignUp()throws Exception{
        intent = new Intent();
        intent.putExtra("username", "");
        setActivityIntent(intent);

        solo = new Solo(getInstrumentation(), getActivity());

        solo.assertCurrentActivity("Wrong Activity", UserInfoActivity.class);

        solo.enterText((EditText)solo.getView(R.id.editText_EnterUsername), "newUser");
        solo.enterText((EditText)solo.getView(R.id.editText_EnterEmail), "test@example.com");
        solo.enterText((EditText)solo.getView(R.id.editText_EnterPhoneNum), "0000000000");

        solo.clickOnView(solo.getView(R.id.button_SignUpMain));

    }

    @Override
    public void tearDown() throws Exception{
        solo.finishOpenedActivities();
    }
}
