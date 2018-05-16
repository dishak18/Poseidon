package com.example.admin.litebulb;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.admin.litebulb.SQL.AppConfig;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import static com.example.admin.litebulb.ItemClickFragment.READ_TIMEOUT;


public class DepositFragment extends Fragment {
    Activity referenceActivity;
    View parentHolder;
    int count=0;
    String []deposit_array;
    LinearLayout.LayoutParams dim;
    RadioGroup lin;
    Button pay;
    RadioButton c[];
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        referenceActivity = getActivity();
        parentHolder = inflater.inflate(R.layout.fragment_deposit, container,
                false);

        pay=(Button) parentHolder.findViewById(R.id.pay);
        lin=(RadioGroup) parentHolder.findViewById(R.id.radioGroup);
        c=new RadioButton[20];
        dim=new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        new DepositCollection().execute();

        lin.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                c[1].getText().toString();
            }
        });
        // now we have array called deposit_array which has all the values, and radiobuttons c[i] with ids 1000+i respectively
        //so now we will put onclicklistener, and see which is clicked then take simultaneous value from deposit array and pass to paypal
        pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               // call the paypal thing here

            }
        });

        return parentHolder;
    }
    private class DepositCollection extends AsyncTask<String, String, String> {
        ProgressDialog pdLoading = new ProgressDialog(referenceActivity);
        HttpURLConnection conn;
        URL url = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            pdLoading.setMessage("\tLoading...");
            pdLoading.setCancelable(false);
            pdLoading.show();

        }

        @Override
        protected String doInBackground(String... params) {
            try {

                // Enter URL address where your json file resides
                // Even you can make call to php file which returns json data
                url = new URL(AppConfig.URL_DEPOSIT_SUMS);

            } catch (MalformedURLException e) {

                e.printStackTrace();
                return e.toString();
            }
            try {

                // Setup HttpURLConnection class to send and receive data from php and mysql
                conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(READ_TIMEOUT);
                conn.setConnectTimeout(ItemClickFragment.CONNECTION_TIMEOUT);
                conn.setRequestMethod("GET");

                // setDoOutput to true as we recieve data from json file
                conn.setDoOutput(true);

            } catch (IOException e1) {

                e1.printStackTrace();
                return e1.toString();
            }

            try {

                int response_code = conn.getResponseCode();

                // Check if successful connection made
                if (response_code == HttpURLConnection.HTTP_OK) {

                    // Read data sent from server
                    InputStream input = conn.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(input));
                    StringBuilder result = new StringBuilder();
                    String line;

                    while ((line = reader.readLine()) != null) {
                        result.append(line);
                    }

                    // Pass data to onPostExecute method
                    return (result.toString());

                } else {

                    return ("unsuccessful");
                }

            } catch (IOException e) {
                e.printStackTrace();
                return e.toString();
            } finally {
                conn.disconnect();
            }


        }

        @Override
        protected void onPostExecute(String result) {

            //this method will be running on UI thread

            pdLoading.dismiss();
            try {

                JSONArray jArray = new JSONArray(result);
                ArrayList<String> deposit_list = new ArrayList<String>();
                // Extract data from json and store into ArrayList as class objects
                for (int i = 0; i < jArray.length(); i++) {
                    JSONObject json_data = jArray.getJSONObject(i);
                    int deposit=json_data.getInt("deposit");
                    count++;
                    c[i]=new RadioButton(referenceActivity);
                    c[i].setLayoutParams(dim);
                    c[i].setId(i+1000);
                    c[i].setText("$"+ deposit);
                    deposit_list.add(deposit+"");
                    lin.addView(c[i]);
                    if(count>=20)
                    {
                        break;
                    }
                }
                deposit_array = deposit_list.toArray(new String[deposit_list.size()]);

            } catch (JSONException e) {
                Log.e("lala", "this haha in catch");
                Toast.makeText(referenceActivity, "Check your internet connection and try again later.", Toast.LENGTH_LONG).show();
            }
        }


    }



}