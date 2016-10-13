package assignment1.ridengo;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by Mingjun on 10/11/2016.
 */
public class User {

    private String username;
    private String password;
    private String email;
    private String phoneNum;
    private List<RideRequest> requests;

    public User(String username, String password, String email, String phoneNum){
        this.username = username;
        this.password = password;
        this.email = email;
        this.phoneNum = phoneNum;
    }

    public String getUsername(){
        return this.username;
    }

    public String getEmail(){
        return this.email;
    }

    public void setEmail(String newEmail){
        this.email = newEmail;
    }

    public String getPhoneNum(){
        return this.phoneNum;
    }

    public void setPhoneNum(String newPhoneNum){
        this.phoneNum = newPhoneNum;
    }

    public List<RideRequest> getRequests(){
        if(requests == null){
            requests = new ArrayList<RideRequest>();
        }
        return this.requests;
    }
}
