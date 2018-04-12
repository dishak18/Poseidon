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
import com.example.admin.litebulb.Models.ModelUserPortfolio;
import com.example.admin.litebulb.R;

import java.util.List;

public class AdapterUserPortfolio extends RecyclerView.Adapter<AdapterUserPortfolio.MyViewHolder> {

    private Context mContext;
    private List<ModelUserPortfolio> collectionsItemsList;
    public ImageView thumbnail;

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView tv_item_name, tv_categories, tv_user_name, tv_sales, tv_price;
        public ImageView thumbnail;
        public CardView cardView;
        int itemId;

        public MyViewHolder(View view) {
            super(view);
            view.setOnClickListener(this);
            tv_item_name = (TextView) view.findViewById(R.id.item_name);
            tv_categories = (TextView) view.findViewById(R.id.categories);
            tv_user_name = (TextView) view.findViewById(R.id.username);
            tv_sales = (TextView) view.findViewById(R.id.sales);
            tv_price=(TextView) view.findViewById(R.id.price);
            thumbnail = (ImageView) view.findViewById(R.id.thumbnail);
            cardView = (CardView) view.findViewById(R.id.card_view);
            thumbnail.setOnClickListener(this);
        }
        public void openItem(int id){
            itemId = id;
        }


        public void onClick(View view) {
            Log.e("USER PORTFOLIO", "this has been clicked + the ID is : "+itemId);
            //Toast.makeText(mContext, "Item ID : "+itemId, Toast.LENGTH_SHORT).show();
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


    public AdapterUserPortfolio(Context mContext, List<ModelUserPortfolio> collectionsItemsList) {
        this.mContext = mContext;
        this.collectionsItemsList = collectionsItemsList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_user_portfolio, parent, false);
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        ModelUserPortfolio ModelUserPortfolio = collectionsItemsList.get(position);
        holder.tv_item_name.setText(ModelUserPortfolio.getItemName());
        holder.tv_user_name.setText(ModelUserPortfolio.getUser_name());
        holder.tv_categories.setText(ModelUserPortfolio.getCategories());
        holder.tv_sales.setText(ModelUserPortfolio.getSales());
        holder.tv_price.setText(ModelUserPortfolio.getPrice()+"");

        /*loading ModelUserPortfolio cover using Glide library*/
        try{
            Glide.with(mContext)
                    .load(ModelUserPortfolio.getThumbnail())
                    .placeholder(R.drawable.studio)
                    .error(R.drawable.studio)
                    .into(holder.thumbnail);
        }catch(NullPointerException e)
        {
            throw e;
        }

        holder.openItem(ModelUserPortfolio.getItemId());

    }

    @Override
    public int getItemCount() {
        return collectionsItemsList.size();
    }

    public interface AlbumsAdapterListener {

        /*void onCardSelected(int position, ImageView thumbnail);*/
    }
}