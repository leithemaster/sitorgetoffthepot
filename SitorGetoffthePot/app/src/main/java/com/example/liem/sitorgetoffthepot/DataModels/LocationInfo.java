package com.example.liem.sitorgetoffthepot.DataModels;

import org.w3c.dom.Comment;

import java.util.UUID;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.Ignore;
import io.realm.annotations.Index;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;

public class LocationInfo extends RealmObject {

    @Required
    private String mLocationId;
    @Required
    private String mTitle;
    @Ignore
    private String mDetail;
    @Required
    private String mRating;
    @Required
    private Double mLongitund;
    @Required
    private Double mLatitund;
    @Required
    private String mUsername;
    @Ignore
    private RealmList<Comments> mComments;

    public LocationInfo(String mTitle, String mDetail, String mRating, double mLongitund, double mLatitund, String mUsername) {

        this.mLocationId = UUID.randomUUID().toString();
        this.mTitle = mTitle;
        this.mDetail = mDetail;
        this.mRating = mRating;
        this.mLongitund = mLongitund;
        this.mLatitund = mLatitund;
        this.mUsername = mUsername;
    }

    public LocationInfo() {

        this.mLocationId = UUID.randomUUID().toString();
        this.mTitle = "Test Location";
        this.mDetail = "Testing Details";
        this.mRating = "Hover";
        this.mLongitund = 28.5990647;
        this.mLatitund = -81.3045101;
        this.mUsername = "TestingDummy1";
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String mTitle) {
        this.mTitle = mTitle;
    }

    public String getDetail() {
        return mDetail;
    }

    public void setDetail(String mDetail) {
        this.mDetail = mDetail;
    }

    public String getRating() {
        return mRating;
    }

    public void setRating(String mRating) {
        this.mRating = mRating;
    }

    public double getLongitund() {
        return mLongitund;
    }

    public void setLongitund(double mLongitund) {
        this.mLongitund = mLongitund;
    }

    public double getLatitund() {
        return mLatitund;
    }

    public void setLatitund(double mLatitund) {
        this.mLatitund = mLatitund;
    }

    public String getUsername() {
        return mUsername;
    }

    public void setmUsername(String mUser) {
        this.mUsername = mUser;
    }

    public String getLocationId() {
        return mLocationId;
    }

    public void setLocationId(String mLocationId) {
        this.mLocationId = mLocationId;
    }

    public RealmList<Comments> getComments() {
        return mComments;
    }

    public void setComments(RealmList<Comments> mComments) {
        this.mComments = mComments;
    }

    public void addComment(Comments comments) {
        this.mComments.add(comments);
    }


}
