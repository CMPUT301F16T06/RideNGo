package assignment1.ridengo;

import android.os.AsyncTask;
import android.util.Log;

import com.searchly.jestdroid.DroidClientConfig;
import com.searchly.jestdroid.JestClientFactory;
import com.searchly.jestdroid.JestDroidClient;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import io.searchbox.client.JestResult;
import io.searchbox.core.Delete;
import io.searchbox.core.DeleteByQuery;
import io.searchbox.core.DocumentResult;
import io.searchbox.core.Index;
import io.searchbox.core.Search;
import io.searchbox.core.SearchResult;
import io.searchbox.core.Update;

/**
 * Controller for a list of users.
 * 1.load from server
 * 2.save to server
 * 3.load from file(while offline)
 * 4.save to file(while offline)
 */
public class UserController {

    private static JestDroidClient client;
    private static UserList userList = null;

    /**
     * Get user list user list.
     *
     * @return the user list
     */
// To implement load from file or server
    static public UserList getUserList(){
        /*
        If(online){
            load from server
        }else{
            //load from file
        }
         */
        if(userList == null){
            userList = new UserList();
        }
        return userList;
    }

    /**
     * Save user list.
     */
// Save current user list
    static public void saveUserList(){
        //Save to server.
    }

    /**
     * Load user list from server.
     */
    static public void loadUserListFromServer(){
        if(userList == null) {
            userList = new UserList();
        }
        userList.clear();

        GetUsersTask getUsersTask = new GetUsersTask();
        getUsersTask.execute("");

        try {
            userList.getUsers().addAll(getUsersTask.get());
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        for(User user : userList.getUsers()) {
            user.addUpdateListener(user);
        }
    }

    static public void addUser(User user) {
        userList.addUser(user);
        AddUsersTask addUsersTask = new AddUsersTask();
        try{
            addUsersTask.execute(user);
        }catch(RuntimeException e) {
            Log.i("Error", "Not able to add user to elasticsearch server.");
            e.printStackTrace();
        }
    }

    static public void updateUser(User user) {
        DeleteUsersTask deleteUsersTask = new DeleteUsersTask();
        AddUsersTask addUsersTask = new AddUsersTask();

        try{

            deleteUsersTask.execute(user);
            addUsersTask.execute(user);
        }catch(RuntimeException e) {
            Log.i("Error", "Not able to delete user to elasticsearch server.");
            e.printStackTrace();
        }
    }

    /**
     * The type Get users task.
     */
    public static class GetUsersTask extends AsyncTask<String, Void, ArrayList<User>> {
        @Override
        protected ArrayList<User> doInBackground(String... search_parameters) {
            verifySettings();

            ArrayList<User> users = new ArrayList<User>();


            // String search_string = "{\"from\": 0, \"size\": 10000}";
            String search_string = search_parameters[0];

            // assume that search_parameters[0] is the only search term we are interested in using
            Search search = new Search.Builder(search_string)
                    .addIndex("t06")
                    .addType("user")
                    .build();

            try {
                SearchResult result = client.execute(search);
                if (result.isSucceeded()) {
                    List<User> foundUsers = result.getSourceAsObjectList(User.class);
                    users.addAll(foundUsers);
                }
                else {
                    Log.i("Error", "The search query failed to find any users that matched.");
                }
            }
            catch (Exception e) {
                Log.i("Error", "Something went wrong when we tried to communicate with the elasticsearch server!");
            }

            return users;
        }
    }


    /**
     * The type Add users task.
     */
// TODO we need a function which adds a tweet!
    public static class AddUsersTask extends AsyncTask<User, Void, Void> {

        @Override
        protected Void doInBackground(User... users) {
            verifySettings();

            for (User user: users) {
                Index index = new Index.Builder(user).index("t06").type("user").build();

                try {
                    DocumentResult result = client.execute(index);

                    if (result.isSucceeded()) {
                        user.setId(result.getId());
                    }
                    else {
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

    public static class DeleteUsersTask extends AsyncTask<User, Void, Void> {

        @Override
        protected Void doInBackground(User... users) {
            verifySettings();

            for (User user: users) {
                final String query = "{\n" +
                        "    \"query\": {\n" +
                        "        \"term\": { \"username\" : \"" + user.getUsername() + "\" }\n" +
                        "    }\n" +
                        "}";
                DeleteByQuery index = new DeleteByQuery.Builder(query).addIndex("t06").addType("user").build();

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
