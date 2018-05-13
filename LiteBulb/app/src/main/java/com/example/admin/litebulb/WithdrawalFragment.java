package com.example.admin.litebulb;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
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

import static android.content.Context.MODE_PRIVATE;
import static com.example.admin.litebulb.ItemClickFragment.CONNECTION_TIMEOUT;
import static com.example.admin.litebulb.ItemClickFragment.READ_TIMEOUT;


public class WithdrawalFragment extends Fragment {
    Activity referenceActivity;
    SharedPreferences loginPreferences;
    String user_name;
    int earning, deposit, total;
    TextView tv_total, tv_balance, tv_earnings;
    RadioGroup radioGroup;
    EditText et_amount;
    int user_id;
    RadioButton rb_paypal, rb_moneybookers, rb_international_transfer;
    View parentHolder;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        referenceActivity = getActivity();
        parentHolder = inflater.inflate(R.layout.fragment_withdrawal, container,
                false);

        loginPreferences = getActivity().getSharedPreferences("loginPrefs", MODE_PRIVATE);

        Log.e("MainActivity", "this is username: "+loginPreferences.getString("username", ""));
        user_name=loginPreferences.getString("username", "");

        et_amount=(EditText) parentHolder.findViewById(R.id.amount);
        tv_balance=(TextView) parentHolder.findViewById(R.id.balance);
        tv_earnings=(TextView) parentHolder.findViewById(R.id.earnings);
        tv_total=(TextView) parentHolder.findViewById(R.id.total);
        radioGroup=(RadioGroup) parentHolder.findViewById(R.id.radioGroup);
        radioGroup.clearCheck();

        rb_international_transfer=(RadioButton) parentHolder.findViewById(R.id.rb_international_transfer);
        rb_moneybookers=(RadioButton) parentHolder.findViewById(R.id.rb_moneybookers);
        rb_paypal=(RadioButton) parentHolder.findViewById(R.id.rb_paypal);

        new UserDetails().execute();


        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
                if(checkedId==R.id.rb_international_transfer)
                {
                    if(Integer.parseInt(et_amount.getText().toString().trim())<=earning)
                    {
                        InternationalTransferPopUp fragment1 = new InternationalTransferPopUp();
                        Bundle args = new Bundle();
                        args.putInt("user_id", user_id);
                        args.putString("amount", et_amount.getText().toString());
                        fragment1.setArguments(args);
                        FragmentManager fragmentManager =getFragmentManager();
                        FragmentTransaction fragmentTransaction =fragmentManager.beginTransaction();
                        fragmentTransaction.replace(R.id.contentContainer, fragment1);
                        fragmentTransaction.addToBackStack(null);
                        fragmentTransaction.commit();

                    }
                    else
                    {
                        Toast.makeText(getContext(), "Please enter amount which is equal to or less than youw withrawable limit.", Toast.LENGTH_LONG).show();
                    }

                }
                else if(checkedId==R.id.rb_moneybookers)
                {
                    if(Integer.parseInt(et_amount.getText().toString().trim())<=earning)
                    {
                        MoneyBookersPopUp fragment1 = new MoneyBookersPopUp();
                        Bundle args = new Bundle();
                        args.putInt("user_id", user_id);
                        args.putString("amount", et_amount.getText().toString());
                        fragment1.setArguments(args);
                        FragmentManager fragmentManager =getFragmentManager();
                        FragmentTransaction fragmentTransaction =fragmentManager.beginTransaction();
                        fragmentTransaction.replace(R.id.contentContainer, fragment1);
                        fragmentTransaction.addToBackStack(null);
                        fragmentTransaction.commit();

                    }
                    else
                    {
                        Toast.makeText(getContext(), "Please enter amount which is equal to or less than youw withrawable limit.", Toast.LENGTH_LONG).show();
                    }

                }else if(checkedId==R.id.rb_paypal)
                {
                    if(Integer.parseInt(et_amount.getText().toString().trim())<=earning)
                    {
                        PaypalPopUp fragment1 = new PaypalPopUp();
                        Bundle args = new Bundle();
                        args.putInt("user_id", user_id);
                        args.putString("amount", et_amount.getText().toString());
                        fragment1.setArguments(args);
                        FragmentManager fragmentManager =getFragmentManager();
                        FragmentTransaction fragmentTransaction =fragmentManager.beginTransaction();
                        fragmentTransaction.replace(R.id.contentContainer, fragment1);
                        fragmentTransaction.addToBackStack(null);
                        fragmentTransaction.commit();

                    }
                    else
                    {
                        Toast.makeText(getContext(), "Please enter amount which is equal to or less than youw withrawable limit.", Toast.LENGTH_LONG).show();
                    }
                }
            }
        });





        return parentHolder;
    }

    @Override
    public void onStart() {
        super.onStart();
        radioGroup.clearCheck();
    }

    private class UserDetails extends AsyncTask<String, String, String> {
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
                url = new URL(AppConfig.URL_USER_FEATURED);

            } catch (MalformedURLException e) {

                e.printStackTrace();
                return e.toString();
            }
            try {

                // Setup HttpURLConnection class to send and receive data from php and mysql
                conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(READ_TIMEOUT);
                conn.setConnectTimeout(CONNECTION_TIMEOUT);
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
                ArrayList<String> item_ids = new ArrayList<String>();


                // Extract data from json and store into ArrayList as class objects
                for(int i=0;i<jArray.length();i++){
                    //Log.e("ColletionsItemClick", "this is in itemsdetails");
                    JSONObject json_data = jArray.getJSONObject(i);

                    //DataFish fishData = new DataFish();
                    String user_name_ran=json_data.getString("username");
                    if(user_name_ran.equals(user_name)) {
                        user_id=json_data.getInt("user_id");
                        earning=json_data.getInt("earning");
                        deposit=json_data.getInt("deposit");
                        total=deposit+earning;

                        tv_total.setText("$"+total);
                        tv_earnings.setText("$"+earning);
                        tv_balance.setText("$"+deposit);
                    }

                }

                //user_name_user_array = user_ids_of_user_table.toArray(new String[user_ids_of_user_table.size()]);

            } catch (JSONException e) {
                Toast.makeText(referenceActivity, e.toString(), Toast.LENGTH_LONG).show();
            }
        }
    }



}
