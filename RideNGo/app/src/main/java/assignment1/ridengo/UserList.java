package assignment1.ridengo;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * The type User list.
 * A list that keep tracks of all users register to the application
 */
public class UserList {

    private static ArrayList<User> userList = null;
    /**
     * The constant listeners.
     */
//    private static final long serialVersionUID = 6673446047991058932L;
    protected transient ArrayList<Listener> listeners = null;

    /**
     * Instantiates a new User list.
     */
    public UserList() {
        userList = new ArrayList<User>();
        listeners = new ArrayList<Listener>();
    }

    /**
     * Contains boolean.
     *
     * @param testUser the test user
     * @return the boolean
     */
    public boolean contains(User testUser) {
        for(User user : userList){
            if(user.getUsername().equals(testUser.getUsername())){
                return true;
            }
        }
        return false;
    }

    /**
     * Contains boolean.
     *
     * @param username the username
     * @return the boolean
     */
    public boolean contains(String username) {
        for(User user : userList){
            if(username.equals(user.getUsername())){
                return true;
            }
        }
        return false;
    }

    /**
     * Add listener.
     *
     * @param l the l
     */
    public void addListener(Listener l) {
        getListeners().add(l);
        for(User user : this.getUsers()){
            user.addListener(l);
        }
    }

    /**
     * Remove listener.
     *
     * @param l the l
     */
    public void removeListener(Listener l) {
        getListeners().remove(l);
        for(User user : this.getUsers()){
            user.removeListener(l);
        }
    }

    private ArrayList<Listener> getListeners() {
        if (listeners == null ) {
            listeners = new ArrayList<Listener>();
        }
        return listeners;
    }

    /**
     * Gets users.
     *
     * @return the users
     */
    public ArrayList<User> getUsers() {
        return userList;
    }

    /**
     * Add user.
     *
     * @param testUser the test user
     * @throws RuntimeException the runtime exception
     */
    public void addUser(User testUser) throws RuntimeException{
        if(!this.contains(testUser)){
            userList.add(testUser);
        }else{
            throw new RuntimeException("User Already Exists.");
        }
        notifyListeners();
    }

    /**
     * Gets user by username.
     *
     * @param name the name
     * @return the user by username
     */
    public User getUserByUsername(String name) {
        for(User user : userList){
            if(user.getUsername().equals(name)){
                return user;
            }
        }
        return null;
    }

    /**
     * Clear the userlist
     */
    public void clear(){
        userList.clear();
        notifyListeners();
    }

    /**
     * Update all listeners
     */
    private void notifyListeners() {
        for (Listener  listener : getListeners()) {
            listener.update();
        }
    }
}
