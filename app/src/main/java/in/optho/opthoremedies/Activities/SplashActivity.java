package in.optho.opthoremedies.Activities;

import android.app.AlarmManager;
import android.app.Dialog;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

import in.optho.opthoremedies.Database.EmployeeDatabaseHelper;
import in.optho.opthoremedies.R;
import in.optho.opthoremedies.Service.SampleBC;

public class SplashActivity extends AppCompatActivity {

// DB Class to perform DB related operations
    // Progress Dialog Object
    ProgressDialog prgDialog;
    SharedPreferences pref;
    HashMap<String, String> queryValues;
    EmployeeDatabaseHelper controller;
    int count =0;
    int days=10;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        controller = new EmployeeDatabaseHelper(this);

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {





            }
        }, 1000);





        // Get User records from SQLite DB
        ArrayList<HashMap<String, String>> userList = controller.getAllUsers();
        // If users exists in SQLite DB
        if (userList.size() != 0) {
            // Set the User Array list in ListView


        }
        // Initialize Progress Dialog properties
        prgDialog = new ProgressDialog(this);
        prgDialog.setMessage("Transferring Data from Remote MySQL DB and Syncing SQLite. Please wait...");
        prgDialog.setCancelable(false);
        // BroadCase Receiver Intent Object
        Intent alarmIntent = new Intent(getApplicationContext(), SampleBC.class);
        // Pending Intent Object
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 0, alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        // Alarm Manager Object
        AlarmManager alarmManager = (AlarmManager) getApplicationContext().getSystemService(Context.ALARM_SERVICE);
        // Alarm Manager calls BroadCast for every Ten seconds (10 * 1000), BroadCase further calls service to check if new records are inserted in
        // Remote MySQL DB
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, Calendar.getInstance().getTimeInMillis() + 5000, 10 * 1000, pendingIntent);

        alarmManager.cancel(pendingIntent); // cancel any existing alarms
        alarmManager.setInexactRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime() + AlarmManager.INTERVAL_DAY, AlarmManager.INTERVAL_DAY, pendingIntent);

        pref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);

        count = pref.getInt("count_key",count);

        days=10-count;
        final Dialog syncDialog = new Dialog(this,R.style.NewDialog);
        syncDialog.setContentView(R.layout.syncdialog);
        final Button okBtn = (Button) syncDialog.findViewById(R.id.Sync);
        final Button cancelBtn = (Button) syncDialog.findViewById(R.id.Skip);


        cancelBtn.setText("Skip ("+days+" Days Remaining)");

        if(days<1){
            cancelBtn.setEnabled(false);
        }

        syncDialog.create();

        Window window = syncDialog.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();

        wlp.gravity = Gravity.BOTTOM;
        wlp.flags &= ~WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        window.setAttributes(wlp);


                syncDialog.show();
                okBtn.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {

                        syncSQLiteMySQLDB();
                        syncDialog.dismiss();
                    }
                });
                cancelBtn.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {

                        NextActivity();
                        syncDialog.dismiss();
                    }
                });




    }

    // Method to Sync MySQL to SQLite DB
    public void syncSQLiteMySQLDB() {
        // Create AsycHttpClient object
        AsyncHttpClient client = new AsyncHttpClient();
        // Http Request Params Object
        RequestParams params = new RequestParams();
        // Show ProgressBar
        prgDialog.show();
        // Make Http call to getusers.php
        client.post("http://192.168.2.4:9000/mysqlsqlitesync/getusers.php", params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(String response) {
                // Hide ProgressBar
                prgDialog.hide();
                // Update SQLite DB with response sent by getusers.php
                updateSQLite(response);
            }
            // When error occured
            @Override
            public void onFailure(int statusCode, Throwable error, String content) {
                // TODO Auto-generated method stub
                // Hide ProgressBar
                prgDialog.hide();
                if (statusCode == 404) {
                    Toast.makeText(getApplicationContext(), "Requested resource not found", Toast.LENGTH_LONG).show();
                } else if (statusCode == 500) {
                    Toast.makeText(getApplicationContext(), "Something went wrong at server end", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getApplicationContext(), "Unexpected Error occcured! [Most common Error: Device might not be connected to Internet]",
                            Toast.LENGTH_LONG).show();
                }

                if(days>0){
                    NextActivity();
                }
            }
        });
    }

    public void updateSQLite(String response){
        ArrayList<HashMap<String, String>> usersynclist;
        usersynclist = new ArrayList<HashMap<String, String>>();
        // Create GSON object
        Gson gson = new GsonBuilder().create();
        try {
            // Extract JSON array from the response
            JSONArray arr = new JSONArray(response);
            System.out.println(arr.length());
            // If no of array elements is not zero
            if(arr.length() != 0){
                // Loop through each array element, get JSON object which has userid and username
                for (int i = 0; i < arr.length(); i++) {
                    // Get JSON object
                    JSONObject obj = (JSONObject) arr.get(i);
                    System.out.println(obj.get("id"));
                    System.out.println(obj.get("pin"));
                    // DB QueryValues Object to insert into SQLite
                    queryValues = new HashMap<String, String>();
                    // Add userID extracted from Object
                    queryValues.put("id", obj.get("id").toString());
                    // Add userName extracted from Object
                    queryValues.put("pin", obj.get("pin").toString());
                    // Insert User into SQLite DB
                    controller.insertUser(queryValues);
                    HashMap<String, String> map = new HashMap<String, String>();
                    // Add status for each User in Hashmap
                    map.put("id", obj.get("id").toString());
                    map.put("status", "1");
                    usersynclist.add(map);
                }
                SharedPreferences.Editor editor = pref.edit();
                editor.putInt("count_key", 0);
                editor.commit();
                // Inform Remote MySQL DB about the completion of Sync activity by passing Sync status of Users
                updateMySQLSyncSts(gson.toJson(usersynclist));
                // Reload the Main Activity
                NextActivity();
            }
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    // Method to inform remote MySQL DB about completion of Sync activity
    public void updateMySQLSyncSts(String json) {
        System.out.println(json);
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.put("syncsts", json);
        // Make Http call to updatesyncsts.php with JSON parameter which has Sync statuses of Users
        client.post("http://192.168.2.4:9000/mysqlsqlitesync/updatesyncsts.php", params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(String response) {
                Toast.makeText(getApplicationContext(),	"MySQL DB has been informed about Sync activity", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFailure(int statusCode, Throwable error, String content) {
                Toast.makeText(getApplicationContext(), "Error Occured", Toast.LENGTH_LONG).show();
            }
        });
    }

    // Reload MainActivity
    public void NextActivity() {
        startActivity(new Intent(SplashActivity.this, IDActivity.class));
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }

}
