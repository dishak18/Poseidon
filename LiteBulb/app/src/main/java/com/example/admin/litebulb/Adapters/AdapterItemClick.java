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
import com.example.admin.litebulb.Models.ItemClick;

import java.util.List;

public class AdapterItemClick extends RecyclerView.Adapter<AdapterItemClick.MyViewHolder> {

    private Context mContext;
    private List<ItemClick> itemClickList;
    private AlbumsAdapterListener listener;
    public ImageView thumbnail;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView name_of_item, price_of_prepaid, item_price;
        public ImageView top_image;
        public CardView cardView;

        public MyViewHolder(View view) {
            super(view);
            name_of_item = (TextView) view.findViewById(R.id.name_of_item);
            price_of_prepaid = (TextView) view.findViewById(R.id.price_of_prepaid);

            item_price = (TextView) view.findViewById(R.id.item_price);
            top_image = (ImageView) view.findViewById(R.id.top_image);
            cardView = (CardView) view.findViewById(R.id.card_view);
        }

    }


    public AdapterItemClick(Context mContext, List<ItemClick> itemClickList) {
        this.mContext = mContext;
        this.itemClickList = itemClickList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_item_click, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        ItemClick itemClick = itemClickList.get(position);
        holder.name_of_item.setText(itemClick.getName());
        holder.price_of_prepaid.setText("$"+itemClick.getPrepaid_price());
         holder.item_price.setText("$"+itemClick.getprice());
        /*loading ItemClick cover using Glide library*/
        Glide.with(mContext)
                .load(itemClick.getThumbnail())
                .placeholder(R.drawable.loader)
                .error(R.drawable.studio)
                .into(holder.top_image);

/*        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onCardSelected(position, holder.thumbnail);
            }
        });*/


    }

    @Override
    public int getItemCount() {
        return itemClickList.size();
    }

    public interface AlbumsAdapterListener {

        /*void onCardSelected(int position, ImageView thumbnail);*/
    }
}





