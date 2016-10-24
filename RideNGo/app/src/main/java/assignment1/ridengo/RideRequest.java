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

    private LatLng startPoint;
    private LatLng endPoint;
    private String description;
    private Double fare;
    private UserRider rider;
    private UserDriver driver;
    private String status;
    private List<UserDriver> acceptions = null;
    private ArrayList<Listener> listeners;

    public RideRequest(LatLng startPoint, LatLng endPoint, String description, UserRider rider, Double fare){
        this.startPoint = startPoint;
        this.endPoint = endPoint;
        this.description = description;
        this.rider = rider;
        this.fare = fare;
        this.listeners = new ArrayList<Listener>();
    }

    public UserRider getRider(){
        return this.rider;
    }

    public LatLng getStartPoint(){
        return this.startPoint;
    }

    public LatLng getEndPoint(){
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
        notifyListeners();
    }

    public void addAcception(UserDriver driver){
        if(acceptions == null){
            acceptions = new ArrayList<UserDriver>();
        }
        acceptions.add(driver);
        notifyListeners();
    }

    public void setStatus(String status){
        this.status = status;
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
}
