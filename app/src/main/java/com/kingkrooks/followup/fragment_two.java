package com.kingkrooks.followup;


import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.app.Fragment;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import org.json.JSONObject;


public class fragment_two extends Fragment {

    private TextView info;
    private LoginButton loginButton;
    private CallbackManager callbackManager;
    private Button b;
    public JSONObject object = null;
    public View view;

    public fragment_two newInstance(){
        return new fragment_two();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_fragment_two, container, false);

        info = (TextView)view.findViewById(R.id.info);
        loginButton = (LoginButton)view.findViewById(R.id.login_button);
        loginButton.setReadPermissions("user_birthday");

        b = (Button)view.findViewById(R.id.logout);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginManager.getInstance().logOut();
            }
        });
        callbackManager = CallbackManager.Factory.create();



        return view;
    }

    public void demo(String txt){
        info.setText(txt);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data){
        Toast.makeText(getActivity().getApplicationContext(), "HEy, was gud", Toast.LENGTH_SHORT).show();

    }



}
