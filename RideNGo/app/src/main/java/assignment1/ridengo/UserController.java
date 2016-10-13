package assignment1.ridengo;

import java.util.ArrayList;

/**
 * Created by Mingjun on 10/11/2016.
 */
public class UserController {

    private static UserList userList = null;

    // To implement load from file or server
    static public UserList getUserList(){
        if(userList == null){
            userList = new UserList();
        }
        return userList;
    }

    // Save current user list
    static public void saveUserList(){

    }

}
