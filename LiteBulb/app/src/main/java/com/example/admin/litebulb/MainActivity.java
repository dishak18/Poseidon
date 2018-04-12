package com.example.admin.litebulb;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ExpandableListView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.example.admin.litebulb.Adapters.ExpandableListAdapter;
import com.example.admin.litebulb.BottomBarFragments.BlankFragment1;
import com.example.admin.litebulb.BottomBarFragments.BlankFragment2;
import com.example.admin.litebulb.BottomBarFragments.BlankFragment3;
import com.example.admin.litebulb.BottomBarFragments.BlankFragment4;
import com.example.admin.litebulb.SQL.AppConfig;
import com.example.admin.litebulb.SQL.AppController;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnTabReselectListener;
import com.roughike.bottombar.OnTabSelectListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import static com.bumptech.glide.gifdecoder.GifHeaderParser.TAG;


public class MainActivity extends AppCompatActivity {
    private DrawerLayout drawerLayout;
    NavigationView rightNavigationView;
    ExpandableListView expandableList;
    ExpandableListAdapter mMenuAdapter;
    NavigationView navigationView;
    BottomBar bottomBar;
    private DatabaseReference mCategoriesRef;
    ArrayList<String> mLocation=new ArrayList<>();
    ArrayAdapter<String> list_adapter;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private SharedPreferences preferences;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        drawerLayout=(DrawerLayout)findViewById(R.id.d1);
        actionBarDrawerToggle= new ActionBarDrawerToggle(this,drawerLayout, R.string.Open, R.string.Close);
        actionBarDrawerToggle.setDrawerIndicatorEnabled(true);
        //expandableList= (ExpandableListView) findViewById(R.id.navigationmenu);
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);

        preferences = getApplicationContext().getSharedPreferences("preferences", MODE_PRIVATE);

        bottomBar= (BottomBar) findViewById(R.id.bottomBar);
        bottomBar.setDefaultTabPosition(2);
        bottomBar.setOnTabReselectListener(new OnTabReselectListener() {
            @Override
            public void onTabReSelected(int tabId) {
                if (tabId == R.id.tab_featured) {
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
        bottomBar.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelected(@IdRes int tabId) {
                if (tabId == R.id.tab_featured) {
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

        FragmentTransaction transaction2 = getSupportFragmentManager().beginTransaction();
        final String itemId = preferences.getString("itemId","");
        Log.e("MainActivity","Item ID : "+itemId+"a");
        if(itemId==null || itemId.equalsIgnoreCase("") || itemId.isEmpty()){
            Log.e("MainActivity","In else of shared preference");
            BlankFragment3 fragment3=new BlankFragment3();
            transaction2.replace(R.id.contentContainer, fragment3);
            transaction2.addToBackStack(null);
            transaction2.commit();
        }else{
            Log.e("MainActivity","Item details got");
            ItemClickFragment itemClickFragment = new ItemClickFragment();
            Bundle args = new Bundle();
            int itemIdInt=Integer.parseInt(itemId);
            args.putInt("id", itemIdInt);
            itemClickFragment.setArguments(args);
            transaction2.replace(R.id.contentContainer, itemClickFragment);
            transaction2.addToBackStack(null);
            SharedPreferences.Editor editor = getSharedPreferences("preferences", MODE_PRIVATE).edit();
            editor.remove("itemId");
            editor.apply();
            Log.e("MainActivity","After deletion of shared pref -- ItemId : "+preferences.getString("itemId",""));
            transaction2.commit();
        }
        actionBarDrawerToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //final NavigationView nav_view=(NavigationView)findViewById(R.id.nav_view);
        mCategoriesRef = FirebaseDatabase.getInstance().getReference().child("categories");
        rightNavigationView = (NavigationView) findViewById(R.id.users_nav_view);


        list_adapter = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_list_item_1, mLocation);



        rightNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                // Handle Right navigation view item clicks here.
                int id = item.getItemId();

                if (id == R.id.portfolio) {
                    switchToUserPortfolio();
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
        // prepareListData();
        makeJsonArrayRequest();

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                String title=item.getTitle().toString();
                Log.e("MAINACTIVITY", title+" This is it" + item.getItemId());
//                mMenu.removeItem(item.getItemId());
                if (parentChildren.containsKey(item.getItemId())) {
                    int i=0;
                    try {
                        while(true) {
                            MenuItem mi = mMenu.getItem(i);
                            if (parentChildren.get(item.getItemId()).contains(mi.getItemId())) {
                                mi.setVisible(true);
                            } else if (children.contains(mi.getItemId())) {
                                mi.setVisible(false);
                            }
                            i++;

                        }
                    } catch (IndexOutOfBoundsException e) {}
                }else{
                    int parentId = -1;
                    for (HashMap.Entry<Integer, ArrayList<Integer> > e:
                            parentChildren.entrySet()) {
                        if (e.getValue().contains(item.getItemId())) {
                            parentId = e.getKey();
                            break;
                        }
                    }

                    int i =0;
                    try {
                        int j=0;
                        while (true) {
                            MenuItem mi = mMenu.getItem(i++);
                            if (mi.getItemId() == parentId) {
                                String parentName = mi.getTitle().toString();
                                Log.e("MainActivity","Category Name : "+parentName+"Sub Title"+title);
                                SharedPreferences.Editor editor = preferences.edit();
                                editor.putString("category", parentName);
                                editor.putString("subCategory", title);
                                editor.apply();
                            }
                        }
                    } catch (IndexOutOfBoundsException e) {};
                    Intent intent = new Intent(MainActivity.this, CategoriesTabs.class);
                    startActivity(intent);
                }
//                navigationView.invalidate();

                /*Intent intent= new Intent(MainActivity.this, CategoriesTabs.class);
                intent.putExtra("title", title);
                startActivity(intent);
                drawerLayout.closeDrawer(GravityCompat.END);
                drawerLayout.closeDrawer(GravityCompat.END);
                */return true;
            }
        });
        rightNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                Intent intent= new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
                drawerLayout.closeDrawer(GravityCompat.END);
                return true;
            }
        });
    }
    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        }
        if (drawerLayout.isDrawerOpen(GravityCompat.END)) {  /*Closes the Appropriate Drawer*/
            drawerLayout.closeDrawer(GravityCompat.END);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.options_menu, menu);

        return true;
    }
    private Menu mMenu;
    private int id = 1;
    private HashMap<Integer, ArrayList<Integer> > parentChildren = new HashMap<>();
    private ArrayList<Integer> children = new ArrayList<>();

    private void makeJsonArrayRequest() {

        JsonArrayRequest req = new JsonArrayRequest(AppConfig.URL_CATEGORIES,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            for (int i = 0; i < response.length(); i++) {

                                JSONObject person = (JSONObject) response.get(i);

                                String meta_title = person.getString("meta_title");
                                int sub_of = person.getInt("sub_of");
                                int idJSON = person.getInt("id");

                                if (sub_of == 0) {
                                    Menu menu = navigationView.getMenu();
                                    mMenu = menu;
                                    menu.add(Menu.NONE, id, id, meta_title);
                                    int gid = id;
                                    id++;
                                    parentChildren.put(gid, new ArrayList<Integer>());
                                    for(int j=0; j < response.length(); j++){
                                        JSONObject child = (JSONObject) response.get(j);
                                        int sub_of_child = child.getInt("sub_of");

                                        if(sub_of_child==idJSON){
                                            String meta_title_child = child.getString("meta_title");
                                            menu.add(Menu.NONE, id, id, "\t\t\t"+meta_title_child);
                                            parentChildren.get(gid).add(id);
//                                    menu.getItem(id).setVisible(false);
                                            children.add(id);
                                            id++;
                                        }
                                    }
/*
                                    id++;
                                    menu.add(Menu.NONE, id, id, "\t\tchild:" + gid+"-"+id);
                                    parentChildren.get(gid).add(id);
//                                    menu.getItem(id).setVisible(false);
                                    children.add(id);

                                    id++;
                                    menu.add(Menu.NONE, id, id, "\t\tchild:"+gid+"-"+id);
                                    parentChildren.get(gid).add(id);
//                                    menu.getItem(id).setVisible(false);
                                    children.add(id);*/
                                    id++;

//                                    menu.setGroupVisible(gid, false);
                    /*                mMenu.add(Menu.NONE, id++, Menu.NONE, "test-submenu-child" + id);
                                    mMenu.add(Menu.NONE, id++, Menu.NONE, "test-submenu-child" + id);
*/
                                    navigationView.invalidate();
                                    mLocation.add(meta_title);
                                }
                            }
                            int i=0;
                            try {
                                while(true) {
                                    MenuItem mi = mMenu.getItem(i);
                                    if (children.contains(mi.getItemId())) {
                                        mi.setVisible(false);
                                    }
                                    i++;

                                }
                            } catch (IndexOutOfBoundsException e) {}
                        }

                            /*txtResponse.setText(jsonResponse);*/
                        catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(MainActivity.this,
                                    "Error: " + e.getMessage(),
                                    Toast.LENGTH_LONG).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                Toast.makeText(MainActivity.this,
                        error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        try{
            AppController.getInstance().addToRequestQueue(req);

        }catch(Exception e)
        {
            Log.e("MainActivity", e+ " This is the error");
        }

    }
    public void switchToFragment1() {
        FragmentManager manager = getSupportFragmentManager();
        manager.beginTransaction().replace(R.id.contentContainer, new BlankFragment1()).addToBackStack(null).commit();
    }
    public void switchToFragment2() {
        FragmentManager manager = getSupportFragmentManager();
        manager.beginTransaction().replace(R.id.contentContainer, new BlankFragment2()).addToBackStack(null).commit();
    }
    public void switchToFragment3() {
        FragmentManager manager = getSupportFragmentManager();
        manager.beginTransaction().replace(R.id.contentContainer, new BlankFragment3()).addToBackStack(null).commit();
    }
    public void switchToFragment4() {
        FragmentManager manager = getSupportFragmentManager();
        manager.beginTransaction().replace(R.id.contentContainer, new BlankFragment4()).addToBackStack(null).commit();
    }
    public void switchToFragment5() {
        FragmentManager manager = getSupportFragmentManager();
        manager.beginTransaction().replace(R.id.contentContainer, new CollectionsFolderFragment()).addToBackStack(null).commit();
    }
    public void switchToUserPortfolio() {
        FragmentManager manager = getSupportFragmentManager();
        manager.beginTransaction().replace(R.id.contentContainer, new UserPortfolio()).addToBackStack(null).commit();
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