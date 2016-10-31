package assignment1.ridengo;

import java.util.ArrayList;

/**
 * Created by Mingjun on 10/11/2016.
 */
public class RideRequestController {

    private static RideRequestList requestList = null;

    // To implement load from file or server
    static public RideRequestList getRequestList(){
        if(requestList == null){
            requestList = new RideRequestList();
        }
        return requestList;
    }


    // Save current request list
    static public void saveRequestList(){

    }

    static public void loadReqeustList(){

    }
}
