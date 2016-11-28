package assignment1.ridengo;

import android.app.Activity;
import android.app.Dialog;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.widget.Toast;

import com.searchly.jestdroid.DroidClientConfig;
import com.searchly.jestdroid.JestClientFactory;
import com.searchly.jestdroid.JestDroidClient;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import io.searchbox.client.JestResult;
import io.searchbox.core.DeleteByQuery;
import io.searchbox.core.DocumentResult;
import io.searchbox.core.Index;
import io.searchbox.core.Search;
import io.searchbox.core.SearchResult;

/**
 * The type Ride request controller.
 */
public class RideRequestController {

    private static JestDroidClient client;
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
     * Load request list from server.
     *
     * @param query the query
     */
    static public void loadRequestListFromServer(String query){
        if(requestList == null) {
            requestList = new RideRequestList();
        }
        requestList.clear();

        GetRequestsTask getRequestsTask = new GetRequestsTask();
        getRequestsTask.execute(query);

        try {
            requestList.getRequests().addAll(getRequestsTask.get());
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        for(RideRequest request : requestList.getRequests()) {
            request.addUpdateListener(request);
        }
    }

    /**
     * Add request.
     *
     * @param request the request
     */
    static public void addRequest(RideRequest request) {
        getRequestList().addRequest(request);
        //requestList.addRequest(request);
        AddRequestsTask addRequestsTask = new AddRequestsTask();
        try{
            addRequestsTask.execute(request);
        }catch(RuntimeException e) {
            Log.i("Error", "Not able to add user to elasticsearch server.");
            e.printStackTrace();
        }
    }

    /**
     * Remove request.
     *
     * @param request the request
     */
    public static void removeRequest(RideRequest request) {
        requestList.removeRequest(request);
        DeleteRequestsTask deleteRequestsTask = new DeleteRequestsTask();
        try{
            deleteRequestsTask.execute(request);
        }catch(RuntimeException e){
            Log.i("Error", "Not able to add user to elasticsearch server.");
            e.printStackTrace();
        }
    }

    /**
     * Notify user.
     *
     * @param username the username
     * @param activity the activity
     */
    public static void notifyUser(String username, Activity activity){
        loadRequestListFromServer("{\"from\":0,\"size\":10000,\"query\": { \"match\": { \"rider.username\": \"" + username + "\"}}}");
        for(RideRequest request : requestList.getRequests()) {

            if(request.isNotifyRider() && request.getRider().getUsername().equals(username)) {
                AlertDialog.Builder adb = new AlertDialog.Builder(activity);
                adb.setMessage("Someone accepted your request.");
                adb.show();
                request.setNotifyRider(false);
            }
        }

        loadRequestListFromServer("{\"from\":0,\"size\":10000,\"query\": { \"match\": { \"driver.username\": \"" + username + "\"}}}");
        for(RideRequest request : requestList.getRequests()) {
            if(request.isNotifyDriver() && request.getDriver().getUsername().equals(username)) {
                AlertDialog.Builder adb = new AlertDialog.Builder(activity);
                adb.setMessage("Rider has confirmed you as the driver.");
                adb.show();
                request.setNotifyDriver(false);
            }
        }
    }

    /**
     * The type Get requests task.
     */
    public static class GetRequestsTask extends AsyncTask<String, Void, ArrayList<RideRequest>> {
        @Override
        protected ArrayList<RideRequest> doInBackground(String... search_parameters) {
            verifySettings();

            ArrayList<RideRequest> requests = new ArrayList<RideRequest>();


            String search_string = search_parameters[0];
            //String search_string = search_parameters[0];

            // assume that search_parameters[0] is the only search term we are interested in using
            Search search = new Search.Builder(search_string)
                    .addIndex("t06")
                    .addType("request")
                    .build();

            try {
                SearchResult result = client.execute(search);
                if (result.isSucceeded()) {
                    List<RideRequest> foundRequests = result.getSourceAsObjectList(RideRequest.class);
                    requests.addAll(foundRequests);
                }
                else {
                    Log.i("Error", "The search query failed to find any users that matched.");
                }
            }
            catch (Exception e) {
                Log.i("Error", "Something went wrong when we tried to communicate with the elasticsearch server!");
            }

            return requests;
        }
    }


    /**
     * The type Add requests task.
     */
// TODO we need a function which adds a tweet!
    public static class AddRequestsTask extends AsyncTask<RideRequest, Void, Void> {

        @Override
        protected Void doInBackground(RideRequest... requests) {
            verifySettings();

            for (RideRequest request: requests) {
                Index index = new Index.Builder(request).index("t06").type("request").build();

                try {
                    DocumentResult result = client.execute(index);

                    if (!result.isSucceeded()) {
                        Log.i("Error", "Elastic search was not able to add the user.");
                    }
                }
                catch (Exception e) {
                    Log.i("Uhoh", "We failed to add a user to elastic search!");
                    e.printStackTrace();
                }
            }

            return null;
        }
    }

    /**
     * The type Delete requests task.
     */
    public static class DeleteRequestsTask extends AsyncTask<RideRequest, Void, Void> {

        @Override
        protected Void doInBackground(RideRequest... requests) {
            verifySettings();

            for (RideRequest request: requests) {
                final String query = "{\n" +
                        "    \"query\": {\n" +
                        "        \"match\": { \"id\" : " + request.getId() + " }\n" +
                        "    }\n" +
                        "}";
                DeleteByQuery index = new DeleteByQuery.Builder(query).addIndex("t06").addType("request").build();

                try {
                    JestResult result = client.execute(index);
                    if (!result.isSucceeded()) {
                        Log.i("Error", "Elastic search was not able to delete the user.");
                    }
                }
                catch (Exception e) {
                    Log.i("Uhoh", "We failed to delete a user to elastic search!");
                    e.printStackTrace();
                }
            }

            return null;
        }
    }


    private static void verifySettings() {
        // if the client hasn't been initialized then we should make it!
        if (client == null) {
            DroidClientConfig.Builder builder = new DroidClientConfig.Builder("http://cmput301.softwareprocess.es:8080");
            DroidClientConfig config = builder.build();

            JestClientFactory factory = new JestClientFactory();
            factory.setDroidClientConfig(config);
            client = (JestDroidClient) factory.getObject();
        }
    }
}
