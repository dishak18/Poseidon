package com.example.admin.litebulb;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


public class SocialProfileFragment extends Fragment {
    Activity referenceActivity;
    View parentHolder;
    TextView trash1, trash2, trash3;
    EditText mediaName, mediaName2, mediaName3;
    EditText profileLink1, profileLink2, profileLink3;
    Button save;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        referenceActivity = getActivity();
        parentHolder = inflater.inflate(R.layout.fragment_social_profile, container,
                false);
        trash1=(TextView) parentHolder.findViewById(R.id.trash1);
        trash2=(TextView) parentHolder.findViewById(R.id.trash2);
        trash3=(TextView) parentHolder.findViewById(R.id.trash3);

        mediaName=(EditText) parentHolder.findViewById(R.id.mediaName);
        mediaName2=(EditText) parentHolder.findViewById(R.id.mediaName2);
        mediaName3=(EditText) parentHolder.findViewById(R.id.mediaName3);

        profileLink1=(EditText) parentHolder.findViewById(R.id.profileLink);
        profileLink2=(EditText) parentHolder.findViewById(R.id.profileLink2);
        profileLink3=(EditText) parentHolder.findViewById(R.id.profileLink3);

        save=(Button) parentHolder.findViewById(R.id.save);

        trash1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mediaName.setText("");
                profileLink1.setText("");
            }
        });
        trash2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mediaName2.setText("");
                profileLink2.setText("");
            }
        });
        trash3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mediaName3.setText("");
                profileLink3.setText("");
            }
        });
        return parentHolder;
    }

    public interface OnFragmentInteractionListener {
    }
}