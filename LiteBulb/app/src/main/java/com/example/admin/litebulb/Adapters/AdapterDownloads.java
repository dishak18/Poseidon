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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.admin.litebulb.ItemClickFragment;
import com.example.admin.litebulb.Models.Downloads;
import com.example.admin.litebulb.R;

import java.util.List;

public class AdapterDownloads extends RecyclerView.Adapter<AdapterDownloads.MyViewHolder>  {

    private Context mContext;
    private List<Downloads> downloadsList;
    private AdapterListener listener;
    public ImageView thumbnail;

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView item_name, votes, license_name;
        public ImageView thumbnail;
        public Button download_button;
        public CardView cardView;
        public int itemId;

        public MyViewHolder(View view) {
            super(view);
            //view.setOnClickListener(this);
            item_name = (TextView) view.findViewById(R.id.item_name);
            votes = (TextView) view.findViewById(R.id.votes);
            license_name = (TextView) view.findViewById(R.id.license_name);
            thumbnail = (ImageView) view.findViewById(R.id.thumbnail);
            download_button = (Button) view.findViewById(R.id.download_button);
            cardView = (CardView) view.findViewById(R.id.card_view);
            //thumbnail.setOnClickListener(this);
            download_button.setOnClickListener(this);
        }
        public void openItem(int id){
            itemId = id;
        }


        public void onClick(View view) {
            Log.e("ADAPTER DOWNLOADS", "this has been clicked + the ID is : "+itemId);
            Toast.makeText(mContext, "Item Id : "+itemId, Toast.LENGTH_SHORT).show();
            ItemClickFragment fragment1 = new ItemClickFragment();
            Bundle args = new Bundle();
            args.putInt("id", itemId);
            fragment1.setArguments(args);
            FragmentManager fragmentManager =((AppCompatActivity)view.getContext()).getSupportFragmentManager();
            FragmentTransaction fragmentTransaction =fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.contentContainer, fragment1);
            fragmentTransaction.commit();
           /* final DownloadActivity.DownloadTask downloadTask = new DownloadActivity.DownloadTask(DownloadActivity.this);
            downloadTask.execute("http://studio.litebulb.in/uploads//items/2012/11/150/c323ac6c04c28cb19927d9d300b2f7ec.zip");
       */ }
    }


    public AdapterDownloads(Context mContext, List<Downloads> downloadsList) {
        this.mContext = mContext;
        this.downloadsList = downloadsList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_download, parent, false);
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        Downloads Downloads = downloadsList.get(position);
        holder.item_name.setText(Downloads.getItemName());
        holder.votes.setText(Downloads.getVotes());
        holder.license_name.setText(Downloads.getDownload_link());
        /*loading Downloads cover using Glide library*/
        Glide.with(mContext)
                .load(Downloads.getThumbnail())
                .placeholder(R.drawable.studio)
                .error(R.drawable.studio)
                .into(holder.thumbnail);

        //holder.openItem(Downloads.getID());

        /*holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onCardSelected(position, holder.thumbnail);
            }
        });*/


    }

    @Override
    public int getItemCount() {
        return downloadsList.size();
    }

    public interface AdapterListener {

        /*void onCardSelected(int position, ImageView thumbnail);*/
    }
}





