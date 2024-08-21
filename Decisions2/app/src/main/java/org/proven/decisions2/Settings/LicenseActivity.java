package org.proven.decisions2.Settings;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;

import org.proven.decisions2.Friends.FriendsActivity;
import org.proven.decisions2.R;
import org.proven.decisions2.SocialInterface;

public class LicenseActivity extends Activity {

    Button btHome, btSettings, btFriends;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cc_license_layout);

        initElements();

        btHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LicenseActivity.this, SocialInterface.class));
                finishAndRemoveTask();
            }
        });

        btSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LicenseActivity.this, SettingsActivity.class));
                finishAndRemoveTask();
            }
        });

        btFriends.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LicenseActivity.this, FriendsActivity.class));
                finishAndRemoveTask();
            }
        });
    }

    private void initElements() {
        btHome = findViewById(R.id.btHome);
        btFriends = findViewById(R.id.btFriends);
        btSettings = findViewById(R.id.btSettings);
    }
}
