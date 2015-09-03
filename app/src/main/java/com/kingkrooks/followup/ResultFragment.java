package com.kingkrooks.followup;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;

import org.w3c.dom.Text;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import objects.MediaAdapter;
import objects.Media_Info;


public class ResultFragment extends Fragment {

    private String result = "No result yet hamie";
    private View view;
    private int counter = 1;
    private FloatingActionButton fab;


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
        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        fab = (FloatingActionButton)view.findViewById(R.id.saveButton);


        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(counter == 0){
                    File directory = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/Connext");
                    directory.mkdirs();
                    File file = new File(directory, "contacts.txt");
                    if(!file.exists()){
                        Log.i("tag", "File is being created, holmes");
                        try{
                            Toast.makeText(getActivity().getApplicationContext(), "File written", Toast.LENGTH_SHORT)
                                    .show();
                            ArrayList<String> list = new ArrayList<String>();
                            list.add(result);
                            FileOutputStream fos = new FileOutputStream(file);
                            ObjectOutputStream oos = new ObjectOutputStream(fos);
                            oos.writeObject(list);
                            oos.close();
                        }catch (Exception e){
                            e.printStackTrace();
                        }

                    }else{
                        Log.i("tag", "File has been created, reading the results");
                        try{
                            FileInputStream fis = new FileInputStream(file);
                            ObjectInputStream ois = new ObjectInputStream(fis);
                            ArrayList<String> list = (ArrayList<String>)ois.readObject();
                            list.add(result);
                            ois.close();



                            FileOutputStream fos = new FileOutputStream(file);
                            ObjectOutputStream oos = new ObjectOutputStream(fos);
                            oos.writeObject(list);
                            oos.close();

                        }catch (Exception e){
                            e.printStackTrace();

                        }

                    }

                    Snackbar.make(view, "Contact Saved for Later", Snackbar.LENGTH_SHORT)
                            .show();
                    counter++;
                }else{
                    Snackbar.make(view, "Contact Already Saved", Snackbar.LENGTH_SHORT)
                            .show();
                }

            }
        });

        fab.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Toast.makeText(getActivity().getApplicationContext(), "Save Contact for Later", Toast.LENGTH_SHORT)
                        .show();
                return true;
            }
        });


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

    @Override
    public void onDetach(){
        super.onDetach();
        getActivity().getFragmentManager().beginTransaction().remove(this);
        counter = 0;
    }

    @Override
    public void onResume(){
        super.onResume();
        counter = 0;
    }



}
