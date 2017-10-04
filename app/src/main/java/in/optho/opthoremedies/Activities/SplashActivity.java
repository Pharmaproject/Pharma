package in.optho.opthoremedies.Activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import in.optho.opthoremedies.Database.DatabaseHelper;

public class SplashActivity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        insertData();
        //remove

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

    private void insertData() {
        DatabaseHelper db = new DatabaseHelper(SplashActivity.this);

        db.insertData("12", "1232");
        db.insertData("13", "1233");
        db.insertData("14", "1234");
        db.insertData("15", "1235");
        db.insertData("16", "1236");


    }
}
