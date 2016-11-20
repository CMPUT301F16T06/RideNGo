package assignment1.ridengo;

import android.app.Application;
import android.test.ApplicationTestCase;

import com.google.android.gms.maps.model.LatLng;

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
     */
    public void testUS010101() {
        User rider1 = new User("User1", "user1@gmail.com", "8888888888");
        //User rider1 = new User(user1);
        Double fare = 50.0;
        //RideRequest newRequest = new RideRequest(new LatLng(0, 0), new LatLng(0, 0), "From start to end", rider1, fare);
        RideRequest newRequest = new RideRequest("", "", "From start to end", rider1, fare);
        rider1.postRideRequest(newRequest);
        assertTrue(rider1.getRequests().contains(newRequest.getId()));

        UserController.getUserList().clear();
        RideRequestController.getRequestList().clear();
    }

    /**
     * Test us 010201.
     */
    public void testUS010201() {
        User rider1 = new User("User1", "user1@gmail.com", "8888888888");
        //User rider1 = new User(user1);
        Double fare = 50.0;
        //RideRequest newRequest1 = new RideRequest(new LatLng(0, 0), new LatLng(0, 0), "From start1 to end", rider1, fare);
        RideRequest newRequest1 = new RideRequest("", "", "From start to end", rider1, fare);

        rider1.postRideRequest(newRequest1);


        RideRequest newRequest = new RideRequest("", "", "From start to end", rider1, fare);
        //RideRequest newRequest = new RideRequest(new LatLng(1, 1), new LatLng(1, 1), "From start1 to end", rider1, fare);
        rider1.postRideRequest(newRequest);
        assertTrue(rider1.getRequests().contains(newRequest));

        User driver2 = new User("User2", "user2@gmail.com", "8888888888");
        //User driver2 = new UserDriver(user2);
        driver2.acceptRequest(newRequest);
        assertTrue(rider1.isNotified());

        UserController.getUserList().clear();
        RideRequestController.getRequestList().clear();
    }

    /**
     * Test us 010401.
     */
    public void testUS010401() {
        RideRequestController.loadRequestListFromServer();

        User rider1 = new User("User1", "user1@gmail.com", "8888888888");
        //UserRider rider1 = new UserRider(user1);
        Double fare = 50.0;
        //RideRequest newRequest = new RideRequest(new LatLng(0, 0), new LatLng(0, 0), "From start to end", rider1, fare);
        RideRequest newRequest = new RideRequest("", "", "From start to end", rider1, fare);

        rider1.postRideRequest(newRequest);
        assertTrue(rider1.getRequests().contains(newRequest.getId()));

        rider1.cancelRequest(newRequest);
        assertTrue(newRequest.getStatus().equals("Cancelled"));

        UserController.getUserList().clear();
        RideRequestController.getRequestList().clear();
    }

    /**
     * Test us 010501.
     */
    public void testUS010501() {
        User rider1 = new User("User1", "user1@gmail.com", "8888888888");
        //UserRider rider1 = new UserRider(user1);
        Double fare = 50.0;
        //RideRequest newRequest = new RideRequest(new LatLng(0, 0), new LatLng(0, 0), "From start to end", rider1, fare);
        RideRequest newRequest = new RideRequest("", "", "From start to end", rider1, fare);

        rider1.postRideRequest(newRequest);
        assertTrue(rider1.getRequests().contains(newRequest.getId()));

        User driver2 = new User("User2", "user2@gmail.com", "8888888888");
        //UserDriver driver2 = new UserDriver(user2);
        driver2.acceptRequest(newRequest);
        assertTrue(rider1.isNotified());

        assertTrue(newRequest.getAcceptions().get(0).getPhoneNum().equals(driver2.getPhoneNum()));
        assertTrue(newRequest.getAcceptions().get(0).getEmail().equals(driver2.getEmail()));

        UserController.getUserList().clear();
        RideRequestController.getRequestList().clear();
    }

    /**
     * Test us 010601.
     */
    public void testUS010601() {
        User rider1 = new User("User1", "user1@gmail.com", "8888888888");
        //UserRider rider1 = new UserRider(user1);
        Double fare = 50.0;
        //RideRequest newRequest = new RideRequest(new LatLng(0, 0), new LatLng(0, 0), "From start to end", rider1, fare);
        RideRequest newRequest = new RideRequest("", "", "From start to end", rider1, fare);

        rider1.postRideRequest(newRequest);
        assertTrue(rider1.getRequests().contains(newRequest.getId()));

        User driver2 = new User("User2", "user2@gmail.com", "8888888888");
        //UserDriver driver2 = new UserDriver(user2);
        driver2.acceptRequest(newRequest);
        assertTrue(rider1.isNotified());

        rider1.confirmAcception(newRequest, newRequest.getAcceptions().get(0));
        driver2.driverCompleteRide(newRequest);
        assertTrue(newRequest.getStatus().equals("Driver Confirmed Completion"));
        rider1.riderCompleteRide(newRequest);
        assertTrue(newRequest.getStatus().equals("Completed"));

        UserController.getUserList().clear();
        RideRequestController.getRequestList().clear();
    }

    /**
     * Test us 010701.
     */
    public void testUS010701() {
        User rider1 = new User("User1", "user1@gmail.com", "8888888888");
        //UserRider rider1 = new UserRider(user1);
        Double fare = 50.0;
        //RideRequest newRequest = new RideRequest(new LatLng(0, 0), new LatLng(0, 0), "From start to end", rider1, fare);
        RideRequest newRequest = new RideRequest("", "", "From start to end", rider1, fare);

        rider1.postRideRequest(newRequest);
        assertTrue(rider1.getRequests().contains(newRequest.getId()));

        User driver2 = new User("User2", "user2@gmail.com", "8888888888");
        //UserDriver driver2 = new UserDriver(user2);
        driver2.acceptRequest(newRequest);
        assertTrue(rider1.isNotified());

        User driver3 = new User("User3", "user3@gmail.com", "8888888888");
        //UserDriver driver3 = new UserDriver(user3);
        driver3.acceptRequest(newRequest);
        assertTrue(rider1.isNotified());

        rider1.confirmAcception(newRequest, newRequest.getAcceptions().get(0));
        assertTrue(newRequest.getDriver().equals(driver2));

        UserController.getUserList().clear();
        RideRequestController.getRequestList().clear();
    }

    public void testUS010901(){
        /**
         * test getvehicle info
         */

        User rider = new User("rider", "rider@gmail.com", "0000000000");
        User driver = new User("driver", "driver@gmail.com", "1111111111");
        Vehicle vehicle = new Vehicle("001", 2017, "Rolls Royce", "Ghost", "Black");
        driver.setVehicle(vehicle);

        RideRequest rideRequest = new RideRequest("start", "end", "", rider, 50.00);
        rider.postRideRequest(rideRequest);

        assertTrue(rider.getRequests().contains(rideRequest.getId()));

        rider.confirmAcception(rideRequest, driver);

        RideRequest request = RideRequestController.getRequestList().getRequestWithHash(rider.getRequests().get(0));
        Vehicle v = request.getDriver().getVehicle();

        assertTrue(v.getPlateNum().equals("001"));
        assertTrue(v.getYear() == 2017);
        assertTrue(v.getMake().equals("Rolls Royce"));
        assertTrue(v.getModel().equals("Ghost"));
        assertTrue(v.getColor().equals("Black"));



    }


    /**
     * Test us 020101.
     */
    public void testUS020101() {
        User rider1 = new User("User1", "user1@gmail.com", "8888888888");
        //UserRider rider1 = new UserRider(user1);
        Double fare = 50.0;
        RideRequest request;

        //RideRequest newRequest = new RideRequest(new LatLng(0, 0), new LatLng(0, 0), "From start to end", rider1, fare);
        RideRequest newRequest = new RideRequest("", "", "From start to end", rider1, fare);

        rider1.postRideRequest(newRequest);
        assertTrue(rider1.getRequests().contains(newRequest.getId()));
        request = RideRequestController.getRequestList().getRequestWithHash(rider1.getRequests().get(0));
        assertTrue(request.getStatus().equals("Posted"));

        User driver2 = new User("User2", "user2@gmail.com", "8888888888");
        //UserDriver driver2 = new UserDriver(user2);
        driver2.acceptRequest(newRequest);
        assertTrue(rider1.isNotified());

        assertTrue(rider1.getRequests().contains(newRequest));

        request = RideRequestController.getRequestList().getRequestWithHash(rider1.getRequests().get(0));
        assertTrue(request.getStatus().equals("Accepted By Driver"));
        assertTrue(driver2.getRequests().contains(newRequest));

        request = RideRequestController.getRequestList().getRequestWithHash(driver2.getRequests().get(0));
        assertTrue(request.getStatus().equals("Accepted By Driver"));

        rider1.confirmAcception(newRequest, driver2);
        request = RideRequestController.getRequestList().getRequestWithHash(rider1.getRequests().get(0));
        assertTrue(request.getStatus().equals("Driver Confirmed"));

        request = RideRequestController.getRequestList().getRequestWithHash(driver2.getRequests().get(0));
        assertTrue(request.getStatus().equals("Driver Confirmed"));

        driver2.driverCompleteRide(newRequest);
        request = RideRequestController.getRequestList().getRequestWithHash(rider1.getRequests().get(0));
        assertTrue(request.getStatus().equals("Driver Confirmed Completion"));

        request = RideRequestController.getRequestList().getRequestWithHash(driver2.getRequests().get(0));
        assertTrue(request.getStatus().equals("Driver Confirmed Completion"));

        rider1.riderCompleteRide(newRequest);

        request = RideRequestController.getRequestList().getRequestWithHash(rider1.getRequests().get(0));
        assertTrue(request.getStatus().equals("Completed"));

        request = RideRequestController.getRequestList().getRequestWithHash(driver2.getRequests().get(0));
        assertTrue(request.getStatus().equals("Completed"));

        UserController.getUserList().clear();
        RideRequestController.getRequestList().clear();
    }

    /**
     * Test us 030101.
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

    public void testUS030401() {
        User driver = new User("User1", "user1@gmail.com", "0000000000");
        UserList users = UserController.getUserList();
        users.addUser(driver);
        assertTrue(users.contains(driver));

        Vehicle vehicle = new Vehicle("001", 2017, "Rolls Royce", "Ghost", "Black");
        driver.setVehicle(vehicle);

        assertTrue(driver.getVehicle() != null);
        assertTrue(driver.getVehicle().getPlateNum().equals("001"));
        assertTrue(driver.getVehicle().getYear() == 2017);
        assertTrue(driver.getVehicle().getMake().equals("Rolls Royce"));
        assertTrue(driver.getVehicle().getModel().equals("Ghost"));
        assertTrue(driver.getVehicle().getColor().equals("Black"));

        vehicle = new Vehicle("008", 2017, "Mercedes Benz", "GT AMG", "Yellow");
        driver.setVehicle(vehicle);

        assertTrue(driver.getVehicle() != null);
        assertTrue(driver.getVehicle().getPlateNum().equals("008"));
        assertTrue(driver.getVehicle().getYear() == 2017);
        assertTrue(driver.getVehicle().getMake().equals("Mercedes Benz"));
        assertTrue(driver.getVehicle().getModel().equals("GT AMG"));
        assertTrue(driver.getVehicle().getColor().equals("Yellow"));

        UserController.getUserList().clear();

    }

    /**
     * Test us 040101.
     */
    public void testUS040101() {
        User rider1 = new User("User1", "user1@gmail.com", "8888888888");
        //UserRider rider1 = new UserRider(user1);
        Double fare = 50.0;
        //RideRequest newRequest = new RideRequest(new LatLng(0, 0), new LatLng(0, 0), "From start to end", rider1, fare);
        RideRequest newRequest = new RideRequest("", "", "From start to end", rider1, fare);

        rider1.postRideRequest(newRequest);

        User driver2 = new User("User2", "user2@gmail.com", "8888888888");
        //UserDriver driver2 = new UserDriver(user2);

        RideRequestList requests = driver2.getRequestsByGeoLocation(new LatLng(0, 0));
        assertTrue(requests.contains(newRequest));

        UserController.getUserList().clear();
        RideRequestController.getRequestList().clear();
    }

    /**
     * Test us 040201.
     */
    public void testUS040201() {
        User rider1 = new User("User1", "user1@gmail.com", "8888888888");
        //UserRider rider1 = new UserRider(user1);
        Double fare = 50.0;

        //RideRequest newRequest = new RideRequest(new LatLng(0, 0), new LatLng(0, 0), "From start to end", rider1, fare);
        RideRequest newRequest = new RideRequest("", "", "From start to end", rider1, fare);

        rider1.postRideRequest(newRequest);

        User driver2 = new User("User2", "user2@gmail.com", "8888888888");
        //UserDriver driver2 = new UserDriver(user2);

        RideRequestList requests = driver2.getRequestsByKeyword("start");
        assertTrue(requests.contains(newRequest));

        UserController.getUserList().clear();
        RideRequestController.getRequestList().clear();
    }

    /**
     * Test us 050101.
     */
    public void testUS050101() {
        User rider1 = new User("User1", "user1@gmail.com", "8888888888");
        //UserRider rider1 = new UserRider(user1);
        Double fare = 50.0;
        //RideRequest newRequest1 = new RideRequest(new LatLng(0, 0), new LatLng(0, 0), "From start to end", rider1, fare);
        RideRequest newRequest1 = new RideRequest("", "", "From start to end", rider1, fare);

        rider1.postRideRequest(newRequest1);

        User driver2 = new User("User2", "user2@gmail.com", "8888888888");
        //UserDriver driver2 = new UserDriver(user2);
        driver2.acceptRequest(newRequest1);

        rider1.confirmAcception(newRequest1, driver2);
        driver2.driverCompleteRide(newRequest1);
        rider1.riderCompleteRide(newRequest1);

        assertTrue("Payment not implemented", false);

        UserController.getUserList().clear();
        RideRequestController.getRequestList().clear();
    }

    /**
     * Test us 050201.
     */
    public void testUS050201() {
        User rider1 = new User("User1", "user1@gmail.com", "8888888888");
        //UserRider rider1 = new UserRider(user1);
        Double fare = 50.0;
        //RideRequest newRequest1 = new RideRequest(new LatLng(0, 0), new LatLng(0, 0), "From start to end", rider1, fare);
        RideRequest newRequest1 = new RideRequest("", "", "From start to end", rider1, fare);

        rider1.postRideRequest(newRequest1);
        //RideRequest newRequest2 = new RideRequest(new LatLng(0, 0), new LatLng(0, 0), "From start to end", rider1, fare);
        RideRequest newRequest2 = new RideRequest("", "", "From start to end", rider1, fare);

        rider1.postRideRequest(newRequest2);

        User driver2 = new User("User2", "user2@gmail.com", "8888888888");
        //UserDriver driver2 = new UserDriver(user2);
        driver2.acceptRequest(newRequest1);
        driver2.acceptRequest(newRequest2);

        assertTrue(driver2.getPendingRequests().contains(newRequest1));
        assertTrue(driver2.getPendingRequests().contains(newRequest2));

        UserController.getUserList().clear();
        RideRequestController.getRequestList().clear();
    }

    /**
     * Test us 050301.
     */
    public void testUS050301() {
        User rider1 = new User("User1", "user1@gmail.com", "8888888888");
        //UserRider rider1 = new UserRider(user1);
        Double fare = 50.0;

        //RideRequest newRequest1 = new RideRequest(new LatLng(0, 0), new LatLng(0, 0), "From start to end", rider1, fare);
        RideRequest newRequest1 = new RideRequest("", "", "From start to end", rider1, fare);

        rider1.postRideRequest(newRequest1);

        User driver2 = new User("User2", "user2@gmail.com", "8888888888");
        //UserDriver driver2 = new UserDriver(user2);
        driver2.acceptRequest(newRequest1);

        rider1.confirmAcception(newRequest1, driver2);
        assertTrue(newRequest1.getDriver().equals(driver2));

        UserController.getUserList().clear();
        RideRequestController.getRequestList().clear();
    }

    /**
     * Test us 050401.
     */
    public void testUS050401() {
        User rider1 = new User("User1", "user1@gmail.com", "8888888888");
        //UserRider rider1 = new UserRider(user1);
        Double fare = 50.0;
        //RideRequest newRequest1 = new RideRequest(new LatLng(0, 0), new LatLng(0, 0), "From start to end", rider1, fare);
        RideRequest newRequest1 = new RideRequest("", "", "From start to end", rider1, fare);

        rider1.postRideRequest(newRequest1);

        User driver2 = new User("User2", "user2@gmail.com", "8888888888");
        //UserDriver driver2 = new UserDriver(user2);
        driver2.acceptRequest(newRequest1);

        rider1.confirmAcception(newRequest1, driver2);
        assertTrue(driver2.isNotified());

        UserController.getUserList().clear();
        RideRequestController.getRequestList().clear();
    }

    /**
     * Test us 080101.
     */
    public void testUS080101() {
        // Without actually building code for server, runnable test for offline behavior is hard
        // to write. The basic idea is to use local data to store data and retrieve when offline.
        assertTrue(false);
    }

    /**
     * Test us 080201.
     */
    public void testUS080201() {
        // Without actually building code for server, runnable test for offline behavior is hard
        // to write. The basic idea is to use local data to store data and retrieve when offline.
        assertTrue(false);
    }

    /**
     * Test us 080301.
     */
    public void testUS080301() {
        // Without actually building code for server, runnable test for offline behavior is hard
        // to write. The basic idea is to use local data to store data and retrieve when offline.
        assertTrue(false);
    }

    /**
     * Test us 080401.
     */
    public void testUS080401() {
        // Without actually building code for server, runnable test for offline behavior is hard
        // to write. The basic idea is to use local data to store data and retrieve when offline.
        assertTrue(false);
    }

    /**
     * Test us 100101.
     */
    public void testUS100101() {
        // Without knowledge of google map api, runnable test for selecting and showing location
        // on a map
        assertTrue(false);
    }

    /**
     * Test us 100201.
     */
    public void testUS100201() {
        // Without knowledge of google map api, runnable test for selecting and showing location
        // on a map
        assertTrue(false);
    }
}