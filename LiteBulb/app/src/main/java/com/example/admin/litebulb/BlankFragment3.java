package com.example.admin.litebulb;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import SQL.AppConfig;


public class BlankFragment3 extends Fragment {
    private RecyclerView recyclerView, recyclerView2, recyclerView3;
    private AlbumAdapter adapter_featured_items, adapter_featured_authors, adapter_weekly_free;
    private List<album> featured_items, featured_authors, weekly_free;
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
        myRef = database.getReference().child("items");

        parentHolder = inflater.inflate(R.layout.fragment_blank_fragment3, container,
                false);

        recyclerView = (RecyclerView) parentHolder.findViewById(R.id.recycler_view);
        recyclerView2 = (RecyclerView) parentHolder.findViewById(R.id.recycler_view_weekly);
        recyclerView3 = (RecyclerView) parentHolder.findViewById(R.id.recycler_view_featured);

        featured_items = new ArrayList<>();
        featured_authors = new ArrayList<>();
        weekly_free = new ArrayList<>();
        adapter_featured_items = new AlbumAdapter(getActivity(), featured_items );
        adapter_featured_authors = new AlbumAdapter(getActivity(), featured_authors );
        adapter_weekly_free = new AlbumAdapter(getActivity(), weekly_free );
        RecyclerView.LayoutManager mLayoutManager= new LinearLayoutManager(getActivity(), GridLayoutManager.HORIZONTAL, false);
        RecyclerView.LayoutManager mLayoutManager2= new LinearLayoutManager(getActivity(),  LinearLayoutManager.HORIZONTAL, false);
        RecyclerView.LayoutManager mLayoutManager3= new LinearLayoutManager(getActivity(),  LinearLayoutManager.HORIZONTAL, false);

        // RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getActivity(), 2);
        recyclerView.setLayoutManager(mLayoutManager);
        // recyclerView.setLayoutManager(layoutManager);
        recyclerView2.setLayoutManager(mLayoutManager2);
        recyclerView3.setLayoutManager(mLayoutManager3);
        //recyclerView.addItemDecoration(new GridSpacingItemDecoration(2, dpToPx(10), true));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter_weekly_free);
        //recyclerView2.addItemDecoration(new GridSpacingItemDecoration(2, dpToPx(10), true));
        recyclerView2.setItemAnimator(new DefaultItemAnimator());
        recyclerView2.setAdapter(adapter_featured_items);
        //recyclerView3.addItemDecoration(new GridSpacingItemDecoration(2, dpToPx(10), true));
        recyclerView3.setItemAnimator(new DefaultItemAnimator());
        recyclerView3.setAdapter(adapter_featured_authors);
        prepareAlbums();
        adapter_featured_items.notifyDataSetChanged();
        adapter_weekly_free.notifyDataSetChanged();
        return parentHolder;
    }

    private void prepareAlbums() {
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot dataSnapshot1 :dataSnapshot.getChildren()){

                    if(!dataSnapshot1.child("weekly_from").getValue().equals("0000-00-00") && !dataSnapshot1.child("weekly_to").getValue().equals("0000-00-00"))
                    {
                        album fire = new album();
                        fire.setName(dataSnapshot1.child("name").getValue(String.class));
                        fire.setprice(dataSnapshot1.child("price").getValue(String.class));
                        // Image url
                        String half_url=dataSnapshot1.child("thumbnail").getValue(String.class);
                        //half_url.replaceAll("_", "");
                        String image_url = AppConfig.URL_PHOTOS+dataSnapshot1.child("thumbnail").getValue(String.class);
                        //Log.e("This is the URL", image_url);
                        fire.setThumbnail(image_url);
                        featured_items.add(fire);
                        adapter_featured_items.notifyDataSetChanged();
                    }
                    if(dataSnapshot1.child("free_request").getValue().equals("true") && dataSnapshot1.child("free_file").getValue().equals("true"))
                    {
                        album fire2 = new album();
                        fire2.setName(dataSnapshot1.child("name").getValue(String.class));
                        fire2.setprice(dataSnapshot1.child("price").getValue(String.class));
                        String image_url = AppConfig.URL_PHOTOS+dataSnapshot1.child("thumbnail").getValue(String.class);
                        fire2.setThumbnail(image_url);
                        weekly_free.add(fire2);
                        adapter_weekly_free.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("Hello", "Failed to read value.", error.toException());
            }
        });

    }
}









