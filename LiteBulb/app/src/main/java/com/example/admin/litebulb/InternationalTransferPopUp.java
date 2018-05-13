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


public class InternationalTransferPopUp extends Fragment {
    Activity referenceActivity;
    View parentHolder;
    EditText et_address, et_acc_name, et_acc_num, et_swift_code, et_iban, et_bank_name, et_branch_address, et_dest_country;
    Button submit;
    int user_id;
    String amount;
    String method, text;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        referenceActivity = getActivity();
        parentHolder = inflater.inflate(R.layout.fragment_international_transfer_pop_up, container,
                false);
        user_id = getArguments().getInt("user_id");
        amount=getArguments().getString("amount");
        et_address=(EditText) parentHolder.findViewById(R.id.address);
        et_acc_name=(EditText) parentHolder.findViewById(R.id.acc_name);
        et_acc_num=(EditText) parentHolder.findViewById(R.id.acc_number);
        et_swift_code=(EditText) parentHolder.findViewById(R.id.swift_code);
        et_iban=(EditText) parentHolder.findViewById(R.id.iban);
        et_bank_name=(EditText) parentHolder.findViewById(R.id.bank_name);
        et_branch_address=(EditText) parentHolder.findViewById(R.id.branch_address);
        et_dest_country=(EditText) parentHolder.findViewById(R.id.dest_country);
       /* et_address.setText(et_address.getText().toString().trim()+" ");
        et_acc_name.setText(et_acc_name.getText().toString().trim()+" ");
        et_acc_num.setText(et_acc_num.getText().toString().trim()+" ");
        et_swift_code.setText(et_swift_code.getText().toString().trim()+" ");
        et_iban.setText(et_iban.getText().toString().trim()+" ");
        et_bank_name.setText(et_bank_name.getText().toString().trim()+" ");
        et_branch_address.setText(et_branch_address.getText().toString().trim()+" ");
        et_dest_country.setText(et_dest_country.getText().toString().trim()+" ");*/

        submit=(Button) parentHolder.findViewById(R.id.submit);
        method="swift";
        text=et_address.getText().toString().trim()+" , "+et_acc_num.getText().toString().trim()+" , "+et_acc_name.getText().toString().trim()+" , "+et_dest_country.getText().toString().trim()+" , "+et_swift_code.getText().toString().trim()+" , "+et_iban.getText().toString().trim()+" , "+et_bank_name.getText().toString().trim()+" , "+et_branch_address.getText().toString().trim();
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                InsertData(method, text, user_id+"" , amount);
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


            }
        }

        SendPostReqAsyncTask sendPostReqAsyncTask = new SendPostReqAsyncTask();

        sendPostReqAsyncTask.execute(amount, user_id, text, method);
    }






}