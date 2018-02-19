package com.example.admin.litebulb;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
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
import com.example.admin.litebulb.Adapters.AlbumAdapterWeeklyFreebies;
import com.example.admin.litebulb.Models.Users_get;
import com.example.admin.litebulb.Models.album;
import com.example.admin.litebulb.SQL.AppConfig;
import com.example.admin.litebulb.SQL.AppController;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.bumptech.glide.gifdecoder.GifHeaderParser.TAG;


public class BlankFragment3 extends Fragment {
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
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference().child("items");
        parentHolder = inflater.inflate(R.layout.fragment_blank_fragment3, container,
                false);
        pDialog = new ProgressDialog(referenceActivity);
        pDialog.setMessage("Please wait...");

        pDialog.setCancelable(false);
        rvWeekly = (RecyclerView) parentHolder.findViewById(R.id.recycler_view_weekly);
        rvFeaturedItems = (RecyclerView) parentHolder.findViewById(R.id.recycler_view_featured);
        rvFeaturedAuthors = (RecyclerView) parentHolder.findViewById(R.id.recycler_view);

        view_featured_authors=(TextView) parentHolder.findViewById(R.id.view_featured_authors);
        view_featured_items=(TextView) parentHolder.findViewById(R.id.view_featured_items);
        view_weekly=(TextView) parentHolder.findViewById(R.id.view_weekly);

        featured_items = new ArrayList<>();
        featured_authors = new ArrayList<>();
        weekly_free = new ArrayList<>();
        adapter_featured_items = new AdapterFeatured(getActivity(), featured_items );
        adapter_featured_authors = new AdapterAuthors(getActivity(), featured_authors );
        adapter_weekly_free = new AlbumAdapterWeeklyFreebies(getActivity(), weekly_free );
        RecyclerView.LayoutManager mLayoutManager= new LinearLayoutManager(getActivity(), GridLayoutManager.HORIZONTAL, false);
        RecyclerView.LayoutManager mLayoutManager2= new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        RecyclerView.LayoutManager mLayoutManager3= new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);

        rvWeekly.setLayoutManager(mLayoutManager);
        rvFeaturedItems.setLayoutManager(mLayoutManager2);
        rvFeaturedAuthors.setLayoutManager(mLayoutManager3);
        //rvWeekly.addItemDecoration(new GridSpacingItemDecoration(2, dpToPx(10), true));
        rvWeekly.setItemAnimator(new DefaultItemAnimator());
        rvWeekly.setAdapter(adapter_weekly_free);
        //rvFeaturedItems.addItemDecoration(new GridSpacingItemDecoration(2, dpToPx(10), true));
        rvFeaturedItems.setItemAnimator(new DefaultItemAnimator());
        rvFeaturedItems.setAdapter(adapter_featured_items);
        //rvFeaturedAuthors.addItemDecoration(new GridSpacingItemDecoration(2, dpToPx(10), true));
        rvFeaturedAuthors.setItemAnimator(new DefaultItemAnimator());
        rvFeaturedAuthors.setAdapter(adapter_featured_authors);
        prepareCards();
        makeJsonArrayRequest();
        //listView = (ListView) parentHolder.findViewById(R.id.listView);
        //getJSON(AppConfig.URL_USER_FEATURED);
        adapter_weekly_free.notifyDataSetChanged();
        adapter_featured_items.notifyDataSetChanged();
        adapter_featured_authors.notifyDataSetChanged();

        view_weekly.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent=new Intent(referenceActivity, LoginActivity.class);
                startActivity(intent);
            }
        });
        view_featured_items.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BlankFragment1 fragment1 = new BlankFragment1();
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction =fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.contentContainer, fragment1);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();

            }
        });
        view_featured_authors.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent=new Intent(referenceActivity, LoginActivity.class);
                startActivity(intent);
            }
        });

        return parentHolder;
    }
    
    private void prepareCards() {

        myRef.addValueEventListener(new ValueEventListener() {
            int count_weekly=0;
            int count_featured_items=0;

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    if (dataSnapshot1.child("free_request").getValue().equals("true") && dataSnapshot1.child("free_file").getValue().equals("true")) {
                        count_weekly++;
                        if(count_weekly>4)
                        {

                        }
                        else
                        {
                            album fire2 = new album();
                            fire2.setName((dataSnapshot1.child("name").getValue(String.class)).replace("&amp;", ""));
                            fire2.setprice(dataSnapshot1.child("price").getValue(String.class));
                            String image_url = AppConfig.URL_PHOTOS + dataSnapshot1.child("thumbnail").getValue(String.class);
                            fire2.setThumbnail(image_url);
                            weekly_free.add(fire2);
                            adapter_weekly_free.notifyDataSetChanged();
                        }

                    }
                    if (!dataSnapshot1.child("weekly_from").getValue().equals("0000-00-00") && !dataSnapshot1.child("weekly_to").getValue().equals("0000-00-00")) {
                        if(count_featured_items>4)
                        {

                        }
                        else
                        {
                            album fire = new album();
                            fire.setName((dataSnapshot1.child("name").getValue(String.class)).replace("&amp;", ""));
                            fire.setprice(dataSnapshot1.child("price").getValue(String.class));
                            //Image url
                            //String half_url = dataSnapshot1.child("thumbnail").getValue(String.class);
                            //half_url.replaceAll("_", "");
                            String image_url = AppConfig.URL_PHOTOS + dataSnapshot1.child("thumbnail").getValue(String.class);
                            //Log.e("This is the URL", image_url);
                            fire.setThumbnail(image_url);
                            featured_items.add(fire);
                            adapter_featured_items.notifyDataSetChanged();
                        }


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

    private void showpDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hidepDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }
    private void makeJsonArrayRequest() {

        showpDialog();

        JsonArrayRequest req = new JsonArrayRequest(AppConfig.URL_USER_FEATURED,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        //Log.e(TAG, "This is it"+response.toString());


                        try {
                            jsonResponse = "";
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
                            /*txtResponse.setText(jsonResponse);*/
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
                Toast.makeText(referenceActivity,
                        error.getMessage(), Toast.LENGTH_SHORT).show();
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