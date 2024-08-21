package org.proven.decisions2.Friends;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
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

public class RequestsFriendsActivity extends Activity {
    //The buttons of the footer to navigate in the app
    Button btFriends, btHome, btSettings;
    //Text to inform the user
    TextView infoText;
    //Listview shows a list of friends
    ListView listFriend;
    //CustomListAdapter is a custom class that extends Android's default list adapter. It is used to customize the appearance of each item in the friends list.
    CustomListAdapter mFriendsAdapter;
    //This the list the friends
    ArrayList<String> friendsNames = new ArrayList<>();
    //User authentication token
    String token;
    //User selected in friend list
    String selectedUsername;
    //Url for the http post in see friend Request
    String url = "https://5.75.251.56:8443/seeFriendRequest";
    //Url for the http post in accept friend request
    String url2 = "https://5.75.251.56:8443/aceptFriendRequest";
    SecureConnection secureConnection = new SecureConnection();

    /**
     * The onCreate() method in the RequestsFriendsActivity is responsible for initializing the activity, setting the layout, and handling button clicks.
     * Here's an explanation of the code:
     *
     * The super.onCreate(savedInstanceState) method is called to perform the default creation of the activity.
     * The layout file "requests_friends_layout" is set as the content view using setContentView(R.layout.requests_friends_layout).
     * The initializeElements() method is called to initialize the elements of the activity.
     * The readUser() method is called to retrieve user information.
     * The getFriends(token) method is called to retrieve the friend requests.
     * The btHome button's OnClickListener is set to start the SocialInterface activity when clicked.
     * The btFriends button's OnClickListener is set to start the FriendsActivity activity when clicked.
     * The btSettings button's OnClickListener is set to start the SettingsActivity activity when clicked.
     *
     * @param savedInstanceState If the activity is being re-initialized after
     *     previously being shut down then this Bundle contains the data it most
     *     recently supplied in {@link #onSaveInstanceState}.  <b><i>Note: Otherwise it is null.</i></b>
     *
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.requests_friends_layout);

        //Initialize the elements
        initializeElements();
        //Call the method
        readUser();
        //Call the method
        getFriends(token);

        btHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RequestsFriendsActivity.this, SocialInterface.class));
            }
        });

        btFriends.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RequestsFriendsActivity.this, FriendsActivity.class));
            }
        });

        btSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RequestsFriendsActivity.this, SettingsActivity.class));
            }
        });
    }

    /*Initialize the elements*/
    private void initializeElements() {
        btHome = findViewById(R.id.btHome);
        btFriends = findViewById(R.id.btFriends);
        btSettings = findViewById(R.id.btSettings);
        listFriend = findViewById(R.id.lvPersons);
        infoText = findViewById(R.id.infoText);
    }

    /* Method to instantiate the FriendsAsyncTask and start it */
    private void getFriends(String token) {
        new FriendsAsyncTask().execute(token);
    }

