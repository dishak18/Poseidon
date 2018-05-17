package com.example.admin.litebulb;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;
import static com.example.admin.litebulb.ItemClickFragment.READ_TIMEOUT;


public class DepositFragment extends Fragment {
    Activity referenceActivity;
    View parentHolder;
    int count=0;
    String []deposit_array;
    LinearLayout.LayoutParams dim;
    RadioGroup lin;
    Button pay;
    String username;
    SharedPreferences loginPreferences;
    RadioButton c[];
    private SharedPreferences preferences;
    private PayPalConfiguration mConfiguration;
    private String mPaypalClientId = "Ad6IZpxI8Q_oFapwnrk0iAGQ914n5qHrpe5iyC5DyaaJDgb0Oyw6LlzeGG6EnLa5TEiCMWXDsc8MQBpP";
    private Intent mService;
    private int mPaypalRequestCode = 999;
    private String depositPrice = null;
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
        loginPreferences = getActivity().getSharedPreferences("loginPrefs", MODE_PRIVATE);
        username=loginPreferences.getString("username", "");

        dim=new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        preferences = getActivity().getSharedPreferences("preferences", Context.MODE_PRIVATE);
        final SharedPreferences.Editor editor = preferences.edit();
        new DepositCollection().execute();



        lin.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                Log.e("DepositFragment","Text of button is :    "+c[i-1000].getText().toString());
                depositPrice = c[i-1000].getText().toString().substring(1);
                if(depositPrice.equals(""))
                {

                }
            }
        });

        mConfiguration = new PayPalConfiguration()
                .environment(PayPalConfiguration.ENVIRONMENT_SANDBOX)
                .clientId(mPaypalClientId);

        mService = new Intent(getContext(), PayPalService.class);
        mService.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, mConfiguration);
        getActivity().startService(mService);
        // now we have array called deposit_array which has all the values, and radiobuttons c[i] with ids 1000+i respectively
        //so now we will put onclicklistener, and see which is clicked then take simultaneous value from deposit array and pass to paypal
        pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(depositPrice.equals(""))
                {
                    Toast.makeText(getContext(), "Please choose an amount", Toast.LENGTH_SHORT).show();
                }else {


                    PayPalPayment payment = new PayPalPayment(new BigDecimal(Integer.parseInt(depositPrice)), "USD", "Test Payment with PayPal",
                            PayPalPayment.PAYMENT_INTENT_SALE);

                    Intent intent = new Intent(getContext(), PaymentActivity.class);
                    intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, mConfiguration);
                    intent.putExtra(PaymentActivity.EXTRA_PAYMENT, payment);
                    startActivityForResult(intent, mPaypalRequestCode);
                }

            }
        });

        return parentHolder;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == mPaypalRequestCode){
            if(resultCode == Activity.RESULT_OK){
                PaymentConfirmation confirmation = data.getParcelableExtra(PaymentActivity.EXTRA_RESULT_CONFIRMATION);
                if(confirmation!=null){
                    String state = confirmation.getProofOfPayment().getState();
                    if(state.equals("approved")){

                        InsertData(username, depositPrice);
                        //mResponse.setText("Approved");
                    }
                    else{

                    }
                    //mResponse.setText("Error in the payment");
                }
                else{

                }
                //mResponse.setText("Confirmation is null");
            }
        }
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

    public void InsertData(final String username, final String deposit){

        class SendPostReqAsyncTask extends AsyncTask<String, Void, String> {
            @Override
            protected String doInBackground(String... params) {

                String UserNameHolder = username ;
                String TotalHolder = deposit ;


                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();

                nameValuePairs.add(new BasicNameValuePair("username",UserNameHolder));
                nameValuePairs.add(new BasicNameValuePair("deposit", TotalHolder));

                try {
                    HttpClient httpClient = new DefaultHttpClient();

                    HttpPost httpPost = new HttpPost(AppConfig.URL_USER_DEPOSIT);

                    httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

                    HttpResponse httpResponse = httpClient.execute(httpPost);

                    HttpEntity httpEntity = httpResponse.getEntity();


                } catch (ClientProtocolException e) {

                } catch (IOException e) {

                }
                return "Data Inserted Successfully";
            }

            @Override
            protected void onPostExecute(String result) {

                super.onPostExecute(result);

                Toast.makeText(referenceActivity, "Deposit Successful", Toast.LENGTH_LONG).show();

            }
        }

        SendPostReqAsyncTask sendPostReqAsyncTask = new SendPostReqAsyncTask();

        sendPostReqAsyncTask.execute(username, deposit);
    }







}