package com.droidsize.utter;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.facebook.Request;
import com.facebook.Response;
import com.facebook.model.GraphUser;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseFacebookUtils;
import com.parse.ParseUser;

import java.util.HashMap;

import de.keyboardsurfer.android.widget.crouton.Crouton;
import de.keyboardsurfer.android.widget.crouton.Style;


public class GetStartedActivity extends Activity implements BaseSliderView.OnSliderClickListener {

    protected Button mSignupButton;
    protected Button mFbSignpButton;
    protected SliderLayout mGetStartedDemoSlider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_started);

        ActionBar actionBar = getActionBar();
        actionBar.hide();

        mSignupButton = (Button) findViewById(R.id.signupButton);
        mFbSignpButton = (Button) findViewById(R.id.signupFbButton);
        mGetStartedDemoSlider = (SliderLayout) findViewById(R.id.slider);

        HashMap<String,Integer> file_maps = new HashMap<String, Integer>();
        file_maps.put("Hannibal",R.drawable.background);
        file_maps.put("Big Bang Theory",R.drawable.background);
        file_maps.put("House of Cards",R.drawable.background);
        file_maps.put("Game of Thrones", R.drawable.background);

        for(String name : file_maps.keySet()) {
            TextSliderView textSliderView = new TextSliderView(this);
            // initialize a SliderLayout
            textSliderView
                    .image(file_maps.get(name))
                    .setScaleType(BaseSliderView.ScaleType.Fit)
                    .setOnSliderClickListener(this);

            //add your extra information
            textSliderView.getBundle()
                    .putString("extra", name);

            mGetStartedDemoSlider.addSlider(textSliderView);

        }


        mSignupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GetStartedActivity.this, SignUpActivity.class);
                startActivity(intent);
            }
        });

        mFbSignpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ParseFacebookUtils.logIn(GetStartedActivity.this, 10, new LogInCallback() {
                    @Override
                    public void done(ParseUser parseUser, ParseException e) {

                        if (parseUser == null) {
                            Crouton.makeText(GetStartedActivity.this, "You cancelled the login",
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
        getMenuInflater().inflate(R.menu.get_started, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
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
        Intent intent = new Intent(GetStartedActivity.this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK |
                Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    @Override
    public void onSliderClick(BaseSliderView baseSliderView) {
        //Todo stub
    }
}
