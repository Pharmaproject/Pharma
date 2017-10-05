package in.optho.opthoremedies.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

import in.optho.opthoremedies.R;

public class SplashActivity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                        startActivity(new Intent(SplashActivity.this, IDActivity.class));
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);

                    finish();

            }
        }, 1000);

    }

}
