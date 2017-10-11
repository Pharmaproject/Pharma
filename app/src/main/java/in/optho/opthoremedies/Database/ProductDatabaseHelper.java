package in.optho.opthoremedies.Database;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import com.loopj.android.http.Base64;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;

import in.optho.opthoremedies.Models.Product;

import static android.content.Context.MODE_PRIVATE;
import static android.net.Uri.decode;


/**
 * Created by krishna on 1/10/17.
 */


public class ProductDatabaseHelper extends SQLiteOpenHelper {

    public static final String TAG = "OPTHO";
    private static String DB_PATH = "/data/data/in.optho.opthoremedies/databases/";
    private static String DB_NAME = "optho.db";
    public static final String TABLE_NAME = "product";

    private SQLiteDatabase myDataBase;
    private final Context myContext;
    static int dbVersion = 2;

    private int id;
    private String code;
    private int priority;
    private String name;
    private int category;
    private int design;

    private String datetime;
    private int counter;

    /**
     * Constructor
     * Takes and keeps a reference of the passed context in order to access to the application assets and resources.
     * @param context
     */
    public ProductDatabaseHelper(Context context) {

        super(context, DB_NAME, null, dbVersion);
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

                copyDataBase();

            } catch (IOException e) {

                throw new Error("Error copying database");

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
        InputStream myInput = myContext.getAssets().open(DB_NAME);

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

        openDataBase();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " +TABLE_NAME );

        onCreate(db);


    }

    // Add your public helper methods to access and get content from the database.
    // You could return cursors by doing "return myDataBase.query(....)" so it'd be easy
    // to you to create adapters for your views.



    public ArrayList<Product> getProductList(){

        Product product = null;
        ArrayList<Product> prodList = new ArrayList<>();
        openDataBase();

        Cursor cursor = myDataBase.rawQuery("select datetime,id,code,priority,name,category,design from " + TABLE_NAME+";" , null);
        SharedPreferences storeddata = myContext.getSharedPreferences("myPrefs", MODE_PRIVATE);
        SharedPreferences.Editor edit = storeddata.edit();

        if(cursor.getCount()==0){
            Toast.makeText(myContext, "Empty Database", Toast.LENGTH_SHORT).show();

            return prodList;
        }
        while (cursor.moveToNext()) {
            datetime = cursor.getString(0);
            id = cursor.getInt(1);
            code = cursor.getString(2);
            priority = cursor.getInt(3);
            name = cursor.getString(4);
            category = cursor.getInt(5);
            design = cursor.getInt(6);

            product=new Product(datetime,id,code,priority,name ,category,design,storeddata.getInt(String.valueOf(id),0));
            prodList.add(product);
        }


        cursor.close();
        close();

        //z
        return prodList;
    }

    public void insertUpdateProduct(HashMap<String, String> queryValues) {
        openDataBase();

        Cursor cursor = myDataBase.rawQuery("select * from " +TABLE_NAME+" where id="+ queryValues.get("id") +";" , null);
        ContentValues values = new ContentValues();
        values.put("datetime", queryValues.get("datetime"));
        values.put("code", queryValues.get("code"));
        values.put("name", queryValues.get("name"));
        values.put("priority", queryValues.get("priority"));
        values.put("category", queryValues.get("category"));
        values.put("design", queryValues.get("design"));
        values.put("brand", Base64.decode(queryValues.get("brand"), Base64.DEFAULT));
        values.put("openpunch", Base64.decode(queryValues.get("openpunch"), Base64.DEFAULT));
        values.put("graphic", Base64.decode(queryValues.get("graphic"), Base64.DEFAULT));
        values.put("carton", Base64.decode(queryValues.get("carton"), Base64.DEFAULT));
        values.put("indication", Base64.decode(queryValues.get("indication"), Base64.DEFAULT));
        values.put("description", Base64.decode(queryValues.get("description"), Base64.DEFAULT));
        values.put("closepunch", Base64.decode(queryValues.get("closepunch"), Base64.DEFAULT));
        values.put("customicon", Base64.decode(queryValues.get("customicon"), Base64.DEFAULT));

          if((cursor.getCount()==0)){
              values.put("id", queryValues.get("id"));
              myDataBase.insert("product", null, values);
        }
        else {
            // TODO  SQL Update record
            myDataBase.update("product", values, "id=" + queryValues.get("id"), null);

        }
        myDataBase.close();


    }

