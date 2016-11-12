package assignment1.ridengo;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;

/**
 * The type User driver.
 */
public class UserDriver {

    private RideRequestList requests;
    private boolean isNotified;
    private User user;

    /**
     * Instantiates a new User driver.
     *
     * @param user the user
     */
    public UserDriver(User user){
        this.user = user;
        this.requests = new RideRequestList();
    }

    /**
     * Set notified.
     *
     * @param notify the notify
     */
    public void setNotified(boolean notify){
        this.isNotified = notify;
    }

    /**
     * Is notified boolean.
     *
     * @return the boolean
     */
    public boolean isNotified(){
        return this.isNotified;
    }

    /**
     * Get user user.
     *
     * @return the user
     */
    public User getUser(){
        return this.user;
    }

    /**
     * Get requests ride request list.
     *
     * @return the ride request list
     */
    public RideRequestList getRequests(){
        return this.requests;
    }







    /**
     * Get pending requests ride request list.
     *
     * @return the ride request list
     */
    public RideRequestList getPendingRequests(){
        RideRequestList pendingRequests = new RideRequestList();
        for(RideRequest request : requests.getRequests()){
            if(request.getStatus().equals("Accepted By Driver")){
                pendingRequests.addRequest(request);
            }
//            else if(request.getStatus() == "Driver Confirmed" && request.getDriver().equals(this)){
//                pendingRequests.add(request);
//            }
        }
        return pendingRequests;
    }

    /**
     * Get requests by keyword ride request list.
     *
     * @param keyword the keyword
     * @return the ride request list
     */
    public RideRequestList getRequestsByKeyword(String keyword){
        RideRequestList requests = new RideRequestList();
        for(RideRequest request : RideRequestController.getRequestList().getRequests()){
            if(request.getDescription().contains(keyword)){
                if(request.getStatus().equals("Posted")) {
                    requests.addRequest(request);
                }
            }
        }
        return requests;
    }

    /**
     * Get requests by geo location ride request list.
     *
     * @param geoLocation the geo location
     * @return the ride request list
     */
    public RideRequestList getRequestsByGeoLocation(LatLng geoLocation){
        RideRequestList requests = new RideRequestList();
        //Code... Don't know how to do.


        return requests;
    }

    // Display driver username
    @Override
    public String toString(){
        return user.getUsername();
    }
}
