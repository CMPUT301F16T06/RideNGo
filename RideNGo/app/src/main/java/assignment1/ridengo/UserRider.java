package assignment1.ridengo;

/**
 * The type User rider.
 */
public class UserRider{

    private RideRequestList requests;
    private boolean isNotified;
    private User user;

    /**
     * Instantiates a new User rider.
     *
     * @param user the user
     */
    public UserRider (User user){
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
     * Post ride request.
     *
     * @param request the request
     */
    public void postRideRequest(RideRequest request){
        request.setStatus("Posted");
        RideRequestController.getRequestList().addRequest(request);
        requests.addRequest(request);
    }

    /**
     * Accept acception.
     *
     * @param request the request
     * @param driver  the driver
     */
    public void acceptAcception(RideRequest request, UserDriver driver){
        request.setDriver(driver);
        request.setStatus("Driver Confirmed");
        driver.setNotified(true);
    }

    /**
     * Cancel request.
     *
     * @param request the request
     */
    public void cancelRequest(RideRequest request){
        request.setStatus("Cancelled");
    }

    /**
     * Complete ride.
     *
     * @param request the request
     */
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
