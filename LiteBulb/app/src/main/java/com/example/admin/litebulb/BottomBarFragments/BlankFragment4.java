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
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.example.admin.litebulb.Adapters.AdapterFeaturedAll;
import com.example.admin.litebulb.Models.album;
import com.example.admin.litebulb.R;
import com.example.admin.litebulb.SQL.AppConfig;
import com.example.admin.litebulb.SQL.AppController;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.bumptech.glide.gifdecoder.GifHeaderParser.TAG;


public class BlankFragment4 extends Fragment {
    private RecyclerView rvPopularItems;
    private AdapterFeaturedAll adapter_popular_items;
    private List<album> popular_items;
    private ProgressDialog pDialog;
    int count_featured_items=0;
    TextView oops;
    Activity referenceActivity;
    View parentHolder;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        referenceActivity = getActivity();
        parentHolder = inflater.inflate(R.layout.fragment_blank_fragment4, container,
                false);
        rvPopularItems = (RecyclerView) parentHolder.findViewById(R.id.recycler_view_featured);
        popular_items= new ArrayList<>();
        adapter_popular_items = new AdapterFeaturedAll(getActivity(), popular_items );
        rvPopularItems.setLayoutManager(new LinearLayoutManager(getContext()));
        rvPopularItems.setItemAnimator(new DefaultItemAnimator());
        rvPopularItems.setAdapter(adapter_popular_items);
        oops=(TextView) parentHolder.findViewById(R.id.oops);

        makeJsonArrayRequestForPopularItems();
        if(count_featured_items==0)
        {
            oops.setText(getResources().getString(R.string.oops));
        }

        adapter_popular_items.notifyDataSetChanged();
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


    private void makeJsonArrayRequestForPopularItems() {
        //showpDialog();

        JsonArrayRequest req = new JsonArrayRequest(AppConfig.URL_ITEM,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {

                            for (int i = 0; i < response.length(); i++) {
                                JSONObject item = (JSONObject) response.get(i);
                                int id=item.getInt("id");

                                String name = item.getString("name");
                                int price = item.getInt("price");
                                int ratings=item.getInt("rating");
                                String thumbnail=item.getString("thumbnail");
                                String image_url = AppConfig.URL_PHOTOS +thumbnail;

                                if (ratings>=5) {
                                    count_featured_items++;
                                        album fire3= new album();
                                        //Log.e("BLANKFRAGMENT3", "in the loop for the "+i+"th time with name "+ name);
                                        fire3.setName(name);
                                        fire3.setprice(price);
                                        fire3.setID(id);
                                        fire3.setThumbnail(image_url);
                                        popular_items.add(fire3);
                                        adapter_popular_items.notifyDataSetChanged();

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