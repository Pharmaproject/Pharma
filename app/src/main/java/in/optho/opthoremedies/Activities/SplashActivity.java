package in.optho.opthoremedies.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

public class SplashActivity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                //sample

                    System.out.println("New user");

                        startActivity(new Intent(SplashActivity.this, IDActivity.class));

                    finish();

            }
        }, 1000);

    }

}
