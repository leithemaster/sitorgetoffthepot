package com.example.liem.sitorgetoffthepot.UI;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.liem.sitorgetoffthepot.DataModels.LocationInfo;
import com.example.liem.sitorgetoffthepot.R;

import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmResults;

public class ListViewSetUp extends BaseAdapter {

    // Base Id
    private static final int bID = 100000;

    // Context
    private final Context mContext;

    // Collection item
    private RealmResults<LocationInfo> mCollection;

    // Constructor
    public ListViewSetUp(Context _context) {
        mContext = _context;

        Realm realm = Realm.getDefaultInstance();

        mCollection = realm.where(LocationInfo.class).findAll();
    }

    // Count
    public int getCount() {
        if(mCollection!=null) {
            return mCollection.size();
        }
        return 0;
    }

    // Item
    public Object getItem(int _position) {
        if( mCollection!=null &&
                _position < mCollection.size() &&
                _position > -1) {
            return mCollection.get(_position);
        }
        return null;
    }

    // Item ID
    public long getItemId(int _position) {
        return bID + _position;
    }

    // Get the inflated child / line-item view
    public View getView(int _position, View _reusableView, ViewGroup _parentView) {

        if(_reusableView == null) {
            _reusableView = LayoutInflater.from(mContext)
                    .inflate(R.layout.location_listview_adapter , _parentView,false);
        }

        TextView title = _reusableView.findViewById(R.id.name);
        TextView detail = _reusableView.findViewById(R.id.detail);
        TextView rating = _reusableView.findViewById(R.id.rating);

        LocationInfo locationInfo = (LocationInfo) getItem(_position);

        title.setText(locationInfo.getTitle());
        detail.setText(locationInfo.getDetail());
        rating.setText(locationInfo.getRating());

        return _reusableView;
    }

}
