package org.proven.decisions2.Settings;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import android.view.View;
import android.widget.Button;

import org.proven.decisions2.Friends.FriendsActivity;
import org.proven.decisions2.R;
import org.proven.decisions2.SocialInterface;

public class LanguageActivity extends Activity {


    Button btHome, btSettings, btFriends, btSpanish, btEnglish, btChinese, btHindi, btPortuguese, btCatalan, btGerman;

    /**
     * The onCreate() method in the LanguageActivity class sets up the language selection functionality for your app. Here's an overview of the code:
     *
     * The method begins by calling the superclass's onCreate() method and setting the layout for the activity using setContentView(R.layout.language_layout).
     *
     * The initElements() method is called to initialize the elements of the activity.
     *
     * Various Button click listeners are set up for the navigation buttons (btHome, btSettings, btFriends) to start corresponding activities when clicked.
     *
     * Individual click listeners are set for each language button (btSpanish, btEnglish, etc.).
     *
     * When a language button is clicked, the corresponding language code is passed to the LanguageManager class's updateResource() method, which updates the app's language resource.
     *
     * After updating the language resource, the recreate() method is called to recreate the activity and apply the language changes.
     *
     * By clicking the language buttons, the user can switch between different supported languages, and the activity will be recreated with the selected language applied.
     *
     * @param savedInstanceState If the activity is being re-initialized after
     *     previously being shut down then this Bundle contains the data it most
     *     recently supplied in {@link #onSaveInstanceState}.  <b><i>Note: Otherwise it is null.</i></b>
     *
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.language_layout);

        initElements();
        LanguageManager lang = new LanguageManager(this);

        btHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LanguageActivity.this, SocialInterface.class));
            }
        });

        btSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LanguageActivity.this, SettingsActivity.class));
            }
        });

        btFriends.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LanguageActivity.this, FriendsActivity.class));
            }
        });

        btSpanish.setOnClickListener(view ->{

            lang.updateResource("es");
            recreate();

        });

        btEnglish.setOnClickListener(view ->{

            lang.updateResource("en");
            recreate();

        });

        btChinese.setOnClickListener(view ->{

            lang.updateResource("zh");
            recreate();

        });

        btHindi.setOnClickListener(view ->{

            lang.updateResource("hi");
            recreate();

        });

        btPortuguese.setOnClickListener(view ->{

            lang.updateResource("pt");
            recreate();

        });

        btGerman.setOnClickListener(view ->{

            lang.updateResource("de");
            recreate();

        });

        btCatalan.setOnClickListener(view ->{

            lang.updateResource("ca");
            recreate();

        });

    }

    public void initElements(){
        btHome = findViewById(R.id.btHome);
        btSettings = findViewById(R.id.btSettings);
        btFriends = findViewById(R.id.btFriends);
        btSpanish = findViewById(R.id.btSpanish);
        btEnglish = findViewById(R.id.btEnglish);
        btChinese = findViewById(R.id.btChinese);
        btHindi = findViewById(R.id.btHindi);
        btPortuguese = findViewById(R.id.btPortuguese);
        btCatalan = findViewById(R.id.btCatalan);
        btGerman = findViewById(R.id.btGerman);
    }
}