package com.thebluealliance.androidclient.database;

import com.google.gson.Gson;

import android.content.ContentValues;

public interface TbaDatabaseModel {

    String getKey();
    Long getLastModified();
    void setLastModified(Long lastModified);
    ContentValues getParams(Gson gson);
}
