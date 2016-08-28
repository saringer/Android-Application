package de.fu_berlin.agdb.settings;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.preference.PreferenceManager;
import android.util.DisplayMetrics;
import de.fu_berlin.agdb.adapters.ConstraintsListAdapter;
import de.fu_berlin.agdb.data.Constraints;

import java.util.ArrayList;
import java.util.Locale;

/**
 * Created by Riva on 21.06.2016.
 */
public class Localization {

    private SharedPreferences sharedPreferences;

    private static Localization sInstance;

    // Create a singleton
    public static synchronized Localization getInstance() {
        if (sInstance == null) {
            sInstance = new Localization();
        }
        return sInstance;
    }

    public Localization() {


    }

    public void initLocal(Context context, String lang) {



        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("languagePref", lang);
        editor.commit(); // Very important to save the preference
        /*Locale myLocale = new Locale(lang);
        Resources res = context.getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        conf.locale = myLocale;
        res.updateConfiguration(conf, dm); */


        Locale locale = new Locale(lang);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        context.getResources().updateConfiguration(config, context.getResources().getDisplayMetrics());

    }

    public void setLocal(Context context) {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        String lang = sharedPreferences.getString("languagePref", "default");

        Locale myLocale = new Locale(lang);
        Resources res = context.getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        conf.locale = myLocale;
        res.updateConfiguration(conf, dm);
    }



}
