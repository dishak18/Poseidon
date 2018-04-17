package com.example.admin.litebulb;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.admin.litebulb.SQL.AppConfig;
import com.example.admin.litebulb.SQL.SQLiteHandler;
import com.example.admin.litebulb.SQL.SessionManager;

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

import static com.example.admin.litebulb.ItemClickFragment.CONNECTION_TIMEOUT;
import static com.example.admin.litebulb.ItemClickFragment.READ_TIMEOUT;


public class LoginActivity extends Activity {
    private Button btnLogin;
    private Button btnLinkToRegister;
    private EditText inputName;
    private EditText inputPassword;
    private ProgressDialog pDialog;
    private SharedPreferences loginPreferences;
    private String username,password;
    private SharedPreferences.Editor loginPrefsEditor;
    private SessionManager session;
    String status;
    private SQLiteHandler db;
    JSONParser jsonParser=new JSONParser();
    CheckBox remember_me;
    private ImageButton back_btn;
    private Boolean saveLogin;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        remember_me=(CheckBox) findViewById(R.id.rem);
        inputName = (EditText) findViewById(R.id.name);
        inputPassword = (EditText) findViewById(R.id.pass);
        btnLogin = (Button) findViewById(R.id.log);
        btnLinkToRegister = (Button) findViewById(R.id.register);
        back_btn=(ImageButton) findViewById(R.id.back);
        //CheckBox remember=(CheckBox)findViewById(R.id.rem) ;
        /*SystemBarTintManager tintManager = new SystemBarTintManager(this);
        tintManager.setStatusBarTintEnabled(true);
        tintManager.setStatusBarTintResource(R.color.ColorPrimary);*/

        // Progress dialog
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);

        // SQLite database handler
        db = new SQLiteHandler(getApplicationContext());

        // Session manager
        session = new SessionManager(getApplicationContext());

        // Check if user is already logged in or not
        if (session.isLoggedIn()) {
            // User is already logged in. Take him to main activity
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }

        // Login button Click Event
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new UserDetails().execute();
                /*AttemptLogin attemptLogin= new AttemptLogin();
                attemptLogin.execute(inputName.getText().toString(),inputPassword.getText().toString(),"");
       */     }
        });

       /* btnLogin.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                String username = inputName.getText().toString().trim();
                String password = inputPassword.getText().toString().trim();

                // Check for empty data in the form
                if (!username.isEmpty() && !password.isEmpty()) {
                    // login user
                    checkLogin(username, password);
                    InputMethodManager imm = (InputMethodManager)getSystemService(LoginActivity.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(inputName.getWindowToken(), 0);

                    username = inputName.getText().toString();
                    password = inputPassword.getText().toString();

                    if (remember_me.isChecked()) {
                        loginPrefsEditor.putBoolean("saveLogin", true);
                        loginPrefsEditor.putString("username", username);
                        loginPrefsEditor.putString("password", password);
                        loginPrefsEditor.commit();
                    } else {
                        loginPrefsEditor.clear();
                        loginPrefsEditor.commit();
                    }

                    doSomethingElse();
                } else {
                    // Prompt user to enter credentials
                    Toast.makeText(getApplicationContext(),
                            "Please enter the credentials!", Toast.LENGTH_LONG)
                            .show();
                }
            }

        });
*/
        // Link to Register Screen
        btnLinkToRegister.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(),
                        SignUpActivity.class);
                startActivity(i);
                finish();
            }
        });
        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);
                finish();

            }
        });
        loginPreferences = getSharedPreferences("loginPrefs", MODE_PRIVATE);
        loginPrefsEditor = loginPreferences.edit();

        saveLogin = loginPreferences.getBoolean("saveLogin", false);
        if (saveLogin == true) {
            inputName.setText(loginPreferences.getString("username", ""));
            inputPassword.setText(loginPreferences.getString("password", ""));
            remember_me.setChecked(true);
        }


    }


    public void doSomethingElse() {
        inputName.setText(username+"");
        inputPassword.setText(password+"");
        /*startActivity(new Intent(LoginActivity.this, MainActivity.class));
        LoginActivity.this.finish();*/
    }
    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
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

            JSONObject json = jsonParser.makeHttpRequest(AppConfig.URL, "POST", params);
            return json;

        }

        protected void onPostExecute(JSONObject result) {
            try {
                if (result != null) {
                    Log.e("LOGINACTIVITY", "Successfully done!"+result.getString("message"));

                    username=inputName.getText().toString();
                    password=inputPassword.getText().toString();
                    Log.e("LoginActivity", "this is the username in the login activity "+username);

                    loginPrefsEditor.putString("username", username);
                    loginPrefsEditor.apply();
                    if (remember_me.isChecked()) {
                        loginPrefsEditor.putBoolean("saveLogin", true);
                        loginPrefsEditor.putString("password", password);
                        loginPrefsEditor.apply();
                    } else {
                        loginPrefsEditor.clear();
                        loginPrefsEditor.putString("username", username);
                        loginPrefsEditor.apply();
                    }
                    if (result.getString("message").equals("Successfully logged in")) {
                        if(status.equals("activate"))
                        {
                            Toast.makeText(getApplicationContext(), result.getString("message"), Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            startActivity(intent);
                        }
                        else if(status.equals("waiting"))
                        {
                            Toast.makeText(getApplicationContext(),
                                    "Your account has not yet been activated", Toast.LENGTH_LONG)
                                    .show();
                        }
                        else if(status.equals("suspended"))
                        {
                            Toast.makeText(getApplicationContext(),
                                    "Your account has been suspended!", Toast.LENGTH_LONG)
                                    .show();
                        }
                        else {

                        }

                       // doSomethingElse();

                    }


                } else {
                        InputMethodManager imm = (InputMethodManager) getSystemService(LoginActivity.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(inputName.getWindowToken(), 0);
                        // Prompt user to enter credentials
                        Toast.makeText(getApplicationContext(),
                                "Please enter the credentials!", Toast.LENGTH_LONG)
                                .show();
                }
                //Toast.makeText(getApplicationContext(), "Unable to retrieve any data from server", Toast.LENGTH_LONG).show();
            } catch (JSONException e) {
                e.printStackTrace();
            }


        }
    }
    private class UserDetails extends AsyncTask<String, String, String> {
        ProgressDialog pdLoading = new ProgressDialog(LoginActivity.this);
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

            pdLoading.dismiss();
            try {

                JSONArray jArray = new JSONArray(result);

                // Extract data from json and store into ArrayList as class objects
                for(int i=0;i<jArray.length();i++){
                    JSONObject json_data = jArray.getJSONObject(i);
                    //DataFish fishData = new DataFish();
                    status=json_data.getString("status");
                    Log.e("LoginActivity", "This is the status "+status);
                }

            } catch (JSONException e) {
                Toast.makeText(LoginActivity.this, e.toString(), Toast.LENGTH_LONG).show();
            }
            AttemptLogin attemptLogin= new AttemptLogin();
            attemptLogin.execute(inputName.getText().toString(),inputPassword.getText().toString(),"");
        }

    }


}





