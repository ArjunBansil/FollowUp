package com.kingkrooks.followup;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.provider.MediaStore;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.zxing.integration.android.IntentIntegrator;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import objects.MediaAdapter;
import objects.Media_Info;


public class ResultFragment extends Fragment {

    private String result = "No result yet hamie";
    private View view;


    public ResultFragment() {
    }


    public void setContent(String r){
        this.result = r;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view= inflater.inflate(R.layout.fragment_result, container, false);


        getActivity().setTitle(getResources().getString(R.string.results));

        RecyclerView recList = (RecyclerView)view.findViewById(R.id.cardList);
        recList.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity().getApplicationContext());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recList.setLayoutManager(llm);

        List<Media_Info> mList = getList();
        Log.i("tag", "List got received");

        MediaAdapter ma = new MediaAdapter(mList);
        Log.i("tag", "Adapter is set, holmes");
        recList.setAdapter(ma);
        Log.i("tag", "RecList has set the adapter");


        return view;
    }

    public ArrayList<Media_Info> getList(){
        String[] r_array = new String[10];
        try {
            r_array = result.split("\\s+");
        }catch (Exception e){
            e.printStackTrace();
        }
        ArrayList<Media_Info> list = new ArrayList<Media_Info>();

        for(int i = 0; i < r_array.length; i++){
            list.add(new Media_Info(r_array[i], getActivity().getApplicationContext()));
            Log.i("tag", r_array[i] +" is being added");
        }

        return list;

    }



}
