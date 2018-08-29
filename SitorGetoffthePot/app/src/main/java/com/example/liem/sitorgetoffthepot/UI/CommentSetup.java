package com.example.liem.sitorgetoffthepot.UI;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.liem.sitorgetoffthepot.DataModels.Comments;
import com.example.liem.sitorgetoffthepot.DataModels.LocationInfo;
import com.example.liem.sitorgetoffthepot.R;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmResults;

public class CommentSetup extends BaseAdapter{

    // Base Id
    private static final int bID = 100000;

    // Context
    private final Context mContext;

    // Collection item
    private RealmList mCollection;

    // Constructor
    public CommentSetup(Context _context, String id ) {
        mContext = _context;
        Realm realm = Realm.getDefaultInstance();

        RealmResults<LocationInfo> results = realm.where(LocationInfo.class).equalTo("mLocationId", id).findAll();

        mCollection = results.get(0).getComments();
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
                    .inflate(R.layout.comments_listview_adapter , _parentView,false);
        }

        TextView title = _reusableView.findViewById(R.id.comment_username);
        TextView detail = _reusableView.findViewById(R.id.comment_comments);

        Comments comments = (Comments) getItem(_position);

        title.setText(comments.getmUsername());
        detail.setText(comments.getmComment());

        return _reusableView;
    }

}
