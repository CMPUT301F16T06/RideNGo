package assignment1.ridengo;

import android.app.Application;
import android.location.Location;
import android.test.ApplicationTestCase;

import com.google.android.gms.maps.model.LatLng;

import java.util.Locale;

/**
 * The type Application test.
 */
public class ApplicationTest extends ApplicationTestCase<Application> {
    /**
     * Instantiates a new Application test.
     */
    public ApplicationTest() {
        super(Application.class);
    }

    /**
     * Test us 010101.
     * As a rider, I want to request ride between two location
     */
    public void testUS010101() {
        User rider1 = new User("User1", "user1@gmail.com", "8888888888");

        Double fare = 50.0;
        RideRequest newRequest = new RideRequest(new LatLng(0,0), new LatLng(0,0),"", "", "From start to end", rider1, fare);

        UserController.getUserList().addUser(rider1);
        rider1.postRideRequest(newRequest);

        assertTrue(rider1.getRequests().contains(newRequest.getId()));

        UserController.getUserList().clear();
        RideRequestController.getRequestList().clear();
    }

    /**
     * Test us 010201.
     * As a rider, I want to see current requests I have opened
     */
    public void testUS010201() {
        User rider1 = new User("User1", "user1@gmail.com", "8888888888");
        UserController.getUserList().addUser(rider1);

        User rider2= new User("User2", "user2@gmail.com","");
        UserController.getUserList().addUser(rider2);

        Double fare = 50.0;
        RideRequest newRequest1 = new RideRequest(new LatLng(0,0), new LatLng(0,0),"", "", "From start to end", rider1, fare);
        rider1.postRideRequest(newRequest1);
        assertTrue(rider1.getRequests().contains(newRequest1.getId()));

        RideRequest newRequest2 = new RideRequest(new LatLng(0,0), new LatLng(0,0),"", "", "From start to end", rider1, fare);
        rider1.postRideRequest(newRequest2);
        assertTrue(rider1.getRequests().contains(newRequest2.getId()));

        RideRequest newRequest3 = new RideRequest(new LatLng(0,0), new LatLng(0,0),"", "", "From start to end", rider2, fare);
        assertFalse(rider2.getRequests().contains(newRequest3));
        rider2.postRideRequest(newRequest3);
        assertTrue(rider2.getRequests().contains(newRequest3.getId()));

        UserController.getUserList().clear();
        RideRequestController.getRequestList().clear();
    }

    /**
     *Test us 010301
     * As a rider, I want to be notified if my request is accepted
     */
    public void testUS010301(){
        User rider1 = new User("User1", "user1@gmail.com", "8888888888");
        UserController.getUserList().addUser(rider1);

        User driver1= new User("User2", "user2@gmail.com","");
        UserController.getUserList().addUser(driver1);

        Double fare = 50.0;
        RideRequest newRequest1 = new RideRequest(new LatLng(0,0), new LatLng(0,0),"", "", "From start to end", rider1, fare);
        rider1.postRideRequest(newRequest1);
        assertTrue(rider1.getRequests().contains(newRequest1.getId()));

        driver1.acceptRequest(RideRequestController.getRequestList().getRequestWithHash(rider1.getRequests().get(0)));
        assertTrue(newRequest1.isNotifyRider());

        UserController.getUserList().clear();
        RideRequestController.getRequestList().clear();
    }

    /**
     * Test us 010401.
     * As a rider, I want to cancel requests
     */
    public void testUS010401() {
        User rider1 = new User("User1", "user1@gmail.com", "8888888888");
        UserController.getUserList().addUser(rider1);

        Double fare = 50.0;
        RideRequest newRequest = new RideRequest(new LatLng(0,0), new LatLng(0,0),"", "", "From start to end", rider1, fare);

        rider1.postRideRequest(newRequest);
        assertTrue(rider1.getRequests().contains(newRequest.getId()));

        rider1.cancelRequest(newRequest);
        assertFalse(rider1.getRequests().contains(newRequest.getId()));

        UserController.getUserList().clear();
        RideRequestController.getRequestList().clear();
    }

