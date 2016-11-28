package assignment1.ridengo.UnitTesting;

import com.google.android.gms.maps.model.LatLng;

import junit.framework.TestCase;

import java.util.List;

import assignment1.ridengo.RideRequest;
import assignment1.ridengo.RideRequestController;
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
        assertTrue(user.getVehicle() != null);
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

    public void testSetRating() throws Exception{
        User user = new User("test1", "test@example.com", "");
        assertEquals(user.getNumRatings(), 0);

        user.setRating((float)5);
        assertEquals(user.getRating(), (float) 5);
        assertEquals(user.getNumRatings(), 1);
        assertEquals(user.getTotalOfRating(), 5);
    }

    public void testAcceptRequest() throws Exception{
        User rider = new User("test1", "test@example.com", "1234567890");
        User driver = new User("test2", "test2@example.com", "1234567891");

        RideRequest newRequest = new RideRequest(new LatLng(0,0), new LatLng(0,0),"","","",rider, 0);
        driver.acceptRequest(newRequest);

        assertTrue(driver.getAcceptedRequests().contains(newRequest.getId()));
        RideRequestController.getRequestList().clear();

    }

    public void testDriverCompleteRide() throws Exception{
        User rider = new User("test1", "test@example.com", "1234567890");
        User driver = new User("test2", "test2@example.com", "1234567891");

        RideRequest newRequest = new RideRequest(new LatLng(0,0), new LatLng(0,0),"","","",rider, 0);
        driver.driverCompleteRide(newRequest);

        assertTrue(newRequest.getStatus().equals("Driver Confirmed Completion"));
        RideRequestController.getRequestList().clear();

    }

    public void testReceivePay() throws Exception{
        fail("Not implement yet");
    }

    public void testGetRequests() throws Exception{
        User rider = new User("test1", "test@example.com", "1234567890");

        RideRequest newRequest = new RideRequest(new LatLng(0,0), new LatLng(0,0),"","","",rider, 0);
        rider.postRideRequest(newRequest);

        List<Integer> requestList = rider.getRequests();
        assertEquals((int)requestList.get(0), newRequest.getId());

        RideRequestController.getRequestList().clear();
    }

    public void testGetAcceptedRequests() throws Exception{
        User rider = new User("test1", "test@example.com", "1234567890");
        User driver = new User("test2", "test2@example.com", "1234567891");

        RideRequest newRequest = new RideRequest(new LatLng(0,0), new LatLng(0,0),"","","",rider, 0);
        driver.acceptRequest(newRequest);

        List<Integer> requestList = driver.getAcceptedRequests();
        assertEquals((int)requestList.get(0), newRequest.getId());
        RideRequestController.getRequestList().clear();

    }

    public void testPostRideRequest() throws Exception{
        User rider = new User("test1", "test@example.com", "1234567890");

        RideRequest newRequest = new RideRequest(new LatLng(0,0), new LatLng(0,0),"","","",rider, 0);
        rider.postRideRequest(newRequest);

        assertTrue(rider.getRequests().contains(newRequest.getId()));
        assertNotNull(RideRequestController.getRequestList().getRequestWithHash(newRequest.getId()));

        RideRequestController.getRequestList().clear();


    }



    public void testConfirmAcception() throws Exception {
        User rider = new User("test1", "test@example.com", "1234567890");
        User driver = new User("test2", "test2@example.com", "1234567891");

        RideRequest newRequest = new RideRequest(new LatLng(0,0), new LatLng(0,0),"","","",rider, 0);
        driver.confirmAcception(newRequest, driver);

        assertEquals(newRequest.getDriver().getUsername(), driver.getUsername());
        assertEquals(newRequest.getDriver().getEmail(), driver.getEmail());
        assertEquals(newRequest.getDriver().getPhoneNum(), driver.getPhoneNum());

        RideRequestController.getRequestList().clear();


    }

    public void testCancelRequest() throws Exception{
        User rider = new User("test1", "test@example.com", "1234567890");

        RideRequest newRequest = new RideRequest(new LatLng(0,0), new LatLng(0,0),"","","",rider, 0);
        rider.postRideRequest(newRequest);

        assertTrue(rider.getRequests().contains(newRequest.getId()));

        rider.cancelRequest(newRequest);
        assertFalse(rider.getRequests().contains(newRequest.getId()));
        RideRequestController.getRequestList().clear();

    }

    public void testRiderCompleteRide() throws Exception {
        User rider = new User("test1", "test@example.com", "1234567890");
        RideRequest newRequest = new RideRequest(new LatLng(0,0), new LatLng(0,0),"","","",rider, 0);

        rider.riderCompleteRide(newRequest);
        RideRequestController.getRequestList().clear();


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