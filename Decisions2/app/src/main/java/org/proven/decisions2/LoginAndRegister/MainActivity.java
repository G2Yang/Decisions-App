package org.proven.decisions2.LoginAndRegister;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.proven.decisions2.R;
import org.proven.decisions2.SecureConnection;
import org.proven.decisions2.Settings.AppCompat;
import org.proven.decisions2.SocialInterface;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MainActivity extends AppCompat {
    //InputUsername is to insert the name of the user, inputPassword is to insert the password of the user
    EditText inputUsername, inputPassword;
    //btLogin is for the login in the app and next Activity SocialInterface , btRegister is for the next Activity Register for the register in the app.
    Button btLogin, btRegister;
    //progressDialog is for the dialog in the login or and register
    ProgressDialog progressDialog;
    //username the user , password the user , token the user for the login in the app
    String username, password, token;
    //The CheckBox is used to allow the user to remember their session
    CheckBox cbRemember;
    //Filename the document name for save the token
    String filename = "token.txt";

    //Url for the http post request for the login in the app
    String url = "https://5.75.251.56:8443/login";

    //Url for the http post request for the getUserToken
    String url2 = "https://5.75.251.56:8443/getUserToken";

    //Create FileOutputStream for the save the document internal
    FileOutputStream outputStream;
    //Method returns an OkHttpClient object that can be used to make HTTP requests, but ignores any SSL certificate issues that might arise when establishing an HTTPS connection.
    SecureConnection secureConnection = new SecureConnection();
    //Textview of recover password
    TextView forgot;

    boolean loginSuccessful = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_main);

        //Initialize the elements
        initializeElements();
        // Initialize the checkbox
        checkboxInitialize();
        //Save user token
        saveUser();

        cbRemember.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                // Check if the network is available
                if (!isNetworkAvailable()) {
                    showNoInternetDialog();
                    return; // Exit the method if there is no internet connection
                }
                //Check the compoundButton is checked
                if (compoundButton.isChecked()) {
                    SharedPreferences preferences = getSharedPreferences("checkbox", MODE_PRIVATE);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString("cbremember", "true");
                    editor.apply();
                    //Check the compoundButton is not checked
                } else if (!compoundButton.isChecked()) {
                    SharedPreferences preferences = getSharedPreferences("checkbox", MODE_PRIVATE);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString("cbremember", "false");
                    editor.apply();
                }
            }
        });

        // Set an OnClickListener for the btLogin button
        btLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Check if the network is available
                if (!isNetworkAvailable()) {
                    showNoInternetDialog();
                    SharedPreferences preferences = getSharedPreferences("checkbox", MODE_PRIVATE);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString("cbremember", "false");
                    editor.apply();
                    return; // Exit the method if there is no internet connection
                }

                // Call the method for login
                perforLogin();


            }
        });

        // OnClickListener for btRegister button
        btRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Checking if network is available
                if (!isNetworkAvailable()) {
                    // Showing a dialog for no internet connection
                    showNoInternetDialog();
                    return;
                }
                // Starting Register activity
                startActivity(new Intent(MainActivity.this, Register.class));
                // Applying slide animations
                overridePendingTransition(R.anim.slide_right, R.anim.slide_out_left);
            }
        });

        // OnClickListener for forgot button
        forgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Checking if network is available
                if (!isNetworkAvailable()) {
                    // Showing a dialog for no internet connection
                    showNoInternetDialog();
                    SharedPreferences preferences = getSharedPreferences("checkbox", MODE_PRIVATE);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString("cbremember", "false");
                    editor.apply();
                    return;
                }
                // Starting RecoverPassword activity
                startActivity(new Intent(MainActivity.this, RecoverPassword.class));
            }
        });
    }


    //Method to initialize the checkbox to keep me logged in
    private void checkboxInitialize() {
        SharedPreferences preferences = getSharedPreferences("checkbox", MODE_PRIVATE);
        String checkbox = preferences.getString("cbremember", "");
        //Check the comboBox is in true
        if (checkbox.equals("true")) {
            startActivity(new Intent(MainActivity.this, SocialInterface.class));
            finish();
        }
    }

    //Method to initialize the elements
    private void initializeElements() {
        btLogin = findViewById(R.id.btLogin);
        btRegister = findViewById(R.id.btRegister);
        inputUsername = findViewById(R.id.etUsername2);
        inputPassword = findViewById(R.id.etPassword);
        cbRemember = findViewById(R.id.cbRemember);
        progressDialog = new ProgressDialog(this);
        forgot = findViewById(R.id.forgot);
    }

    //Method to login by asking the user for the username and password
    private void perforLogin() {
        username = inputUsername.getText().toString();
        password = inputPassword.getText().toString();
        //Check the username is not equals
        if (!username.matches(username)) {
            inputUsername.setError(getString(R.string.correct_username));
            //Check the username is empty
        } else if (username.isEmpty()) {
            inputUsername.setError(getString(R.string.username_empty));
            //Check that the password is empty or the length is correct
        } else if (password.isEmpty()) {
            inputPassword.setError(getString(R.string.password_empty));
        }else if(password.length() < 4){
            inputPassword.setError(getString(R.string.password_correct));
        }
        else {
            //Call the method for execute de asyncTask
            http();
        }
    }

    // Method to go to the next activity
    private void sendUserToNextActivity() {
        Intent intent = new Intent(MainActivity.this, SocialInterface.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    // Method to instantiate the asyncTask of the HttpTask and getToken and execute it
    private void http() {
        // Execute the HttpTask
        new HttpTask().execute();
        // Execute the getToken
        new getToken().execute();
    }


    //Method to execute the post requests for the login
    private class HttpTask extends AsyncTask<Void, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //Dialog for the correct login
            progressDialog.setMessage(getString(R.string.wait_login));
            progressDialog.setTitle(R.string.login);
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();
        }

        @Override
        protected String doInBackground(Void... params) {
            OkHttpClient client = secureConnection.getClient();
            //Confirm the username and password the user
            MediaType mediaType = MediaType.parse("application/json");
            RequestBody requestBody = RequestBody.create(mediaType, "username=" + username + "&password=" + password);

            Request request = new Request.Builder()
                    .url(url)
                    .post(requestBody)
                    .addHeader("content-type", "application/json")
                    .addHeader("cache-control", "no-cache")
                    .addHeader("Authorization", "Bearer " + token)
                    .build();

            try {
                Response response = client.newCall(request).execute();
                return response.body().string();
            } catch (IOException e) {
                e.printStackTrace();
                return "error_connection";
            }
        }

        //check that the login is correct and check if the credentials are correct or incorrect
        @Override
        protected void onPostExecute(String responseData) {
            loginSuccessful = false;
            String textWithoutQuotes = responseData.replace("\"", "");
            if (responseData != null) {
                if (textWithoutQuotes.equals("Credenciales o usuario incorrecto!!!")) {
                    inputUsername.setError("User not exists");
                    inputPassword.setError(getString(R.string.password_correct));
                    loginSuccessful = false;
                } else if (textWithoutQuotes.equals("error_connection")) {
                    showServerConnectDialog();
                    SharedPreferences preferences = getSharedPreferences("checkbox", MODE_PRIVATE);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString("cbremember", "false");
                    editor.apply();
                    loginSuccessful = false;
                } else {
                    token = textWithoutQuotes;
                    System.out.println(token);
                    loginSuccessful = true;
                }

                Log.d("TAG", "Response data: " + textWithoutQuotes);
                //Parse the response data to check if login was successful
                if (loginSuccessful == true) {
                    progressDialog.dismiss();
                    // redirects the user to the next activity
                    sendUserToNextActivity();
                } else {
                    progressDialog.dismiss();
                }
            } else {
                progressDialog.dismiss();
            }
        }
    }

    //Method to execute login POST requests and get token
    private class getToken extends AsyncTask<Void, Void, String> {
        @Override
        protected String doInBackground(Void... params) {
            // Creating an instance of OkHttpClient
            OkHttpClient client = secureConnection.getClient();

            // Creating a media type for the request body
            MediaType mediaType = MediaType.parse("application/json");

            // Creating the request body with username and password parameters
            RequestBody requestBody = RequestBody.create(mediaType, "username=" + username + "&password=" + password);

            // Creating the request
            Request request = new Request.Builder()
                    .url(url2)
                    .post(requestBody)
                    .addHeader("content-type", "application/json")
                    .addHeader("cache-control", "no-cache")
                    .addHeader("Authorization", "Bearer " + token)
                    .build();

            try {
                // Executing the request and getting the response
                Response response = client.newCall(request).execute();

                // Returning the response body as a string
                return response.body().string();
            } catch (IOException e) {
                e.printStackTrace();

                // Returning null in case of an exception
                return null;
            }
        }

        //Verifies that the login is correct and checks if the credentials are correct or incorrect
        @Override
        protected void onPostExecute(String responseData) {
            // Check if the response data is not null
            if (responseData != null) {
                // Remove quotes from the response data
                String textWithoutQuotes = responseData.replace("\"", "");

                // Check if the response data indicates incorrect credentials or user
                if (textWithoutQuotes.equals("Credenciales o usuario incorrecto!!!")) {
                    loginSuccessful = false;
                } else {
                    // Set the token to the response data
                    token = textWithoutQuotes;
                    System.out.println(token);
                    // Save the user
                    saveUser();
                    loginSuccessful = true;
                }

                // Log the response data
                Log.d("TAG", "Response data: " + textWithoutQuotes);
            }
        }
    }



    //Method to save the token that logs in to be able to use it in other activities
    private void saveUser() {
        try {
            if (token !=null){
                outputStream = openFileOutput(filename, Context.MODE_PRIVATE);
                outputStream.write(token.getBytes());
                outputStream.close();
            }else{
                // handle the null case
            }
        } catch (
                Exception e) {
            e.printStackTrace();
        }

    }



    //Return if is network available
    private boolean isNetworkAvailable() {
        // Get the ConnectivityManager system service
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        // Get the active network info
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        // Check if network info is not null and network is connected
        return networkInfo != null && networkInfo.isConnected();
    }

    //Show a dialog for internet connection error
    private void showNoInternetDialog() {
        // Create an AlertDialog builder with the MainActivity context
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        // Set the title of the dialog
        builder.setTitle(R.string.no_internet_connection);
        // Set the message of the dialog
        builder.setMessage(R.string.check_your_connection);

        // Set the positive button for retrying
        builder.setPositiveButton(R.string.retry, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                // Check if network is available
                if (isNetworkAvailable()) {
                    dialogInterface.dismiss();
                } else {
                    showNoInternetDialog();
                }
            }
        });

        // Set the negative button for canceling
        builder.setNegativeButton(R.string.btCancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });

        // Create and show the dialog
        AlertDialog dialog = builder.create();
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false); // Prevent the dialog from being closed by touching outside of it
        dialog.show();
    }

    //Show a dialog for server connection error
    private void showServerConnectDialog() {
        // Create an AlertDialog builder with the MainActivity context
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        // Set the title of the dialog
        builder.setTitle(R.string.error_connect_server);
        // Set the message of the dialog
        builder.setMessage(R.string.problem_with_server);

        // Set the positive button for retrying
        builder.setPositiveButton(R.string.retry, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                // Check if network is available
                if (isNetworkAvailable()) {
                    dialogInterface.dismiss();
                    perforLogin();
                }
            }
        });

        // Set the negative button for closing the app
        builder.setNegativeButton(R.string.close_app, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                finishAndRemoveTask();
            }
        });

        // Create and show the dialog
        AlertDialog dialog = builder.create();
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false); // Prevent the dialog from being closed by touching outside of it
        dialog.show();
    }

}