package com.example.admin.litebulb;

import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
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

import static com.example.admin.litebulb.ItemClickFragment.READ_TIMEOUT;


public class FAQFragment extends Fragment {
    private ProgressDialog pDialog;
    LinearLayout lin_faq, lin_question;
    LinearLayout.LayoutParams dim;
    Activity referenceActivity;
    View parentHolder;
    TextView tv_answers[], tv_edit[], tv_delete[];
    TextView tv_questions[];
    int count=0;
    EditText question ,answer;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        referenceActivity = getActivity();
        parentHolder = inflater.inflate(R.layout.fragment_faq, container,
                false);

        lin_faq=(LinearLayout) parentHolder.findViewById(R.id.lin_faq);


        question=(EditText) parentHolder.findViewById(R.id.question);
        answer=(EditText) parentHolder.findViewById(R.id.answer);
        tv_answers=new TextView[1000];
        tv_questions=new TextView[1000];
        tv_edit=new TextView[1000];
        tv_delete=new TextView[1000];
        dim=new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

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
                ArrayList<String> question_list = new ArrayList<String>();
                // Extract data from json and store into ArrayList as class objects
                for (int i = 0; i < jArray.length(); i++) {
                    JSONObject json_data = jArray.getJSONObject(i);
                    int deposit=json_data.getInt("deposit");
                    lin_question=new LinearLayout(getContext());
                    count++;
                    tv_questions[i]=new TextView(referenceActivity);
                    tv_questions[i].setLayoutParams(dim);

                    tv_answers[i]=new TextView(referenceActivity);
                    tv_answers[i].setLayoutParams(dim);

                    tv_delete[i]=new TextView(referenceActivity);
                    tv_delete[i].setLayoutParams(dim);
                    tv_edit[i]=new TextView(referenceActivity);
                    tv_edit[i].setLayoutParams(dim);

                    tv_edit[i].setTextColor(Color.GREEN);
                    tv_delete[i].setTextColor(Color.GREEN);
                    /*tv_questions[i].setId(i+1000);
                    tv_edit[i].setId("tv_edit"+i);
                    tv_delete[i].setId("tv_delete"+i);*/
                    final int j=i;
                    tv_questions[i].setText("Q. ");
                    lin_question.addView(tv_questions[i]);
                    lin_question.addView(tv_edit[i]);
                    lin_question.addView(tv_delete[i]);
                    lin_faq.addView(lin_question);
                    tv_questions[i].setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            tv_answers[j].setText("set answer here");
                            lin_faq.addView(tv_answers[j]);
                        }
                    });
                    tv_delete[i].setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                        }
                    });
                    tv_edit[i].setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                        }
                    });

                    question_list.add(deposit+"");





                    if(count>=1000)
                    {
                        break;
                    }
                }

            } catch (JSONException e) {
                Log.e("lala", "this haha in catch");
                Toast.makeText(referenceActivity, e.toString(), Toast.LENGTH_LONG).show();
            }
        }


    }


}