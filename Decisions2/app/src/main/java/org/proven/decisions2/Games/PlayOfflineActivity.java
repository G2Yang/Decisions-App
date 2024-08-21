package org.proven.decisions2.Games;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import org.proven.decisions2.Friends.FriendsActivity;
import org.proven.decisions2.R;
import org.proven.decisions2.Settings.SettingsActivity;
import org.proven.decisions2.SocialInterface;

import java.util.Random;

public class PlayOfflineActivity extends Activity {

    Button btHome, btSettings, btFriends, btPlay;
    EditText decision1, decision2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.play_offline_layout);
        //initialize elements
        initializeElements();


        btHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(PlayOfflineActivity.this, SocialInterface.class));
            }
        });

        btSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(PlayOfflineActivity.this, SettingsActivity.class));
            }
        });

        btFriends.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(PlayOfflineActivity.this, FriendsActivity.class));
            }
        });
        btPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validateEditText()) {
                    Random rand = new Random();
                    int num = rand.nextInt(3);
                    String textoDecision1 = decision1.getText().toString();
                    String textoDecision2 = decision2.getText().toString();
                    System.out.println("PlayOffline "+textoDecision1);
                    System.out.println("PlayOffline "+textoDecision2);
                    Intent intent;
                    if (num == 0) {
                        intent = new Intent(PlayOfflineActivity.this, ElementsGame.class);
                    } else if (num == 1) {
                        intent = new Intent(PlayOfflineActivity.this, PenaltisGame.class);
                    } else {
                        intent = new Intent(PlayOfflineActivity.this, QuestionQuizGame.class);
                    }

                    intent.putExtra("decision1", textoDecision1);
                    intent.putExtra("decision2", textoDecision2);
                    startActivity(intent);
                }
            }
        });

    }

    //Method that initializes the elements
    private void initializeElements() {
        btHome = findViewById(R.id.btHome);
        btSettings = findViewById(R.id.btSettings);
        btFriends = findViewById(R.id.btFriends);
        btPlay = findViewById(R.id.btPlay);
        decision1 = findViewById(R.id.etDecision1);
        decision2 = findViewById(R.id.etDecision2);

    }
    //Method Validate editText
    private boolean validateEditText() {
        String textDecision1 = decision1.getText().toString();
        String textDecision2 = decision2.getText().toString();

        if (textDecision1.isEmpty()) {
            decision1.setError(getString(R.string.write_first_decision));
            return false;
        }

        if (textDecision2.isEmpty()) {
            decision2.setError(getString(R.string.write_second_decision));
            return false;
        }

        return true;
    }


}
