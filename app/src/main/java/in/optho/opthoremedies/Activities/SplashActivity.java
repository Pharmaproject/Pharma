package in.optho.opthoremedies.Activities;

import android.app.AlarmManager;
import android.app.Dialog;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.HashMap;

import cz.msebera.android.httpclient.Header;
import in.optho.opthoremedies.Database.EmployeeDatabaseHelper;
import in.optho.opthoremedies.Database.ProductDatabaseHelper;
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
    ProductDatabaseHelper controller2;
    Dialog syncDialog;
    int remain;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // hide nav bar
                        | View.SYSTEM_UI_FLAG_FULLSCREEN // hide status bar
                        | View.SYSTEM_UI_FLAG_IMMERSIVE);

        controller = new EmployeeDatabaseHelper(this);
        controller2 = new ProductDatabaseHelper(this);

        // Initialize Progress Dialog properties
        prgDialog = new ProgressDialog(this);
        prgDialog.setMessage("Transferring Data from Remote Server and Syncing Local Database. Please wait...");
        prgDialog.setCancelable(false);
        // BroadCase Receiver Intent Object
        Intent alarmIntent = new Intent(getApplicationContext(), SampleBC.class);
        // Pending Intent Object
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 0, alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        // Alarm Manager Object
        AlarmManager alarmManager = (AlarmManager) getApplicationContext().getSystemService(Context.ALARM_SERVICE);
        // Alarm Manager calls BroadCast for every Ten seconds (10 * 1000), BroadCase further calls service to check if new records are inserted in
        // Remote MySQL DB
       // alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, Calendar.getInstance().getTimeInMillis() + 5000, 1000 * 60 * 10
   //             , pendingIntent);

   //     alarmManager.cancel(pendingIntent); // cancel any existing alarms
       alarmManager.setInexactRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime() + AlarmManager.INTERVAL_DAY, AlarmManager.INTERVAL_DAY, pendingIntent);

        storeddata = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        int noOfTimes = storeddata.getInt("Nonet", 0);
        System.out.println(storeddata.getAll());

        final Boolean updateEmp = storeddata.getBoolean("updateEmp", false);
        final Boolean updatePro = storeddata.getBoolean("updatePro", false);
        int Days = storeddata.getInt("update",0);

        remain=11-Days;
        syncDialog = new Dialog(this,R.style.NewDialog);
        syncDialog.setContentView(R.layout.syncdialog);
        final Button okBtn = (Button) syncDialog.findViewById(R.id.Sync);
        final Button cancelBtn = (Button) syncDialog.findViewById(R.id.Skip);
        String DateEmpLocal = storeddata.getString("dateEmp", "2017-10-06 00:00:00");
        String DateProLocal = storeddata.getString("datePro", "2017-10-06 00:00:00");

        System.out.println("Local Emp Date: "+ DateEmpLocal);
        System.out.println("Local Product Date: "+ DateProLocal);

        int Fresh = storeddata.getInt("FirstLaunch",0);
        if(Fresh==0){
            syncSQLiteEmployee();
            syncSQLiteProduct();
            edit = storeddata.edit();
            edit.putInt("FirstLaunch",1);
            edit.commit();
        }
        Window window = syncDialog.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();

        wlp.gravity = Gravity.BOTTOM;
        wlp.flags &= ~WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        window.setAttributes(wlp);

        if(remain<11) {
            cancelBtn.setText("Skip (" + remain + " Days Remaining)");
        }
        if(remain<1||noOfTimes>30){
            okBtn.setTextSize(20);
            cancelBtn.setEnabled(false);
            cancelBtn.setText("App Locked");
            cancelBtn.setTextColor(getResources().getColor(R.color.warning_color));
            wlp.gravity = Gravity.CENTER;

        }

        okBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if(updateEmp) {
                    syncDialog.dismiss();
                    syncSQLiteEmployee();
                }
                else syncSQLiteProduct();
        //        syncSQLiteEmployee();

            }
        });
        cancelBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                NextActivity();
                syncDialog.dismiss();
            }
        });
        syncDialog.setCanceledOnTouchOutside(false);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            syncDialog.create();

        }



        if(updateEmp|updatePro) {
            syncDialog.show();

        }
        else{
           NextActivity();
        }




    }

    // TODO : Method to Sync MySQL to EMPLOYEE DATABASE
    public void syncSQLiteEmployee() {
        // Create AsycHttpClient object
        System.out.println("request for sync");

        prgDialog.show();
        String json = storeddata.getString("dateEmp", "2017-10-06 00:00:00");
        AsyncHttpClient client = new AsyncHttpClient();
        // Http Request Params Object
        RequestParams params = new RequestParams();
        // Show ProgressBar
        System.out.println("Fetch new Employee records earlier than "+json);
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
                try {
                    JSONArray jarr = response.getJSONArray("value");
                    updateSQLiteEmployee(jarr);
                }catch (JSONException e){
                    e.printStackTrace();

                }
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray timeline) {


                    // Update SQLite DB with response sent by getusers.php
                    updateSQLiteEmployee(timeline);
                }


            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                // called when response HTTP status is "4XX" (eg. 401, 403, 404)
                // TODO Auto-generated method stub
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
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                System.out.println("Failure: "+errorResponse);

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
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {

                System.out.println("Failure: "+responseString);

                if (statusCode == 404) {
                    Toast.makeText(getApplicationContext(), "Requested resource not found" +responseString, Toast.LENGTH_LONG).show();
                } else if (statusCode == 500) {
                    Toast.makeText(getApplicationContext(), "Something went wrong at server end" +responseString, Toast.LENGTH_LONG).show();
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
                prgDialog.hide();

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
                    System.out.println(obj.get("datetime"));
                    // DB QueryValues Object to insert into SQLite
                    queryValues = new HashMap<String, String>();
                    // Add ID extracted from Object
                    queryValues.put("id", obj.get("id").toString());
                    queryValues.put("pin", obj.get("pin").toString());
                    queryValues.put("lock", obj.get("lock").toString());
                    queryValues.put("datetime", obj.get("datetime").toString());
                    // Insert User into SQLite DB
                    controller.insertUpdateUser(queryValues);

                }
                String DateEmpServer=storeddata.getString("DateEmpServer","");
                System.out.println("Updating Local Emp Date: "+DateEmpServer);
                edit = storeddata.edit();
                edit.putString("dateEmp",DateEmpServer);
                edit.putBoolean("updateEmp", false);
                edit.putInt("update",0);
                edit.commit();
                System.out.println(storeddata.getAll());

                // load the Main Activity
                NextActivity();
            }
            else  Toast.makeText(getApplicationContext(), "Requested Data not received", Toast.LENGTH_LONG).show();

        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            System.out.println("Couldn't handle the JSONArray");

        }
    }

    // TODO : Method to Sync MySQL to PRODUCT DATABASE
    public void syncSQLiteProduct() {
        // Create AsycHttpClient object
        System.out.println("Fetch request for sync products");

        prgDialog.show();
        String json = storeddata.getString("datePro", "2017-10-06 00:00:00");
        AsyncHttpClient client = new AsyncHttpClient();
        // Http Request Params Object
        RequestParams params = new RequestParams();
        // Show ProgressBar
        System.out.println("New Products earlier than "+json);
        params.put("datePro", json);
        // Make Http call to get employee.php
        client.post("http://obligo.in/optho/getproduct.php", params, new JsonHttpResponseHandler() {
            @Override
            public void onStart() {
                System.out.println("Checking for updates");
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                // If the response is JSONObject instead of expected JSONArray
                try {
                    JSONArray jarr = response.getJSONArray("value");
                    updateSQLiteProduct(jarr);
                }catch (JSONException e){
                    e.printStackTrace();

                }
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray timeline) {


                // Update SQLite DB with response sent by getusers.php
                updateSQLiteProduct(timeline);
            }


            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                // called when response HTTP status is "4XX" (eg. 401, 403, 404)
                // TODO Auto-generated method stub
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
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                System.out.println("Failure: "+errorResponse);

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
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {

                System.out.println("Failure: "+responseString);

                if (statusCode == 404) {
                    Toast.makeText(getApplicationContext(), "Requested resource not found" +responseString, Toast.LENGTH_LONG).show();
                } else if (statusCode == 500) {
                    Toast.makeText(getApplicationContext(), "Something went wrong at server end" +responseString, Toast.LENGTH_LONG).show();
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
                prgDialog.hide();

            }
            @Override
            public void onProgress(long bytesWritten, long totalSize) {
                // Progress notification
                int progress= (int)(bytesWritten/totalSize);
                prgDialog.setProgress(progress);
            }


        });
    }

    public void updateSQLiteProduct(JSONArray response){


        try {
            // Extract JSON array from the response
            System.out.println(response.length());
            // If no of array elements is not zero
            if(response.length() != 0){
                // Loop through each array element, get JSON object which has userid and username
                for (int i = 0; i < response.length(); i++) {
                    // Get JSON object
                    JSONObject obj = (JSONObject) response.get(i);
                    System.out.println(obj.get("code"));
                    System.out.println(obj.get("datetime"));
                    // DB QueryValues Object to insert into SQLite
                    queryValues = new HashMap<String, String>();
                    // Add ID extracted from Object
                    queryValues.put("id", obj.get("id").toString());
                    queryValues.put("datetime", obj.get("datetime").toString());
                    queryValues.put("name", obj.get("name").toString());
                    queryValues.put("code", obj.get("code").toString());
                    queryValues.put("priority", obj.get("priority").toString());
                    queryValues.put("category", obj.get("category").toString());
                    queryValues.put("design", obj.get("design").toString());
                    queryValues.put("brand", obj.get("brand").toString());
                    queryValues.put("openpunch", obj.get("openpunch").toString());
                    queryValues.put("graphic", obj.get("graphic").toString());
                    queryValues.put("carton", obj.get("carton").toString());
                    queryValues.put("indication", obj.get("indication").toString());
                    queryValues.put("description", obj.get("description").toString());
                    queryValues.put("closepunch", obj.get("closepunch").toString());
                    queryValues.put("customicon", obj.get("customicon").toString());

                    // Insert product into SQLite DB
                    controller2.insertUpdateProduct(queryValues);

                }
                String DateProServer=storeddata.getString("DateProServer","");
                System.out.println("Updating Local Product Date: "+DateProServer);
                edit = storeddata.edit();
                edit.putString("datePro",DateProServer);
                edit.putBoolean("updatePro", false);
                edit.putInt("update",0);
                edit.commit();
                System.out.println(storeddata.getAll());

                // load the Main Activity
                NextActivity();
            }  else  Toast.makeText(getApplicationContext(), "Requested Data not received", Toast.LENGTH_LONG).show();

        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            System.out.println("Couldn't handle the JSONArray");

        }
    }




    // Reload MainActivity
    public void NextActivity() {
        syncDialog.dismiss();
        startActivity(new Intent(SplashActivity.this, IDActivity.class));
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }



}
