package assignment1.ridengo;

/**
 * Created by Mingjun on 10/11/2016.
 */
public class UserRider{

    private RideRequestList requests;
    private boolean isNotified;
    private User user;

    public UserRider (User user){
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

    public void postRideRequest(RideRequest request){
        request.setStatus("Posted");
        RideRequestController.getRequestList().addRequest(request);
        requests.addRequest(request);
    }

    public void acceptAcception(RideRequest request, UserDriver driver){
        request.setDriver(driver);
        request.setStatus("Driver Confirmed");
        driver.setNotified(true);
    }

    public void cancelRequest(RideRequest request){
        request.setStatus("Cancelled");
    }

    public void completeRide(RideRequest request){
//        if(request.getStatus().equals("Driver Confirmed Completion")){
//            request.setStatus("Completed");
//            payDriver(request);
//        }
        request.setStatus("Completed");
        payDriver(request);
    }

    private void payDriver(RideRequest request){
        // pay the driver
    }
}
