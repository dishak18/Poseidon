package com.example.admin.litebulb;

import android.app.Activity;
import android.content.res.Resources;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;


public class BlankFragment3 extends Fragment {
    private RecyclerView recyclerView, recyclerView2, recyclerView3;
    private AlbumAdapter adapter;
    private List<album> albumList;
    Activity referenceActivity;
    View parentHolder;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        referenceActivity = getActivity();

        parentHolder = inflater.inflate(R.layout.fragment_blank_fragment3, container,
                false);
        Toolbar toolbar = (Toolbar) parentHolder.findViewById(R.id.toolbar);
        //       ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);

//        initCollapsingToolbar();

        recyclerView = (RecyclerView) parentHolder.findViewById(R.id.recycler_view);
        recyclerView2 = (RecyclerView) parentHolder.findViewById(R.id.recycler_view_weekly);
        recyclerView3 = (RecyclerView) parentHolder.findViewById(R.id.recycler_view_featured);

        albumList = new ArrayList<>();
        adapter = new AlbumAdapter(getActivity(), albumList );
        Log.e("Hahahahahahhaha", adapter+"");
        RecyclerView.LayoutManager mLayoutManager= new LinearLayoutManager(getActivity(), GridLayoutManager.HORIZONTAL, false);
        RecyclerView.LayoutManager mLayoutManager2= new LinearLayoutManager(getActivity(),  LinearLayoutManager.HORIZONTAL, false);
        RecyclerView.LayoutManager mLayoutManager3= new LinearLayoutManager(getActivity(),  LinearLayoutManager.HORIZONTAL, false);

        // RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getActivity(), 2);
        recyclerView.setLayoutManager(mLayoutManager);
       // recyclerView.setLayoutManager(layoutManager);
        recyclerView2.setLayoutManager(mLayoutManager2);
        recyclerView3.setLayoutManager(mLayoutManager3);
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(2, dpToPx(10), true));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
        recyclerView2.addItemDecoration(new GridSpacingItemDecoration(2, dpToPx(10), true));
        recyclerView2.setItemAnimator(new DefaultItemAnimator());
        recyclerView2.setAdapter(adapter);
        recyclerView3.addItemDecoration(new GridSpacingItemDecoration(2, dpToPx(10), true));
        recyclerView3.setItemAnimator(new DefaultItemAnimator());
        recyclerView3.setAdapter(adapter);


        prepareAlbums();
        adapter.notifyDataSetChanged();

        try {
            Glide.with(this).load(R.drawable.cover).into((ImageView) parentHolder.findViewById(R.id.backdrop));
        } catch (Exception e) {
            e.printStackTrace();
            //Log.e("Fuck", "here");
        }
        return parentHolder;
    }




    /**
     * Initializing collapsing toolbar
     * Will show and hide the toolbar title on scroll
     */
 /*   private void initCollapsingToolbar() {
        final CollapsingToolbarLayout collapsingToolbar =
                (CollapsingToolbarLayout) parentHolder.findViewById(R.id.collapsing_toolbar);
        collapsingToolbar.setTitle(" ");
        AppBarLayout appBarLayout = (AppBarLayout) parentHolder.findViewById(R.id.appbar);
        appBarLayout.setExpanded(true);
        Log.e("BlankFragment", "hiiiiiiiiiiiii");
        // hiding & showing the title when toolbar expanded & collapsed
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            boolean isShow = false;
            int scrollRange = -1;

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.getTotalScrollRange();
                }
                if (scrollRange + verticalOffset == 0) {
                    collapsingToolbar.setTitle(getString(R.string.digital));
                    isShow = true;
                } else if (isShow) {
                    collapsingToolbar.setTitle(getString(R.string.digital));
                    isShow = true;
                }
            }
        });
    }
*/
    /**
     * Adding few albums for testing
     */
    private void prepareAlbums() {

        int[] covers = new int[]{
                R.drawable.album1,
                R.drawable.album2,
                R.drawable.album3,
                R.drawable.album4,
                R.drawable.album5,
                R.drawable.album6,
                R.drawable.album7,
                R.drawable.album8,
                R.drawable.album9,
                R.drawable.album10,
                R.drawable.album11};

        album a = new album("True Romance", 13, covers[0]);
        albumList.add(a);

        a = new album("Xscpae", 8, covers[1]);
        albumList.add(a);

        a = new album("Maroon 5", 11, covers[2]);
        albumList.add(a);

        a = new album("Born to Die", 12, covers[3]);
        albumList.add(a);

        a = new album("Honeymoon", 14, covers[4]);
        albumList.add(a);

        a = new album("I Need a Doctor", 1, covers[5]);
        albumList.add(a);


    }

    /**
     * RecyclerView item decoration - give equal margin around grid item
     */

    public class GridSpacingItemDecoration extends RecyclerView.ItemDecoration {

        private int spanCount;
        private int spacing;
        private boolean includeEdge;

        public GridSpacingItemDecoration(int spanCount, int spacing, boolean includeEdge) {
            this.spanCount = spanCount;
            this.spacing = spacing;
            this.includeEdge = includeEdge;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            int position = parent.getChildAdapterPosition(view); // item position
            int column = position % spanCount; // item column

            if (includeEdge) {
                outRect.left = spacing - column * spacing / spanCount; // spacing - column * ((1f / spanCount) * spacing)
                outRect.right = (column + 1) * spacing / spanCount; // (column + 1) * ((1f / spanCount) * spacing)

                if (position < spanCount) { // top edge
                    outRect.top = spacing;
                }
                outRect.bottom = spacing; // item bottom
            } else {
                outRect.left = column * spacing / spanCount; // column * ((1f / spanCount) * spacing)
                outRect.right = spacing - (column + 1) * spacing / spanCount; // spacing - (column + 1) * ((1f /    spanCount) * spacing)
                if (position >= spanCount) {
                    outRect.top = spacing; // item top
                }
            }
        }
    }

    /**
     * Converting dp to pixel
     */
    private int dpToPx(int dp) {

        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }
}