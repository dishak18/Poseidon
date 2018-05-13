package com.example.admin.litebulb;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.admin.litebulb.SQL.AppConfig;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class PaypalPopUp extends Fragment {
    Activity referenceActivity;
    View parentHolder;
    int user_id;
    EditText et_re, et_address;
    Button submit;
    String amount;
    String method, text;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        referenceActivity = getActivity();
        parentHolder = inflater.inflate(R.layout.fragment_paypal_pop_up, container,
                false);
        user_id = getArguments().getInt("user_id");
        amount=getArguments().getString("amount");

        et_address=(EditText) parentHolder.findViewById(R.id.paypal_address);
        et_re=(EditText) parentHolder.findViewById(R.id.re);
        submit=(Button) parentHolder.findViewById(R.id.submit);
        method="moneybooker";
        /*et_address.setText(et_address.getText().toString().trim()+" ");
        et_re.setText(et_re.getText().toString().trim()+" ");*/
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(et_address.getText().toString().trim().equals(et_re.getText().toString().trim()))
                {
                    if(et_address.getText().toString().trim().equals(""))
                    {
                        Toast.makeText(getContext(), "Please enter the details", Toast.LENGTH_SHORT).show();

                    }
                    else
                    {
                        text=et_address.getText().toString().trim();
                        InsertData(method, text, user_id+"", amount);
                    }

                }
                else
                {
                    Toast.makeText(getContext(), "The addresses don't match.", Toast.LENGTH_SHORT).show();
                    et_address.setText("");
                    et_re.setText("");
                }

            }
        });

        return parentHolder;
    }

    public void InsertData(final String method, final String text, final String user_id, final String amount){

        class SendPostReqAsyncTask extends AsyncTask<String, Void, String> {
            @Override
            protected String doInBackground(String... params) {

                String MethodHolder = method ;
                String DescriptionHolder = text ;
                String UserIdHolder = user_id ;
                String AmountHolder=amount;

                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();

                nameValuePairs.add(new BasicNameValuePair("amount", AmountHolder));
                nameValuePairs.add(new BasicNameValuePair("user_id", UserIdHolder));
                nameValuePairs.add(new BasicNameValuePair("text", DescriptionHolder));
                nameValuePairs.add(new BasicNameValuePair("method", MethodHolder));
                //nameValuePairs.add(new BasicNameValuePair("method", PublicHolder));
                try {
                    HttpClient httpClient = new DefaultHttpClient();

                    HttpPost httpPost = new HttpPost(AppConfig.URL_INSERT_WITHDRAW);

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

                Toast.makeText(referenceActivity, "Request Submitted Successfully", Toast.LENGTH_LONG).show();
                et_address.setText("");
                et_re.setText("");

            }
        }

        SendPostReqAsyncTask sendPostReqAsyncTask = new SendPostReqAsyncTask();

        sendPostReqAsyncTask.execute(amount, user_id, text, method);
    }


}