package com.example.admin.litebulb.Adapters;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.admin.litebulb.R;
import com.example.admin.litebulb.Models.album;

import java.util.List;

public class AlbumAdapterWeeklyFreebiesAll extends RecyclerView.Adapter<AlbumAdapterWeeklyFreebiesAll.MyViewHolder> {

    private Context mContext;
    private List<album> albumList;
    private AlbumsAdapterListener listener;
    public ImageView thumbnail;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title, count;
        public ImageView thumbnail;
        public CardView cardView;

        public MyViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.title);
            count = (TextView) view.findViewById(R.id.count);
            thumbnail = (ImageView) view.findViewById(R.id.thumbnail);
            cardView = (CardView) view.findViewById(R.id.card_view);
        }
    }


    public AlbumAdapterWeeklyFreebiesAll(Context mContext, List<album> albumList) {
        this.mContext = mContext;
        this.albumList = albumList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_weekly_freebies, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        album album = albumList.get(position);
        holder.title.setText(album.getName());
        holder.count.setText("$"+album.getprice());
        /*loading album cover using Glide library*/
        Glide.with(mContext)
                .load(album.getThumbnail())
                .placeholder(R.drawable.loader)
                .error(R.drawable.studio)
                .into(holder.thumbnail);

        /*holder.cardView.setOnClickListener(new View.OnClickListener() {
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

       /* void onCardSelected(int position, ImageView thumbnail);*/
    }
}





