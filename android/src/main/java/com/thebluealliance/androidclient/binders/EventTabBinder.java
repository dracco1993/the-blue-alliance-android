package com.thebluealliance.androidclient.binders;

import android.support.annotation.Nullable;

import com.thebluealliance.androidclient.fragments.EventsByWeekFragment;
import com.thebluealliance.androidclient.models.EventWeekTab;

import java.util.List;

import butterknife.ButterKnife;

public class EventTabBinder extends AbstractDataBinder<List<EventWeekTab>> {

    private EventsByWeekFragment mFragment;
    private List<EventWeekTab> mTabs;

    public void setFragment(EventsByWeekFragment fragment) {
        mFragment = fragment;
    }

    @Override
    public void updateData(@Nullable List<EventWeekTab> data) {
        if (data != null && !data.equals(mTabs)) {
            mTabs = data;
            mFragment.updateLabels(mTabs);
        }
    }

    @Override
    public void onComplete() {

    }

    @Override
    public void bindViews() {
        ButterKnife.bind(this, mRootView);
    }

    @Override
    public void unbind() {
        super.unbind();
        ButterKnife.unbind(this);
    }

    @Override
    public void onError(Throwable throwable) {

    }
}