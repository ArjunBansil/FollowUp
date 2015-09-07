package com.kingkrooks.followup;


import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.app.Fragment;
import android.os.Environment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.util.ArrayList;

import objects.ContactAdapter;


public class Contacts extends Fragment {

    private View view;
    private TextView txt;

    public static Contacts newInstance() {
        Contacts fragment = new Contacts();

        return fragment;
    }

    public Contacts() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_contacts, container, false);

        getActivity().setTitle("Saved Contacts");
        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);

        RecyclerView recList = (RecyclerView)view.findViewById(R.id.contactList);
        recList.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity().getApplicationContext());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recList.setLayoutManager(llm);
        if(getList() != null){
            if(getList().isEmpty()){
                txt = (TextView)view.findViewById(R.id.empty);
                txt.setVisibility(View.VISIBLE);
                txt.setText("No Contact Info Saved! Start Scanning!");
            }else{
                ContactAdapter ca = new ContactAdapter(getList());
                recList.setAdapter(ca);
            }
        }else{
            txt = (TextView)view.findViewById(R.id.empty);
            txt.setVisibility(View.VISIBLE);
            txt.setText("No Contact Info Saved! Start Scanning!");
        }





        return view;
    }

    private ArrayList<String> getList(){
        ArrayList<String> list = null;
        try{
            Log.i("tag", "File is there, holmes");
            File directory = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/Connext");
            File file = new File(directory, "contacts.txt");
            FileInputStream fis = new FileInputStream(file);
            ObjectInputStream ois = new ObjectInputStream(fis);
            list = (ArrayList<String>)ois.readObject();
            ois.close();
        }catch (Exception e){
            Log.i("tag", "File ain't there, holmes");
            list = null;
        }

        return list;
    }


}
