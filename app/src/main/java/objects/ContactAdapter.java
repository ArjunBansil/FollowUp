package objects;

import android.content.Context;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.kingkrooks.followup.MainActivity;
import com.kingkrooks.followup.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.util.List;

/**
 * Created by Arjun Bansil on 8/14/2015.
 */
public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.ContactViewHolder> {

    private List<String> list;

    public ContactAdapter(List<String> contactList){
        this.list = contactList;
    }

    @Override
    public int getItemCount(){
        return list.size();
    }

    @Override
    public void onBindViewHolder(ContactViewHolder ch, int i){
        String s = list.get(i);
        final String l = list.get(i);
        int beginning = s.indexOf("(Name)");
        int end = s.indexOf("|");

        s = s.substring(beginning + 6, end);

        s = s.replace(",", " ");
        final MainActivity activity = (MainActivity)ch.view.getContext();

        final String fin = s;

        ch.name.setText(s);
        ch.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                list.remove(l);
                try{
                    File directory = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/Connext");
                    directory.mkdirs();
                    File file = new File(directory, "contacts.txt");
                    FileOutputStream fos = new FileOutputStream(file);
                    ObjectOutputStream oos = new ObjectOutputStream(fos);
                    oos.writeObject(list);
                    oos.close();
                }catch (Exception e){
                    e.printStackTrace();
                }


                activity.useResult(l);
            }
        });
    }

    @Override
    public ContactViewHolder onCreateViewHolder(ViewGroup viewGroup, int i){
        View itemView = LayoutInflater
                .from(viewGroup.getContext())
                .inflate(R.layout.contact_card, viewGroup, false);
        return new ContactViewHolder(itemView);
    }

    public static class ContactViewHolder extends RecyclerView.ViewHolder{
        protected TextView name;
        protected FloatingActionButton fab;
        private View view;

        public ContactViewHolder(View v){
            super(v);
            view = v;

            name = (TextView)v.findViewById(R.id.name);
            fab = (FloatingActionButton)v.findViewById(R.id.goTo);
        }
    }
}
