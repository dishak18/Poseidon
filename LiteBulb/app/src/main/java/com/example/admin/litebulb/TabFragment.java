package com.example.admin.litebulb;

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

import com.example.admin.litebulb.Adapters.CategoryItemAdapter;
import com.example.admin.litebulb.Adapters.MoreCategoryItemAdapter;
import com.example.admin.litebulb.Models.CategoryItem;
import com.example.admin.litebulb.Models.MoreMainCategory;
import com.example.admin.litebulb.Models.TabsModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created on 10/2/18.
 */

public class TabFragment extends android.support.v4.app.Fragment {
    private TabsModel tabInfo = new TabsModel();
    private RecyclerView itemrv;
    private TextView noItemText;
    private Spinner subCategories;
    private DatabaseReference mCategoriesRef;
    private DatabaseReference databaseReference;
    private ArrayList<CategoryItem> categoryItems = new ArrayList<>();
    private CategoryItemAdapter categoryItemAdapter;
    private MoreCategoryItemAdapter moreCategoryItemAdapter;
    private ArrayList<MoreMainCategory> moreMainCategories = new ArrayList<>();

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
        if(!tabInfo.getMetaTitle().equalsIgnoreCase("more")) {
            View rootView = inflater.inflate(R.layout.fragment_categories_tabs, container, false);
            noItemText = (TextView) rootView.findViewById(R.id.no_item_text);
            itemrv = (RecyclerView) rootView.findViewById(R.id.items_rv);
            subCategories = (Spinner) rootView.findViewById(R.id.sub_categories_dropdown);
            itemrv.setLayoutManager(new LinearLayoutManager(getContext()));
            categoryItemAdapter = new CategoryItemAdapter(categoryItems);
            itemrv.setAdapter(categoryItemAdapter);

            mCategoriesRef = FirebaseDatabase.getInstance().getReference().child("categories");
            databaseReference = FirebaseDatabase.getInstance().getReference();

            final ArrayList<String> subCategoriesSpinner = new ArrayList<>();
            final ArrayList<String> subCategoriesIds = new ArrayList<>();
            final ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(getContext(), R.layout.sub_categories_spinner_item, subCategoriesSpinner);

            spinnerArrayAdapter.setDropDownViewResource(R.layout.sub_categories_spinner_item);
            subCategories.setAdapter(spinnerArrayAdapter);

            mCategoriesRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    subCategoriesSpinner.clear();
                    subCategoriesIds.clear();
                    for (DataSnapshot categories : dataSnapshot.getChildren()) {
                        int i = 0;
                        if (categories.child("sub_of").getValue(String.class).equalsIgnoreCase(tabInfo.getCategoryId())) {
                            Log.e("TabFragment", "Sub Categories : " + categories.child("meta_title").getValue(String.class));
                            subCategoriesIds.add(categories.child("id").getValue(String.class));
                            subCategoriesSpinner.add(categories.child("meta_title").getValue(String.class));
                            spinnerArrayAdapter.notifyDataSetChanged();
                        }
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
            AdapterView.OnItemSelectedListener countrySelectedListener = new AdapterView.OnItemSelectedListener() {

                @Override
                public void onItemSelected(AdapterView<?> spinner, View container,
                                           final int position, long id) {
                    Log.e("TabFragment", "Item Selected : " + spinner.getItemAtPosition(position));

                    databaseReference.child("items_to_category").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            categoryItems.clear();
                            for (final DataSnapshot itemsToCategory : dataSnapshot.getChildren()) {
                                final String categoryIdsPartOf = itemsToCategory.child("categories").getValue(String.class);
                                String[] category = categoryIdsPartOf.split(",");
                                //ArrayList<String> categoryPartOf = new ArrayList<>(Arrays.asList(category));
                                if (Arrays.asList(category).contains(subCategoriesIds.get(position))) {
                                    Log.e("TabFragment", "subCategoriesIds are: " + subCategoriesIds.get(position));
                                    databaseReference.child("items").addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                            int counter = 0;
                                            for (DataSnapshot items : dataSnapshot.getChildren()) {
                                                Log.e("TabFragment", "Item name : " + items.child("name").getValue(String.class));
                                                if (items.child("id").getValue(String.class)
                                                        .equals(itemsToCategory.child("item_id").getValue(String.class))) {
                                                    final CategoryItem ci = new CategoryItem();
                                                    counter++;
                                                    Log.e("TabFragment", "inside if statement");
                                                    ci.setItemId(items.child("id").getValue(String.class));
                                                    ci.setName(items.child("name").getValue(String.class));
                                                    ci.setPrice(items.child("price").getValue(String.class));
                                                    ci.setCategory(categoryIdsPartOf);
                                                    categoryItems.add(ci);
                                                    noItemText.setVisibility(View.GONE);
                                                }
                                            }
                                            Log.e("TabFragment", "No. of items are : " + counter);
                                        }

                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {

                                        }
                                    });
                                }
                            }
                            categoryItemAdapter.notifyDataSetChanged();
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }

                @Override
                public void onNothingSelected(AdapterView<?> arg0) {
                    // TODO Auto-generated method stub
                }
            };
            subCategories.setOnItemSelectedListener(countrySelectedListener);
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
            databaseReference = FirebaseDatabase.getInstance().getReference().child("categories");
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
            });
            return rootView;
        }
    }
}