    /**
     * Test us 010501.
     * As a rider, I want to be able to phone or email the driver who accepted a request.
     */
    public void testUS010501() {
        User rider1 = new User("User1", "user1@gmail.com", "8888888888");
        UserController.getUserList().addUser(rider1);

        User driver2 = new User("User2", "user2@gmail.com", "11234567890");
        UserController.getUserList().addUser(driver2);

        Double fare = 50.0;
        RideRequest newRequest = new RideRequest(new LatLng(0,0), new LatLng(0,0),"", "", "From start to end", rider1, fare);

        rider1.postRideRequest(newRequest);
        assertTrue(rider1.getRequests().contains(newRequest.getId()));

        driver2.acceptRequest(RideRequestController.getRequestList().getRequestWithHash(rider1.getRequests().get(0)));
        assertTrue(newRequest.isNotifyRider());

        assertTrue(newRequest.getAcceptions().get(0).getPhoneNum().equals(driver2.getPhoneNum()));
        assertTrue(newRequest.getAcceptions().get(0).getEmail().equals(driver2.getEmail()));

        UserController.getUserList().clear();
        RideRequestController.getRequestList().clear();
    }

    /**
     * Test us 010601.
     * As a rider, I want an estimate of a fair fare to offer to drivers.
     */
    public void testUS010601() {
        User rider1 = new User("User1", "user1@gmail.com", "8888888888");
        UserController.getUserList().addUser(rider1);

        Location start = new Location("");
        start.setLatitude(0.00);
        start.setLongitude(50.00);

        Location end = new Location("");
        end.setLatitude(50.00);
        end.setLongitude(0.00);

        float distance = (start.distanceTo(end))/1000;
        double fare = (double) distance * 2;
        RideRequest newRequest = new RideRequest(new LatLng(0,50), new LatLng(50,0),"", "", "From start to end", rider1, fare);

        start.setLatitude(0.00);
        start.setLongitude(100.00);
        end.setLatitude(100.00);
        end.setLongitude(0.00);
        distance = (start.distanceTo(end))/1000;
        fare = (double) distance * 2;

        RideRequest newRequest1 = new RideRequest(new LatLng(0,100), new LatLng(100,0),"", "", "From start to end", rider1, fare);

        assertTrue(newRequest1.getFare()>newRequest.getFare());

        UserController.getUserList().clear();
        RideRequestController.getRequestList().clear();
    }

    /**
     * Test us 010701.
     * As a rider, I want to confirm the completion of a request and enable payment.
     */
    public void testUS010701() {
        User rider1 = new User("User1", "user1@gmail.com", "8888888888");
        UserController.getUserList().addUser(rider1);

        User driver2 = new User("User2", "user2@gmail.com", "8888888888");
        UserController.getUserList().addUser(driver2);

        //Add Request
        Double fare = 50.0;
        RideRequest newRequest = new RideRequest(new LatLng(0,0), new LatLng(0,0),"", "", "From start to end", rider1, fare);
        rider1.postRideRequest(newRequest);
        assertTrue(rider1.getRequests().contains(newRequest.getId()));

        driver2.acceptRequest(RideRequestController.getRequestList().getRequestWithHash(rider1.getRequests().get(0)));
        assertTrue(newRequest.isNotifyRider());

        rider1.riderCompleteRide(newRequest);
        assertTrue(newRequest.getStatus().trim().equals("Trip Completed"));

        UserController.getUserList().clear();
        RideRequestController.getRequestList().clear();
    }

    /**
     * Test us 01.08.01
     * As a rider, I want to confirm a driver's acceptance.
     * This allows us to choose from a list of acceptances if more than 1 driver accepts simultaneously.
     */
    public void testUS010801(){
        User rider1 = new User("User1", "user1@gmail.com", "8888888888");
        UserController.getUserList().addUser(rider1);

        User driver2 = new User("User2", "user2@gmail.com", "8888888888");
        UserController.getUserList().addUser(driver2);

        User driver3 = new User("User3", "user3@gmail.com", "8888888888");
        UserController.getUserList().addUser(driver3);

        //Add Request
        Double fare = 50.0;
        RideRequest newRequest = new RideRequest(new LatLng(0,0), new LatLng(0,0),"", "", "From start to end", rider1, fare);
        rider1.postRideRequest(newRequest);
        assertTrue(rider1.getRequests().contains(newRequest.getId()));

        driver2.acceptRequest(RideRequestController.getRequestList().getRequestWithHash(rider1.getRequests().get(0)));
        assertTrue(newRequest.isNotifyRider());

        driver3.acceptRequest(RideRequestController.getRequestList().getRequestWithHash(rider1.getRequests().get(0)));
        assertTrue(newRequest.isNotifyRider());

        assertTrue(newRequest.getAcceptions().contains(driver2));
        assertTrue(newRequest.getAcceptions().contains(driver3));

        rider1.confirmAcception(newRequest, driver2);

        assertTrue(newRequest.getDriver().getUsername().equals(driver2.getUsername()));

        UserController.getUserList().clear();
        RideRequestController.getRequestList().clear();
    }

