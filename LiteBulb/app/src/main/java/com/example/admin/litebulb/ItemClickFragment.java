package com.example.admin.litebulb;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
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


public class ItemClickFragment extends Fragment {

    Activity referenceActivity;
    View parentHolder;
    String []tag_ids_array;
    String name, thumbnail, image_url, key;
    int id_of_items_table, price, id_of_badges;
    int itemId;
    ImageView top_image, image_author;
    public static final int CONNECTION_TIMEOUT = 10000;
    public static final int READ_TIMEOUT = 15000;
    TextView name_of_item, description, item_price, text_under_select_license, price_of_prepaid, all_about_user, name_of_user, tv_votes, tv_sales, tv_comments, tv_rating;
    Spinner select_license;
    TextView files_included, compatible_browsers, added_on, documentation, tags;

    String selectedItem, value, index, prepaid, badges_from_users, votes, rating, comments, sales;
    String user_id, user_id_from_item_table, name_of_badges, id_of_tags_table, name_of_tags, item_id_of_itemstotags_table, tag_id_of_itemstotags_table;
    String exclusive_author;


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
        files_included=(TextView) parentHolder.findViewById(R.id.files_included);
        documentation=(TextView) parentHolder.findViewById(R.id.documentation);
        added_on=(TextView) parentHolder.findViewById(R.id.added_on);
        compatible_browsers=(TextView) parentHolder.findViewById(R.id.compatible);
        tags=(TextView) parentHolder.findViewById(R.id.tags);

        select_license.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedItem = parent.getItemAtPosition(position).toString();
                new SystemDetails().execute();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                selectedItem=parent.getItemAtPosition(0).toString();
                new SystemDetails().execute();

            }
        });

        new SystemDetails().execute();
        new ItemsTagsDetails().execute();

        return parentHolder;
    }
    @Override
    public void onStart() {
        super.onStart();
        select_license.setSelection(0);
        //getSelectedItemDetails();
        new SystemDetails().execute();
    }
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
                // TODO Auto-generated catch block
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
                // TODO Auto-generated catch block
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
                // TODO Auto-generated catch block
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
                // TODO Auto-generated catch block
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
                    user_id_from_item_table=json_data.getString("user_id");
                    Log.e("lala", "this be from getitemdetails "+user_id_from_item_table);
                    if(id_of_items_table==itemId) {
                        name = json_data.getString("name");
                        price = json_data.getInt("price");
                        thumbnail = json_data.getString("thumbnail");
                        votes=json_data.getString("votes");

                        rating=json_data.getString("rating");

                        sales=json_data.getString("sales");

                        comments=json_data.getString("comments");


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

                        String new_prepaid = prepaid.substring(0, prepaid.length() - 1);

                        if (selectedItem.equals("Personal Use License")) {
                            item_price.setText(price + "");
                            price_of_prepaid.setText((price - (price * Integer.parseInt(new_prepaid) / 100)) + "");
                            text_under_select_license.setText(getResources().getString(R.string.personal_use_license) + "");

                        } else {
                            Log.e("lalalalala", "This is the index for extended " + index);
                            int net_price = price * Integer.parseInt(index);
                            item_price.setText("");
                            item_price.setText(net_price + "");
                            price_of_prepaid.setText((net_price - (net_price * Integer.parseInt(new_prepaid) / 100)) + "");
                            text_under_select_license.setText(getResources().getString(R.string.extended_use_license) + "");
                            //text_under_select_license.setText("lalalalala");
                        }
                    }
                }

            } catch (JSONException e) {
                Toast.makeText(referenceActivity, e.toString(), Toast.LENGTH_LONG).show();
            }
            new UsersDetails().execute();
        }

    }
    private class UsersDetails extends AsyncTask<String, String, String> {
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
                url = new URL(AppConfig.URL_USER_FEATURED);

            } catch (MalformedURLException e) {
                // TODO Auto-generated catch block
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
                // TODO Auto-generated catch block
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
                            badge_no[k] = Integer.parseInt(badge[i]);

                        }
                        for(int k = 0; k < badge.length; k++) {
                            if(id_of_badges==badge_no[k])
                            {
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
                // TODO Auto-generated catch block
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
                // TODO Auto-generated catch block
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
                // TODO Auto-generated catch block
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
                // TODO Auto-generated catch block
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
                ArrayList<String> tag_ids = new ArrayList<String>();

                // Extract data from json and store into ArrayList as class objects
                for(int i=0;i<jArray.length();i++) {
                    JSONObject json_data = jArray.getJSONObject(i);
                    //DataFish fishData = new DataFish();
                    item_id_of_itemstotags_table = json_data.getString("item_id");


                    if (item_id_of_itemstotags_table.equals(itemId+"")) {
                        tag_id_of_itemstotags_table = json_data.getString("tag_id");
                        tag_ids.add(tag_id_of_itemstotags_table);
                        Log.e("lala", "This is the tag id of item_tags table "+tag_id_of_itemstotags_table);
                    }
                    else {
                        Log.e("lala", "This is when it goes in else");
                    }
                }

                tag_ids_array = tag_ids.toArray(new String[tag_ids.size()]);

            } catch (JSONException e) {
                Toast.makeText(referenceActivity, e.toString(), Toast.LENGTH_LONG).show();
            }
            new TagsDetails().execute();

        }

    }



}

