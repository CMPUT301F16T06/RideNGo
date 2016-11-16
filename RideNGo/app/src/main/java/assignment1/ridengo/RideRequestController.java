package assignment1.ridengo;

/**
 * Controller for Ride request lists
 *1. Load from server
 * 2. Save to Server
 * 3. Load from file
 * 4. Save to file
 */
class RideRequestController {

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
