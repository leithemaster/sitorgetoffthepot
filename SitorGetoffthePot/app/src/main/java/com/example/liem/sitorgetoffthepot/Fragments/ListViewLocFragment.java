package com.example.liem.sitorgetoffthepot.Fragments;

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
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.liem.sitorgetoffthepot.DataModels.LocationInfo;
import com.example.liem.sitorgetoffthepot.Interfaces.LocListViewInterface;
import com.example.liem.sitorgetoffthepot.R;
import com.example.liem.sitorgetoffthepot.UI.ListViewSetUp;

import java.util.Objects;

import io.realm.Realm;
import io.realm.RealmResults;

public class ListViewLocFragment extends Fragment {

    private static final String TAG = "ListViewLocFragment";

    private ListView mListView;

    private LocListViewInterface mLocListViewInterface;

    public static ListViewLocFragment newInstance() {

        Bundle args = new Bundle();

        ListViewLocFragment fragment = new ListViewLocFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.list_view_fragment, container, false);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.list_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.go_to_map_button) {

            mLocListViewInterface.mapButtonClick();

        }

        return true;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if(context instanceof LocListViewInterface){
            mLocListViewInterface = (LocListViewInterface) context;
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mListView = Objects.requireNonNull(getActivity()).findViewById(android.R.id.list);
        mListView.setOnItemClickListener(itemSelected);
        // Create the adapter
        ListViewSetUp list = new ListViewSetUp(getActivity());
        // Inform the list view of its new adapter
        mListView.setAdapter(list);
    }

    private final AdapterView.OnItemClickListener itemSelected = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

            Realm realm = Realm.getDefaultInstance();

            RealmResults<LocationInfo> loc = realm.where(LocationInfo.class).findAll();

            mLocListViewInterface.locationClicked(Objects.requireNonNull(loc.get(i)).getLocationId());
        }
    };





}
