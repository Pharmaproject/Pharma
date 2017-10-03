package in.optho.opthoremedies;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

public class SplashActivity extends AppCompatActivity {

    SharedPreferences sharedpreferences;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sharedpreferences = getSharedPreferences("EMPLOYEE_ID", Context.MODE_PRIVATE);

        final String ID=sharedpreferences.getString("ID", "0000");

        Toast.makeText(this, "Emplyee: "+ID, Toast.LENGTH_SHORT).show();

        boolean isDataSaved=sharedpreferences.getBoolean("isDataSaved", false);
        if(!isDataSaved)
            insertData();


        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

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

        db.insterData("12", "1232");
        db.insterData("13", "1233");
        db.insterData("14", "1234");
        db.insterData("15", "1235");
        db.insterData("16", "1236");

        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putBoolean("isDataSaved", true);
        editor.commit();

    }
}
