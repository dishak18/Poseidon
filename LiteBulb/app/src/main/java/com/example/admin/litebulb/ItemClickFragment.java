package com.example.admin.litebulb;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.bumptech.glide.Glide;
import com.example.admin.litebulb.SQL.AppConfig;
import com.example.admin.litebulb.SQL.AppController;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static com.bumptech.glide.gifdecoder.GifHeaderParser.TAG;


public class ItemClickFragment extends Fragment {

    Activity referenceActivity;
    View parentHolder;
    String name, thumbnail, image_url, key;
    int id_of_items_table, price;
    int itemId;
    ImageView top_image;
    TextView name_of_item, description, item_price, text_under_select_license;
    Spinner select_license;
    String selectedItem, value, index, prepaid;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        referenceActivity = getActivity();
        itemId = getArguments().getInt("id");
        try{
            parentHolder = inflater.inflate(R.layout.fragment_item_click, container,
                    false);
        }catch (Exception e) {
            Log.e("ITEMCLICKFRAGMENT", e + " This is the error");
            throw e;
        }
        top_image=(ImageView) parentHolder.findViewById(R.id.top_image);
        name_of_item=(TextView) parentHolder.findViewById(R.id.name_of_item);
        description=(TextView) parentHolder.findViewById(R.id.description);
        item_price=(TextView) parentHolder.findViewById(R.id.item_price);
        select_license=(Spinner) parentHolder.findViewById(R.id.select_license);
        getSystemDetails();
        getSelectedItemDetails();
        select_license.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedItem = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                selectedItem=parent.getItemAtPosition(0).toString();

            }
        });

        return parentHolder;
    }

    private void getSelectedItemDetails() {

        JsonArrayRequest req = new JsonArrayRequest(AppConfig.URL_ITEM,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            //int count_featured_authors=0;
                            for (int i = 0; i < response.length(); i++) {

                                JSONObject person = (JSONObject) response.get(i);

                                 name = person.getString("name");
                                 id_of_items_table = person.getInt("id");
                                 price = person.getInt("price");
                                 String descrip=person.getString("description");
                                 thumbnail=person.getString("thumbnail");
                                 image_url = AppConfig.URL_PHOTOS +thumbnail;
                                //Toast.makeText(referenceActivity, itemId +"anddd "+ id_of_items_table, Toast.LENGTH_SHORT).show();
                               // Log.e("ITEMCLICKFRAGMENT", "this is the item id from other fragment "+itemId+" this is a\the id "+id_of_items_table);
                                 if(id_of_items_table==itemId)
                                 {
                                     Glide.with(getContext())
                                             .load(image_url)
                                             .placeholder(R.drawable.loader)
                                             .error(R.drawable.studio)
                                             .into(top_image);
                                     name_of_item.setText(name);
                                     description.setText(descrip);
                                   //  item_price.setText(price);
                                 }
                                 if(selectedItem.equals("Personal Use License"))
                                 {
                                     item_price.setText(price);
                                 }
                                 else
                                 {
                                     int net_price=price*Integer.parseInt(index);
                                     item_price.setText(net_price);
                                 }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(referenceActivity,
                                    "Error: " + e.getMessage(),
                                    Toast.LENGTH_LONG).show();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                Toast.makeText(referenceActivity,
                        error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        try{
            AppController.getInstance().addToRequestQueue(req);

        }catch(Exception e)
        {
            Log.e("BlankFragment3", e+ " This is the error");
        }
    }
    private void getSystemDetails() {

        JsonArrayRequest req = new JsonArrayRequest(AppConfig.URL_SYSTEM,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            //int count_featured_authors=0;
                            for (int i = 0; i < response.length(); i++) {

                                JSONObject person = (JSONObject) response.get(i);

                                key=person.getString("key");
                                value=person.getString("value");
                                //image_url = AppConfig.URL_PHOTOS +thumbnail;
                                //Toast.makeText(referenceActivity, itemId +"anddd "+ id_of_items_table, Toast.LENGTH_SHORT).show();
                                // Log.e("ITEMCLICKFRAGMENT", "this is the item id from other fragment "+itemId+" this is a\the id "+id_of_items_table);
                                if(key.equals("extended_price"))
                                {

                                    index=value;
                                    //  item_price.setText(price);
                                }
                                else if(key.equals("prepaid_price_discount"))
                                {
                                    prepaid=value;
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(referenceActivity,
                                    "Error: " + e.getMessage(),
                                    Toast.LENGTH_LONG).show();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                Toast.makeText(referenceActivity,
                        error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        try{
            AppController.getInstance().addToRequestQueue(req);

        }catch(Exception e)
        {
            Log.e("ItemCickFragment", e+ " This is the error");
        }
    }

}
