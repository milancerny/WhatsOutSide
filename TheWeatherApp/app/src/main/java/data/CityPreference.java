package data;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.SharedPreferences;

/**
 * Created by mcernyux on 15.12.2016.
 */

public class CityPreference {

    SharedPreferences prefs;

    public CityPreference(Activity activity) {
        prefs = activity.getPreferences(Activity.MODE_PRIVATE);
    }

    public String getCity() {
        return prefs.getString("city", "Bratislava,sk"); //default city
    }

    public void setCity(String city) {
        prefs.edit().putString("city", city).commit();

    }
}
