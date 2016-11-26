package assignment1.ridengo.UITesting.ProfileTest;

import android.app.Activity;
import android.test.ActivityInstrumentationTestCase2;

import junit.framework.TestSuite;

/**
 * Created by Rui on 2016-11-20.
 */
public class runProfileTest extends ActivityInstrumentationTestCase2<Activity> {
    public runProfileTest(){
        super(Activity.class);
    }

    public static TestSuite suite(){
        TestSuite t = new TestSuite();
        t.addTestSuite(testSignUp.class);
        t.addTestSuite(testEditProfile.class);
        return t;
    }
}
