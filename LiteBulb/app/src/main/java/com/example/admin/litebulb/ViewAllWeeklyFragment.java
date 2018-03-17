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
import com.example.admin.litebulb.Adapters.AlbumAdapterWeeklyFreebiesAll;
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

        rvWeekly = (RecyclerView) parentHolder.findViewById(R.id.recycler_view_weekly);
        weekly_free = new ArrayList<>();
        adapter_weekly_free = new AlbumAdapterWeeklyFreebiesAll(getActivity(), weekly_free);
        rvWeekly.setLayoutManager(new LinearLayoutManager(getContext()));
        rvWeekly.setItemAnimator(new DefaultItemAnimator());
        rvWeekly.setAdapter(adapter_weekly_free);
        //prepareCards();
        makeJsonArrayRequestForWeekly();
        adapter_weekly_free.notifyDataSetChanged();
        return parentHolder;
    }
    private void makeJsonArrayRequestForWeekly() {


        JsonArrayRequest req = new JsonArrayRequest(AppConfig.URL_ITEM,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            int count_weekly=0;
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject item = (JSONObject) response.get(i);

                                String name = item.getString("name");
                                String free_request = item.getString("free_request");
                                String free_file = item.getString("free_file");
                                int id=item.getInt("id");
                                int price = item.getInt("price");
                                String thumbnail=item.getString("thumbnail");
                                String image_url = AppConfig.URL_PHOTOS +thumbnail;

                                if(free_request.equals("true")&&free_file.equals("true"))
                                {
                                    album fire3= new album();
                                    //Log.e("BLANKFRAGMENT3", "in the loop for the "+i+"th time with name "+ name);
                                    fire3.setName(name);
                                    fire3.setID(id);
                                    fire3.setprice(price);
                                    fire3.setThumbnail(image_url);
                                    weekly_free.add(fire3);
                                    adapter_weekly_free.notifyDataSetChanged();
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