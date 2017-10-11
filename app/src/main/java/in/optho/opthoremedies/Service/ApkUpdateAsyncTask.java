package in.optho.opthoremedies.Service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Environment;
import android.widget.RemoteViews;

import java.io.BufferedInputStream;
import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;

import in.optho.opthoremedies.R;

import static android.R.attr.id;

/**
 * Created by Anveshak on 11-10-17.
 */

public class ApkUpdateAsyncTask extends AsyncTask<String, Integer, String> {


    int progress = 0;
    Notification notification;
    NotificationManager notificationManager;
    int id = 10;

    public ApkUpdateAsyncTask(){


    }
    @Override
    protected String doInBackground(String... urls){

        String path = Environment.getExternalStorageDirectory()+"/opthoapp.apk";

        //download the apk from your server and save to sdk card here
        try{
            URL url = new URL(urls[0]);
            URLConnection connection = url.openConnection();
            connection.connect();

            // download the file
            InputStream input = new BufferedInputStream(url.openStream());
            OutputStream output = new FileOutputStream(path);

            byte data[] = new byte[1024];
            int count;
            while ((count = input.read(data)) != -1)
            {
                output.write(data, 0, count);
            }

            output.flush();
            output.close();
            input.close();
        }catch(Exception e){}

        return path;
    }

    protected void onProgressUpdate(Integer... progress) {

       /* Intent intent = new Intent();
        final PendingIntent pendingIntent = PendingIntent.getActivity(
                getApplicationContext(), 0, intent, 0);
        notification = new Notification(R.drawable.video_upload,
                "Uploading file", System.currentTimeMillis());
        notification.flags = notification.flags
                | Notification.FLAG_ONGOING_EVENT;
        notification.contentView = new RemoteViews(getApplicationContext()
                .getPackageName(), R.layout.upload_progress_bar);
        notification.contentIntent = pendingIntent;
        notification.contentView.setImageViewResource(R.id.status_icon,
                R.drawable.video_upload);
        notification.contentView.setTextViewText(R.id.status_text,
                "Uploading...");
        notification.contentView.setProgressBar(R.id.progressBar1, 100,
                progress[0], false);
        getApplicationContext();
        notificationManager = (NotificationManager) getApplicationContext()
                .getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(id, notification);*/

    }

    @Override
    protected void onPostExecute(String path)
    {
        Process process = null;

        // call to superuser command, pipe install updated APK without writing over files/DB
        try
        {
            process = Runtime.getRuntime().exec("su");
            DataOutputStream outs = new DataOutputStream(process.getOutputStream());

            String cmd = "pm install -r "+path;

            outs.writeBytes(cmd+"\\n");
        }
        catch (IOException e)
        {}
    }

}