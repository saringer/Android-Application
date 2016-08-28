package de.fu_berlin.agdb.authentication;

import android.app.ProgressDialog;
import android.content.Intent;
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
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {
    EditText etEmail, etUsername, etPassword;
    Button bRegister;
    TextView tvLoginLink;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        etEmail = (EditText) findViewById(R.id.etEmail);
        etUsername = (EditText) findViewById(R.id.etUsername);
        etPassword = (EditText) findViewById(R.id.etPassword);
        bRegister = (Button) findViewById(R.id.bRegister);
        tvLoginLink = (TextView) findViewById(R.id.tvToLogin);

        bRegister.setOnClickListener(this);
        tvLoginLink.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bRegister:

                String email = etEmail.getText().toString();
                String username = etUsername.getText().toString();
                String password = etPassword.getText().toString();

                if (TextUtils.isEmpty(email)) {
                    etEmail.setError("Field \"Email\" cant be empty");
                } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    etEmail.setError("Please enter valid email address");
                } else if (TextUtils.isEmpty(username)) {
                    etUsername.setError("Field \"Username\" cant be empty");
                } else if (TextUtils.isEmpty(password)) {
                    etPassword.setError("Field \"Password\" cant be empty");
                } else {
                    User user = new User(email, username, password);
                    registerUser(user);
                }
                break;

            case R.id.tvToLogin:

                Intent registerIntent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(registerIntent);
                break;
        }
    }

    private void registerUser(User user) {

        userRegistrationProcessWithRetrofit(user);
    }




    public void userRegistrationProcessWithRetrofit(User user) {

        ApiInterface mApiService = ApiService.getInstance().getInterfaceService();
        Call<ResponseBody> call = mApiService.createUser(user);
        call.enqueue(new Callback<ResponseBody>() {

            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccess()) {
                    // Registration successful
                    ProgressDialog progressDialog = new ProgressDialog(RegisterActivity.this);
                    progressDialog.show();
                    Toast.makeText(getBaseContext(), "Your registration was successful", Toast.LENGTH_SHORT).show();
                    Intent loginIntent = new Intent(getBaseContext(), LoginActivity.class);
                    startActivity(loginIntent);
                    progressDialog.dismiss();


                } else {
                    // There is already an account with given email
                    if (response.code() == 409) {
                        Toast.makeText(getBaseContext(), "The given email is already in use", Toast.LENGTH_SHORT).show();

                    } else {
                        // Registration failed
                        Log.d("error message: ", response.message());
                        Toast.makeText(getBaseContext(), "Registration failed", Toast.LENGTH_SHORT).show();

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

