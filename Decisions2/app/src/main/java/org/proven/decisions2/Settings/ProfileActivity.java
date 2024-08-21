package org.proven.decisions2.Settings;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.proven.decisions2.Friends.FriendsActivity;
import org.proven.decisions2.R;
import org.proven.decisions2.SocialInterface;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ProfileActivity extends Activity {
    //Button for the navigate in the change profile or in the app
    Button btFriends, btHome, btSettings, btAccept, btCancel;
    //inputUsername is to insert the username the user,
    EditText inputUsername;
    //Url for the http post request for the change username in the app
//    String url = "http://143.47.249.102:7070/switchUsername";
    String url="http://5.75.251.56:7070/switchUsername";
    //new username for the user and token the user for the login in the app
    String token, newUsername;

    /**
     * The onCreate method in the ProfileActivity class is responsible for initializing the activity and setting up the event listeners for various buttons. Here's an explanation of the code:
     *
     * The method starts by calling the super.onCreate(savedInstanceState) and setContentView(R.layout.profile_layout) to set up the activity.
     *
     * The initializeElements() method is called to initialize the elements of the activity.
     *
     * The readUser() method is called to retrieve the user information.
     *
     * Event listeners are set up for the buttons in the activity (btHome, btFriends, btSettings, btAccept, btCancel).
     *
     * When the btHome button is clicked, it starts the SocialInterface activity.
     *
     * When the btFriends button is clicked, it starts the FriendsActivity.
     *
     * When the btSettings button is clicked, it starts the SettingsActivity.
     *
     * When the btAccept button is clicked, it calls the changeUsername() method.
     *
     * When the btCancel button is clicked, it starts the SettingsActivity and finishes the ProfileActivity.
     *
     * @param savedInstanceState If the activity is being re-initialized after
     *     previously being shut down then this Bundle contains the data it most
     *     recently supplied in {@link #onSaveInstanceState}.  <b><i>Note: Otherwise it is null.</i></b>
     *
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_layout);
        /*Initialize the elements*/
        initializeElements();
        //call the method
        readUser();


        btHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ProfileActivity.this, SocialInterface.class));
            }
        });

        btFriends.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ProfileActivity.this, FriendsActivity.class));
            }
        });

        btSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(ProfileActivity.this, SettingsActivity.class));
            }
        });
        btAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeUsername();
            }
        });
        btCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ProfileActivity.this, SettingsActivity.class));
                finish();
            }
        });
    }

    /*Initialize the elements*/
    private void initializeElements() {
        inputUsername = findViewById(R.id.etUsername3);
        btHome = findViewById(R.id.btHome);
        btFriends = findViewById(R.id.btFriends);
        btSettings = findViewById(R.id.btSettings);
        btAccept = findViewById(R.id.btConfirm);
        btCancel = findViewById(R.id.btCancel);

    }

    /* Method to instantiate the UsernameChangeTask and start it */
    private void getFriends(String token) {
        new UsernameChangeTask().execute(token);
    }

    /**
     *The changeUsername() method is responsible for handling the logic when the user wants to change their username. Here's an explanation of the code:
     *
     * The method starts by retrieving the new username entered by the user from the inputUsername field.
     *
     * It checks if the new username is empty. If it is, it sets an error message using inputUsername.setError() to indicate that the username cannot be empty.
     *
     * If the new username is not empty, it proceeds to call the getFriends(token) method.
     * It assumes that the getFriends(token) method is responsible for updating the username and performing any necessary tasks asynchronously.
     */
    private void changeUsername() {
        newUsername = inputUsername.getText().toString();
        //check the new username is empty
        if (newUsername.isEmpty()) {
            inputUsername.setError(getString(R.string.username_empty));
        } else {
            //call the method for execute de asyncTask
            getFriends(token);


        }

    }

    /**
     * The UsernameChangeTask class extends AsyncTask and is responsible for executing the username change request asynchronously. Here's an explanation of the code:
     *
     * The doInBackground() method is executed on a background thread. It performs the HTTP request to change the username.
     * It uses the provided token for authorization and sends a POST request with the new username in the request body. It handles the response and returns the result as a String.
     *
     * The onPostExecute() method is executed on the UI thread after the doInBackground() method completes.
     * It receives the result of the username change operation as a parameter. If the result indicates a successful username change,
     * it displays a toast message with the new username and starts the SettingsActivity.
     * Otherwise, it sets an error message on the inputUsername field indicating that the username already exists or is invalid.
     */
    private class UsernameChangeTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            token = params[0];
            OkHttpClient client = new OkHttpClient();
            // Change username
            MediaType mediaType = MediaType.parse("application/json");
            RequestBody requestBody = RequestBody.create(mediaType, "newValue=" + newUsername + "&paramether=username");
            Request request = new Request.Builder()
                    .url(url)
                    .post(requestBody)
                    .addHeader("content-type", "application/json")
                    .addHeader("Authorization", token)
                    .build();

            // Send HTTP POST
            try {
                Response response = client.newCall(request).execute();
                if (response.isSuccessful()) {
                    // Check if the response indicates successful username change
                    String responseBody = response.body().string();
                    return responseBody;
                } else {
                    return "Error: Please enter a valid username";
                }
            } catch (IOException e) {
                e.printStackTrace();
                return "Error: Failed to connect to the server";
            }
        }

        @Override
        protected void onPostExecute(String result) {
            // Operaciones de la interfaz de usuario aquÃ­
            if (result.equals("Username change successful")) {
                Toast.makeText(ProfileActivity.this, "Change username " + newUsername, Toast.LENGTH_SHORT).show();
                //go back to activity settings
                startActivity(new Intent(ProfileActivity.this, SettingsActivity.class));
            } else {
                inputUsername.setError(getString(R.string.username_exists));
            }
        }
    }

    /**
     * The readUser() method reads the user's authentication token from a file named "token.txt" stored in the app's private file directory. Here's an explanation of the code:
     *
     * The method creates a File object filename representing the file "token.txt" in the app's private file directory using the getFilesDir() method.
     *
     * Inside a try-catch block, it creates a FileReader object reader to read the contents of the file.
     *
     * It wraps the FileReader in a BufferedReader object bufferedReader for efficient reading.
     *
     * It reads the token from the file by calling readLine() on the bufferedReader object and assigns it to the token variable.
     *
     * After reading the token, it closes the bufferedReader and reader using the close() method.
     *
     * If an IOException occurs during the file reading process, it is caught in the catch block, and a RuntimeException is thrown.
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