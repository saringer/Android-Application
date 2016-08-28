package de.fu_berlin.agdb.adapters;

import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;
import de.fu_berlin.agdb.R;
import de.fu_berlin.agdb.data.ProfileData;
import de.fu_berlin.agdb.server_requests.ApiInterface;
import de.fu_berlin.agdb.server_requests.ApiService;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.util.ArrayList;

/**
 * Created by Riva on 01.06.2016.
 */
public class ProfilesListAdapter extends ArrayAdapter<ProfileData> {

    Context mContext;

    public ProfilesListAdapter(Context context, ArrayList<ProfileData> profileData) {

        super(context, 0, profileData);
        mContext = context;
    }


    // Set false in order to disable the item being clickable at whole
    @Override
    public boolean isEnabled(int position) {
        return false;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        // Get the data item for this position

        final ProfileData profileData = getItem(position);

        // Check if an existing view is being reused, otherwise inflate the view

        if (convertView == null) {

            convertView = LayoutInflater.from(getContext()).inflate(R.layout.profiles_list_item, parent, false);

        }

        // Lookup view for data population

        // Display profile description
        TextView profileDescription = (TextView) convertView.findViewById(R.id.profileDescription);
        ///profileDescription.setText(profileData.getProfileDescription());
        profileDescription.setText(profileData.getProfileDescription());
        TextView deleteProfile = (TextView) convertView.findViewById(R.id.deleteProfile);

        // Dispay button(clickable textview) to delete profile
        TextView deleteButton = (TextView) convertView.findViewById(R.id.deleteProfile);
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //do something
                deleteProfileProcessWithRetrofit(profileData.getId());
                remove(profileData);
                notifyDataSetChanged();

            }
        });


        return convertView;

    }

    public void deleteProfileProcessWithRetrofit(Long profileID) {
        final ProgressDialog progressDialog = new ProgressDialog(mContext);
        ApiInterface mApiService = ApiService.getInstance().getInterfaceService();
        Call<ResponseBody> call = mApiService.deleteProfile(profileID);
        call.enqueue(new Callback<ResponseBody>() {

            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccess()) {
                    progressDialog.show();
                    // Registration successful
                    Toast.makeText(mContext, "Your Alarm was successfully deleted", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();

                } else {


                        Toast.makeText(mContext, "Your alarm couldnt be deleted", Toast.LENGTH_SHORT).show();
                    Log.d("error message: ", response.message());




                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

                Log.d("Error: ", t.getMessage());
            }

        });

    }
}
