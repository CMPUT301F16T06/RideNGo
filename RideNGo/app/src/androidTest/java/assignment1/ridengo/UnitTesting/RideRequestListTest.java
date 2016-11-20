package assignment1.ridengo.UnitTesting;

import junit.framework.TestCase;

import java.util.ArrayList;
import java.util.List;

import assignment1.ridengo.RideRequest;
import assignment1.ridengo.RideRequestList;
import assignment1.ridengo.User;
import assignment1.ridengo.UserController;

/**
 * Created by Rui on 2016-11-19.
 */
public class RideRequestListTest extends TestCase {

    public void testContains() throws Exception {
        RideRequestList rideRequestList = new RideRequestList();
        RideRequest newRequest = new RideRequest("start","end", "", new User("","",""), 0.00);

        assertFalse(rideRequestList.contains(newRequest));
        rideRequestList.addRequest(newRequest);
        assertTrue(rideRequestList.contains(newRequest));
    }

    public void testGetRequests() throws Exception {
        RideRequestList rideRequestList = new RideRequestList();
        List<RideRequest> requestList = new ArrayList<RideRequest>();

        RideRequest newRequest1 = new RideRequest("start1","end1", "", new User("1","",""), 0.00);
        RideRequest newRequest2 = new RideRequest("start2","end2", "", new User("2","",""), 0.00);
        RideRequest newRequest3 = new RideRequest("start3","end3", "", new User("3","",""), 0.00);
        requestList.add(newRequest1);
        requestList.add(newRequest2);
        requestList.add(newRequest3);

        rideRequestList.addRequest(newRequest1);
        rideRequestList.addRequest(newRequest2);
        rideRequestList.addRequest(newRequest3);

        List<RideRequest> list = rideRequestList.getRequests();
        assertEquals(list.size(), 3);
        for(int i = 0; i< requestList.size(); i++){
            assertEquals(requestList.get(i).getId(), list.get(i).getId());
        }
    }

    public void testGetRequestsWithRider() throws Exception {
        User rider = new User("rider", "","");
        User rider1 = new User("rider1", "", "");

        RideRequestList rideRequestList = new RideRequestList();
        RideRequest newRequest = new RideRequest("start","end", "rider", rider, 0.00);
        RideRequest newRequest1 = new RideRequest("start1","end1", "rider1", rider1, 1.00);

        rideRequestList.addRequest(newRequest);
        rideRequestList.addRequest(newRequest1);

        List<RideRequest> list = rideRequestList.getRequestsWithRider(rider.getUsername());

        assertTrue(list.size()==1);

        RideRequest getRequest = list.get(0);
        assertEquals(getRequest.getId(), newRequest.getId());
    }

    public void testGetRequestsWithDriver() throws Exception {
        User rider = new User("rider", "","");
        User driver = new User("driver", "", "");
        User driver1 = new User("driver1", "", "");

        UserController.getUserList().addUser(rider);
        UserController.getUserList().addUser(driver);
        UserController.getUserList().addUser(driver1);

        RideRequestList rideRequestList = new RideRequestList();
        RideRequest newRequest = new RideRequest("start","end", "rider", rider, 0.00);
        newRequest.addAcception(driver);
        RideRequest newRequest1 = new RideRequest("start1","end1", "rider1", rider, 1.00);
        newRequest1.addAcception(driver1);
        rideRequestList.addRequest(newRequest);
        rideRequestList.addRequest(newRequest1);

        List<RideRequest> list = rideRequestList.getRequestsWithDriver(driver.getUsername());

        assertEquals(list.size(), 1);

        RideRequest getRequest = list.get(0);
        assertEquals(getRequest.getId(), newRequest.getId());
    }

    public void testGetRequestWithHash() throws Exception {
        User rider = new User("name", "", "");

        RideRequestList rideRequestList = new RideRequestList();
        RideRequest newRequest = new RideRequest("start", "end", "rider", rider, 0.00);

        rideRequestList.addRequest(newRequest);
        assertTrue(rideRequestList.contains(newRequest));

        RideRequest getRequest = rideRequestList.getRequestWithHash(newRequest.getId());
        assertEquals(getRequest.getId(), newRequest.getId());

    }

    public void testAddRequest() throws Exception {
        RideRequestList rideRequestList = new RideRequestList();
        RideRequest newRequest = new RideRequest("start","end", "rider", new User("","",""), 0.00);

        assertFalse(rideRequestList.contains(newRequest));

        rideRequestList.addRequest(newRequest);
        assertTrue(rideRequestList.contains(newRequest));
    }

    public void testClear() throws Exception {
        RideRequestList rideRequestList = new RideRequestList();
        RideRequest newRequest = new RideRequest("start","end", "rider", new User("","",""), 0.00);
        rideRequestList.addRequest(newRequest);

        assertFalse(rideRequestList.getRequests().size() == 0);
        rideRequestList.clear();
        assertTrue(rideRequestList.getRequests().size() == 0);
    }

    public void testRemoveRequest() throws Exception {
        RideRequestList rideRequestList = new RideRequestList();
        RideRequest newRequest = new RideRequest("start","end", "rider", new User("","",""), 0.00);

        rideRequestList.addRequest(newRequest);
        assertTrue(rideRequestList.contains(newRequest));

        rideRequestList.removeRequest(0);
        assertFalse(rideRequestList.contains(newRequest));
    }
}