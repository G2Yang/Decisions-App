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
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.proven.decisions2.R;
import org.proven.decisions2.SecureConnection;
import org.proven.decisions2.Settings.EmailSettings.MailSender;

import java.io.IOException;
import java.security.cert.CertificateException;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Register extends AppCompatActivity {
    //inputEmail is to insert the email the user, inputUsername is to insert the name of the user, inputPassword is to insert the password of the user
    EditText inputEmail, inputusername, inputPassword, inputConfirmPasword;
    //Button for the confirm the register
    Button btnRegister, btLogin;
    //Correct format for email
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    //progressDialog is for the dialog in the register
    ProgressDialog progressDialog;
    //email the user, username the user, password the user
    String email, username, password;
    //Url for the http post request for the register in the app
    //String url = "http://143.47.249.102:7070/register";
    String url = "https://5.75.251.56:8443/register";
    //String url = "http://5.75.251.56:7070/register";
    //Method returns an OkHttpClient object that can be used to make HTTP requests, but ignores any SSL certificate issues that might arise when establishing an HTTPS connection.
    SecureConnection secureConnection = new SecureConnection();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        //Initialize the elements
        initializeElements();

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isNetworkAvailable()){
                    showNoInternetDialog();
                    return;
                }
                //call the method for the register
                PerforAuth();
                //Change the checkbox in false
                SharedPreferences preferences = getSharedPreferences("checkbox", MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("cbremember", "false");
                editor.apply();

            }
        });

        btLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isNetworkAvailable()){
                    showNoInternetDialog();
                    return;
                }
                startActivity(new Intent(Register.this, MainActivity.class));
                overridePendingTransition(R.anim.slide_left, R.anim.slide_out_right);
                //Change the checkbox in false
                SharedPreferences preferences = getSharedPreferences("checkbox", MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("cbremember", "false");
                editor.apply();

            }
        });
    }

    /*Initialize the elements*/
    private void initializeElements() {
        inputEmail = findViewById(R.id.etMail);
        inputusername = findViewById(R.id.etUsername);
        inputPassword = findViewById(R.id.etPassword);
        inputConfirmPasword = findViewById(R.id.etConfirmPassword);
        btnRegister = findViewById(R.id.btAccept);
        btLogin = findViewById(R.id.btLogin);
        progressDialog = new ProgressDialog(this);
    }

    /*Method to register by asking the user for the email, username and password*/
    private void PerforAuth() {
        email = inputEmail.getText().toString();
        username = inputusername.getText().toString();
        password = inputPassword.getText().toString();
        String confirmPassword = inputConfirmPasword.getText().toString();
        //Check the email if it contains the elements of an email correctly
        if (!email.matches(emailPattern) || email.isEmpty()) {
            inputEmail.setError(getString(R.string.format_email));
            //Check the username is empty
        } else if (username.isEmpty()) {
            inputusername.setError(getString(R.string.username_empty));
            //Check that the password is empty or the length is correct
        } else if (password.isEmpty() || password.length() < 4) {
            inputPassword.setError(getString(R.string.password_correct));
            //Check that the password is equals
        } else if (!password.equals(confirmPassword)) {
            inputConfirmPasword.setError(getString(R.string.equal_password));
        } else {
            //Dialog for the correct register
            progressDialog.setMessage(getString(R.string.please_wait_register));
            progressDialog.setTitle(R.string.register);
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();
            //call the method for execute de asyncTask
            http();

        }

    }

    /*Method to instantiate the asyncTask of the HttpTask and execute it*/
    private void http() {
        new HttpTask().execute();
        //Call the MailSender class to be able to send an email
        MailSender sender = new MailSender(email, getString(R.string.successful_registration), getString(R.string.welcome_decisions));
        sender.execute();
    }

    /*Method to execute the post requests for the register*/
    private class HttpTask extends AsyncTask<Void, Void, String> {
        @Override
        protected String doInBackground(Void... params) {
            OkHttpClient client = secureConnection.getClient();
            MediaType mediaType = MediaType.parse("application/json");
            RequestBody requestBody = RequestBody.create(mediaType, "mail=" + email + "&username=" + username + "&password=" + password);

            Request request = new Request.Builder()
                    .url(url).post(requestBody).addHeader("content-type", "application/json").addHeader("cache-control", "no-cache").build();
            try {
                Response response = client.newCall(request).execute();
                return response.body().string();
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(String responseData) {
            if (responseData != null) {
                // The response is not null, continue processing the response
                boolean registerSuccessful = true;
                if (responseData.equalsIgnoreCase("user exists")) {
                    inputusername.setError(getString(R.string.username_exists));
                    progressDialog.dismiss();
                    registerSuccessful = false;
                } else if (responseData.equalsIgnoreCase("mail aready used")) {
                    inputEmail.setError("email already used");
                    progressDialog.dismiss();
                    registerSuccessful = false;
                }
                if (registerSuccessful) {
                    progressDialog.dismiss();
                    sendUserToNextActivity();
                    Toast.makeText(Register.this, getString(R.string.register_succesful), Toast.LENGTH_SHORT).show();
                }
            } else {
                // The response is null, show the server connection dialog
                showServerConnectDialog();
            }
        }
    }


    /*Method to go to the next activity*/
    private void sendUserToNextActivity() {
        Intent intent = new Intent(Register.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_left, R.anim.slide_out_right);
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
        AlertDialog.Builder builder = new AlertDialog.Builder(Register.this);
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
        AlertDialog.Builder builder = new AlertDialog.Builder(Register.this);
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
                    PerforAuth();
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