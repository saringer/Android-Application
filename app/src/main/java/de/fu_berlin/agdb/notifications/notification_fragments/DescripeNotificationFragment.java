package de.fu_berlin.agdb.notifications.notification_fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import com.joanzapata.iconify.IconDrawable;
import com.joanzapata.iconify.fonts.FontAwesomeIcons;
import de.fu_berlin.agdb.R;
import de.fu_berlin.agdb.data.LocationData;
import de.fu_berlin.agdb.data.ProfileData;
import de.fu_berlin.agdb.server_requests.firebase_cloud_messaging.MyInstanceIDListenerService;
import de.fu_berlin.agdb.server_requests.ApiInterface;
import de.fu_berlin.agdb.server_requests.ApiService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Riva on 27.06.2016.
 */

public class DescripeNotificationFragment extends Fragment implements View.OnClickListener {

    private Button submit;
    private Button cancel;
    private LocationData locationData;
    private String rule;
    private MyInstanceIDListenerService listenerService;
    private int numberOfConstraints;
    private EditText description;

    public int getNumberOfConstraints() {
        return numberOfConstraints;
    }

    public void setNumberOfConstraints(int numberOfConstraints) {
        this.numberOfConstraints = numberOfConstraints;
    }

    public LocationData getLocationData() {
        return locationData;
    }

    public void setLocationData(LocationData locationData) {
        this.locationData = locationData;
    }

    public String getRule() {
        return rule;
    }

    public void setRule(String rule) {
        this.rule = rule;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        View v = inflater.inflate(R.layout.fragment_finalize, container, false);


        submit = (Button) v.findViewById(R.id.submit);
        submit.setOnClickListener(this);

        description = (EditText) v.findViewById(R.id.profileDescription);
        description.setOnFocusChangeListener(new View.OnFocusChangeListener(){
            public void onFocusChange(View v, boolean hasFocus){
                if(hasFocus)
                    description.setHint("");
                else
                    description.setHint("Set up a description for your profile. You will receive a " +
                            "NotificationActivity which includes your description as soon as your rule got triggered!t");
            }
        });

        return v;

    }

    @Override
    public void onResume() {
        super.onResume();


    }

    private ActionBar getActionBar() {
        return ((AppCompatActivity) getActivity()).getSupportActionBar();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


        // Set action bar title
        getActionBar().setTitle(" Describtion");
        getActionBar().setIcon(
                new IconDrawable(getActivity(), FontAwesomeIcons.fa_edit)
                        .colorRes(R.color.actionBarColor)
                        .actionBarSize());


    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {


            case R.id.cancel:
                getFragmentManager().popBackStackImmediate();
                break;

            case R.id.submit:

                ProfileData profile = new ProfileData();
                profile.setLatitude(getLocationData().getLatitude());
                profile.setLongitude(getLocationData().getLongitude());
                profile.setProfileDescription("Wunderschönes Profil");
                profile.setFirebaseToken(getMyInstanceIDListenerService().getToken());

                profile.setDsl(getRule());
                profile.setNumberOfConstraints(getNumberOfConstraints());


                ApiInterface mApiService = ApiService.getInstance().getInterfaceService();
                Call<ProfileData> call = mApiService.createProfile(profile);
                call.enqueue(new Callback<ProfileData>() {
                    @Override
                    public void onResponse(Call<ProfileData> call, Response<ProfileData> response) {

                    }

                    @Override
                    public void onFailure(Call<ProfileData> call, Throwable t) {
                        System.out.println(t);
                    }

                });

                break;


        }


    }

    private MyInstanceIDListenerService getMyInstanceIDListenerService() {
        if (listenerService == null) {
            listenerService = new MyInstanceIDListenerService();
            return listenerService;
        } else {
            return listenerService;
        }
    }

}
