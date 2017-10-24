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

public class changenotify extends Service {

	public changenotify() {
	}

	@Override
	public IBinder onBind(Intent intent) {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	@Override
	public void onCreate() {

	}

	@Override
	public void onStart(Intent intent, int startId) {

		Intent resultIntent = new Intent(this, SplashActivity.class);
		PendingIntent resultPendingIntent = PendingIntent.getActivity(this, 0,
				resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);
		NotificationCompat.Builder mNotifyBuilder;
		NotificationManager mNotificationManager;
		mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
		String product = intent.getStringExtra("intntdata");

		int notifyID = 9003;
		mNotifyBuilder = new NotificationCompat.Builder(this)
				.setContentTitle("Optho Remedies Products Changed")
				.setContentText(product+" has been updated. Kindly Check.")
				.setSmallIcon(R.drawable.logocrop)
				.setTicker("Some Products Have been Changed");
		// Set pending intent
		mNotifyBuilder.setContentIntent(resultPendingIntent);
		// Set Vibrate, Sound and Light
		int defaults = 0;
		defaults = defaults | Notification.DEFAULT_LIGHTS;
		defaults = defaults | Notification.DEFAULT_VIBRATE;
		defaults = defaults | Notification.DEFAULT_SOUND;
		mNotifyBuilder.setDefaults(defaults);
		mNotificationManager.notify(notifyID, mNotifyBuilder.build());



	}

	@Override
	public void onDestroy() {

	}
}