package com.example.liem.sitorgetoffthepot.Actvities;

import android.content.Intent;
import android.location.Location;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.example.liem.sitorgetoffthepot.DataModels.Constants;
import com.example.liem.sitorgetoffthepot.DataModels.LocationInfo;
import com.example.liem.sitorgetoffthepot.Fragments.DetailViewFragment;
import com.example.liem.sitorgetoffthepot.Fragments.ListViewLocFragment;
import com.example.liem.sitorgetoffthepot.Fragments.LoginFragment;
import com.example.liem.sitorgetoffthepot.Fragments.NewLocationFormFragment;
import com.example.liem.sitorgetoffthepot.Fragments.RegFragment;
import com.example.liem.sitorgetoffthepot.Interfaces.AddLocationInterface;
import com.example.liem.sitorgetoffthepot.Interfaces.LocListViewInterface;
import com.example.liem.sitorgetoffthepot.Interfaces.LoginRegInterface;
import com.example.liem.sitorgetoffthepot.Interfaces.RegInterface;
import com.example.liem.sitorgetoffthepot.R;
import com.google.android.gms.maps.model.LatLng;

import java.util.Objects;

import io.realm.Realm;
import io.realm.RealmObject;
import io.realm.SyncConfiguration;
import io.realm.SyncUser;

public class FunctionalActivity extends AppCompatActivity implements LoginRegInterface, RegInterface, AddLocationInterface, LocListViewInterface {

    private static final String TAG = "FunctionalActivity";

    private LatLng mLocation;
    private Realm realm;
    private String mUsername;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_functional);

        realm = Realm.getDefaultInstance();

        Intent i = getIntent();

        if (Objects.equals(i.getAction(), Constants.ACTION_LOGIN)){

            settingUpLoginFragment();

        } else if (Objects.equals(i.getAction(), Constants.ACTION_ADD_NEW_LOCATION)){

            double lon = i.getDoubleExtra(Constants.EXTRA_LONG, 0);
            double lat = i.getDoubleExtra(Constants.EXTRA_LAT, 0);
            mUsername = i.getStringExtra(Constants.EXTRA_USERNAME);
            mLocation = new LatLng(lat, lon);
            Log.i(TAG, "onCreate: New loc = " + "lat: " + lat + " --- " + "long: " + lon + " --- " + "Username: " + mUsername);

            settingUpNewLocationFragment();

        } else if (Objects.equals(i.getAction(), Constants.ACTION_LIST_VIEW)) {

            settingUpListViewFragment();

        } else if (Objects.equals(i.getAction(), Constants.ACTION_DETAIL)) {

            String id = i.getStringExtra(Constants.EXTRA_LOCATION_ID);
            mUsername = i.getStringExtra(Constants.EXTRA_USERNAME);

            settingUpDetailFragment(id);
        }

    }

    //Note: These are the Fragment layouts methods
    public void settingUpLoginFragment() {
        LoginFragment fragment = LoginFragment.newInstance();
        getSupportFragmentManager().beginTransaction().replace(R.id.functional_frame, fragment).commit();
    }

    public void settingUpRegFragment() {
        RegFragment fragment = RegFragment.newInstance();
        getSupportFragmentManager().beginTransaction().replace(R.id.functional_frame, fragment).commit();
    }

    public void settingUpListViewFragment() {
        ListViewLocFragment fragment = ListViewLocFragment.newInstance();
        getSupportFragmentManager().beginTransaction().replace(R.id.functional_frame, fragment).commit();
    }

    public void settingUpNewLocationFragment() {
        NewLocationFormFragment fragment = NewLocationFormFragment.newInstance();
        getSupportFragmentManager().beginTransaction().replace(R.id.functional_frame, fragment).commit();
    }

    public void settingUpDetailFragment(String id) {
        DetailViewFragment fragment = DetailViewFragment.newInstance(id);
        getSupportFragmentManager().beginTransaction().replace(R.id.functional_frame, fragment).commit();
    }

    // Note: These are the interfaces methods
    @Override
    public void loginCancelButtonPress() {
        finish();
    }

    @Override
    public void loginRegButtonPress() {
        settingUpRegFragment();
    }

    @Override
    public void regCancelButtonPress() {
        finish();
    }

    @Override
    public void newLocationCancelButtonPress() {
        finish();
    }

    @Override
    public void newLocationAddButtonPress(final String _title, final String _rating, final String _detail) {
        Log.i(TAG, "newLocationAddButtonPress: Title: " + _title + " -- Rating: " + _rating + " -- Detail: " + _detail);

        //Note: add new location to the DB
        realm.beginTransaction();
        LocationInfo locationInfo = realm.createObject(LocationInfo.class);

        locationInfo.setTitle(_title);
        locationInfo.setDetail(_detail);
        locationInfo.setRating(_rating);
        locationInfo.setLatitund(mLocation.latitude);
        locationInfo.setLongitund(mLocation.longitude);
        locationInfo.setmUsername(mUsername);

        realm.commitTransaction();

        // Note: this is connecting to realm but at this moment it does not syn properly
//        Realm.setDefaultConfiguration(SyncConfiguration.automatic());
//        realm = Realm.getDefaultInstance();
//
//        realm.executeTransactionAsync(realm -> {
//            LocationInfo locationInfo = realm.createObject(LocationInfo.class);
//
//            locationInfo.setTitle(_title);
//            locationInfo.setDetail(_detail);
//            locationInfo.setRating(_rating);
//            locationInfo.setLatitund(mLocation.latitude);
//            locationInfo.setLongitund(mLocation.longitude);
//            locationInfo.setmUsername(mUsername);
//
//            realm.insert(locationInfo);
//        });

        finish();
    }

    @Override
    public void loginButtonPress(String username) {
        Log.i( TAG, "loginButtonPress: " + username );
        Intent i = new Intent();
        i.putExtra(Constants.EXTRA_USERNAME, username);
        setResult(RESULT_OK, i);
        finish();
    }

    @Override
    public void regButtonPress(String username) {
        Log.i(TAG, "regButtonPress: " + username);
        Intent i = new Intent();
        i.putExtra(Constants.EXTRA_USERNAME, username);
        setResult(RESULT_OK, i);
        finish();
    }

    @Override
    public void locationClicked(String id) {
        Intent i = new Intent();
        i.putExtra(Constants.EXTRA_LOCATION_ID, id);
        setResult(RESULT_OK, i);
        finish();
    }

    @Override
    public void mapButtonClick() {
        Intent i = new Intent();
        setResult(RESULT_OK, i);
        finish();
        finish();
    }

}
