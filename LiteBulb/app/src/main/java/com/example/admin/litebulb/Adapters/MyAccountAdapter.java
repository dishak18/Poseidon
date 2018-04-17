package com.example.admin.litebulb.Adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.example.admin.litebulb.PasscodeFragment;
import com.example.admin.litebulb.ProfileEdit;
import com.example.admin.litebulb.SalesLicense;
import com.example.admin.litebulb.SocialProfileFragment;


public class MyAccountAdapter extends FragmentStatePagerAdapter {

    int mNoOfTabs;
    public MyAccountAdapter(FragmentManager fm, int noOfTabs) {
        super(fm);
        this.mNoOfTabs=noOfTabs;
    }

    /**
     * Return the Fragment associated with a specified position.
     *
     * @param position
     */
    @Override
    public Fragment getItem(int position) {

        switch (position)
        {
            case 0:
                ProfileEdit profileEdit=new ProfileEdit();
                return profileEdit;
            case 1:
                PasscodeFragment passcode=new PasscodeFragment();
                return passcode;
            case 2:
                SalesLicense salesLicense=new SalesLicense();
                return salesLicense;
            case 3:
                SocialProfileFragment socialProfileFragment=new SocialProfileFragment();
                return socialProfileFragment;
            default:
                return null;
        }
    }

    /**
     * Return the number of views available.
     */
    @Override
    public int getCount() {
        return mNoOfTabs;
    }
}
