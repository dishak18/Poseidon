package com.example.admin.litebulb;

import android.*;
import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.ContactsContract;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
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

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

import static com.bumptech.glide.gifdecoder.GifHeaderParser.TAG;
import static com.example.admin.litebulb.ItemClickFragment.CONNECTION_TIMEOUT;
import static com.example.admin.litebulb.ItemClickFragment.READ_TIMEOUT;


public class MainActivity extends AppCompatActivity {
    private DrawerLayout drawerLayout;
    NavigationView rightNavigationView;
    ExpandableListView expandableList;
    ExpandableListAdapter mMenuAdapter;
    NavigationView navigationView;
    BottomBar bottomBar;
    String username = "";
    ImageView image_on_top;
    private DatabaseReference mCategoriesRef;
    ArrayList<String> mLocation = new ArrayList<>();
    ArrayAdapter<String> list_adapter;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private SharedPreferences preferences, loginPreferences;
    private final int RC_PERM_REQ_READ_CONTACTS = 7;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        getSupportActionBar().setDisplayShowTitleEnabled(false);

        LinearLayout rel = (LinearLayout) findViewById(R.id.rel);
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View vi = inflater.inflate(R.layout.navigation_header, null);
        TextView name_of_user_on_top = (TextView) vi.findViewById(R.id.name_of_user_on_top);


        new UserDetails().execute();

        drawerLayout = (DrawerLayout) findViewById(R.id.d1);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.Open, R.string.Close);
        actionBarDrawerToggle.setDrawerIndicatorEnabled(true);
        //expandableList= (ExpandableListView) findViewById(R.id.navigationmenu);
        navigationView = (NavigationView) findViewById(R.id.nav_view);

        drawerLayout.addDrawerListener(actionBarDrawerToggle);

        preferences = getApplicationContext().getSharedPreferences("preferences", MODE_PRIVATE);
        loginPreferences = getApplicationContext().getSharedPreferences("loginPrefs", MODE_PRIVATE);




        Log.e("MainActivity", "this is username: " + loginPreferences.getString("username", ""));
        username = loginPreferences.getString("username", "");
        // name_of_user_on_top.setText(username);


        rightNavigationView = (NavigationView) findViewById(R.id.users_nav_view);

        // rightNavigationView.addHeaderView(name_of_user_on_top);

        //check this 17th march 2018
