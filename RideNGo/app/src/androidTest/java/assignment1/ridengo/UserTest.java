package assignment1.ridengo;

import junit.framework.TestCase;

/**
 * Created by Rui on 2016-11-12.
 */
public class UserTest extends TestCase {

    public void testGetUsername() throws Exception {
        User user = new User("test1", "test@example.com", "1234567890");
        assertEquals(user.getUsername(), "test1");
    }

    public void testGetEmail() throws Exception {
        User user = new User("test1", "test@example.com", "1234567890");
        assertEquals(user.getEmail(), "test@example.com");

    }

    public void testSetEmail() throws Exception {
        User user = new User("test1", "", "1234567890");
        assertFalse(user.getEmail().equals("test@example.com"));
        user.setEmail("test@example.com");
        assertEquals(user.getEmail(), "test@example.com");

    }

    public void testGetPhoneNum() throws Exception {
        User user = new User("test1", "test@example.com", "1234567890");
        assertEquals(user.getPhoneNum(), "1234567890");

    }

    public void testSetPhoneNum() throws Exception {
        User user = new User("test1", "test@example.com", "");
        user.setPhoneNum("1234567890");
        assertEquals(user.getPhoneNum(), "1234567890");
    }

    public void testSetId() throws Exception {
        User user = new User("test1", "test@example.com", "1234567890");
        user.setId("1");
        assertEquals(user.getId(), "1");
    }

    public void testAcceptRequest() throws Exception {
        fail("Never been used, re-confirmed");
    }

    public void testDriverCompleteRide() throws Exception {
        fail("Never been used, re-confirmed");
    }

    public void testGetRequests() throws Exception {
        fail("Never been used, re-confirmed");
    }

    public void testPostRideRequest() throws Exception {
        fail("Never been used, re-confirmed");
    }

    public void testConfirmAcception() throws Exception {
        User rider = new User("test1", "test@example.com", "1234567890");
        User driver = new User("test2", "test2@example.com", "1234567891");

        RideRequest newRequest = new RideRequest("","","",rider, 0.00);
        driver.confirmAcception(newRequest, driver);

        assertEquals(newRequest.getDriver().getUsername(), driver.getUsername());
        assertEquals(newRequest.getDriver().getEmail(), driver.getEmail());
        assertEquals(newRequest.getDriver().getPhoneNum(), driver.getPhoneNum());
    }

    public void testCancelRequest() throws Exception {
        fail("Never been used, re-confirmed");
    }

    public void testRiderCompleteRide() throws Exception {
        User rider = new User("test1", "test@example.com", "1234567890");
        RideRequest newRequest = new RideRequest("","","",rider, 0.00);

        rider.riderCompleteRide(newRequest);
    }

    public void testToString() throws Exception {
        User user = new User("test1", "test@example.com", "1234567890");
        assertEquals(user.toString(), "test1");
    }
}