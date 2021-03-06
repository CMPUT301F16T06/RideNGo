package assignment1.ridengo;

import com.google.android.gms.maps.model.LatLng;
import java.util.ArrayList;

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
    private Vehicle vehicle;
    private float rating;
    private int totalOfRating;
    private int numRatings;
    private ArrayList<Listener> listeners;
    private ArrayList<Integer> acceptedRequests;
    private ArrayList<Integer> postedRequests;


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
        this.rating = 0;
        this.totalOfRating = 0;
        this.numRatings = 0;
        this.listeners = new ArrayList<Listener>();
        this.acceptedRequests = new ArrayList<Integer>();
        this.postedRequests = new ArrayList<Integer>();
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

    public int getNumRatings() {
        return numRatings;
    }

    public int getTotalOfRating() {
        return totalOfRating;
    }

    /**
     *
     * @param newRating the driver's rating
     */
    public void setRating(float newRating) {
        this.numRatings +=1;
        this.totalOfRating += newRating;
        this.rating = totalOfRating/(float)numRatings;
        notifyListeners();
    }

    /**
     * Get driver's rating
     * @return Driver's rating
     */
    public float getRating() {
        return rating;
    }

    /**
     *
     * @param v the user's vehicle
     */
    public void setVehicle(Vehicle v){
        this.vehicle = v;
        notifyListeners();
    }

    /**
     * Remove the vehicle of user
     */
    public void rmVehicle(){
        this.vehicle = null;
        notifyListeners();
    }

    /**
     * Get vehicle information
     *
     * @return Vehicle type vehicle information
     */
    public Vehicle getVehicle(){
        return this.vehicle;
    }

    public boolean haveVehicle(){
        return !(getVehicle() == null);
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
        acceptedRequests.add(request.getId());
        request.setNotifyRider(true);
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
    public ArrayList<Integer> getRequests(){
        return this.postedRequests;
    }

    public ArrayList<Integer> getAcceptedRequests(){
        return this.acceptedRequests;
    }

    /**
     * Post ride request.
     *
     * @param request the request
     */
    public void postRideRequest(RideRequest request){
        RideRequestController.addRequest(request);
        request.setStatus("Waiting for Driver");
        postedRequests.add(request.getId());
    }

    /**
     * Accept acception.
     *
     * @param request the request
     * @param driver  the driver
     */
    public void confirmAcception(RideRequest request, User driver){
        request.setDriver(driver);
        request.setNotifyDriver(true);
    }

    /**
     * Cancel request.
     *
     * @param request the request
     */
    public void cancelRequest(RideRequest request){
        RideRequestController.removeRequest(request);
        postedRequests.remove((Integer)request.getId());

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

}
