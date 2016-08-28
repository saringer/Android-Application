package de.fu_berlin.agdb.authentication;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import de.fu_berlin.agdb.R;
import de.fu_berlin.agdb.data.User;
import de.fu_berlin.agdb.server_requests.ApiInterface;
import de.fu_berlin.agdb.server_requests.ApiService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    Button bLogin;
    TextView registerLink;
    EditText etEmail, etPassword;

    UserLocalStore userLocalStore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        TextView welcomeText = (TextView) findViewById(R.id.welcomeText);

        Typeface typeface = Typeface.createFromAsset(getAssets(), "fonts/IDroid.otf");
        // Set a text to the TextView
        welcomeText.setText("IRIS Weather");
        // Set the typeface
        welcomeText.setTypeface(typeface);

        bLogin = (Button) findViewById(R.id.bLogin);
        etEmail = (EditText) findViewById(R.id.email);
        etPassword = (EditText) findViewById(R.id.password);
        registerLink = (TextView) findViewById(R.id.tvRegisterLink);

        bLogin.setOnClickListener(this);
        registerLink.setOnClickListener(this);

        userLocalStore = new UserLocalStore(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bLogin:

                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                userLocalStore.setUserLoggedIn(true);
                startActivity(intent);
                break;

                /*String email = this.etEmail.getText().toString();
                String password = this.etPassword.getText().toString();

                if (TextUtils.isEmpty(email)) {
                    etEmail.setError("Field \"Email\" cannot be empty");
                } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    etEmail.setError("Please enter valid email address");
                } else if (TextUtils.isEmpty(password)) {
                    etPassword.setError("Field \"Password\" cannot be empty");
                } else {
                    userAuthenticationProcessWithRetrofit(email, password);
                }


                break;    */
            case R.id.tvRegisterLink:
                Intent registerIntent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(registerIntent);
                break;
        }
    }


    private void showErrorMessage() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(LoginActivity.this);
        dialogBuilder.setMessage("Incorrect user details");
        dialogBuilder.setPositiveButton("Ok", null);
        dialogBuilder.show();
    }

    private void logUserIn(User returnedUser) {
        userLocalStore.storeUserData(returnedUser);
        userLocalStore.setUserLoggedIn(true);
        startActivity(new Intent(this, MainActivity.class));
    }


    /**
     * @param email
     * @param password
     */
    public void userAuthenticationProcessWithRetrofit(String email, String password) {
        final ProgressDialog progressDialog = new ProgressDialog(LoginActivity.this);
        ApiInterface mApiService = ApiService.getInstance().getInterfaceService();
        Call<User> call = mApiService.authenticate(email, password);
        call.enqueue(new Callback<User>() {

            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccess()) {
                    progressDialog.show();
                    // Registration successful
                    if (response.body() != null) {

                        logUserIn(response.body());
                    }
                    progressDialog.dismiss();

                } else {

                    if (response.code() == 404)
                        Toast.makeText(getApplication(), "No account registered for the given email", Toast.LENGTH_SHORT).show();
                    Log.d("error message: ", response.message());

                    if (response.code() == 401)
                        Toast.makeText(getApplication(), "Wrong password, try again", Toast.LENGTH_SHORT).show();
                    Log.d("error message: ", response.message());


                }

            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {

                Log.d("Error", t.getMessage());
            }

        });

    }

}
