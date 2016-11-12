package assignment1.ridengo;

import junit.framework.TestCase;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Rui on 2016-11-12.
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
        RideRequest newRequest = new RideRequest("start","end", "", new User("","",""), 0.00);
        rideRequestList.addRequest(newRequest);

        List<RideRequest> list = rideRequestList.getRequests();
        assertEquals(list.get(0).getStartPoint(), newRequest.getStartPoint());
        assertEquals(list.get(0).getEndPoint(), newRequest.getEndPoint());
        assertEquals(list.get(0).getDescription(), newRequest.getDescription());
        assertEquals(list.get(0).getRider(), newRequest.getRider());
        assertEquals(list.get(0).getFare(), newRequest.getFare());

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
        assertEquals(list.get(0).getStartPoint(), newRequest.getStartPoint());
        assertEquals(list.get(0).getEndPoint(), newRequest.getEndPoint());
        assertEquals(list.get(0).getDescription(), newRequest.getDescription());
        assertEquals(list.get(0).getRider(), newRequest.getRider());
        assertEquals(list.get(0).getFare(), newRequest.getFare());

    }

    public void testGetRequestsWithDriver() throws Exception {
        User rider = new User("rider", "","");
        User driver = new User("driver", "", "");
        User driver1 = new User("driver1", "", "");

        RideRequestList rideRequestList = new RideRequestList();
        RideRequest newRequest = new RideRequest("start","end", "rider", rider, 0.00);
        newRequest.setDriver(driver);
        RideRequest newRequest1 = new RideRequest("start1","end1", "rider1", rider, 1.00);
        newRequest.setDriver(driver1);
        rideRequestList.addRequest(newRequest);
        rideRequestList.addRequest(newRequest1);

        List<RideRequest> list = rideRequestList.getRequestsWithDriver(driver.getUsername());

        assertEquals(list.get(0).getStartPoint(), newRequest.getStartPoint());
        assertEquals(list.get(0).getEndPoint(), newRequest.getEndPoint());
        assertEquals(list.get(0).getDescription(), newRequest.getDescription());
        assertEquals(list.get(0).getDriver(), newRequest.getDriver());
        assertEquals(list.get(0).getFare(), newRequest.getFare());

    }

    public void testGetRequestWithHash() throws Exception {
        fail("Don't know how to test");
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