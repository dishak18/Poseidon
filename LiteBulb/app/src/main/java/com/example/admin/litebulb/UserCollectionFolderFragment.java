package com.example.admin.litebulb;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
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

import com.example.admin.litebulb.Adapters.AdapterUserCollectionsFolder;
import com.example.admin.litebulb.Models.Collectionsfolder;
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


public class UserCollectionFolderFragment extends Fragment {
    private RecyclerView rvCollectionsFolder;
    private AdapterUserCollectionsFolder adapter_collections_folder;
    private List<Collectionsfolder> collections_folder_list;
    private ProgressDialog pDialog;
    ImageButton new_collection;
    SharedPreferences loginPreferences;
    String name, items, votes, user_name, user_name_user, user_id;
    int user_id_user;

    String[] user_id_array, user_id_user_array;
    String[] user_name_user_array;

    String username;
    TextView oops;
    Activity referenceActivity;
    DatabaseReference myRef;
    int count=0;

    FirebaseDatabase database;
    View parentHolder;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        referenceActivity = getActivity();
        database = FirebaseDatabase.getInstance();
        parentHolder = inflater.inflate(R.layout.fragment_user_collection_folder, container,
                false);
        myRef = database.getReference().child("items");
        oops=(TextView) parentHolder.findViewById(R.id.oops);

        rvCollectionsFolder = (RecyclerView) parentHolder.findViewById(R.id.rv_collections_folder);
        collections_folder_list = new ArrayList<>();
        adapter_collections_folder = new AdapterUserCollectionsFolder(getActivity(), collections_folder_list);
        rvCollectionsFolder.setLayoutManager(new LinearLayoutManager(getContext()));
        rvCollectionsFolder.setItemAnimator(new DefaultItemAnimator());
        rvCollectionsFolder.setAdapter(adapter_collections_folder);
        new_collection=(ImageButton) parentHolder.findViewById(R.id.new_collection);
        //prepareCards();
        //makeJsonArrayRequestForWeekly();
        loginPreferences = getActivity().getSharedPreferences("loginPrefs", MODE_PRIVATE);
        username=loginPreferences.getString("username", "");
        new UserDetails().execute();
      /*  if(count==0)
        {
           oops.setText("Oops! No Collections made!!");
        }*/

        new_collection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager manager = getFragmentManager();
                manager.beginTransaction().replace(R.id.contentContainer, new NewCollectionFragment()).addToBackStack(null).commit();
            }
        });
        adapter_collections_folder.notifyDataSetChanged();
        return parentHolder;
    }


    private class CollectionsDetails extends AsyncTask<String, String, String> {
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
                url = new URL(AppConfig.URL_COLLECTIONS);

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
                Log.e("lala","this is where we are now- collectionsdetails" );
                // Extract data from json and store into ArrayList as class objects
                for(int i=0;i<jArray.length();i++){
                    JSONObject json_data = jArray.getJSONObject(i);
                    //DataFish fishData = new DataFish();
                    name= json_data.getString("name");
                    user_id= json_data.getString("user_id");
                    items= json_data.getString("items");
                    votes= json_data.getString("votes");
                    int id=json_data.getInt("id");
                    String thumbnail=json_data.getString("photo");
                    String image_url=AppConfig.URL_PHOTOS + thumbnail;
                    if(user_id.equals(user_id_user+""))
                    {
                        count++;
                        Collectionsfolder fire=new Collectionsfolder();
                        fire.setName(name);
                        fire.setItems(items);
                        fire.setVotes(votes);
                        fire.setId(id);
                        fire.setThumbnail(image_url);
                        fire.setUser_name(username);
                        collections_folder_list.add(fire);
                        adapter_collections_folder.notifyDataSetChanged();
                    }



                }


            } catch (JSONException e) {
                Log.e("lala", "this haha in catch");
                Toast.makeText(referenceActivity, "Please check your network connection", Toast.LENGTH_LONG).show();
            }
            //new ItemClickFragment.ItemDetails().execute();

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
                ArrayList<String> user_ids_of_user_table = new ArrayList<String>();
                ArrayList<String> user_name_of_user_table = new ArrayList<String>();


                // Extract data from json and store into ArrayList as class objects
                for(int i=0;i<jArray.length();i++){
                    JSONObject json_data = jArray.getJSONObject(i);
                    //DataFish fishData = new DataFish();
                    user_name_user= json_data.getString("username");
                    if(user_name_user.equals(username))
                    {
                        user_id_user= json_data.getInt("user_id");
                    }

                    user_ids_of_user_table.add(user_id_user+"");
                    user_name_of_user_table.add(user_name_user);

                }
                user_id_user_array = user_ids_of_user_table.toArray(new String[user_ids_of_user_table.size()]);
                user_name_user_array = user_name_of_user_table.toArray(new String[user_name_of_user_table.size()]);



            } catch (JSONException e) {
                Toast.makeText(referenceActivity, "Please check your network connection", Toast.LENGTH_LONG).show();
                Log.e("lala", " this haha is user catch");
            }
            new CollectionsDetails().execute();

        }

    }






}