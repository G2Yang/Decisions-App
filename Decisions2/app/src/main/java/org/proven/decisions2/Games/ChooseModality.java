package org.proven.decisions2.Games;

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

import org.proven.decisions2.Friends.FriendsActivity;
import org.proven.decisions2.R;
import org.proven.decisions2.Settings.SettingsActivity;
import org.proven.decisions2.SocialInterface;

public class ChooseModality extends Activity {

    Button btHome, btSettings, btFriends, btPlayOnline, btPlayOffline;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.choose_modality);

        // Initialize elements
        initElements();


        // Set click listeners for buttons
        btHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ChooseModality.this, SocialInterface.class));
            }
        });

        btSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ChooseModality.this, SettingsActivity.class));
            }
        });

        btFriends.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ChooseModality.this, FriendsActivity.class));
            }
        });

        btPlayOnline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Check network availability before starting the online play activity
                if (!isNetworkAvailable()){
                    showNoInternetDialog();
                    return;
                }
                startActivity(new Intent(ChooseModality.this, PlayOnlineActivity.class));
            }
        });

        btPlayOffline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Check network availability before starting the offline play activity
                if (!isNetworkAvailable()){
                    showNoInternetDialog();
                    return;
                }
                startActivity(new Intent(ChooseModality.this, PlayOfflineActivity.class));
            }
        });
    }

    //Init elements of layout
    private void initElements() {
        btHome = findViewById(R.id.btHome);
        btSettings = findViewById(R.id.btSettings);
        btFriends = findViewById(R.id.btFriends);
        btPlayOnline = findViewById(R.id.btPlayOnline);
        btPlayOffline = findViewById(R.id.btPlayOffline);
    }

    // Check if network connection is available
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }

    // Show a dialog when no internet connection is available
    private void showNoInternetDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(ChooseModality.this);
        builder.setTitle(R.string.no_internet_connection);
        builder.setMessage(R.string.check_your_connection);

        builder.setPositiveButton(R.string.retry, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                // Retry if internet connection is available, show dialog again otherwise
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
                //If cancel click dialog dismiss
                dialogInterface.dismiss();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
    }
}