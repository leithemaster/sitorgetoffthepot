package com.example.liem.sitorgetoffthepot.Actvities;

import android.Manifest;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.liem.sitorgetoffthepot.DataModels.Constants;
import com.example.liem.sitorgetoffthepot.Fragments.MapViewFragment;
import com.example.liem.sitorgetoffthepot.Interfaces.MapViewInterface;
import com.example.liem.sitorgetoffthepot.R;
import com.google.android.gms.maps.model.Marker;

import java.util.Map;

import io.realm.ObjectServerError;
import io.realm.Realm;
import io.realm.SyncCredentials;
import io.realm.SyncUser;

public class HomeActivity extends AppCompatActivity implements MapViewInterface {

    private static final String TAG = "HomeActivity";

    private Realm realm;

    MapViewFragment fragment;

    private LocationManager mLocationManager;
    private Location mLastKnownLocation;
    private String mUsername;
    public static final int REQUEST_LOCATION_PERMISSION = 0x01000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        //Note: logged out multiple users.
        //Note: logged out multiple users.

        Map<String, SyncUser> users = SyncUser.all();
        for (Map.Entry < String, SyncUser > user: users.entrySet()) {
            SyncUser temp = user.getValue();
            temp.logOut();
        }
        Log.i(TAG, "onCreate: " + users);

        //Note: default the user to anonymous account with read only(in theory)
        mUsername = "";

        if (SyncUser.current() == null) {
            SyncCredentials credentials = SyncCredentials.anonymous();
            Log.i(TAG, "onCreate: 1 =" + mUsername);
            SyncUser.logInAsync(credentials, Constants.AUTH_URL, new SyncUser.Callback<SyncUser>() {
                @Override
                public void onSuccess(SyncUser user) {
                    mUsername = "";
                    Log.i(TAG, "onSuccess: username: 2 =" + mUsername);
                }

                @Override
                public void onError(ObjectServerError error) {
                    Log.e("Login error", error.toString());
                }
            });
        } else {
            mUsername = SyncUser.current().getIdentity();
            Log.i(TAG, "onCreate: 3 =" + mUsername);
        }

        getLocation();

        settingUpMapFragment(mLocationManager, mLastKnownLocation, null);
    }

    public void settingUpMapFragment(LocationManager _locManager, Location _loc, String _locId) {
        if (fragment == null) {
            fragment = MapViewFragment.newInstance(_locManager, _loc, _locId);
            getFragmentManager().beginTransaction().replace(R.id.mapframe, fragment, "try").commit();
        } else {
            getFragmentManager().beginTransaction().remove(fragment).commit();

            fragment = MapViewFragment.newInstance(_locManager, _loc, _locId);
            getFragmentManager().beginTransaction().replace(R.id.mapframe, fragment, "try").commit();
        }
    }

