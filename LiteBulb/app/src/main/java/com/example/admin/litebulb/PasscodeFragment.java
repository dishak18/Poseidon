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
import android.widget.Toast;

import com.example.admin.litebulb.SQL.AppConfig;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;


public class PasscodeFragment extends Fragment {
    Activity referenceActivity;
    View parentHolder;
    JSONParser jsonParser=new JSONParser();
    SharedPreferences loginPreferences;
    EditText currentPass, newPass, reEnterPass;
    String user_name;
    Button save;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        referenceActivity = getActivity();
        parentHolder = inflater.inflate(R.layout.fragment_passcode, container,
                false);
        currentPass=(EditText) parentHolder.findViewById(R.id.currentPass);
        newPass=(EditText) parentHolder.findViewById(R.id.newPass);
        reEnterPass=(EditText) parentHolder.findViewById(R.id.reEnter);
        save=(Button) parentHolder.findViewById(R.id.save);
        loginPreferences = getActivity().getSharedPreferences("loginPrefs", MODE_PRIVATE);

        Log.e("MainActivity", "this is username: "+loginPreferences.getString("username", ""));
        user_name=loginPreferences.getString("username", "");

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e("LoginActivity", "This is the name "+user_name+"     This is the password "+currentPass.getText().toString().trim());
                if(newPass.getText().toString().trim().equals(reEnterPass.getText().toString().trim())) {

                    AttemptLogin attemptLogin = new AttemptLogin();
                    attemptLogin.execute(user_name, currentPass.getText().toString().trim(), "");
                }
                else
                {
                    Toast.makeText(getContext(), "The passwords dont match!", Toast.LENGTH_LONG).show();
                }
            }
        });

        return parentHolder;
    }

    public interface OnFragmentInteractionListener {
    }


    private class AttemptLogin extends AsyncTask<String, String, JSONObject> {

        @Override

        protected void onPreExecute() {

            super.onPreExecute();

        }

        @Override

        protected JSONObject doInBackground(String... args) {



            String email = args[2];
            String password = args[1];
            String name= args[0];

            ArrayList params = new ArrayList();
            params.add(new BasicNameValuePair("username", name));
            params.add(new BasicNameValuePair("password", password));
            if(email.length()>0)
                params.add(new BasicNameValuePair("email",email));

            JSONObject json = jsonParser.makeHttpRequest(AppConfig.URL_PASSCODE, "POST", params);


            return json;

        }

        protected void onPostExecute(JSONObject result) {
            try {
                if (result != null) {
                    Log.e("LOGINACTIVITY", "this is the message"+result.getString("message"));
                    Toast.makeText(getContext(), result.getString("message"), Toast.LENGTH_LONG).show();

                    if (result.getString("message").equals("Successfully updated the password")) {

                        Log.e("PasscodeFragment", "in this now");

                        if( newPass.getText().toString().trim().equals(reEnterPass.getText().toString().trim()))
                        {
                            AttemptChangePassword attemptLogin= new AttemptChangePassword();
                            attemptLogin.execute(user_name, newPass.getText().toString());

                        }
                        else
                        {
                            Toast.makeText(getContext(), "The new passwords dont match", Toast.LENGTH_LONG).show();
                        }

                    }
                    else
                    {
                        Toast.makeText(getContext(),
                                "The password entered does not match your current password!!", Toast.LENGTH_LONG)
                                .show();

                    }


                } else {
                    Toast.makeText(getContext(),
                            "Please enter the credentials!", Toast.LENGTH_LONG)
                            .show();
                }
                //Toast.makeText(getApplicationContext(), "Unable to retrieve any data from server", Toast.LENGTH_LONG).show();
            } catch (JSONException e) {
                e.printStackTrace();
            }


        }
    }



    private class AttemptChangePassword extends AsyncTask<String, String, JSONObject> {

        @Override

        protected void onPreExecute() {

            super.onPreExecute();

        }

        @Override

        protected JSONObject doInBackground(String... args) {

            String password = args[1];
            String name= args[0];

            ArrayList params = new ArrayList();
            params.add(new BasicNameValuePair("username", name));
            params.add(new BasicNameValuePair("password", password));
            JSONObject json = jsonParser.makeHttpRequest(AppConfig.URL_PASSCODE_UPDATE, "POST", params);


            return json;

        }

        protected void onPostExecute(JSONObject result) {

            // dismiss the dialog once product deleted
            //Toast.makeText(getApplicationContext(),result,Toast.LENGTH_LONG).show();

            try {
                if (result != null) {
                    Toast.makeText(getContext(),result.getString("message"),Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getContext(), "Unable to retrieve any data from server", Toast.LENGTH_LONG).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }


        }

    }

}
