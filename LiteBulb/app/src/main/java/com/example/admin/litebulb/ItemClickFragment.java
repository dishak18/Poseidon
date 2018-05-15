//PAYMENT FRAGMENT



//ItemClickFragment

package com.example.admin.litebulb;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
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
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.admin.litebulb.Adapters.AdapterItems;
import com.example.admin.litebulb.Models.album;
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
import java.util.Arrays;
import java.util.List;


public class ItemClickFragment extends Fragment {

    Activity referenceActivity;
    View parentHolder;
    String []attribute_ids_array;
    String []category_ids_item_attr_array;
    String []tag_ids_array;
    String []names_of_attributes_categories;
    String []name_attributes_array;
    String name, thumbnail, image_url, key, username_from_users_table;
    int id_of_items_table, price, id_of_badges;
    int itemId;
    // SharedPreferences loginPreferences;
    ImageView top_image, image_author;
    public static final int CONNECTION_TIMEOUT = 10000;
    int item_id_of_items_attrbutes_table;

    private List<album> moreItems;
    private AdapterItems adapter_more_items_by_user;
    String category_id, attribute_id;
    String id_of_attributes_categories_table, name_of_attributes_categories_table;
    String category_id_of_attributes_table, name_of_attributes_table, id_of_attributes_table;
    public static final int READ_TIMEOUT = 15000;
    TextView name_of_item, description, item_price, text_under_select_license, price_of_prepaid, all_about_user, name_of_user, tv_votes, tv_sales, tv_comments, tv_rating;
    Spinner select_license;
    TextView added_on, file_statistics, tags;
    Button view_portfolio, follow;
    RecyclerView rv_moreItems;
    String selectedItem, value, index, prepaid, badges_from_users, votes, rating, comments, sales;
    String user_id, user_id_from_item_table, name_of_badges, id_of_tags_table, name_of_tags, item_id_of_itemstotags_table, tag_id_of_itemstotags_table;
    String exclusive_author;

    private SharedPreferences preferences;
    private Button buyNow;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        referenceActivity = getActivity();
        itemId = getArguments().getInt("id");
        try{
            parentHolder = inflater.inflate(R.layout.fragment_item_click, container,
                    false);
        }catch (Exception e) {
            Log.e("ITEMCLICKFRAGMENT", e + " This is the error");
            throw e;
        }

        moreItems = new ArrayList<>();
        adapter_more_items_by_user = new AdapterItems(referenceActivity, moreItems );
        view_portfolio=(Button)parentHolder.findViewById(R.id.view_portfolio);
        follow=(Button)parentHolder.findViewById(R.id.follow);
        RecyclerView.LayoutManager mLayoutManager= new LinearLayoutManager(getActivity(), GridLayoutManager.HORIZONTAL, false);
        rv_moreItems=(RecyclerView) parentHolder.findViewById(R.id.recycler_view_more_items);

        rv_moreItems.setLayoutManager(mLayoutManager);
        rv_moreItems.setItemAnimator(new DefaultItemAnimator());
        rv_moreItems.setAdapter(adapter_more_items_by_user);

        top_image=(ImageView) parentHolder.findViewById(R.id.top_image);
        name_of_item=(TextView) parentHolder.findViewById(R.id.name_of_item);
        description=(TextView) parentHolder.findViewById(R.id.description);
        image_author=(ImageView) parentHolder.findViewById(R.id.image_author);
        item_price=(TextView) parentHolder.findViewById(R.id.item_price);
        select_license=(Spinner) parentHolder.findViewById(R.id.select_license);
        text_under_select_license=(TextView) parentHolder.findViewById(R.id.text_under_select_license);
        name_of_user=(TextView) parentHolder.findViewById(R.id.name);
        all_about_user=(TextView) parentHolder.findViewById(R.id.all_about_user);
        price_of_prepaid=(TextView) parentHolder.findViewById(R.id.price_of_prepaid);
        tv_comments=(TextView) parentHolder.findViewById(R.id.no_of_comments);
        tv_rating=(TextView) parentHolder.findViewById(R.id.ratings);
        tv_sales=(TextView) parentHolder.findViewById(R.id.sales);
        tv_votes=(TextView) parentHolder.findViewById(R.id.votes);
        added_on=(TextView) parentHolder.findViewById(R.id.added_on);
        file_statistics=(TextView) parentHolder.findViewById(R.id.file_statistics);
        tags=(TextView) parentHolder.findViewById(R.id.tags);

