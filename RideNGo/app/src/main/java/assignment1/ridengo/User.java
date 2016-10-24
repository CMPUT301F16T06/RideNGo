package assignment1.ridengo;

import java.sql.Driver;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by Mingjun on 10/11/2016.
 */
public class User {

    private String username;
    private String email;
    private String phoneNum;
    private UserRider rider;
    private UserDriver driver;
    private ArrayList<Listener> listeners;

    public User(String username, String email, String phoneNum){
        this.username = username;
        this.email = email;
        this.phoneNum = phoneNum;
        this.listeners = new ArrayList<Listener>();
    }

    public String getUsername(){
        return this.username;
    }

    public String getEmail(){
        return this.email;
    }

    public void setEmail(String newEmail){
        this.email = newEmail;
        notifyListeners();
    }

    public String getPhoneNum(){
        return this.phoneNum;
    }

    public void setPhoneNum(String newPhoneNum){
        this.phoneNum = newPhoneNum;
        notifyListeners();
    }

    public void setRider(){
        rider = new UserRider(this);
        notifyListeners();
    }

    public UserRider getRider(){
        return this.rider;
    }

    public void setDriver(){
        driver = new UserDriver(this);
        notifyListeners();
    }

    public UserDriver getDriver(){
        return this.driver;
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
