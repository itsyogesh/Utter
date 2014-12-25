package com.droidsize.utter;

import android.app.Application;

import com.parse.Parse;
import com.parse.ParseObject;


/**
 * Created by apple on 05/12/14.
 */
public class UtterApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Parse.initialize(this, "zFs0P9cUuegXTEp6skFlpmv61NaeilD5o5hbxHI4",
                "6vkAKwuTOVHrRFQIKldqkkgwBtakLtpBdCQW8UUj");
    }

}
