package in.optho.opthoremedies.Service;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

public class SampleBC extends BroadcastReceiver {
	static int noOfTimes = 0;

	// Method gets called when Broad Case is issued from MainActivity for every 10 seconds
	@Override
	public void onReceive(final Context context, Intent intent) {
		// TODO Auto-generated method stub

		noOfTimes++;
		Toast.makeText(context, "Update Check number " + noOfTimes + " times", Toast.LENGTH_SHORT).show();
		AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        // Checks if new records are inserted in Remote MySQL DB to proceed with Sync operation
        client.post("http://192.168.2.4:9000/mysqlsqlitesync/getdbrowcount.php",params ,new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(String response) {
                System.out.println(response);
                try {
                	// Create JSON object out of the response sent by getdbrowcount.php
                    JSONObject obj = new JSONObject(response);
                    System.out.println(obj.get("count"));
                    // If the count value is not zero, call MyService to display notification 
                    if(obj.getInt("count") != 0){
                        int count = 0;
                        SharedPreferences.Editor editor = context.getSharedPreferences("myPrefs", Context.MODE_PRIVATE).edit();
                        int defaultValue = context.getSharedPreferences("MyPref", context.MODE_PRIVATE).getInt("count_key",count);
                        ++defaultValue;
                        context.getSharedPreferences("MyPref", context.MODE_PRIVATE).edit().putInt("count_key",defaultValue).commit();
                        count = context.getSharedPreferences("MyPref", context.MODE_PRIVATE).getInt("count_key",count);
                        System.out.println("Days Remaining for  " + count);
                        int days = 10-count;
                        Toast.makeText(context, "Sync needed before "+days+" days", Toast.LENGTH_SHORT).show();

                    	final Intent intnt = new Intent(context, MyService.class);
                    	// Set unsynced count in intent data
                    	intnt.putExtra("intntdata", "Unsynced Rows Count "+obj.getInt("count"));
                    	// Call MyService
                    	context.startService(intnt);
                    }else{
                    	Toast.makeText(context, "Sync not needed", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Throwable error,
                String content) {
                // TODO Auto-generated method stub
                if(statusCode == 404){
                	Toast.makeText(context, "404", Toast.LENGTH_SHORT).show();
                }else if(statusCode == 500){
                	Toast.makeText(context, "500", Toast.LENGTH_SHORT).show();
                }else{
                	Toast.makeText(context, "Error occured!", Toast.LENGTH_SHORT).show();
                }
            }
        });
	}	
}
