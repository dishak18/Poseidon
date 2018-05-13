package com.example.admin.litebulb.Adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.admin.litebulb.Models.Collectionsfolder;
import com.example.admin.litebulb.R;
import com.example.admin.litebulb.SQL.AppConfig;
import com.example.admin.litebulb.UserCollectionFolderFragment;
import com.example.admin.litebulb.UserCollectionsFolderItems;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class AdapterUserCollectionsFolder extends RecyclerView.Adapter<AdapterUserCollectionsFolder.MyViewHolder> {

    private Context mContext;
    private List<Collectionsfolder> collectionsList;
    public ImageView thumbnail;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView tv_name, tv_user_name, tv_votes, tv_items, tv_public, tv_private, tv_view_items, tv_delete;
        public ImageView thumbnail;
        public CardView cardView;
        int collectionsId;
        int count=0;
        String public_collection;
        String userName, collectionName;

        public MyViewHolder(View view) {
            super(view);
            //view.setOnClickListener(this);
            tv_name = (TextView) view.findViewById(R.id.name);
            tv_items = (TextView) view.findViewById(R.id.items);
            tv_user_name = (TextView) view.findViewById(R.id.user_name);
            tv_votes = (TextView) view.findViewById(R.id.votes);
            thumbnail = (ImageView) view.findViewById(R.id.thumbnail);
            cardView = (CardView) view.findViewById(R.id.card_view);
            tv_view_items=(TextView) view.findViewById(R.id.view_items);
            tv_delete=(TextView) view.findViewById(R.id.delete);
            tv_private=(TextView)view.findViewById(R.id.private_collection);
            tv_public=(TextView)view.findViewById(R.id.public_collection);
            //thumbnail.setOnClickListener(this);
            tv_private.setTextColor(Color.RED);
            tv_public.setTextColor(Color.RED);

            tv_view_items.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.e("COLLECTION FOLDER ITEMS", "this has been clicked + the ID is : "+collectionsId);
                    //Toast.makeText(mContext, "Item ID : "+itemId, Toast.LENGTH_SHORT).show();
                    UserCollectionsFolderItems fragment1 = new UserCollectionsFolderItems();
                    Bundle args = new Bundle();
                    args.putInt("id", collectionsId);
                    args.putString("user_name", userName);
                    args.putString("collection_name", collectionName);
                    fragment1.setArguments(args);
                    FragmentManager fragmentManager =((AppCompatActivity)view.getContext()).getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction =fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.contentContainer, fragment1);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();

                }
            });
            tv_delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {


                    AlertDialog.Builder alert = new AlertDialog.Builder(
                            mContext);
                    alert.setTitle("Delete?");
                    alert.setMessage("Are you sure you want to delete your collection?");
                    alert.setPositiveButton("YES", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //do your work here

                            dialog.dismiss();
                            Toast.makeText(mContext, "Deleting Collection...", Toast.LENGTH_SHORT).show();
                            DeleteData(collectionsId+"");
                            UserCollectionFolderFragment fragment1=new UserCollectionFolderFragment();
                            FragmentManager fragmentManager =((AppCompatActivity)mContext).getSupportFragmentManager();
                            FragmentTransaction fragmentTransaction =fragmentManager.beginTransaction();
                            fragmentTransaction.replace(R.id.contentContainer, fragment1);
                            fragmentTransaction.commit();

                        }
                    });
                    alert.setNegativeButton("NO", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            dialog.dismiss();
                        }
                    });

                    alert.show();

                }
            });

            tv_public.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    tv_public.setTextColor(Color.BLUE);
                    tv_private.setTextColor(Color.RED);
                    public_collection="true";
                    InsertData(collectionsId+"", public_collection);
                }
            });
            tv_private.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    tv_private.setTextColor(Color.BLUE);
                    tv_public.setTextColor(Color.RED);
                    public_collection="false";
                    InsertData(collectionsId+"", public_collection);
                }
            });
        }
        public void openItem(int id, String user_name, String collection_name){
            collectionsId = id;
            userName=user_name;
            collectionName=collection_name;
        }


        /*public void onClick(View view) {
            Log.e("COLLECTION FOLDER ITEMS", "this has been clicked + the ID is : "+collectionsId);
            //Toast.makeText(mContext, "Item ID : "+itemId, Toast.LENGTH_SHORT).show();
            UserCollectionsFolderItems fragment1 = new UserCollectionsFolderItems();
            Bundle args = new Bundle();
            args.putInt("id", collectionsId);
            args.putString("user_name", userName);
            args.putString("collection_name", collectionName);
            fragment1.setArguments(args);
            FragmentManager fragmentManager =((AppCompatActivity)view.getContext()).getSupportFragmentManager();
            FragmentTransaction fragmentTransaction =fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.contentContainer, fragment1);
            fragmentTransaction.commit();
        }*/
    }


    public AdapterUserCollectionsFolder(Context mContext, List<Collectionsfolder> collectionsList) {
        this.mContext = mContext;
        this.collectionsList = collectionsList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_user_collections_folder, parent, false);
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        Collectionsfolder Collectionsfolder = collectionsList.get(position);
        holder.tv_name.setText(Collectionsfolder.getName());
        holder.tv_user_name.setText(Collectionsfolder.getUser_name());
        holder.tv_votes.setText(Collectionsfolder.getVotes());
        holder.tv_items.setText(Collectionsfolder.getItems());

        /*loading Collectionsfolder cover using Glide library*/
        try{
            Glide.with(mContext)
                    .load(Collectionsfolder.getThumbnail())
                    .placeholder(R.drawable.studio)
                    .error(R.drawable.studio)
                    .into(holder.thumbnail);
        }catch(NullPointerException e)
        {
            throw e;
        }
        holder.openItem(Collectionsfolder.getId(), Collectionsfolder.getUser_name(), Collectionsfolder.getName());

    }

    @Override
    public int getItemCount() {
        return collectionsList.size();
    }

    public interface AlbumsAdapterListener {

        /*void onCardSelected(int position, ImageView thumbnail);*/
    }


    public void InsertData(final String ID, final String public_collection) {

        class SendPostReqAsyncTask extends AsyncTask<String, Void, String> {
            @Override
            protected String doInBackground(String... params) {

                String IDHolder = ID;
                String PublicHolder = public_collection;

                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();

                nameValuePairs.add(new BasicNameValuePair("id", IDHolder));
                nameValuePairs.add(new BasicNameValuePair("public", PublicHolder));
                try {
                    HttpClient httpClient = new DefaultHttpClient();

                    HttpPost httpPost = new HttpPost(AppConfig.URL_UPDATE_COLLECTIONS_PUBLIC);

                    httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

                    HttpResponse httpResponse = httpClient.execute(httpPost);

                    HttpEntity httpEntity = httpResponse.getEntity();


                } catch (ClientProtocolException e) {

                } catch (IOException e) {

                }
                return "Data Inserted Successfully";
            }

            @Override
            protected void onPostExecute(String result) {

                super.onPostExecute(result);

                //Toast.makeText(mContext, "Request Submitted Successfully", Toast.LENGTH_LONG).show();

            }
        }

        SendPostReqAsyncTask sendPostReqAsyncTask = new SendPostReqAsyncTask();

        sendPostReqAsyncTask.execute(ID, public_collection);
    }



    public void DeleteData(final String ID) {

        class SendPostReqAsyncTask extends AsyncTask<String, Void, String> {
            @Override
            protected String doInBackground(String... params) {

                String IDHolder = ID;

                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();

                nameValuePairs.add(new BasicNameValuePair("id", IDHolder));
                try {
                    HttpClient httpClient = new DefaultHttpClient();

                    HttpPost httpPost = new HttpPost(AppConfig.URL_DELETE_COLLECTIONS);

                    httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

                    HttpResponse httpResponse = httpClient.execute(httpPost);

                    HttpEntity httpEntity = httpResponse.getEntity();


                } catch (ClientProtocolException e) {

                } catch (IOException e) {

                }
                return "Data Inserted Successfully";
            }

            @Override
            protected void onPostExecute(String result) {

                super.onPostExecute(result);

                Toast.makeText(mContext, "Deleted Collection Successfully", Toast.LENGTH_LONG).show();

            }
        }

        SendPostReqAsyncTask sendPostReqAsyncTask = new SendPostReqAsyncTask();

        sendPostReqAsyncTask.execute(ID);
    }
}