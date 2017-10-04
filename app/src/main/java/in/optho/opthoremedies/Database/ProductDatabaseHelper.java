package in.optho.opthoremedies.Database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;

import in.optho.opthoremedies.Models.Product;


/**
 * Created by krishna on 1/10/17.
 */


public class ProductDatabaseHelper extends SQLiteOpenHelper {

    public static final String TAG = "OPTHO";

    public static final String DATABASE_NAME = "Optho.db";
    public static final String DATABASE_PATH = "xxxxxxxxxxxxxxxxxx";
    public static final String TABLE_NAME = "product_table";

    private Context context;
    private SQLiteDatabase database;


    public ProductDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
        this.context = context;

    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.i(TAG, "onCreate: onCreate");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        Log.i(TAG, "onCreate: onUpgrade");

    }



    public void openDatabase(){
        String dbPath = context.getDatabasePath(DATABASE_PATH).getPath();
        if(database != null && database.isOpen()){
            return;
        }
        database = SQLiteDatabase.openDatabase(dbPath, null, SQLiteDatabase.OPEN_READWRITE);

    }

    public void closeDatabase(){
        if(database!=null)
            database.close();
    }




    public ArrayList<Product> getProductList(){

        Product product = null;
        ArrayList<Product> prodList = new ArrayList<>();
        openDatabase();

        Cursor cursor = database.rawQuery("select * from " + TABLE_NAME+";" , null);

        if(cursor.getCount()==0){
            Toast.makeText(context, "data not availabel", Toast.LENGTH_SHORT).show();
        }
        while (cursor.moveToNext()){
            product=new Product(cursor.getString(0),cursor.getString(1),cursor.getString(2),cursor.getString(3));
            prodList.add(product);
        }
        cursor.close();
        closeDatabase();

        return prodList;
    }


}

