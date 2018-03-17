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
import com.example.admin.litebulb.Adapters.AdapterAuthorsAll;
import com.example.admin.litebulb.Models.Users_get;
import com.example.admin.litebulb.R;
import com.example.admin.litebulb.SQL.AppConfig;
import com.example.admin.litebulb.SQL.AppController;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.bumptech.glide.gifdecoder.GifHeaderParser.TAG;


public class BlankFragment2 extends Fragment {
    private RecyclerView rvFeaturedAuthors;
    private AdapterAuthorsAll adapter_featured_authors;
    private List<Users_get> featured_authors;
    private ProgressDialog pDialog;

    Activity referenceActivity;
    View parentHolder;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        referenceActivity = getActivity();
        parentHolder = inflater.inflate(R.layout.fragment_blank_fragment2, container,
                false);
        rvFeaturedAuthors = (RecyclerView) parentHolder.findViewById(R.id.recycler_view);
        featured_authors= new ArrayList<>();
        adapter_featured_authors = new AdapterAuthorsAll(getActivity(), featured_authors );
        rvFeaturedAuthors.setLayoutManager(new LinearLayoutManager(getContext()));
        rvFeaturedAuthors.setItemAnimator(new DefaultItemAnimator());
        rvFeaturedAuthors.setAdapter(adapter_featured_authors);
        makeJsonArrayRequest();

        adapter_featured_authors.notifyDataSetChanged();
        return parentHolder;
    }

    private void makeJsonArrayRequest() {

        JsonArrayRequest req = new JsonArrayRequest(AppConfig.URL_USER_FEATURED,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        //Log.e(TAG, "This is it"+response.toString());


                        try {
                            for (int i = 0; i < response.length(); i++) {


                                JSONObject person = (JSONObject) response.get(i);
                                //int id=person.getInt("id");
                                String name = person.getString("username");
                                String featured_author = person.getString("featured_author");
                                int sales = person.getInt("sales");
                                int items = person.getInt("items");
                                String thumbnail=person.getString("avatar");
                                String image_url = AppConfig.URL_PHOTOS +thumbnail;
                                    Users_get fire3= new Users_get();
                                    //Log.e("BLANKFRAGMENT3", "in the loop for the "+i+"th time with name "+ name);
                                    fire3.setUsername(name);
                                    fire3.setFeatured_author(featured_author);
                                    fire3.setItems(items);
                                    fire3.setSales(sales);
                                    fire3.setThumbnail(image_url);
                                    featured_authors.add(fire3);
                                    adapter_featured_authors.notifyDataSetChanged();


                            }
                            /*txtResponse.setText(jsonResponse);*/
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