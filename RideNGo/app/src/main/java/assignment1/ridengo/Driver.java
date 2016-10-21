package assignment1.ridengo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mingjun on 10/11/2016.
 */
public class Driver {

    private List<RideRequest> requests;

    private User user;

    public void setUser(User user){
        this.user = user;
    }

    public User getUser(){
        return this.user;
    }

    public void acceptRequest(RideRequest request){
        request.addAcception(this);
        request.setStatus("Accepted By Driver");
        requests.add(request);
    }

    public void completeRide(RideRequest request){
        request.setStatus("Driver Confirmed Completion");
    }

    public List<RideRequest> getPendingRequests(){
        List<RideRequest> pendingRequests = new ArrayList<RideRequest>();
        for(RideRequest request : requests){
            if(request.getStatus() == "Accepted By Driver"){
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
        for(RideRequest request : RideRequestController.getRequestList()){
            if(request.getDescription().contains(keyword)){
                if(request.getStatus().equals("Posted")) {
                    requests.add(request);
                }
            }
        }
        return requests;
    }

    public List<RideRequest> getRequestsByGeoLocation(){
        List<RideRequest> requests = new ArrayList<RideRequest>();
        //Code... Don't know how to do.


        return requests;
    }
}
