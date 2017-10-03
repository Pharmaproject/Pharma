package in.optho.opthoremedies;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;


/**
 * Created by krishna on 1/10/17.
 */


public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String TAG = "OPTHO";


    public static final String DATABASE_NAME = "Employee.db";
    public static final String TABLE_NAME = "employee_table";
    public static final String ID = "ID";
    public static final String PIN = "PIN";

    private SharedPreferences sharedpreferences;

    Context context;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);

        this.context = context;

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.i(TAG, "onCreate: onCreate");
        db.execSQL("create table " + TABLE_NAME + " (ID text primary key, pin text)");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        Log.i(TAG, "onCreate: onUpgrade");
//        db.execSQL("drop table if exists '" + TABLE_NAME + "'");
//        onCreate(db);
    }

    public boolean insterData(String id, String pin) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(ID, id);
        contentValues.put(PIN, pin);

        long result = db.insert(TABLE_NAME, null, contentValues);
        Log.i(TAG, "insterData: " + result);

        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }

    public void dropTable(){

        
        SQLiteDatabase db = this.getWritableDatabase();
        
        if(db!=null)
            db.execSQL("drop table if exists "+TABLE_NAME);
        else
            Log.i(TAG, "dropTable: Table does not exist");
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putBoolean("dataIsSaved", false);
        editor.commit();



    }
    public String getPin(String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        String pin="invalid";

        Cursor res = db.rawQuery("select * from " + TABLE_NAME+" where id="+id+";" , null);

        if(res.getCount()==0){
            Toast.makeText(context, "No such data found!", Toast.LENGTH_SHORT).show();
        }
        while (res.moveToNext()){
            pin = res.getString(1);
//            Toast.makeText(context, "Pin: "+pin, Toast.LENGTH_SHORT).show();
        }

        return pin;
    }

    public boolean checkId(String id){
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor res = db.rawQuery("select * from " + TABLE_NAME+" where id="+id+";" , null);
        if(res.getCount()==0)
            return false;
        else
            return true;

    }


}