    /**
     * Test us 010901
     * As a rider, I should see a description of the driver's vehicle.
     */
    public void testUS010901(){
        User rider = new User("rider", "rider@gmail.com", "0000000000");
        UserController.getUserList().addUser(rider);
        User driver = new User("driver", "driver@gmail.com", "1111111111");
        UserController.getUserList().addUser(driver);

        Vehicle vehicle = new Vehicle("001", 2017, "Rolls Royce", "Ghost", "Black");
        driver.setVehicle(vehicle);

        RideRequest rideRequest = new RideRequest(new LatLng(0,0), new LatLng(0,0),"start", "end", "", rider, 50.00);
        rider.postRideRequest(rideRequest);

        assertTrue(rider.getRequests().contains(rideRequest.getId()));
        driver.acceptRequest(RideRequestController.getRequestList().getRequestWithHash(rider.getRequests().get(0)));

        Vehicle v = rideRequest.getAcceptions().get(0).getVehicle();

        assertTrue(v.getPlateNum().equals("001"));
        assertTrue(v.getYear() == 2017);
        assertTrue(v.getMake().equals("Rolls Royce"));
        assertTrue(v.getModel().equals("Ghost"));
        assertTrue(v.getColor().equals("Black"));

        UserController.getUserList().clear();
        RideRequestController.getRequestList().clear();
    }

    /**
     * Test US 1.10.01 (added 2016-11-14)
     * As a rider, I want to see some summary rating of the drivers who accepted my offers.
    */
    public void testUS011001(){
        User rider = new User("rider", "rider@gmail.com", "0000000000");
        UserController.getUserList().addUser(rider);
        User driver = new User("driver", "driver@gmail.com", "1111111111");
        UserController.getUserList().addUser(driver);
        driver.setRating(5);

        RideRequest rideRequest = new RideRequest(new LatLng(0,0), new LatLng(0,0),"start", "end", "", rider, 50.00);
        rider.postRideRequest(rideRequest);

        assertTrue(rider.getRequests().contains(rideRequest.getId()));
        driver.acceptRequest(RideRequestController.getRequestList().getRequestWithHash(rider.getRequests().get(0)));

        User acceptDriver = rideRequest.getAcceptions().get(0);

        assertEquals(acceptDriver.getRating(), (float)5.00);
        assertEquals(acceptDriver.getNumRatings(), 1);
        assertEquals(acceptDriver.getTotalOfRating(), 5);
        UserController.getUserList().clear();
        RideRequestController.getRequestList().clear();
    }

    /**
     * US 1.11.01 (added 2016-11-14)
     * As a rider, I want to rate a driver for his/her service (1-5).
     */
    public void testUS011101(){
        User rider = new User("rider", "rider@gmail.com", "0000000000");
        UserController.getUserList().addUser(rider);
        User driver = new User("driver", "driver@gmail.com", "1111111111");
        UserController.getUserList().addUser(driver);

        RideRequest rideRequest = new RideRequest(new LatLng(0,0), new LatLng(0,0),"start", "end", "", rider, 50.00);
        rider.postRideRequest(rideRequest);
        driver.acceptRequest(RideRequestController.getRequestList().getRequestWithHash(rider.getRequests().get(0)));
        rider.confirmAcception(rideRequest, rideRequest.getAcceptions().get(0));
        rider.riderCompleteRide(rideRequest);

        //Should able to rate rider
        RideRequestController.getRequestList().getRequestWithHash(rider.getRequests().get(0)).getDriver().setRating(5);

        assertEquals(driver.getRating(), (float)5);
        assertEquals(driver.getNumRatings(), 1);
        assertEquals(driver.getTotalOfRating(), 5);

        UserController.getUserList().clear();
        RideRequestController.getRequestList().clear();
    }

