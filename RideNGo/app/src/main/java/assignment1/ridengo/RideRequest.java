package assignment1.ridengo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mingjun on 10/11/2016.
 */
public class RideRequest {

    private String startPoint;
    private String endPoint;
    private String description;
    private Double fare;
    private Rider rider;
    private Driver driver;
    private String status;
    private List<Driver> acceptions = null;

    public RideRequest(String startPoint, String endPoint, String description, Rider rider, Double fare){
        this.startPoint = startPoint;
        this.endPoint = endPoint;
        this.description = description;
        this.rider = rider;
        this.fare = fare;
    }

    public Rider getRider(){
        return this.rider;
    }

    public String getStartPoint(){
        return this.startPoint;
    }

    public String getEndPoint(){
        return this.endPoint;
    }

    public String getDescription(){
        return this.description;
    }

    public Driver getDriver(){
        return this.driver;
    }

    public Double getFare(){
        return this.fare;
    }

    public String getStatus(){
        return this.status;
    }

    public List<Driver> getAcceptions(){
        if(acceptions == null){
            acceptions = new ArrayList<Driver>();
        }
        return acceptions;
    }

    public void setDriver(Driver driver){
        this.driver = driver;
    }

    public void addAcception(Driver driver){
        if(acceptions == null){
            acceptions = new ArrayList<Driver>();
        }
        acceptions.add(driver);
    }

    public void setStatus(String status){
        this.status = status;
    }
}
