package assignment1.ridengo;

import java.util.ArrayList;
import java.util.List;

/**
 * A list that keep tracks of all the ride request
 * Able to manipulate(add, delete, update) ride requests
 */
public class RideRequestList {

    private ArrayList<RideRequest> requestList = null;
    /**
     * The constant listeners.
     */
//    private static final long serialVersionUID = 6673446047991058932L;
    private transient ArrayList<Listener> listeners = null;

    /**
     * Instantiates a new Ride request list.
     */
    public RideRequestList() {
        requestList = new ArrayList<RideRequest>();
        listeners = new ArrayList<Listener>();
    }

    /**
     * Check whether the list contain a ride request
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

    /**
     * Get a list of listeners
     * @return
     */
    private ArrayList<Listener> getListeners() {
        if (listeners == null ) {
            listeners = new ArrayList<Listener>();
        }
        return listeners;
    }

    /**
     * Gets all requests stored in ride request lsit
     *
     * @return the requests
     */
    public List<RideRequest> getRequests() {
        if(requestList == null){
            requestList = new ArrayList<RideRequest>();
        }
        return requestList;
    }

    public RideRequest getRequestById(int id) {
        for(RideRequest request : requestList) {
            if(request.getId() == id) {
                return request;
            }
        }
        return null;
    }

    /**
     * Get requests related to a particular user
     *
     * @param username the username
     * @return the list
     */
    public List<RideRequest> getRequestsWithRider(String username){
        List<RideRequest> rideRequests = new ArrayList<RideRequest>();
        for(RideRequest request: requestList){
            if(request.getRider().getUsername().equals(username)){
                rideRequests.add(request);
            }
        }
        return rideRequests;
    }

    /**
     * Get requests related to a particular driver
     *
     * @param username the username
     * @return the list
     */
    public List<RideRequest> getRequestsWithDriver(String username){
        List<RideRequest> rideRequests = new ArrayList<RideRequest>();
        User driver = UserController.getUserList().getUserByUsername(username);
        for(RideRequest request: requestList){
            if(request.getAcceptions().contains(driver)){
                rideRequests.add(request);
            }
        }
        return rideRequests;
    }

    /**
     * Get request with a unique hash code
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
     * Add a new request
     *
     * @param testRequest
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
     * Clear the request list
     */
    public void clear() {
        requestList.clear();
        notifyListeners();
    }

    /**
     * Remove a request from list.
     *
     * @param request the request
     */
    public void removeRequest(RideRequest request){
        requestList.remove(request);
        notifyListeners();
    }

}