    /**
     * Test us 020101.
     * As a rider or driver, I want to see the status of a request that I am involved in
     */
    public void testUS020101() {
        String waitForDriver = "Waiting for Driver";
        String waitForConfirmation = "Waiting for Confirmation";
        String tripConfirmed = "Driver Confirmed";
        String tripCompleted = "Trip Completed";
        String requestCanceled = "Request canceled";

        User rider1 = new User("User1", "user1@gmail.com", "8888888888");
        UserController.getUserList().addUser(rider1);
        User driver2 = new User("User2", "user2@gmail.com", "8888888888");
        UserController.getUserList().addUser(driver2);

        Double fare = 50.0;
        RideRequest riderRequest = new RideRequest(new LatLng(0,0), new LatLng(0,0),"", "", "From start to end", rider1, fare);
        RideRequest driverRequest;
        //Post Request
        rider1.postRideRequest(riderRequest);
        assertTrue(rider1.getRequests().contains(riderRequest.getId()));
        assertTrue(riderRequest.getStatus().equals(waitForDriver));
        //Accepted By Driver
        driverRequest = RideRequestController.getRequestList().getRequestWithHash(rider1.getRequests().get(0));
        driver2.acceptRequest(driverRequest);

        assertTrue(driver2.getAcceptedRequests().contains(driverRequest.getId()));
        assertTrue(driverRequest.getStatus().equals(waitForConfirmation));
        assertTrue(riderRequest.getStatus().equals(waitForConfirmation));
        //Confirm driver
        rider1.confirmAcception(riderRequest, riderRequest.getAcceptions().get(0));

        assertTrue(riderRequest.getStatus().equals(tripConfirmed));
        //assertTrue(driverRequest.isNotifyDriver());
        assertTrue(driverRequest.getStatus().equals(tripConfirmed));

        rider1.riderCompleteRide(riderRequest);
        assertTrue(riderRequest.getStatus().equals(tripCompleted));
        assertTrue(driverRequest.getStatus().equals(tripCompleted));

        UserController.getUserList().clear();
        RideRequestController.getRequestList().clear();
    }

    /**
     * Test us 030101.
     * As a user, I want a profile with a unique username and my contact information.
     */
    public void testUS030101() {
        User user1 = new User("User1", "user1@gmail.com", "8888888888");
        UserList users = UserController.getUserList();
        users.addUser(user1);
        assertTrue(users.contains(user1));
        User user2 = new User("User1", "user1@gmail.com", "8888888888");
        try {
            users.addUser(user2);
            assertTrue("Should not reach here", false);
        } catch (RuntimeException e) {
            assertTrue("User2 not being added", true);
        }

        UserController.getUserList().clear();
        RideRequestController.getRequestList().clear();
    }

    /**
     * Test us 030201.
     * As a user, I want to edit the contact information in my profile.
     */
    public void testUS030201() {
        User user1 = new User("User1", "user1@gmail.com", "8888888888");
        UserList users = UserController.getUserList();
        users.addUser(user1);
        assertTrue(users.contains(user1));
        String newEmail = "newuser1@gmail.com";
        user1.setEmail(newEmail);
        assertTrue(user1.getEmail().equals(newEmail));
        String newPhone = "9999999999";
        user1.setPhoneNum(newPhone);
        assertTrue(user1.getPhoneNum().equals(newPhone));

        UserController.getUserList().clear();
        RideRequestController.getRequestList().clear();
    }

    /**
     * Test us 030301.
     * As a user, I want to, when a username is presented for a thing, retrieve and show its contact information.
     */
    public void testUS030301() {
        User user1 = new User("User1", "user1@gmail.com", "8888888888");
        UserList users = UserController.getUserList();
        users.addUser(user1);
        assertTrue(users.contains(user1));

        User user2 = new User("User2", "user2@gmail.com", "8888888888");
        users.addUser(user2);
        assertTrue(users.contains(user2));

        User user = users.getUserByUsername("User2");
        assertTrue(user.getPhoneNum().equals(user2.getPhoneNum()));
        assertTrue(user.getEmail().equals(user2.getEmail()));

        UserController.getUserList().clear();
        RideRequestController.getRequestList().clear();
    }