//    public void settingUpMapFragment(LocationManager _locManager, Location _loc, String _locId) {
//        MapViewFragment fragment = MapViewFragment.newInstance(_locManager, _loc, _locId);
//        getFragmentManager().beginTransaction().replace(R.id.mapframe, fragment, "try").commit();
//    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        Map<String, SyncUser> users = SyncUser.all();
        for (Map.Entry < String, SyncUser > user: users.entrySet()) {
            SyncUser temp = user.getValue();
            temp.logOut();
        }
        realm.close();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && requestCode == Constants.LOGIN_REQUEST_CODE){
            mUsername = data.getStringExtra(Constants.EXTRA_USERNAME);
            Log.i(TAG, "onActivityResult: " + mUsername);
        } else if (resultCode == RESULT_OK && requestCode == Constants.DETAIL_REQUEST_CODE) {
            getLocation();
//            settingUpMapFragment(mLocationManager, mLastKnownLocation, null);
            // Reload current fragment
            Fragment currentFragment = getFragmentManager().findFragmentById(R.id.mapframe);
            if (currentFragment instanceof MapViewFragment) {
                FragmentTransaction fragTransaction =   getFragmentManager().beginTransaction();
                fragTransaction.detach(currentFragment);
                fragTransaction.attach(currentFragment);
                fragTransaction.commit();}

        } else if (resultCode == RESULT_OK && requestCode == Constants.ADD_NEW_LOCATION_REQUEST_CODE) {
            String id = data.getStringExtra(Constants.EXTRA_LOCATION_ID);
//            settingUpMapFragment(mLocationManager, mLastKnownLocation, id);
            // Reload current fragment
            Fragment currentFragment = getFragmentManager().findFragmentById(R.id.mapframe);
            if (currentFragment instanceof MapViewFragment) {
                FragmentTransaction fragTransaction =   getFragmentManager().beginTransaction();
                fragTransaction.detach(currentFragment);
                fragTransaction.attach(currentFragment);
                fragTransaction.commit();}
        } else if (resultCode == RESULT_OK && requestCode == Constants.LIST_VIEW_REQUEST_CODE){

        }



    }

    private void getLocation() {
        mLocationManager = (LocationManager) this.getSystemService(LOCATION_SERVICE);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_DENIED) {

            mLastKnownLocation = mLocationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            mLastKnownLocation = mLocationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

            if (mLastKnownLocation == null) {
                mLastKnownLocation = mLocationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                mLastKnownLocation = mLocationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            }

        } else {

            while (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_DENIED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION_PERMISSION);
            }
        }
    }

    @Override
    public void profileButtonPress() {
        if (mUsername.equals("")) {

            Intent i = new Intent(this, FunctionalActivity.class);
            i.setAction(Constants.ACTION_LOGIN);
            startActivityForResult(i,Constants.LOGIN_REQUEST_CODE);

        } else {

            SyncUser syncUser = SyncUser.current();
            if (syncUser != null) {
                syncUser.logOut();
                Toast.makeText(HomeActivity.this, "You are now logged out.", Toast.LENGTH_LONG).show();

                if (SyncUser.current() == null) {
                    SyncCredentials credentials = SyncCredentials.anonymous();
                    SyncUser.logInAsync(credentials, Constants.AUTH_URL, new SyncUser.Callback<SyncUser>() {
                        @Override
                        public void onSuccess(SyncUser user) {
                            mUsername = "";
                            Log.i(TAG, "onSuccess: username: " + mUsername);
                        }

                        @Override
                        public void onError(ObjectServerError error) {
                            Log.e("Login error", error.toString());
                        }
                    });
                }

            }
        }
    }

    @Override
    public void listViewButtonPress() {
        Intent i = new Intent(this, FunctionalActivity.class);
        i.setAction(Constants.ACTION_LIST_VIEW);
        startActivityForResult(i,Constants.LIST_VIEW_REQUEST_CODE);
    }

    @Override
    public void addNewLocationButtonPress() {
        if (!mUsername.equals("")) {
            getLocation();
            Intent i = new Intent(this, FunctionalActivity.class);
            i.setAction(Constants.ACTION_ADD_NEW_LOCATION);
            i.putExtra(Constants.EXTRA_LONG, mLastKnownLocation.getLongitude());
            i.putExtra(Constants.EXTRA_LAT, mLastKnownLocation.getLatitude());
            i.putExtra(Constants.EXTRA_USERNAME, mUsername);
            startActivityForResult(i,Constants.ADD_NEW_LOCATION_REQUEST_CODE);
        } else {
            Toast.makeText(HomeActivity.this, "You need log in to add a location.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void longPressAddLocation(double lat, double lon) {
        if (!mUsername.equals("")) {
            Intent i = new Intent(this, FunctionalActivity.class);
            i.setAction(Constants.ACTION_ADD_NEW_LOCATION);
            i.putExtra(Constants.EXTRA_LAT, lat);
            i.putExtra(Constants.EXTRA_LONG, lon);
            i.putExtra(Constants.EXTRA_USERNAME, mUsername);
            startActivityForResult(i,Constants.ADD_NEW_LOCATION_REQUEST_CODE);
        } else {
            Toast.makeText(HomeActivity.this, "You need log in to add a location.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void showDetail(String id) {
        Intent i = new Intent(this, FunctionalActivity.class);
        i.setAction(Constants.ACTION_DETAIL);
        i.putExtra(Constants.EXTRA_LOCATION_ID, id);
        startActivityForResult(i,Constants.DETAIL_REQUEST_CODE);
    }


}
