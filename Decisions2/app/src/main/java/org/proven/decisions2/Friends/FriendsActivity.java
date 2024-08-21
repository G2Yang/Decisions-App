package org.proven.decisions2.Friends;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import org.proven.decisions2.R;
import org.proven.decisions2.Settings.SettingsActivity;
import org.proven.decisions2.SocialInterface;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class FriendsActivity extends Activity {
    //The buttons of the menu friends to navigate
    Button btHome, btSettings, btAdd, btRemove, btRequests;
    //User authentication token
    String token;


    /**
     * The code provided shows the onCreate method of the FriendsActivity class. This method is called when the activity is created and is responsible for initializing the activity, setting up event listeners, and performing other necessary tasks.
     *
     * Here's what the code does:
     *
     * It calls the superclass onCreate method to perform any necessary setup.
     * It sets the content view of the activity to the layout defined in R.layout.activity_friends.
     * It initializes the elements of the activity by calling the initializeElements() method.
     * It calls the readUser() method.
     * It sets an OnClickListener for the btHome button. When clicked, it starts a new SocialInterface activity and calls the readUser() method.
     * It sets an OnClickListener for the btSettings button. When clicked, it starts a new SettingsActivity and calls the readUser() method.
     *
     * It sets an OnClickListener for the btAdd button. When clicked, it checks if there is an available network connection. If there is,
     * it starts a new AddFriendsActivity and calls the readUser() method. Otherwise, it shows a dialog indicating no internet connection.
     *
     * It sets an OnClickListener for the btRemove button. When clicked, it checks if there is an available network connection. If there is,
     * it starts a new RemoveFriendsActivity and calls the readUser() method. Otherwise, it shows a dialog indicating no internet connection.
     *
     * It sets an OnClickListener for the btRequests button. When clicked, it checks if there is an available network connection. If there is,
     * it starts a new RequestsFriendsActivity and calls the readUser() method. Otherwise, it shows a dialog indicating no internet connection.
     *
     * @param savedInstanceState If the activity is being re-initialized after
     *     previously being shut down then this Bundle contains the data it most
     *     recently supplied in {@link #onSaveInstanceState}.  <b><i>Note: Otherwise it is null.</i></b>
     *
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends);
        //Initialize the elements
        initializeElements();
        //Call the method
        readUser();


        btHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(FriendsActivity.this, SocialInterface.class));
                readUser();
            }
        });

        btSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(FriendsActivity.this, SettingsActivity.class));
                readUser();
            }
        });

        btAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isNetworkAvailable()){
                    showNoInternetDialog();
                    return;
                }
                startActivity(new Intent(FriendsActivity.this, AddFriendsActivity.class));
                readUser();
            }
        });

        btRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isNetworkAvailable()){
                    showNoInternetDialog();
                    return;
                }
                startActivity(new Intent(FriendsActivity.this, RemoveFriendsActivity.class));
                readUser();
            }
        });

        btRequests.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isNetworkAvailable()){
                    showNoInternetDialog();
                    return;
                }
                startActivity(new Intent(FriendsActivity.this, RequestsFriendsActivity.class));
                readUser();
            }
        });
    }

    /*Initialize the elements*/
    private void initializeElements() {
        btHome = findViewById(R.id.btHome);
        btSettings = findViewById(R.id.btSettings);
        btAdd = findViewById(R.id.btAdd);
        btRemove = findViewById(R.id.btRemove);
        btRequests = findViewById(R.id.btRequests);

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


    /**
     * The code provided shows a private method called isNetworkAvailable() in the current class. This method is used to check the availability of a network connection.
     *
     * Here's how the code works:
     *
     * It obtains a reference to the ConnectivityManager by calling getSystemService(Context.CONNECTIVITY_SERVICE).
     * The ConnectivityManager is a system service that provides information about the device's network connections.
     *
     * It retrieves the active network information by calling connectivityManager.getActiveNetworkInfo().
     * This returns a NetworkInfo object representing the current network connection.
     *
     * It checks if the networkInfo object is not null and if the network is connected by calling networkInfo.isConnected().
     *
     * If both conditions are true (there is a network connection and it is connected), it returns true. Otherwise, it returns false.
     * @return
     */
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }

    /**
     * The code provided shows a private method called showNoInternetDialog() in the current class.
     * This method is used to display a dialog to the user when there is no internet connection available.
     *
     * Here's what the code does:
     *
     * It creates an AlertDialog.Builder object, passing the current activity (FriendsActivity.this) as the context.
     * It sets the title of the dialog to the string resource specified by R.string.no_internet_connection.
     * It sets the message of the dialog to the string resource specified by R.string.check_your_connection.
     * It sets a positive button with the text specified by R.string.retry and an OnClickListener to handle the button click.
     * When clicked, it checks if there is a network connection available by calling isNetworkAvailable().
     * If there is a network connection, it dismisses the dialog by calling dialogInterface.dismiss().
     * If there is no network connection, it recursively calls showNoInternetDialog() to show the dialog again.
     * It sets a negative button with the text specified by R.string.btCancel and an OnClickListener to handle the button click. When clicked, it dismisses the dialog by calling dialogInterface.dismiss().
     * It creates the AlertDialog by calling builder.create().
     * It sets the dialog to be non-cancelable by calling dialog.setCancelable(false). This prevents the dialog from being dismissed when the user presses the back button.
     * It sets the dialog to be not canceled on touch outside by calling dialog.setCanceledOnTouchOutside(false). This prevents the dialog from being dismissed when the user touches outside the dialog area.
     * It displays the dialog by calling dialog.show().
     */
    private void showNoInternetDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(FriendsActivity.this);
        builder.setTitle(R.string.no_internet_connection);
        builder.setMessage(R.string.check_your_connection);

        builder.setPositiveButton(R.string.retry, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (isNetworkAvailable()) {
                    dialogInterface.dismiss();
                } else {
                    showNoInternetDialog();
                }
            }
        });

        builder.setNegativeButton(R.string.btCancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false); // Evita que el diálogo se cierre al tocar fuera de él
        dialog.show();
    }

}