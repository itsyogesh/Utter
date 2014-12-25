package com.droidsize.utter;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.droidsize.utter.utils.FileHelper;
import com.droidsize.utter.utils.ParseConstants;
import com.parse.ParseException;
import com.parse.ParseFacebookUtils;
import com.parse.ParseFile;
import com.parse.ParseUser;
import com.parse.SignUpCallback;
import com.soundcloud.android.crop.Crop;

import java.io.File;

import de.keyboardsurfer.android.widget.crouton.Crouton;
import de.keyboardsurfer.android.widget.crouton.Style;


public class SignUpActivity extends Activity {

    protected EditText mUserName;
    protected EditText mPassword;
    protected EditText mEmail;
    protected EditText mFirstName;
    protected EditText mLastName;
    protected ImageView mUserProfileImage;

    protected TextView mLoginText;

    protected Uri mProfileImageUri = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        mFirstName = (EditText) findViewById(R.id.firstNameField);
        mLastName = (EditText) findViewById(R.id.lastNameField);
        mUserName = (EditText) findViewById(R.id.usernameField);
        mEmail = (EditText) findViewById(R.id.emailField);
        mPassword = (EditText) findViewById(R.id.passwordField);
        mLoginText = (TextView) findViewById(R.id.loginButton);

        mUserProfileImage = (ImageView) findViewById(R.id.userImageSelection);

        mUserProfileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Crop.pickImage(SignUpActivity.this);

            }
        });

        mLoginText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.login, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.signup_menu_button) {

            String username = mUserName.getText().toString().trim();
            String password = mPassword.getText().toString().trim();
            String email = mEmail.getText().toString().trim();
            String firstName = mFirstName.getText().toString().trim();
            String lastName = mLastName.getText().toString().trim();

            if(username.isEmpty() || password.isEmpty() || email.isEmpty()){
                Crouton.makeText(SignUpActivity.this, R.string.signup_error_message,
                        Style.ALERT).show();
            }
            else {

                final ProgressDialog progressDialog = new ProgressDialog(SignUpActivity.this);
                progressDialog.setMessage("Signing Up..");
                progressDialog.show();

                ParseUser newUser = new ParseUser();
                newUser.setUsername(username);
                newUser.setPassword(password);
                newUser.setEmail(email);
                newUser.put("first_name", firstName);
                newUser.put("last_name", lastName);

                if(mProfileImageUri != null){
                    byte[] fileBytes = FileHelper.getByteArrayFromFile(this, mProfileImageUri);
                    fileBytes = FileHelper.reduceImageForUpload(fileBytes);
                    String fileName = FileHelper.getFileName(this, mProfileImageUri,
                            ParseConstants.TYPE_IMAGE);
                    ParseFile file = new ParseFile(fileName, fileBytes);
                    newUser.put("profile_image", file);
                }


                newUser.signUpInBackground(new SignUpCallback() {
                    @Override
                    public void done(ParseException e) {
                        progressDialog.dismiss();

                        if(e != null){
                            Crouton.makeText(SignUpActivity.this, e.getMessage(), Style.ALERT)
                                    .show();
                        }
                        else {
                            goToMainActivity();
                        }
                    }
                });
            }

        }
        return super.onOptionsItemSelected(item);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent result) {
        super.onActivityResult(requestCode, resultCode, result);
        ParseFacebookUtils.finishAuthentication(requestCode, resultCode, result);

        if (requestCode == Crop.REQUEST_PICK && resultCode == RESULT_OK) {
            beginCrop(result.getData());
        } else if (requestCode == Crop.REQUEST_CROP) {
            handleCrop(resultCode, result);
        }
    }

    private void beginCrop(Uri source) {
        Uri outputUri = Uri.fromFile(new File(getCacheDir(), "cropped"));
        new Crop(source).output(outputUri).asSquare().start(this);
        mProfileImageUri = outputUri;
    }

    private void handleCrop(int resultCode, Intent result) {
        if (resultCode == RESULT_OK) {
            mUserProfileImage.setImageURI(Crop.getOutput(result));
        } else if (resultCode == Crop.RESULT_ERROR) {
            Crouton.makeText(this, Crop.getError(result).getMessage(), Style.ALERT).show();
        }
    }


    private void goToMainActivity(){
        Intent intent = new Intent(SignUpActivity.this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK |
                Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

}


