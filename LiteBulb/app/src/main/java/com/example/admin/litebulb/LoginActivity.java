package com.example.admin.litebulb;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class LoginActivity extends AppCompatActivity {
    public EditText user;
    public EditText pass;
   // public String userid,password;
    Button login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        user = (EditText) findViewById(R.id.name);
        pass=(EditText) findViewById(R.id.pass);
        login=(Button) findViewById(R.id.log);







    }
}
