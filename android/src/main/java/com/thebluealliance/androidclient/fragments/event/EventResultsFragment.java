package com.thebluealliance.androidclient.fragments.event;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.thebluealliance.androidclient.R;
import com.thebluealliance.androidclient.background.PopulateEventResults;

/**
 * File created by phil on 4/22/14.
 */
public class EventResultsFragment extends Fragment {

    private String eventKey;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //TODO implement savedInstanceState and stuff
        if(getArguments() != null){
            eventKey = getArguments().getString("eventKey","");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View results = inflater.inflate(R.layout.fragment_event_results, null);
        new PopulateEventResults(getActivity(), results).execute(eventKey);
        return results;
    }
}
