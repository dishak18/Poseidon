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
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
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
import java.util.HashMap;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;
import static com.example.admin.litebulb.ItemClickFragment.READ_TIMEOUT;


public class ProfileEdit extends Fragment {
    Activity referenceActivity;
    public static final int CONNECTION_TIMEOUT = 10000;
    View parentHolder;
    HashMap<String,String> hashMap = new HashMap<>();
    EditText firstName, lastName, email, companyName, livesIn;
    Switch login_with_fb, available_for_free, item_update, daily_summary;
    Spinner country;
    SharedPreferences loginPreferences;
    String FirstName, LastName, Email, LivesIn, CompanyName, FreeLance, Daily, ItemNote;
    Button save;
    String username;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        referenceActivity = getActivity();
        parentHolder = inflater.inflate(R.layout.fragment_profile_edit, container,
                false);
        firstName=(EditText) parentHolder.findViewById(R.id.firstName);
        lastName=(EditText) parentHolder.findViewById(R.id.lastName);
        email=(EditText) parentHolder.findViewById(R.id.email);
        livesIn=(EditText) parentHolder.findViewById(R.id.livesIn);
        companyName=(EditText) parentHolder.findViewById(R.id.companyName);
        login_with_fb=(Switch) parentHolder.findViewById(R.id.login_with_fb);
        available_for_free=(Switch) parentHolder.findViewById(R.id.available_for_free);
        item_update=(Switch) parentHolder.findViewById(R.id.item_update);
        daily_summary=(Switch) parentHolder.findViewById(R.id.daily_summary);
        //country=(Spinner) parentHolder.findViewById(R.id.country);
        save=(Button) parentHolder.findViewById(R.id.save);


        loginPreferences = getActivity().getSharedPreferences("loginPrefs", MODE_PRIVATE);
        username=loginPreferences.getString("username", "");
         new UserDetails().execute();
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                GetData();
                InsertData(FirstName, Email, LastName, LivesIn, CompanyName, Daily, FreeLance, ItemNote);
                new UserDetails().execute();

            }
        });

        return parentHolder;
    }
    public void GetData(){

        FirstName = firstName.getText().toString();
        LastName = lastName.getText().toString();
        Email = email.getText().toString();
        LivesIn = livesIn.getText().toString();
        CompanyName = companyName.getText().toString();
        if(available_for_free.isChecked())
        {

            FreeLance="true";
            Log.e("LALALALA", "This is the value of the string freelance "+FreeLance);
        }
        else
        {
            FreeLance="false";
            Log.e("LALALALA", "This is the value of the string freelance "+FreeLance);
        }
        if(item_update.isChecked())
        {
            ItemNote="true";
        }
        else
        {
            ItemNote="false";
        }
        if(daily_summary.isChecked())
        {
            Daily="true";
        }
        else
        {
            Daily="false";
        }


    }

    public interface OnFragmentInteractionListener {
    }

    public void InsertData(final String firstName, final String email, final String lastName, final String livesIn, final String companyName, final String daily, final String freelance, final String item_note){

        class SendPostReqAsyncTask extends AsyncTask<String, Void, String> {
            @Override
            protected String doInBackground(String... params) {

                String FirstNameHolder = firstName ;
                String EmailHolder = email ;
                String LastNameHolder = lastName ;
                String LivesInHolder = livesIn ;
                String CompanyNameHolder = companyName ;
                String DailyHolder=daily;
                String FreeLanceHolder=freelance;
                String ItemNoteHolder=item_note;

                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();

                nameValuePairs.add(new BasicNameValuePair("firstname", FirstNameHolder));
                nameValuePairs.add(new BasicNameValuePair("lastname", LastNameHolder));
                nameValuePairs.add(new BasicNameValuePair("email", EmailHolder));
                nameValuePairs.add(new BasicNameValuePair("live_city", LivesInHolder));
                nameValuePairs.add(new BasicNameValuePair("firmname", CompanyNameHolder));
                nameValuePairs.add(new BasicNameValuePair("username", username));
                nameValuePairs.add(new BasicNameValuePair("daily", DailyHolder));
                nameValuePairs.add(new BasicNameValuePair("freelance", FreeLanceHolder));
                nameValuePairs.add(new BasicNameValuePair("item_note", ItemNoteHolder));

                try {
                    HttpClient httpClient = new DefaultHttpClient();

                    HttpPost httpPost = new HttpPost(AppConfig.URL_USER_GETDATA);

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

                Toast.makeText(referenceActivity, "Data Updated Successfully", Toast.LENGTH_LONG).show();

            }
        }

        SendPostReqAsyncTask sendPostReqAsyncTask = new SendPostReqAsyncTask();

        sendPostReqAsyncTask.execute(firstName, lastName, email, livesIn, companyName);
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
                        firstName.setText(json_data.getString("firstname"));
                        lastName.setText(json_data.getString("lastname"));
                        email.setText(json_data.getString("email"));
                        livesIn.setText(json_data.getString("live_city"));
                        companyName.setText(json_data.getString("firmname"));
                    }
                }

            } catch (JSONException e) {
                Toast.makeText(referenceActivity, e.toString(), Toast.LENGTH_LONG).show();
            }

        }

    }

}