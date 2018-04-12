package com.example.admin.litebulb;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.example.admin.litebulb.Adapters.CategoryItemAdapter;
import com.example.admin.litebulb.Adapters.MoreCategoryItemAdapter;
import com.example.admin.litebulb.Models.CategoryItem;
import com.example.admin.litebulb.Models.MoreMainCategory;
import com.example.admin.litebulb.Models.TabsModel;
import com.example.admin.litebulb.SQL.AppConfig;
import com.example.admin.litebulb.SQL.AppController;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static com.bumptech.glide.gifdecoder.GifHeaderParser.TAG;


public class TabFragment extends android.support.v4.app.Fragment {
    private TabsModel tabInfo = new TabsModel();
    private RecyclerView itemrv;
    private TextView noItemText;
    private Spinner subCategories;
    private DatabaseReference mCategoriesRef;
    private DatabaseReference databaseReference;
    private ArrayList<CategoryItem> categoryItems = new ArrayList<>();
    private ArrayList<CategoryItem> categoryItemsFinal = new ArrayList<>();
    private CategoryItemAdapter categoryItemAdapter;
    private MoreCategoryItemAdapter moreCategoryItemAdapter;
    private ArrayList<MoreMainCategory> moreMainCategories = new ArrayList<>();
    final ArrayList<String> subCategoriesSpinner = new ArrayList<>();
    final ArrayList<String> subCategoriesIds = new ArrayList<>();
    final ArrayList<CategoryItem> categoryIdToName = new ArrayList<>();
    private int positionSpinner = 0;
    private ProgressDialog progressDialog;
    private AdapterView.OnItemSelectedListener countrySelectedListener;
    /*final ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(getContext(),
            R.layout.sub_categories_spinner_item, subCategoriesSpinner);*/

    final ArrayList<CategoryItem> itemsToCategory= new ArrayList<>();

    private SharedPreferences preferences;

    public TabFragment() {
    }

