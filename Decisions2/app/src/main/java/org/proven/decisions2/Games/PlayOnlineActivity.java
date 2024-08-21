package org.proven.decisions2.Games;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.proven.decisions2.Friends.CustomListAdapter;
import org.proven.decisions2.Friends.FriendsActivity;
import org.proven.decisions2.R;
import org.proven.decisions2.Settings.SettingsActivity;
import org.proven.decisions2.SocialInterface;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class PlayOnlineActivity extends Activity {

    TextView infoRequests, infoConnect;
    Button btHome, btSettings, btFriends;
    ListView listFriend, listOfPetitions;
    CustomListAdapter mFriendsAdapter;
    String selectedUsername;
    EditText decision;

    String url = "http://5.75.251.56:7070/getFriends";
    String url2 = "http://5.75.251.56:7070/getNameOfUser";


    ArrayList<String> roomList;
    ArrayList<String> friendList;
    String username, selectedUsr;
    int minigame;
    boolean rivalFound, waiting = false;

    String playerName = "";
    String verify = "";
    String roomName = "";
    ProgressDialog dialog;


    FirebaseDatabase database;
    DatabaseReference playerRef;
    DatabaseReference randomMinigame;
    DatabaseReference roomRef;
    DatabaseReference toCompare;
    DatabaseReference checkCanPlay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.play_online_layout);

        readUser();

        initelements();

        initButtons();

        getFriends(username);

        getsPlayer();
        getRooms();

    }

    private void initelements() {
        infoConnect = findViewById(R.id.infoConnect);
        infoRequests = findViewById(R.id.infoRequests);
        btHome = findViewById(R.id.btHome);
        btSettings = findViewById(R.id.btSettings);
        btFriends = findViewById(R.id.btFriends);
        database = FirebaseDatabase.getInstance();
        roomList = new ArrayList<>();
        friendList = new ArrayList<>();
        listFriend = findViewById(R.id.lvConnect);
        //cambiar a false
        rivalFound = false;
        listOfPetitions = findViewById(R.id.lvAccept);
        dialog = new ProgressDialog(this);
        decision = findViewById(R.id.etdecision);
    }

    /**
     * The initButtons() method sets up the click listeners for the buttons in the activity. Here's what the updated code does:
     * <p>
     * It sets an OnClickListener for the btHome button. When the button is clicked, it starts a new activity by creating an Intent with the source activity (PlayOnlineActivity.this)
     * and the destination activity (SocialInterface.class). It calls the startActivity() method to start the new activity.
     * <p>
     * It sets an OnClickListener for the btSettings button. When the button is clicked, it starts a new activity by creating an Intent with the source activity
     * (PlayOnlineActivity.this) and the destination activity (SettingsActivity.class). It calls the startActivity() method to start the new activity.
     * <p>
     * It sets an OnClickListener for the btFriends button. When the button is clicked, it starts a new activity by creating an Intent with the source activity
     * (PlayOnlineActivity.this) and the destination activity (FriendsActivity.class). It calls the startActivity() method to start the new activity.
     */
    private void initButtons() {
        btHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(PlayOnlineActivity.this, SocialInterface.class));
            }
        });

        btSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(PlayOnlineActivity.this, SettingsActivity.class));
            }
        });

        btFriends.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(PlayOnlineActivity.this, FriendsActivity.class));
            }
        });

    }

    /**
     * The checkPlayerExists() method is used to check if a player already exists in the database. Here's an explanation of the code:
     * <p>
     * It retrieves the shared preferences object using the key "PREFS" and mode 0 (private mode) to access the shared preferences.
     * <p>
     * It retrieves the value associated with the key "playerName" from the shared preferences. This value represents the player's name.
     * <p>
     * If the player's name exists (!verify.equals("")), it means that the player already exists in the database. In this case, it performs the following actions:
     * <p>
     * It obtains a reference to the player's data in the database using the playerRef object.
     * It adds an event listener to the playerRef to listen for changes in the player's data.
     * It sets the value of the playerRef to an empty string. This is done to trigger the event listener and indicate that the player is online or active.
     * If the player's name doesn't exist, it means that this is a new player. In this case, it performs the following actions:
     * <p>
     * It obtains an editor object from the shared preferences to make changes.
     * It stores the player's name (playerName) in the shared preferences using the key "playerName".
     * It commits the changes made by the editor to the shared preferences.
     */
    private void checkPlayerExists() {
        SharedPreferences preferences = getSharedPreferences("PREFS", 0);
        verify = preferences.getString("playerName", "");

        if (!verify.equals("")) {
            playerRef = database.getReference("player/" + playerName);
            addEventListener();
            playerRef.setValue("");
        } else {
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString("playerName", playerName);
            editor.commit();
        }
    }

    /**
     * The getsPlayer() method is used to retrieve the player's data from the database and set the player's status to online. Here's an explanation of the code:
     * <p>
     * It obtains a reference to the player's data in the database using the playerRef object. The reference is based on the player's name (playerName).
     * <p>
     * It adds an event listener to the playerRef to listen for changes in the player's data. The addEventListener() method is responsible for handling the data changes.
     * <p>
     * It sets the value of the playerRef to an empty string (""). This is done to indicate that the player is online or active.
     * By setting the value, it triggers the event listener added in the previous step.
     */
    private void getsPlayer() {
        playerRef = database.getReference("player/" + playerName);
        addEventListener();
        playerRef.setValue("");
    }

    /**
     * The assignPlayerToRoom() method is used to assign the current player to a room in the database. Here's an explanation of the code:
     * <p>
     * It obtains a reference to the specific room in the database using the roomRef object. The reference is based on the room name (roomName)
     * and the player number (player1 in this case).
     * <p>
     * It adds an event listener to the roomRef to listen for changes in the room's data. The addRoomEventListener() method is responsible for handling the data changes.
     * <p>
     * It sets the value of the roomRef to the current player's name (playerName). This assigns the player to the room by storing their name in the player1 field of the room's data.
     */
    private void assignPlayerToRoom() {
        roomRef = database.getReference("rooms/" + roomName + "/player1");
        addRoomEventListener();
        roomRef.setValue(playerName);
    }

    /**
     * The asignSecondPlayer() method is used to assign the second player to a room in the database. Here's an explanation of the code:
     * <p>
     * It obtains a reference to the specific room in the database using the roomRef object. The reference is based on the room name (roomName)
     * and the player number (player2 in this case).
     * <p>
     * It adds an event listener to the roomRef to listen for changes in the room's data. The addRoomEventListener() method is responsible for handling the data changes.
     * <p>
     * It sets the value of the roomRef to the second player's name (playN). This assigns the second player to the room by storing their name in the player2 field of the room's data.
     */
    private void asignSecondPlayer(String playN) {
        roomRef = database.getReference("rooms/" + roomName + "/player2");
        addRoomEventListener();
        roomRef.setValue(playN);
    }


    /**
     * The addEventListener() method sets up a ValueEventListener for the playerRef DatabaseReference. Here's an explanation of the code:
     * <p>
     * The playerRef DatabaseReference is listening for changes in the data at that location.
     * <p>
     * When the data changes (specifically, when the onDataChange() method is triggered), it checks if the playerName is not empty.
     * <p>
     * If the playerName is not empty, it retrieves the shared preferences using the key "PREFS" and mode 0.
     * <p>
     * It obtains an editor for the shared preferences and sets the "playerName" key with the value of playerName.
     * <p>
     * Finally, it applies the changes to the shared preferences.
     */
    private void addEventListener() {
        playerRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (!playerName.equals("")) {
                    SharedPreferences preferences = getSharedPreferences("PREFS", 0);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString("playerName", playerName);
                    editor.apply();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    /**
     * The addRoomEventListener() method sets up a ValueEventListener for the roomRef DatabaseReference. Here's an explanation of the code:
     * <p>
     * The roomRef DatabaseReference is listening for changes in the data at that location.
     * <p>
     * When the data changes (specifically, when the onDataChange() method is triggered), the code inside the method will be executed. However, in the provided code,
     * the onDataChange() method is empty, so it does not perform any specific action when the data changes.
     * <p>
     * The onCancelled() method is triggered if the listener is canceled or if there is an error with the database operation.
     * In this case, it simply prints an error message to the console, stating "Error creating room".
     */
    private void addRoomEventListener() {
        roomRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                System.out.println("Error creating room");
            }
        });
    }

    /**
     * The getRooms() method retrieves the data from the "rooms" DatabaseReference and sets up a ValueEventListener to listen for changes. Here's an explanation of the code:
     * <p>
     * The roomRef DatabaseReference is set to the location "rooms".
     * <p>
     * The roomRef ValueEventListener is added using the addValueEventListener() method.
     * <p>
     * When the data changes (specifically, when the onDataChange() method is triggered), the code inside the method will be executed.
     * <p>
     * Inside the onDataChange() method, the roomList (presumably a list of rooms) is cleared to remove any existing data.
     * <p>
     * The snapshot parameter contains the snapshot of the data at the "rooms" location. By calling getChildren() on the snapshot, you can iterate over the children nodes (rooms)
     * under the "rooms" location.
     * <p>
     * For each room (DataSnapshot), a reference to the "player2" location within that room is obtained using database.getReference("rooms/"+dataS.getKey()+"/player2").
     * This allows you to check if there is a second player in the room.
     * <p>
     * The addCompareListener() method is called, passing the room key as an argument, to set up a listener to compare the room with the second player.
     * <p>
     * The onCancelled() method is triggered if the listener is canceled or if there is an error with the database operation.
     * In this case, it is empty and does not perform any specific action.
     */
    private void getRooms() {
        roomRef = database.getReference("rooms");
        roomRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                roomList.clear();
                Iterable<DataSnapshot> rooms = snapshot.getChildren();

                for (DataSnapshot dataS : rooms) {
                    toCompare = database.getReference("rooms/" + dataS.getKey() + "/player2");
                    addCompareListener(dataS.getKey());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    /**
     * The addCompareListener() method sets up a ValueEventListener for the toCompare DatabaseReference to compare the room's second player with the current player.
     * Here's an explanation of the code:
     * <p>
     * The toCompare DatabaseReference represents the location of the "player2" within a specific room.
     * <p>
     * The toCompare ValueEventListener is added using the addValueEventListener() method.
     * <p>
     * When the data changes (when the onDataChange() method is triggered), the code inside the method will be executed.
     * <p>
     * Inside the onDataChange() method, the snapshot parameter contains the snapshot of the data at the "player2" location. By calling getValue(String.class) on the snapshot,
     * you can retrieve the value stored at that location as a String.
     * <p>
     * The if condition checks if the playerName is not null and the value stored at the "player2" location is not null.
     * This ensures that both the current player and the second player exist.
     * <p>
     * If the playerName matches the value stored at the "player2" location, the if condition is true. In this case, the code checks if the roomList does not already contain the room (things).
     * If the roomList does not contain the room, it adds the room to the roomList.
     * <p>
     * Finally, the setListOfPetitions() method is called, passing the updated roomList as an argument.
     * This method is responsible for updating the list of room petitions in your application.
     * <p>
     * The onCancelled() method is triggered if the listener is canceled or if there is an error with the database operation.
     * In this case, it is empty and does not perform any specific action.
     */
    private void addCompareListener(String things) {
        toCompare.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (playerName != null && snapshot.getValue(String.class) != null) {
                    if (snapshot.getValue(String.class).equalsIgnoreCase(playerName)) {
                        if (!roomList.contains(things)) {
                            roomList.add(things);
                        }
                    }
                }
                setListOfPetitions(roomList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    /**
     * The setCheckCanPlay() method sets up a ValueEventListener for the checkCanPlay DatabaseReference to monitor the game status. Here's an explanation of the code:
     * <p>
     * The checkCanPlay DatabaseReference represents the location of the game status, which is being checked for changes.
     * <p>
     * The checkCanPlay ValueEventListener is added using the addValueEventListener() method.
     * <p>
     * When the data changes (when the onDataChange() method is triggered), the code inside the method will be executed.
     * <p>
     * Inside the onDataChange() method, the snapshot parameter contains the snapshot of the data at the checkCanPlay location.
     * By calling getValue(String.class) on the snapshot, you can retrieve the value stored at that location as a String.
     * <p>
     * The if condition checks if the value stored at the checkCanPlay location is not null. If it is not null, further checks are performed.
     * <p>
     * If the value contains the string "waiting", it means that the game is still in the waiting phase, indicating that a rival player has not been found yet.
     * In this case, the rivalFound variable is set to false.
     * <p>
     * If the value contains the string "start", it means that the game is ready to start. In this case, the code dismisses the dialog (if any), and proceeds to create a
     * random minigame by calling the createRandomMinigame() method and passing the roomNa parameter.
     * After that, the randomMinigame DatabaseReference is used to set the value of the random minigame to an empty string.
     * <p>
     * The onCancelled() method is triggered if the listener is canceled or if there is an error with the database operation.
     * In this case, it is empty and does not perform any specific action.
     */
    private void setCheckCanPlay(String roomNa) {
        checkCanPlay.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.getValue(String.class) != null) {
                    if (snapshot.getValue(String.class).contains("waiting")) {
                        rivalFound = false;

                    } else if (snapshot.getValue(String.class).contains("start")) {
                        dialog.dismiss();

                        System.out.println("setcheckandPlay: " + roomNa);
                        randomMinigame = database.getReference("rooms/" + roomNa + "/game");
                        createRandomMinigame(roomNa);
                        randomMinigame.setValue("");
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    /**
     * The setList() method is used to set up a list of friends in the listFriend ListView. Here's an explanation of the code:
     * <p>
     * The method takes an ArrayList of friendsList as a parameter, which contains the list of friends to be displayed.
     * <p>
     * A CustomListAdapter named mFriendsAdapter is created, passing the this context, the friendsList, and the layout resource R.layout.list_item_send to the constructor.
     * This adapter is used to populate the listFriend ListView.
     * <p>
     * The mFriendsAdapter is set as the adapter for the listFriend ListView using the setAdapter() method.
     * <p>
     * The code checks if the friendsList is empty or contains only empty strings. If so, it hides the listFriend ListView and shows the infoConnect view,
     * indicating that there are no friends available. Otherwise, it shows the listFriend ListView and hides the infoConnect view.
     * <p>
     * The listFriend ListView sets an OnItemClickListener to handle item clicks. When an item is clicked, the onItemClick() method is triggered.
     * <p>
     * Inside the onItemClick() method, the selected username is retrieved from the clicked item.
     * <p>
     * If the selected username is null, a toast message is displayed asking the user to select a username.
     * <p>
     * If a valid username is selected, an AlertDialog.Builder is created to confirm the selection.
     * <p>
     * If the user clicks "Yes" in the dialog, the assignPlayerToRoom() method is called to assign the current player to the room,
     * and the asignSecondPlayer() method is called to assign the selected username as the second player in the room.
     * <p>
     * The dialog is displayed with a message indicating that the opponent's acceptance is awaited.
     * <p>
     * The checkCanPlay DatabaseReference is initialized with the location "rooms/{roomName}/status",
     * and the setCheckCanPlay() method is called to monitor changes in the game status.
     * <p>
     * The checkCanPlay value is set to "waiting" to indicate that the game is in the waiting phase.
     * <p>
     * The waiting variable is set to true.
     * <p>
     * If the rivalFound flag is already set to true, it means that the rival player has already been found.
     * In this case, the dialog is dismissed, and the checkCanPlay value is set to an empty string.
     * <p>
     * The dialog is set to dismiss and handle the onCancel event by calling onBackPressed().
     */
    private void setList(ArrayList<String> friendsList) {
        mFriendsAdapter = new CustomListAdapter(this, friendsList, R.layout.list_item_send);
        listFriend.setAdapter(mFriendsAdapter);

        if (friendsList.isEmpty() || friendsList.size() == 0 || friendsList.contains("")) {
            listFriend.setVisibility(View.GONE);
            infoConnect.setVisibility(View.VISIBLE);


        } else {
            listFriend.setVisibility(View.VISIBLE);
            infoConnect.setVisibility(View.GONE);

        }
        listFriend.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (!validateEditText()) {
                    return;
                }
                // Get the username of the selected item in the list
                selectedUsername = (String) parent.getItemAtPosition(position);
                Log.d("Selected username", selectedUsername);

                if (selectedUsername == null) {
                    Toast.makeText(getApplicationContext(), "Please select a username", Toast.LENGTH_SHORT).show();
                    return;
                }

                AlertDialog.Builder builder = new AlertDialog.Builder(PlayOnlineActivity.this);
                builder.setTitle(R.string.btConfirm);
                builder.setMessage(getString(R.string.play_with) + " " + selectedUsername + "?");
                builder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        assignPlayerToRoom();
                        asignSecondPlayer(selectedUsername);

                        PlayOnlineActivity.this.dialog.setMessage(getString(R.string.waiting));
                        PlayOnlineActivity.this.dialog.setCanceledOnTouchOutside(false);
                        PlayOnlineActivity.this.dialog.show();

                        checkCanPlay = database.getReference("rooms/" + roomName + "/status");
                        setCheckCanPlay(roomName);
                        selectedUsr = selectedUsername;

                        checkCanPlay.setValue("waiting");
                        waiting = true;


                        if (rivalFound) {
                            PlayOnlineActivity.this.dialog.dismiss();
                            checkCanPlay.setValue("");
                        }

                        PlayOnlineActivity.this.dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                            @Override
                            public void onCancel(DialogInterface dialogInterface) {
                                onBackPressed();
                            }
                        });


                    }
                });
                builder.setNegativeButton(R.string.no, null);
                builder.show();

            }


        });
    }

    //Method Validate editText
    private boolean validateEditText() {
        String textDecision1 = decision.getText().toString();

        if (textDecision1.isEmpty()) {
            decision.setError(getString(R.string.write_first_decision));
            return false;
        }
        return true;
    }


    /**
     * The createRandomMinigame() method is used to create a random mini-game for the players in the specified room. Here's an explanation of the code:
     * <p>
     * The method takes the roomNa parameter, which represents the room name.
     * <p>
     * Inside the onDataChange() method of the randomMinigame ValueEventListener, a random number generator rand is initialized.
     * <p>
     * The code checks if the value retrieved from the randomMinigame DatabaseReference contains the string "game:". If so, it means a mini-game has been selected and the value represents the index of the mini-game.
     * <p>
     * If a mini-game is selected, the dialog is dismissed, and based on the value of ele, different actions are performed:
     * <p>
     * If ele is 0, it indicates a Question Quiz game. An intent is created to start the QuestionQuizGameOnline activity,
     * passing the roomName as an extra, and the activity is started. The current activity is finished.
     * <p>
     * If ele is 1, it indicates an Elements game. An intent is created to start the ElementsGameOnline activity,
     * passing the roomName as an extra, and the activity is started. The current activity is finished.
     * <p>
     * If ele is 2, it indicates a Penalties game. An intent is created to start the PenaltiesGameOnline activity,
     * passing the roomName and the selectedUsr as extras, and the activity is started. The current activity is finished.
     * <p>
     * If the value retrieved from the randomMinigame DatabaseReference is an empty string, it means the mini-game selection is pending.
     * In this case, if the roomNa matches the playerName,
     * it means the current player has the authority to select a mini-game. The dialog is dismissed, and a new value is set in the randomMinigame DatabaseReference,
     * representing the mini-game selection. The minigame variable stores a random number between 0 and 2, inclusive, which determines the mini-game to be played.
     */
    public void createRandomMinigame(String roomNa) {
        randomMinigame.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Random rand = new Random();
                if (snapshot.getValue(String.class) != null) {
                    if (snapshot.getValue(String.class).contains("game:")) {
                        dialog.dismiss();
                        int ele = Integer.parseInt(snapshot.getValue(String.class).replace("game:", ""));
                        if (ele == 0) {
                            String textoDecision1 = decision.getText().toString();
                            System.out.println("PlayOnline "+textoDecision1);
                            Intent intent = new Intent(getApplicationContext(), QuestionQuizGameOnline.class);
                            intent.putExtra("roomName", roomNa);
                            intent.putExtra("decision1", textoDecision1);
                            startActivity(intent);
                            finish();
                        } else if (ele == 1) {
                            String textoDecision1 = decision.getText().toString();
                            System.out.println("PlayOnline "+textoDecision1);
                            Intent intent = new Intent(getApplicationContext(), ElementsGameOnline.class);
                            intent.putExtra("roomName", roomNa);
                            intent.putExtra("decision1", textoDecision1);
                            startActivity(intent);
                            finish();
                        } else if (ele == 2) {
                            String textoDecision1 = decision.getText().toString();
                            System.out.println("PlayOnline "+textoDecision1);
                            Intent intent = new Intent(getApplicationContext(), PenaltiesGameOnline.class);
                            intent.putExtra("roomName", roomNa);
                            intent.putExtra("decision1", textoDecision1);
                            intent.putExtra("secondUsr", selectedUsr);
                            startActivity(intent);
                            finish();
                        }
                    } else if (snapshot.getValue(String.class).equals("")) {
                        if (roomNa.equals(playerName)) {
                            dialog.dismiss();
                            randomMinigame = database.getReference("rooms/" + roomNa + "/game");

                            minigame = rand.nextInt(3);
                            randomMinigame.setValue("game:" + minigame);
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    /**
     * The setListOfPetitions() method is used to set the list of friend requests or petitions to play with other users. Here's an explanation of the code:
     * <p>
     * The method takes the friendsList parameter, which is the list of friend requests or petitions.
     * <p>
     * The mFriendsAdapter is initialized with the CustomListAdapter, which takes the current activity (this), the friendsList,
     * and the layout resource R.layout.list_item_play for each item in the list.
     * <p>
     * The mFriendsAdapter is set as the adapter for the listOfPetitions ListView.
     * <p>
     * The code checks if the friendsList is empty or contains only empty strings. If so, it means there are no friend requests or petitions,
     * and the visibility of listOfPetitions is set to View.GONE, while the visibility of infoRequests is set to View.VISIBLE.
     * <p>
     * If there are friend requests or petitions in the friendsList, the visibility of listOfPetitions is set to View.VISIBLE,
     * and the visibility of infoRequests is set to View.GONE.
     * <p>
     * The listOfPetitions ListView is set with an OnItemClickListener to handle item clicks. When an item is clicked, the username of the selected item is retrieved.
     * <p>
     * If no username is selected, a toast is shown to prompt the user to select a username.
     * <p>
     * If a username is selected, an AlertDialog is created with a confirmation message to play with the selected user.
     * <p>
     * If the user confirms by clicking "Yes", the checkCanPlay DatabaseReference is set to the status of the selected user's room.
     * The setCheckCanPlay() method is called to listen for changes in the status.
     * <p>
     * The checkCanPlay DatabaseReference is set to "start" to indicate that the player is ready to start the game.
     */
    private void setListOfPetitions(ArrayList<String> friendsList) {

        mFriendsAdapter = new CustomListAdapter(this, friendsList, R.layout.list_item_play);
        listOfPetitions.setAdapter(mFriendsAdapter);

        if (friendsList.isEmpty() || friendsList.size() == 0 || friendsList.contains("")) {

            listOfPetitions.setVisibility(View.GONE);
            infoRequests.setVisibility(View.VISIBLE);

        } else {
            listOfPetitions.setVisibility(View.VISIBLE);
            infoRequests.setVisibility(View.GONE);
        }
        listOfPetitions.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (!validateEditText()) {
                    return;
                }
                // Get the username of the selected item in the list
                selectedUsername = (String) parent.getItemAtPosition(position);
                Log.d("Selected username", selectedUsername);

                if (selectedUsername == null) {
                    return;
                }
                AlertDialog.Builder builder = new AlertDialog.Builder(PlayOnlineActivity.this);
                builder.setTitle(R.string.btConfirm);
                builder.setMessage(getString(R.string.play_with) + " " + selectedUsername + "?");
                builder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        checkCanPlay = database.getReference("rooms/" + selectedUsername + "/status");
                        setCheckCanPlay(selectedUsername);
                        checkCanPlay.setValue("start");

                    }
                });
                builder.setNegativeButton(R.string.no, null);
                builder.show();

            }
        });
    }

    /**
     * The onBackPressed() method is overridden to handle the back button press in the PlayOnlineActivity. Here's an explanation of the code:
     * <p>
     * The method checks if the checkCanPlay DatabaseReference is null or empty. If it is, it means that there is no active game or room,
     * so the default behavior of the back button is invoked by calling super.onBackPressed().
     * <p>
     * If the checkCanPlay DatabaseReference is not null or empty, it means that the user is in a waiting state for a game to start. In this case,
     * the room is deleted by calling the deleteRoom(roomName) method to remove the room from the database.
     * <p>
     * The PlayOnlineActivity dialog is dismissed by calling PlayOnlineActivity.this.dialog.dismiss().
     * <p>
     * The waiting variable is set to false to indicate that the user is no longer waiting for a game.
     */
    @Override
    public void onBackPressed() {
        if (checkCanPlay == null || checkCanPlay.equals("")) {
            super.onBackPressed();
        } else if (waiting == true) {
            deleteRoom(roomName);
            PlayOnlineActivity.this.dialog.dismiss();
            waiting = !waiting;
        }
    }

    /**
     * Calls getUserName for get the name of the user and playFriends to show the list of frinds
     */
    private void getFriends(String username) {
        new GetUserName().execute(username);
        new PlayFriend().execute(username);
    }

    /**
     * The provided code shows the implementation of the PlayFriend class, an asynchronous task that fetches the friend list for the given username using an HTTP request.
     * <p>
     * Here's the breakdown of the implementation:
     * <p>
     * The PlayFriend class extends AsyncTask<String, Void, ArrayList<String>>, indicating that it takes a string parameter as input, does not provide progress updates,
     * and returns an ArrayList<String> as the result.
     * <p>
     * The doInBackground method is overridden to perform the network request in the background. It receives the username as a parameter.
     * <p>
     * Inside the doInBackground method, an OkHttpClient is created to make the HTTP request.
     * <p>
     * The username is added as an authorization header in the request.
     * <p>
     * The response is obtained and processed to extract the friend list.
     * <p>
     * The extracted friend list is returned as the result.
     * <p>
     * The onPostExecute method is overridden to handle the result of the background task.
     * <p>
     * The friendList variable is updated with the result, and the setList method is called to update the UI with the friend list.
     */
    private class PlayFriend extends AsyncTask<String, Void, ArrayList<String>> {
        @Override
        protected ArrayList<String> doInBackground(String... params) {
            username = params[0];
            ArrayList<String> friendsList = new ArrayList<>();
            OkHttpClient client = new OkHttpClient();
            MediaType mediaType = MediaType.parse("application/json");
            if (username != null) {
                String requestBodyString = "";
                RequestBody requestBody = RequestBody.create(mediaType, requestBodyString);
                Request request = new Request.Builder()
                        .url(url)
                        .post(requestBody)
                        .addHeader("content-type", "application/json")
                        .addHeader("cache-control", "no-cache")
                        .addHeader("Authorization", username)
                        .build();

                try {
                    Response response = client.newCall(request).execute();
                    String toSplit = response.body().string();
                    String textoSinComillas = toSplit.replace("\"", "");
                    String textoSinCorchetes = textoSinComillas.replace("[", "").replace("]", "");
                    friendsList.addAll(Arrays.asList(textoSinCorchetes.split(",")));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return friendsList;
        }

        @Override
        protected void onPostExecute(ArrayList<String> result) {
            super.onPostExecute(result);
            friendList = result;
            setList(friendList);
        }
    }

    /**
     * The provided code shows the implementation of the GetUserName class, an asynchronous task that fetches the username and performs related operations using an HTTP request.
     * <p>
     * Here's the breakdown of the implementation:
     * <p>
     * The GetUserName class extends AsyncTask<String, Void, String>, indicating that it takes a string parameter as input,
     * does not provide progress updates, and returns a String as the result.
     * <p>
     * The doInBackground method is overridden to perform the network request in the background. It receives the username as a parameter.
     * <p>
     * Inside the doInBackground method, an OkHttpClient is created to make the HTTP request.
     * <p>
     * The username is added as an authorization header in the request.
     * <p>
     * The response is obtained and processed to extract the username.
     * <p>
     * The extracted username is stored in the playerName variable.
     * <p>
     * The checkPlayerExists method is called to perform related operations.
     * <p>
     * The roomName variable is set to the player's username.
     * <p>
     * The doInBackground method returns null as there is no specific result to be passed to the onPostExecute method.
     */
    private class GetUserName extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            username = params[0];
            OkHttpClient client = new OkHttpClient();
            MediaType mediaType = MediaType.parse("application/json");
            if (username != null) {
                String requestBodyString = "";
                RequestBody requestBody = RequestBody.create(mediaType, requestBodyString);
                Request request = new Request.Builder()
                        .url(url2)
                        .post(requestBody)
                        .addHeader("content-type", "application/json")
                        .addHeader("cache-control", "no-cache")
                        .addHeader("Authorization", username)
                        .build();

                try {
                    Response response = client.newCall(request).execute();
                    String toSplit = response.body().string();
                    String textoSinComillas = toSplit.replace("\"", "");
                    String textoSinCorchetes = textoSinComillas.replace("[", "").replace("]", "");

                    playerName = textoSinCorchetes;
                    checkPlayerExists();
                    roomName = playerName;

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }
    }

    /**
     * The provided code reads the contents of a file named "token.txt" and assigns the read value to the username variable. Here's a breakdown of the code:
     * <p>
     * The method readUser() is defined.
     * <p>
     * It creates a File object named filename with the path to the "token.txt" file in the app's internal storage directory (getFilesDir()).
     * <p>
     * It wraps the file reading operations in a try-catch block to handle any potential IOException.
     * <p>
     * Inside the try block, it creates a FileReader to read the file.
     * <p>
     * It creates a BufferedReader named bufferedReader to read the file contents line by line.
     * <p>
     * It reads the first line of the file using the readLine() method and assigns the value to the username variable.
     * <p>
     * It closes the BufferedReader and FileReader using the close() method.
     * <p>
     * If an IOException occurs, it throws a RuntimeException with the caught exception as the cause.
     */
    private void readUser() {
        File filename = new File(getFilesDir(), "token.txt");
        try {
            FileReader reader = new FileReader(filename);
            BufferedReader bufferedReader = new BufferedReader(reader);
            username = bufferedReader.readLine();
            bufferedReader.close();
            reader.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * The deleteRoom method you provided deletes a room from the Firebase Realtime Database. Here's how the code works:
     * <p>
     * The method deleteRoom is defined with a roomName parameter.
     * It retrieves a reference to the "rooms" node in the database using database.getReference("rooms/").
     * It appends the roomName to the reference path to specify the specific room to be deleted.
     * It calls the removeValue() method on the database reference to remove the room and all its child nodes from the database.
     * Please note that this code assumes you have
     */
    private void deleteRoom(String roomName) {
        database.getReference("rooms/" + roomName).removeValue();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        deleteRoom(roomName);
    }
}