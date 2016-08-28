package de.fu_berlin.agdb.authentication;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;
import de.fu_berlin.agdb.data.User;
import de.fu_berlin.agdb.server_requests.ApiInterface;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static de.fu_berlin.agdb.data.Constants.BASE_URL;


public class ServerRequests {
    ProgressDialog progressDialog;
    public static final int CONNECTION_TIMEOUT = 1000 * 15;
    Context context;
    //public static final String SERVER_ADDRESS = "http://localhost:8060/";

    public ServerRequests() {

    }

    public ServerRequests(Context context) {
        this.context = context;
        progressDialog = new ProgressDialog(context);
        progressDialog.setCancelable(false);
        progressDialog.setTitle("Processing...");
        progressDialog.setMessage("Please wait...");
    }


    private ApiInterface getInterfaceService() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        final ApiInterface mInterfaceService = retrofit.create(ApiInterface.class);
        return mInterfaceService;
    }


    public void userRegistrationProcessWithRetrofit(User user) {

        ApiInterface mApiService = this.getInterfaceService();
        Call<ResponseBody> call = mApiService.createUser(user);
        call.enqueue(new Callback<ResponseBody>() {

            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccess()) {
                    // Registration successful
                    progressDialog.show();
                    Toast.makeText(context, "Your registration was successful", Toast.LENGTH_SHORT).show();
                    Intent loginIntent = new Intent(context, LoginActivity.class);
                    context.startActivity(loginIntent);
                    progressDialog.dismiss();


                } else {
                    // There is already an account with given email
                    if (response.code() == 409) {
                        Toast.makeText(context, "The given email is already in use", Toast.LENGTH_SHORT).show();

                    } else {
                        // Registration failed
                        Log.d("error message: ", response.message());
                        Toast.makeText(context, "Registration failed", Toast.LENGTH_SHORT).show();

                    }


                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

                Log.d("Error", t.getMessage());

            }

        });


    }


}