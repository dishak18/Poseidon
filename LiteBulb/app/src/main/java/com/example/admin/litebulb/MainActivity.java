package com.example.admin.litebulb;

import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ExpandableListView;
import android.widget.Toast;

import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnTabSelectListener;


public class MainActivity extends AppCompatActivity {
    private DrawerLayout drawerLayout;
    NavigationView rightNavigationView;
    ExpandableListView expandableList;
    /*List<ExpandedMenuModel> listDataHeader;
    HashMap<ExpandedMenuModel, List<String>> listDataChild;*/
    private ActionBarDrawerToggle actionBarDrawerToggle;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        drawerLayout=(DrawerLayout)findViewById(R.id.d1);
        actionBarDrawerToggle= new ActionBarDrawerToggle(this,drawerLayout, R.string.Open, R.string.Close);
        actionBarDrawerToggle.setDrawerIndicatorEnabled(true);
        expandableList= (ExpandableListView) findViewById(R.id.navigationmenu);

        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        /*FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/
        actionBarDrawerToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
       //final NavigationView nav_view=(NavigationView)findViewById(R.id.nav_view);
        rightNavigationView = (NavigationView) findViewById(R.id.users_nav_view);


    rightNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                // Handle Right navigation view item clicks here.
                int id = item.getItemId();

                if (id == R.id.portfolio) {
                    Toast.makeText(MainActivity.this, "Right Drawer - Settings", Toast.LENGTH_SHORT).show();
                } else if (id == R.id.download) {
                    Toast.makeText(MainActivity.this, "Right Drawer - Logout", Toast.LENGTH_SHORT).show();
                } else if (id == R.id.collection) {
                    Toast.makeText(MainActivity.this, "Right Drawer - Help", Toast.LENGTH_SHORT).show();
                } else if (id == R.id.deposit) {
                    Toast.makeText(MainActivity.this, "Right Drawer - About", Toast.LENGTH_SHORT).show();
                }

                drawerLayout.closeDrawer(GravityCompat.END); /*Important Line*/
                return true;
            }
        });
        if(drawerLayout.isDrawerOpen(GravityCompat.START)) {
            //drawer is open

        }
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
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else if (drawerLayout.isDrawerOpen(GravityCompat.END)) {  /*Closes the Appropriate Drawer*/
            drawerLayout.closeDrawer(GravityCompat.END);
        } else {
            super.onBackPressed();
            System.exit(0);
        }
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
        int id = item.getItemId();

        if (id == R.id.action_openRight) {
            drawerLayout.openDrawer(GravityCompat.END); /*Opens the Right Drawer*/
            return true;
        }
        return actionBarDrawerToggle.onOptionsItemSelected(item) ||
                super.onOptionsItemSelected(item);

    }

}
