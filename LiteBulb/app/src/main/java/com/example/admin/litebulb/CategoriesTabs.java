package com.example.admin.litebulb;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.example.admin.litebulb.Models.TabsModel;
import com.example.admin.litebulb.SQL.AppConfig;
import com.example.admin.litebulb.SQL.AppController;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.bumptech.glide.gifdecoder.GifHeaderParser.TAG;

public class CategoriesTabs extends AppCompatActivity {

    private ViewPager mViewPager;
    TabLayout tabLayout;
    private ProgressDialog mProgress;
    private DatabaseReference mCategoriesRef;
    int id_of_item_table, id_of_categories_table, price;
    String name, thumbnail, meta_title, sub_of, categories;
    private ArrayList<TabsModel> categoriesTabs = new ArrayList<>();
    private SharedPreferences preferences;
    //private FloatingActionButton floatingActionButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categories_tabs);
        mProgress = new ProgressDialog(this);
        mProgress.setMessage("Loading...");
        tabLayout = (TabLayout) findViewById(R.id.tabs);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mProgress.show();
        mCategoriesRef = FirebaseDatabase.getInstance().getReference().child("categories");
        //floatingActionButton = (FloatingActionButton) findViewById(R.id.fab);
        ImageButton home=(ImageButton) findViewById(R.id.home);

        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(CategoriesTabs.this, MainActivity.class);
                startActivity(intent);
            }
        });

        mCategoriesRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                categoriesTabs.clear();
                for(DataSnapshot categories : dataSnapshot.getChildren()){
                    if(categories.child("sub_of").getValue(String.class).equalsIgnoreCase("0")){
                        TabsModel categoriesInfo = new TabsModel();
                        categoriesInfo.setCategoryId(categories.child("id").getValue(String.class));
                        categoriesInfo.setMetaTitle(categories.child("meta_title").getValue(String.class));
                        categoriesInfo.setSubOf(categories.child("sub_of").getValue(String.class));
                        Log.e("CategoriesTab","-- Title : "+categories.child("meta_title").getValue(String.class)
                                +"sub_of : "+categories.child("sub_of").getValue(String.class));
                        categoriesTabs.add(categoriesInfo);
                    }
                }
                setupViewPager();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        mViewPager = (ViewPager) findViewById(R.id.container);
        //mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);

        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));

    }

    private void setupViewPager(){
        final ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        TabFragment tabs[] = new TabFragment[7];
        for(int i=0; i<7; i++){
            tabs[i] = TabFragment.getInstance(categoriesTabs.get(i));
            if(i<6) {
                adapter.addFrag(tabs[i], categoriesTabs.get(i).getMetaTitle());
            }
            else{
                categoriesTabs.get(i).setMetaTitle("more");
                adapter.addFrag(tabs[i], categoriesTabs.get(i).getMetaTitle());
            }
        }

        mViewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(mViewPager);
        preferences = getSharedPreferences("preferences", MODE_PRIVATE);
        int position=0;
        for(int i=0; i<categoriesTabs.size(); i++){
            if(categoriesTabs.get(i).getMetaTitle().equals(preferences.getString("category","")))
                position = i;
        }
        //Toast.makeText(this, "Position : "+position, Toast.LENGTH_SHORT).show();
        mViewPager.setCurrentItem(position);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_categories_tabs, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.home) {
            Intent intent=new Intent(CategoriesTabs.this, LoginActivity.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            //Log.e("Tabbed_Menu", "no of frags " + mFragmentList.size());
            return mFragmentList.size();
        }

        public void addFrag(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
            mProgress.hide();
//            noItemText.setText("No" + title + "items to show");
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }
    private void makeJsonArrayRequest() {

        JsonArrayRequest req = new JsonArrayRequest(AppConfig.URL_CATEGORIES,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            for (int i = 0; i < response.length(); i++) {

                                JSONObject person = (JSONObject) response.get(i);

                                id_of_categories_table = person.getInt("id");
                                meta_title = person.getString("meta_title");
                                sub_of = person.getString("sub_of");

                                //add your if code here


                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(CategoriesTabs.this,
                                    "Error: " + e.getMessage(),
                                    Toast.LENGTH_LONG).show();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                Toast.makeText(CategoriesTabs.this,
                        error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        try{
            AppController.getInstance().addToRequestQueue(req);

        }catch(Exception e)
        {
            Log.e("BlankFragment3", e+ " This is the error");
        }
        JsonArrayRequest req1 = new JsonArrayRequest(AppConfig.URL_ITEM,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            for (int i = 0; i < response.length(); i++) {

                                JSONObject person = (JSONObject) response.get(i);

                                id_of_item_table = person.getInt("id");
                                name = person.getString("name");
                                price = person.getInt("price");
                                thumbnail = person.getString("thumbnail");

                                //add your if code here


                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(CategoriesTabs.this,
                                    "Error: " + e.getMessage(),
                                    Toast.LENGTH_LONG).show();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                Toast.makeText(CategoriesTabs.this,
                        error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        try{
            AppController.getInstance().addToRequestQueue(req);

        }catch(Exception e)
        {
            Log.e("BlankFragment3", e+ " This is the error");
        }
        JsonArrayRequest req2= new JsonArrayRequest(AppConfig.URL_ITEMS_TO_CATEGORY,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            for (int i = 0; i < response.length(); i++) {

                                JSONObject person = (JSONObject) response.get(i);

                                categories = person.getString("categories");
                                int item_id=person.getInt("item_id");

                                //add your if code here


                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(CategoriesTabs.this,
                                    "Error: " + e.getMessage(),
                                    Toast.LENGTH_LONG).show();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                Toast.makeText(CategoriesTabs.this,
                        error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        try{
            AppController.getInstance().addToRequestQueue(req);

        }catch(Exception e)
        {
            Log.e("BlankFragment3", e+ " This is the error");
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        mProgress.dismiss();
    }
}




