package com.example.admin.litebulb.Adapters;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.admin.litebulb.CollectionsItemClick;
import com.example.admin.litebulb.Models.Collectionsfolder;
import com.example.admin.litebulb.R;

import java.util.List;

public class AdapterCollectionsFolder extends RecyclerView.Adapter<AdapterCollectionsFolder.MyViewHolder> {

    private Context mContext;
    private List<Collectionsfolder> collectionsList;
    public ImageView thumbnail;

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView tv_name, tv_user_name, tv_votes, tv_items, view_items;
        public ImageView thumbnail;
        public CardView cardView;
        int collectionsId;
        String userName, collectionName;

        public MyViewHolder(View view) {
            super(view);
            view.setOnClickListener(this);
            tv_name = (TextView) view.findViewById(R.id.name);
            tv_items = (TextView) view.findViewById(R.id.items);
            tv_user_name = (TextView) view.findViewById(R.id.user_name);
            tv_votes = (TextView) view.findViewById(R.id.votes);
            view_items=(TextView) view.findViewById(R.id.view_items);
            thumbnail = (ImageView) view.findViewById(R.id.thumbnail);
            cardView = (CardView) view.findViewById(R.id.card_view);
           // thumbnail.setOnClickListener(this);
            view_items.setOnClickListener(this);
        }
        public void openItem(int id, String user_name, String collection_name){
            collectionsId = id;
            userName=user_name;
            collectionName=collection_name;
        }


        public void onClick(View view) {
            Log.e("COLLECTION FOLDER ITEMS", "this has been clicked + the ID is : "+collectionsId);
            //Toast.makeText(mContext, "Item ID : "+itemId, Toast.LENGTH_SHORT).show();
            CollectionsItemClick fragment1 = new CollectionsItemClick();
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
    }


    public AdapterCollectionsFolder(Context mContext, List<Collectionsfolder> collectionsList) {
        this.mContext = mContext;
        this.collectionsList = collectionsList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_collections_folder, parent, false);
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
        holder.tv_votes.setText(Collectionsfolder.getVotes()+" votes");
        holder.tv_items.setText(Collectionsfolder.getItems()+" items");

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
}