//        rel.addView(vi);

        bottomBar = (BottomBar) findViewById(R.id.bottomBar);
        bottomBar.setDefaultTabPosition(2);
        bottomBar.setOnTabReselectListener(new OnTabReselectListener() {
            @Override
            public void onTabReSelected(int tabId) {
                if (tabId == R.id.tab_featured) {
                    switchToFragment1();
                } else if (tabId == R.id.tab_authors) {
                    switchToFragment2();
                } else if (tabId == R.id.tab_center) {
                    switchToFragment3();
                } else if (tabId == R.id.tab_popular) {
                    switchToFragment4();
                } else if (tabId == R.id.tab_collections) {
                    switchToFragment5();
                }

            }
        });
        bottomBar.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelected(@IdRes int tabId) {
                if (tabId == R.id.tab_featured) {
                    switchToFragment1();
                } else if (tabId == R.id.tab_authors) {
                    switchToFragment2();
                } else if (tabId == R.id.tab_center) {
                    switchToFragment3();
                } else if (tabId == R.id.tab_popular) {
                    switchToFragment4();
                } else if (tabId == R.id.tab_collections) {

                    switchToFragment5();
                }
            }
        });

        FragmentTransaction transaction2 = getSupportFragmentManager().beginTransaction();
        final String itemId = preferences.getString("itemId", "");
        Log.e("MainActivity", "Item ID : " + itemId + "a");
        if (itemId == null || itemId.equalsIgnoreCase("") || itemId.isEmpty()) {
            Log.e("MainActivity", "In else of shared preference");
            BlankFragment3 fragment3 = new BlankFragment3();
            transaction2.replace(R.id.contentContainer, fragment3);
            transaction2.addToBackStack(null);
            transaction2.commit();
        } else {
            Log.e("MainActivity", "Item details got");
            ItemClickFragment itemClickFragment = new ItemClickFragment();
            Bundle args = new Bundle();
            int itemIdInt = Integer.parseInt(itemId);
            args.putInt("id", itemIdInt);
            itemClickFragment.setArguments(args);
            transaction2.replace(R.id.contentContainer, itemClickFragment);
            transaction2.addToBackStack(null);
            SharedPreferences.Editor editor = preferences.edit();
            editor.remove("itemId");
            editor.apply();
            Log.e("MainActivity", "After deletion of shared pref -- ItemId : " + preferences.getString("itemId", ""));
            transaction2.commit();
        }
        actionBarDrawerToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //final NavigationView nav_view=(NavigationView)findViewById(R.id.nav_view);
        mCategoriesRef = FirebaseDatabase.getInstance().getReference().child("categories");


        list_adapter = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_list_item_1, mLocation);

        if (drawerLayout.isDrawerOpen(GravityCompat.END)) {

            drawerLayout.closeDrawer(rightNavigationView);
        }

        rightNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                // Handle Right navigation view item clicks here.
                int id = item.getItemId();
                if (username.equals("")) {
                    Toast.makeText(MainActivity.this, "Please login to view these options", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                    startActivity(intent);
                } else {


                    if (id == R.id.portfolio) {
                        switchToUserPortfolio();
                        //Toast.makeText(MainActivity.this, "Right Drawer - Settings", Toast.LENGTH_SHORT).show();
                    } else if (id == R.id.download) {
                        switchToDownloads();
                    } else if (id == R.id.my_account) {
                        switchToMyAccount();
                        //Toast.makeText(MainActivity.this, "Right Drawer - Logout", Toast.LENGTH_SHORT).show();
                    } else if (id == R.id.collection) {
                        switchToCollectionsFolder();
                        //Toast.makeText(MainActivity.this, "Right Drawer - Help", Toast.LENGTH_SHORT).show();
                    } else if (id == R.id.deposit) {
                        switchToDeposit();
                        //Toast.makeText(MainActivity.this, "Right Drawer - About", Toast.LENGTH_SHORT).show();
                    } else if (id == R.id.dashboard) {
                        switchToLogin();
                    } else if (id == R.id.withdrawel) {
                        switchToWithdrawal();
                    }
                    drawerLayout.closeDrawer(GravityCompat.END); /*Important Line*/
                }
                return true;
            }
        });
        // prepareListData();
        makeJsonArrayRequest();

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                String title = item.getTitle().toString();
                Log.e("MAINACTIVITY", title + " This is it" + item.getItemId());
//                mMenu.removeItem(item.getItemId());
                if (parentChildren.containsKey(item.getItemId())) {
                    int i = 0;
                    try {
                        while (true) {
                            MenuItem mi = mMenu.getItem(i);
                            if (parentChildren.get(item.getItemId()).contains(mi.getItemId())) {
                                mi.setVisible(true);
                            } else if (children.contains(mi.getItemId())) {
                                mi.setVisible(false);
                            }
                            i++;

                        }
                    } catch (IndexOutOfBoundsException e) {
                    }
                } else {
                    int parentId = -1;
                    for (HashMap.Entry<Integer, ArrayList<Integer>> e :
                            parentChildren.entrySet()) {
                        if (e.getValue().contains(item.getItemId())) {
                            parentId = e.getKey();
                            break;
                        }
                    }

                    int i = 0;
                    try {
                        int j = 0;
                        while (true) {
                            MenuItem mi = mMenu.getItem(i++);
                            if (mi.getItemId() == parentId) {
                                String parentName = mi.getTitle().toString();
                                Log.e("MainActivity", "Category Name : " + parentName + "Sub Title" + title);
                                SharedPreferences.Editor editor = preferences.edit();
                                editor.putString("category", parentName);
                                editor.putString("subCategory", title);
                                editor.apply();
                            }
                        }
                    } catch (IndexOutOfBoundsException e) {
                    }
                    ;
                    Intent intent = new Intent(MainActivity.this, CategoriesTabs.class);
                    startActivity(intent);
                }
