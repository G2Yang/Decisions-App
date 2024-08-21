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

public class EmailActivity extends Activity {
    //inputActualEmail is to insert the email the user, inputNewEmail is to insert the new email of the user, inputActualPassword is to insert the password of the user
    EditText inputActualEmail, inputNewEmail, inputActualPassword;
    //Button for the navigate in the change email or in the app
    Button btFriends, btHome, btSettings, btAccept, btCancel;
    //Correct format for email
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    //actual email the user, new email the user, actual password the user,
    String token, actualEmail, newEmail, actualPassword;
    //Url for the http post request for the change email in the app
    //String url = "http://143.47.249.102:7070/switchEmail";
    String url = "http://5.75.251.56:7070/switchEmail";

    //Capar que el correo no sea el mismo que introduce nuevamente
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.email_layout);
        //Initialize the elements
        initializeElements();
        //call the method
        readUser();

        btHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(EmailActivity.this, SocialInterface.class));
            }
        });

        btFriends.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(EmailActivity.this, FriendsActivity.class));
            }
        });

        btSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(EmailActivity.this, SettingsActivity.class));
            }
        });

        btAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //call the method for the change email
                changeEmail();
            }
        });

        btCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(EmailActivity.this, SettingsActivity.class));
                finish();
            }
        });
    }

    /*Initialize the elements*/
    private void initializeElements() {
        btHome = findViewById(R.id.btHome);
        btFriends = findViewById(R.id.btFriends);
        btSettings = findViewById(R.id.btSettings);
        btAccept = findViewById(R.id.btConfirm);
        btCancel = findViewById(R.id.btCancel);
        inputActualEmail = findViewById(R.id.etActualEmail);
        inputNewEmail = findViewById(R.id.etNewEmail);
        inputActualPassword = findViewById(R.id.etActualPassword);

    }


    /**
     * The method begins by retrieving the values entered in the actual email, new email, and actual password input fields.
     *
     * It then performs several checks to validate the input:
     * It checks if the actual email matches the email pattern using the matches() method. If it doesn't match, an error is set on the actual email input field, indicating that the actual email is not entered correctly.
     * It checks if the actual email is empty. If it is, an error is set on the actual email input field, indicating that the email is empty.
     * It checks if the new email matches the email pattern. If it doesn't match, an error is set on the new email input field, indicating that the email is not in the correct format.
     * It checks if the actual password is empty. If it is, an error is set on the actual password input field, indicating that the actual password is not entered.
     *
     * If all the input validations pass, the changesEmail() method is called to execute the asynchronous task for changing the email.
     */
    private void changeEmail() {
        actualEmail = inputActualEmail.getText().toString();
        newEmail = inputNewEmail.getText().toString();
        actualPassword = inputActualPassword.getText().toString();
        //Check the email if it contains the elements of an email correctly
        if (!actualEmail.matches(emailPattern)) {
            inputActualEmail.setError(getString(R.string.enter_actual_email));
            //Check the actual email is empty
        } else if (actualEmail.isEmpty()) {
            inputActualEmail.setError(getString(R.string.email_empty));
            //Check the email if it contains the elements of an email correctly
        } else if (!newEmail.matches(emailPattern)) {
            inputNewEmail.setError(getString(R.string.format_email));
            //Check the password is empty
        } else if (actualPassword.isEmpty()) {
            inputActualPassword.setError(getString(R.string.enter_actual_password));
        } else {
            //call the method for execute de asyncTask
            changesEmail(token);

        }
    }

    /* Method to instantiate the EmailChangeTask and start it */
    private void changesEmail(String token) {
        new EmailChangeTask().execute(token);
    }


    /**
     * The EmailChangeTask is an asynchronous task that performs the email change request. Here's an explanation of the code:
     *
     * The doInBackground() method is executed in the background thread and performs the email change request using the provided input values.
     * It creates an instance of OkHttpClient and prepares the request with the necessary headers and request body.
     * The request body includes the new email, current email, and current password.
     * The response from the server is obtained, and if the response is successful, the response body is returned as a string. Otherwise, an error message is returned.
     *
     * The onPostExecute() method is executed on the main UI thread after the background task completes. It receives the result from the doInBackground() method.
     *
     * If the result is "Successful email change," it indicates that the email change was successful.
     * A toast message is displayed to notify the user about the successful email change, and the user is redirected to the SettingsActivity.
     *
     * If the result is an error message, it indicates that the email change was unsuccessful.
     * Error messages are set on the input fields to indicate the specific error, such as entering the actual email or password incorrectly.
     */

    private class EmailChangeTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            token = params[0];
            OkHttpClient client = new OkHttpClient();
            MediaType mediaType = MediaType.parse("application/json");
            RequestBody requestBody = RequestBody.create(mediaType, "newMail=" + newEmail + "&currentEmail=" + actualEmail + "&currentPassword=" + actualPassword);

            Request request = new Request.Builder()
                    .url(url)
                    .post(requestBody)
                    .addHeader("content-type", "application/json")
                    .addHeader("Authorization", token)
                    .build();

            try {
                Response response = client.newCall(request).execute();
                if (response.isSuccessful()) {
                    return response.body().string(); // Devuelve la respuesta del servidor
                } else {
                    return "Error al cambiar el correo electrónico";
                }
            } catch (IOException e) {
                e.printStackTrace();
                return "Error al cambiar el correo electrónico";
            }
        }

        @Override
        protected void onPostExecute(String result) {
            if (result.equals("Successful email change")) {
                Toast.makeText(EmailActivity.this, "Change email " + newEmail, Toast.LENGTH_SHORT).show();
                //go back to activity settings
                startActivity(new Intent(EmailActivity.this, SettingsActivity.class));
            } else {
                inputActualEmail.setError(getString(R.string.enter_actual_email));
                inputActualPassword.setError(getString(R.string.enter_actual_password));
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