package com.thebluealliance.androidclient.notifications;

import android.app.Notification;
import android.content.Context;
import android.content.Intent;

import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.thebluealliance.androidclient.R;
import com.thebluealliance.androidclient.activities.ViewEventActivity;
import com.thebluealliance.androidclient.adapters.ViewEventFragmentPagerAdapter;
import com.thebluealliance.androidclient.database.writers.AwardListWriter;
import com.thebluealliance.androidclient.datafeed.framework.ModelMaker;
import com.thebluealliance.androidclient.gcm.notifications.AwardsPostedNotification;
import com.thebluealliance.androidclient.gcm.notifications.NotificationTypes;
import com.thebluealliance.androidclient.helpers.MyTBAHelper;
import com.thebluealliance.androidclient.models.Award;
import com.thebluealliance.androidclient.models.StoredNotification;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.RETURNS_DEEP_STUBS;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(RobolectricTestRunner.class)
@Config(manifest = Config.NONE)
public class AwardsPostedNotificationTest {

    @Mock private Context mContext;
    @Mock private AwardListWriter mWriter;
    private AwardsPostedNotification mNotification;
    private JsonObject mData;

    @Before
    public void setUp() {
        mContext = mock(Context.class, RETURNS_DEEP_STUBS);
        mWriter = mock(AwardListWriter.class);
        mData = ModelMaker.getModel(JsonObject.class, "notification_awards_posted");
        mNotification = new AwardsPostedNotification(mData.toString(), mWriter);
    }

    @Test
    public void testParseData() {
        mNotification.parseMessageData();

        assertEquals(mNotification.getEventKey(), "2014necmp");
        assertEquals(mNotification.getEventName(), "New England FRC Region Championship");

        List<Award> awards = mNotification.getAwards();
        assertNotNull(awards);
        assertEquals(awards.size(), 1);
    }

    @Test
    public void testDbWrite() {
        mNotification.parseMessageData();
        mNotification.updateDataLocally();

        List<Award> awards = mNotification.getAwards();
        verify(mWriter).write(awards);
    }

    @Test(expected = JsonParseException.class)
    public void testNoEventKey() {
        mData.remove("event_key");
        mNotification = new AwardsPostedNotification(mData.toString(), mWriter);
        mNotification.parseMessageData();
    }

    @Test(expected = JsonParseException.class)
    public void testNoEventName() {
        mData.remove("event_name");
        mNotification = new AwardsPostedNotification(mData.toString(), mWriter);
        mNotification.parseMessageData();
    }

    @Test(expected = JsonParseException.class)
    public void testNoAwards() {
        mData.remove("awards");
        mNotification = new AwardsPostedNotification(mData.toString(), mWriter);
        mNotification.parseMessageData();
    }

    @Test
    public void testBuildNotification() {
        mNotification.parseMessageData();
        when(mContext.getString(R.string.notification_awards_updated_title, "NECMP"))
          .thenReturn("Event Awards Updated NECMP");
        when(mContext.getString(R.string.notification_awards_updated, "New England"))
          .thenReturn("Awards have been updated at New England");
        Notification notification = mNotification.buildNotification(mContext);
        assertNotNull(notification);

        StoredNotification stored = mNotification.getStoredNotification();
        assertEquals(stored.getType(), NotificationTypes.AWARDS);
        assertEquals(stored.getTitle(), "Event Awards Updated NECMP");
        assertEquals(stored.getBody(), "Awards have been updated at New England");
        assertEquals(stored.getMessageData(), mData.toString());
        assertEquals(stored.getIntent(), MyTBAHelper.serializeIntent(mNotification.getIntent(mContext)));
        assertNotNull(stored.getTime());
    }

    @Test
    public void testGetIntent() {
        mNotification.parseMessageData();
        Intent intent = mNotification.getIntent(mContext);
        assertNotNull(intent);
        assertEquals(intent.getComponent().getClassName(), "com.thebluealliance.androidclient.activities.ViewEventActivity");
        assertEquals(intent.getStringExtra(ViewEventActivity.EVENTKEY), mNotification.getEventKey());
        assertEquals(intent.getIntExtra(ViewEventActivity.TAB, -1), ViewEventFragmentPagerAdapter.TAB_AWARDS);
    }
}