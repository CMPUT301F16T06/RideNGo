package assignment1.ridengo;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mingjun on 10/11/2016.
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

    public RideRequest(String startPoint, String endPoint, String description, UserRider rider, Double fare){
        this.startPoint = startPoint;
        this.endPoint = endPoint;
        this.description = description;
        this.rider = rider;
        this.fare = fare;
        this.status = waitForDriver;
        this.listeners = new ArrayList<Listener>();
    }

    public UserRider getRider(){
        return this.rider;
    }

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
    public String getEndPoint(){
        return this.endPoint;
    }

    public String getDescription(){
        return this.description;
    }

    public UserDriver getDriver(){
        return this.driver;
    }

    public Double getFare(){
        return this.fare;
    }

    public String getStatus(){
        return this.status;
    }

    public List<UserDriver> getAcceptions(){
        if(acceptions == null){
            acceptions = new ArrayList<UserDriver>();
        }
        return acceptions;
    }

    public void setDriver(UserDriver driver){
        this.driver = driver;
        setStatus(tripConfirmed);
        notifyListeners();
    }

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

    public void setStatus(String status){
        this.status = status;
//        if(this.status.equals(tripConfirmed)){
//            this.status = this.status + ", Driver is " + getDriver().toString();
//        }
        notifyListeners();
    }


    public void addListener(Listener l){
        this.listeners.add(l);
    }

    public void notifyListeners(){
        for(Listener listener : this.listeners){
            listener.update();
        }
    }

    public void removeListener(Listener l) {
        this.listeners.remove(l);
    }

    public String toString(){
        String string= "From: "+ getStartPoint() + " to "+ getEndPoint();
        return string;
    }
}
