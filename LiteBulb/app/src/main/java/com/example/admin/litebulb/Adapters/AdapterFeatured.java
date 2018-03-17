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
import com.example.admin.litebulb.ItemClickFragment;
import com.example.admin.litebulb.Models.album;
import com.example.admin.litebulb.R;

import java.util.List;

public class AdapterFeatured extends RecyclerView.Adapter<AdapterFeatured.MyViewHolder> {

    private Context mContext;
    private List<album> albumList;
    private AlbumsAdapterListener listener;
    public ImageView thumbnail;

    public static class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView title, count;
        public ImageView thumbnail;
        public CardView cardView;
        public int itemId;

        public MyViewHolder(View view) {
            super(view);
            view.setOnClickListener(this);
            title = (TextView) view.findViewById(R.id.title);
            count = (TextView) view.findViewById(R.id.count);
            thumbnail = (ImageView) view.findViewById(R.id.thumbnail);
            cardView = (CardView) view.findViewById(R.id.card_view);
            thumbnail.setOnClickListener(this);
        }

        public void openItem(int id){
            itemId = id;
        }


        public void onClick(View view) {
            Log.e("ADAPTER ITEMS", "this has been clicked + the ID is : "+itemId );
            //Toast.makeText(mContext, "Item Name : "+itemId, Toast.LENGTH_SHORT).show();
            ItemClickFragment fragment1 = new ItemClickFragment();
            Bundle args = new Bundle();
            args.putInt("id", itemId);
            fragment1.setArguments(args);
            FragmentManager fragmentManager =((AppCompatActivity)view.getContext()).getSupportFragmentManager();
            FragmentTransaction fragmentTransaction =fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.contentContainer, fragment1);
            fragmentTransaction.commit();
        }
   }


    public AdapterFeatured(Context mContext, List<album> albumList) {
        this.mContext = mContext;
        this.albumList = albumList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_featured, parent, false);
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        album album = albumList.get(position);
        holder.title.setText(album.getName());
        holder.count.setText("$" + album.getprice());
        /*loading album cover using Glide library*/
        Glide.with(mContext)
                .load(album.getThumbnail())
                .placeholder(R.drawable.loader)
                .error(R.drawable.studio)
                .into(holder.thumbnail);
        holder.openItem(album.getID());
/*        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onCardSelected(position, holder.thumbnail);
            }
        });*/


    }

    @Override
    public int getItemCount() {
        return albumList.size();
    }

    public interface AlbumsAdapterListener {

        /*void onCardSelected(int position, ImageView thumbnail);*/
    }

}





