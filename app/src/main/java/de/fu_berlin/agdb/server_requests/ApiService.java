package de.fu_berlin.agdb.server_requests;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static de.fu_berlin.agdb.data.Constants.BASE_URL;

/**
 * Created by Riva on 27.06.2016.
 */
public class ApiService {

    private static ApiService sInstance;
    private ApiInterface apiInterface;

    public ApiService() {
                this.apiInterface = setInterfaceService();
    }

    // Create a singleton in order to share the instance
    public static synchronized ApiService getInstance() {
        if (sInstance == null) {
            sInstance = new ApiService();
        }
        return sInstance;
    }

    private ApiInterface setInterfaceService() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        final ApiInterface mInterfaceService = retrofit.create(ApiInterface.class);
        return mInterfaceService;
    }

    public ApiInterface getInterfaceService() {
        return this.apiInterface;
    }


}
