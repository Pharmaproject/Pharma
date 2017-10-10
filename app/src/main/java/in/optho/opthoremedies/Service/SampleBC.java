package in.optho.opthoremedies.Service;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import java.text.SimpleDateFormat;
import java.util.Date;


import cz.msebera.android.httpclient.Header;

import static android.content.Context.MODE_PRIVATE;

public class SampleBC extends BroadcastReceiver {
	static int noOfTimes = 0;
    boolean updateEmp=false;
    boolean updatePro=false;
    SharedPreferences storeddata;
    SharedPreferences.Editor edit;
    // Method gets called when Broad Case is issued from MainActivity for every 10 seconds
	@Override
	public void onReceive(final Context context, Intent intent) {
		// TODO Auto-generated method stub
        storeddata = context.getSharedPreferences("myPrefs", MODE_PRIVATE);
        edit = storeddata.edit();
        noOfTimes = storeddata.getInt("Nonet", 0);
        if(!isConnected(context)){
            noOfTimes++;
            Toast.makeText(context, "Warning: No Internet for checking updates. "+noOfTimes + " out of 30 days allowed", Toast.LENGTH_SHORT).show();
            if(noOfTimes>30){
                edit.putInt("Nonet",0);
            }
        }
        else {
            noOfTimes=0;
            edit.putInt("Nonet",noOfTimes);
        }
        edit.commit();

        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        // Checks if new records are inserted in Remote MySQL DB to proceed with Sync operation
        client.post("http://obligo.in/optho/getdbstatus.php",params ,new JsonHttpResponseHandler() {
            @Override
            public void onStart() {
                System.out.println("Checking for updates");
            }
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                // If the response is JSONObject instead of expected JSONArray
                processResponse(response,context);
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray timeline) {
                JSONObject response=new JSONObject();
                try {

                    response = timeline.getJSONObject(0);


                }
                catch (JSONException j){
                    j.printStackTrace();
                }

                // Do something with the response
                processResponse(response,context);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                // called when response HTTP status is "4XX" (eg. 401, 403, 404)
                // TODO Auto-generated method stub
                if(statusCode == 404){
                    Toast.makeText(context, "Optho update: Error 404", Toast.LENGTH_SHORT).show();
                }else if(statusCode == 500){
                    Toast.makeText(context, "Optho update: Error 500", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(context, "Optho update: Error "+statusCode, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onRetry(int retryNo) {
                // called when request is retried
                System.out.println("Request for update was retried");

            }
            @Override
            public void onFinish() {
                System.out.println("Checked for updates");

            }



        });
	}

	public void processResponse(JSONObject response,Context context){

        try {
            // Create JSON object out of the response sent by getdbrowcount.php
            String DateEmpServer= response.getString("dateEmp");
            String DateProServer= response.getString("datePro");

            System.out.println("Emp Server Date: "+DateEmpServer);
            System.out.println("Pro Server Date: "+DateProServer);

            String DateEmpLocal = storeddata.getString("dateEmp", "2017-10-06 00:00:00");
            String DateProLocal = storeddata.getString("datePro", "2017-10-06 00:00:00");

            updateEmp= CompareDates(DateEmpLocal,DateEmpServer);
            updatePro= CompareDates(DateProLocal,DateProServer);


            edit = storeddata.edit();

            if(updateEmp|updatePro){
                edit.putString("DateEmpServer",DateEmpServer);
                edit.putString("DateProServer",DateProServer);

                edit.putBoolean("updateEmp", updateEmp);
                edit.putBoolean("updatePro", updatePro);
                int Days = storeddata.getInt("update",0);
                edit.putInt("update",++Days);
                edit.commit();
                System.out.println("Days Remaining to Update  " + (11-Days));
                String notification = (11-Days)+ " Days Remaining to Update ";
                Toast.makeText(context,notification, Toast.LENGTH_LONG).show();

                final Intent intnt = new Intent(context, MyService.class);
                // Set unsynced count in intent data
                intnt.putExtra("intntdata", notification);
                // Call MyService
                context.startService(intnt);
            }else{
                Toast.makeText(context, "No updates from Optho today!", Toast.LENGTH_SHORT).show();
            }
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            Toast.makeText(context, e.toString(), Toast.LENGTH_SHORT).show();

            e.printStackTrace();
        }
    }

    public static boolean CompareDates(String date1, String date2) {

        boolean update=false;
        try {
            String pattern = "yyyy-MM-dd HH:mm:ss";
            SimpleDateFormat formatter = new SimpleDateFormat(pattern);
            Date Date1 = formatter.parse(date1);
            Date Date2 = formatter.parse(date2);
            if (Date1 != null && Date2 != null) {

                if (Date1.equals(Date2)) {
                    update=false;
                    } else {
                        update=true;
                    }
            }else {
                update=true;
            }

            }catch(Exception e1){
                e1.printStackTrace();
            }

            return update;

    }

    public boolean isConnected(Context c){
        ConnectivityManager connMgr = (ConnectivityManager) c.getSystemService(Activity.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        return networkInfo != null && networkInfo.isConnected();
    }
}
