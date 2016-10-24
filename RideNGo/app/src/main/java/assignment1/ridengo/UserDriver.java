package assignment1.ridengo;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mingjun on 10/11/2016.
 */
public class UserDriver {

    private RideRequestList requests;
    private boolean isNotified;
    private User user;

    public UserDriver(User user){
        this.user = user;
        this.requests = new RideRequestList();
    }

    public void setNotified(boolean notify){
        this.isNotified = notify;
    }

    public boolean isNotified(){
        return this.isNotified;
    }

    public User getUser(){
        return this.user;
    }

    public RideRequestList getRequests(){
        return this.requests;
    }

    public void acceptRequest(RideRequest request){
        request.addAcception(this);
        request.setStatus("Accepted By Driver");
        requests.addRequest(request);
        request.getRider().setNotified(true);
    }

    public void completeRide(RideRequest request){
        request.setStatus("Driver Confirmed Completion");
        // receive payment
        receivePay(request);
    }

    private void receivePay(RideRequest request){

    }

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

    public RideRequestList getRequestsByGeoLocation(LatLng geoLocation){
        RideRequestList requests = new RideRequestList();
        //Code... Don't know how to do.


        return requests;
    }
}
