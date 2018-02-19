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

import com.example.admin.litebulb.Adapters.AlbumAdapterWeeklyFreebiesAll;
import com.example.admin.litebulb.Models.album;
import com.example.admin.litebulb.SQL.AppConfig;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class ViewAllWeeklyFragment extends Fragment {
    private RecyclerView rvWeekly;
    private AlbumAdapterWeeklyFreebiesAll adapter_weekly_free;
    private List<album> weekly_free;
    private ProgressDialog pDialog;

    Activity referenceActivity;
    DatabaseReference myRef;

    FirebaseDatabase database;
    View parentHolder;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        referenceActivity = getActivity();
        database = FirebaseDatabase.getInstance();
        parentHolder = inflater.inflate(R.layout.fragment_view_all_weekly, container,
                false);
        myRef = database.getReference().child("items");

        rvWeekly = (RecyclerView) parentHolder.findViewById(R.id.recycler_view_featured);
        weekly_free = new ArrayList<>();
        adapter_weekly_free = new AlbumAdapterWeeklyFreebiesAll(getActivity(), weekly_free );
        rvWeekly.setLayoutManager(new LinearLayoutManager(getContext()));
        rvWeekly.setItemAnimator(new DefaultItemAnimator());
        rvWeekly.setAdapter(adapter_weekly_free);
        prepareCards();

        adapter_weekly_free.notifyDataSetChanged();
        return parentHolder;
    }

    private void prepareCards() {

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    if (dataSnapshot1.child("free_request").getValue().equals("true") && dataSnapshot1.child("free_file").getValue().equals("true")) {
                            album fire2 = new album();
                            fire2.setName((dataSnapshot1.child("name").getValue(String.class)).replace("&amp;", ""));
                            fire2.setprice(dataSnapshot1.child("price").getValue(String.class));
                            String image_url = AppConfig.URL_PHOTOS + dataSnapshot1.child("thumbnail").getValue(String.class);
                            fire2.setThumbnail(image_url);
                            weekly_free.add(fire2);
                            adapter_weekly_free.notifyDataSetChanged();

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