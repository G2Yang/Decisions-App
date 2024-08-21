package org.proven.decisions2.LoginAndRegister;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;

import org.proven.decisions2.R;
import org.proven.decisions2.SecureConnection;
import org.proven.decisions2.Settings.EmailSettings.MailSender;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class RecoverPassword extends Activity {

    Button btSendEmail, btLogin, btAccept;

    LinearLayout layoutIntroduceCode, layoutGeneral;

    EditText inputemail, inputRecovery, inputNewPassword, inputConfirmpassword;
    String email, newPassword, recoveryToken, confirmPassword;

    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

    SecureConnection secureConnection = new SecureConnection();

    String url = "http://5.75.251.56:7070/recover-password";
    //String url="http://5.75.251.56:8443/recover-password";
    //String url="http://5.75.251.56:7070/recover-password";

    String url2 = "http://5.75.251.56:7070/reset-password";
    //String url2="http://5.75.251.56:8443/reset-password";
    //String url2="http://5.75.251.56:7070/reset-password";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recover_password_layout);
        /* Initialize the elements */
        initElements();

        btLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RecoverPassword.this, MainActivity.class));
            }
        });

        btSendEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isNetworkAvailable()){
                    showNoInternetDialog();
                    return;
                }
                initRecoverPass();

            }
        });

        btAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isNetworkAvailable()){
                    showNoInternetDialog();
                    return;
                }
                recoveryPasswordWithToken();
            }
        });
    }
    /* Initialize the elements */
    private void initElements() {
        btSendEmail = findViewById(R.id.btSendEmail);
        btLogin = findViewById(R.id.btLogin);
        layoutIntroduceCode = findViewById(R.id.layoutIntroduceCode);
        layoutGeneral = findViewById(R.id.layoutGeneral);
        btAccept = findViewById(R.id.btAccept);
        inputemail = findViewById(R.id.etMail);
        inputRecovery = findViewById(R.id.etTemporalCode);
        inputNewPassword = findViewById(R.id.etNewPassword);
        inputConfirmpassword = findViewById(R.id.etConfirmPassword);
    }


    private void initRecoverPass() {
        email = inputemail.getText().toString();
        // Perform email check
        if (email.isEmpty() || !email.matches(emailPattern)) {
            // The email field is empty or not in the correct format
            inputemail.setError(getString(R.string.format_email));
            return;
        }
        //Set visible layout to change password
        layoutIntroduceCode.setVisibility(View.VISIBLE);
        ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams) layoutGeneral.getLayoutParams();
        layoutParams.setMargins(0, 0, 0, 0);
        layoutGeneral.setLayoutParams(layoutParams);
        layoutGeneral.setGravity(Gravity.CENTER);
        // Call getPassword() method only after successful email check
        new getPassword().execute();

    }

    private void recoveryPasswordWithToken() {
        recoveryToken = inputRecovery.getText().toString();
        newPassword = inputNewPassword.getText().toString();
        confirmPassword = inputConfirmpassword.getText().toString();
        // Check if the recovery token is valid
        if (recoveryToken.isEmpty()) {
            // El token de recuperación no es válido
            inputRecovery.setError(getString(R.string.invalid_code));
            return;
        }

        if (newPassword.isEmpty() || newPassword.length() < 4) {
            // The new password field is empty or does not meet the requirements
            inputNewPassword.setError(getString(R.string.password_correct));
            return;
        }
        if (!newPassword.equals(confirmPassword)) {
            // The new password and the confirmation password do not match
            inputConfirmpassword.setError(getString(R.string.equal_password));
            return;
        }
        // All checks are successful, call the recoveryPassword() method
        new recoveryPassword().execute();
    }


    private class getPassword extends AsyncTask<Void, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(Void... params) {
            OkHttpClient client = secureConnection.getClient();
            //Confirm the username and password the user
            MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
            RequestBody requestBody = RequestBody.create(mediaType, "mail=" + email);

            Request request = new Request.Builder().url(url).post(requestBody).addHeader("content-type", "application/x-www-form-urlencoded").addHeader("cache-control", "no-cache").build();

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
            if (responseData != null){
                String textWithoutQuotes = responseData.replace("\"", "");

                if (responseData.equals("Invalid email")){
                    inputemail.setError(getString(R.string.format_email));
                }
                else if (textWithoutQuotes == "" || textWithoutQuotes.isEmpty()) {
                    // No valid recovery code received
                    // Here you can handle the logic to display an error message or perform other actions if necessary
                } else {
                    // The email is valid and a recovery code was received
                    MailSender sender = new MailSender();
                    sender.setmRecipient(email);
                    sender.setmSubject(getString(R.string.recover_password));
                    sender.setmMessage("<html><body style=\\\"text-align: center;\\\">\n" + "        " +
                            "<h1>"+getString(R.string.restore_password)+"</h1>\n" + "        " +
                            "<p>"+ getString(R.string.your_code_is) + " " + textWithoutQuotes + "</p>\n" + "      " +
                            "  </body></html>");

                    sender.execute();
                }
            }else{
                showServerConnectDialog();
            }

        }
    }
    //Method
    private class recoveryPassword extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... params) {
            OkHttpClient client = new OkHttpClient();

            // Construir los parámetros de la solicitud
            MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
            String requestBody = "recoveryToken=" + recoveryToken + "&newPassword=" + newPassword;

            Request request = new Request.Builder()
                    .url(url2)
                    .post(RequestBody.create(mediaType, requestBody))
                    .addHeader("content-type", "application/x-www-form-urlencoded")
                    .build();

            try {
                Response response = client.newCall(request).execute();
                if (response.isSuccessful()) {
                    // The request was successful, you can get the response body
                    return response.body().string();
                } else {
                    // The request was not successful, handle the error appropriately
                    return null;
                }
            } catch (IOException e) {
                e.printStackTrace();
                // Handle connection or data read/write exception
                return null;
            }
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            if (result != null) {
                // The request was successful, you can do something with the response
                Toast.makeText(RecoverPassword.this, getString(R.string.password_change), Toast.LENGTH_SHORT).show();
                startActivity(new Intent(RecoverPassword.this, MainActivity.class));
            } else {
                showServerConnectDialog();
            }
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
        AlertDialog.Builder builder = new AlertDialog.Builder(RecoverPassword.this);
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
        AlertDialog.Builder builder = new AlertDialog.Builder(RecoverPassword.this);
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