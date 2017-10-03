package in.optho.opthoremedies.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;


/**
 * Created by krishna on 1/10/17.
 */


public class ProductDatabaseHelper extends SQLiteOpenHelper {

    public static final String TAG = "OPTHO";


    public static final String DATABASE_NAME = "Employee.db";
    public static final String TABLE_NAME = "product_table";
    public static final String SNO = "sno";
    public static final String NAME = "name";

    Context context;

    public ProductDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
        this.context = context;



    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.i(TAG, "onCreate: onCreate");
        db.execSQL("create table " + TABLE_NAME + " (SNO INTEGER PRIMARY KEY AUTOINCREMENT, "+NAME+" text)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        Log.i(TAG, "onCreate: onUpgrade");
        db.execSQL("drop table if exists '" + TABLE_NAME + "'");
        onCreate(db);
    }

    public boolean insertData(String name) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(NAME, name);

        long result = db.insert(TABLE_NAME, null, contentValues);
        Log.i(TAG, "insertData: " + result);

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

    }

    public ArrayList<String > getProductList(){

        ArrayList<String> prodList = new ArrayList<>();

        SQLiteDatabase db = this.getWritableDatabase();

        Cursor res = db.rawQuery("select * from " + TABLE_NAME+";" , null);

        if(res.getCount()==0){
            Toast.makeText(context, "data not availabel", Toast.LENGTH_SHORT).show();
        }
        while (res.moveToNext()){
             prodList.add(res.getString(1));;
//            Toast.makeText(context, "Pin: "+pin, Toast.LENGTH_SHORT).show();
        }

        return prodList;
    }

    /*
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
*/

    /*public boolean checkId(String id){
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor res = db.rawQuery("select * from " + TABLE_NAME+" where id="+id+";" , null);
        if(res.getCount()==0)
            return false;
        else
            return true;

    }
*/

}

