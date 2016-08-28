package de.fu_berlin.agdb.server_requests;

/**
 * Created by Riva on 31.05.2016.
 */


import de.fu_berlin.agdb.data.User;
import de.fu_berlin.agdb.data.ProfileData;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

import java.util.List;


public interface ApiInterface {
    @POST("users/newUser")
    Call<ResponseBody> createUser(@Body User user);

    @GET("users/auth/{email}/{password}")
    Call<User> authenticate(@Path("email") String email, @Path("password") String password);

    @POST("android_profiles/new")
    Call<ProfileData> createProfile(@Body ProfileData profile);

    @POST("android_profiles/delete/{profileID}")
    Call<ResponseBody> deleteProfile(@Path("profileID") long profileID);

    @GET("android_profiles/get_profiles/{userEmail}")
    Call<List<ProfileData>> getProfilesForUser(@Path("userEmail") String userEmail);


}
