package in.optho.opthoremedies.SessionHelper;

/**
 * Created by Anveshak on 1-10-17.
 */

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.HashMap;

public class SQLiteHandler extends SQLiteOpenHelper {

    private static final String TAG = SQLiteHandler.class.getSimpleName();

    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "android_api.db";

    // Login table name
    private static final String TABLE_USER = "user";

    // Login Table Columns names
    private static final String KEY_ID = "id";

    public SQLiteHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase iddb) {
        String CREATE_LOGIN_TABLE = "CREATE TABLE " + TABLE_USER + "("
                + KEY_ID + " TEXT PRIMARY KEY"  + ")";
        iddb.execSQL(CREATE_LOGIN_TABLE);

        Log.d(TAG, "Database tables created");
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase iddb, int oldVersion, int newVersion) {
        // Drop older table if existed
        iddb.execSQL("DROP TABLE IF EXISTS " + TABLE_USER);

        // Create tables again
        onCreate(iddb);
    }

    /**
     * Storing user details in database
     * */
    public void addUser(String ID) {
        SQLiteDatabase iddb = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_ID, ID); // Name

        // Inserting Row
        long id = iddb.insert(TABLE_USER, null, values);
        iddb.close(); // Closing database connection

        Log.d(TAG, "New user inserted into sqlite: " + id);
    }

    /**
     * Getting user data from database
     * */
    public String getUserDetails() {
        String user="null";
        String selectQuery = "SELECT  * FROM " + TABLE_USER +";";

        SQLiteDatabase iddb = this.getReadableDatabase();
        Cursor cursor = iddb.rawQuery(selectQuery, null);
        // Move to first row

        while (cursor.moveToNext()) {
            user = cursor.getString(cursor.getColumnIndex(KEY_ID));

        }
        cursor.close();
        iddb.close();
        // return user
        Log.d(TAG, "Fetching user from Sqlite: " + user);

        return user;
    }

    /**
     * Re crate database Delete all tables and create them again
     * */
    public void deleteUsers() {
        SQLiteDatabase iddb = this.getWritableDatabase();
        // Delete All Rows
        iddb.delete(TABLE_USER, null, null);
        iddb.close();

        Log.d(TAG, "Deleted all user info from sqlite");
    }

}