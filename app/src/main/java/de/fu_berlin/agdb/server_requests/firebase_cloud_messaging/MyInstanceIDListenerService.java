package de.fu_berlin.agdb.server_requests.firebase_cloud_messaging;

import android.util.Log;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import de.fu_berlin.agdb.authentication.ServerRequests;

/**
 * Created by Riva on 08.06.2016.
 */
public class MyInstanceIDListenerService extends FirebaseInstanceIdService {

    private static final String TAG = "MyFirebaseIIDService";

    /**
     * Called if InstanceID token is updated. This may occur if the security of
     * the previous token had been compromised. Note that this is also called
     * when the InstanceID token is initially generated, so this is where
     * you retrieve the token.
     */
    // [START refresh_token]
    @Override
    public void onTokenRefresh() {
        // Get updated InstanceID token.
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.d(TAG, "Refreshed token: " + refreshedToken);

        updateTokenOnServer(refreshedToken);
    }

    // [END refresh_token]

    /**
     * Persist token to third-party servers.
     *
     * Modify this method to associate the user's FCM InstanceID token with any server-side account
     * maintained by your application.
     *
     * @param token The new token.
     */


    public void updateTokenOnServer(String token) {
        ServerRequests serverRequest = new ServerRequests();
        //serverRequest.tokenRegistrationProcessWithRetrofit(new TokenData(token,"bidding@web.de"));
    }

    public String getToken() {
        return FirebaseInstanceId.getInstance().getToken();
    }

}
