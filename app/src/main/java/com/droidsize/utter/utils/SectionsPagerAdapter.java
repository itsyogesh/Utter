package com.droidsize.utter.utils;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.droidsize.utter.fragments.FriendsFragment;
import com.droidsize.utter.fragments.InboxFragment;
import com.droidsize.utter.R;

import java.util.Locale;

/**
 * Created by apple on 05/12/14.
 */
public class SectionsPagerAdapter extends FragmentPagerAdapter {

    private Context mContext;

    public SectionsPagerAdapter(Context context, FragmentManager fm){
        super(fm);
        mContext = context;
    }

    @Override
    public Fragment getItem(int position){
        switch (position) {
            case 0:
                return new InboxFragment();
            case 1:
                return new FriendsFragment();
        }

        return null;
    }

    @Override
    public int getCount(){
        //Show 2 pages
        return 2;
    }

    @Override
    public CharSequence getPageTitle(int position){
        Locale l = Locale.getDefault();
        switch (position) {
            case 0:
                return mContext.getString(R.string.title_section1).toUpperCase(l);
            case 1:
                return mContext.getString(R.string.title_section2).toUpperCase(l);
        }

        return null;
    }
}
