package org.proven.decisions2.Games;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.proven.decisions2.R;

public class ElementsGameOnline extends Activity {

    // Declare a CountDownTimer variable called countDownTimer
    CountDownTimer countDownTimer;

    // Declare CardView variables called btWater, btFire, btIce, machine, and player
    CardView btWater, btFire, btIce, machine, player;

    // Declare a View.OnClickListener variable called listener
    View.OnClickListener listener;

    // Declare a TextView variable called tvResult
    TextView tvResult;

    // Declare integer variables called election, rival, and value.
    int election, rival, value, afk;

    String playerName;
    String roomName;
    String role;
    String message;

    FirebaseDatabase database;
    DatabaseReference messageRef;
    DatabaseReference hostEle;
    DatabaseReference onExit;

    Boolean charge = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Set the layout for this activity to elements_animation_layout.xml
        setContentView(R.layout.elements_animation_layout);

        // Use a Handler to delay the execution of some code
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // After 6.5 seconds, change the layout to elements_game_layout.xml
                setContentView(R.layout.elements_game_layout);
                charge = true;

                //Init multiplayer elements
                earlyerInit();

                // Call initializeElements() method to initialize the game
                initializeElements();

                // Call initCrono() method to initialize the timer
                initCrono();
                countDownTimer.start();

