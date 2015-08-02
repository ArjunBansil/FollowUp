package objects;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;

/**
 * Created by Arjun Bansil on 8/1/2015.
 */
public class DatabaseHandler extends SQLiteOpenHelper {

    private static final int db_version = 1;

    private static final String db_name = "Social Media Collection";
    private static final String table_name = "Social_Media";

    private static final String key_id = "id";
    private static final String key_type = "name";
    private static final String key_link = "link";

    public DatabaseHandler(Context context){
        super(context, db_name, null, db_version);
    }

    public void addMedia(Media_Container m){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(key_type, m.getSMedia());
        values.put(key_link, m.getLink());

        db.insert(table_name, null, values);
        db.close();
    }

    @Override
    public void onCreate(SQLiteDatabase db){
        String createTable = "CREATE TABLE " + table_name + "(" +
                key_id +" INTEGER PRIMARY KEY, " + key_type + " TEXT, "
                + key_link + " TEXT)";
        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
        db.execSQL("DROP TABLE IF EXISTS " + table_name);
        onCreate(db);
    }

    public Media_Container getMedia(int id){
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(table_name, new String[]{ key_id, key_type,
            key_link}, key_id + "=?", new String[]{String.valueOf(id)},
                null, null, null, null);
        if(cursor!=null){
            cursor.moveToFirst();
        }

        Media_Container m = new Media_Container(cursor.getString(1), cursor.getString(2));
        return m;
    }

    public List<Media_Container> getAllMedia(){
        List<Media_Container> mediaList = new ArrayList<Media_Container>();
        String selectQuery = "SELECT * FROM " + table_name;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if(cursor.moveToFirst()){
            do{
                Media_Container m = new Media_Container();
                m.setId(Integer.parseInt(cursor.getString(0)));
                m.setSMedia(cursor.getString(1));
                m.setLink(cursor.getString(2));
                mediaList.add(m);
            }while(cursor.moveToNext());
        }

        return mediaList;
    }

    public void cleanUp(String type){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(table_name, key_type +" = ?", new String[]{type});
        db.close();
    }

    public int updateMedia(Media_Container m){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(key_type, m.getSMedia());
        values.put(key_link, m.getLink());

        return db.update(table_name, values, key_id + " = ?",
                new String[]{String.valueOf(m.getId())});

    }

    public void deleteMedia(Media_Container m){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(table_name, key_id+" = ?", new String[]{String.valueOf(m.getId())});
        db.close();
    }
}
