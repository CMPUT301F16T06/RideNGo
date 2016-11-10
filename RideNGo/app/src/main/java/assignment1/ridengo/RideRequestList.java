package assignment1.ridengo;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by Mingjun on 10/11/2016.
 */
public class RideRequestList {

    private ArrayList<RideRequest> requestList = null;
//    private static final long serialVersionUID = 6673446047991058932L;
    protected transient ArrayList<Listener> listeners = null;

    public RideRequestList() {
        requestList = new ArrayList<RideRequest>();
        listeners = new ArrayList<Listener>();
    }

    public boolean contains(RideRequest testRequest) {
        return requestList.contains(testRequest);
    }

    public void addListener(Listener l) {
        getListeners().add(l);
        for(RideRequest request : requestList){
            request.addListener(l);
        }
    }

    public void removeListener(Listener l) {
        getListeners().remove(l);
        for(RideRequest request : requestList){
            request.removeListener(l);
        }
    }

    private ArrayList<Listener> getListeners() {
        if (listeners == null ) {
            listeners = new ArrayList<Listener>();
        }
        return listeners;
    }

    public List<RideRequest> getRequests() {
        if(requestList == null){
            requestList = new ArrayList<RideRequest>();
        }
        return requestList;
    }

    public List<RideRequest> getRequestsWithRider(String username){
        List<RideRequest> rideRequests = new ArrayList<RideRequest>();
        for(RideRequest request: requestList){
            if(request.getRider().getUser().getUsername().equals(username)){
                rideRequests.add(request);
            }
        }
        return rideRequests;
    }

    public RideRequest getRequestWithHash(int h){
        for(RideRequest rideRequest: requestList){
            if(rideRequest.hashCode() == h){
                return rideRequest;
            }
        }
        return null;
    }

    public void addRequest(RideRequest testRequest) {
        requestList.add(testRequest);
        notifyListeners();
    }

    private void notifyListeners() {
        for (Listener  listener : getListeners()) {
            listener.update();
        }
    }

    public void clear() {
        requestList.clear();
        notifyListeners();
    }

    public void removeRequest(int index){
        requestList.remove(index);
        notifyListeners();
    }

    //////////////Temp Test Case/////////////
    public void addTestCase(){
        User user1 = new User("A","a@example.com","7800000000");
        UserRider rider1 = new UserRider(user1);
        RideRequest test1 = new RideRequest("Uni","100St","Have fun",rider1,10.00);
        User user2 = new User("A","a@example.com","7800000000");
        UserRider rider2 = new UserRider(user1);
        RideRequest test2 = new RideRequest("Uni","101St","Have fun",rider1,20.00);
        requestList.add(test1);
        requestList.add(test2);
    }
    public List<RideRequest> getTestRequests(){
        addTestCase();
        if(requestList == null){
            requestList = new ArrayList<RideRequest>();
        }
        return requestList;
    }

}
