package org.proven.decisions2;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;

import org.proven.decisions2.LoginAndRegister.MainActivity;

public class ChargingScreen extends Activity {

    /**
     * The code provided represents the onCreate() method of an activity called ChargingScreen. Here's an explanation of the code:
     *
     * The window is set to fullscreen using the getWindow().setFlags() method. This ensures that the activity is displayed in fullscreen mode.
     *
     * The super.onCreate(savedInstanceState) method is called to perform the default creation of the activity.
     *
     * The content view is set to the layout file activity_charging_screen.xml using the setContentView() method. This defines the UI layout for the charging screen activity.
     *
     * The variable Duration is set to 4000, representing the duration (in milliseconds) for which the charging screen will be displayed.
     *
     * A new Handler is created, and a delayed Runnable is posted to it. This Runnable contains the logic to be executed after the specified duration.
     *
     * Inside the run() method of the Runnable, an Intent is created to move on to the next activity (MainActivity in this case).
     *
     * The new activity is started using the startActivity() method, and the current activity is finished using the finish() method. This ensures that the charging screen activity is closed after starting the next activity.
     *
     * @param savedInstanceState If the activity is being re-initialized after
     *     previously being shut down then this Bundle contains the data it most
     *     recently supplied in {@link #onSaveInstanceState}.  <b><i>Note: Otherwise it is null.</i></b>
     *
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        //Set the window to fullscreen
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        //Call the superclass onCreate method
        super.onCreate(savedInstanceState);

        //Set the content view to activity_charging_screen.xml
        setContentView(R.layout.activity_charging_screen);

        //Set the duration for the charging screen
        final int Duration = 4000;

        //Create a new Handler and post a delayed Runnable to it
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                //Create an Intent to move on to the next activity
                Intent intent = new Intent(ChargingScreen.this, MainActivity.class);
                //Start the new activity
                startActivity(intent);
                //Finish the current activity
                finish();
            }
        }, Duration);
    }
}
