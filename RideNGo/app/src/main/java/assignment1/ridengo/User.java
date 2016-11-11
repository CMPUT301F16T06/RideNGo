package assignment1.ridengo;

import java.sql.Driver;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * The type User.
 */
public class User {

    private String id;
    private String username;
    private String email;
    private String phoneNum;
    private UserRider rider = new UserRider(this);
    private UserDriver driver = new UserDriver(this);
    private ArrayList<Listener> listeners;

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
     * Set rider.
     */
    public void setRider(){
        rider = new UserRider(this);
        notifyListeners();
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
     * Set driver.
     */
    public void setDriver(){
        driver = new UserDriver(this);
        notifyListeners();
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

}
