package in.optho.opthoremedies.Service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.widget.Toast;

import in.optho.opthoremedies.Activities.SplashActivity;
import in.optho.opthoremedies.R;

public class MyService extends Service {
	int numMessages = 0;

	public MyService() {
	}

	@Override
	public IBinder onBind(Intent intent) {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	@Override
	public void onCreate() {
		Toast.makeText(this, "Service was Created", Toast.LENGTH_LONG).show();

	}

	@Override
	public void onStart(Intent intent, int startId) {

		Toast.makeText(this, "Service Started", Toast.LENGTH_LONG).show();
		Intent resultIntent = new Intent(this, SplashActivity.class);
		PendingIntent resultPendingIntent = PendingIntent.getActivity(this, 0,
				resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);
		NotificationCompat.Builder mNotifyBuilder;
		NotificationManager mNotificationManager;
		mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
		// Sets an ID for the notification, so it can be updated
		int notifyID = 9001;
		mNotifyBuilder = new NotificationCompat.Builder(this)
				.setContentTitle("Optho Remedies update required")
				.setContentText("A new database is available from Optho Remedies.")
				.setSmallIcon(R.drawable.logocrop)
                .setTicker("New Database update from Obligo");
		// Set pending intent
		mNotifyBuilder.setContentIntent(resultPendingIntent);
		// Set Vibrate, Sound and Light
		int defaults = 0;
		defaults = defaults | Notification.DEFAULT_LIGHTS;
		defaults = defaults | Notification.DEFAULT_VIBRATE;
		defaults = defaults | Notification.DEFAULT_SOUND;
		mNotifyBuilder.setDefaults(defaults);
		// Set the content for Notification
		if(intent!=null) {
            if(intent.getStringExtra("intntdata")!=null) {
                mNotifyBuilder.setContentText(intent.getStringExtra("intntdata"));
                // Set autocancel
                mNotifyBuilder.setAutoCancel(true);
                // Post a notification
                mNotificationManager.notify(notifyID, mNotifyBuilder.build());
            }
            if(intent.getStringExtra("appupdate")!=null){
                Intent yesReceive = new Intent(getApplicationContext(), SampleBC.class);
                yesReceive.setAction("AppUpdate");
                PendingIntent pendingIntentYes = PendingIntent.getBroadcast(this,1 , yesReceive, PendingIntent.FLAG_UPDATE_CURRENT);
                mNotifyBuilder.setContentText("New app update Available");
                // Set autocancel
                mNotifyBuilder.setAutoCancel(false);
                mNotifyBuilder.setContentIntent(PendingIntent.getActivity(this, 3, new Intent(), 0));
                mNotifyBuilder.addAction(R.drawable.pin_code_round_full,"Download Now",pendingIntentYes);
                // Post a notification
                mNotificationManager.notify(9002, mNotifyBuilder.build());

            }

		}


	}

	@Override
	public void onDestroy() {
		Toast.makeText(this, "Service Destroyed", Toast.LENGTH_LONG).show();

	}
}