package com.example.admin.litebulb;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Switch;


public class SalesLicense extends Fragment {
    Activity referenceActivity;
    View parentHolder;
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
        return parentHolder;
    }

    public interface OnFragmentInteractionListener {
    }
}