package com.example.liem.sitorgetoffthepot.Fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.example.liem.sitorgetoffthepot.DataModels.Comments;
import com.example.liem.sitorgetoffthepot.DataModels.LocationInfo;
import com.example.liem.sitorgetoffthepot.Interfaces.DetailInterface;
import com.example.liem.sitorgetoffthepot.Interfaces.LocListViewInterface;
import com.example.liem.sitorgetoffthepot.R;
import com.example.liem.sitorgetoffthepot.UI.CommentSetup;
import com.example.liem.sitorgetoffthepot.UI.ListViewSetUp;

import org.w3c.dom.Comment;

import java.util.Date;
import java.util.Objects;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmResults;

public class DetailViewFragment extends Fragment {


    private static final String TAG = "DetailViewFragment";

    private DetailInterface mDetailInterface;

    private TextView mTitleView;
    private TextView mDeatilTextView;
    private TextView mRatingTextView;
    private ListView mCommentListView;

    private LocationInfo info;
    private CommentSetup list;

    private static String mLocationId;
    private Realm realm;
    private String mUsername;

    public static DetailViewFragment newInstance(String _locationId) {

        Bundle args = new Bundle();

        mLocationId = _locationId;

        DetailViewFragment fragment = new DetailViewFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if(context instanceof DetailInterface){
            mDetailInterface = (DetailInterface) context;
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.location_detail_fragment, container, false);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        realm = Realm.getDefaultInstance();

        RealmResults<LocationInfo> results = realm.where(LocationInfo.class).equalTo("mLocationId", mLocationId).findAll();

        info = results.get(0);

        mUsername = info.getUsername();

        mTitleView = Objects.requireNonNull(getActivity()).findViewById(R.id.detail_screen_title_text_view);
        mDeatilTextView = Objects.requireNonNull(getActivity()).findViewById(R.id.detail_screen_detail_text_view);
        mRatingTextView = Objects.requireNonNull(getActivity()).findViewById(R.id.detail_screen_rating_text_view);

        mTitleView.setText(info.getTitle());
        mDeatilTextView.setText("Detail: " + info.getDetail());
        mRatingTextView.setText("Rating: " + info.getRating());

        mCommentListView = Objects.requireNonNull(getActivity()).findViewById(R.id.comment_list_view);

        list = new CommentSetup( getActivity(), info.getLocationId() );
        // Inform the list view of its new adapter
        mCommentListView.setAdapter(list);

    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.detail_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.add_comment_button) {
            View dialogView = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_task, null);
            EditText taskText = dialogView.findViewById(R.id.task);
            new AlertDialog.Builder(getActivity())
                    .setTitle("New Comment")
                    .setMessage("Anything else you want to let other user know?")
                    .setView(dialogView)
                    .setPositiveButton("Add", (dialog, which) -> {

                        Realm realm = Realm.getDefaultInstance();

                        realm.executeTransaction(new Realm.Transaction() {
                            @Override
                            public void execute(Realm realm) {
                                LocationInfo loc = realm.where(LocationInfo.class).equalTo("mLocationId", info.getLocationId()).findFirst();

                                if (loc.getComments() == null) {
                                    loc.setComments(new RealmList<>());
                                }

                                Comments comments = realm.createObject(Comments.class);

                                comments.mUsername = mUsername;
                                comments.mComment = taskText.getText().toString();
                                comments.mTimeStamp = new Date();

                                loc.addComment(comments);
                            }
                        });
                    })
                    .setNegativeButton("Cancel", null)
                    .create()
                    .show();

            list.notifyDataSetChanged();
        }

        return true;
    }


}
