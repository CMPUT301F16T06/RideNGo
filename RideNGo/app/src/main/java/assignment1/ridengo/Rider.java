package assignment1.ridengo;

/**
 * Created by Mingjun on 10/11/2016.
 */
public class Rider extends User {

    public Rider(String username, String password, String email, String phoneNum){
        super(username, password, email, phoneNum);
    }

    public void postRideRequest(RideRequest request){
        request.setStatus("Posted");
        RideRequestController.getRequestList().addRequest(request);
        this.getRequests().add(request);
    }

    public void acceptAcception(RideRequest request, Driver driver){
        request.setDriver(driver);
        request.setStatus("Driver Confirmed");
    }

    public void cancelRequest(RideRequest request){
        request.setStatus("Cancelled");
    }

    public void completeRide(RideRequest request){
        if(request.getStatus().equals("Driver Confirmed Completion")){
            request.setStatus("Completed");
        }
    }
}
