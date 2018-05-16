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
import com.facebook.FacebookSdk;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

//import com.example.admin.litebulb.SystemBarTintManager;

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
    private SQLiteHandler db;
    JSONParser jsonParser=new JSONParser();
    CheckBox remember_me;
    private ImageButton back_btn;
    private Boolean saveLogin;

    /*    private SignInButton googleButton;
        private GoogleApiClient mGoogleApiClient;


        private static final int RC_SIGN_IN = 9001;

        //GOOGLE SIGN IN
        private FirebaseAuth mAuth;
        private GoogleSignInClient mGoogleSignInClient;
        private ProgressDialog mProgress;
        private String googleName;
        private String googleEmail;

        //FACEBOOK SIGN IN
        private static final String TAG = "FacebookLogin";
        private CallbackManager mCallbackManager;
        private LoginButton loginButton;*/
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_login);
        remember_me=(CheckBox) findViewById(R.id.rem);
        inputName = (EditText) findViewById(R.id.name);
        inputPassword = (EditText) findViewById(R.id.pass);
        btnLogin = (Button) findViewById(R.id.log);
        btnLinkToRegister = (Button) findViewById(R.id.register);
        back_btn=(ImageButton) findViewById(R.id.back);

       /* googleButton = (SignInButton) findViewById(R.id.google_sign_in);
        username=inputName.getText().toString().trim();
        loginButton = (LoginButton) findViewById(R.id.facebook_login);

        mCallbackManager = CallbackManager.Factory.create();
        loginButton.setReadPermissions("email", "public_profile");
        loginButton.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.d(TAG, "facebook:onSuccess:" + loginResult);
                handleFacebookAccessToken(loginResult.getAccessToken());
                Toast.makeText(LoginActivity.this, ""+loginResult.getAccessToken().getUserId(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancel() {
                Log.d(TAG, "facebook:onCancel");
            }

            @Override
            public void onError(FacebookException error) {
                Log.d(TAG, "facebook:onError", error);
            }
        });
*/
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Log.e("LoginActivity", "This is the name "+inputName.getText().toString()+"     This is the password "+inputPassword.getText().toString().trim());
                AttemptLogin attemptLogin= new AttemptLogin();
                attemptLogin.execute(inputName.getText().toString(),inputPassword.getText().toString(),"");
            }
        });

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

/*        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        mAuth = FirebaseAuth.getInstance();

        mProgress = new ProgressDialog(this);
        mProgress.setTitle("Please Wait...");

        googleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signIn();
            }
        });*/

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
        /*if (session.isLoggedIn()) {
            // User is already logged in. Take him to main activity
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }*/

        // Login button Click Event
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e("LoginActivity", "This is the name "+inputName.getText().toString()+"     This is the password "+inputPassword.getText().toString().trim());

                AttemptLogin attemptLogin= new AttemptLogin();
                attemptLogin.execute(inputName.getText().toString().trim(),inputPassword.getText().toString().trim(),"");
            }
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
        loginPrefsEditor.clear();
        loginPrefsEditor.commit();

        saveLogin = loginPreferences.getBoolean("saveLogin", false);
       /* if (saveLogin == true) {
            inputName.setText(loginPreferences.getString("username", ""));
            inputPassword.setText(loginPreferences.getString("password", ""));
            remember_me.setChecked(true);
        }*/


    }

   /* private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mProgress.show();
        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        try {
            if (requestCode == RC_SIGN_IN) {
                Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
                try {
                    // Google Sign In was successful, authenticate with Firebase
                    GoogleSignInAccount account = task.getResult(ApiException.class);
                    firebaseAuthWithGoogle(account);
                } catch (ApiException e) {
                    // Google Sign In failed, update UI appropriately
                    Log.w(TAG, "Google sign in failed", e);

                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        try{
            mCallbackManager.onActivityResult(requestCode, resultCode, data);
        }catch (Exception e){
            e.printStackTrace();
        }
        mProgress.dismiss();
    }

    private void firebaseAuthWithGoogle(final GoogleSignInAccount acct) {
        Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId());
        // [START_EXCLUDE silent]
        mProgress.show();
        // [END_EXCLUDE]

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            googleEmail = acct.getEmail();
                            googleName = acct.getDisplayName();
                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            startActivity(intent);
                            Log.e("LoginActivity","Email : "+googleEmail+"Name : " + googleName);

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            Toast.makeText(LoginActivity.this, "Authentication failed!", Toast.LENGTH_SHORT).show();

                        }

                        // [START_EXCLUDE]
                        mProgress.dismiss();
                        // [END_EXCLUDE]
                    }
                });
    }
    private void handleFacebookAccessToken(final AccessToken token) {
        Log.d(TAG, "handleFacebookAccessToken:" + token);
        // [START_EXCLUDE silent]
        mProgress.show();
        // [END_EXCLUDE]

        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            Log.e("LoginActivity","token"+token);

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            Toast.makeText(LoginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();

                        }

                        // [START_EXCLUDE]
                        mProgress.dismiss();
                        // [END_EXCLUDE]
                    }
                });
    }*/


    public void doSomethingElse() {
        inputName.setText(username+"");
        inputPassword.setText(password+"");
        /*startActivity(new Intent(LoginActivity.this, MainActivity.class));
        LoginActivity.this.finish();*/
    }


    /**
     * function to verify login details in mysql db
     * */
   /* private void checkLogin(final String username, final String password) {
        // Tag used to cancel the request
        String tag_string_req = "req_login";

        pDialog.setMessage("Logging in ...");
        showDialog();

        StringRequest strReq = new StringRequest(Method.POST, AppConfig.URL_LOGIN, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("Login", "Login Response: " + response.toString());
                Log.e("LOGIN ACTIVITY", username+"");
                hideDialog();

                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");

                    // Check for error node in json
                    if (!error) {
                        // user successfully logged in
                        // Create login session
                        session.setLogin(true);

                        // Now store the user in SQLite
                        String uid = jObj.getString("user_id");

                        JSONObject user = jObj.getJSONObject("user");
                        String username = user.getString("username");
                        String email = user.getString("email");
                        String created_at = user
                                .getString("register_datetime");

                        // Inserting row in users table
                        db.addUser(username, email, uid, created_at);

                        // Launch main activity
                        Intent intent = new Intent(LoginActivity.this,
                                MainActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        // Error in login. Get the error message
                        String errorMsg = jObj.getString("error_msg");
                        Toast.makeText(getApplicationContext(),
                                errorMsg, Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    // JSON error
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Json error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                }

            }
        }
        , new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("LOGINACTIVITY","Login Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();
                hideDialog();
            }
        })
        {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<String, String>();
                params.put("username", username);
                params.put("password", password);

                return params;
            }

        };
*/
/*        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }*/

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
                    Toast.makeText(getApplicationContext(), result.getString("message"), Toast.LENGTH_LONG).show();
                    Log.e("LoginActivity", " this is the username ");



                    if (result.getString("message").equals("Successfully logged in")) {
                        loginPrefsEditor.clear();
                        loginPrefsEditor.commit();
                        loginPrefsEditor.putBoolean("saveLogin", true);
                        loginPrefsEditor.putString("username", inputName.getText().toString().trim());
                        Log.e("LoginActivity", "this hahahahhahhaha "+loginPreferences.getString("username", ""));

                        loginPrefsEditor.putString("password", password);
                        loginPrefsEditor.commit();
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(intent);
                        //doSomethingElse();

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

}





