package assignment1.ridengo.UnitTesting;

import junit.framework.TestCase;

import assignment1.ridengo.RideRequest;
import assignment1.ridengo.User;
import assignment1.ridengo.UserController;
import assignment1.ridengo.UserList;

/**
 * Created by Rui on 2016-11-19.
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
        UserList userList = UserController.getUserList();
        userList.addUser(rider);
        userList.addUser(driver);

        RideRequest newRequest = new RideRequest("","","",rider, 0.00);

        assertFalse(newRequest.isAccepted(driver.getUsername()));
        newRequest.addAcception(driver);
        assertTrue(newRequest.isAccepted(driver.getUsername()));
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