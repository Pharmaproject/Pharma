package in.optho.opthoremedies.Service;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;

import com.chaos.view.PinView;

import in.optho.opthoremedies.R;

public class ManageSpace extends AppCompatActivity {
    private PinView password;
    private Button clr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_space);
        password = (PinView) findViewById(R.id.pinView4);
        clr = (Button) findViewById(R.id.ClearBtn);

        clr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String number =password.getText().toString();

                if(number.equals("998844")){
                    if (Build.VERSION_CODES.KITKAT <= Build.VERSION.SDK_INT) {
                        ((ActivityManager) getSystemService(ACTIVITY_SERVICE))
                                .clearApplicationUserData(); // note: it has a return value!
                    }else {
                        // use old hacky way, which can be removed
                        // once minSdkVersion goes above 19 in a few years.
                    }

                }else {
                    password.setError(Html.fromHtml("<font color='red'>Invalid Admin PIN</font>"));
                    password.requestFocus();
                    final Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            password.setText(null);

                        }
                    }, 1000);
                }


            }
        });



    }

    private static void killProcessesAround(Activity activity) throws PackageManager.NameNotFoundException {
        ActivityManager am = (ActivityManager)activity.getSystemService(Context.ACTIVITY_SERVICE);
        String myProcessPrefix = activity.getApplicationInfo().processName;
        String myProcessName = activity.getPackageManager().getActivityInfo(activity.getComponentName(), 0).processName;
        for (ActivityManager.RunningAppProcessInfo proc : am.getRunningAppProcesses()) {
            if (proc.processName.startsWith(myProcessPrefix) && !proc.processName.equals(myProcessName)) {
                android.os.Process.killProcess(proc.pid);
            }
        }
    }
}
