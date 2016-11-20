package assignment1.ridengo.UnitTesting;

import junit.framework.TestCase;

import assignment1.ridengo.RideRequest;
import assignment1.ridengo.User;
import assignment1.ridengo.Vehicle;

/**
 * Created by Rui on 2016-11-19.
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

    public void testSetVehicle() throws Exception {
        User user = new User("","","");
        Vehicle vehicle = new Vehicle("",0,"","","");
        assertEquals(user.getVehicle(), null);

        user.setVehicle(vehicle);
        assertEquals(user.getVehicle(), vehicle);
    }

    public void testRmVehicle() throws Exception {
        User user = new User("","","");
        Vehicle vehicle = new Vehicle("",0,"","","");

        user.setVehicle(vehicle);
        assertEquals(user.getVehicle(), vehicle);

        user.rmVehicle();
        assertEquals(user.getVehicle(), null);
    }

    public void testHaveVehicle() throws Exception {
        User user = new User("","","");
        Vehicle vehicle = new Vehicle("",0,"","","");
        assertEquals(user.getVehicle(), null);

        user.setVehicle(vehicle);
        assertTrue(user.getVehicle()!= null);
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

    public void testConfirmAcception() throws Exception {
        User rider = new User("test1", "test@example.com", "1234567890");
        User driver = new User("test2", "test2@example.com", "1234567891");

        RideRequest newRequest = new RideRequest("","","",rider, 0.00);
        driver.confirmAcception(newRequest, driver);

        assertEquals(newRequest.getDriver().getUsername(), driver.getUsername());
        assertEquals(newRequest.getDriver().getEmail(), driver.getEmail());
        assertEquals(newRequest.getDriver().getPhoneNum(), driver.getPhoneNum());

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

    public void testGetPendingRequests() throws Exception {
        fail("Not Implement yet");
    }

    public void testGetRequestsByKeyword() throws Exception {
        fail("Not Implement yet");
    }

    public void testGetRequestsByGeoLocation() throws Exception {
        fail("Not Implement yet");
    }
}