    public static TabFragment getInstance(TabsModel tabInfo) {
        TabFragment tab = new TabFragment();
        tab.tabInfo = tabInfo;
        return tab;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //FOR THE FIRST SIX TABS (NOT INCLUDING THE MORE TAB)
        preferences = getContext().getSharedPreferences("preferences", Context.MODE_PRIVATE);
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setTitle("Loading...");
        if(!tabInfo.getMetaTitle().equalsIgnoreCase("more")) {
            View rootView = inflater.inflate(R.layout.fragment_categories_tabs, container, false);
            noItemText = (TextView) rootView.findViewById(R.id.no_item_text);
            itemrv = (RecyclerView) rootView.findViewById(R.id.items_rv);
            subCategories = (Spinner) rootView.findViewById(R.id.sub_categories_dropdown);
            itemrv.setLayoutManager(new LinearLayoutManager(getContext()));
            categoryItemAdapter = new CategoryItemAdapter(categoryItemsFinal, getContext());
            itemrv.setAdapter(categoryItemAdapter);

            mCategoriesRef = FirebaseDatabase.getInstance().getReference().child("categories");
            databaseReference = FirebaseDatabase.getInstance().getReference();

            progressDialog.show();
            makeJsonArrayRequest();
            countrySelectedListener = new AdapterView.OnItemSelectedListener() {

                @Override
                public void onItemSelected(AdapterView<?> spinner, View container,
                                           final int position, long id) {
                    Log.e("TabFragment","on item selected");
                    categoryItemsFinal.clear();
                    Log.e("TabFragment","Size of itemsToCategory : "+itemsToCategory.size());
                    Log.e("TabFragment","Size of categoryItems : "+categoryItems.size());
                    Log.e("TabFragment","Size of subCategories : "+subCategoriesIds.size());
                    for(int i=0; i<itemsToCategory.size(); i++){
                        Log.e("TabFragment","for entered");
                        if(itemsToCategory.get(i).getCategory().contains(subCategoriesIds.get(position)) &&
                                itemsToCategory.get(i).getCategory().contains(tabInfo.getCategoryId())){
                            Log.e("TabFragment","if entered; value of i : "+i);
                            for(int j=0; j<categoryItems.size(); j++){
                                if(itemsToCategory.get(i).getItemId().equalsIgnoreCase(categoryItems.get(j).getItemId())){
                                    Log.e("TabFragment","if entered");
                                    CategoryItem categoryItem = new CategoryItem();
                                    categoryItem.setName(categoryItems.get(j).getName());
                                    categoryItem.setPrice(categoryItems.get(j).getPrice());
                                    categoryItem.setItemId(categoryItems.get(j).getItemId());
                                    categoryItem.setThumbnail(categoryItems.get(j).getThumbnail());
                                    categoryItem.setCategory("");
                                    int counter=0;
                                    for(int k=0; k<categoryIdToName.size(); k++){
                                        Log.e("TabFragment","Value of k : " + k);
                                        if(itemsToCategory.get(i).getCategory().contains(categoryIdToName.get(k).getItemId())){
                                            if(counter==0){
                                                categoryItem.setCategory(categoryItem.getCategory()+categoryIdToName.get(k).getCategory());
                                            }else
                                                categoryItem.setCategory(categoryItem.getCategory()+", "+categoryIdToName.get(k).getCategory());
                                            counter++;
                                        }
                                    }
                                    //categoryItem.setCategory(itemsToCategory.get(i).getCategory());
                                    categoryItemsFinal.add(categoryItem);
                                }
                            }
                        }
                    }
                    if(categoryItemsFinal.size()>0){
                        noItemText.setVisibility(View.GONE);
                    }
                    categoryItemAdapter.notifyDataSetChanged();
                }

                @Override
                public void onNothingSelected(AdapterView<?> arg0) {
                    // TODO Auto-generated method stub
                }
            };

            Log.e("TabFragment","makeJsonArray exited");
            populate();
            Log.e("TabFragment","populate exited");
            Log.e("TabFragment","Spinner Position : " + positionSpinner);
            progressDialog.dismiss();

            //subCategories.performClick();

            return rootView;
        }
        //FOR THE MORE TAB
        else{
            View rootView = inflater.inflate(R.layout.fragment_categories_more_tab, container, false);
            noItemText = (TextView) rootView.findViewById(R.id.no_item_text);
            itemrv = (RecyclerView) rootView.findViewById(R.id.more_category_rv);
            itemrv.setLayoutManager(new LinearLayoutManager(getContext()));
            moreCategoryItemAdapter = new MoreCategoryItemAdapter(moreMainCategories);
            itemrv.setAdapter(moreCategoryItemAdapter);
            makeJsonArrayRequestForMainCategory();
            /*databaseReference = FirebaseDatabase.getInstance().getReference().child("categories");
            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    int counter = 0;
                    for(DataSnapshot categories : dataSnapshot.getChildren()){
                        if(categories.child("sub_of").getValue(String.class).equalsIgnoreCase("0")){
                            counter = counter+1;
                            if(counter>6){
                                MoreMainCategory moreMainCategory = new MoreMainCategory();
                                moreMainCategory.setMainCategoryId(categories.child("id").getValue(String.class));
                                moreMainCategory.setMainCategory(categories.child("meta_title").getValue(String.class));
                                moreMainCategories.add(moreMainCategory);
                            }
                        }
                    }
                    moreCategoryItemAdapter.notifyDataSetChanged();
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });*/
            return rootView;
        }
    }

