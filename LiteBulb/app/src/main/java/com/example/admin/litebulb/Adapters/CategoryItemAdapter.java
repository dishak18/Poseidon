package com.example.admin.litebulb.Adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.admin.litebulb.R;
import com.example.admin.litebulb.Viewholders.CategoryItemViewholder;
import com.example.admin.litebulb.Models.CategoryItem;

import java.util.ArrayList;

/**
 * Created on 13/2/18.
 */

public class CategoryItemAdapter extends RecyclerView.Adapter<CategoryItemViewholder>{

    private ArrayList<CategoryItem> categoryItems = new ArrayList<>();

    public CategoryItemAdapter(ArrayList<CategoryItem> categoryItems){
        this.categoryItems = categoryItems;
    }

    @Override
    public CategoryItemViewholder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View newView = layoutInflater.inflate(R.layout.item_categories_fragment, parent, false);
        return new CategoryItemViewholder(newView);
    }

    @Override
    public void onBindViewHolder(CategoryItemViewholder holder, final int position) {
        holder.populate(categoryItems.get(position));
    }

    @Override
    public int getItemCount() {
        return categoryItems.size();
    }
}