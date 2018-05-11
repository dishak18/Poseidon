package com.example.admin.litebulb;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.admin.litebulb.Adapters.AdapterUserCollectionsItems;
import com.example.admin.litebulb.Models.CollectionsFolderItems;
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

import static com.example.admin.litebulb.ItemClickFragment.CONNECTION_TIMEOUT;
import static com.example.admin.litebulb.ItemClickFragment.READ_TIMEOUT;


public class UserCollectionsFolderItems extends Fragment {
    private RecyclerView rvCollectionsFolderItems;
    private AdapterUserCollectionsItems adapter_collections_folder_items;
    private List<CollectionsFolderItems> collections_folder_items_list;
    private ProgressDialog pDialog;
    String collection_name;

    TextView collection_name_tv;
    String[] item_id_array;

    String item_name, sales_items, categories, user_name, sales;
    int price, item_id;
    int count=0;
    int collectionId;

    Activity referenceActivity;
    DatabaseReference myRef;

    TextView oops;

    FirebaseDatabase database;
    View parentHolder;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        referenceActivity = getActivity();
        try
        {
            collectionId = getArguments().getInt("id");
            user_name=getArguments().getString("user_name");
            collection_name=getArguments().getString("collection_name");
            Log.e("UsersCollectionItems", "this is the collection id clicked "+collectionId);
        }catch (NullPointerException e)
        {

        }


        database = FirebaseDatabase.getInstance();
        parentHolder = inflater.inflate(R.layout.fragment_user_collections_folder_items, container,
                false);
        ImageButton edit=(ImageButton) parentHolder.findViewById(R.id.edit_collection);

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle args = new Bundle();
                EditCollectionFragment fragment1 = new EditCollectionFragment();
                args.putInt("id", collectionId);
                fragment1.setArguments(args);
                FragmentManager fragmentManager =((AppCompatActivity)view.getContext()).getSupportFragmentManager();
                FragmentTransaction fragmentTransaction =fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.contentContainer, fragment1);
                fragmentTransaction.commit();
            }
        });
        myRef = database.getReference().child("items");
        collection_name_tv=(TextView) parentHolder.findViewById(R.id.collection_name);
        oops=(TextView) parentHolder.findViewById(R.id.oops);
        collection_name_tv.setText(collection_name);

        rvCollectionsFolderItems = (RecyclerView) parentHolder.findViewById(R.id.rv_collections_folder_items);
        collections_folder_items_list = new ArrayList<>();
        adapter_collections_folder_items = new AdapterUserCollectionsItems(getActivity(), collections_folder_items_list);
        rvCollectionsFolderItems.setLayoutManager(new LinearLayoutManager(getContext()));
        rvCollectionsFolderItems.setItemAnimator(new DefaultItemAnimator());
        rvCollectionsFolderItems.setAdapter(adapter_collections_folder_items);
        //prepareCards();
        //makeJsonArrayRequestForWeekly();
        new ItemsCollectionsDetails().execute();
        if(count==0)
        {
            oops.setText(getResources().getString(R.string.oops));
        }
        else
        {
            oops.setText("");
        }
        adapter_collections_folder_items.notifyDataSetChanged();
        return parentHolder;

    }
    @Override
    public void onDetach(){
        super.onDetach();
    }



    private class ItemsCollectionsDetails extends AsyncTask<String, String, String> {
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
                url = new URL(AppConfig.URL_ITEMS_COLLECTIONS);

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
                    JSONObject json_data = jArray.getJSONObject(i);

                    //DataFish fishData = new DataFish();

                    int collection_id= json_data.getInt("collection_id");
                    if(collection_id==collectionId)
                    {
                        item_id= json_data.getInt("item_id");
                        item_ids.add(item_id+"");
                        count++;

                    }
                }
                item_id_array = item_ids.toArray(new String[item_ids.size()]);
                //user_name_user_array = user_ids_of_user_table.toArray(new String[user_ids_of_user_table.size()]);

            } catch (JSONException e) {
                Toast.makeText(referenceActivity, e.toString(), Toast.LENGTH_LONG).show();
            }
            new ItemsDetails().execute();
        }
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
                    int id_of_items=json_data.getInt("id");
                    for(int j=0; j<item_id_array.length; j++)
                    {
                        if(id_of_items==Integer.parseInt(item_id_array[j])) {

                            String name = json_data.getString("name");
                            String sales = json_data.getString("sales");
                            int price = json_data.getInt("price");
                            String thumbnail = json_data.getString("thumbnail");
                            String image_url = AppConfig.URL_PHOTOS + thumbnail;
                            CollectionsFolderItems fire= new CollectionsFolderItems();
                            fire.setItemId(id_of_items);
                            fire.setItemName(name);
                            fire.setPrice(price);
                            fire.setThumbnail(image_url);
                            fire.setSales_items(sales);
                            fire.setUser_name(user_name);
                            collections_folder_items_list.add(fire);
                            adapter_collections_folder_items.notifyDataSetChanged();
                        }

                    }




                }

                item_id_array = item_ids.toArray(new String[item_ids.size()]);
                //user_name_user_array = user_ids_of_user_table.toArray(new String[user_ids_of_user_table.size()]);

            } catch (JSONException e) {
                Toast.makeText(referenceActivity, e.toString(), Toast.LENGTH_LONG).show();
            }
            //new CollectionsDetails().execute();
        }
    }
}