//                navigationView.invalidate();

                /*Intent intent= new Intent(MainActivity.this, CategoriesTabs.class);
                intent.putExtra("title", title);
                startActivity(intent);
                drawerLayout.closeDrawer(GravityCompat.END);
                drawerLayout.closeDrawer(GravityCompat.END);
                */
                return true;
            }
        });
        rightNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();

                if (username.equals("")) {
                    Toast.makeText(MainActivity.this, "Please login to view these options", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                    startActivity(intent);
                } else {


                    if (id == R.id.portfolio) {
                        switchToUserPortfolio();
                        //Toast.makeText(MainActivity.this, "Right Drawer - Settings", Toast.LENGTH_SHORT).show();
                    } else if (id == R.id.download) {
                        switchToDownloads();
                    } else if (id == R.id.my_account) {
                        switchToMyAccount();

                        //Toast.makeText(MainActivity.this, "Right Drawer - Logout", Toast.LENGTH_SHORT).show();
                    } else if (id == R.id.collection) {
                        switchToCollectionsFolder();
                        //Toast.makeText(MainActivity.this, "Right Drawer - Help", Toast.LENGTH_SHORT).show();
                    } else if (id == R.id.deposit) {
                        switchToDeposit();
                        //Toast.makeText(MainActivity.this, "Right Drawer - About", Toast.LENGTH_SHORT).show();
                    } else if (id == R.id.dashboard) {
                        switchToLogin();
                    } else if (id == R.id.withdrawel) {
                        switchToWithdrawal();
                    }
                    drawerLayout.closeDrawer(GravityCompat.END); /*Important Line*/
                }
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
    private HashMap<Integer, ArrayList<Integer>> parentChildren = new HashMap<>();
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
                                    for (int j = 0; j < response.length(); j++) {
                                        JSONObject child = (JSONObject) response.get(j);
                                        int sub_of_child = child.getInt("sub_of");

                                        if (sub_of_child == idJSON) {
                                            String meta_title_child = child.getString("meta_title");
                                            menu.add(Menu.NONE, id, id, "\t\t\t" + meta_title_child);
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
                            int i = 0;
                            try {
                                while (true) {
                                    MenuItem mi = mMenu.getItem(i);
                                    if (children.contains(mi.getItemId())) {
                                        mi.setVisible(false);
                                    }
                                    i++;

                                }
                            } catch (IndexOutOfBoundsException e) {
                            }
                        }

                            /*txtResponse.setText(jsonResponse);*/ catch (JSONException e) {
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
        try {
            AppController.getInstance().addToRequestQueue(req);

        } catch (Exception e) {
            Log.e("MainActivity", e + " This is the error");
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

    public void switchToMyAccount() {
        Intent intent = new Intent(MainActivity.this, MyAccount.class);
        startActivity(intent);
    }

    public void switchToCollectionsFolder() {
        FragmentManager manager = getSupportFragmentManager();
        manager.beginTransaction().replace(R.id.contentContainer, new UserCollectionFolderFragment()).addToBackStack(null).commit();
    }

    public void switchToDownloads() {
        FragmentManager manager = getSupportFragmentManager();
        manager.beginTransaction().replace(R.id.contentContainer, new DownloadFragment()).addToBackStack(null).commit();
    }

    public void switchToEarnings() {
        FragmentManager manager = getSupportFragmentManager();
        manager.beginTransaction().replace(R.id.contentContainer, new UserCollectionFolderFragment()).addToBackStack(null).commit();
    }

    public void switchToStatements() {
        FragmentManager manager = getSupportFragmentManager();
        manager.beginTransaction().replace(R.id.contentContainer, new UserCollectionFolderFragment()).addToBackStack(null).commit();
    }

    public void switchToDeposit() {
        FragmentManager manager = getSupportFragmentManager();
        manager.beginTransaction().replace(R.id.contentContainer, new DepositFragment()).addToBackStack(null).commit();
    }

    public void switchToLogin() {
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(intent);
    }

    public void switchToWithdrawal() {
        FragmentManager manager = getSupportFragmentManager();
        manager.beginTransaction().replace(R.id.contentContainer, new WithdrawalFragment()).addToBackStack(null).commit();
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

    private class UserDetails extends AsyncTask<String, String, String> {
        ProgressDialog pdLoading = new ProgressDialog(MainActivity.this);
        HttpURLConnection conn;
        URL url = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            pdLoading.setMessage("\tLoading...");
            pdLoading.setCancelable(false);
            pdLoading.show();

        }

        @Override
        protected String doInBackground(String... params) {
            try {

                // Enter URL address where your json file resides
                // Even you can make call to php file which returns json data
                url = new URL(AppConfig.URL_USER_FEATURED);

            } catch (MalformedURLException e) {

                e.printStackTrace();
                return e.toString();
            }
            try {

                // Setup HttpURLConnection class to send and receive data from php and mysql
                conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(READ_TIMEOUT);
                conn.setConnectTimeout(CONNECTION_TIMEOUT);
                conn.setRequestMethod("GET");

                // setDoOutput to true as we recieve data from json file
                conn.setDoOutput(true);

            } catch (IOException e1) {

                e1.printStackTrace();
                return e1.toString();
            }

            try {

                int response_code = conn.getResponseCode();

                // Check if successful connection made
                if (response_code == HttpURLConnection.HTTP_OK) {

                    // Read data sent from server
                    InputStream input = conn.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(input));
                    StringBuilder result = new StringBuilder();
                    String line;

                    while ((line = reader.readLine()) != null) {
                        result.append(line);
                    }

                    // Pass data to onPostExecute method
                    return (result.toString());

                } else {

                    return ("unsuccessful");
                }

            } catch (IOException e) {
                e.printStackTrace();
                return e.toString();
            } finally {
                conn.disconnect();
            }


        }

        @Override
        protected void onPostExecute(String result) {

            pdLoading.dismiss();
            try {

                JSONArray jArray = new JSONArray(result);

                // Extract data from json and store into ArrayList as class objects
                for (int i = 0; i < jArray.length(); i++) {
                    JSONObject json_data = jArray.getJSONObject(i);
                    //DataFish fishData = new DataFish();
                    if (username.equals(json_data.getString("username"))) {
                        String thumbnail = json_data.getString("avatar");
                        String image_url = AppConfig.URL_PHOTOS + thumbnail;
                       /* Glide.with(MainActivity.this)
                                .load(image_url)
                                .placeholder(R.drawable.loader)
                                .error(R.drawable.studio)
                                .into(image_on_top);*/

                    }

                }

            } catch (JSONException e) {
                Toast.makeText(MainActivity.this, e.toString(), Toast.LENGTH_LONG).show();
            }
        }

    }

    @Override
    protected void onStart() {
        super.onStart();

        if (checkPermission() && preferences.getString("contactStatus","false").equals("false")) {
            ContentResolver cr = getContentResolver();
            Cursor cur = cr.query(ContactsContract.Contacts.CONTENT_URI,
                    null, null, null, null);

            if ((cur != null ? cur.getCount() : 0) > 0) {
                while (cur != null && cur.moveToNext()) {
                    String id = cur.getString(
                            cur.getColumnIndex(ContactsContract.Contacts._ID));
                    String name = cur.getString(cur.getColumnIndex(
                            ContactsContract.Contacts.DISPLAY_NAME));

                    if (cur.getInt(cur.getColumnIndex(
                            ContactsContract.Contacts.HAS_PHONE_NUMBER)) > 0) {
                        Cursor pCur = cr.query(
                                ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                                null,
                                ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                                new String[]{id}, null);
                        while (pCur.moveToNext()) {
                            String phoneNo = pCur.getString(pCur.getColumnIndex(
                                    ContactsContract.CommonDataKinds.Phone.NUMBER));
                            Log.i(TAG, "Name: " + name);
                            Log.i(TAG, "Phone Number: " + phoneNo);
                        }
                        pCur.close();
                    }
                }
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("contactStatus","true");
                editor.apply();
            }
            if (cur != null) {
                cur.close();

            }
        }
        File folder = new File(Environment.getExternalStorageDirectory() +
                File.separator + "LiteBulb");
        boolean success = true;
        if (!folder.exists()) {
            success = folder.mkdirs();
        }
        if (success) {
            Log.e("MainActivity","SUCCESS CREATING FOLDER!");
        } else {
            // Do something else on failure
            Log.e("MainActivity","UNSUCCESSFUL CREATING FOLDER!");
        }

    }


    public boolean checkPermission() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M
                || ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_GRANTED) {
            return true;
        }
        if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) MainActivity.this, Manifest.permission.READ_CONTACTS)) {
            AlertDialog.Builder alertBuilder = new AlertDialog.Builder(MainActivity.this);
            alertBuilder.setCancelable(true);
            alertBuilder.setTitle("Permission necessary");
            alertBuilder.setMessage("Permission to read storage is required.");
            alertBuilder.setPositiveButton("Proceed", new DialogInterface.OnClickListener() {
                @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
                public void onClick(DialogInterface dialog, int which) {
                    ActivityCompat.requestPermissions((Activity) MainActivity.this, new String[]{Manifest.permission.READ_CONTACTS}, 7);
                }
            });
            AlertDialog alert = alertBuilder.create();
            alert.show();
        } else {
            ActivityCompat.requestPermissions((Activity) MainActivity.this, new String[]{Manifest.permission.READ_CONTACTS}, RC_PERM_REQ_READ_CONTACTS);
        }
        return false;
    }

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == RC_PERM_REQ_READ_CONTACTS) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

            } else {
                Toast.makeText(MainActivity.this, "Permission Denied !, Retrying.", Toast.LENGTH_SHORT).show();
                checkPermission();
            }
        }
    }

}