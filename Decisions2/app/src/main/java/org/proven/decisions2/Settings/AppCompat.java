package org.proven.decisions2.Settings;

import android.app.Activity;
import android.os.Bundle;

import androidx.annotation.Nullable;

public class AppCompat extends Activity {

    // This method is the entry point for the Activity lifecycle and is called when the Activity is first created.
    // It overrides the default implementation of the method and provides a custom implementation to set up the Activity.
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        // Call the default implementation of the method to ensure that the Activity is properly initialized.
        super.onCreate(savedInstanceState);
        // Create a new instance of the LanguageManager class and pass in the current Activity as a parameter.
        LanguageManager languageManager = new LanguageManager(this);

         // Call the updateResource method of the LanguageManager instance to update the language resources.
        // Pass in the current language as a parameter by calling the getLang method of the LanguageManager instance.
        languageManager.updateResource(languageManager.getLang());
    }
}
