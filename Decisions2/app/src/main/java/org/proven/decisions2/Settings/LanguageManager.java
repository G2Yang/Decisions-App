package org.proven.decisions2.Settings;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;

import java.util.Locale;

public class LanguageManager
{

    //"ct" is short for "Context" and refers to an Android context object. A context in Android provides access to system resources and allows you to perform actions within an app such as starting an activity or creating a view. The "ct" variable is used in this class to access the app's resources and configuration.
    private Context ct;

    //"sharedPreferences" is an Android class that provides a way to store and retrieve key-value pairs of data. It is typically used to store app settings or user preferences. In this class, the "sharedPreferences" variable is used to store and retrieve the app's language preference, using a key-value pair with the key "lang". The "MODE_PRIVATE" parameter in the "getSharedPreferences" method indicates that the preferences should only be accessible within the app and not by other apps or users.
    private SharedPreferences sharedPreferences;

    /**
     * The LanguageManager class is responsible for managing the language settings in your app. Let's go through the code snippet you provided:
     *
     * The constructor public LanguageManager(Context ctx) is defined. It takes a Context object as a parameter, which is used to access resources and system services.
     *
     * Inside the constructor, the ct variable is assigned the value of the passed Context object (ctx).
     *
     * The sharedPreferences variable is initialized using the getSharedPreferences() method of the Context object.
     * It creates or retrieves a shared preferences file named "LANG" with a private mode.
     * This shared preferences file will be used to store and retrieve the language preference for the app.
     * @param ctx
     */
    public LanguageManager(Context ctx){
        // Set the ct variable to the value passed in
        ct=ctx;
        // Initialize the sharedPreferences variable with a "LANG" key and a private mode
        sharedPreferences = ct.getSharedPreferences("LANG", Context.MODE_PRIVATE);
    }

    /**
     * The updateResource method in the LanguageManager class is responsible for updating the app's language resources based on the provided language code. Here's a breakdown of the method:
     *
     * It takes a code parameter, which represents the language code for the desired language.
     *
     * A new Locale object is created using the provided language code.
     *
     * The default Locale is set to the new Locale object using Locale.setDefault(locale). This ensures that any subsequent resource retrieval will use the updated locale.
     *
     * The app's resources are obtained using ct.getResources().
     *
     * The app's configuration is obtained using resources.getConfiguration().
     *
     * The app's locale configuration is updated to the new Locale object by assigning it to configuration.locale.
     *
     * The app's resources configuration is updated with the new configuration using resources.updateConfiguration(configuration, resources.getDisplayMetrics()).
     * This ensures that the app's resources are refreshed with the new language configuration.
     *
     * The setLang method (not shown in the provided code snippet) is called to save the current language code in shared preferences.
     * @param code
     */
    public void updateResource(String code){
        // Create a new Locale object with the provided code
        Locale locale = new Locale(code);
        // Set the default Locale to the new Locale object
        Locale.setDefault(locale);
        // Get the app's resources
        Resources resources = ct.getResources();
        // Get the app's configuration
        Configuration configuration = resources.getConfiguration();
        // Set the app's locale configuration to the new Locale object
        configuration.locale = locale;
        // Update the app's resources configuration with the new configuration
        resources.updateConfiguration(configuration, resources.getDisplayMetrics());
        // Set the current language code in shared preferences
        setLang(code);
    }

    /**
     * The getLang method in the LanguageManager class is responsible for retrieving the currently stored language code from shared preferences. Here's an explanation of the method:
     *
     * It retrieves the value stored in shared preferences under the key "lang" using sharedPreferences.getString("lang", "en").
     * If a value is found under the "lang" key, it is returned as the current language code.
     * If no value is found (i.e., the key does not exist), the default value "en" is returned, indicating English as the default language.
     * @return
     */
    public String getLang(){
        // Return the value stored in shared preferences under the "lang" key or "en" if it doesn't exist
        return sharedPreferences.getString("lang", "en");
    }

    /**
     * The setLang method in the LanguageManager class is responsible for setting the language code in shared preferences. Here's an explanation of the method:
     *
     * It creates an editor object for the shared preferences using sharedPreferences.edit().
     *
     * It puts the new language code in the editor using editor.putString("lang", code), where code is the language code provided as an argument to the method.
     *
     * It commits the changes to the shared preferences using editor.commit().
     * @param code
     */
    public void setLang(String code){
        // Create a new editor for the shared preferences object
        SharedPreferences.Editor editor = sharedPreferences.edit();
        // Put the new language code in the editor
        editor.putString("lang", code);
        // Commit the changes to shared preferences
        editor.commit();
    }
}