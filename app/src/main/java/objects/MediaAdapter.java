package objects;

import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.text.ClipboardManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.kingkrooks.followup.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Arjun Bansil on 8/5/2015.
 */
public class MediaAdapter extends RecyclerView.Adapter<MediaAdapter.MediaHolder> {

    private List<Media_Info> mList;

    public MediaAdapter(List<Media_Info> l){
        this.mList = l;
    }

    @Override
    public int getItemCount(){
        return mList.size();
    }

    @Override
    public void onBindViewHolder(MediaHolder mHolder, int i){
        final Media_Info m = mList.get(i);

        final Intent a;
        final Context c;

        mHolder.title.setText(m.returnType());
        mHolder.content.setText(m.getUrl());
        c = m.getP();

        int color = m.getColor();

        mHolder.title.setBackgroundColor(color);

        if(m.returnType().equals("Phone Number")){
            a = new Intent(Intent.ACTION_CALL, Uri.parse("tel:"+m.getUrl()));
        }else if(m.returnType().equals("Email")){
            a = new Intent(Intent.ACTION_SENDTO, Uri.fromParts("mailto", m.getUrl(), null));
            a.putExtra(Intent.EXTRA_SUBJECT, "Subject");
            a.putExtra(Intent.EXTRA_TEXT, "Body");
        }
        else{
            a = new Intent(Intent.ACTION_VIEW, Uri.parse(m.getUrl()));
        }
        a.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);


        mHolder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!m.returnType().equals("Name")){
                    c.startActivity(a);
                }
                android.content.ClipboardManager clipboardManager = (android.content.ClipboardManager)c.getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("text",m.getUrl());
                clipboardManager.setPrimaryClip(clip);
            }
        });



    }


    @Override
    public MediaHolder onCreateViewHolder(ViewGroup viewGroup, int i){
        View itemView = LayoutInflater
                .from(viewGroup.getContext()).inflate(R.layout.result_layout, viewGroup, false);
        return new MediaHolder(itemView);
    }


    public static class MediaHolder extends RecyclerView.ViewHolder{
        private View view;
        private TextView title, content;

        public MediaHolder(View v){
            super(v);
            this.view = v;
            this.title = (TextView)v.findViewById(R.id.title);
            this.content = (TextView)v.findViewById(R.id.url);
        }
    }
}
