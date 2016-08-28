package de.fu_berlin.agdb.server_requests.firebase_cloud_messaging;

/**
 * Created by Riva on 08.06.2016.
 */

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import de.fu_berlin.agdb.R;
import de.fu_berlin.agdb.data.Constants;

/**
 * This Activity is displayed when users click the notification itself. It provides
 * UI for snoozing and dismissing the notification.
 */
public class PushNotificationActivity extends Activity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_push_notification);
        String message = getIntent().getStringExtra(Constants.EXTRA_MESSAGE);
        TextView text = (TextView) findViewById(R.id.result_message);

    }
    public void onSnoozeClick(View v) {
        this.finish();
    }
    public void onDismissClick(View v) {
       this.finish();
    }
}