    /**
     * Get list of Users from SQLite DB as Array List
     * @return
     */
    public ArrayList<HashMap<String, String>> getAllUsers() {
        ArrayList<HashMap<String, String>>employeeList;
        employeeList = new ArrayList<HashMap<String, String>>();
        String selectQuery = "SELECT  * FROM "+TABLE_NAME +";";
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                HashMap<String, String> map = new HashMap<String, String>();
                map.put("id", cursor.getString(0));
                map.put("pin", cursor.getString(1));
                employeeList.add(map);
            } while (cursor.moveToNext());
        }
        database.close();
        return employeeList;
    }


    public byte[] getBrand(int id){
        byte[] brand=null;
        openDataBase();
        Cursor cursor = myDataBase.rawQuery("select brand from " + TABLE_NAME+" where id = "+id+" ;" , null);
        if(cursor.getCount()==0){
            brand=null;
        }
        else {
            while (cursor.moveToNext()) {
                brand = cursor.getBlob(0);
            }
        }
        cursor.close();
        close();

        return brand;
    }

    public byte[] getOpenpunch(int id){
        byte[] opl=null;
        openDataBase();
        Cursor cursor = myDataBase.rawQuery("select openpunch from " + TABLE_NAME+" where id = "+id+" ;" , null);
        if(cursor.getCount()==0){
            opl=null;
        }
        else {
            while (cursor.moveToNext()) {
                opl = cursor.getBlob(0);
            }
        }
        cursor.close();
        close();
        return opl;
    }

    public byte[] getGraphic(int id){
        byte[] a=null;
        openDataBase();
        Cursor cursor = myDataBase.rawQuery("select graphic from " + TABLE_NAME+" where id = "+id+" ;" , null);
        if(cursor.getCount()==0){
            a=null;
        }
        else {
            while (cursor.moveToNext()) {
                a = cursor.getBlob(0);
            }
        }
        cursor.close();
        close();
        return a;
    }

    public byte[] getCarton(int id){
        byte[] a=null;
        openDataBase();
        Cursor cursor = myDataBase.rawQuery("select carton from " + TABLE_NAME+" where id = "+id+" ;" , null);
        if(cursor.getCount()==0){
            a=null;
        }
        else {
            while (cursor.moveToNext()) {
                a = cursor.getBlob(0);
            }
        }
        cursor.close();
        close();
        return a;
    }

    public byte[] getIndication(int id){
        byte[] a=null;
        openDataBase();
        Cursor cursor = myDataBase.rawQuery("select indication from " + TABLE_NAME+" where id = "+id+" ;" , null);
        if(cursor.getCount()==0){
            a=null;
        }
        else {
            while (cursor.moveToNext()) {
                a = cursor.getBlob(0);
            }
        }
        cursor.close();
        close();
        return a;
    }

    public byte[] getDesc(int id){
        byte[] a=null;
        openDataBase();
        Cursor cursor = myDataBase.rawQuery("select description from " + TABLE_NAME+" where id = "+id+" ;" , null);
        if(cursor.getCount()==0){
            a=null;
        }
        else {
            while (cursor.moveToNext()) {
                a = cursor.getBlob(0);
            }
        }
        cursor.close();
        close();
        return a;
    }

    public byte[] getClosepunch(int id){
        byte[] a=null;
        openDataBase();
        Cursor cursor = myDataBase.rawQuery("select closepunch from " + TABLE_NAME+" where id = "+id+" ;" , null);
        if(cursor.getCount()==0){
            a=null;
        }
        else {
            while (cursor.moveToNext()) {
                a = cursor.getBlob(0);
            }
        }
        cursor.close();
        close();
        return a;
    }

    public byte[] getCustomicon(int id){
        byte[] a=null;
        openDataBase();
        Cursor cursor = myDataBase.rawQuery("select customicon from " + TABLE_NAME+" where id = "+id+" ;" , null);
        if(cursor.getCount()==0){
            a=null;
        }
        else {
            while (cursor.moveToNext()) {
                a = cursor.getBlob(0);
            }
        }
        cursor.close();
        close();
        return a;
    }





}

