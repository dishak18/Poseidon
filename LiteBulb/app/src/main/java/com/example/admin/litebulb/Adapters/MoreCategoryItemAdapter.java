package com.example.admin.litebulb.Adapters;

        import android.support.v7.widget.RecyclerView;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;

        import com.example.admin.litebulb.R;
        import com.example.admin.litebulb.Viewholders.MoreCategoryItemViewholder;
        import com.example.admin.litebulb.Models.MoreMainCategory;

        import java.util.ArrayList;

public class MoreCategoryItemAdapter extends RecyclerView.Adapter<MoreCategoryItemViewholder> {

    private ArrayList<MoreMainCategory> moreMainCategories;

    public MoreCategoryItemAdapter(ArrayList<MoreMainCategory> moreMainCategories){
        this.moreMainCategories = moreMainCategories;
    }

    @Override
    public MoreCategoryItemViewholder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View newView = layoutInflater.inflate(R.layout.item_categories_more_fragment, parent, false);
        return new MoreCategoryItemViewholder(newView);
    }

    @Override
    public void onBindViewHolder(MoreCategoryItemViewholder holder, int position) {
        holder.populate(moreMainCategories.get(position));
        holder.openItem(moreMainCategories.get(position).getMainCategoryId());
    }

    @Override
    public int getItemCount() {
        return moreMainCategories.size();
    }
}