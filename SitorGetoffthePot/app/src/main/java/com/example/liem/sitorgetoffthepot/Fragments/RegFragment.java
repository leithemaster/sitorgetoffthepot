package com.example.liem.sitorgetoffthepot.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.liem.sitorgetoffthepot.DataModels.Constants;
import com.example.liem.sitorgetoffthepot.Interfaces.LoginRegInterface;
import com.example.liem.sitorgetoffthepot.Interfaces.RegInterface;
import com.example.liem.sitorgetoffthepot.R;

import java.util.Map;
import java.util.MissingResourceException;
import java.util.Objects;

import io.realm.ObjectServerError;
import io.realm.Realm;
import io.realm.SyncConfiguration;
import io.realm.SyncCredentials;
import io.realm.SyncUser;

public class RegFragment extends Fragment {

    private static final String TAG = "RegFragment";

    private EditText mUsernameEditTextView;
    private EditText mPassword;
    private EditText mComfirmPassword;
    private RegInterface mRegInterFace;

    public static RegFragment newInstance() {

        Bundle args = new Bundle();

        RegFragment fragment = new RegFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.reg_screen_fragment, container, false);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if(context instanceof RegInterface){
            mRegInterFace = (RegInterface) context;
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mUsernameEditTextView = Objects.requireNonNull(getActivity()).findViewById(R.id.reg_screen_username_edit_text_field);
        mPassword = getActivity().findViewById(R.id.reg_screen_password_edit_text_field);
        mComfirmPassword = getActivity().findViewById(R.id.reg_screen_comfirm_password_edit_text_field);


        Button loginBut = getActivity().findViewById(R.id.reg_screen_reg_button);
        Button cancelBut = getActivity().findViewById(R.id.reg_screen_cancel_button);
        loginBut.setOnClickListener(regButton);
        cancelBut.setOnClickListener(cancelButton);
    }

    private View.OnClickListener regButton;

    {
        regButton = new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mUsernameEditTextView.setError(null);
                mPassword.setError(null);
                mComfirmPassword.setError(null);

                String username = mUsernameEditTextView.getText().toString();
                String password = mPassword.getText().toString();
                String confirmPassword = mComfirmPassword.getText().toString();

                // Note: Checking the user inputs
                if (username.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {

                    if (username.isEmpty()) {
                        mUsernameEditTextView.setError("You can't leave this field blank.");
                    } else if (password.isEmpty()) {
                        mPassword.setError("You can't leave this field blank.");
                    } else {
                        mComfirmPassword.setError("You can't leave this field blank.");
                    }

                } else if (!(password.equals(confirmPassword))) {

                    mComfirmPassword.setError("Your passwords does not match.");

                } else {

                    Map<String, SyncUser> users = SyncUser.all();
                    for (Map.Entry < String, SyncUser > user: users.entrySet()) {
                        SyncUser temp = user.getValue();
                        temp.logOut();
                    }

                    Log.i(TAG, "onClick: Logged out the current anonymous user");
                    final SyncCredentials credentials = SyncCredentials.usernamePassword(username, confirmPassword, true);
                    SyncUser.logInAsync(credentials, Constants.AUTH_URL, new SyncUser.Callback<SyncUser>() {
                        @Override
                        public void onSuccess(SyncUser user) {
                            Log.i(TAG, "onSuccess: User Created");
                            mRegInterFace.regButtonPress(credentials.getUserIdentifier());
                        }

                        @Override
                        public void onError(ObjectServerError error) {
                            Log.e("Login error", error.toString());
                        }
                    });


                }
            }
        };
    }

    private View.OnClickListener cancelButton = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            mRegInterFace.regCancelButtonPress();
        }
    };

}
