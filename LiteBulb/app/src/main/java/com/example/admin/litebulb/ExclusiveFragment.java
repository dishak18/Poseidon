package com.example.admin.litebulb;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
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
import java.util.List;

import static android.content.Context.MODE_PRIVATE;
import static com.example.admin.litebulb.ItemClickFragment.READ_TIMEOUT;
import static com.example.admin.litebulb.ProfileEdit.CONNECTION_TIMEOUT;


public class ExclusiveFragment extends Fragment {
    Activity referenceActivity;
    View parentHolder;
    SharedPreferences loginPreferences;
    String username;
    String exclusive_author_text;
    TextView exclusive_author;
    Button sub_button;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        referenceActivity = getActivity();
        parentHolder = inflater.inflate(R.layout.fragment_exclusive, container,
                false);
        sub_button=(Button) parentHolder.findViewById(R.id.sub_button);
        exclusive_author=(TextView) parentHolder.findViewById(R.id.exclusive_author_or_not);

        loginPreferences = getActivity().getSharedPreferences("loginPrefs", MODE_PRIVATE);
        username=loginPreferences.getString("username", "");
        new UserDetails().execute();
        sub_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                /*GetData();*/

                InsertData(exclusive_author_text, username);
                new UserDetails().execute();

            }
        });


        return parentHolder;
    }


    public interface OnFragmentInteractionListener {
    }
    public void InsertData(final String exclusiveAuthor, final String username){

        class SendPostReqAsyncTask extends AsyncTask<String, Void, String> {
            @Override
            protected String doInBackground(String... params) {

                String ExclusiveAuthorHolder = exclusiveAuthor ;
                String UserNameHolder = username ;

                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();

                nameValuePairs.add(new BasicNameValuePair("exclusive_author", ExclusiveAuthorHolder));
                nameValuePairs.add(new BasicNameValuePair("username", UserNameHolder));

                try {
                    HttpClient httpClient = new DefaultHttpClient();

                    HttpPost httpPost = new HttpPost(AppConfig.URL_EXCLUSIVE_UPDATE);

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

                Toast.makeText(referenceActivity, "Updated Successfully", Toast.LENGTH_LONG).show();

            }
        }

        SendPostReqAsyncTask sendPostReqAsyncTask = new SendPostReqAsyncTask();

        sendPostReqAsyncTask.execute(exclusiveAuthor, username);
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
                        if(json_data.getString("exclusive_author").equals("true"))
                        {

                            Log.e("ExclusiveFragment", "This is where I am in exc frag and exc is true ");
                            exclusive_author.setText("Exclusive Author");
                            sub_button.setText("Unsubscribe me as an Exclusive Author");
                            exclusive_author_text="false";
                        }
                        else
                        {
                            exclusive_author.setText("Not Exclusive Author");
                            sub_button.setText("Subscribe me as an Exclusive Author");
                            exclusive_author_text="true";
                        }

                    }
                }

            } catch (JSONException e) {
                Toast.makeText(referenceActivity, "Please check your internet connection", Toast.LENGTH_LONG).show();
            }

        }

    }

}