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
}
