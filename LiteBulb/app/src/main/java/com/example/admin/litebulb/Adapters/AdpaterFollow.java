package com.example.admin.litebulb.Adapters;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.admin.litebulb.Models.Users_get;
import com.example.admin.litebulb.R;
import com.example.admin.litebulb.UserPortfolio;

import java.util.List;

public class AdpaterFollow extends RecyclerView.Adapter<AdpaterFollow.MyViewHolder> {

    private Context mContext;
    private List<Users_get> albumList;
    private AlbumsAdapterListener listener;
    public ImageView thumbnail;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title, sales_items, view_portfolio, unfollow;
        public ImageView thumbnail;
        public CardView cardView;

        public MyViewHolder(View view) {
            super(view);
            //view.setOnClickListener(this);
            title = (TextView) view.findViewById(R.id.title);
            sales_items = (TextView) view.findViewById(R.id.sales_items);
            thumbnail = (ImageView) view.findViewById(R.id.thumbnail);
            cardView = (CardView) view.findViewById(R.id.card_view);

            view_portfolio = (TextView) view.findViewById(R.id.view_portfolio);
            unfollow = (TextView) view.findViewById(R.id.unfollow);


            view_portfolio.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    UserPortfolio fragment1 = new UserPortfolio();
                    Bundle args = new Bundle();

                    //PUT HERE

                    // args.putString("username", username_from_users_table);
                    fragment1.setArguments(args);
                    FragmentManager fragmentManager =((AppCompatActivity)view.getContext()).getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction =fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.contentContainer, fragment1);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();
                }
            });
            unfollow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                }
            });
            //thumbnail.setOnClickListener(this);
        }

    }


    public AdpaterFollow(Context mContext, List<Users_get> albumList) {
        this.mContext = mContext;
        this.albumList = albumList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_follow, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        Users_get Users_get = albumList.get(position);
        holder.title.setText(Users_get.getUsername());
        holder.sales_items.setText("Author has "+Users_get.getItems()+" items for sale and has racked up "+Users_get.getSales()+" sales!");
        /*loading Users_get cover using Glide library*/
        Glide.with(mContext)
                .load(Users_get.getThumbnail())
                .placeholder(R.drawable.studio)
                .error(R.drawable.studio)
                .into(holder.thumbnail);


    }

    @Override
    public int getItemCount() {
        return albumList.size();
    }

    public interface AlbumsAdapterListener {

       /* void onCardSelected(int position, ImageView thumbnail);  */
    }
}





