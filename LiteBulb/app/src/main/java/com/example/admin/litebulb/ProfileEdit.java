package com.example.admin.litebulb;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;


public class ProfileEdit extends Fragment {
    Activity referenceActivity;
    View parentHolder;
    EditText firstName, lastName, email, companyName, livesIn;
    Switch login_with_fb, available_for_free, item_update, daily_summary;
    Spinner country;
    Button save;
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
        country=(Spinner) parentHolder.findViewById(R.id.country);
        save=(Button) parentHolder.findViewById(R.id.save);


        return parentHolder;
    }

    public interface OnFragmentInteractionListener {
    }
}