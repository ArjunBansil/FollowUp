package com.kingkrooks.followup;


import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.app.Fragment;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.Profile;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.identity.TwitterLoginButton;
import objects.DatabaseHandler;
import objects.Media_Container;


public class fragment_two extends Fragment {

    private CallbackManager callbackManager;
    public View view;
    private TwitterLoginButton twitterLoginButton;
    private LoginButton loginButton;
    public DatabaseHandler db;
    private EditText phoneNum, emailAddress;
    private FloatingActionButton b, b2;

    private FacebookCallback<LoginResult> mCallBack = new FacebookCallback<LoginResult>() {
        @Override
        public void onSuccess(LoginResult loginResult) {
            Profile profile = Profile.getCurrentProfile();
            if(profile!=null){
                Media_Container m = new Media_Container("Facebook", profile.getLinkUri().toString());
                db.cleanUp("Facebook");
                db.addMedia(m);
                Snackbar.make(view, "Facebook Account Added", Snackbar.LENGTH_SHORT)
                        .show();
            }
        }

        @Override
        public void onCancel() {
            Snackbar.make(view, "Facebook Account Was Not Added", Snackbar.LENGTH_LONG)
                    .show();
        }

        @Override
        public void onError(FacebookException e) {
            Log.i("tag", "Facebook Error: " + e.toString());
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_fragment_two, container, false);

        getActivity().setTitle(getResources().getString(R.string.add_info));




        twitterLoginButton = (TwitterLoginButton)view.findViewById(R.id.twitter_login);
        loginButton = (LoginButton)view.findViewById(R.id.login_button);
        callbackManager = CallbackManager.Factory.create();
        loginButton.registerCallback(callbackManager, mCallBack);
        db = new DatabaseHandler(getActivity().getApplicationContext());
        b = (FloatingActionButton)view.findViewById(R.id.addPhone);
        b2 = (FloatingActionButton)view.findViewById(R.id.addEmail);
        phoneNum = (EditText)view.findViewById(R.id.textDialog);
        emailAddress = (EditText)view.findViewById(R.id.emailEntry);


        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Media_Container m = new Media_Container("Phone", phoneNum.getText().toString());
                db.cleanUp("Phone");
                db.addMedia(m);
                Snackbar.make(view, "Phone Number Added", Snackbar.LENGTH_SHORT)
                        .show();
                InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
        });

        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Media_Container m = new Media_Container("Email", emailAddress.getText().toString());
                db.cleanUp("Email");
                db.addMedia(m);
                Snackbar.make(view, "Primary Email Account Added", Snackbar.LENGTH_SHORT)
                        .show();
                InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
        });


        twitterLoginButton.setCallback(new Callback<TwitterSession>() {
            @Override
            public void success(Result<TwitterSession> result) {
                TwitterSession session = Twitter.getSessionManager().getActiveSession();
                String id = session.getUserName();
                String url = "@";
                Media_Container m = new Media_Container("Twitter", url + id);
                db.cleanUp("Twitter");
                db.addMedia(m);

                Snackbar.make(view, "Twitter Account Added", Snackbar.LENGTH_SHORT)
                        .show();
            }

            @Override
            public void failure(TwitterException e) {
                Log.i("tag", "Twitter Failure: " + e.toString());
            }
        });

        return view;
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data){

        callbackManager.onActivityResult(requestCode, resultCode, data);
        twitterLoginButton.onActivityResult(requestCode, resultCode, data);
    }


}
