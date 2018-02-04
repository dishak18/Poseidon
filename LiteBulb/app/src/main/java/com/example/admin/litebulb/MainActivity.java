package com.example.admin.litebulb;

import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnTabSelectListener;


public class MainActivity extends AppCompatActivity {
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.e("MainSctivity", "We are here");
        drawerLayout=(DrawerLayout)findViewById(R.id.d1);
        actionBarDrawerToggle= new ActionBarDrawerToggle(this,drawerLayout, R.string.Open, R.string.Close);
        actionBarDrawerToggle.setDrawerIndicatorEnabled(true);
        Log.e("MainActivity", "Hildhxudvkhsvc");
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
       // final NavigationView nav_view=(NavigationView)findViewById(R.id.nav_view);

        BottomBar bottomBar = (BottomBar) findViewById(R.id.bottomBar);
        bottomBar.setDefaultTabPosition(2);
        bottomBar.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelected(@IdRes int tabId) {
                if (tabId == R.id.tab_featured) {
                    // The tab with id R.id.tab_favorites was selected,
                    // change your content accordingly.
                    //mTextMessage.setText(R.string.title_home);
/*
                    Intent intent=new Intent(MainActivity.this, TrialActivity.class);
                    startActivity(intent);*/
                    switchToFragment1();
                }
                else if(tabId==R.id.tab_authors)
                {

                    switchToFragment2();
                }
                else if(tabId==R.id.tab_center)
                {

                    switchToFragment3();
                }
                else if(tabId==R.id.tab_popular)
                {

                    switchToFragment4();
                }
                else if(tabId==R.id.tab_collections)
                {

                    switchToFragment5();
                }
            }
        });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.options_menu, menu);

        return true;
    }
    public void switchToFragment1() {
        FragmentManager manager = getSupportFragmentManager();
        manager.beginTransaction().replace(R.id.contentContainer, new BlankFragment1()).commit();
    }
    public void switchToFragment2() {
        FragmentManager manager = getSupportFragmentManager();
        manager.beginTransaction().replace(R.id.contentContainer, new BlankFragment2()).commit();
    }
    public void switchToFragment3() {
        FragmentManager manager = getSupportFragmentManager();
        manager.beginTransaction().replace(R.id.contentContainer, new BlankFragment3()).commit();
    }
    public void switchToFragment4() {
        FragmentManager manager = getSupportFragmentManager();
        manager.beginTransaction().replace(R.id.contentContainer, new BlankFragment4()).commit();
    }
    public void switchToFragment5() {
        FragmentManager manager = getSupportFragmentManager();
        manager.beginTransaction().replace(R.id.contentContainer, new BlankFragment5()).commit();
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return actionBarDrawerToggle.onOptionsItemSelected(item) ||
                super.onOptionsItemSelected(item);
    }
}
