package de.fu_berlin.agdb.authentication;

import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.joanzapata.iconify.Iconify;
import com.joanzapata.iconify.fonts.FontAwesomeModule;
import de.fu_berlin.agdb.R;
import de.fu_berlin.agdb.data.User;
import de.fu_berlin.agdb.notifications.NotificationActivity;
import de.fu_berlin.agdb.settings.Localization;
import de.fu_berlin.agdb.settings.SettingsActivity;

import static de.fu_berlin.agdb.data.Constants.PLAY_SERVICES_RESOLUTION_REQUEST;

//import de.fu_berlin.agdb.activities.NotificationActivity;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    UserLocalStore userLocalStore;
    EditText etName, etAge, etUsername;
    Button bLogout;
    Button bNotification;
    Button bSettings;
    Button bAbout;
    Button bTest;



    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        // Enabel Font Awesome Icons
        // https://github.com/JoanZapata/android-iconify/
        Iconify.with(new FontAwesomeModule());

        // Check currently selected language
        Localization.getInstance().setLocal(getBaseContext());

        setContentView(R.layout.activity_main);

        if (!checkPlayServices()) {
            Toast.makeText(MainActivity.this, "This application needs Google Play services", Toast.LENGTH_LONG).show();
            return;
        }


        bLogout = (Button) findViewById(R.id.bLogout);
        bNotification = (Button) findViewById(R.id.bNotification);
        bSettings = (Button) findViewById(R.id.bSettings);
        bAbout  = (Button) findViewById(R.id.bAbout);

        Typeface typeface = Typeface.createFromAsset(getAssets(), "fonts/IDroid.otf");

        bLogout.setTypeface(typeface);
        bNotification.setTypeface(typeface);
        bAbout.setTypeface(typeface);


        bSettings.setTypeface(typeface);






        /*WebView forecastWebview = (WebView) findViewById(R.id.forecastWebview);
        forecastWebview.getSettings().setJavaScriptEnabled(true);
        String html = "<iframe id=\"forecast_embed\" type=\"text/html\" frameborder=\"0\" " +
                "height=\"245\" width=\"100%\" " +
                "src=\"http://forecast.io/embed/#lat=42.3583&lon=-71.0603&name=Downtown Boston&color=#00aaff&font=Georgia&units=ca\"\"> </iframe>";
        forecastWebview.loadData(html, "text/html", null); */

        bLogout.setOnClickListener(this);
        bNotification.setOnClickListener(this);
        bSettings.setOnClickListener(this);


        userLocalStore = new UserLocalStore(this);


    }

    @Override
    protected void onResume() {
        super.onResume();
        // Recreate if Locale got changed


        checkPlayServices();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        Localization.getInstance().setLocal(getBaseContext());
    }




    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bLogout:
                userLocalStore.clearUserData();
                userLocalStore.setUserLoggedIn(false);
                Intent loginIntent = new Intent(this, LoginActivity.class);
                startActivity(loginIntent);
                break;
            case R.id.bNotification:
                Intent notificationIntent = new Intent(this, NotificationActivity.class);
                startActivity(notificationIntent);
                break;
            case R.id.bSettings:
                Intent settingsIntent = new Intent(this, SettingsActivity.class);
                startActivity(settingsIntent);
                break;
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (authenticate() == true) {
            displayUserDetails();
        }
    }

    private boolean authenticate() {
        if (userLocalStore.getLoggedInUser() == null) {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            return false;
        }
        return true;
    }

    private void displayUserDetails() {
        User user = userLocalStore.getLoggedInUser();

    }

    private boolean checkPlayServices() {
        GoogleApiAvailability googleAPI = GoogleApiAvailability.getInstance();
        int result = googleAPI.isGooglePlayServicesAvailable(this);
        if (result != ConnectionResult.SUCCESS) {
            if (googleAPI.isUserResolvableError(result)) {
                googleAPI.getErrorDialog(this, result,
                        PLAY_SERVICES_RESOLUTION_REQUEST).show();
            }

            return false;
        }

        return true;
    }

}
