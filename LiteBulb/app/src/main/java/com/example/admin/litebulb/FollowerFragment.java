package com.example.admin.litebulb;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.admin.litebulb.Adapters.AdpaterFollow;
import com.example.admin.litebulb.Models.Users_get;
import com.example.admin.litebulb.SQL.AppConfig;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static com.example.admin.litebulb.ItemClickFragment.READ_TIMEOUT;
import static com.example.admin.litebulb.ProfileEdit.CONNECTION_TIMEOUT;


public class FollowerFragment extends Fragment {
    private RecyclerView rvFollowers;
    private AdpaterFollow adapter_follow;
    private List<Users_get> followerslist;
    private ProgressDialog pDialog;

    Activity referenceActivity;
    View parentHolder;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        referenceActivity = getActivity();
        parentHolder = inflater.inflate(R.layout.fragment_follower, container,
                false);
        rvFollowers = (RecyclerView) parentHolder.findViewById(R.id.recycler_view);
        followerslist= new ArrayList<>();
        adapter_follow = new AdpaterFollow(getActivity(), followerslist );
        rvFollowers.setLayoutManager(new LinearLayoutManager(getContext()));
        rvFollowers.setItemAnimator(new DefaultItemAnimator());
        rvFollowers.setAdapter(adapter_follow);

        adapter_follow.notifyDataSetChanged();
        return parentHolder;
    }


    private class UsersDetails extends AsyncTask<String, String, String> {
        ProgressDialog pdLoading = new ProgressDialog(referenceActivity);
        HttpURLConnection conn;
        URL url = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            pdLoading.setMessage("\tLoading...");
            pdLoading.setCancelable(false);
            pdLoading.show();

        }

        @Override
        protected String doInBackground(String... params) {
            try {

                // Enter URL address where your json file resides
                // Even you can make call to php file which returns json data
                url = new URL(AppConfig.URL_USER_FEATURED);

            } catch (MalformedURLException e) {

                e.printStackTrace();
                return e.toString();
            }
            try {

                // Setup HttpURLConnection class to send and receive data from php and mysql
                conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(READ_TIMEOUT);
                conn.setConnectTimeout(CONNECTION_TIMEOUT);
                conn.setRequestMethod("GET");

                // setDoOutput to true as we recieve data from json file
                conn.setDoOutput(true);

            } catch (IOException e1) {

                e1.printStackTrace();
                return e1.toString();
            }

            try {

                int response_code = conn.getResponseCode();

                // Check if successful connection made
                if (response_code == HttpURLConnection.HTTP_OK) {

                    // Read data sent from server
                    InputStream input = conn.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(input));
                    StringBuilder result = new StringBuilder();
                    String line;

                    while ((line = reader.readLine()) != null) {
                        result.append(line);
                    }

                    return (result.toString());

                } else {

                    return ("unsuccessful");
                }

            } catch (IOException e) {
                e.printStackTrace();
                return e.toString();
            } finally {
                conn.disconnect();
            }


        }

        @Override
        protected void onPostExecute(String result) {

            //this method will be running on UI thread

            pdLoading.dismiss();
            try {

                JSONArray jArray = new JSONArray(result);

                for(int i=0;i<jArray.length();i++) {
                    JSONObject person = jArray.getJSONObject(i);

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
                        followerslist.add(fire3);
                        adapter_follow.notifyDataSetChanged();


                }

            } catch (JSONException e) {
                Toast.makeText(referenceActivity, e.toString(), Toast.LENGTH_LONG).show();
            }
        }

    }




    private class UsersFollowersDetails extends AsyncTask<String, String, String> {
        ProgressDialog pdLoading = new ProgressDialog(referenceActivity);
        HttpURLConnection conn;
        URL url = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            pdLoading.setMessage("\tLoading...");
            pdLoading.setCancelable(false);
            pdLoading.show();

        }

        @Override
        protected String doInBackground(String... params) {
            try {

                // Enter URL address where your json file resides
                // Even you can make call to php file which returns json data
                url = new URL(AppConfig.URL_FOLLOW);

            } catch (MalformedURLException e) {

                e.printStackTrace();
                return e.toString();
            }
            try {

                // Setup HttpURLConnection class to send and receive data from php and mysql
                conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(READ_TIMEOUT);
                conn.setConnectTimeout(CONNECTION_TIMEOUT);
                conn.setRequestMethod("GET");

                // setDoOutput to true as we recieve data from json file
                conn.setDoOutput(true);

            } catch (IOException e1) {

                e1.printStackTrace();
                return e1.toString();
            }

            try {

                int response_code = conn.getResponseCode();

                // Check if successful connection made
                if (response_code == HttpURLConnection.HTTP_OK) {

                    // Read data sent from server
                    InputStream input = conn.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(input));
                    StringBuilder result = new StringBuilder();
                    String line;

                    while ((line = reader.readLine()) != null) {
                        result.append(line);
                    }

                    return (result.toString());

                } else {

                    return ("unsuccessful");
                }

            } catch (IOException e) {
                e.printStackTrace();
                return e.toString();
            } finally {
                conn.disconnect();
            }


        }

        @Override
        protected void onPostExecute(String result) {

            //this method will be running on UI thread

            pdLoading.dismiss();
            try {

                JSONArray jArray = new JSONArray(result);

                for(int i=0;i<jArray.length();i++) {
                    JSONObject person = jArray.getJSONObject(i);

                    String user_id = person.getString("user_id");
                    String follow_id = person.getString("follow_id");
                }

            } catch (JSONException e) {
                Toast.makeText(referenceActivity, "Check internet connection", Toast.LENGTH_LONG).show();
            }
            new UsersDetails().execute();
        }

    }
}