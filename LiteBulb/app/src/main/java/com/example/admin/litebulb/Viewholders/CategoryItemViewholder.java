package com.example.admin.litebulb.Viewholders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.admin.litebulb.R;
import com.example.admin.litebulb.Models.CategoryItem;

/**
 * Created on 13/2/18.
 */

public class CategoryItemViewholder extends RecyclerView.ViewHolder{

    private TextView nametv;
    private TextView pricetv;
    private TextView categoryInfotv;
    private TextView byInfotv;

    public CategoryItemViewholder (View itemView){
        super(itemView);
        nametv = (TextView) itemView.findViewById(R.id.item_title);
        pricetv = (TextView) itemView.findViewById(R.id.item_price);
        categoryInfotv = (TextView) itemView.findViewById(R.id.item_sub_of);
        byInfotv = (TextView) itemView.findViewById(R.id.by_information);
    }

    public void populate(CategoryItem categoryItem){
        nametv.setText(categoryItem.getName());
        pricetv.setText(categoryItem.getPrice());
        categoryInfotv.setText(categoryItem.getCategory());
    }
}