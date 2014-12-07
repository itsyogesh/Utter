package com.droidsize.utter;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.Timer;
import java.util.TimerTask;


public class ViewImageActivity extends Activity {

    public static final String TAG = ViewImageActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_image);

        ImageView imageView = (ImageView) findViewById(R.id.imageView);

        Uri imageUri = getIntent().getData();
        Log.d("url", imageUri.toString());

        setProgressBarIndeterminateVisibility(true);

        Picasso.with(this).load(imageUri.toString()).into(imageView, new Callback() {
            @Override
            public void onSuccess() {
                setProgressBarIndeterminateVisibility(false);
                Timer timer = new Timer();
                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        finish();
                    }
                }, 10*1000);
            }

            @Override
            public void onError() {

            }
        });
    }

}
