package org.proven.decisions2.Friends;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.proven.decisions2.R;
import org.proven.decisions2.SecureConnection;
import org.proven.decisions2.Settings.SettingsActivity;
import org.proven.decisions2.SocialInterface;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class RemoveFriendsActivity extends Activity {
    //The buttons of the footer to navigate in the app
    Button btFriends, btHome, btSettings;
    //Text to inform the user
    TextView infoText;
    //Filter for search the user in the list
    EditText searchFriend;
    //Listview shows a list of friends
    ListView listFriend;
    //hide the line
    View line;
    //CustomListAdapter is a custom class that extends Android's default list adapter. It is used to customize the appearance of each item in the friends list.
    CustomListAdapter mFriendsAdapter;
    //This the list the friends
    ArrayList<String> friendsNames = new ArrayList<>();
    //User authentication token
    String token;
    //User selected in friend list
    String selectedUsername;
    //Url for the http post in get friends
    String url = "https://5.75.251.56:8443/getFriends";

    //Url for the http post in remove friend
    String url2 = "https://5.75.251.56:8443/removeFriend";

    //Method returns an OkHttpClient object that can be used to make HTTP requests, but ignores any SSL certificate issues that might arise when establishing an HTTPS connection.
    SecureConnection secureConnection = new SecureConnection();

    /**
     * The code provided shows an overridden onCreate() method in the current class, which is likely an activity called RemoveFriendsActivity.
     * This method is called when the activity is being created.
     *
     * Here's what the code does:
     *
     * It calls the superclass implementation of onCreate() by using super.onCreate(savedInstanceState) to perform any necessary initialization.
     *
     * It sets the content view of the activity to the layout specified by R.layout.remove_friends_layout using setContentView().
     * This determines the UI layout to be displayed on the activity.
     *
     * It initializes the elements by calling the initializeElements() method.
     *
     * It calls the readUser() method, which is likely used to retrieve user information or perform user-related tasks.
     *
     * It calls the getFriends(token) method, which is likely used to retrieve the user's friends or perform related tasks based on the provided token.
     *
     * It sets an OnClickListener for the btHome button, which is likely a button representing the "Home" functionality.
     *
     * When the button is clicked, it creates an Intent to start the SocialInterface activity.
     * It calls readUser() to retrieve user information.
     * It logs the user ID using Log.d().
     * It starts the SocialInterface activity by calling startActivity(intent).
     * It sets an OnClickListener for the btFriends button, which is likely a button representing the "Friends" functionality.
     *
     * When the button is clicked, it creates an Intent to start the FriendsActivity activity.
     * It calls readUser() to retrieve user information.
     * It logs the user ID using Log.d().
     * It starts the FriendsActivity activity by calling startActivity(intent).
     * It sets an OnClickListener for the btSettings button, which is likely a button representing the "Settings" functionality.
     *
     * When the button is clicked, it creates an Intent to start the SettingsActivity activity.
     * It calls readUser() to retrieve user information.
     * It logs the user ID using Log.d().
     * It starts the SettingsActivity activity by calling startActivity(intent).
     *
     * @param savedInstanceState If the activity is being re-initialized after
     *     previously being shut down then this Bundle contains the data it most
     *     recently supplied in {@link #onSaveInstanceState}.  <b><i>Note: Otherwise it is null.</i></b>
     *
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.remove_friends_layout);
        //Initialize the elements
        initializeElements();
        //Call the method
        readUser();
        //Call the method
        getFriends(token);

        btHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RemoveFriendsActivity.this, SocialInterface.class);
                readUser();
                Log.d("TAG", "userIdFriendsActivity: " + token);
                startActivity(intent);
            }
        });
        btFriends.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RemoveFriendsActivity.this, FriendsActivity.class);
                readUser();
                Log.d("TAG", "userIdFriendsActivity: " + token);
                startActivity(intent);
            }
        });

        btSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RemoveFriendsActivity.this, SettingsActivity.class);
                readUser();
                Log.d("TAG", "userIdFriendsActivity: " + token);
                startActivity(intent);
            }
        });

    }

    /*Initialize the elements*/
    private void initializeElements() {
        btHome = findViewById(R.id.btHome);
        btFriends = findViewById(R.id.btFriends);
        btSettings = findViewById(R.id.btSettings);
        listFriend = findViewById(R.id.lvPersons);
        searchFriend = findViewById(R.id.etdecision);
        infoText = findViewById(R.id.infoText);
        line = findViewById(R.id.line);
    }

    /*Method for the get friends*/
    private void getFriends(String username) {
        new FriendsAsyncTask().execute(username);
    }

    /**
     * The setList() method appears to be responsible for setting up the list of friends in the RemoveFriendsActivity and handling various UI
     * elements and interactions. Here's an explanation of the code:
     *
     * It takes an ArrayList<String> parameter friendsList, which represents the list of friends to be displayed.
     *
     * It creates a new instance of CustomListAdapter called mFriendsAdapter and initializes it with the activity, the friendsList, and the layout resource ID R.layout.list_item_remove.
     *
     * It sets the adapter for the listFriend ListView using listFriend.setAdapter(mFriendsAdapter). This connects the adapter to the ListView and displays the list of friends.
     *
     * It checks if the friendsList is empty or contains empty strings. If it is empty or contains empty strings, it adjusts the visibility of certain UI elements:
     * listFriend ListView is set to View.GONE (hidden).
     * infoText TextView is set to View.VISIBLE (visible).
     * searchFriend EditText is set to View.GONE (hidden).
     * line View is set to View.INVISIBLE (invisible).
     * This is likely to handle the case when there are no friends to display.
     *
     * If the friendsList is not empty, it adjusts the visibility of the UI elements to display the list:
     * listFriend ListView is set to View.VISIBLE (visible).
     * infoText TextView is set to View.GONE (hidden).
     * searchFriend EditText is set to View.VISIBLE (visible).
     * line View is set to View.VISIBLE (visible).
     *
     * It sets an OnItemClickListener for the listFriend ListView to handle item click events. When an item is clicked, the username of the selected item is retrieved.
     * If the selected username is null, it displays a toast message asking the user to select a username.
     * If a username is selected, it displays an AlertDialog to confirm the removal of the friend.
     * If the user confirms the removal by clicking "Yes", it executes an AsyncTask called removeFriendTask to remove the friend.
     *
     * It sets a TextWatcher for the searchFriend EditText to filter the friends list based on the text entered.
     * The onTextChanged() method of the TextWatcher triggers the filter by calling mFriendsAdapter.getFilter().filter(s), where s is the entered text.
     * This allows the user to search for specific friends in the list by typing in the search field.
     * @param friendsList
     */
    private void setList(ArrayList<String> friendsList) {
        mFriendsAdapter = new CustomListAdapter(this, friendsList, R.layout.list_item_remove);
        listFriend.setAdapter(mFriendsAdapter);
        //check the list friend is empty or size is zero or contains is a empty string
        if (friendsList.isEmpty() || friendsList.size() == 0 || friendsList.contains("")) {
            //list is gone
            listFriend.setVisibility(View.GONE);
            //info text is visible
            infoText.setVisibility(View.VISIBLE);
            //search friend is gone
            searchFriend.setVisibility(View.GONE);
            //line is gone
            line.setVisibility(View.INVISIBLE);

        } else {
            //list is visible
            listFriend.setVisibility(View.VISIBLE);
            //info text is gone
            infoText.setVisibility(View.GONE);
            //search friend is visible
            searchFriend.setVisibility(View.VISIBLE);
            //line is visible
            line.setVisibility(View.VISIBLE);
        }
        listFriend.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Get the username of the selected item in the list
                selectedUsername = (String) parent.getItemAtPosition(position);
                Log.d("Selected username", selectedUsername);

                if (selectedUsername == null) {
                    Toast.makeText(getApplicationContext(), "Please select a username", Toast.LENGTH_SHORT).show();
                    return;
                }
                //Dialog for the user confirm
                AlertDialog.Builder builder = new AlertDialog.Builder(RemoveFriendsActivity.this);
                builder.setTitle(getString(R.string.btConfirm));
                builder.setMessage(R.string.confirm_remove);
                builder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Send the friend request
                        new removeFriendTask().execute();
                    }
                });
                builder.setNegativeButton(R.string.no, null);
                builder.show();

            }

        });

        searchFriend.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mFriendsAdapter.getFilter().filter(s);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


    }

    /**
     * The FriendsAsyncTask class extends AsyncTask and is responsible for retrieving the list of friends from the server. Here's an explanation of the code:
     *
     * The doInBackground() method is executed in the background thread. It takes a variable number of String parameters, with the first parameter (params[0]) representing the token.
     *
     * The token variable is assigned the value of the first parameter.
     *
     * An ArrayList<String> called friendsList is created to store the list of friends.
     *
     * An OkHttpClient instance is created using secureConnection.getClient().
     *
     * The mediaType variable is initialized with the media type "application/json".
     *
     *
     *
     * If the token is not null, the following steps are executed:
     * The request body string is constructed as "username=" + token.
     * A RequestBody is created using the mediaType and the request body string.
     * A Request is constructed with the URL, request body, and necessary headers.
     * The request is executed using client.newCall(request).execute(), and the response is obtained.
     * The response body is converted to a string and stored in the toSplit variable.
     * The toSplit string is processed to remove unnecessary characters and split it into an array of strings representing the friend names.
     * The friend names are added to the friendsList using friendsList.addAll(Arrays.asList(textoSinCorchetes.split(","))).
     *
     * The friendsList is returned as the result of the background operation.
     *
     * The onPostExecute() method is executed in the main/UI thread after the background operation is complete.
     *
     * The result parameter represents the friendsList obtained from the background operation.
     *
     * The friendsNames variable is assigned the value of result.
     *
     * The setList() method is called with friendsNames to update the UI and display the list of friends.
     */
    private class FriendsAsyncTask extends AsyncTask<String, Void, ArrayList<String>> {

        @Override
        protected ArrayList<String> doInBackground(String... params) {
            token = params[0];
            ArrayList<String> friendsList = new ArrayList<>();
            OkHttpClient client =secureConnection.getClient();
            MediaType mediaType = MediaType.parse("application/json");
            if (token != null) {
                String requestBodyString = "username=" + token;
                RequestBody requestBody = RequestBody.create(mediaType, requestBodyString);
                Request request = new Request.Builder()
                        .url(url)
                        .post(requestBody)
                        .addHeader("content-type", "application/json")
                        .addHeader("cache-control", "no-cache")
                        .addHeader("Authorization", token)
                        .build();

                try {
                    Response response = client.newCall(request).execute();
                    String toSplit = response.body().string();
                    String textoSinComillas = toSplit.replace("\"", "");
                    String textoSinCorchetes = textoSinComillas.replace("[", "").replace("]", "");
                    friendsList.addAll(Arrays.asList(textoSinCorchetes.split(",")));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return friendsList;
        }

        @Override
        protected void onPostExecute(ArrayList<String> result) {
            super.onPostExecute(result);
            friendsNames = result;
            setList(friendsNames);
        }
    }

    /**
     * The removeFriendTask class extends AsyncTask and is responsible for sending a request to remove a friend. Here's an explanation of the code:
     *
     * The doInBackground() method is executed in the background thread. It takes a variable number of String parameters, but in this case, the parameters are not used.
     * An OkHttpClient instance is created using secureConnection.getClient().
     * The mediaType variable is initialized with the media type "application/json".
     * The request body is constructed using the selected username: "username=" + selectedUsername.
     * A Request is constructed with the URL, request body, and necessary headers.
     * The request is executed using client.newCall(request).execute(), and the response is obtained.
     * If the response is successful (HTTP status code 2xx), true is returned; otherwise, false is returned.
     * The onPostExecute() method is executed in the main/UI thread after the background operation is complete.
     * The result parameter represents the result of the background operation (whether the friend removal was successful or not).
     * If the result is true, a toast is shown indicating that the friend has been successfully deleted.
     * If the result is false, a toast is shown indicating that there was an error deleting the friend.
     */
    private class removeFriendTask extends AsyncTask<String, Void, Boolean> {

        @Override
        protected Boolean doInBackground(String... params) {

            OkHttpClient client =secureConnection.getClient();
            MediaType mediaType = MediaType.parse("application/json");
            RequestBody requestBody = RequestBody.create(mediaType, "username=" + selectedUsername);

            Request request = new Request.Builder()
                    .url(url2)
                    .post(requestBody)
                    .addHeader("content-type", "application/json")
                    .addHeader("Authorization", token)
                    .build();

            // Send HTTP POST friend request
            try {
                Response response = client.newCall(request).execute();
                if (response.isSuccessful()) {
                    return true;
                } else {
                    return false;
                }
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
        }

        @Override
        protected void onPostExecute(Boolean result) {
            if (result) {
                Toast.makeText(getApplicationContext(), getString(R.string.friend_deleted) + selectedUsername, Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getApplicationContext(), "Error delete friend  " + selectedUsername, Toast.LENGTH_SHORT).show();
            }
        }
    }


    /**
     * The provided code reads the contents of a file named "token.txt" and assigns the read value to the username variable. Here's a breakdown of the code:
     *
     * The method readUser() is defined.
     *
     * It creates a File object named filename with the path to the "token.txt" file in the app's internal storage directory (getFilesDir()).
     *
     * It wraps the file reading operations in a try-catch block to handle any potential IOException.
     *
     * Inside the try block, it creates a FileReader to read the file.
     *
     * It creates a BufferedReader named bufferedReader to read the file contents line by line.
     *
     * It reads the first line of the file using the readLine() method and assigns the value to the username variable.
     *
     * It closes the BufferedReader and FileReader using the close() method.
     *
     * If an IOException occurs, it throws a RuntimeException with the caught exception as the cause.
     */
    private void readUser() {
        File filename = new File(getFilesDir(), "token.txt");
        try {
            FileReader reader = new FileReader(filename);
            BufferedReader bufferedReader = new BufferedReader(reader);
            token = bufferedReader.readLine();
            bufferedReader.close();
            reader.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}