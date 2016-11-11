package assignment1.ridengo;

import java.util.ArrayList;

/**
 * The type Ride request controller.
 */
public class RideRequestController {

    private static RideRequestList requestList = null;

    /**
     * Get request list ride request list.
     *
     * @return the ride request list
     */
// To implement load from file or server
    static public RideRequestList getRequestList(){
        if(requestList == null){
            requestList = new RideRequestList();
        }
        return requestList;
    }


    /**
     * Save request list.
     */
// Save current request list
    static public void saveRequestList(){

    }

    /**
     * Load reqeust list.
     */
    static public void loadReqeustList(){

    }
}
