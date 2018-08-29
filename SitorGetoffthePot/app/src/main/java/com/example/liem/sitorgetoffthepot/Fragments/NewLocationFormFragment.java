package com.example.liem.sitorgetoffthepot.Fragments;

import android.content.Context;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.PluralsRes;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.liem.sitorgetoffthepot.Interfaces.AddLocationInterface;
import com.example.liem.sitorgetoffthepot.Interfaces.LoginRegInterface;
import com.example.liem.sitorgetoffthepot.R;
import com.google.android.gms.maps.model.LatLng;

import java.util.Objects;

import io.realm.Realm;

public class NewLocationFormFragment extends Fragment {

    private static final String TAG = "NewLocFragment";

    private AddLocationInterface mAddLocationInterface;

    private EditText mLocationTitle;
    private EditText mLocationDetail;
    private RadioButton mHoverRadioButton;
    private RadioButton mFullContactButton;

    public static NewLocationFormFragment newInstance() {

        Bundle args = new Bundle();

        NewLocationFormFragment fragment = new NewLocationFormFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if(context instanceof AddLocationInterface){
            mAddLocationInterface = (AddLocationInterface) context;
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.new_location_fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mLocationTitle = Objects.requireNonNull(getActivity()).findViewById(R.id.location_title_edit_text_view);
        mLocationDetail = getActivity().findViewById(R.id.location_detail_edit_text_view);
        mHoverRadioButton = getActivity().findViewById(R.id.radio_hover);
        mFullContactButton = getActivity().findViewById(R.id.radio_full_contact);

        Button addNewBut = getActivity().findViewById(R.id.add_new_location_button);
        Button cancelBut = getActivity().findViewById(R.id.cancel_location_button);
        addNewBut.setOnClickListener(addNewLocationButton);
        cancelBut.setOnClickListener(cancelButton);

    }

    private View.OnClickListener addNewLocationButton = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            mLocationTitle.setError(null);
            mLocationDetail.setError(null);

            String title = mLocationTitle.getText().toString();
            String detail = mLocationDetail.getText().toString();
            String rating = "";
            boolean hoverCheck = mHoverRadioButton.isChecked();
            boolean fullContactCheck = mFullContactButton.isChecked();

            if ( title.isEmpty() ) {

                if (title.isEmpty()){
                    mLocationTitle.setError("You can't leave this field blank");
                }

            } else if ( !hoverCheck && !fullContactCheck) {

                Toast.makeText(getContext(), "Please select a rating", Toast.LENGTH_SHORT).show();

            } else {

                if (hoverCheck) {
                    rating = "hover";
                } else {
                    rating = "full contact";
                }

                mAddLocationInterface.newLocationAddButtonPress(title,rating,detail);
            }

        }
    };

    private View.OnClickListener cancelButton = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            mAddLocationInterface.newLocationCancelButtonPress();
        }
    };


}
