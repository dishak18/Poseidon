package com.example.admin.litebulb.Viewholders;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.admin.litebulb.Adapters.CategoryItemAdapter;
import com.example.admin.litebulb.Models.CategoryItem;
import com.example.admin.litebulb.R;

/**
 * Created on 13/2/18.
 */

public class CategoryItemViewholder extends RecyclerView.ViewHolder {

    private TextView nametv;
    private TextView pricetv;
    private TextView categoryInfotv;
    private TextView byInfotv;
    CategoryItemAdapter categoryItemAdapter;
    private ImageView thumbnail;

    public CategoryItemViewholder (View itemView){
        super(itemView);
        nametv = (TextView) itemView.findViewById(R.id.item_title);
        pricetv = (TextView) itemView.findViewById(R.id.item_price);
        categoryItemAdapter = new CategoryItemAdapter();
        categoryInfotv = (TextView) itemView.findViewById(R.id.item_sub_of);
        byInfotv = (TextView) itemView.findViewById(R.id.by_information);
        thumbnail=(ImageView) itemView.findViewById(R.id.thumbnail);
    }
    public void populate(CategoryItem categoryItem, Context context){
        nametv.setText(categoryItem.getName());
        pricetv.setText(categoryItem.getPrice());
        categoryInfotv.setText(categoryItem.getCategory());
        Glide.with(context)
                .load(categoryItem.getThumbnail())
                .placeholder(R.drawable.loader)
                .error(R.drawable.studio)
                .into(thumbnail);
    }
    public void onClick()
    {
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


            }
        });
    }

}
