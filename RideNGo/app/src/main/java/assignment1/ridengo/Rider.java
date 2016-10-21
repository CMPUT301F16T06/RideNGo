package assignment1.ridengo;

/**
 * Created by Mingjun on 10/11/2016.
 */
public class Rider{

    private RideRequestList requests;

    private User user;

    public void setUser(User user){
        this.user = user;
    }

    public User getUser(){
        return this.user;
    }

    public void postRideRequest(User user, RideRequest request){

        request.setStatus("Posted");
        RideRequestController.getRequestList().addRequest(request);
        requests.addRequest(request);
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
