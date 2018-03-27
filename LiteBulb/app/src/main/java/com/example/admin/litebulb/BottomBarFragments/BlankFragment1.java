package com.example.admin.litebulb.BottomBarFragments;

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
import com.example.admin.litebulb.Adapters.AdapterFeaturedAll;
import com.example.admin.litebulb.Adapters.AlbumAdapterWeeklyFreebies;
import com.example.admin.litebulb.Models.album;
import com.example.admin.litebulb.R;
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


public class BlankFragment1 extends Fragment {
    private RecyclerView rvFeaturedItems;
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
      //  prepareCards();
        makeJsonArrayRequestForFeaturedItems();

        adapter_featured_items.notifyDataSetChanged();
        return parentHolder;
    }
    private void showpDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hidepDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }

    private void makeJsonArrayRequestForFeaturedItems() {
        //showpDialog();

        JsonArrayRequest req = new JsonArrayRequest(AppConfig.URL_ITEM,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            int count_featured_items=0;
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject item = (JSONObject) response.get(i);
                                int id=item.getInt("id");

                                String name = item.getString("name");
                                int price = item.getInt("price");
                                String weekly_from=item.getString("weekly_from");
                                String weekly_to=item.getString("weekly_to");
                                String thumbnail=item.getString("thumbnail");
                                String image_url = AppConfig.URL_PHOTOS +thumbnail;

                                if (!weekly_from.equals("0000-00-00") && !weekly_to.equals("0000-00-00")) {
                                    if (count_featured_items > 4) {
                                        break;

                                    } else {
                                        album fire3= new album();
                                        //Log.e("BLANKFRAGMENT3", "in the loop for the "+i+"th time with name "+ name);
                                        fire3.setName(name);
                                        fire3.setprice(price);
                                        fire3.setID(id);
                                        fire3.setThumbnail(image_url);
                                        featured_items.add(fire3);
                                        adapter_featured_items.notifyDataSetChanged();

                                    }
                                }
                                else
                                {
                                    continue;
                                }


                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(referenceActivity,
                                    "Error: " + e.getMessage(),
                                    Toast.LENGTH_LONG).show();
                        }
          //              hidepDialog();
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