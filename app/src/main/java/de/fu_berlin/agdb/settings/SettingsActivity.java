package de.fu_berlin.agdb.settings;

import android.content.DialogInterface;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;
import de.fu_berlin.agdb.R;
import org.w3c.dom.Text;

import java.util.Locale;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.util.DisplayMetrics;

public class SettingsActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView tvSelectLanguage;
    private int selected;;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        openOptionsMenu();
        Localization.getInstance().setLocal(getBaseContext());
        setContentView(R.layout.activity_settings);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(getResources().getString(R.string.app_settings));
        // tell Android you want to use your Toolbar as your ActionBar
        setSupportActionBar(toolbar);


        // Get a support ActionBar corresponding to this toolbar
        ActionBar ab = getSupportActionBar();

        // Enable the Up button
        ab.setDisplayHomeAsUpEnabled(true);

        tvSelectLanguage = (TextView) findViewById(R.id.tvSelectLanguage);
        tvSelectLanguage.setOnClickListener(this);
    }

    // Menu icons are inflated just as they were with actionbar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_notification, menu);
        return true;
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        Localization.getInstance().setLocal(getBaseContext());
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.tvSelectLanguage:
                // Strings to Show In Dialog with Radio Buttons

                final CharSequence[] items = {" English ", " German "};

                final AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle(getResources().getString(R.string.settings_choose_language));
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        if (selected == 0) {
                            Localization.getInstance().initLocal(getBaseContext(), "default");
                        }
                        else if (selected == 1) {
                            Localization.getInstance().initLocal(getBaseContext(), "ger");
                        }

                        dialog.dismiss();
                        finish();
                        Intent refresh = new Intent(getBaseContext(), SettingsActivity.class);
                        startActivity(refresh);

                    }

                });
                builder.setNegativeButton(getResources().getString(R.string.button_cancel), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }

                });
                if (getResources().getConfiguration().locale.getDisplayName().equals("ger")) {
                    selected = 1;
                } else {
                    selected = 0;
                }
                builder.setSingleChoiceItems(items, selected, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int item) {


                        switch (item) {
                            case 0:
                                selected = 0;


                                break;
                            case 1:

                                selected = 1;


                                break;
                        }


                    }
                });
                builder.create().show();
        }
    }

    public void setLocale(String lang) {
        Locale myLocale = new Locale(lang);
        Resources res = getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        conf.locale = myLocale;
        res.updateConfiguration(conf, dm);

    }
}
