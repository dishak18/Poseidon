package com.example.admin.litebulb;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
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
import static com.example.admin.litebulb.ProfileEdit.CONNECTION_TIMEOUT;


public class PaymentFragment extends Fragment {
    Activity referenceActivity;
    View parentHolder;
    private Button buyNowPaypal, prepaidDeposits;
    private PayPalConfiguration mConfiguration;
    private String mPaypalClientId = "Ad6IZpxI8Q_oFapwnrk0iAGQ914n5qHrpe5iyC5DyaaJDgb0Oyw6LlzeGG6EnLa5TEiCMWXDsc8MQBpP";
    private Intent mService;
    int price_of_item;
    int total;
    String username;
    SharedPreferences loginPreferences;
    private int mPaypalRequestCode = 999;
    private SharedPreferences preferences;
    TextView pay_from_account, payment_text;
    private TextView mResponse;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        referenceActivity = getActivity();
        parentHolder = inflater.inflate(R.layout.fragment_payment, container,
                false);
        prepaidDeposits=(Button) parentHolder.findViewById(R.id.prepaid_deposits);
        pay_from_account=(TextView)parentHolder.findViewById(R.id.pay_from_account);
        payment_text=(TextView) parentHolder.findViewById(R.id.payment_text);
        buyNowPaypal = (Button) parentHolder.findViewById(R.id.buy_now_payment);
        preferences = getContext().getSharedPreferences("preferences", Context.MODE_PRIVATE);
        mResponse = (TextView) parentHolder.findViewById(R.id.paypal_response);
        price_of_item=Integer.parseInt(preferences.getString("itemPrice",""));

        loginPreferences = getActivity().getSharedPreferences("loginPrefs", MODE_PRIVATE);
        username=loginPreferences.getString("username", "");

        pay_from_account.setText("Pay From Your Account Balance $"+price_of_item);
        payment_text.setText("You Will Be Redirected To The Choice Of Payment Mathod for $"+price_of_item);
        new UserDetails().execute();

        buyNowPaypal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PayPalPayment payment = new PayPalPayment(new BigDecimal(Integer.parseInt(preferences.getString("itemPrice",""))), "USD", "Test Payment with PayPal",
                        PayPalPayment.PAYMENT_INTENT_SALE);

                Intent intent = new Intent(getContext(), PaymentActivity.class);
                intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, mConfiguration);
                intent.putExtra(PaymentActivity.EXTRA_PAYMENT, payment);
                startActivityForResult(intent, mPaypalRequestCode);
            }
        });
        prepaidDeposits.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(price_of_item<=total)
                {
                    Toast.makeText(getContext(), "Paying via Deposits..", Toast.LENGTH_SHORT).show();
                    total=total-price_of_item;
                    InsertData(username, total+"");
                    Toast.makeText(getContext(), "Your Prepaid Balance now is $"+total, Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Toast.makeText(getContext(), "Low Balance!! Cannot Pay", Toast.LENGTH_SHORT).show();
                }

            }
        });
        mConfiguration = new PayPalConfiguration()
                .environment(PayPalConfiguration.ENVIRONMENT_SANDBOX)
                .clientId(mPaypalClientId);

        mService = new Intent(getContext(), PayPalService.class);
        mService.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, mConfiguration);
        getActivity().startService(mService);
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
                        mResponse.setText("Approved");
                    }
                    else
                        mResponse.setText("Error in the payment");
                }
                else
                    mResponse.setText("Confirmation is null");
            }
        }
    }

    public void InsertData(final String username, final String total){

        class SendPostReqAsyncTask extends AsyncTask<String, Void, String> {
            @Override
            protected String doInBackground(String... params) {

                String UserNameHolder = username ;
                String TotalHolder = total ;


                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();

                nameValuePairs.add(new BasicNameValuePair("username",UserNameHolder));
                nameValuePairs.add(new BasicNameValuePair("total", TotalHolder));

                try {
                    HttpClient httpClient = new DefaultHttpClient();

                    HttpPost httpPost = new HttpPost(AppConfig.URL_USER_PREPAID);

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

                Toast.makeText(referenceActivity, "Payment Successful", Toast.LENGTH_LONG).show();

            }
        }

        SendPostReqAsyncTask sendPostReqAsyncTask = new SendPostReqAsyncTask();

        sendPostReqAsyncTask.execute(username, total);
    }




    private class UserDetails extends AsyncTask<String, String, String> {

        HttpURLConnection conn;
        URL url = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected String doInBackground(String... params) {
            try {

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
                conn.setDoOutput(true);

            } catch (IOException e1) {

                e1.printStackTrace();
                return e1.toString();

            }

            try {

                int response_code = conn.getResponseCode();

                if (response_code == HttpURLConnection.HTTP_OK) {

                    InputStream input = conn.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(input));
                    StringBuilder result = new StringBuilder();
                    String line;

                    while ((line = reader.readLine()) != null) {
                        result.append(line);
                    }

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

            // pdLoading.dismiss();
            try {

                JSONArray jArray = new JSONArray(result);

                for(int i=0;i<jArray.length();i++) {
                    JSONObject json_data = jArray.getJSONObject(i);
                    String username_of_user=json_data.getString("username");

                    if(username_of_user.equals(username)) {
                        //Log.e("lalala", "This is the badges of the users table " + badges_from_users);
                        total=json_data.getInt("total");
                    }
                }

            } catch (JSONException e) {
                //Toast.makeText(referenceActivity, e.toString(), Toast.LENGTH_LONG).show();
            }

        }

    }



}
