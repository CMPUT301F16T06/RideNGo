package assignment1.ridengo;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;

/**
 * The type Ride request.
 */
public class RideRequest {

    //private LatLng startPoint;
    //private LatLng endPoint;
    private String startPoint;
    private String endPoint;
    final private String waitForDriver="Waiting for Driver";
    final private String waitForConfirmation = "Waiting for Confirmation";
    final private String tripConfirmed = "Driver Confirmed";
    final private String tripCompleted = "Trip Completed";
    final private String requestCanceled = "Request canceled";

    private String description;
    private Double fare;
    private UserRider rider;
    private UserDriver driver;
    private String status;
    private List<UserDriver> acceptions = null;
    private ArrayList<Listener> listeners;

    /**
     * Instantiates a new Ride request.
     *
     * @param startPoint  the start point
     * @param endPoint    the end point
     * @param description the description
     * @param rider       the rider
     * @param fare        the fare
     */
    public RideRequest(String startPoint, String endPoint, String description, UserRider rider, Double fare){
        this.startPoint = startPoint;
        this.endPoint = endPoint;
        this.description = description;
        this.rider = rider;
        this.fare = fare;
        this.status = waitForDriver;
        this.listeners = new ArrayList<Listener>();
    }

    /**
     * Get rider user rider.
     *
     * @return the user rider
     */
    public UserRider getRider(){
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
     * Get end point string.
     *
     * @return the string
     */
    public String getEndPoint(){
        return this.endPoint;
    }

    /**
     * Get description string.
     *
     * @return the string
     */
    public String getDescription(){
        return this.description;
    }

    /**
     * Get driver user driver.
     *
     * @return the user driver
     */
    public UserDriver getDriver(){
        return this.driver;
    }

    /**
     * Get fare double.
     *
     * @return the double
     */
    public Double getFare(){
        return this.fare;
    }

    /**
     * Get status string.
     *
     * @return the string
     */
    public String getStatus(){
        return this.status;
    }

    /**
     * Get acceptions list.
     *
     * @return the list
     */
    public List<UserDriver> getAcceptions(){
        if(acceptions == null){
            acceptions = new ArrayList<UserDriver>();
        }
        return acceptions;
    }

    /**
     * Set driver.
     *
     * @param driver the driver
     */
    public void setDriver(UserDriver driver){
        this.driver = driver;
        setStatus(tripConfirmed);
        notifyListeners();
    }

    /**
     * Add acception.
     *
     * @param driver the driver
     */
    public void addAcception(UserDriver driver){
        if(acceptions == null){
            acceptions = new ArrayList<UserDriver>();
        }
        acceptions.add(driver);
        if(status == waitForDriver){
            setStatus(waitForConfirmation);
        }
        notifyListeners();
    }

    /**
     * Is accepted boolean.
     *
     * @param driver the driver
     * @return the boolean
     */
    public boolean isAccepted(UserDriver driver){
        if(getAcceptions().contains(driver)){
            return true;
        }
        return false;
    }

    public boolean isDriver(String username){
        if(getDriver() == null){
            return true;
        }
        else if(getDriver().getUser().getUsername().equals(username)){
            return true;
        }
        return false;
    }

    public void completeTrip(){
        setStatus(tripCompleted);
    }

    /**
     * Set status.
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
        this.listeners.add(l);
    }

    /**
     * Notify listeners.
     */
    public void notifyListeners(){
        for(Listener listener : this.listeners){
            listener.update();
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
        String string= "From: "+ getStartPoint() + " to "+ getEndPoint();
        return string;
    }
}
