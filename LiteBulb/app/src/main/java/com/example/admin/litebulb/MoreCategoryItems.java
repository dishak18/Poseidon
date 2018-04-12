package com.example.admin.litebulb;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
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

public class MoreCategoryItems extends AppCompatActivity {

    private TabsModel tabInfo = new TabsModel();
    private RecyclerView itemrv;
    private TextView noItemText;
    private Spinner subCategories;
    private DatabaseReference mCategoriesRef;
    private DatabaseReference databaseReference;
    private ArrayList<CategoryItem> categoryItems = new ArrayList<>();
    private CategoryItemAdapter categoryItemAdapter;
    private MoreCategoryItemAdapter moreCategoryItemAdapter;
    private ProgressDialog progressDialog;
    private ArrayList<MoreMainCategory> moreMainCategories = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_more_category_items);
        noItemText = (TextView) findViewById(R.id.no_item_text);
        itemrv = (RecyclerView) findViewById(R.id.items_rv);
        subCategories = (Spinner) findViewById(R.id.sub_categories_dropdown);
        subCategories.setVisibility(View.GONE);
        itemrv.setLayoutManager(new LinearLayoutManager(this));
        categoryItemAdapter = new CategoryItemAdapter(categoryItems, getApplicationContext());
        itemrv.setAdapter(categoryItemAdapter);
        Intent intent = getIntent();
        final String moreMainCategoryId = intent.getExtras().getString("moreMainCategoryId");
        Log.e("MoreCategoriesItems","The Category Id is : "+moreMainCategoryId);
        mCategoriesRef = FirebaseDatabase.getInstance().getReference().child("categories");
        databaseReference = FirebaseDatabase.getInstance().getReference();
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Loading...");
        progressDialog.show();

     /*   final ArrayList<String> subCategoriesSpinner = new ArrayList<>();
        final ArrayList<String> subCategoriesIds = new ArrayList<>();
        final ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(this, R.layout.sub_categories_spinner_item, subCategoriesSpinner);
        spinnerArrayAdapter.setDropDownViewResource(R.layout.sub_categories_spinner_item);
        subCategories.setAdapter(spinnerArrayAdapter);
        mCategoriesRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                subCategoriesSpinner.clear();
                subCategoriesIds.clear();
                for (DataSnapshot categories : dataSnapshot.getChildren()) {
                    int i = 0;
                    if (categories.child("sub_of").getValue(String.class).equalsIgnoreCase(moreMainCategoryId)) {
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
        });*/


        databaseReference.child("items_to_category").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                categoryItems.clear();
                for (final DataSnapshot itemsToCategory : dataSnapshot.getChildren()) {
                    final String categoryIdsPartOf = itemsToCategory.child("categories").getValue(String.class);
                    //String[] category = categoryIdsPartOf.split(",");
                    //ArrayList<String> categoryPartOf = new ArrayList<>(Arrays.asList(category));
                    if (categoryIdsPartOf.contains(moreMainCategoryId)) {
                        Log.e("TabFragment", "subCategoriesIds are: " + moreMainCategoryId);
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
                                        ci.setCategory("");

                                        databaseReference.child("categories").addValueEventListener(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(DataSnapshot dataSnapshot) {
                                                int counterName=0;
                                                for(DataSnapshot childSnapshot : dataSnapshot.getChildren()){
                                                    if(categoryIdsPartOf.contains(childSnapshot.child("id").getValue(String.class))){
                                                        if(counterName==0){
                                                            ci.setCategory(ci.getCategory()+childSnapshot.child("meta_title").getValue(String.class));
                                                        }else
                                                            ci.setCategory(ci.getCategory()+", "+childSnapshot.child("meta_title").getValue(String.class));
                                                        counterName++;
                                                    }
                                                }
                                                progressDialog.dismiss();
                                            }

                                            @Override
                                            public void onCancelled(DatabaseError databaseError) {

                                            }
                                        });
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
        AdapterView.OnItemSelectedListener countrySelectedListener = new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> spinner, View container,
                                       final int position, long id) {
                Log.e("TabFragment", "Item Selected : " + spinner.getItemAtPosition(position));


            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub
            }
        };
        subCategories.setOnItemSelectedListener(countrySelectedListener);
    }
}