package assignment1.ridengo;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * The type Ride request list.
 */
public class RideRequestList {

    private ArrayList<RideRequest> requestList = null;
    /**
     * The constant listeners.
     */
//    private static final long serialVersionUID = 6673446047991058932L;
    protected transient ArrayList<Listener> listeners = null;

    /**
     * Instantiates a new Ride request list.
     */
    public RideRequestList() {
        requestList = new ArrayList<RideRequest>();
        listeners = new ArrayList<Listener>();
    }

    /**
     * Contains boolean.
     *
     * @param testRequest the test request
     * @return the boolean
     */
    public boolean contains(RideRequest testRequest) {
        return requestList.contains(testRequest);
    }

    /**
     * Add listener.
     *
     * @param l the l
     */
    public void addListener(Listener l) {
        getListeners().add(l);
        for(RideRequest request : requestList){
            request.addListener(l);
        }
    }

    /**
     * Remove listener.
     *
     * @param l the l
     */
    public void removeListener(Listener l) {
        getListeners().remove(l);
        for(RideRequest request : requestList){
            request.removeListener(l);
        }
    }

    private ArrayList<Listener> getListeners() {
        if (listeners == null ) {
            listeners = new ArrayList<Listener>();
        }
        return listeners;
    }

    /**
     * Gets requests.
     *
     * @return the requests
     */
    public List<RideRequest> getRequests() {
        if(requestList == null){
            requestList = new ArrayList<RideRequest>();
        }
        return requestList;
    }

    /**
     * Get requests with rider list.
     *
     * @param username the username
     * @return the list
     */
    public List<RideRequest> getRequestsWithRider(String username){
        List<RideRequest> rideRequests = new ArrayList<RideRequest>();
        for(RideRequest request: requestList){
            if(request.getRider().getUser().getUsername().equals(username)){
                rideRequests.add(request);
            }
        }
        return rideRequests;
    }

    /**
     * Get requests with driver list.
     *
     * @param username the username
     * @return the list
     */
    public List<RideRequest> getRequestsWithDriver(String username){
        List<RideRequest> rideRequests = new ArrayList<RideRequest>();
        UserDriver driver = UserController.getUserList().getUserByUsername(username).getDriver();
        for(RideRequest request: requestList){
            if(request.getAcceptions().contains(driver)){
                rideRequests.add(request);
            }
        }
        return rideRequests;
    }

    /**
     * Get request with hash ride request.
     *
     * @param h the h
     * @return the ride request
     */
    public RideRequest getRequestWithHash(int h){
        for(RideRequest rideRequest: requestList){
            if(rideRequest.hashCode() == h){
                return rideRequest;
            }
        }
        return null;
    }

    /**
     * Add request.
     *
     * @param testRequest the test request
     */
    public void addRequest(RideRequest testRequest) {
        requestList.add(testRequest);
        notifyListeners();
    }

    private void notifyListeners() {
        for (Listener  listener : getListeners()) {
            listener.update();
        }
    }

    /**
     * Clear.
     */
    public void clear() {
        requestList.clear();
        notifyListeners();
    }

    /**
     * Remove request.
     *
     * @param index the index
     */
    public void removeRequest(int index){
        requestList.remove(index);
        notifyListeners();
    }

    /**
     * Add test case.
     */
//////////////Temp Test Case/////////////
    public void addTestCase(){
        User user1 = new User("A","a@example.com","7800000000");
        UserRider rider1 = new UserRider(user1);
        RideRequest test1 = new RideRequest("Uni","100St","Have fun",rider1,10.00);
        User user2 = new User("A","a@example.com","7800000000");
        UserRider rider2 = new UserRider(user1);
        RideRequest test2 = new RideRequest("Uni","101St","Have fun",rider1,20.00);
        requestList.add(test1);
        requestList.add(test2);
    }

    /**
     * Get test requests list.
     *
     * @return the list
     */
    public List<RideRequest> getTestRequests(){
        addTestCase();
        if(requestList == null){
            requestList = new ArrayList<RideRequest>();
        }
        return requestList;
    }

}
