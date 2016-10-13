package assignment1.ridengo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mingjun on 10/11/2016.
 */
public class Driver extends User {

    public Driver(String username, String password, String email, String phoneNum){
        super(username, password, email, phoneNum);
    }

    public void acceptRequest(RideRequest request){
        request.addAcception(this);
        request.setStatus("Accepted By Driver");
        this.getRequests().add(request);
    }

    public void completeRide(RideRequest request){
        request.setStatus("Driver Confirmed Completion");
    }

    public List<RideRequest> getPendingRequests(){
        List<RideRequest> requests = new ArrayList<RideRequest>();
        for(RideRequest request : this.getRequests()){
            if(request.getStatus() == "Accepted By Driver"){
                requests.add(request);
            }else if(request.getStatus() == "Driver Confirmed" && request.getDriver().equals(this)){
                requests.add(request);
            }
        }
        return requests;
    }

    public List<RideRequest> getRequestsByKeyword(String keyword){
        List<RideRequest> requests = new ArrayList<RideRequest>();
        for(RideRequest request : this.getPendingRequests()){
            if(request.getDescription().contains(keyword)){
                requests.add(request);
            }
        }
        return requests;
    }
}
