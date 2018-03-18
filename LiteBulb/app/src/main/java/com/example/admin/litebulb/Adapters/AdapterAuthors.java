package com.example.admin.litebulb.Adapters;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.admin.litebulb.Models.Users_get;
import com.example.admin.litebulb.R;

import java.util.List;

public class AdapterAuthors extends RecyclerView.Adapter<AdapterAuthors.MyViewHolder> {

    private Context mContext;
    private List<Users_get> albumList;
    private AdapterAuthorsListener listener;
    public ImageView thumbnail;

    public static class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public TextView title, sales_items;
        public ImageView thumbnail;
        public CardView cardView;
        public int itemId;

        public MyViewHolder(View view) {
            super(view);
            view.setOnClickListener(this);
            title = (TextView) view.findViewById(R.id.title);
            sales_items = (TextView) view.findViewById(R.id.sales_items);
            thumbnail = (ImageView) view.findViewById(R.id.thumbnail);
            cardView = (CardView) view.findViewById(R.id.card_view);
            thumbnail.setOnClickListener(this);
        }
        public void openItem(int id){
            itemId = id;
        }
        public void onClick(View view) {
            Log.e("ADAPTER ITEMS", "this has been clicked + the ID is : "+itemId);

           // Toast.makeText(mContext, "Item Name : "+itemId, Toast.LENGTH_SHORT).show();
            /*ItemClickFragment fragment1 = new ItemClickFragment();
            Bundle args = new Bundle();
            args.putInt("id", itemId);
            fragment1.setArguments(args);
            //ItemClickFragment fragment1 = new ItemClickFragment();
            FragmentManager fragmentManager =((AppCompatActivity)view.getContext()).getSupportFragmentManager();
            FragmentTransaction fragmentTransaction =fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.contentContainer, fragment1);
            fragmentTransaction.commit();*/


        }
    }


    public AdapterAuthors(Context mContext, List<Users_get> albumList) {
        this.mContext = mContext;
        this.albumList = albumList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_featured_author, parent, false);

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

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

        //holder.openItem(Users_get.getUsername());
       /* holder.cardView.setOnClickListener(new View.OnClickListener() {
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

    public interface AdapterAuthorsListener {

        void onCardSelected(int position, ImageView thumbnail);
    }
}




