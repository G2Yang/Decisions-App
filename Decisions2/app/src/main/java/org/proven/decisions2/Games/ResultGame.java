package org.proven.decisions2.Games;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import org.proven.decisions2.PublicDecision.CameraActivity;
import org.proven.decisions2.Friends.FriendsActivity;
import org.proven.decisions2.R;
import org.proven.decisions2.Settings.SettingsActivity;
import org.proven.decisions2.SocialInterface;

public class ResultGame extends Activity {

    Button btHome, btSettings, btFriends, btPhoto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        int result = getIntent().getIntExtra("result",0);

        if (result == 0){
            setContentView(R.layout.result_win_layout);
        }else if(result == 1){
            setContentView(R.layout.result_lose_layout);
        }else if(result == 2) {
            setContentView(R.layout.result_lose_machine_layout);
        }else if (result==3){
            setContentView(R.layout.result_afk_layout);
        }else{
            setContentView(R.layout.result_lost_connection_layout);
        }

        btHome = findViewById(R.id.btHome);
        btSettings = findViewById(R.id.btSettings);
        btFriends = findViewById(R.id.btFriends);
        btPhoto = findViewById(R.id.btPhoto);

        btHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ResultGame.this, SocialInterface.class));
                finishAndRemoveTask();
            }
        });

        btSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ResultGame.this, SettingsActivity.class));
                finishAndRemoveTask();
            }
        });

        btFriends.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ResultGame.this, FriendsActivity.class));
                finishAndRemoveTask();
            }
        });

        btPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = getIntent();
                String textoDecision1 = intent.getStringExtra("decision1");
                String textoDecision2 = intent.getStringExtra("decision2");
                System.out.println("ResultGame "+textoDecision1);
                System.out.println("ResultGame "+textoDecision2);
                Intent intent3 = new Intent(ResultGame.this, CameraActivity.class);
                intent3.putExtra("decision1", textoDecision1);
                intent3.putExtra("decision2", textoDecision2);
                startActivity(intent3);
                finishAndRemoveTask();
            }
        });
    }
}

