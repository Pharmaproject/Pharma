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

    SharedPreferences sharedpreferences;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sharedpreferences = getSharedPreferences("EMPLOYEE_ID", Context.MODE_PRIVATE);


        final String ID=sharedpreferences.getString("SNO", "0000");

        Toast.makeText(this, "Emplyee: "+ID, Toast.LENGTH_SHORT).show();

        boolean isDataSaved=sharedpreferences.getBoolean("isDataSaved", false);
        if(!isDataSaved)
            insertData();


        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                //sample

                    System.out.println("New user");

                    if(ID.equals("0000")){
                        startActivity(new Intent(SplashActivity.this, IDActivity.class));
                    }
                    else
                        startActivity(new Intent(SplashActivity.this, LoginActivity.class));
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

        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putBoolean("isDataSaved", true);
        editor.commit();

    }
}
