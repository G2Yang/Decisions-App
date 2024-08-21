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

public class AddFriendsActivity extends Activity {
    //The buttons of the footer to navigate in the app
    Button btFriends, btHome, btSettings;
    //Listview shows a list of friends
    ListView listFriend;
    //Filter for search the user in the list
    EditText searchFriend;
    //CustomListAdapter is a custom class that extends Android's default list adapter. It is used to customize the appearance of each item in the friends list.
    CustomListAdapter mFriendsAdapter;
    //This the list the friends
    ArrayList<String> friendsNames = new ArrayList<>();
    //User authentication token
    String token;
    //User selected in friend list
    String selectedUsername;
    //Url for the http post in get users
    //String url = "http://143.47.249.102:7070/getUsers";
    String url = "https://5.75.251.56:8443/getUsers";
    //Url for the http post in send friend request
    //String url2 = "http://143.47.249.102:7070/sendFriendRequest";
    String url2 = "https://5.75.251.56:8443/sendFriendRequest";
    //Method returns an OkHttpClient object that can be used to make HTTP requests, but ignores any SSL certificate issues that might arise when establishing an HTTPS connection.
    SecureConnection secureConnection = new SecureConnection();


    /**
     * The onCreate method you provided is part of an activity in an Android application. Here's a breakdown of what the code does:
     *
     * The onCreate method is overridden to perform initialization tasks when the activity is created.
     * The setContentView method is called to set the layout of the activity to the "addfriends_layout" XML layout file.
     * The initializeElements method is called to initialize the elements (views) in the layout.
     * The readUser method is called to read the user token from a file.
     * The getFriends method is called to retrieve the friends list using the user token.
     * Three click listeners are set for the "Home," "Friends," and "Settings" buttons to navigate to different activities.
     * In the click listener for the "Home" button, the user token is read again, and an intent is created to navigate to the "SocialInterface" activity.
     * In the click listener for the "Friends" button, an intent is created to navigate to the "FriendsActivity" activity.
     * In the click listener for the "Settings" button, the user token is read again, and an intent is created to navigate to the "SettingsActivity" activity.
     *
     * @param savedInstanceState If the activity is being re-initialized after
     *     previously being shut down then this Bundle contains the data it most
     *     recently supplied in {@link #onSaveInstanceState}.  <b><i>Note: Otherwise it is null.</i></b>
     *
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.addfriends_layout);
        //Initialize the elements
        initializeElements();
        //Call the method
        readUser();

        //Call the method
        getFriends(token);

        btHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AddFriendsActivity.this, SocialInterface.class);
                readUser();
                Log.d("TAG", "userIdFriendsActivity: " + token);
                startActivity(intent);
            }
        });
        btFriends.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AddFriendsActivity.this, FriendsActivity.class));
            }
        });


        btSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AddFriendsActivity.this, SettingsActivity.class);
                readUser();
                Log.d("TAG", "userIdSocial: " + token);
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
    }

    /* Method to instantiate the FriendsAsyncTask and start it */
    private void getFriends(String token) {
        new FriendsAsyncTask().execute(token);
    }

