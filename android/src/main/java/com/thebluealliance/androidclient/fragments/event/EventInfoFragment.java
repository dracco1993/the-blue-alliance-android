package com.thebluealliance.androidclient.fragments.event;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.thebluealliance.androidclient.R;
import com.thebluealliance.androidclient.background.PopulateEventInfo;

/**
 * File created by phil on 4/22/14.
 */
public class EventInfoFragment extends Fragment {

    private String eventKey;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getArguments() != null){
            eventKey = getArguments().getString("eventKey");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View info = inflater.inflate(R.layout.fragment_event_info, null);
        new PopulateEventInfo(getActivity(),info).execute(eventKey);
        return info;
    }
}