                // Set the foreground drawable of the machine ImageView to question.png
                machine.setForeground(ContextCompat.getDrawable(ElementsGameOnline.this, R.drawable.question));
            }
        }, 6500); // Wait for 6.5 seconds before executing the code inside the Runnable
    }

    // This method initializes the countdown timer
    private void initCrono() {
        // Create a new instance of CountDownTimer and set its duration and tick interval
        countDownTimer = new CountDownTimer(15000, 1000) {
            // This method is called every tick and updates the remaining time on the UI
            @Override
            public void onTick(long time) {
                // Calculate the remaining seconds and update the text view with the result
                long segPendiente = time / 1000;
                tvResult.setText(getString(R.string.time) + ": " + segPendiente);
            }

            // This method is called when the timer finishes
            @Override
            public void onFinish() {
                btWater.setEnabled(false);
                btFire.setEnabled(false);
                btIce.setEnabled(false);

                // Generate a random number to select an element for the rival
                if(rival != 0 && election != 0){
                    checkWin(election,rival);
                }else{
                    afk++;
                    if(rival == 0 && election == 0){
                        tvResult.setText(R.string.select_element);
                    } else if(rival == 0 || election == 0) {
                        tvResult.setText(R.string.one_player_dont_select);
                    }

                    if (afk==3){
                        countDownTimer.cancel();
                        afkDisconnection();

                    }

                    if (afk<3){
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                forceGuestHostEmpty();
                                restartGame();
                            }
                        }, 2000); // 2000 milisegundos = 3 segundos
                    }
                }
            }
        };
    }

    private void afkDisconnection(){
        countDownTimer.cancel(); // Cancels the countdown timer
        tvResult.setText(R.string.afk_disconnection); // Sets the text of a TextView to the string resource "afk_disconnection"
        value = 3; // Sets the value variable to 3
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent afkResult = new Intent(ElementsGameOnline.this, ResultGame.class); // Creates a new Intent to navigate to the ResultGame activity
                afkResult.putExtra("result", value); // Adds the value variable as an extra to the intent with the key "result"
                startActivity(afkResult); // Starts the ResultGame activity with the created intent
                finish(); // Finishes the current activity
            }
        }, 2000); // Delays the execution of the code inside the runnable by 2000 milliseconds (2 seconds)
        deleteRoom(roomName); // Calls a method to delete a room with the specified roomName
    }


    // Initialize the views and set the click listener for the element buttons
    public void initializeElements(){
        btWater = findViewById(R.id.btWater);
        btFire = findViewById(R.id.btFire);
        btIce = findViewById(R.id.btIce);
        tvResult = findViewById(R.id.crono);
        machine = findViewById(R.id.machine);
        player = findViewById(R.id.player);
        database = FirebaseDatabase.getInstance();
        setOnClickListener();
        instanciateListener();
    }

    public void earlyerInit(){
        SharedPreferences preferences = getSharedPreferences("PREFS",0); // Retrieves the SharedPreferences object with the name "PREFS"
        playerName = preferences.getString("playerName",""); // Retrieves the value of the "playerName" key from the SharedPreferences, with a default value of ""

        Bundle extras = getIntent().getExtras(); // Retrieves the extras from the intent that started the activity
        if(extras != null){
            roomName = extras.getString("roomName"); // Retrieves the value associated with the key "roomName" from the extras

            if(roomName.equals(playerName)){ // Checks if the roomName is equal to the playerName
                role="host"; // Sets the role variable to "host"
            }else{
                role="guest"; // Sets the role variable to "guest"
            }
        }
    }

    // Set the click listener for the element buttons
    public void instanciateListener(){
        btWater.setOnClickListener(listener);
        btFire.setOnClickListener(listener);
        btIce.setOnClickListener(listener);
    }

    // This method sets an OnClickListener for the buttons btWater, btFire, and btIce.
    public void setOnClickListener(){
        // A new View.OnClickListener object is created and assigned to the listener variable.
        listener= new View.OnClickListener() {
            @Override
            // The onClick method of the View.OnClickListener interface is overridden.
            public void onClick(View v) {
                // If the id of the clicked view is equal to the id of btWater, the election variable is set to 2.
                if(v.getId() == btWater.getId()){
                    election = 2;

                    sendDepending(2);
                    disableBt(2);
                    // The checkWin method is called with the election variable as its parameter.
                    //checkWin(election);
                }
                // If the id of the clicked view is equal to the id of btFire, the election variable is set to 1.
                if(v.getId() == btFire.getId()){
                    election = 1;

                    sendDepending(1);
                    disableBt(1);
                    // The checkWin method is called with the election variable as its parameter.
                    //checkWin(election);
                }

                // If the id of the clicked view is equal to the id of btIce, the election variable is set to 3.
                if(v.getId() == btIce.getId()){
                    election = 3;

                    sendDepending(3);
                    disableBt(3);
                    // The checkWin method is called with the election variable as its parameter.
                    //checkWin(election);
                }
            }
        };
    }

    private void sendDepending(int i){
        if(role.equals("guest")){ // Checks if the role is "guest"
            messageRef = database.getReference("rooms/"+roomName+"/message"); // Gets the database reference to the "message" node in the specific room
            message = role+":"+i; // Constructs the message string with the role and the value of i
            addRoomEventListener(); // Adds a listener to the database reference for handling events
            messageRef.setValue(message); // Sets the value of the database reference to the constructed message

        }else if(role.equals("host")){ // Checks if the role is "host"
            hostEle = database.getReference("rooms/"+roomName+"/hostele"); // Gets the database reference to the "hostele" node in the specific room
            message = role+":"+i; // Constructs the message string with the role and the value of i
            getHostEventListener(); // Retrieves a listener for the host event handling
            hostEle.setValue(message); // Sets the value of the database reference to the constructed message
        }
    }

    private void addRoomEventListener(){
        messageRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(role.equals("guest")){ // Checks if the role is "guest"
                    if (snapshot.getValue(String.class) != null){ // Checks if the snapshot has a non-null value
                        hostEle = database.getReference("rooms/"+roomName+"/hostele"); // Gets the database reference to the "hostele" node in the specific room
                        getHostEventListener(); // Retrieves a listener for the host event handling
                    }
                }else if(role.equals("host")){ // Checks if the role is "host"
                    if(snapshot.getValue(String.class) != null){ // Checks if the snapshot has a non-null value
                        String alter = snapshot.getValue(String.class).replace("guest:",""); // Removes the "guest:" prefix from the snapshot value
                        if(alter.matches("\\d+") && election != 0){ // Checks if the altered value is a number and election is not 0
                            rival = Integer.parseInt(alter); // Parses the altered value to an integer and assigns it to the rival variable
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                System.out.println("Error creating room: "+error);
            }
        });
    }

    private void getHostEventListener(){
        hostEle.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(role.equals("guest")){ // Checks if the role is "guest"
                    if(snapshot.getValue(String.class) != null){ // Checks if the snapshot has a non-null value
                        String alter = snapshot.getValue(String.class).replace("host:",""); // Removes the "host:" prefix from the snapshot value
                        if(alter.matches("\\d+") && election != 0){ // Checks if the altered value is a number and election is not 0
                            rival = Integer.parseInt(alter); // Parses the altered value to an integer and assigns it to the rival variable
                        }
                    }
                }else if(role.equals("host")){ // Checks if the role is "host"
                    messageRef = database.getReference("rooms/"+roomName+"/message"); // Gets the database reference to the "message" node in the specific room
                    addRoomEventListener(); // Adds a listener to the messageRef for handling events
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                System.out.println("Error getting host: "+error); // Prints an error message if there is a database error
            }
        });
    }

    // This method is used to check if the user has won or lost the game
    // It takes an integer argument "election" which represents the user's choice of element
    public void checkWin(int electio, int rivae){

        countDownTimer.cancel();

        btWater.setEnabled(false);
        btFire.setEnabled(false);
        btIce.setEnabled(false);


        // Sets the drawable ID for the rival's element based on the randomly generated number
        int machineDrawableId = 0;
        switch (rivae) {
            case 1:
                machineDrawableId = R.drawable.fire_element;
                break;
            case 2:
                machineDrawableId = R.drawable.water_element;
                break;
            case 3:
                machineDrawableId = R.drawable.ice_element;
                break;
        }

        // Sets the drawable ID for the user's chosen element based on the argument passed to the method
        int playerDrawableId = 0;
        switch (electio) {
            case 1:
                playerDrawableId = R.drawable.fire_element;
                break;
            case 2:
                playerDrawableId = R.drawable.water_element;
                break;
            case 3:
                playerDrawableId = R.drawable.ice_element;
                break;
        }

        // Sets the foreground of the machine ImageView to the drawable of the rival's chosen element
        machine.setForeground(ContextCompat.getDrawable(ElementsGameOnline.this, machineDrawableId));

        // Sets the foreground of the player ImageView to the drawable of the user's chosen element
        player.setForeground(ContextCompat.getDrawable(ElementsGameOnline.this, playerDrawableId));

        // Determines the outcome of the game based on the user's choice and the rival's choice
        if(election == 1 && rival == 2 || election == 2 && rival == 3 || election == 3 && rival == 1){
            // If the user loses, sets the text of the tvResult TextView to "Defeat"
            tvResult.setText(R.string.defeat);

            // Sets the value of "value" to 1
            value = 1;

            // Creates a new Handler and posts a delayed Runnable to start the ResultGame Activity after a 2 second delay
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent intent = new Intent(ElementsGameOnline.this, ResultGame.class);
                    intent.putExtra("result", value);
                    startActivity(intent);
                    finish();
                    deleteRoom(roomName);
                }
            }, 2000); // 2000 milliseconds = 2 seconds delay
        }else if(election == 2 && rival == 1 || election == 3 && rival == 2 || election == 1 && rival == 3){
            // If the user wins, sets the text of the tvResult TextView to "Victory"
            tvResult.setText(R.string.victory);

            // Sets the value of "value" to 0
            value = 0;

            // Creates a new Handler and posts a delayed Runnable to start the ResultGame Activity after a 2 second delay
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent intent = getIntent();
                    String textoDecision1 = intent.getStringExtra("decision1");
                    String textoDecision2 = intent.getStringExtra("decision2");
                    System.out.println("ElementsGameOnline "+textoDecision1);
                    System.out.println("ElementsGameOnline "+textoDecision2);
                    Intent intent2 = new Intent(ElementsGameOnline.this, ResultGame.class);
                    intent2.putExtra("decision1", textoDecision1);
                    intent2.putExtra("decision2", textoDecision2);
                    intent.putExtra("result", value);
                    startActivity(intent2);
                    finish();
                    deleteRoom(roomName);
                }
            }, 2000); // 2000 milliseconds = 2 seconds delay
        }else {
            // If the game is a draw, sets the text of the tvResult TextView to "Draw"
            tvResult.setText(R.string.draw);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    restartGame(); //call the method for the restart
                }
            }, 2000); // 2000 milliseconds = 2 seconds delay
        }
    }

    // This method restarts the game by enabling all the buttons, resetting the result text view, initializing the chronometer,
    // resetting the player and machine foregrounds, and setting the value to zero.
    private void restartGame() {
        countDownTimer.start();
        btWater.setEnabled(true);
        btFire.setEnabled(true);
        btIce.setEnabled(true);

        int color = getResources().getColor(R.color.white);
        btFire.setCardBackgroundColor(color);
        btIce.setCardBackgroundColor(color);
        btWater.setCardBackgroundColor(color);

        tvResult.setText("");
        initCrono();
        player.setForeground(null);
        machine.setForeground(ContextCompat.getDrawable(ElementsGameOnline.this, R.drawable.question));
    }

    private void disableBt(int id){
        btWater.setEnabled(false); // Disables the btWater button
        btFire.setEnabled(false); // Disables the btFire button
        btIce.setEnabled(false); // Disables the btIce button

        int color = getResources().getColor(R.color.light_blue); // Retrieves the color defined in the resources with the name "light_blue"

        if(id == 1){ // Checks if the id is 1
            btFire.setCardBackgroundColor(color); // Sets the background color of btFire button to the defined color
        }else if(id == 2){ // Checks if the id is 2
            btWater.setCardBackgroundColor(color); // Sets the background color of btWater button to the defined color
        }else if(id == 3){ // Checks if the id is 3
            btIce.setCardBackgroundColor(color); // Sets the background color of btIce button to the defined color
        }
    }

    // This method overrides the default behavior of the back button press in the activity
    @Override
    public void onBackPressed() {
        if (charge) {
            super.onBackPressed();
            // Cancels the countdown timer associated with the activity
            countDownTimer.cancel();
            finish();
            deleteRoom(roomName);
        }
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (countDownTimer != null) {
            countDownTimer.cancel(); // Cancels the countDownTimer if it is not null
        }
        deleteRoom(roomName); // Calls the deleteRoom method passing the roomName to delete the room from the database
    }

    private void deleteRoom(String roomName) {
        if (database != null) { // Checks if the database reference is not null
            database.getReference("rooms/" + roomName).removeValue(); // Removes the specified room from the database
        }
    }

    private void forceGuestHostEmpty(){
        hostEle = database.getReference("rooms/"+roomName+"/hostele"); // Gets the database reference to the "hostele" node in the specific room
        message = "host:"+0; // Sets the message to "host:0"
        election=0; // Sets the election variable to 0
        rival = 0; // Sets the rival variable to 0
        hostEle.setValue(message); // Sets the value of the hostEle reference to the message
    }
}