package com.kingkrooks.followup;

import android.app.ActionBar;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.app.Fragment;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toolbar;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.google.zxing.qrcode.QRCodeWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import objects.DatabaseHandler;
import objects.Media_Container;


public class MainActivityFragment extends Fragment {

    private DatabaseHandler db;
    private View view;
    private ImageView img;
    private FloatingActionButton fab;
    private Button login;
    private TextView txt;
    private static final int PIX_HEIGHT_WIDTH = 350;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){
        view = inflater.inflate(R.layout.fragment_first, container, false);

        final MainActivity activity = (MainActivity)getActivity();
        activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        db = new DatabaseHandler(getActivity().getApplicationContext());
        img= (ImageView)view.findViewById(R.id.imageView);
        login = (Button)view.findViewById(R.id.goToLogin);
        fab = (FloatingActionButton)view.findViewById(R.id.fab);
        txt = (TextView)view.findViewById(R.id.textView);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doThingss();
            }
        });

        getActivity().setTitle(getResources().getString(R.string.main_title));

        Cursor cursor = db.getReadableDatabase()
                .rawQuery("SELECT * FROM " + "Social_Media", null);
        boolean row;

        if(cursor.moveToFirst()){
            row = true;
        }else{
            row = false;
        }

        if(row){
            img.setVisibility(View.VISIBLE);
            login.setVisibility(View.GONE);
            List<Media_Container> mList = db.getAllMedia();
            List<Media_Container> fList = new ArrayList<Media_Container>();

            Iterator iterator = mList.iterator();

            while(iterator.hasNext()){
                Media_Container m = (Media_Container)iterator.next();
                if(!fList.contains(m)){
                    fList.add(m);
                }
            }

            String data = "";
            for(int i = 0; i< fList.size(); i++){
                data+=fList.get(i).getLink();
                data+=" ";
            }
            try{
                generateQRCode(data);
                txt.setText(getResources().getString(R.string.explanation));

            }catch (WriterException e){
                e.printStackTrace();
            }

        }else{
            img.setVisibility(View.INVISIBLE);
            login.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    activity.initial = true;
                    activity.goToLogin();
                }
            });


        }



        return view;
    }

    public void doThingss(){
        IntentIntegrator.forFragment(this).initiateScan();
    }

    private void generateQRCode(String data) throws WriterException {
        com.google.zxing.Writer writer = new QRCodeWriter();

        BitMatrix bm = writer.encode(data, BarcodeFormat.QR_CODE, PIX_HEIGHT_WIDTH, PIX_HEIGHT_WIDTH);
        Bitmap ImageBitmap = Bitmap.createBitmap(PIX_HEIGHT_WIDTH, PIX_HEIGHT_WIDTH, Bitmap.Config.ARGB_8888);

        for (int i = 0; i < PIX_HEIGHT_WIDTH; i++) {
            for (int j = 0; j < PIX_HEIGHT_WIDTH; j++) {
                ImageBitmap.setPixel(i, j, bm.get(i, j) ? Color.BLACK: Color.WHITE);
            }
        }
        img.setImageBitmap(ImageBitmap);
    }


    public void onActivityResult(int requestCode, int resultCode, Intent data){
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        Log.i("tag", "Result received");
        if(result!=null){
            if(result.getContents() == null){
                Snackbar.make(view, "No result captured", Snackbar.LENGTH_SHORT)
                        .show();
            }else{
                MainActivity activity = (MainActivity)getActivity();
                Log.i("tag", "Sending result over, holmes");
                activity.useResult(result.getContents());
            }
        }

    }
}
