package com.example.admin.litebulb;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.example.admin.litebulb.Adapters.MyAccountAdapter;

public class MyAccount extends AppCompatActivity implements PasscodeFragment.OnFragmentInteractionListener, ProfileEdit.OnFragmentInteractionListener, SocialProfileFragment.OnFragmentInteractionListener, SalesLicense.OnFragmentInteractionListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_account);
        TabLayout tabLayout=(TabLayout) findViewById(R.id.tabLayout);
        tabLayout.addTab(tabLayout.newTab().setText("Profile Edit"));
        tabLayout.addTab(tabLayout.newTab().setText("Passcode"));
        tabLayout.addTab(tabLayout.newTab().setText("Sales Licenses"));
        tabLayout.addTab(tabLayout.newTab().setText("Social Profile"));
        tabLayout.addTab(tabLayout.newTab().setText("Exclusive Author"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        final ViewPager viewPager=(ViewPager) findViewById(R.id.pager);

        final MyAccountAdapter adapter=new MyAccountAdapter(getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(adapter);
        viewPager.setOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }
}