        preferences = getContext().getSharedPreferences("preferences", Context.MODE_PRIVATE);
        buyNow = (Button) parentHolder.findViewById(R.id.buy_now_item_click);

        all_about_user.setText("");

        select_license.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedItem = parent.getItemAtPosition(position).toString();
                tags.setText("");
                file_statistics.setText("");
                added_on.setText("");
                all_about_user.setText("");
                new SystemDetails().execute();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                selectedItem=parent.getItemAtPosition(0).toString();
                tags.setText("");
                file_statistics.setText("");
                added_on.setText("");
                all_about_user.setText("");
                new SystemDetails().execute();

            }
        });

        //new SystemDetails().execute();

        follow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FollowerFragment fragment1 = new FollowerFragment();
                Bundle args = new Bundle();
                args.putString("username", username_from_users_table);
                fragment1.setArguments(args);
                FragmentManager fragmentManager =getFragmentManager();
                FragmentTransaction fragmentTransaction =fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.contentContainer, fragment1);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });

        view_portfolio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UserPortfolio fragment1 = new UserPortfolio();
                Bundle args = new Bundle();
                args.putString("username", username_from_users_table);
                fragment1.setArguments(args);
                FragmentManager fragmentManager =getFragmentManager();
                FragmentTransaction fragmentTransaction =fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.contentContainer, fragment1);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();

            }
        });
        return parentHolder;
    }
    /*    @Override
        public void onStart() {
            super.onStart();
            select_license.setSelection(0);
            //getSelectedItemDetails();
            new SystemDetails().execute();
        }*/
    private class SystemDetails extends AsyncTask<String, String, String> {
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
                url = new URL(AppConfig.URL_SYSTEM);

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

                // Extract data from json and store into ArrayList as class objects
                for(int i=0;i<jArray.length();i++){
                    JSONObject json_data = jArray.getJSONObject(i);
                    //DataFish fishData = new DataFish();
                    key= json_data.getString("key");
                    value= json_data.getString("value");
                    if(key.equals("extended_price"))
                    {

                        index=value;

                    }
                    else if(key.equals("prepaid_price_discount")) {

                        prepaid = value;

                    }
                }

            } catch (JSONException e) {
                Toast.makeText(referenceActivity, e.toString(), Toast.LENGTH_LONG).show();
            }
            new ItemDetails().execute();

        }

    }
    private class ItemDetails extends AsyncTask<String, String, String> {
        ProgressDialog pdLoading = new ProgressDialog(referenceActivity);
        HttpURLConnection conn;
        URL url = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            //this method will be running on UI thread
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

                // Extract data from json and store into ArrayList as class objects
                for(int i=0;i<jArray.length();i++){
                    JSONObject json_data = jArray.getJSONObject(i);

                    id_of_items_table = json_data.getInt("id");

                    Log.e("lala", "this be from getitemdetails "+user_id_from_item_table);
                    if(id_of_items_table==itemId) {
                        name = json_data.getString("name");
                        price = json_data.getInt("price");
                        thumbnail = json_data.getString("thumbnail");
                        votes=json_data.getString("votes");
                        String datetime=json_data.getString("datetime");
                        rating=json_data.getString("rating");
                        sales=json_data.getString("sales");
                        comments=json_data.getString("comments");
                        user_id_from_item_table=json_data.getString("user_id");

                        SharedPreferences.Editor editor = preferences.edit();
                        editor.putString("itemPrice",price+"");
                        editor.apply();
                        buyNow.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                PaymentFragment fragment1 = new PaymentFragment();
                                FragmentManager fragmentManager = getFragmentManager();
                                FragmentTransaction fragmentTransaction =fragmentManager.beginTransaction();
                                fragmentTransaction.replace(R.id.contentContainer, fragment1);
                                fragmentTransaction.addToBackStack(null);
                                fragmentTransaction.commit();
                            }
                        });

                        image_url = AppConfig.URL_PHOTOS + thumbnail;
                        Glide.with(getContext())
                                .load(image_url)
                                .placeholder(R.drawable.loader)
                                .error(R.drawable.studio)
                                .into(top_image);
                        name_of_item.setText(name);
                        tv_comments.setText(getResources().getString(R.string.comments)+comments);
                        tv_rating.setText(getResources().getString(R.string.ratings)+rating);
                        tv_sales.setText(getResources().getString(R.string.sales)+sales);
                        tv_votes.setText(getResources().getString(R.string.votes)+votes);
                        added_on.setText(getResources().getString(R.string.added_on)+"\t\t\t\t\t"+datetime+"");

                        String new_prepaid = prepaid.substring(0, prepaid.length() - 1);

                        if (selectedItem.equals("Personal Use License")) {
                            item_price.setText("$"+price + "");
                            price_of_prepaid.setText("Pay Just $"+(price - (price * Integer.parseInt(new_prepaid) / 100)) + "");
                            text_under_select_license.setText(getResources().getString(R.string.personal_use_license) + "");

                        } else {
                            Log.e("lalalalala", "This is the index for extended " + index);
                            int net_price = price * Integer.parseInt(index);
                            item_price.setText("");
                            item_price.setText("$"+net_price + "");
                            price_of_prepaid.setText("Pay Just $"+(net_price - (net_price * Integer.parseInt(new_prepaid) / 100)) + "");
                            text_under_select_license.setText(getResources().getString(R.string.extended_use_license) + "");
                            //text_under_select_license.setText("lalalalala");
                        }
                    }
                }

            } catch (JSONException e) {
                Toast.makeText(referenceActivity, e.toString(), Toast.LENGTH_LONG).show();
            }
            new UsersDetails().execute();

            new ItemsTagsDetails().execute();

            new ItemAttributesDetails().execute();
        }

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
                    JSONObject json_data = jArray.getJSONObject(i);

                    user_id = json_data.getString("user_id");
                    Log.e("lalal", "this " + user_id + " and " + user_id_from_item_table);
                    if (user_id.equals(user_id_from_item_table)) {

                        exclusive_author = json_data.getString("exclusive_author");
                        all_about_user.setText("");
                        thumbnail = json_data.getString("avatar");
                        image_url = AppConfig.URL_PHOTOS + thumbnail;
                        username_from_users_table=json_data.getString("username");
                        badges_from_users = json_data.getString("badges");
                        Log.e("lalala", "this user json " + badges_from_users+" haha");

                        Glide.with(getContext())
                                .load(image_url)
                                .placeholder(R.drawable.loader)
                                .error(R.drawable.studio)
                                .into(image_author);
                        name_of_user.setText(json_data.getString("username"));

                        if (exclusive_author.equals("true")) {
                            all_about_user.append("Exclusive Author\n");
                        }
                        break;

                    }
                }

            } catch (JSONException e) {
                Toast.makeText(referenceActivity, e.toString(), Toast.LENGTH_LONG).show();
            }
            new BadgesDetails().execute();

            new ItemDetailsForMoreItemsByUser().execute();
        }

    }


    private class BadgesDetails extends AsyncTask<String, String, String> {

        HttpURLConnection conn;
        URL url = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected String doInBackground(String... params) {
            try {

                url = new URL(AppConfig.URL_BADGES);

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
                conn.setDoOutput(true);

            } catch (IOException e1) {

                e1.printStackTrace();
                return e1.toString();

            }

            try {

                int response_code = conn.getResponseCode();

                if (response_code == HttpURLConnection.HTTP_OK) {

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

            // pdLoading.dismiss();
            try {

                JSONArray jArray = new JSONArray(result);

                for(int i=0;i<jArray.length();i++) {
                    JSONObject json_data = jArray.getJSONObject(i);

                    id_of_badges=json_data.getInt("id");
                    name_of_badges=json_data.getString("name");
                    if(badges_from_users.equals(" "))
                    {
                        break;
                    }
                    else
                    {
                        Log.e("lalala",  "This is the badges of the users table "+badges_from_users);
                        String [] badge = badges_from_users.split(",");
                        int[] badge_no = new int[badge.length];
                        for(int k = 0; k < badge.length; k++) {
                            badge_no[k] = Integer.parseInt(badge[k]);


                        }
                        for(int k = 0; k < badge.length; k++) {
                            if(id_of_badges==badge_no[k])
                            {
                                Log.e("lalala",  "This is the badges of the users table for the "+k+"th "+badge_no[k]);
                                all_about_user.append(name_of_badges+"\n");
                            }
                        }
                    }
                }

            } catch (JSONException e) {
                Toast.makeText(referenceActivity, e.toString(), Toast.LENGTH_LONG).show();
            }

        }

    }

    private class TagsDetails extends AsyncTask<String, String, String> {
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
                url = new URL(AppConfig.URL_TAGS);

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
                tags.setText("Tags:\t\t\t");

                // Extract data from json and store into ArrayList as class objects
                for(int i=0;i<jArray.length();i++){
                    JSONObject json_data = jArray.getJSONObject(i);
                    //DataFish fishData = new DataFish();
                    id_of_tags_table= json_data.getString("id");
                    name_of_tags= json_data.getString("name");
                    for(int j=0; j<tag_ids_array.length; j++)
                    {
                        if(id_of_tags_table.equals(tag_ids_array[j]))
                        {
                            tags.append(name_of_tags+"  ");
                        }
                    }
                }

            } catch (JSONException e) {
                Toast.makeText(referenceActivity, e.toString(), Toast.LENGTH_LONG).show();
            }

        }

    }


    private class ItemsTagsDetails extends AsyncTask<String, String, String> {
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
                url = new URL(AppConfig.URL_ITEMS_TAGS);

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

            pdLoading.dismiss();
            try {

                JSONArray jArray = new JSONArray(result);
                ArrayList<String> tag_ids = new ArrayList<String>();

                // Extract data from json and store into ArrayList as class objects
                for(int i=0;i<jArray.length();i++) {
                    JSONObject json_data = jArray.getJSONObject(i);
                    //DataFish fishData = new DataFish();
                    item_id_of_itemstotags_table = json_data.getString("item_id");


                    if (item_id_of_itemstotags_table.equals(itemId+"")) {
                        tag_id_of_itemstotags_table = json_data.getString("tag_id");
                        tag_ids.add(tag_id_of_itemstotags_table);
                        //Log.e("lala", "This is the tag id of item_tags table "+tag_id_of_itemstotags_table);
                    }
                    else {
                        //Log.e("lala", "This is when it goes in else");
                    }
                }

                tag_ids_array = tag_ids.toArray(new String[tag_ids.size()]);

            } catch (JSONException e) {
                Toast.makeText(referenceActivity, e.toString(), Toast.LENGTH_LONG).show();
            }
            new TagsDetails().execute();

        }

    }

    private class ItemAttributesDetails extends AsyncTask<String, String, String> {
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
                url = new URL(AppConfig.URL_ITEM_ATTRIBUTES);

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
                ArrayList<String> category_ids = new ArrayList<String>();
                ArrayList<String> attribute_ids = new ArrayList<String>();

                // Extract data from json and store into ArrayList as class objects
                for(int i=0;i<jArray.length();i++){
                    JSONObject json_data = jArray.getJSONObject(i);

                    item_id_of_items_attrbutes_table=json_data.getInt("item_id");
                    if(item_id_of_items_attrbutes_table==itemId) {

                        category_id = json_data.getString("category_id");
                        attribute_id = json_data.getString("attribute_id");
                        category_ids.add(category_id);
                        attribute_ids.add(attribute_id);
                        Log.e("lala", "THis in item att "+category_id+", "+attribute_id);


                    }
                }
                Log.e("lala", "this category_id_array length in itemattri "+category_ids.size()+", "+attribute_ids.size());
                category_ids_item_attr_array = category_ids.toArray(new String[category_ids.size()]);
                attribute_ids_array = attribute_ids.toArray(new String[attribute_ids.size()]);


            } catch (JSONException e) {
                Toast.makeText(referenceActivity, e.toString()+"ItemAttributeDetails", Toast.LENGTH_LONG).show();
            }
            new AttributesCategoriesDetails().execute();

        }

    }




    private class AttributesCategoriesDetails extends AsyncTask<String, String, String> {
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
                url = new URL(AppConfig.URL_ATTRIBUTES_CATEGORIES);

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
                ArrayList<String> names = new ArrayList<String>();
                // ArrayList<String> attribute_ids = new ArrayList<String>();

                // Extract data from json and store into ArrayList as class objects
                for(int i=0;i<jArray.length();i++){
                    JSONObject json_data = jArray.getJSONObject(i);

                    id_of_attributes_categories_table=json_data.getString("id");
                    // Log.e("lala", "this is the id of att_cat "+id_of_attributes_categories_table+"   ");
                    for(int j=0; j<attribute_ids_array.length; j++) {
                        if (id_of_attributes_categories_table.equals(category_ids_item_attr_array[j])) {
                            name_of_attributes_categories_table=json_data.getString("name");
                            // category_id_of_attributes_table = json_data.getString("category_id");
                            Log.e("lala", "this is the name_of_att_cat_table "+name_of_attributes_categories_table);
                            names.add(name_of_attributes_categories_table);
                        }

                    }
                }

                names_of_attributes_categories = names.toArray(new String[names.size()]);
                /*for(int y=0; y<names_of_attributes_categories.length; y++)
                {
                    Log.e("lala", "this is in att details table name of att cat "+names_of_attributes_categories[y]);
                }*/

            } catch (JSONException e) {
                Toast.makeText(referenceActivity, e.toString()+"AttrCattrDetails", Toast.LENGTH_LONG).show();
            }
            new AttributesDetails().execute();

        }

    }
    private class AttributesDetails extends AsyncTask<String, String, String> {
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
                url = new URL(AppConfig.URL_ATTRIBUTES);

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
                ArrayList<String> name = new ArrayList<String>();
                // ArrayList<String> cat_id = new ArrayList<String>();

                for(int i=0;i<jArray.length();i++){
                    JSONObject json_data = jArray.getJSONObject(i);

                    id_of_attributes_table=json_data.getString("id");
                    //Log.e("lala", "this is the id of attdetails "+id_of_attributes_table);
                    for(int j=0; j<attribute_ids_array.length; j++) {
                        if (id_of_attributes_table.equals(attribute_ids_array[j])) {
                            name_of_attributes_table = json_data.getString("name");
                            category_id_of_attributes_table = json_data.getString("category_id");
                            name.add(name_of_attributes_table);

                            Log.e("lala", "this, "+name_of_attributes_table);
                        }
                    }
                }

                String []new_array=new String[names_of_attributes_categories.length];
                Arrays.fill(new_array, "");

                name_attributes_array = name.toArray(new String[name.size()]);
                Log.e("lala", " this name_att_array "+name_attributes_array.length);
                Log.e("lala", " this names_of_att_cat "+names_of_attributes_categories.length);
                Log.e("lala", " this new aray "+new_array.length);
                try {

                    for (int i = 0; i < names_of_attributes_categories.length; i++) {
                        //new_array[i]=name_attributes_array[i];
                        for (int j = 0; j < names_of_attributes_categories.length; j++) {
                            //Log.e("lala", "this name of att cat "+names_of_attributes_categories[i]+", "+names_of_attributes_categories[j]);
                            if (names_of_attributes_categories[i].equals(names_of_attributes_categories[j])) {
                                new_array[i] = new_array[i] + "," + name_attributes_array[j];
                                //Log.e("lala","this + "+ new_array[i]);
                            }
                        }
                    }


                    for(int i=0; i<new_array.length; i++)
                    {
                        int count=0;
                        for(int j=i+1; j<new_array.length; j++)
                        {

                            if(new_array[i].equals(new_array[j]))
                            {
                                count++;
                            }
                        }
                        if(count==0)
                        {
                            file_statistics.append(names_of_attributes_categories[i]+":\t\t\t\t\t"+new_array[i].substring(1, new_array[i].length())+"\n\n\n");
                        }
                    }
                }catch(StringIndexOutOfBoundsException e)
                {
                    //throw e;
                }catch(ArrayIndexOutOfBoundsException e)
                {

                }


            } catch (JSONException e) {
                Toast.makeText(referenceActivity, e.toString()+"AttributesDetails", Toast.LENGTH_LONG).show();
            }
            //new ItemDetails().execute();

        }

    }


    private class ItemDetailsForMoreItemsByUser extends AsyncTask<String, String, String> {
        ProgressDialog pdLoading = new ProgressDialog(referenceActivity);
        HttpURLConnection conn;
        URL url = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

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

                // Extract data from json and store into ArrayList as class objects
                for (int i = 0; i < jArray.length(); i++) {
                    JSONObject json_data = jArray.getJSONObject(i);

                    user_id_from_item_table = json_data.getString("user_id");

                    if (user_id.equals(user_id_from_item_table)) {
                        id_of_items_table=json_data.getInt("id");
                        name = json_data.getString("name");
                        Log.e("lala", "this be from getitemdetails, both the userids " + user_id_from_item_table+" , "+ user_id);
                        Log.e("lala", "this be from getitemdetails, name of file " + name);
                        price = json_data.getInt("price");
                        thumbnail = json_data.getString("thumbnail");
                        image_url = AppConfig.URL_PHOTOS + thumbnail;
                        album fire3 =new album();
                        Log.e("BLANKFRAGMENT3", "this in the loop for the "+i+"th time with name "+ name);
                        fire3.setName(name);
                        fire3.setprice(price);
                        fire3.setID(id_of_items_table);
                        fire3.setThumbnail(image_url);
                        moreItems.add(fire3);
                        adapter_more_items_by_user.notifyDataSetChanged();
                    }
                }

            } catch (JSONException e) {
                Toast.makeText(referenceActivity, e.toString(), Toast.LENGTH_LONG).show();
            }
        }
    }


}

