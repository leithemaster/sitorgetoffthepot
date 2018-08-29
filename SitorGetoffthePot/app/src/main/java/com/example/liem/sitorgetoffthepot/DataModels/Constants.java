package com.example.liem.sitorgetoffthepot.DataModels;

public class Constants {

    //Note: Realm Stuff
    public static final String INSTANCE_ADDRESS = "project6.us1.cloud.realm.io";
    public static final String SERVER_URL = "realms://" + INSTANCE_ADDRESS + "/~/locations";
    public static final String AUTH_URL = "https://" + INSTANCE_ADDRESS + "/auth";
    public static final String LOCATION_URL = "https://" + INSTANCE_ADDRESS + "/locations";
    public static final int SCHEMA_VERSION = 1;

    // Note: Actions
    // public static final String ACTION_
    public static final String ACTION_LOGIN = "ACTION_LOGIN";
    public static final String ACTION_LIST_VIEW = "ACTION_LIST_VIEW";
    public static final String ACTION_ADD_NEW_LOCATION = "ACTION_ADD_NEW_LOCATION";
    public static final String ACTION_DETAIL = "ACTION_DETAIL";

    // Note: Request Codes
    // public static final int _REQUEST_CODE
    public static final int LOGIN_REQUEST_CODE = 100;
    public static final int LIST_VIEW_REQUEST_CODE = 200;
    public static final int ADD_NEW_LOCATION_REQUEST_CODE = 300;
    public static final int DETAIL_REQUEST_CODE = 400;
    public static final int REGISTER_REQUEST_CODE = 500;

    //Note: Extras
    // public static final String EXTRA_
    public static final String EXTRA_USERNAME = "EXTRA_USERNAME";
    public static final String EXTRA_LONG = "EXTRA_LONG";
    public static final String EXTRA_LAT = "EXTRA_LAT";
    public static final String EXTRA_TITLE = "EXTRA_TITLE";
    public static final String EXTRA_DETAIL = "EXTRA_DETAIL";
    public static final String EXTRA_RATING = "EXTRA_RATING";
    public static final String EXTRA_LOCATION_ID = "EXTRA_LOCATION_ID";

}
