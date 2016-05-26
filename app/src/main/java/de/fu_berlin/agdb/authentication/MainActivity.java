package de.fu_berlin.agdb.authentication;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import de.fu_berlin.agdb.R;
import de.fu_berlin.agdb.activities.Notification;

//import de.fu_berlin.agdb.activities.Notification;


public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    UserLocalStore userLocalStore;
    EditText etName, etAge, etUsername;
    Button bLogout;
    Button bNotification;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etUsername = (EditText) findViewById(R.id.etUsername);
        etName = (EditText) findViewById(R.id.etName);
        etAge = (EditText) findViewById(R.id.etAge);
        bLogout = (Button) findViewById(R.id.bLogout);
        bNotification = (Button) findViewById(R.id.bNotification);

        bLogout.setOnClickListener(this);
        bNotification.setOnClickListener(this);

        userLocalStore = new UserLocalStore(this);
    }




    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.bLogout:
                userLocalStore.clearUserData();
                userLocalStore.setUserLoggedIn(false);
                Intent loginIntent = new Intent(this, Login.class);
                startActivity(loginIntent);
                break;
            case R.id.bNotification:
                Intent NotificationIntent = new Intent(this, Notification.class);
                startActivity(NotificationIntent);
                break;
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (authenticate() == true) {
            displayUserDetails();
        }
    }

    private boolean authenticate() {
        if (userLocalStore.getLoggedInUser() == null) {
            Intent intent = new Intent(this, Login.class);
            startActivity(intent);
            return false;
        }
        return true;
    }

    private void displayUserDetails() {
        User user = userLocalStore.getLoggedInUser();
        etUsername.setText(user.username);
        etName.setText(user.name);
        etAge.setText(user.age + "");
    }
}