    private void makeJsonArrayRequestForMainCategory() {
        JsonArrayRequest req = new JsonArrayRequest(AppConfig.URL_CATEGORIES,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            moreMainCategories.clear();
                            int counter=0;
                            for (int i = 0; i < response.length(); i++) {

                                JSONObject person = (JSONObject) response.get(i);
                                String sub_of = person.getString("sub_of");
                                if(sub_of.equalsIgnoreCase("0")) {
                                    Log.e("TabFragment","Outside counter if ---- Title : "+ person.getString("meta_title"));
                                    if(counter>5) {
                                        Log.e("TabFragment","Inside counter if ---- Title : "+ person.getString("meta_title"));
                                        int id_of_categories_table = person.getInt("id");
                                        String meta_title = person.getString("meta_title");
                                        MoreMainCategory moreMainCategory = new MoreMainCategory();
                                        moreMainCategory.setMainCategoryId(""+id_of_categories_table);
                                        moreMainCategory.setMainCategory(""+meta_title);
                                        moreMainCategories.add(moreMainCategory);
                                    }
                                    counter++;
                                }
                            }
                            moreCategoryItemAdapter.notifyDataSetChanged();
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(getContext(),
                                    "Error: " + e.getMessage(),
                                    Toast.LENGTH_LONG).show();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                Toast.makeText(getContext(),
                        error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        Log.e("TabFragment","req exited");
        try{
            AppController.getInstance().addToRequestQueue(req);

        }catch(Exception e)
        {
            Log.e("BlankFragment3", e+ " This is the error");
        }
    }

    private void populate() {

        subCategories.setOnItemSelectedListener(countrySelectedListener);

    }

    private void makeJsonArrayRequest() {
        final ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(getContext(),
                R.layout.sub_categories_spinner_item, subCategoriesSpinner);

        JsonArrayRequest req = new JsonArrayRequest(AppConfig.URL_CATEGORIES,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            spinnerArrayAdapter.setDropDownViewResource(R.layout.sub_categories_spinner_item);
                            subCategories.setAdapter(spinnerArrayAdapter);
                            subCategoriesSpinner.clear();
                            subCategoriesIds.clear();
                            categoryIdToName.clear();
                            int k=0;
                            for (int i = 0; i < response.length(); i++) {

                                JSONObject person = (JSONObject) response.get(i);

                                int id_of_categories_table = person.getInt("id");
                                String meta_title = person.getString("meta_title");
                                String sub_of = person.getString("sub_of");

                                CategoryItem categoryItem = new CategoryItem();
                                categoryItem.setItemId(id_of_categories_table+"");
                                categoryItem.setCategory(meta_title);
                                categoryIdToName.add(categoryItem);

                                if(sub_of.equalsIgnoreCase(tabInfo.getCategoryId())){
                                    subCategoriesSpinner.add(meta_title);
                                    subCategoriesIds.add(id_of_categories_table+"");
                                    spinnerArrayAdapter.notifyDataSetChanged();
                                    if(preferences.getString("category","").contains(tabInfo.getMetaTitle()) && preferences.getString("subCategory","").contains(meta_title)){
                                        Log.e("TabFragment","Value of k : "+k);
                                        positionSpinner = k;
                                        subCategories.setOnItemSelectedListener(countrySelectedListener);
                                        subCategories.setSelection(positionSpinner);
                                    }
                                    k++;
                                }
                                //add your if code here
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(getContext(),
                                    "Error: " + e.getMessage(),
                                    Toast.LENGTH_LONG).show();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                Toast.makeText(getContext(),
                        error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        Log.e("TabFragment","req exited");
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
                            categoryItems.clear();
                            for (int i = 0; i < response.length(); i++) {

                                JSONObject person = (JSONObject) response.get(i);

                                int id_of_item_table = person.getInt("id");
                                String name = person.getString("name");
                                int price = person.getInt("price");
                                String thumbnail = person.getString("thumbnail");

                                CategoryItem categoryItem = new CategoryItem();
                                categoryItem.setItemId(id_of_item_table+"");
                                categoryItem.setName(name);
                                categoryItem.setPrice(price+"");
                                categoryItem.setThumbnail(thumbnail);

                                categoryItems.add(categoryItem);
                                //add your if code here


                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(getContext(),
                                    "Error: " + e.getMessage(),
                                    Toast.LENGTH_LONG).show();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                Toast.makeText(getContext(),
                        error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        try{
            AppController.getInstance().addToRequestQueue(req1);

        }catch(Exception e)
        {
            Log.e("BlankFragment3", e+ " This is the error");
        }
        Log.e("TabFragment","req1 exited");
        JsonArrayRequest req2= new JsonArrayRequest(AppConfig.URL_ITEMS_TO_CATEGORY,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            itemsToCategory.clear();
                            Log.e("TabFragment","Length of response : "+response.length());
                            for (int i = 0; i < response.length(); i++) {

                                JSONObject person = (JSONObject) response.get(i);
                                Log.e("TabFragment","Categories" + person.getString("categories") +
                                        "item_id : "+person.getInt("item_id"));
                                String categories = person.getString("categories");
                                int item_id = person.getInt("item_id");
                                CategoryItem categoryItem = new CategoryItem();
                                categoryItem.setItemId(item_id+"");
                                categoryItem.setCategory(categories);

                                itemsToCategory.add(categoryItem);
                                //add your if code here


                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(getContext(),
                                    "Error: " + e.getMessage(),
                                    Toast.LENGTH_LONG).show();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                Toast.makeText(getContext(),
                        error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        try{
            AppController.getInstance().addToRequestQueue(req2);

        }catch(Exception e)
        {
            Log.e("BlankFragment3", e+ " This is the error");
        }
        Log.e("TabFragment","req2 exited");

    }

    @Override
    public void onStart() {
        super.onStart();
    }
}