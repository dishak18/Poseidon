package com.example.admin.litebulb;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;


public class PasscodeFragment extends Fragment {
    Activity referenceActivity;
    View parentHolder;
    EditText currentPass, newPass, reEnterPass;
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

        return parentHolder;
    }

    public interface OnFragmentInteractionListener {
    }
}