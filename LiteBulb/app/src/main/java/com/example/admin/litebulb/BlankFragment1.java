package com.example.admin.litebulb;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.admin.litebulb.Adapters.AdapterAuthors;
import com.example.admin.litebulb.Adapters.AdapterFeatured;
import com.example.admin.litebulb.Adapters.AlbumAdapterWeeklyFreebies;
import com.example.admin.litebulb.Models.Users_get;
import com.example.admin.litebulb.Models.album;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONArray;

import java.util.List;


public class BlankFragment1 extends Fragment {
    private RecyclerView rvWeekly, rvFeaturedItems, rvFeaturedAuthors;
    private AlbumAdapterWeeklyFreebies adapter_weekly_free;
    private AdapterFeatured adapter_featured_items;
    private AdapterAuthors adapter_featured_authors;
    private List<album> featured_items, weekly_free;
    private ProgressDialog pDialog;
    private TextView view_featured_items, view_featured_authors, view_weekly;

    private TextView txtResponse;

    // temporary string to show the parsed response
    private String jsonResponse;
    private List<Users_get> featured_authors;
    Activity referenceActivity;
    DatabaseReference myRef ;
    private JSONArray result;
    FirebaseDatabase database;
    String tag_json_arry = "json_array_req";
    View parentHolder;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        referenceActivity = getActivity();
        return parentHolder;
    }
}