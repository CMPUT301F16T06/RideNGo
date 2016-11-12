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













}
