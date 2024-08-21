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

public class PasswordActivity extends Activity {
    EditText inputActualPassword, inputNewPassword, inputConfirmNewPassword;
    Button btFriends, btHome, btSettings, btAccept, btCancel;
    //    String url = "http://143.47.249.102:7070/switchPassword";
    String url = "http://5.75.251.56:7070/switchPassword";
    String token, actualPassword, newPassword, confirmPassword;

    /**
     * The onCreate method in the PasswordActivity class is responsible for setting up the activity when it is created. Here's an explanation of the method:
     *
     * It calls the super.onCreate(savedInstanceState) method to perform the default activity creation tasks.
     *
     * It sets the content view of the activity to the layout file password_layout using setContentView(R.layout.password_layout).
     *
     * It initializes the elements of the activity using the initializeElements() method.
     *
     * It calls the readUser() method to read the user's information.
     *
     * It sets click listeners for the buttons btHome, btFriends, btSettings, btAccept, and btCancel to handle user interactions.
     *
     * When the btHome button is clicked, it starts a new SocialInterface activity.
     *
     * When the btFriends button is clicked, it starts a new FriendsActivity activity.
     *
     * When the btSettings button is clicked, it starts a new SettingsActivity activity.
     *
     * When the btAccept button is clicked, it calls the changePassword() method to initiate the password change process.
     *
     * When the btCancel button is clicked, it starts a new SettingsActivity activity and finishes the current activity.
     *
     * @param savedInstanceState If the activity is being re-initialized after
     *     previously being shut down then this Bundle contains the data it most
     *     recently supplied in {@link #onSaveInstanceState}.  <b><i>Note: Otherwise it is null.</i></b>
     *
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.password_layout);
        /*Initialize the elements*/
        initializeElements();
        //call the method
        readUser();


        btHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(PasswordActivity.this, SocialInterface.class));
            }
        });

        btFriends.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(PasswordActivity.this, FriendsActivity.class));
            }
        });

        btSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(PasswordActivity.this, SettingsActivity.class));
            }
        });

        btAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //call the method for the change password
                changePassword();
            }
        });
        btCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(PasswordActivity.this, SettingsActivity.class));
                finish();
            }
        });
    }

    /*Initialize the elements*/
    private void initializeElements() {
        btHome = findViewById(R.id.btHome);
        btFriends = findViewById(R.id.btFriends);
        btSettings = findViewById(R.id.btSettings);
        inputActualPassword = findViewById(R.id.etActualPassword);
        inputNewPassword = findViewById(R.id.etNewPassword);
        inputConfirmNewPassword = findViewById(R.id.etNewConfPassword);
        btAccept = findViewById(R.id.btConfirm);
        btCancel = findViewById(R.id.btCancel);

    }

    /* Method to instantiate the  PasswordChangeTask and start it */
    private void changesPassword(String token) {
        new PasswordChangeTask().execute(token);
    }

    /**
     * The changePassword() method is responsible for validating the entered passwords and initiating the password change process. Here's an explanation of the method:
     *
     * It retrieves the values entered in the inputActualPassword, inputNewPassword, and inputConfirmNewPassword fields.
     *
     * It performs several checks to validate the passwords:
     * It checks if the actual password matches the expected actual password. If not, it sets an error message on the inputActualPassword field indicating to enter the correct actual password.
     * It checks if the actual password is empty. If so, it sets an error message on the inputActualPassword field indicating that the password cannot be empty.
     * It checks if the new password is empty or its length is less than 4 characters. If so, it sets an error message on the inputNewPassword field indicating that the password should have a minimum length of 4 characters.
     * It checks if the new password and the confirmed password are equal. If not, it sets an error message on the inputConfirmNewPassword field indicating that the passwords should match.
     * If all the password checks pass, it calls the changesPassword(token) method to initiate the password change process.
     */
    private void changePassword() {
        actualPassword = inputActualPassword.getText().toString();
        newPassword = inputNewPassword.getText().toString();
        confirmPassword = inputConfirmNewPassword.getText().toString();
        //Check that the actual password is equal
        if (!actualPassword.matches(actualPassword)) {
            inputActualPassword.setError(getString(R.string.enter_actual_password));
            //Check the actual password is empty
        } else if (actualPassword.isEmpty()) {
            inputActualPassword.setError(getString(R.string.password_empty));
            //Check that the new password is empty or the length is correct
        } else if (newPassword.isEmpty() || newPassword.length() < 4) {
            inputNewPassword.setError(getString(R.string.password_correct));
            //Check that the password is equals
        } else if (!newPassword.equals(confirmPassword)) {
            inputConfirmNewPassword.setError(getString(R.string.equal_password));
        } else {
            //call the method for execute de asyncTask
            changesPassword(token);

        }

    }

    /**
     * The PasswordChangeTask class extends AsyncTask and is responsible for performing the password change request in the background. Here's an explanation of the class:
     *
     * The doInBackground method is executed in the background thread. It retrieves the token and other necessary values from the params array.
     *
     * It creates an instance of OkHttpClient to handle the HTTP request.
     *
     * It creates the request body with the new password, parameter, and current password.
     *
     * It builds the request with the necessary headers and executes the HTTP POST request.
     *
     * It checks the response to determine if the password change was successful.
     *
     * If the response is successful, it returns the response body as a string.
     *
     * In the onPostExecute method, the result is received as a parameter.
     *
     * It checks if the result is not null.
     *
     * If the result is not null, it checks if the result equals "Password change successful".
     * If so, it displays a toast message indicating the successful password change and starts the SettingsActivity.
     *
     * If the result is not "Password change successful", it displays a toast message indicating the error and sets an error message on the inputActualPassword field.
     *
     * If the result is null, it displays a toast message indicating an error occurred.
     */
    private class PasswordChangeTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            token = params[0];

            System.out.println("Nueva contraseÃ±a: " + newPassword);
            OkHttpClient client = new OkHttpClient();
            MediaType mediaType = MediaType.parse("application/json");
            RequestBody requestBody = RequestBody.create(mediaType, "newValue=" + newPassword + "&paramether=password" + "&currentPassword=" + actualPassword);

            Request request = new Request.Builder()
                    .url(url)
                    .post(requestBody)
                    .addHeader("content-type", "application/json")
                    .addHeader("Authorization", token)
                    .build();

            // Send HTTP POST friend request
            try {
                Response response = client.newCall(request).execute();
                if (response.isSuccessful()) {
                    return response.body().string();
                } else {
                    return null;
                }
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(String result) {
            if (result != null) {
                if (result.equals("Password change successful")) {
                    Toast.makeText(getApplicationContext(), result, Toast.LENGTH_SHORT).show();
                    // go to next activity SettingsActivity
                    startActivity(new Intent(PasswordActivity.this, SettingsActivity.class));
                } else {
                    Toast.makeText(getApplicationContext(), result, Toast.LENGTH_SHORT).show();
                    inputActualPassword.setError(getString(R.string.enter_actual_password));
                }
            } else {
                Toast.makeText(getApplicationContext(), "Error occurred", Toast.LENGTH_SHORT).show();
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