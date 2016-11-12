package assignment1.ridengo;

import junit.framework.TestCase;

/**
 * Created by Rui on 2016-11-12.
 */
public class RideRequestTest extends TestCase {


    public void testGetAcceptions() throws Exception {
        User driver = new User("test2","", "");
        RideRequest newRequest = new RideRequest("","","",new User("","",""), 0.00);

        assertFalse(newRequest.getAcceptions().contains(driver));
        newRequest.addAcception(driver);
        assertTrue(newRequest.getAcceptions().contains(driver));

    }

    public void testAddAcception() throws Exception {
        User rider = new User("test1", "","");
        User driver = new User("test2","", "");
        RideRequest newRequest = new RideRequest("","","",rider, 0.00);

        newRequest.addAcception(driver);

        assertTrue(newRequest.getAcceptions().contains(driver));
    }

    public void testIsAccepted() throws Exception {
        User rider = new User("test1", "","");
        User driver = new User("test2","", "");
        RideRequest newRequest = new RideRequest("","","",rider, 0.00);

        assertFalse(newRequest.isAccepted(driver));
        newRequest.addAcception(driver);
        assertTrue(newRequest.isAccepted(driver));
    }

    public void testIsDriver() throws Exception {
        User rider = new User("rider", "","");
        User driver = new User("driver","", "");
        RideRequest newRequest = new RideRequest("","","",rider, 0.00);

        assertFalse(newRequest.isDriver("rider"));
        assertFalse(newRequest.isDriver("driver"));
        newRequest.setDriver(driver);
        assertTrue(newRequest.isDriver("rider"));
        assertFalse(newRequest.isDriver("driver"));
    }

    public void testCompleteTrip() throws Exception {
        User rider = new User("","","");
        RideRequest newRequest = new RideRequest("","","",rider, 0.00);
        final String tripCompleted = "Trip Completed";

        assertFalse(newRequest.getStatus().equals(tripCompleted));

        newRequest.completeTrip();
        assertTrue(newRequest.getStatus().equals(tripCompleted));
    }

}