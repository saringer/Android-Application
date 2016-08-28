package de.fu_berlin.agdb.notifications.notification_fragments;


import android.content.DialogInterface;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import com.joanzapata.iconify.IconDrawable;
import com.joanzapata.iconify.fonts.FontAwesomeIcons;
import de.fu_berlin.agdb.R;
import de.fu_berlin.agdb.notifications.NotificationActivity;
import de.fu_berlin.agdb.adapters.ProfilesListAdapter;
import de.fu_berlin.agdb.data.ProfileData;
import de.fu_berlin.agdb.server_requests.ApiInterface;
import de.fu_berlin.agdb.server_requests.ApiService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.util.ArrayList;
import java.util.List;

import static de.fu_berlin.agdb.data.Constants.CUSTOM_ALERT;
import static de.fu_berlin.agdb.data.Constants.IMMEDIATE_ALERT;

/**
 * Created by Riva on 23.05.2016.
 */
public class StarterFragment extends ListFragment implements View.OnClickListener {

    Button newNotificationButton;
    Button customAlertButton;
    Button immediateAlertButton;
    ProfilesListAdapter adapter;
    View dialogView;
    AlertDialog.Builder builder;
    AlertDialog alertDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_create_notification, container, false);

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout


        newNotificationButton = (Button) v.findViewById(R.id.newNotification);
        newNotificationButton.setOnClickListener(this);


        // Set up Dialog Builder
        builder = new AlertDialog.Builder(getActivity());


        // Construct the data source

        ArrayList<ProfileData> arrayOfProfiles = new ArrayList<ProfileData>();

        // Create the adapter to convert the array to views

        adapter = new ProfilesListAdapter(getActivity(), arrayOfProfiles);


        return v;


    }

    // Check for device rotation
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        // Checks the orientation of the screen
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            LinearLayout layout = (LinearLayout) dialogView.findViewById(R.id.dialogAddAlertOrientationLayout);
            layout.setOrientation(LinearLayout.HORIZONTAL);
        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            LinearLayout layout = (LinearLayout) dialogView.findViewById(R.id.dialogAddAlertOrientationLayout);
            layout.setOrientation(LinearLayout.VERTICAL);
        }
    }


    @Override
    public void onClick(View v) {
        NotificationActivity activity = (NotificationActivity) getActivity();
        switch (v.getId()) {
            case R.id.newNotification:

                if (alertDialog == null) {
                    LayoutInflater inflater = getActivity().getLayoutInflater();
                    dialogView = inflater.inflate(R.layout.dialog_add_alert, null);
                    customAlertButton = (Button) dialogView.findViewById(R.id.customAlert);
                    customAlertButton.setOnClickListener(this);
                    immediateAlertButton = (Button) dialogView.findViewById(R.id.immediateAlert);
                    immediateAlertButton.setOnClickListener(this);
                    builder.setView(dialogView);
                    alertDialog = builder.create();
                    alertDialog.show();
                    alertDialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
                        @Override
                        public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                            if(keyCode==KeyEvent.KEYCODE_BACK && !event.isCanceled()) {
                                if(alertDialog.isShowing()) {
                                    alertDialog.dismiss();
                                    alertDialog = null;
                                }
                                return true;
                            }
                            return false;
                        }
                    });

                }

                break;


            case R.id.customAlert:


                MapFragment mapFragment = new MapFragment();
                mapFragment.setAlertTypeFlag(CUSTOM_ALERT);
                // Add the fragment to the 'fragment_container' FrameLayout
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, mapFragment).addToBackStack(null).commit();
                alertDialog.dismiss();
                alertDialog = null;


                break;

            case R.id.immediateAlert:


                mapFragment = new MapFragment();
                mapFragment.setAlertTypeFlag(IMMEDIATE_ALERT);
                // Add the fragment to the 'fragment_container' FrameLayout
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, mapFragment).addToBackStack(null).commit();
                alertDialog.dismiss();
                alertDialog = null;


                break;


            default:

                break;


        }


    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


        // Attach the adapter to a ListView
        setListAdapter(adapter);
        loadProfiles();
        //getListView().setOnItemClickListener(this);
    }

    @Override
    public void onResume() {
        getActionBar().setTitle(" Notifications");
        getActionBar().setIcon(
                new IconDrawable(getActivity(), FontAwesomeIcons.fa_commenting_o)
                        .colorRes(R.color.actionBarColor)
                        .actionBarSize());


        super.onResume();
    }

    private ActionBar getActionBar() {
        return ((AppCompatActivity) getActivity()).getSupportActionBar();
    }




    private void loadProfiles() {
        ApiInterface mApiService = ApiService.getInstance().getInterfaceService();
        Call<List<ProfileData>> call = mApiService.getProfilesForUser("");
        call.enqueue(new Callback<List<ProfileData>>() {
            @Override
            public void onResponse(Call<List<ProfileData>> call, Response<List<ProfileData>> response) {
                for (int i = 0; i < response.body().size(); i++) {

                    adapter.add(response.body().get(i));
                }
            }

            @Override
            public void onFailure(Call<List<ProfileData>> call, Throwable t) {
                Log.d("Error", t.getMessage());
            }
        });

    }
}
