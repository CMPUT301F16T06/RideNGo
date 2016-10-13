package assignment1.ridengo;

import android.app.Application;
import android.test.ApplicationTestCase;
import android.widget.ArrayAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * <a href="http://d.android.com/tools/testing/testing_android.html">Testing Fundamentals</a>
 */
public class ApplicationTest extends ApplicationTestCase<Application> {
    public ApplicationTest() {
        super(Application.class);
    }

    public void testUserProfile(){
        // US 03.01.01
        User user1 = new User("User1", "password1", "user1@gmail.com", "8888888888");
        UserList users = UserController.getUserList();
        users.addUser(user1);
        assertTrue(users.contains(user1));
        User user2 = new User("User1", "password2", "user1@gmail.com", "8888888888");
        try{
            users.addUser(user2);
            assertTrue("Should not reach here", false);
        }catch(RuntimeException e){
            assertTrue("User2 not being added", true);
        }

        // US 03.02.01
        String newEmail = "newUser1@gmail.com";
        user1.setEmail(newEmail);
        assertTrue(newEmail.equals(user1.getEmail()));

        String newPhone = "77777777777";
        user1.setPhoneNum(newPhone);
        assertTrue(newPhone.equals(user1.getPhoneNum()));

        // US 03.03.01
        User user3 = new User("User3", "password3", "user3@gmail.com", "33333333333");
        users.addUser(user3);
        User user = users.getUserByUsername("User3");
        assertTrue(user.getEmail().equals("user3@gmail.com"));
        assertTrue(user.getPhoneNum().equals("33333333333"));

        RideRequestController.getRequestList().clear();
        UserController.getUserList().clear();
    }

    public void testRequests(){
        // US 01.01.01
        Rider rider1 = new Rider("Rider1", "password1", "rider1@gmail.com", "8888888888");
        Double fare = 50.0;
        RideRequest newRequest = new RideRequest("Start", "End", "From start to end", rider1, fare);
        rider1.postRideRequest(newRequest);

        // US 01.02.01
        RideRequest request = rider1.getRequests().get(0);
        assertTrue(request.equals(newRequest));

        // US 01.03.01
        // Notifications not implemented

        // US 01.04.01, US 02.01.01
        rider1.cancelRequest(request);
        assertTrue(request.getStatus().equals("Cancelled"));

        // US 01.05.01, US 05.01.01
        Driver driver1 = new Driver("Driver1", "password1", "driver1@gmail.com", "77777777777");
        driver1.acceptRequest(request);

        Driver driver = rider1.getRequests().get(0).getAcceptions().get(0);
        assertTrue(driver.getUsername().equals("Driver1"));
        assertTrue(driver.getEmail().equals("driver1@gmail.com"));
        assertTrue(driver.getPhoneNum().equals("77777777777"));

        // US 01.06.01
        assertTrue(request.getFare().equals(fare));

        // US 01.08.01, US 05.01.01
        Driver driver2 = new Driver("Driver2", "password2", "driver2@gmail.com", "66666666666");
        driver2.acceptRequest(request);
        rider1.acceptAcception(request, driver);
        assertTrue(request.getDriver().equals(driver1));

        // US 05.03.01
        assertTrue("Accepted Driver1's Acception", request.getDriver().equals(driver1));
        assertFalse("Not Accepted Driver2's Acception", request.getDriver().equals(driver2));

        // US 05.02.01
        List<RideRequest> driver1Requests = driver1.getPendingRequests();
        RideRequest request1 = driver1Requests.get(0);
        assertTrue(request1.getStartPoint().equals("Start"));
        assertTrue(request1.getEndPoint().equals("End"));
        assertTrue(request1.getDescription().equals("From start to end"));

        // US 01.07.01, US 02.01.01
        driver1.completeRide(request);
        assertTrue(request.getStatus().equals("Driver Confirmed Completion"));
        rider1.completeRide(request);
        assertTrue(request.getStatus().equals("Completed"));

        // US 05.04.01
        // Notifications not implemented

        RideRequestController.getRequestList().clear();
        UserController.getUserList().clear();
    }

    public void testSearching(){
        // US 04.01.01
        // geo-location not implemented

        // US 04.02.01
        Rider rider1 = new Rider("Rider1", "password1", "rider1@gmail.com", "8888888888");
        RideRequest newRequest1 = new RideRequest("Start1", "end1", "From start1 to end1", rider1, 50.0);
        rider1.postRideRequest(newRequest1);
        RideRequest newRequest2 = new RideRequest("Start1", "end1", "From start2 to end2", rider1, 50.0);
        rider1.postRideRequest(newRequest2);

        Driver driver1 = new Driver("Driver1", "password1", "driver1@gmail.com", "77777777777");
        driver1.acceptRequest(newRequest1);
        driver1.acceptRequest(newRequest2);

        List<RideRequest> search1 = driver1.getRequestsByKeyword("start");
        assertTrue(search1.size() == 2);
        List<RideRequest> search2 = driver1.getRequestsByKeyword("start1");
        assertTrue(search2.size() == 1);

        RideRequestController.getRequestList().clear();
        UserController.getUserList().clear();
    }

    public void testOfflineBehavior(){
        // Offline behavior not implemented
    }

    public void testLocation(){
        // Map not implemented
    }
}