package Data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import Modal.TaskItems;
import Util.dbInfo;
import androidx.annotation.Nullable;

public class DatabaseHandler extends SQLiteOpenHelper {
    public DatabaseHandler(@Nullable Context context) {
        super(context, dbInfo.DB_NAME, null, dbInfo.VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TABLE = "CREATE TABLE " + dbInfo.TABLE_NAME + "("
                + dbInfo.ID + " INTEGER PRIMARY KEY," + dbInfo.TASK + " TEXT)";

        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+dbInfo.TABLE_NAME);
        onCreate(db);
    }
    //Add
    public void addTask(TaskItems items) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(dbInfo.TASK,items.getTask());
        //Insert the row
        db.insert(dbInfo.TABLE_NAME, null, values);

        Log.d("Saved!!", "Saved to DB");

    }
    public TaskItems getTask(int id){
        SQLiteDatabase db=this.getWritableDatabase();
        Cursor cursor=db.query(dbInfo.TABLE_NAME,new String[]{dbInfo.ID,dbInfo.TASK},
                dbInfo.ID+"=?",new String[]{String.valueOf(id)},null,null,null);
        if (cursor!=null)
            cursor.moveToFirst();
        TaskItems item=new TaskItems();
        item.setId(Integer.parseInt(cursor.getString(0)));
        item.setTask(cursor.getString(1));
        return item;
    }
    public List<TaskItems> getAll(){
        SQLiteDatabase db=this.getReadableDatabase();
        List<TaskItems> itemsList=new ArrayList<>();
        Cursor cursor=db.query(dbInfo.TABLE_NAME,new String[]{dbInfo.ID,dbInfo.TASK},null,null,null,null,dbInfo.ID+" DESC");
        if(cursor.moveToFirst()){
            do {
                TaskItems item=new TaskItems();
                item.setId(Integer.parseInt(cursor.getString(cursor.getColumnIndex(dbInfo.ID))));
                item.setTask(cursor.getString(cursor.getColumnIndex(dbInfo.TASK)));
                itemsList.add(item);
            }while (cursor.moveToNext());
        }
        return itemsList;
    }
    public void deleteTask(String tsk){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(dbInfo.TABLE_NAME, dbInfo.TASK + " = ?",new String[]{tsk});
        db.close();
    }
    //Get count
    public int getCount() {
        String countQuery = "SELECT * FROM " + dbInfo.TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);

        return cursor.getCount();
    }
}