    /**
     * The setList() method in the RequestsFriendsActivity is responsible for setting up the list of friend requests and handling item clicks. Here's an explanation of the code:
     *
     * The mFriendsAdapter is created with the CustomListAdapter using the friendsList data and the layout resource R.layout.list_item_request.
     *
     * The mFriendsAdapter is set as the adapter for the listFriend ListView using listFriend.setAdapter(mFriendsAdapter).
     *
     * The condition checks if the friendsList is empty, has a size of zero, or contains an empty string. If any of these conditions are true, it means there are no friend requests to display.
     * If the condition is true, the listFriend ListView is set to View.GONE (hidden) and the infoText TextView is set to View.VISIBLE (visible).
     * If the condition is false, it means there are friend requests to display.
     *      The listFriend ListView is set to View.VISIBLE (visible) and the infoText TextView is set to View.GONE (hidden).
     *
     * The listFriend ListView's OnItemClickListener is set to handle item clicks. When an item is clicked:
     * The username of the selected item is retrieved using (String) parent.getItemAtPosition(position).
     * If the selectedUsername is null, a toast message is shown to the user asking them to select a username.
     * If the selectedUsername is not null, an AlertDialog is shown to confirm the friend request.
     * The AlertDialog displays a title and message.
     * If the user clicks "Yes", the friendRequestTask AsyncTask is executed to accept the friend request.
     * If the user clicks "No", the AlertDialog is dismissed.
     * @param friendsList
     */
    private void setList(ArrayList<String> friendsList) {
        mFriendsAdapter = new CustomListAdapter(this, friendsList, R.layout.list_item_request);
        listFriend.setAdapter(mFriendsAdapter);
        //check the list friend is empty or size is zero or contains is a empty string
        if (friendsList.isEmpty() || friendsList.size() == 0 || friendsList.contains("")) {
            //list is gone
            listFriend.setVisibility(View.GONE);
            infoText.setVisibility(View.VISIBLE);


        } else {
            //list is visible
            listFriend.setVisibility(View.VISIBLE);
            infoText.setVisibility(View.GONE);

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

                AlertDialog.Builder builder = new AlertDialog.Builder(RequestsFriendsActivity.this);
                builder.setTitle(getString(R.string.btConfirm));
                builder.setMessage(R.string.confirm_requestfriend);
                builder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Accept the friend request
                        new friendRequestTask().execute();
                    }
                });
                builder.setNegativeButton(R.string.no, null);
                builder.show();

            }


        });
    }

    /**
     * The FriendsAsyncTask is an AsyncTask used to fetch the list of friends for a user in the background. Here's an explanation of the code:
     *
     * The doInBackground() method is executed in the background thread when the AsyncTask is executed.
     * The params array contains the token passed as a parameter when executing the AsyncTask.
     *
     * The token variable is assigned the value of the token parameter params[0].
     *
     * An empty ArrayList called friendsList is created to store the friend names.
     *
     * An instance of OkHttpClient is obtained from secureConnection.getClient().
     *
     * The mediaType variable is created with the value MediaType.parse("application/json").
     *
     *
     * If the token is not null, the code block is executed.
     * An empty requestBodyString is created. (You may need to provide the appropriate request body based on your API requirements.)
     * A RequestBody is created with the mediaType and requestBodyString.
     * A Request is created using the URL, request method POST, headers (content-type and authorization), and the requestBody.
     * The request is executed using client.newCall(request).execute().
     * The response body is retrieved as a string and stored in the toSplit variable.
     * The toSplit string is processed to remove quotes and brackets and split it into separate friend names. The resulting friend names are added to the friendsList ArrayList.
     *
     * The friendsList is returned as the result of the background task.
     *
     * The onPostExecute() method is called with the result (friendsList) as a parameter.
     *
     * The friendsNames member variable is assigned the value of result.
     *
     * The setList() method is called with friendsNames to update the UI with the list of friends.
     */
    private class FriendsAsyncTask extends AsyncTask<String, Void, ArrayList<String>> {

        @Override
        protected ArrayList<String> doInBackground(String... params) {
            token = params[0];
            ArrayList<String> friendsList = new ArrayList<>();
            OkHttpClient client = secureConnection.getClient();
            MediaType mediaType = MediaType.parse("application/json");
            if (token != null) {
                String requestBodyString = "";
                RequestBody requestBody = RequestBody.create(mediaType, requestBodyString);
                Request request = new Request.Builder()
                        .url(url).post(requestBody)
                        .addHeader("content-type", "application/json")
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
     The friendRequestTask is an AsyncTask used to send a friend request to a selected user in the background. Here's an explanation of the code:

     The doInBackground() method is executed in the background thread when the AsyncTask is executed. The params array is not used in this case.

     An instance of OkHttpClient is obtained from secureConnection.getClient().

     The mediaType variable is created with the value MediaType.parse("application/json").

     A RequestBody is created with the username of the selected user to whom the friend request is being sent.

     A Request is created using the URL, request method POST, headers (content-type and authorization), and the requestBody.

     The request is executed using client.newCall(request).execute().

     The response is checked if it's successful. If it is, true is returned. Otherwise, false is returned.

     If an exception occurs during the network request, it's caught, and false is returned.

     The onPostExecute() method is called with the result (whether the friend request was successful or not) as a parameter.

     If the result is true, a toast is shown indicating a successful friend request. If it's false, a toast is shown indicating an error occurred during the friend request.
     */
    private class friendRequestTask extends AsyncTask<String, Void, Boolean> {

        @Override
        protected Boolean doInBackground(String... params) {

            OkHttpClient client = secureConnection.getClient();
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
                Toast.makeText(getApplicationContext(), getString(R.string.accept_friend) + selectedUsername, Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getApplicationContext(), "Error accept  friend  " + selectedUsername, Toast.LENGTH_SHORT).show();
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