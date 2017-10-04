package in.optho.opthoremedies.Database;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

import in.optho.opthoremedies.Models.Employee;


/**
 * Created by krishna on 1/10/17.
 */


public class EmployeeDatabaseHelper extends SQLiteOpenHelper {

    public static final String TAG = "OPTHO";
    private final Context myContext;

    //The Android's default system path of your application database.
    private static String DB_PATH;
    private static String DB_NAME = "optho.db";
    public static final String TABLE_NAME = "employee";

    private SQLiteDatabase myDataBase;



    public EmployeeDatabaseHelper(Context context) {

        super(context, DB_NAME, null, 1);
        this.myContext = context;
        DB_PATH = context.getApplicationInfo().dataDir + "/databases/";



    }

    /**
     * Creates a empty database on the system and rewrites it with your own database.
     * */
    public void createDataBase() throws IOException {

        boolean dbExist = checkDataBase();

        if(dbExist){
            //do nothing - database already exist
        }else{

            //By calling this method and empty database will be created into the default system path
            //of your application so we are gonna be able to overwrite that database with our database.
            this.getReadableDatabase();

            try {
                Log.i("Database",
                        "New database is being copied to device!");
                copyDataBase();

            } catch (IOException e) {

                throw new Error("Error copying database "+e);

            }
        }

    }

    /**
     * Check if the database already exist to avoid re-copying the file each time you open the application.
     * @return true if it exists, false if it doesn't
     */
    private boolean checkDataBase(){

        SQLiteDatabase checkDB = null;

        try{
            String myPath = DB_PATH + DB_NAME;
            checkDB = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);

        }catch(SQLiteException e){

            //database does't exist yet.

        }

        if(checkDB != null){

            checkDB.close();

        }

        return checkDB != null ? true : false;
    }

    /**
     * Copies your database from your local assets-folder to the just created empty database in the
     * system folder, from where it can be accessed and handled.
     * This is done by transfering bytestream.
     * */
    private void copyDataBase() throws IOException{

        //Open your local db as the input stream
        InputStream myInput = myContext.getAssets().open("databases/"+DB_NAME);

        // Path to the just created empty db
        String outFileName = DB_PATH + DB_NAME;

        //Open the empty db as the output stream
        OutputStream myOutput = new FileOutputStream(outFileName);

        //transfer bytes from the inputfile to the outputfile
        byte[] buffer = new byte[1024];
        int length;
        while ((length = myInput.read(buffer))>0){
            myOutput.write(buffer, 0, length);
        }

        //Close the streams
        myOutput.flush();
        myOutput.close();
        myInput.close();

    }

    public void openDataBase() throws SQLException {

        try
        {
            createDataBase();
        }
        catch (IOException ex)
        {
            Toast.makeText(myContext, "data not available"+ex, Toast.LENGTH_SHORT).show();
        }

        //Open the database
        String myPath = DB_PATH + DB_NAME;
        myDataBase = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READWRITE);

    }

    @Override
    public synchronized void close() {

        if(myDataBase != null)
            myDataBase.close();

        super.close();

    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    // Add your public helper methods to access and get content from the database.
    // You could return cursors by doing "return myDataBase.query(....)" so it'd be easy
    // to you to create adapters for your views.


    public String getPin(String id){

        String pin = null;
        ArrayList<Employee> employeeList = new ArrayList<>();
        openDataBase();

        Cursor cursor = myDataBase.rawQuery("select pin from " + TABLE_NAME+" where id="+ id+";" , null);

        if(cursor.getCount()==0){
            Toast.makeText(myContext, "data not availabel", Toast.LENGTH_SHORT).show();
        }
        while (cursor.moveToNext()){
            pin=cursor.getString(0);

        }
        cursor.close();
        close();

        return pin;
    }

    public Boolean checkID(String id){

        Boolean emp = null;
        openDataBase();

        Cursor cursor = myDataBase.rawQuery("select * from " + TABLE_NAME+" where id="+ id +";" , null);

        if(cursor.getCount()==0){
            Toast.makeText(myContext, "ID doesn't exist", Toast.LENGTH_SHORT).show();
            emp= false;
        }
        else {
            emp=true;
        }
        cursor.close();
        close();
        return emp;
    }

}

