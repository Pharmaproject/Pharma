package in.optho.opthoremedies.Activities;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.Dialog;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
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
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

import cz.msebera.android.httpclient.Header;
import in.optho.opthoremedies.Database.EmployeeDatabaseHelper;
import in.optho.opthoremedies.R;
import in.optho.opthoremedies.Service.SampleBC;

public class SplashActivity extends AppCompatActivity {

// DB Class to perform DB related operations
    // Progress Dialog Object
    ProgressDialog prgDialog;
    SharedPreferences storeddata;
    SharedPreferences.Editor edit;
    HashMap<String, String> queryValues;
    EmployeeDatabaseHelper controller;
    int remain;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        controller = new EmployeeDatabaseHelper(this);

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

       // alarmManager.cancel(pendingIntent); // cancel any existing alarms
       // alarmManager.setInexactRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime() + AlarmManager.INTERVAL_DAY, AlarmManager.INTERVAL_DAY, pendingIntent);

        storeddata = getSharedPreferences("myPrefs", MODE_PRIVATE);
        edit = storeddata.edit();

        final Boolean updateEmp = storeddata.getBoolean("updateEmp", false);
        final Boolean updatePro = storeddata.getBoolean("updatePro", false);
        int Days = storeddata.getInt("update",0);
        remain=11-Days;
        final Dialog syncDialog = new Dialog(this,R.style.NewDialog);
        syncDialog.setContentView(R.layout.syncdialog);
        final Button okBtn = (Button) syncDialog.findViewById(R.id.Sync);
        final Button cancelBtn = (Button) syncDialog.findViewById(R.id.Skip);



        Window window = syncDialog.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();

        wlp.gravity = Gravity.BOTTOM;
        wlp.flags &= ~WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        window.setAttributes(wlp);

        if(remain<11) {
            cancelBtn.setText("Skip (" + remain + " Days Remaining)");
        }
        if(remain<1){
            cancelBtn.setEnabled(false);
        }

        okBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if(updateEmp) {
                    syncSQLiteEmployee();
                }
                //                       else syncSQLiteProduct();
        //        syncSQLiteEmployee();
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

        syncDialog.create();


        if(updateEmp|updatePro) {
            syncDialog.show();

        }
        else{
           NextActivity();
        }




    }

    // Method to Sync MySQL to SQLite DB
    public void syncSQLiteEmployee() {
        // Create AsycHttpClient object
        System.out.println("request for updates");

        prgDialog.show();
        String json = storeddata.getString("dateEmp", "");
        AsyncHttpClient client = new AsyncHttpClient();
        // Http Request Params Object
        RequestParams params = new RequestParams();
        // Show ProgressBar
        params.put("dateEmp", json);
        // Make Http call to getemployee.php
        client.post("http://obligo.in/optho/getemployee.php", params, new JsonHttpResponseHandler() {
            @Override
            public void onStart() {
                System.out.println("Checking for updates");
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                // If the response is JSONObject instead of expected JSONArray
                prgDialog.hide();
                try {
                    JSONArray jarr = response.getJSONArray("value");
                    updateSQLiteEmployee(jarr);
                }catch (JSONException e){
                    e.printStackTrace();

                }
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray timeline) {

                    prgDialog.hide();
                    // Update SQLite DB with response sent by getusers.php
                    updateSQLiteEmployee(timeline);
                }


            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                // called when response HTTP status is "4XX" (eg. 401, 403, 404)
                // TODO Auto-generated method stub
                prgDialog.hide();
                if (statusCode == 404) {
                    Toast.makeText(getApplicationContext(), "Requested resource not found", Toast.LENGTH_LONG).show();
                } else if (statusCode == 500) {
                    Toast.makeText(getApplicationContext(), "Something went wrong at server end", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getApplicationContext(), "Unexpected Error occcured! [Most common Error: Device might not be connected to Internet]",
                            Toast.LENGTH_LONG).show();
                }

                if (remain > 0) {
                    NextActivity();
                }
            }

            @Override
            public void onRetry(int retryNo) {
                // called when request is retried
                System.out.println("Sync was retried");

            }
            @Override
            public void onFinish() {
                System.out.println("Sync Finished");

            }
            @Override
            public void onProgress(long bytesWritten, long totalSize) {
                // Progress notification
                int progress= (int)(bytesWritten/totalSize);
                prgDialog.setProgress(progress);
            }


        });
    }

    public void updateSQLiteEmployee(JSONArray response){


        try {
            // Extract JSON array from the response
            System.out.println(response.length());
            // If no of array elements is not zero
            if(response.length() != 0){
                // Loop through each array element, get JSON object which has userid and username
                for (int i = 0; i < response.length(); i++) {
                    // Get JSON object
                    JSONObject obj = (JSONObject) response.get(i);
                    System.out.println(obj.get("id"));
                    System.out.println(obj.get("pin"));
                    // DB QueryValues Object to insert into SQLite
                    queryValues = new HashMap<String, String>();
                    // Add ID extracted from Object
                    queryValues.put("id", obj.get("id").toString());
                    queryValues.put("pin", obj.get("pin").toString());
                    queryValues.put("lock", obj.get("lock").toString());

                    // Insert User into SQLite DB
                    controller.insertUpdateUser(queryValues);

                }
                String DateEmpServer=storeddata.getString("DateEmpServer","");
                edit.putString("dateEmp",DateEmpServer);
                edit.putBoolean("updateEmp", false);

/*              After Product Sync
                String DateProServer=storeddata.getString("DateProServer","");
                edit.putString("datePro",DateProServer);
                edit.putBoolean("updatePro", false);
*/

                edit.commit();

                // load the Main Activity
                NextActivity();
            }
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }



    // Reload MainActivity
    public void NextActivity() {
        startActivity(new Intent(SplashActivity.this, IDActivity.class));
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }

    public boolean isConnected(){
            ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Activity.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

                return networkInfo != null && networkInfo.isConnected();
            }

}
