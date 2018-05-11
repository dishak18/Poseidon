package com.example.admin.litebulb;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
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


public class NewCollectionFragment extends Fragment {
    Activity referenceActivity;
    EditText title, description;
    String username;
    int user_id_user_table;
    CheckBox public_collection;
    SharedPreferences loginPreferences;
    String Title, Description, Public;
    int UserId;
    Button submit;
    View parentHolder;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //Inflate the layout for this fragment
        referenceActivity = getActivity();

        parentHolder = inflater.inflate(R.layout.fragment_new_collection, container,
                false);
        title=(EditText)parentHolder.findViewById(R.id.title);
        description=(EditText) parentHolder.findViewById(R.id.description);
        submit=(Button) parentHolder.findViewById(R.id.submit);
        public_collection=(CheckBox) parentHolder.findViewById(R.id.make_public);
        loginPreferences = getActivity().getSharedPreferences("loginPrefs", MODE_PRIVATE);
        username=loginPreferences.getString("username", "");
        new UserDetails().execute();
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                GetData();
                InsertData(Title, Description, UserId+"", Public);
                FragmentManager manager = getFragmentManager();
                manager.beginTransaction().replace(R.id.contentContainer, new UserCollectionFolderFragment()).addToBackStack(null).commit();

            }
        });


        return parentHolder;
    }
    public void GetData(){

        Title = title.getText().toString();
        Description = description.getText().toString();
        UserId = user_id_user_table;

        if(public_collection.isChecked())
        {

            Public="true";
          //  Log.e("LALALALA", "This is the value of the string freelance "+FreeLance);
        }
        else
        {
            Public="false";
            //Log.e("LALALALA", "This is the value of the string freelance "+FreeLance);
        }

    }

    public void InsertData(final String name, final String text, final String user_id, final String public_collection){

        class SendPostReqAsyncTask extends AsyncTask<String, Void, String> {
            @Override
            protected String doInBackground(String... params) {

                String TitleHolder = name ;
                String DescriptionHolder = text ;
                String UserIdHolder = user_id ;
                String PublicHolder=public_collection;

                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();

                nameValuePairs.add(new BasicNameValuePair("name", TitleHolder));
                nameValuePairs.add(new BasicNameValuePair("user_id", UserIdHolder));
                nameValuePairs.add(new BasicNameValuePair("text", DescriptionHolder));
                nameValuePairs.add(new BasicNameValuePair("public", PublicHolder));
                try {
                    HttpClient httpClient = new DefaultHttpClient();

                    HttpPost httpPost = new HttpPost(AppConfig.URL_NEW_COLLECTIONS);

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

        sendPostReqAsyncTask.execute(name, text, user_id, public_collection);
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
                        user_id_user_table=json_data.getInt("user_id");
                    }
                }

            } catch (JSONException e) {
                Toast.makeText(referenceActivity, e.toString(), Toast.LENGTH_LONG).show();
            }

        }

    }

}