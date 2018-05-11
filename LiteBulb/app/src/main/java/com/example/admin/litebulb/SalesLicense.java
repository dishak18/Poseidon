package com.example.admin.litebulb;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Switch;

import static android.content.Context.MODE_PRIVATE;


public class SalesLicense extends Fragment {
    Activity referenceActivity;
    View parentHolder;
    SharedPreferences loginPreferences;
    String username;
    String PremiumLicense, FullLicense;
    Switch fullLicense, personalUseLicense;
    Button save;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        referenceActivity = getActivity();
        parentHolder = inflater.inflate(R.layout.fragment_sales_license, container,
                false);
        fullLicense=(Switch) parentHolder.findViewById(R.id.full_license);
        personalUseLicense=(Switch) parentHolder.findViewById(R.id.personal_use_license);

        save=(Button) parentHolder.findViewById(R.id.save);

        loginPreferences = getActivity().getSharedPreferences("loginPrefs", MODE_PRIVATE);
        username=loginPreferences.getString("username", "");
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                /*GetData();

                InsertData(FirstName, Email, LastName, LivesIn, CompanyName, Daily, FreeLance, ItemNote);
*/
            }
        });


        return parentHolder;
    }
    public void GetData(){

        if(fullLicense.isChecked())
        {

            FullLicense="true";
            //Log.e("LALALALA", "This is the value of the string freelance "+fullLicense);
        }
        else
        {
            FullLicense="false";
            //Log.e("LALALALA", "This is the value of the string freelance "+fullLicense);
        }
        if(personalUseLicense.isChecked())
        {
            PremiumLicense="true";
        }
        else
        {
            PremiumLicense="false";
        }


    }

    public interface OnFragmentInteractionListener {
    }
}