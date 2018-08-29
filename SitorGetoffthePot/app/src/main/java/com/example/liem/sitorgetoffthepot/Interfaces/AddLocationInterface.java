package com.example.liem.sitorgetoffthepot.Interfaces;

import android.location.Location;

import com.google.android.gms.maps.model.LatLng;

public interface AddLocationInterface {

    void newLocationCancelButtonPress();
    void newLocationAddButtonPress(String _title, String _rating, String _detail);

}
