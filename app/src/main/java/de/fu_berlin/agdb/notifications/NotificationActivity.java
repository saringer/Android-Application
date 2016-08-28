package de.fu_berlin.agdb.notifications;


import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AutoCompleteTextView;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import de.fu_berlin.agdb.R;
import de.fu_berlin.agdb.notifications.notification_fragments.CustomizeNotificationFragment;
import de.fu_berlin.agdb.notifications.notification_fragments.MapFragment;
import de.fu_berlin.agdb.notifications.notification_fragments.StarterFragment;
import de.fu_berlin.agdb.data.LocationData;


public class NotificationActivity extends AppCompatActivity implements MapFragment.LocationSelectedListener {
    private MapView mapView;
    public MapboxMap mapboxMap;
    private AutoCompleteTextView autocomplete;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        openOptionsMenu();
        setContentView(R.layout.activity_notification);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Create NotificationActivity");
        // tell Android you want to use your Toolbar as your ActionBar
        setSupportActionBar(toolbar);


        // Get a support ActionBar corresponding to this toolbar
        ActionBar ab = getSupportActionBar();

        // Enable the Up button
        ab.setDisplayHomeAsUpEnabled(true);

        // Check that the activity is using the layout version with
        // the fragment_container FrameLayout
        if (findViewById(R.id.fragment_container) != null) {

            // However, if we're being restored from a previous state,
            // then we don't need to do anything and should return or else
            // we could end up with overlapping fragments.
            if (savedInstanceState != null) {
                return;
            }

            // Create a new Fragment to be placed in the activity layout
            StarterFragment firstFragment = new StarterFragment();

            // In case this activity was started with special instructions from an
            // Intent, pass the Intent's extras to the fragment as arguments
            firstFragment.setArguments(getIntent().getExtras());

            // Add the fragment to the 'fragment_container' FrameLayout
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.fragment_container, firstFragment).commit();



        }




    }








    // Menu icons are inflated just as they were with actionbar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_notification, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onLocationSelected(LocationData result, int alerTypeFlag) {

        CustomizeNotificationFragment customizeNotificationFragment = new CustomizeNotificationFragment();
        customizeNotificationFragment.setAlertTypeFlag(alerTypeFlag);

        // Add the fragment to the 'fragment_container' FrameLayout
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, customizeNotificationFragment).addToBackStack(null).commit();

        customizeNotificationFragment.updateLocation(result);

    }


}
