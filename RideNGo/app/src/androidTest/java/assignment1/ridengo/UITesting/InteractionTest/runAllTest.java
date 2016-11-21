package assignment1.ridengo.UITesting.InteractionTest;

import android.app.Activity;
import android.sax.TextElementListener;
import android.test.ActivityInstrumentationTestCase2;

import junit.framework.TestSuite;

/**
 * Created by Rui on 2016-11-20.
 */
public class runAllTest extends ActivityInstrumentationTestCase2<Activity> {
    public runAllTest(){
        super(Activity.class);
    }

    public static TestSuite suite(){
        TestSuite t = new TestSuite();
        t.addTestSuite(addRider.class);
        t.addTestSuite(addDriver.class);
        t.addTestSuite(postRequest.class);
        t.addTestSuite(acceptRequest.class);
        t.addTestSuite(confirmDriver.class);
        t.addTestSuite(CompleteTrip.class);
        return t;
    }
}
