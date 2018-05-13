
package com.example.admin.litebulb;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.AsyncTask;
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

import static android.content.Context.MODE_PRIVATE;
import static com.example.admin.litebulb.ItemClickFragment.READ_TIMEOUT;
import static com.example.admin.litebulb.ProfileEdit.CONNECTION_TIMEOUT;


public class FollowingFragment extends Fragment {
    private RecyclerView rvFollowing;
    private AdpaterFollow adapter_follow;
    private List<Users_get> followinglist;
    String []follow_ids_array;
    private ProgressDialog pDialog;
    String user_name;
    int user_id;
    SharedPreferences loginPreferences;

    Activity referenceActivity;
    View parentHolder;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        referenceActivity = getActivity();
        parentHolder = inflater.inflate(R.layout.fragment_following, container,
                false);
        rvFollowing = (RecyclerView) parentHolder.findViewById(R.id.recycler_view);
        followinglist= new ArrayList<>();
        adapter_follow = new AdpaterFollow(getActivity(), followinglist );
        rvFollowing.setLayoutManager(new LinearLayoutManager(getContext()));
        rvFollowing.setItemAnimator(new DefaultItemAnimator());
        rvFollowing.setAdapter(adapter_follow);
        loginPreferences = getActivity().getSharedPreferences("loginPrefs", MODE_PRIVATE);

        Log.e("Following Fragment", "this is username: "+loginPreferences.getString("username", ""));
        user_name=loginPreferences.getString("username", "");
        //makeJsonArrayRequest();
        new UserIDDetails().execute();
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

                    String user_id_user=person.getString("user_id");
                    for(int j=0; j<follow_ids_array.length; j++)
                    {
                        if(user_id_user.equals(follow_ids_array[j]))
                        {
                            String name = person.getString("username");
                            String featured_author = person.getString("featured_author");
                            int sales = person.getInt("sales");
                            int items = person.getInt("items");
                            String thumbnail=person.getString("avatar");
                            String image_url = AppConfig.URL_PHOTOS +thumbnail;
                            Users_get fire3= new Users_get();
                            //Log.e("BLANKFRAGMENT3", "in the loop for the "+i+"th time with name "+ name);
                            fire3.setUsername(name);
                            fire3.setItems(items);
                            fire3.setSales(sales);
                            fire3.setThumbnail(image_url);
                            followinglist.add(fire3);
                            adapter_follow.notifyDataSetChanged();
                        }
                    }



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
                ArrayList<String> follow_ids = new ArrayList<String>();

                for(int i=0;i<jArray.length();i++) {
                    JSONObject person = jArray.getJSONObject(i);

                    int user_id_users_followers = person.getInt("user_id");
                    String follow_id = person.getString("follow_id");
                    if(user_id==user_id_users_followers)
                    {
                        follow_ids.add(follow_id);
                    }
                }
                follow_ids_array = follow_ids.toArray(new String[follow_ids.size()]);

            } catch (JSONException e) {
                Toast.makeText(referenceActivity, "Check internet connection", Toast.LENGTH_LONG).show();
            }
            new UsersDetails().execute();
        }

    }


    private class UserIDDetails extends AsyncTask<String, String, String> {
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
                conn.setConnectTimeout(ItemClickFragment.CONNECTION_TIMEOUT);
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

                    // Pass data to onPostExecute method
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
                ArrayList<String> item_ids = new ArrayList<String>();


                // Extract data from json and store into ArrayList as class objects
                for(int i=0;i<jArray.length();i++){
                    Log.e("ColletionsItemClick", "this is in itemsdetails");
                    JSONObject json_data = jArray.getJSONObject(i);

                    //DataFish fishData = new DataFish();
                    String user_name_ran=json_data.getString("username");
                    if(user_name_ran.equals(user_name)) {
                        user_id=json_data.getInt("user_id");
                    }

                }

                //user_name_user_array = user_ids_of_user_table.toArray(new String[user_ids_of_user_table.size()]);

            } catch (JSONException e) {
                Toast.makeText(referenceActivity, e.toString(), Toast.LENGTH_LONG).show();
            }
            new UsersFollowersDetails().execute();
        }
    }

}