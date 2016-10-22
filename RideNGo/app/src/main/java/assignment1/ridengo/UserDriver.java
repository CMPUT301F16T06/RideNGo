package assignment1.ridengo;

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

    public List<RideRequest> getPendingRequests(){
        List<RideRequest> pendingRequests = new ArrayList<RideRequest>();
        for(RideRequest request : requests.getRequests()){
            if(request.getStatus().equals("Accepted By Driver")){
                pendingRequests.add(request);
            }
//            else if(request.getStatus() == "Driver Confirmed" && request.getDriver().equals(this)){
//                pendingRequests.add(request);
//            }
        }
        return pendingRequests;
    }

    public List<RideRequest> getRequestsByKeyword(String keyword){
        List<RideRequest> requests = new ArrayList<RideRequest>();
        for(RideRequest request : RideRequestController.getRequestList().getRequests()){
            if(request.getDescription().contains(keyword)){
                if(request.getStatus().equals("Posted")) {
                    requests.add(request);
                }
            }
        }
        return requests;
    }

    public List<RideRequest> getRequestsByGeoLocation(String geoLocation){
        List<RideRequest> requests = new ArrayList<RideRequest>();
        //Code... Don't know how to do.


        return requests;
    }
}