    /**
     * Test us 030401
     * As a driver, in my profile I can provide details about the vehicle I drive.
     */
    public void testUS030401() {
        User driver = new User("User1", "user1@gmail.com", "0000000000");
        UserList users = UserController.getUserList();
        users.addUser(driver);
        assertTrue(users.contains(driver));

        Vehicle vehicle = new Vehicle("001", 2017, "Rolls Royce", "Ghost", "Black");
        driver.setVehicle(vehicle);

        assertNotNull(driver.getVehicle());
        assertTrue(driver.getVehicle().getPlateNum().equals("001"));
        assertTrue(driver.getVehicle().getYear() == 2017);
        assertTrue(driver.getVehicle().getMake().equals("Rolls Royce"));
        assertTrue(driver.getVehicle().getModel().equals("Ghost"));
        assertTrue(driver.getVehicle().getColor().equals("Black"));

        vehicle = new Vehicle("008", 2017, "Mercedes Benz", "GT AMG", "Yellow");
        driver.setVehicle(vehicle);

        assertNotNull(driver.getVehicle());
        assertTrue(driver.getVehicle().getPlateNum().equals("008"));
        assertTrue(driver.getVehicle().getYear() == 2017);
        assertTrue(driver.getVehicle().getMake().equals("Mercedes Benz"));
        assertTrue(driver.getVehicle().getModel().equals("GT AMG"));
        assertTrue(driver.getVehicle().getColor().equals("Yellow"));

        UserController.getUserList().clear();
        RideRequestController.getRequestList().clear();

    }

    /**
     * Test us 040101.
     * As a driver, I want to browse and search for open requests by geo-location.
     */
    public void testUS040101() {
        fail("Not implement yet");

    }

    /**
     * Test us 040201.
     * As a driver, I want to browse and search for open requests by keyword.
     */
    public void testUS040201() {
        fail("Not implement yet");
    }

    /**
     * Test 040301
     * As a driver, I should be able filter request searches by price per KM and price.
     */
    public void testUS040301(){

        fail("Not implemenet yet");
    }

    /**
     * Test us 050101.
     * As a driver,  I want to accept a request I agree with and accept that offered payment upon completion.
     */
    public void testUS050101() {
        User rider1 = new User("User1", "user1@gmail.com", "8888888888");
        UserController.getUserList().addUser(rider1);

        Double fare = 50.0;
        RideRequest newRequest = new RideRequest(new LatLng(0,0), new LatLng(0,0),"", "", "From start to end", rider1, fare);

        rider1.postRideRequest(newRequest);

        User driver2 = new User("User2", "user2@gmail.com", "8888888888");
        UserController.getUserList().addUser(driver2);
        driver2.acceptRequest(newRequest);

        rider1.confirmAcception(newRequest, driver2);
        driver2.driverCompleteRide(newRequest);
        assertTrue(newRequest.getStatus().equals("Driver Confirmed Completion"));



        UserController.getUserList().clear();
        RideRequestController.getRequestList().clear();

        fail("Rider still need to confirm completion");
    }

    /**
     * Test us 050201.
     * As a driver, I want to view a list of things I have accepted that are pending,
     * each request with its description, and locations.
     */
    public void testUS050201() {
        User rider1 = new User("User1", "user1@gmail.com", "8888888888");
        UserController.getUserList().addUser(rider1);

        Double fare = 50.0;
        RideRequest newRequest1 = new RideRequest(new LatLng(0,0), new LatLng(0,0),"", "", "From start to end", rider1, fare);
        rider1.postRideRequest(newRequest1);

        RideRequest newRequest2 = new RideRequest(new LatLng(0,0), new LatLng(0,0),"", "", "From start to end", rider1, fare);
        rider1.postRideRequest(newRequest2);

        User driver2 = new User("User2", "user2@gmail.com", "8888888888");
        driver2.acceptRequest(newRequest1);
        driver2.acceptRequest(newRequest2);

        assertTrue(driver2.getAcceptedRequests().contains(newRequest1.getId()));
        assertTrue(driver2.getAcceptedRequests().contains(newRequest2.getId()));

        UserController.getUserList().clear();
        RideRequestController.getRequestList().clear();
    }

