package com.example.admin.litebulb.BottomBarFragments;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
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
import com.example.admin.litebulb.Adapters.AdapterAuthors;
import com.example.admin.litebulb.Adapters.AdapterFeatured;
import com.example.admin.litebulb.Adapters.AdapterItems;
import com.example.admin.litebulb.Adapters.AlbumAdapterWeeklyFreebies;
import com.example.admin.litebulb.Models.Users_get;
import com.example.admin.litebulb.Models.album;
import com.example.admin.litebulb.R;
import com.example.admin.litebulb.SQL.AppConfig;
import com.example.admin.litebulb.SQL.AppController;
import com.example.admin.litebulb.ViewAllFeaturedAuthors;
import com.example.admin.litebulb.ViewAllItems;
import com.example.admin.litebulb.ViewAllWeeklyFragment;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.bumptech.glide.gifdecoder.GifHeaderParser.TAG;


public class BlankFragment3 extends Fragment {
    private RecyclerView rvWeekly, rvFeaturedItems, rvFeaturedAuthors, rvItems;
    private AlbumAdapterWeeklyFreebies adapter_weekly_free;
    private AdapterFeatured adapter_featured_items;
    private AdapterItems adapter_items_all;
    private AdapterAuthors adapter_featured_authors;
    private List<album> featured_items, weekly_free, items_all;
    private ProgressDialog pDialog;
    private TextView view_featured_items, view_featured_authors, view_weekly, view_items;

    private List<Users_get> featured_authors;
    Activity referenceActivity;
    DatabaseReference myRef ;
    FirebaseDatabase database;
    String tag_json_arry = "json_array_req";
    View parentHolder;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        referenceActivity = getActivity();
        database = FirebaseDatabase.getInstance();
        //myRef = database.getReference().child("items");
        parentHolder = inflater.inflate(R.layout.fragment_blank_fragment3, container,
                false);
        pDialog = new ProgressDialog(referenceActivity);
        pDialog.setMessage("Please wait...");

        pDialog.setCancelable(false);
        rvWeekly = (RecyclerView) parentHolder.findViewById(R.id.recycler_view_weekly);
        rvFeaturedItems = (RecyclerView) parentHolder.findViewById(R.id.recycler_view_featured);
        rvFeaturedAuthors = (RecyclerView) parentHolder.findViewById(R.id.recycler_view);
        rvItems = (RecyclerView) parentHolder.findViewById(R.id.recycler_view_items);

        view_featured_authors=(TextView) parentHolder.findViewById(R.id.view_featured_authors);
        view_featured_items=(TextView) parentHolder.findViewById(R.id.view_featured_items);
        view_weekly=(TextView) parentHolder.findViewById(R.id.view_weekly);
        view_items=(TextView) parentHolder.findViewById(R.id.view_items);

