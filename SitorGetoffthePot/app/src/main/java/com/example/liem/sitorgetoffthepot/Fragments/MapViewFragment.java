package com.example.liem.sitorgetoffthepot.Fragments;

import android.Manifest;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.liem.sitorgetoffthepot.DataModels.LocationInfo;
import com.example.liem.sitorgetoffthepot.Interfaces.MapViewInterface;
import com.example.liem.sitorgetoffthepot.R;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.HashMap;

import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.SyncConfiguration;
import io.realm.SyncUser;

import static com.example.liem.sitorgetoffthepot.R.id.profile_menu_button;

public class MapViewFragment extends MapFragment implements OnMapReadyCallback,
        GoogleMap.InfoWindowAdapter,
        LocationListener,
        GoogleMap.OnMapLongClickListener,
        GoogleMap.OnInfoWindowClickListener {

    private static final String TAG = "MapViewFragment";

    private MapViewInterface mMapViewInterface;
    private static LocationManager mLocationManager;
    private static Location mLastKnownLocation;

    private static String mLocId;
    private HashMap<String, String> extraMarkerInfo = new HashMap<>();

    private Realm realm;

    private static GoogleMap mGoogleMap;


    @Override
    public void onActivityCreated(Bundle bundle) {
        super.onActivityCreated(bundle);
        getMapAsync(this);
    }

    public static MapViewFragment newInstance(LocationManager _locManager, Location _loc, String _locId) {

        Bundle args = new Bundle();

        mLocationManager = _locManager;
        mLastKnownLocation = _loc;

        mLocId = _locId;

        MapViewFragment fragment = new MapViewFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.map_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){

            case R.id.profile_menu_button:
                mMapViewInterface.profileButtonPress();
                break;

            case R.id.add_location_menu_button:
                mMapViewInterface.addNewLocationButtonPress();
                break;

            case R.id.list_view_menu_button:
                mMapViewInterface.listViewButtonPress();
                break;

        }

        return true;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if(context instanceof MapViewInterface){
            mMapViewInterface = (MapViewInterface) context;
        }
    }

    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    @Override
    public View getInfoWindow(Marker marker) {
        return null;
    }

    @Override
    public View getInfoContents(Marker marker) {

        View v = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            v = getLayoutInflater().inflate(R.layout.info_window, null);
        }

        TextView title_tv = v.findViewById(R.id.info_window_title);
        TextView snippet_tv = v.findViewById(R.id.info_window_snippet);

        title_tv.setText(marker.getTitle());
        snippet_tv.setText(marker.getSnippet());

        return v;
    }

    @Override
    public void onInfoWindowClick(Marker marker) {

        // Get extra data with marker ID
        String marker_data = extraMarkerInfo.get(marker.getId());
        mMapViewInterface.showDetail(marker_data);

    }

    @Override
    public void onMapLongClick(LatLng latLng) {

        if(mGoogleMap == null){
            return;
        }

        Log.i(TAG, "onMapLongClick: " + latLng.latitude + " --- " + latLng.longitude);
        mMapViewInterface.longPressAddLocation(latLng.latitude, latLng.longitude);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        //Note: setting up all the map stuff
        mGoogleMap = googleMap;
        mGoogleMap.setInfoWindowAdapter(this);
        mGoogleMap.setOnMapLongClickListener(this);
        mGoogleMap.setOnInfoWindowClickListener(this);

        mLocationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);

        // Note: getting the user locations
        if (ActivityCompat.checkSelfPermission(getActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(),
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        mLastKnownLocation = mLocationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        mLastKnownLocation = mLocationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

        mGoogleMap.setMyLocationEnabled(true);

        // Note: this is connecting to realm but at this moment it does not syn properly
//        if (SyncUser.current() != null) {
//            Realm.setDefaultConfiguration(SyncConfiguration.automatic());
//            realm = Realm.getDefaultInstance();
//
//            SyncConfiguration config = SyncConfiguration.automatic();
//            realm = Realm.getInstance(config);
//
//            RealmResults<LocationInfo> r = realm.where(LocationInfo.class).findAll();
//            Log.i(TAG, "onMapReady: " + r);
//
//            realm.executeTransactionAsync(realm -> {
//                RealmResults<LocationInfo> results = realm.where(LocationInfo.class).findAll();
//
//                if (results != null) {
//                    for (LocationInfo loc: results
//                            ) {
//                        MarkerOptions place = new MarkerOptions();
//
//                        place.title(loc.getTitle());
//                        place.snippet(loc.getRating());
//                        if (loc.getRating().equals("hover")) {
//                            place.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE));
//                        } else {
//                            place.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_CYAN));
//                        }
//
//                        LatLng officeLocation = new LatLng(loc.getLatitund(), loc.getLongitund());
//                        place.position(officeLocation);
//
//                        mGoogleMap.addMarker(place);
//
//                        Log.i(TAG, "onMapReady: " + loc.getTitle());
//                    }
//                }
//            });
//        }

        realm = Realm.getDefaultInstance();

        RealmResults<LocationInfo> results = realm.where(LocationInfo.class).findAll();

        // Note: adding default data when the app load
        if (results.size() < 1) {
            realm.beginTransaction();
            LocationInfo locationInfo = realm.createObject(LocationInfo.class);

            locationInfo.setTitle("Taco Bell");
            locationInfo.setDetail("Public");
            locationInfo.setRating("full contact");
            locationInfo.setLatitund(28.596755218393724);
            locationInfo.setLongitund(-81.30645968019962);
            locationInfo.setmUsername("The Great Liem");

            realm.commitTransaction();

            realm.beginTransaction();
            LocationInfo locationInfo1 = realm.createObject(LocationInfo.class);

            locationInfo1.setTitle("Gas station");
            locationInfo1.setDetail("Public, you might have to buy something");
            locationInfo1.setRating("hover");
            locationInfo1.setLatitund(28.596951861257573);
            locationInfo1.setLongitund(-81.30731262266637);
            locationInfo1.setmUsername("The Great Liem");

            realm.commitTransaction();
        }

        // Note: look for new locations.
        if (results.size() > 1) {
            for (LocationInfo loc: results
                 ) {
                MarkerOptions place = new MarkerOptions();

                place.title(loc.getTitle());
                place.snippet(loc.getRating());
                if (loc.getRating().equals("hover")) {
                    place.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE));
                } else {
                    place.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_CYAN));
                }

                LatLng officeLocation = new LatLng(loc.getLatitund(), loc.getLongitund());
                place.position(officeLocation);

                if ( mLocId != null && loc.getLocationId().equals(mLocId) ) {
                    Marker marker = mGoogleMap.addMarker(place);
                    extraMarkerInfo.put(marker.getId(),loc.getLocationId());
                    marker.showInfoWindow();
                } else {
                    Marker marker = mGoogleMap.addMarker(place);
                    extraMarkerInfo.put(marker.getId(),loc.getLocationId());
                }

                Log.i(TAG, "onMapReady: " + loc.getTitle());
            }

            if (mLocId == null){
                if (mLastKnownLocation != null) {
                    zoomIn();
                }
            }

        }



    }

    // Note: this method is used to zoom into the user current locations.
    private void zoomIn(){
        // Note: if the map is not available this kick the user back out
        if(mGoogleMap == null){
            return;
        }

        //Note: getting the user location and if they can't then you are running it again to get the user location.
        if(mLastKnownLocation != null) {
            LatLng currentLocation = new LatLng(mLastKnownLocation.getLatitude(),mLastKnownLocation.getLongitude());
            CameraUpdate cameraMovement = CameraUpdateFactory.newLatLngZoom(currentLocation, 16f);

            mGoogleMap.animateCamera(cameraMovement);
        } else {

            //Note: trying again to get the user locations.
            mLocationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);

            if (ActivityCompat.checkSelfPermission(getActivity(),
                    Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(),
                    Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }

            mLastKnownLocation = mLocationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            mLastKnownLocation = mLocationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

            LatLng currentLocation = new LatLng(mLastKnownLocation.getLatitude(),mLastKnownLocation.getLongitude());
            CameraUpdate cameraMovement = CameraUpdateFactory.newLatLngZoom(currentLocation, 16f);

            mGoogleMap.animateCamera(cameraMovement);
        }
    }






}

