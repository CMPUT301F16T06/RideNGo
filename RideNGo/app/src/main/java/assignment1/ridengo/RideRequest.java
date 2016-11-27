package assignment1.ridengo;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;

/**
 * Ride Request, keep tracks of rider, driver, starting point, ending point, request status etc.
 */
public class RideRequest {

    private LatLng startCoord;
    private LatLng endCoord;
    private String startPoint;
    private String endPoint;
    final private String waitForDriver = "Waiting for Driver";
    final private String waitForConfirmation = "Waiting for Confirmation";
    final private String tripConfirmed = "Driver Confirmed";
    final private String tripCompleted = "Trip Completed";
    final private String requestCanceled = "Request canceled";

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    private int id;
    private String description;
    private Double fare;
    private User rider;
    private User driver;
    private String status;
    private List<User> acceptions = null;
    private ArrayList<Listener> listeners;

    public boolean isNotifyRider() {
        return notifyRider;
    }

    public void setNotifyRider(boolean notifyRider) {
        this.notifyRider = notifyRider;
        notifyListeners();
    }

    public boolean isNotifyDriver() {
        return notifyDriver;
    }

    public void setNotifyDriver(boolean notifyDriver) {
        this.notifyDriver = notifyDriver;
        notifyListeners();
    }

    private boolean notifyRider;
    private boolean notifyDriver;

    public LatLng getEndCoord() {
        return this.endCoord;
    }

    public LatLng getStartCoord() {
        return this.startCoord;
    }

    public void setStartCoord(LatLng newStart){
        this.startCoord = newStart;
    }

    public void setEndCoord(LatLng newEnd){
        this.endCoord = newEnd;
    }
    /**
     * Instantiates a new Ride request.
     *
     * @param startPoint  the start point
     * @param endPoint    the end point
     * @param description the description
     * @param rider       the rider
     * @param fare        the fare
     */
    public RideRequest(LatLng startCoord, LatLng endCoord, String startPoint, String endPoint, String description, User rider, Double fare){
        this.startCoord = startCoord;
        this.endCoord = endCoord;
        this.startPoint = startPoint;
        this.endPoint = endPoint;
        this.description = description;
        this.rider = rider;
        this.fare = fare;
        this.status = waitForDriver;
        this.listeners = new ArrayList<Listener>();
        this.id = this.hashCode();
        this.notifyRider = false;
        this.notifyDriver = false;
        addUpdateListener(this);
    }

    public void addUpdateListener(final RideRequest request) {
        Listener l = new Listener() {
            @Override
            public void update() {
                RideRequestController.DeleteRequestsTask deleteRequestsTask = new RideRequestController.DeleteRequestsTask();
                deleteRequestsTask.execute(request);
                RideRequestController.AddRequestsTask addRequestsTask = new RideRequestController.AddRequestsTask();
                addRequestsTask.execute(request);
            }
        };
        addListener(l);
    }

    /**
     * Get rider
     *
     *
     * @return the user rider
     */
    public User getRider(){
        return this.rider;
    }

    /**
     * Gets start point.
     *
     * @return the start point
     */
//    public LatLng getStartPoint(){
//        return this.startPoint;
//    }
//
//    public LatLng getEndPoint(){
//        return this.endPoint;
//    }
    public String getStartPoint(){
        return this.startPoint;
    }

    /**
     * Get end point.
     *
     * @return the string
     */
    public String getEndPoint(){
        return this.endPoint;
    }

    /**
     * Get Ride description.
     *
     * @return the string
     */
    public String getDescription(){
        return this.description;
    }

    /**
     * Get driver.
     *
     * @return the user driver
     */
    public User getDriver(){
        return this.driver;
    }

    /**
     * Get fare for the ride.
     *
     * @return the double
     */
    public Double getFare(){
        return this.fare;
    }

    /**
     * Get request status.
     *
     * @return the string
     */
    public String getStatus(){
        return this.status;
    }

    /**
     * Get acceptions list.
     * Return a list of driver accepted the request
     *
     * @return the list
     */
    public List<User> getAcceptions(){
        if(acceptions == null){
            acceptions = new ArrayList<User>();
        }
        return acceptions;
    }

    /**
     * Set driver. Confirm by rider
     *
     * @param driver the driver
     */
    public void setDriver(User driver){
        this.driver = driver;
        setStatus(tripConfirmed);
        notifyListeners();
    }

    /**
     * Driver accept the ride request
     *
     * @param driver the driver
     */
    public void addAcception(User driver){
        if(acceptions == null){
            acceptions = new ArrayList<User>();
        }
        acceptions.add(driver);
        if(status.equals(waitForDriver)){
            setStatus(waitForConfirmation);
        }
        notifyListeners();
    }

    /**
     * Check whether the driver had accepted the request
     *
     * @param username the driver's username
     * @return the boolean
     */
    public boolean isAccepted(String username){
        for(User user: getAcceptions()){
            if(user.getUsername().equals(username)){
                return true;
            }
        }
        return false;
    }

    /**
     * Confirm driver
     * @param username
     * @return
     */
    public boolean isDriver(String username){
        if(getDriver() == null){
            return true;
        }
        else if(getDriver().getUsername().equals(username)){
            return true;
        }
        return false;
    }

    public void completeTrip(){
        setStatus(tripCompleted);
    }

    /**
     * Set request status
     *
     * @param status the status
     */
    public void setStatus(String status){
        this.status = status;
//        if(this.status.equals(tripConfirmed)){
//            this.status = this.status + ", Driver is " + getDriver().toString();
//        }
        notifyListeners();
    }


    /**
     * Add listener.
     *
     * @param l the l
     */
    public void addListener(Listener l){
        if(l != null) {
            this.listeners.add(l);
        }
    }

    /**
     * Notify listeners.
     */
    private void notifyListeners(){
        for(Listener listener : this.listeners){
            if(listener != null) {
                listener.update();
            }
        }
    }

    /**
     * Remove listener.
     *
     * @param l the l
     */
    public void removeListener(Listener l) {
        this.listeners.remove(l);
    }

    public String toString(){
        return "From: "+ getStartPoint() + " to "+ getEndPoint();
    }
}
