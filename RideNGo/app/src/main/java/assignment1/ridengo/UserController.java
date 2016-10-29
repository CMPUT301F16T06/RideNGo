package assignment1.ridengo;

import android.os.AsyncTask;
import android.util.Log;

import com.searchly.jestdroid.DroidClientConfig;
import com.searchly.jestdroid.JestClientFactory;
import com.searchly.jestdroid.JestDroidClient;

import java.util.ArrayList;
import java.util.List;

import io.searchbox.core.DocumentResult;
import io.searchbox.core.Index;
import io.searchbox.core.Search;
import io.searchbox.core.SearchResult;

/**
 * Created by Mingjun on 10/11/2016.
 */
public class UserController {

    private static JestDroidClient client;
    private static UserList userList = null;

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

    // Save current user list
    static public void saveUserList(){
        //Save to server.
    }

    static public void loadUserListFromServer(){

    }

    public static class GetUsersTask extends AsyncTask<String, Void, ArrayList<User>> {
        @Override
        protected ArrayList<User> doInBackground(String... search_parameters) {
            verifySettings();

            ArrayList<User> users = new ArrayList<User>();


            // String search_string = "{\"from\": 0, \"size\": 10000}";
            String search_string = "{\"from\": 0, \"size\": 10000, \"query\": {\"match\": {\"username\": \"" + search_parameters[0] + "\"}}}";

            // assume that search_parameters[0] is the only search term we are interested in using
            Search search = new Search.Builder(search_string)
                    .addIndex("testing")
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


    // TODO we need a function which adds a tweet!
    public static class AddUsersTask extends AsyncTask<User, Void, Void> {

        @Override
        protected Void doInBackground(User... users) {
            verifySettings();

            for (User user: users) {
                Index index = new Index.Builder(user).index("testing").type("user").build();

                try {
                    DocumentResult result = client.execute(index);
                    if (result.isSucceeded()) {
                        user.setId(result.getId());
                    }
                    else {
                        Log.i("Error", "Elastic search was not able to add the tweet.");
                    }
                }
                catch (Exception e) {
                    Log.i("Uhoh", "We failed to add a tweet to elastic search!");
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
