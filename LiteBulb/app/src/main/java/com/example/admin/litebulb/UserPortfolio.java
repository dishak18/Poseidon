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
import android.widget.TextView;
import android.widget.Toast;

import com.example.admin.litebulb.Adapters.AdapterUserPortfolio;
import com.example.admin.litebulb.Models.ModelUserPortfolio;
import com.example.admin.litebulb.SQL.AppConfig;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

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
import static com.example.admin.litebulb.ItemClickFragment.CONNECTION_TIMEOUT;
import static com.example.admin.litebulb.ItemClickFragment.READ_TIMEOUT;


public class UserPortfolio extends Fragment {
    private AdapterUserPortfolio adapter_user_portfolio;

    private List<ModelUserPortfolio> userPortfolioList;
    private ProgressDialog pDialog;

    TextView user_name_tv;
    int user_id;
    String user_name, user_name_from_view_portfolio;
    int count=0;

    Activity referenceActivity;
    DatabaseReference myRef;
    SharedPreferences loginPreferences;
    TextView oops;

    FirebaseDatabase database;
    View parentHolder;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        referenceActivity = getActivity();
        /*collectionId = getArguments().getInt("id");
        user_name=getArguments().getString("user_name");
        collection_name=getArguments().getString("collection_name");*/
        loginPreferences = getActivity().getSharedPreferences("loginPrefs", MODE_PRIVATE);

        Log.e("MainActivity", "this is username: "+loginPreferences.getString("username", ""));
        user_name=loginPreferences.getString("username", "");
        try
        {
            user_name_from_view_portfolio=getArguments().getString("username")+"";


        if(!(user_name_from_view_portfolio.equals("")||user_name_from_view_portfolio==null))
        {
            user_name=user_name_from_view_portfolio;
        }
        else
        {
            user_name=loginPreferences.getString("username", "");
        }
        }catch(NullPointerException e)
        {

        }

        userPortfolioList = new ArrayList<>();
        database = FirebaseDatabase.getInstance();
        parentHolder = inflater.inflate(R.layout.fragment_user_portfolio, container,
                false);
        myRef = database.getReference().child("items");
        user_name_tv=(TextView) parentHolder.findViewById(R.id.user_name);
        oops=(TextView) parentHolder.findViewById(R.id.oops);
        user_name_tv.setText(user_name);

        RecyclerView rvUserPortfolio = (RecyclerView) parentHolder.findViewById(R.id.rv_user_portfolio);
        adapter_user_portfolio = new AdapterUserPortfolio(getActivity(), userPortfolioList);
        rvUserPortfolio.setLayoutManager(new LinearLayoutManager(getContext()));
        rvUserPortfolio.setItemAnimator(new DefaultItemAnimator());
        rvUserPortfolio.setAdapter(adapter_user_portfolio);

        new UserDetails().execute();
        adapter_user_portfolio.notifyDataSetChanged();
        return parentHolder;
    }



    private class ItemsDetails extends AsyncTask<String, String, String> {
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
                url = new URL(AppConfig.URL_ITEM);

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
                    int user_id_items=json_data.getInt("user_id");
                        if(user_id==user_id_items) {
                            int id_of_items=json_data.getInt("id");
                            String name = json_data.getString("name");
                            String sales = json_data.getString("sales");
                            int price = json_data.getInt("price");
                            String thumbnail = json_data.getString("thumbnail");
                            String image_url = AppConfig.URL_PHOTOS + thumbnail;
                            ModelUserPortfolio fire= new ModelUserPortfolio();
                            fire.setItemId(id_of_items);
                            fire.setItemName(name);
                            fire.setPrice(price);
                            fire.setThumbnail(image_url);
                            fire.setSales(sales);
                            fire.setUser_name(user_name);
                            //fire.setUser_name(user_name);
                            userPortfolioList.add(fire);
                            adapter_user_portfolio.notifyDataSetChanged();
                        }

                    }

                //user_name_user_array = user_ids_of_user_table.toArray(new String[user_ids_of_user_table.size()]);

            } catch (JSONException e) {
                Toast.makeText(referenceActivity, e.toString(), Toast.LENGTH_LONG).show();
            }
            //new CollectionsDetails().execute();
        }
    }

    private class UserDetails extends AsyncTask<String, String, String> {
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
            new ItemsDetails().execute();
        }
    }
}