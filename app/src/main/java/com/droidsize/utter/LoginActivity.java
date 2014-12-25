package com.droidsize.utter;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.facebook.Request;
import com.facebook.Response;
import com.facebook.model.GraphUser;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseFacebookUtils;
import com.parse.ParseUser;

import de.keyboardsurfer.android.widget.crouton.Crouton;
import de.keyboardsurfer.android.widget.crouton.Style;


public class LoginActivity extends Activity {

    protected TextView mForgotPasswordView;
    protected TextView mSignUpButton;
    protected EditText mUserName;
    protected EditText mPassword;
    protected Button mFbLoginButton;

    protected MenuItem mLoginMenuButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        mForgotPasswordView = (TextView) findViewById(R.id.forgotPassword);
        mUserName = (EditText) findViewById(R.id.usernameField);
        mPassword = (EditText) findViewById(R.id.passwordField);
        mFbLoginButton = (Button) findViewById(R.id.fbLoginButton);
        mSignUpButton = (TextView) findViewById(R.id.signupButton);


        mForgotPasswordView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
                startActivity(intent);
            }
        });

        mSignUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
                startActivity(intent);
            }
        });

        mFbLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ParseFacebookUtils.logIn(LoginActivity.this, 10, new LogInCallback() {
                    @Override
                    public void done(ParseUser parseUser, ParseException e) {

                        if (parseUser == null) {
                            Crouton.makeText(LoginActivity.this, "You cancelled the login",
                                    Style.ALERT).show();
                        } else if (parseUser.isNew()) {
                            getFacebookIdInBackground();
                            Log.d("Utter", "User signed up and logged in through Facebook!");
                            goToMainActivity();
                        } else {
                            Log.d("MyApp", "User logged in through Facebook!");
                            goToMainActivity();
                        }
                    }
                });
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.login, menu);
        mLoginMenuButton = menu.getItem(0);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.login_menu_button) {
            String username = mUserName.getText().toString().trim();
            String password = mPassword.getText().toString().trim();


            if (username.isEmpty() || password.isEmpty()) {
                Crouton.makeText(LoginActivity.this, R.string.signup_error_message,
                        Style.ALERT).show();
            } else {

                final ProgressDialog progressDialog = new ProgressDialog(LoginActivity.this);
                progressDialog.setMessage("Logging In..");
                progressDialog.show();

                ParseUser.logInInBackground(username, password, new LogInCallback() {
                    @Override
                    public void done(ParseUser parseUser, ParseException e) {
                        progressDialog.hide();
                        if (e != null) {
                            Crouton.makeText(LoginActivity.this, e.getMessage(), Style.ALERT)
                                    .show();
                        } else {
                            goToMainActivity();
                        }
                    }
                });
            }
        }
        return super.onOptionsItemSelected(item);

    }

    private static void getFacebookIdInBackground() {
        Request.executeMeRequestAsync(ParseFacebookUtils.getSession(), new Request.GraphUserCallback() {
            @Override
            public void onCompleted(GraphUser user, Response response) {
                if (user != null) {
                    ParseUser.getCurrentUser().put("fbId", user.getId());
                    ParseUser.getCurrentUser().put("first_name", user.getFirstName());
                    ParseUser.getCurrentUser().put("last_name", user.getLastName());
                    ParseUser.getCurrentUser().saveInBackground();
                }
            }
        });
    }

    private void goToMainActivity(){
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK |
                Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}
