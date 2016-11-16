package assignment1.ridengo;

import com.google.android.gms.maps.model.LatLng;

import java.sql.Driver;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * The type User.
 * Keep track of all user info, such as phone, email and username
 *Also keep track of a list of requests that a user is involve
 */
public class User {

    private String id;
    private String username;
    private String email;
    private String phoneNum;
//    private UserRider rider = new UserRider(this);
//    private UserDriver driver = new UserDriver(this);
    private ArrayList<Listener> listeners;
    private RideRequestList acceptedRequests;
    private RideRequestList postedRequests;

    /**
     * Instantiates a new User.
     *
     * @param username the username
     * @param email    the email
     * @param phoneNum the phone num
     */
    public User(String username, String email, String phoneNum){
        this.username = username;
        this.email = email;
        this.phoneNum = phoneNum;
        this.listeners = new ArrayList<Listener>();
        this.acceptedRequests = new RideRequestList();
        addUpdateListener(this);
    }

    public void addUpdateListener(final User user) {
        Listener l = new Listener() {
            @Override
            public void update() {
                UserController.DeleteUsersTask deleteUsersTask = new UserController.DeleteUsersTask();
                deleteUsersTask.execute(user);
                UserController.AddUsersTask addUsersTask = new UserController.AddUsersTask();
                addUsersTask.execute(user);
            }
        };
        addListener(l);
    }

    /**
     * Get username string.
     *
     * @return the string
     */
    public String getUsername(){
        return this.username;
    }

    /**
     * Get email string.
     *
     * @return the string
     */
    public String getEmail(){
        return this.email;
    }

    /**
     * Set email.
     *
     * @param newEmail the new email
     */
    public void setEmail(String newEmail){
        this.email = newEmail;
        notifyListeners();
    }

    /**
     * Get phone num string.
     *
     * @return the string
     */
    public String getPhoneNum(){
        return this.phoneNum;
    }

    /**
     * Set phone num.
     *
     * @param newPhoneNum the new phone num
     */
    public void setPhoneNum(String newPhoneNum){
        this.phoneNum = newPhoneNum;
        notifyListeners();
    }

    /**
     * Add listener.
     *
     * @param l the l
     */
    public void addListener(Listener l){
        this.listeners.add(l);
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

    /**
     * Sets id.
     *
     * @param id the id
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Get id string.
     *
     * @return the string
     */
    public String getId(){
        return this.id;
    }

    /**
     * Accept request.
     *
     * @param request the request
     */
    public void acceptRequest(RideRequest request){
        request.addAcception(this);
        request.setStatus("Accepted By Driver");
        acceptedRequests.addRequest(request);
    }

    /**
     * Complete ride.
     *
     * @param request the request
     */
    public void driverCompleteRide(RideRequest request){
        request.setStatus("Driver Confirmed Completion");
        // receive payment
        receivePay(request);
    }

    private void receivePay(RideRequest request){

    }

    /**
     * Get requests ride request list.
     *
     * @return the ride request list
     */
    public RideRequestList getRequests(){
        return this.postedRequests;
    }

    /**
     * Post ride request.
     *
     * @param request the request
     */
    public void postRideRequest(RideRequest request){
        request.setStatus("Posted");
        RideRequestController.getRequestList().addRequest(request);
        postedRequests.addRequest(request);
    }

    /**
     * Accept acception.
     *
     * @param request the request
     * @param driver  the driver
     */
    public void confirmAcception(RideRequest request, User driver){
        request.setDriver(driver);
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
    public void riderCompleteRide(RideRequest request){
//        if(request.getStatus().equals("Driver Confirmed Completion")){
//            request.setStatus("Completed");
//            payDriver(request);
//        }
        request.completeTrip();
        payDriver(request);
    }

    private void payDriver(RideRequest request){
        // pay the driver
    }

    public String toString(){
        return username;
    }

    public boolean isNotified() {
        return false;
    }

    public RideRequestList getPendingRequests() {
        return null;
    }

    public RideRequestList getRequestsByKeyword(String start) {
        return null;
    }

    public RideRequestList getRequestsByGeoLocation(LatLng latLng) {
        return null;
    }
}
