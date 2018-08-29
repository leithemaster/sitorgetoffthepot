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
import com.example.liem.sitorgetoffthepot.R;

import java.util.Map;
import java.util.Objects;

import io.realm.ObjectServerError;
import io.realm.Realm;
import io.realm.SyncConfiguration;
import io.realm.SyncCredentials;
import io.realm.SyncUser;

public class LoginFragment extends Fragment {

    private static final String TAG = "LoginFragment";

    private EditText mUsernameEditTextView;
    private EditText mPassword;
    private LoginRegInterface mLoginRegInterface;

    public static LoginFragment newInstance() {

        Bundle args = new Bundle();

        LoginFragment fragment = new LoginFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.login_screen_fragment, container, false);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if(context instanceof LoginRegInterface){
            mLoginRegInterface = (LoginRegInterface) context;
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        //Note: setting up the views and buttons
        mUsernameEditTextView = Objects.requireNonNull(getActivity()).findViewById(R.id.username_edit_text_field);
        mPassword = getActivity().findViewById(R.id.password_edit_text_field);

        Button loginBut = getActivity().findViewById(R.id.login_screen_login_button);
        Button cancelBut = getActivity().findViewById(R.id.login_screen_cancel_button);
        Button regScreenBut = getActivity().findViewById(R.id.login_screen_to_reg_screen_button);
        loginBut.setOnClickListener(loginButton);
        cancelBut.setOnClickListener(cancelButton);
        regScreenBut.setOnClickListener(regButton);

    }

    private View.OnClickListener loginButton = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            mUsernameEditTextView.setError(null);
            mPassword.setError(null);

            String username = mUsernameEditTextView.getText().toString();
            final String password = mPassword.getText().toString();

            // Note: checking the user inputs
            if ( username.isEmpty() || password.isEmpty() ) {

                if (username.isEmpty()){
                    mUsernameEditTextView.setError("You can't leave this field blank.");
                } else {
                    mPassword.setError("You can't leave this field blank.");
                }

            } else {
                // Note: logging in the user also realm checks if the information is correct or not.

                Map<String, SyncUser> users = SyncUser.all();
                for (Map.Entry < String, SyncUser > user: users.entrySet()) {
                    SyncUser temp = user.getValue();
                    temp.logOut();
                }

                Log.i(TAG, "onClick: Logged out the current anonymous user");
                SyncCredentials credentials = SyncCredentials.usernamePassword(username, password, false);
                SyncUser.logInAsync(credentials, Constants.AUTH_URL, new SyncUser.Callback<SyncUser>() {
                    @Override
                    public void onSuccess(SyncUser user) {
                        Log.i(TAG, "onSuccess: User Login");
                        mLoginRegInterface.loginButtonPress(credentials.getUserIdentifier());
                    }

                    @Override
                    public void onError(ObjectServerError error) {
                        mUsernameEditTextView.setError(error.toString());
                        mPassword.setError(error.toString());
                        Log.e("Login error", error.toString());
                    }
                });
            }
        }
    };

    private View.OnClickListener cancelButton = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            mLoginRegInterface.loginCancelButtonPress();
        }
    };

    private View.OnClickListener regButton = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            mLoginRegInterface.loginRegButtonPress();
        }
    };

}
