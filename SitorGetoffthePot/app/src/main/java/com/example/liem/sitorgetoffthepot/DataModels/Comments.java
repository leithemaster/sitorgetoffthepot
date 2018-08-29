package com.example.liem.sitorgetoffthepot.DataModels;

import java.util.Date;

import io.realm.RealmObject;

public class Comments extends RealmObject{

    public String mUsername;
    public String mComment;
    public Date mTimeStamp;

    public String getmUsername() {
        return mUsername;
    }

    public void setmUsername(String mUsername) {
        this.mUsername = mUsername;
    }

    public String getmComment() {
        return mComment;
    }

    public void setmComment(String mComment) {
        this.mComment = mComment;
    }

    public Date getmTimeStamp() {
        return mTimeStamp;
    }

    public void setmTimeStamp(Date mTimeStamp) {
        this.mTimeStamp = mTimeStamp;
    }



}
