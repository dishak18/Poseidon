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
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.example.admin.litebulb.Adapters.AdapterItemsAll;
import com.example.admin.litebulb.Models.album;
import com.example.admin.litebulb.SQL.AppConfig;
import com.example.admin.litebulb.SQL.AppController;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.bumptech.glide.gifdecoder.GifHeaderParser.TAG;


public class ViewAllItems extends Fragment {
    private RecyclerView rvItems;
    private AdapterItemsAll adapter_items_all;
    private List<album> items_all;
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
        parentHolder = inflater.inflate(R.layout.fragment_view_all_items, container,
                false);
        myRef = database.getReference().child("items");

        rvItems = (RecyclerView) parentHolder.findViewById(R.id.recycler_view_items);
        items_all = new ArrayList<>();
        adapter_items_all = new AdapterItemsAll(getActivity(), items_all);
        rvItems.setLayoutManager(new LinearLayoutManager(getContext()));
        rvItems.setItemAnimator(new DefaultItemAnimator());
        rvItems.setAdapter(adapter_items_all);
        //prepareCards();
        makeJsonArrayRequestForWeekly();
        adapter_items_all.notifyDataSetChanged();
        return parentHolder;
    }
    private void makeJsonArrayRequestForWeekly() {


        JsonArrayRequest req = new JsonArrayRequest(AppConfig.URL_ITEM,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject item = (JSONObject) response.get(i);

                                String name = item.getString("name");
                                int id=item.getInt("id");
                                int price = item.getInt("price");
                                String thumbnail=item.getString("thumbnail");
                                String image_url = AppConfig.URL_PHOTOS +thumbnail;
                                    album fire3= new album();
                                    fire3.setName(name);
                                    fire3.setprice(price);
                                    fire3.setID(id);
                                    fire3.setThumbnail(image_url);
                                    items_all.add(fire3);
                                    adapter_items_all.notifyDataSetChanged();


                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(referenceActivity,
                                    "Error: " + e.getMessage(),
                                    Toast.LENGTH_LONG).show();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                Toast.makeText(referenceActivity,
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


}