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

import com.example.admin.litebulb.Adapters.AdapterDownloads;
import com.example.admin.litebulb.Models.Downloads;
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


public class DownloadFragment extends Fragment {
    Activity referenceActivity;
    View parentHolder;
    String username, name_of_user;
    int user_id;
    String item_id;
    String download_link;
    private List<Downloads> downloadedItems;
    private AdapterDownloads adapter_downloads;
    String []item_ids_array;
    ProgressDialog pdLoading;
    RecyclerView rv_downloads;
    SharedPreferences loginPreferences;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        referenceActivity = getActivity();
        parentHolder = inflater.inflate(R.layout.fragment_download, container,
                false);
        downloadedItems = new ArrayList<>();
        pdLoading = new ProgressDialog(referenceActivity);
        adapter_downloads = new AdapterDownloads(getActivity(), downloadedItems );
        RecyclerView.LayoutManager mLayoutManager= new LinearLayoutManager(getContext());
        rv_downloads=(RecyclerView) parentHolder.findViewById(R.id.recycler_view_download);

        rv_downloads.setLayoutManager(mLayoutManager);
        rv_downloads.setItemAnimator(new DefaultItemAnimator());
        rv_downloads.setAdapter(adapter_downloads);
        loginPreferences = referenceActivity.getSharedPreferences("loginPrefs", MODE_PRIVATE);
        username=loginPreferences.getString("username", "");
        Log.e("DownloadFragment", "this is the useranme "+username);
        new UsersDetails().execute();
        adapter_downloads.notifyDataSetChanged();
        return parentHolder;
    }

    private class UsersDetails extends AsyncTask<String, String, String> {

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

           // pdLoading.dismiss();
            try {

                JSONArray jArray = new JSONArray(result);

                for(int i=0;i<jArray.length();i++) {
                    JSONObject json_data = jArray.getJSONObject(i);
                    name_of_user= json_data.getString("username");

                    if (name_of_user.equals(username)) {

                        user_id=json_data.getInt("user_id");
                        Log.e("DownloadFragment", "this is the user id "+user_id);
                        break;
                    }
                }

            } catch (JSONException e) {
                //Toast.makeText(referenceActivity, e.toString(), Toast.LENGTH_LONG).show();
            }
            new OrderDetails().execute();
        }

    }

    private class OrderDetails extends AsyncTask<String, String, String> {

        HttpURLConnection conn;
        URL url = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected String doInBackground(String... params) {
            try {

                url = new URL(AppConfig.URL_ORDERS);

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
                ArrayList<String> item_ids = new ArrayList<String>();

                for(int i=0;i<jArray.length();i++) {
                    JSONObject json_data = jArray.getJSONObject(i);
                    int user_id_of_orders=json_data.getInt("user_id");
                    if(user_id_of_orders==user_id) {
                        //Log.e("lalala", "This is the badges of the users table " + badges_from_users);
                        item_id = json_data.getString("item_id");
                        String paid=json_data.getString("paid");
                        if(paid.equals("true"))
                        {
                            item_ids.add(item_id);
                            Log.e("LoginActivity", "rthis is the item id "+item_id);
                        }
                    }
                }
                item_ids_array = item_ids.toArray(new String[item_ids.size()]);

            } catch (JSONException e) {
                //  Toast.makeText(referenceActivity, e.toString(), Toast.LENGTH_LONG).show();
            }
            new ItemDetails().execute();

        }

    }

    private class ItemDetails extends AsyncTask<String, String, String> {

        HttpURLConnection conn;
        URL url = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected String doInBackground(String... params) {
            try {

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
            pdLoading.dismiss();
            try {

                JSONArray jArray = new JSONArray(result);
                //ArrayList<String> item_ids = new ArrayList<String>();

                for(int i=0;i<jArray.length();i++) {
                    JSONObject json_data = jArray.getJSONObject(i);
                    String item_id_of_items=json_data.getString("id");
                    for(int j=0; j<item_ids_array.length; j++) {

                        if (item_id_of_items.equals(item_ids_array[j])) {
                            Downloads fire =new Downloads();
                            String link=AppConfig.IP_ADDRESS+"uploads"+json_data.getString("main_file");
                            Log.e("DownloadFragment", "this in the loop for the "+i+"th time with name "+ link);
                            fire.setDownload_link(link);
                            fire.setVotes(json_data.getInt("votes"));
                            fire.setItemId(Integer.parseInt(item_id_of_items));
                            fire.setItemName(json_data.getString("name"));
                            // fire.setLicense_name(json_data.getString(""));
                            String image_url=AppConfig.URL_PHOTOS+json_data.getString("thumbnail");
                            fire.setThumbnail(image_url);
                            downloadedItems.add(fire);
                            adapter_downloads.notifyDataSetChanged();
                        }
                    }
                }
                //item_ids_array = item_ids.toArray(new String[item_ids.size()]);

            } catch (JSONException e) {
                //  Toast.makeText(referenceActivity, e.toString(), Toast.LENGTH_LONG).show();
            }

        }

    }


}