        featured_items = new ArrayList<>();
        featured_authors = new ArrayList<>();
        weekly_free = new ArrayList<>();
        items_all = new ArrayList<>();
        adapter_featured_items = new AdapterFeatured(getActivity(), featured_items );
        adapter_items_all = new AdapterItems(getActivity(), items_all );
        adapter_featured_authors = new AdapterAuthors(getActivity(), featured_authors );
        adapter_weekly_free = new AlbumAdapterWeeklyFreebies(getActivity(), weekly_free );
        RecyclerView.LayoutManager mLayoutManager= new LinearLayoutManager(getActivity(), GridLayoutManager.HORIZONTAL, false);
        RecyclerView.LayoutManager mLayoutManager2= new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        RecyclerView.LayoutManager mLayoutManager3= new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        RecyclerView.LayoutManager mLayoutManager4= new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);

        rvWeekly.setLayoutManager(mLayoutManager);
        rvFeaturedItems.setLayoutManager(mLayoutManager2);
        rvFeaturedAuthors.setLayoutManager(mLayoutManager3);
        rvItems.setLayoutManager(mLayoutManager4);
        //rvWeekly.addItemDecoration(new GridSpacingItemDecoration(2, dpToPx(10), true));
        rvWeekly.setItemAnimator(new DefaultItemAnimator());
        rvWeekly.setAdapter(adapter_weekly_free);
        //rvFeaturedItems.addItemDecoration(new GridSpacingItemDecoration(2, dpToPx(10), true));
        rvFeaturedItems.setItemAnimator(new DefaultItemAnimator());
        rvFeaturedItems.setAdapter(adapter_featured_items);
        //rvFeaturedAuthors.addItemDecoration(new GridSpacingItemDecoration(2, dpToPx(10), true));
        rvFeaturedAuthors.setItemAnimator(new DefaultItemAnimator());
        rvFeaturedAuthors.setAdapter(adapter_featured_authors);

        rvItems.setItemAnimator(new DefaultItemAnimator());
        rvItems.setAdapter(adapter_items_all);
        //prepareCards();
        showpDialog();
        makeJsonArrayRequestForFeaturedAuthors();
        makeJsonArrayRequestForWeeklyAndItems();
        hidepDialog();

        adapter_weekly_free.notifyDataSetChanged();
        adapter_featured_items.notifyDataSetChanged();
        adapter_featured_authors.notifyDataSetChanged();
        adapter_items_all.notifyDataSetChanged();

        view_weekly.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ViewAllWeeklyFragment fragment1 = new ViewAllWeeklyFragment();
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction =fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.contentContainer, fragment1);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
                View view= getActivity().findViewById(R.id.tab_center);
                view.clearFocus();

            }
        });
        view_featured_items.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                View view= getActivity().findViewById(R.id.tab_featured);
                view.performClick();


            }
        });
        view_items.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ViewAllItems fragment1 = new ViewAllItems();
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction =fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.contentContainer, fragment1);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
                View view= getActivity().findViewById(R.id.tab_center);
                view.clearFocus();


            }
        });
        view_featured_authors.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ViewAllFeaturedAuthors fragment1 = new ViewAllFeaturedAuthors();
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction =fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.contentContainer, fragment1);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
                View view= getActivity().findViewById(R.id.tab_center);
                view.clearFocus();
            }
        });

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
    private void makeJsonArrayRequestForFeaturedAuthors() {

        showpDialog();

        JsonArrayRequest req = new JsonArrayRequest(AppConfig.URL_USER_FEATURED,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        showpDialog();
                        try {
                            int count_featured_authors=0;
                            for (int i = 0; i < response.length(); i++) {

                                JSONObject person = (JSONObject) response.get(i);

                                String name = person.getString("username");
                                String featured_author = person.getString("featured_author");
                                int sales = person.getInt("sales");
                                int items = person.getInt("items");
                                String thumbnail=person.getString("avatar");
                                String image_url = AppConfig.URL_PHOTOS +thumbnail;

                                if(featured_author.equals("true"))
                                {
                                    count_featured_authors++;
                                    if(count_featured_authors>4)
                                    {
                                       break;
                                    }
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
                                else {
                                    continue;
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(referenceActivity,
                                    "Error: " + e.getMessage(),
                                    Toast.LENGTH_LONG).show();
                        }

                        hidepDialog();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                /*Toast.makeText(referenceActivity,
                        error.getMessage(), Toast.LENGTH_SHORT).show();*/
                Toast.makeText(referenceActivity,
                        "Please check your network connection.",
                        Toast.LENGTH_LONG).show();
                hidepDialog();
            }
        });
        try{
            AppController.getInstance().addToRequestQueue(req, tag_json_arry);

        }catch(Exception e)
        {
            Log.e("BlankFragment3", e+ " This is the error");
        }
    }







    private void makeJsonArrayRequestForWeeklyAndItems() {

        showpDialog();

        JsonArrayRequest req = new JsonArrayRequest(AppConfig.URL_ITEM,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        showpDialog();
                        try {
                            int count_weekly=0;
                            int count_featured_items=0;
                            int count_items=0;
                            Log.e("Fragment3", "This is the length "+response.length());
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject item = (JSONObject) response.get(i);


                                int id=item.getInt("id");

                                String name = item.getString("name");
                                String free_request = item.getString("free_request");
                                String free_file = item.getString("free_file");
                                int price = item.getInt("price");
                                String weekly_from=item.getString("weekly_from");
                                String weekly_to=item.getString("weekly_to");
                                String thumbnail=item.getString("thumbnail");
                                String image_url = AppConfig.URL_PHOTOS +thumbnail;

                                if(count_items>4)
                                {

                                }
                                else
                                {
                                    album fire5= new album();
                                    //Log.e("BLANKFRAGMENT3", "in the loop for the "+i+"th time with name "+ name);
                                    fire5.setID(id);
                                    fire5.setName(name);
                                    fire5.setprice(price);
                                    fire5.setThumbnail(image_url);
                                    items_all.add(fire5);
                                    adapter_items_all.notifyDataSetChanged();
                                    count_items++;
                                }


                                if(free_request.equals("true")&&free_file.equals("true"))
                                {

                                    if(count_weekly>4)
                                    {
                                    }
                                    else{
                                        album fire3= new album();
                                        //Log.e("BLANKFRAGMENT3", "in the loop for the "+i+"th time with name "+ name);
                                        fire3.setID(id);
                                        fire3.setName(name);
                                        fire3.setprice(price);
                                        fire3.setThumbnail(image_url);
                                        weekly_free.add(fire3);
                                        adapter_weekly_free.notifyDataSetChanged();
                                        count_weekly++;
                                    }

                                }
                                if (!weekly_from.equals("0000-00-00") && !weekly_to.equals("0000-00-00")) {

                                    if (count_featured_items > 4) {

                                    } else {
                                        album fire3= new album();
                                        //Log.e("BLANKFRAGMENT3", "in the loop for the "+i+"th time with name "+ name);
                                        fire3.setID(id);
                                        fire3.setName(name);
                                        fire3.setprice(price);
                                        fire3.setThumbnail(image_url);
                                        featured_items.add(fire3);
                                        adapter_featured_items.notifyDataSetChanged();
                                        count_featured_items++;
                                    }
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(referenceActivity,
                                    "Please check your network connection.",
                                    Toast.LENGTH_LONG).show();
                        }

                        hidepDialog();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                Toast.makeText(referenceActivity,
                        "Please check your network connection.",
                        Toast.LENGTH_LONG).show();
                hidepDialog();
            }
        });
        try{
            AppController.getInstance().addToRequestQueue(req, tag_json_arry);

        }catch(Exception e)
        {
            Log.e("BlankFragment3", e+ " This is the error");
        }
    }


}