package com.example.admin.litebulb;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.admin.litebulb.Adapters.AdapterFeaturedAll;
import com.example.admin.litebulb.Adapters.AlbumAdapterWeeklyFreebies;
import com.example.admin.litebulb.Models.album;
import com.example.admin.litebulb.SQL.AppConfig;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class BlankFragment1 extends Fragment {
    private RecyclerView rvWeekly, rvFeaturedItems, rvFeaturedAuthors;
    private AlbumAdapterWeeklyFreebies adapter_weekly_free;
    private AdapterFeaturedAll adapter_featured_items;
    private List<album> featured_items;
    private ProgressDialog pDialog;

    Activity referenceActivity;
    DatabaseReference myRef ;

    FirebaseDatabase database;
    View parentHolder;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        referenceActivity = getActivity();
        database = FirebaseDatabase.getInstance();
        parentHolder = inflater.inflate(R.layout.fragment_blank_fragment1, container,
                false);
        myRef = database.getReference().child("items");

        rvFeaturedItems = (RecyclerView) parentHolder.findViewById(R.id.recycler_view_featured);
        featured_items = new ArrayList<>();
        adapter_featured_items = new AdapterFeaturedAll(getActivity(), featured_items );
        rvFeaturedItems.setLayoutManager(new LinearLayoutManager(getContext()));
        rvFeaturedItems.setItemAnimator(new DefaultItemAnimator());
        rvFeaturedItems.setAdapter(adapter_featured_items);
        prepareCards();

        adapter_featured_items.notifyDataSetChanged();
        return parentHolder;
    }

    private void prepareCards() {

        myRef.addValueEventListener(new ValueEventListener() {
            int count=0;
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    if (!dataSnapshot1.child("weekly_from").getValue().equals("0000-00-00") && !dataSnapshot1.child("weekly_to").getValue().equals("0000-00-00")) {
                        album fire = new album();
                        fire.setName((dataSnapshot1.child("name").getValue(String.class)).replace("&amp;", ""));
                        fire.setprice(dataSnapshot1.child("price").getValue(String.class));
                        String image_url = AppConfig.URL_PHOTOS + dataSnapshot1.child("thumbnail").getValue(String.class);
                        //Log.e("This is the URL", image_url);
                        fire.setThumbnail(image_url);
                        featured_items.add(fire);
                        adapter_featured_items.notifyDataSetChanged();

                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value

                Log.e("Hello", "Failed to read value.", error.toException());
            }
        });
    }
}