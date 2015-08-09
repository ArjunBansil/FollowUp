package com.kingkrooks.followup;


import android.content.Intent;
import android.os.Bundle;
import android.app.Fragment;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
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
    public DatabaseHandler db;
    private EditText phoneNum;
    private Button b;

    private FacebookCallback<LoginResult> mCallBack = new FacebookCallback<LoginResult>() {
        @Override
        public void onSuccess(LoginResult loginResult) {
            Profile profile = Profile.getCurrentProfile();
            if(profile!=null){
                Media_Container m = new Media_Container("Facebook", profile.getLinkUri().toString());
                db.cleanUp("Facebook");
                db.addMedia(m);
            }
        }

        @Override
        public void onCancel() {

        }

        @Override
        public void onError(FacebookException e) {

        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_fragment_two, container, false);

        twitterLoginButton = (TwitterLoginButton)view.findViewById(R.id.twitter_login);
        LoginButton loginButton = (LoginButton)view.findViewById(R.id.login_button);
        callbackManager = CallbackManager.Factory.create();
        loginButton.registerCallback(callbackManager, mCallBack);
        db = new DatabaseHandler(getActivity().getApplicationContext());
        b = (Button)view.findViewById(R.id.addPhone);
        phoneNum = (EditText)view.findViewById(R.id.textDialog);


        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Media_Container m = new Media_Container("Phone", phoneNum.getText().toString());
                db.cleanUp("Phone");
                db.addMedia(m);
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
            }

            @Override
            public void failure(TwitterException e) {

            }
        });

        return view;
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data){
        Snackbar.make(view, "Login Successful", Snackbar.LENGTH_SHORT)
                .show();
        callbackManager.onActivityResult(requestCode, resultCode, data);
        twitterLoginButton.onActivityResult(requestCode, resultCode, data);
    }


}
