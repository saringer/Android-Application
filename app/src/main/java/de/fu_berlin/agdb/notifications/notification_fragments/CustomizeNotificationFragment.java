package de.fu_berlin.agdb.notifications.notification_fragments;


import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.joanzapata.iconify.IconDrawable;
import com.joanzapata.iconify.fonts.FontAwesomeIcons;
import de.fu_berlin.agdb.R;
import de.fu_berlin.agdb.adapters.ConstraintsListAdapter;
import de.fu_berlin.agdb.adapters.DslAdapter;
import de.fu_berlin.agdb.data.Constraints;
import de.fu_berlin.agdb.data.LocationData;

import java.util.ArrayList;

/**
 * Created by Riva on 16.05.2016.
 */
public class CustomizeNotificationFragment extends ListFragment implements View.OnClickListener {

    private LocationData result;
    private Button plusButton;
    private Button createButton;
    private ConstraintsListAdapter adapter;
    private String selected = "temperature";

    private int alertTypeFlag;
    private View v;
    private ArrayList<Constraints> arrayOfConstraints;
    private static final String STATE_ITEMS = "items";
    private Bundle savedInstanceState;

    public int getAlertTypeFlag() {
        return alertTypeFlag;
    }

    public void setAlertTypeFlag(int alertTypeFlag) {
        this.alertTypeFlag = alertTypeFlag;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        this.v = inflater.inflate(R.layout.fragment_customize_notification, container, false);


        plusButton = (Button) v.findViewById(R.id.plusButton);
        plusButton.setOnClickListener(this);


        createButton = (Button) v.findViewById(R.id.createButton);
        createButton.setOnClickListener(this);

        // If we have a saved state then we can restore it now
        if (savedInstanceState != null) {
            result = (LocationData) savedInstanceState.getSerializable("Old");
            savedInstanceState.remove("Old");

        }


        constructListViewDataSource();

        return v;

    }

    public void onDestroy() {


        super.onDestroy();
        ConstraintsListAdapter constraintsListAdapter = ConstraintsListAdapter.getInstance(getActivity(), arrayOfConstraints);
        constraintsListAdapter.clear();


    }


    private ActionBar getActionBar() {
        return ((AppCompatActivity) getActivity()).getSupportActionBar();
    }

    // Construct data source for list view
    private void constructListViewDataSource() {


        // Create the adapter to convert the array to views
        arrayOfConstraints = new ArrayList<Constraints>();

        ConstraintsListAdapter constraintsListAdapter = ConstraintsListAdapter.getInstance(getActivity(), arrayOfConstraints);
        adapter = constraintsListAdapter;


    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        // Make sure to call the super method so that the states of our views are saved

        // Save our own state now
        outState.putSerializable("Old", result);

        super.onSaveInstanceState(outState);

    }


    // Sets the currently selected Location in a Textview
    public void updateLocation(LocationData result) {
        this.result = result;
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


        // Attach the adapter to a ListView
        setListAdapter(adapter);
        // Set action bar title
        getActionBar().setTitle(" Customize");
        getActionBar().setIcon(
                new IconDrawable(getActivity(), FontAwesomeIcons.fa_gears)
                        .colorRes(R.color.actionBarColor)
                        .actionBarSize());


    }






    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.plusButton:

                // Strings to Show In Dialog with Radio Buttons
                final CharSequence[] items = {" Temperature ", " Precipitation ", " Wind ", " Frost "};


                // Creating and Building the Dialog
                final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("Add another rule to your notification for:");
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        // set selected to default
                        selected = "temperature";
                    }
                });
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        Constraints constraint = new Constraints(getActivity(), selected, getAlertTypeFlag());
                        // Add item to adapter
                        adapter.add(constraint);
                        // set selected to default
                        selected = "temperature";
                        dialog.dismiss();
                    }

                });
                builder.setSingleChoiceItems(items, 0, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int item) {


                        switch (item) {
                            case 0:
                                selected = "temperature";
                                break;
                            case 1:
                                selected = "precipitation";
                                break;

                            case 2:
                                selected = "windspeed";
                                break;
                            case 3:
                                selected = "frost";
                                break;
                            default:
                                selected = "temperature";
                                break;

                        }

                    }
                });

                builder.create().show();


                break;

            case R.id.createButton:

                DescripeNotificationFragment descripeNotificationFragment = new DescripeNotificationFragment();
                descripeNotificationFragment.setLocationData(result);
                descripeNotificationFragment.setRule(new DslAdapter(result.getLatitude() + result.getLongitude(), getAlertTypeFlag()).listOfConstraintsToDSL(this.adapter));
                descripeNotificationFragment.setNumberOfConstraints(this.adapter.getCount());
                // Add the fragment to the 'fragment_container' FrameLayout
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, descripeNotificationFragment).addToBackStack(null).commit();


                break;

        }

    }

}