    /**
     * The provided code performs the following actions:
     *
     * The setList method receives a friendsList parameter which is an ArrayList of Strings.
     * Creates a new ArrayList called firstTenFriends containing the first ten elements of friendsList. This is accomplished by using the subList method of ArrayList.
     * Create an instance of CustomListAdapter by passing this (the current class), firstTenFriends, and the layout of list_item_add as arguments. This instance is assigned to the mFriendsAdapter variable.
     * Sets mFriendsAdapter as the adapter for listFriend, which appears to be a list component in a user interface.
     * Sets an OnItemClickListener to listFriend. When an item in the list is clicked, the code inside the onItemClick method is executed.
     * Inside onItemClick, the username of the selected item in the list is obtained and stored in the selectedUsername variable. Then, a debug message (log) is recorded with the selected username.
     * If selectedUsername is null, a toast message is displayed prompting the user to select a username.
     * Create an alert dialog using AlertDialog.Builder. The dialog displays a title and a confirmation message to add a friend.
     * Set up a positive button in the dialog with the text "Yes" and an OnClickListener that calls the SendFriendRequestTask class to send a friend request. SendFriendRequestTask is likely a class defined elsewhere in your code and is running in the background.
     * Set a negative button in the dialog with the text "No", without taking any action.
     * Shows the alert dialog.
     * Add a TextWatcher to the searchFriend component. The TextWatcher has three methods: beforeTextChanged, onTextChanged, and afterTextChanged. In this case, only the onTextChanged method is implemented. When the text changes in searchFriend, the getFilter().filter(s) method in mFriendsAdapter is called to filter the friends based on the text entered.
     * The beforeTextChanged and afterTextChanged methods do not contain any code and do not perform any action.
     * @param friendsList
     */
    private void setList(ArrayList<String> friendsList) {
        ArrayList<String>[] filteredFriendsList = new ArrayList[]{new ArrayList<>(friendsList.subList(0, Math.min(15, friendsList.size())))};
        mFriendsAdapter = new CustomListAdapter(this, filteredFriendsList[0], R.layout.list_item_add);
        listFriend.setAdapter(mFriendsAdapter);
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
                AlertDialog.Builder builder = new AlertDialog.Builder(AddFriendsActivity.this);
                builder.setTitle(getString(R.string.btConfirm));
                builder.setMessage(R.string.confirm_addfriend);
                builder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Send the friend request
                        new SendFriendRequestTask().execute();
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
                String searchText = s.toString().toLowerCase();

                if (searchText.isEmpty()) {
                    filteredFriendsList[0] = new ArrayList<>(friendsList.subList(0, Math.min(15, friendsList.size())));
                } else {
                    //Filter entire list based on search text
                    filteredFriendsList[0].clear();
                    for (String friend : friendsList) {
                        if (friend.toLowerCase().startsWith(searchText)) {
                            filteredFriendsList[0].add(friend);
                        }
                    }
                }

                mFriendsAdapter = new CustomListAdapter(AddFriendsActivity.this, filteredFriendsList[0], R.layout.list_item_add);
                listFriend.setAdapter(mFriendsAdapter);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    /**
     Here's what each method in FriendsAsyncTask does:
     *
     *
     * 1- doInBackground: This method runs in the background and is in charge of carrying out the operation to obtain the friends list. It takes a string of parameters (in this case, only one parameter is expected) and returns an ArrayList of strings representing the friends list returned.
     *
     * In this method:
     *
     * The value of the first parameter (params[0]) is stored in the token variable.
     * An instance of OkHttpClient is created using secureConnection.getClient(). This implies that the OkHttp library is being used to make secure HTTP requests.
     * The content media type is defined as "application/json".
     * Checks if token is not null. If not null:
     * An empty request body is created using the media type defined above.
     * A POST request is created using the specified URL, the request body, and the "content-type" and "Authorization" headers that are set to the appropriate values.
     * The API call is made using client.newCall(request).execute(). This sends the request to the server and gets a response.
     * Get the response body as a string using response.body().string().
     * Certain manipulations are performed on the text string to remove unwanted quotes and brackets.
     * Split the string into a list of individual friends using split(",") and add it to friendsList.
     * If an IOException occurs during the execution of the call, the stack trace is printed.
     * friendsList is returned as the result.
     *
     *
     * 2- onPostExecute: This method is executed after doInBackground finishes its execution in the background. It receives the result of doInBackground as a parameter (result in this case), which is the ArrayList of strings representing the friends list returned. This method is used to update the user interface with the returned results.
     *
     * In this method:
     *
     * The result is assigned to the variable friendsNames.
     * The setList method is called passing friendsNames as an argument. This method takes care of setting up a list adapter and displaying the friends in the user interface.
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
                        .url(url)
                        .post(requestBody)
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
     * The provided code shows another inner class called SendFriendRequestTask that also extends the AsyncTask class.
     * This class is used to send a friend request through a background HTTP POST call and then update the user interface with the result.
     *
     * Here's what each method in SendFriendRequestTask does:
     *
     * 1- doInBackground: This method runs in the background and handles sending the friend request. It takes a string parameter (in this case, no parameter is expected) and returns a boolean value indicating whether the request was sent successfully or not.
     *
     * In this method:
     *
     * An instance of OkHttpClient is created using secureConnection.getClient().
     * The media type is defined as "application/json".
     * A request body is created using the selected username (selectedUsername) as part of the data being sent.
     * A POST request is created using the specified URL, the request body, and the "content-type" and "Authorization" headers set with the appropriate values.
     * The API call is made using client.newCall(request).execute(). This sends the friend request to the server and gets a response.
     * It checks if the response was successful (response.isSuccessful()) and returns true. Otherwise, it returns false.
     * If an IOException occurs during the call execution, the stack trace is printed, and false is returned.
     *
     * 2- onPostExecute: This method is executed after doInBackground finishes its background execution. It receives the result of doInBackground as a parameter (result in this case), which is a boolean value indicating whether the request was sent successfully or not. This method is used to update the user interface with the result.
     *
     * In this method:
     *
     * A toast message is displayed indicating whether the request was sent successfully or if an error occurred. The toast message varies based on the value of result and includes the selected username (selectedUsername).
     */
    private class SendFriendRequestTask extends AsyncTask<String, Void, Boolean> {

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
                Toast.makeText(getApplicationContext(), getString(R.string.request_sent)+" "+ selectedUsername, Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getApplicationContext(), "Error sending friend request to " + selectedUsername, Toast.LENGTH_SHORT).show();
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
            new FriendsAsyncTask().execute(token);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }


}