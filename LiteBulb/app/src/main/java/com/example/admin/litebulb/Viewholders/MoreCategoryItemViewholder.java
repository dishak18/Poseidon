package com.example.admin.litebulb.Viewholders;
import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.admin.litebulb.MoreCategoryItems;
import com.example.admin.litebulb.R;
import com.example.admin.litebulb.Models.MoreMainCategory;

/**
 * Created by grandiose on 13/2/18.
 */

public class MoreCategoryItemViewholder extends RecyclerView.ViewHolder{
    private TextView categoryName;
    public MoreCategoryItemViewholder(View itemView){
        super(itemView);
        categoryName = (TextView) itemView.findViewById(R.id.category_name);
    }

    public void populate(MoreMainCategory moreMainCategory){
        categoryName.setText(moreMainCategory.getMainCategory());

    }

    public void openItem(final String moreMainCategoryId){
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                Log.e("CartViewholder", "" + moreMainCategoryId);
                intent.setClass(itemView.getContext(), MoreCategoryItems.class).putExtra("moreMainCategoryId", moreMainCategoryId);
                ((Activity) itemView.getContext()).startActivity(intent);
            }
        });
    }

}