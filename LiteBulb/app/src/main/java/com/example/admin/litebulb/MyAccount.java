package com.example.admin.litebulb;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;

import com.example.admin.litebulb.Adapters.MyAccountAdapter;

public class MyAccount extends AppCompatActivity implements PasscodeFragment.OnFragmentInteractionListener, ProfileEdit.OnFragmentInteractionListener, SocialProfileFragment.OnFragmentInteractionListener, SalesLicense.OnFragmentInteractionListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_account);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        TabLayout tabLayout=(TabLayout) findViewById(R.id.tabLayout);
        tabLayout.addTab(tabLayout.newTab().setText("Profile Edit"));
        tabLayout.addTab(tabLayout.newTab().setText("Passcode"));
        tabLayout.addTab(tabLayout.newTab().setText("Sales Licenses"));
        tabLayout.addTab(tabLayout.newTab().setText("Social Profile"));
        tabLayout.addTab(tabLayout.newTab().setText("Exclusive Author"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);

        ImageButton home=(ImageButton) findViewById(R.id.home);

        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(MyAccount.this, MainActivity.class);
                startActivity(intent);
            }
        });

        final ViewPager viewPager=(ViewPager) findViewById(R.id.pager);

        final MyAccountAdapter adapter=new MyAccountAdapter(getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(adapter);
        viewPager.setOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

                viewPager.setCurrentItem(tab.getPosition());
                Log.e("MyAccount", "This is the tab.getposition "+tab.getPosition()+"");
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        /*FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.home);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Click action
                Intent intent = new Intent(MyAccount.this, MainActivity.class);
                startActivity(intent);
            }
        });*/
    }
}
