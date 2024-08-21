package org.proven.decisions2.Settings;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import org.proven.decisions2.Friends.FriendsActivity;
import org.proven.decisions2.LoginAndRegister.MainActivity;
import org.proven.decisions2.R;
import org.proven.decisions2.SocialInterface;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class SettingsActivity extends Activity {
    //The buttons to navigate in the app and in the settings menu
    Button btFriends, btHome, btProfile, btPassword, btCreators, btLanguage, btGuide, btlogout, btEmail, btLicense;
    //User authentication token
    String token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        //Initialize the elements
        initializeElements();
        //call the method
        readUser();

        btHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SettingsActivity.this, SocialInterface.class));
                readUser();
                finish();
            }
        });

        btFriends.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SettingsActivity.this, FriendsActivity.class));
                finish();
            }
        });

        btProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                readUser();
                startActivity(new Intent(SettingsActivity.this, ProfileActivity.class));
                finish();
            }
        });

        btPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                readUser();
                startActivity(new Intent(SettingsActivity.this, PasswordActivity.class));
                finish();
            }
        });

        btCreators.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                readUser();
                startActivity(new Intent(SettingsActivity.this, CreatorsActivity.class));
            }
        });

        btEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SettingsActivity.this, EmailActivity.class));
            }
        });

        btLanguage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                readUser();
                startActivity(new Intent(SettingsActivity.this, LanguageActivity.class));
                finish();
            }
        });
        btGuide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SettingsActivity.this, GuideActivity.class));
            }
        });
        btlogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearNamePref();


                startActivity(new Intent(SettingsActivity.this, MainActivity.class));
                //change the checkbox in false for the logout the app
                SharedPreferences preferences = getSharedPreferences("checkbox", MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("cbremember", "false");
                editor.apply();
                finishAffinity();
            }
        });
        btLicense.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SettingsActivity.this, LicenseActivity.class));
            }
        });


    }

    /*Initialize the elements*/
    private void initializeElements() {
        btHome = findViewById(R.id.btHome);
        btFriends = findViewById(R.id.btFriends);
        btProfile = findViewById(R.id.btProfile);
        btPassword = findViewById(R.id.btPassword);
        btCreators = findViewById(R.id.btCreators);
        btLanguage = findViewById(R.id.btLanguage);
        btlogout = findViewById(R.id.btLogout);
        btGuide = findViewById(R.id.btExplication);
        btEmail = findViewById(R.id.btEmail);
        btLicense = findViewById(R.id.btLicense);

    }


    /**
     * The readUser() method reads the user's authentication token from a file named "token.txt" stored in the app's private file directory. Here's an explanation of the code:
     * <p>
     * The method creates a File object filename representing the file "token.txt" in the app's private file directory using the getFilesDir() method.
     * <p>
     * Inside a try-catch block, it creates a FileReader object reader to read the contents of the file.
     * <p>
     * It wraps the FileReader in a BufferedReader object bufferedReader for efficient reading.
     * <p>
     * It reads the token from the file by calling readLine() on the bufferedReader object and assigns it to the token variable.
     * <p>
     * After reading the token, it closes the bufferedReader and reader using the close() method.
     * <p>
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

    /**
     * The clearNamePref() method is used to clear the stored player name from the shared preferences. Here's an explanation of the code:
     * <p>
     * It retrieves the shared preferences object using the getSharedPreferences() method, passing the preferences file name and the mode 0 (private mode).
     * <p>
     * It obtains an editor instance from the shared preferences object using the edit() method.
     * <p>
     * It sets the player name value in the editor using the putString() method and passing an empty string as the value.
     * <p>
     * It commits the changes made to the editor using the commit() method, which saves the changes to the shared preferences.
     */
    private void clearNamePref() {
        SharedPreferences preferences = getSharedPreferences("PREFS", 0);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("playerName", "");
        editor.commit();
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }

    private void showNoInternetDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(SettingsActivity.this);
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