    /**
     * Test us 050301.
     * As a driver, I want to see if my acceptance was accepted.
     */
    public void testUS050301() {
        User rider1 = new User("User1", "user1@gmail.com", "8888888888");
        //UserRider rider1 = new UserRider(user1);
        Double fare = 50.0;

        RideRequest newRequest1 = new RideRequest(new LatLng(0,0), new LatLng(0,0),"", "", "From start to end", rider1, fare);

        rider1.postRideRequest(newRequest1);

        User driver2 = new User("User2", "user2@gmail.com", "8888888888");
        driver2.acceptRequest(newRequest1);

        rider1.confirmAcception(newRequest1, driver2);
        assertTrue(newRequest1.getDriver().equals(driver2));

        UserController.getUserList().clear();
        RideRequestController.getRequestList().clear();
    }

    /**
     * Test us 050401.
     * US 05.04.01
     * As a driver, I want to be notified if my ride offer was accepted.
     */
    public void testUS050401() {
        User rider1 = new User("User1", "user1@gmail.com", "8888888888");

        Double fare = 50.0;
        RideRequest newRequest1 = new RideRequest(new LatLng(0,0), new LatLng(0,0),"", "", "From start to end", rider1, fare);
        rider1.postRideRequest(newRequest1);

        User driver2 = new User("User2", "user2@gmail.com", "8888888888");
        driver2.acceptRequest(newRequest1);

        rider1.confirmAcception(newRequest1, driver2);
        assertTrue(newRequest1.isNotifyDriver());

        UserController.getUserList().clear();
        RideRequestController.getRequestList().clear();
    }

    /**
     * Test us 080101.
     * As an driver, I want to see requests that I already accepted while offline.
     */
    public void testUS080101() {
        // Without actually building code for server, runnable test for offline behavior is hard
        // to write. The basic idea is to use local data to store data and retrieve when offline.
        fail("Not Implement yet");
    }

    /**
     * Test us 080201.
     * As a rider, I want to see requests that I have made while offline.
     */
    public void testUS080201() {
        // Without actually building code for server, runnable test for offline behavior is hard
        // to write. The basic idea is to use local data to store data and retrieve when offline.
        fail("Not Implement yet");
    }

    /**
     * Test us 080301.
     * As a rider, I want to make requests that will be sent once I get connectivity again.
     */
    public void testUS080301() {
        // Without actually building code for server, runnable test for offline behavior is hard
        // to write. The basic idea is to use local data to store data and retrieve when offline.
        fail("Not Implement yet");
    }

    /**
     * Test us 080401.
     * As a driver, I want to accept requests that will be sent once I get connectivity again.
     */
    public void testUS080401() {
        // Without actually building code for server, runnable test for offline behavior is hard
        // to write. The basic idea is to use local data to store data and retrieve when offline.
        fail("Not Implement yet");
    }

    /**
     * Test us 100101.
     * As a rider, I want to specify a start and end geo locations on a map for a request.
     */
    public void testUS100101() {
        User rider1 = new User("User1", "user1@gmail.com", "8888888888");
        Double fare = 50.0;
        RideRequest newRequest1 = new RideRequest(new LatLng(0,0), new LatLng(0,0),"", "", "From start to end", rider1, fare);

        //The rider able select a point on map activity. And then the point will be convert to LatLng()
        LatLng newStart = new LatLng(50, 100);
        LatLng newEnd = new LatLng(-50, -100);

        newRequest1.setStartCoord(newStart);
        newRequest1.setEndCoord(newEnd);
        rider1.postRideRequest(newRequest1);

        UserController.getUserList().clear();
        RideRequestController.getRequestList().clear();
    }

    /**
     * Test us 100201.
     * As a driver, I want to view start and end geo locations on a map for a request.
     */
    public void testUS100201() {
        //When a driver view on a map, map activity will extract the start and end coordinate point and display on map
        User rider1 = new User("User1", "user1@gmail.com", "8888888888");
        Double fare = 50.0;
        RideRequest newRequest1 = new RideRequest(new LatLng(0,0), new LatLng(0,0),"", "", "From start to end", rider1, fare);
        rider1.postRideRequest(newRequest1);

        User driver = new User("User2", "user2@gmail.com", "888888888");
        RideRequest getRequest = RideRequestController.getRequestList().getRequestWithHash(rider1.getRequests().get(0));

        LatLng start = getRequest.getStartCoord();
        LatLng end = getRequest.getEndCoord();

        assertNotNull(start);
        assertNotNull(end);

        UserController.getUserList().clear();
        RideRequestController.getRequestList().clear();
    }
}