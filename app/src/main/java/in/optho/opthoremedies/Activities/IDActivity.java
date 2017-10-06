package in.optho.opthoremedies.Activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.Html;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.support.design.widget.Snackbar;

import com.chaos.view.PinView;

import in.optho.opthoremedies.Database.EmployeeDatabaseHelper;
import in.optho.opthoremedies.SessionHelper.SQLiteHandler;
import in.optho.opthoremedies.SessionHelper.SessionManager;
import in.optho.opthoremedies.R;

public class IDActivity extends AppCompatActivity {

    private PinView  idET;
    private static long back_pressed;

    EmployeeDatabaseHelper db;

    private SessionManager session;
    private SQLiteHandler iddb;

    void initialise(){


        // SQLite database handler
        iddb = new SQLiteHandler(getApplicationContext());

        // Session manager
        session = new SessionManager(getApplicationContext());



        db = new EmployeeDatabaseHelper(getApplicationContext());
        // Check if user is already logged in or not
        if (session.isLoggedIn()) {
            // User is already logged in. Take him to main activity
            Intent intent = new Intent(IDActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();




        }

        idET = (PinView) findViewById(R.id.pinView);
        idET.setBorderColor(getResources().getColor(R.color.colorWhiteHigh));
        idET.setAnimationEnable(true);// start animation when adding text

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_id);

        initialise();



        idET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s,
                                          int start,
                                          int count,
                                          int after) {

            }

            @Override
            public void onTextChanged(CharSequence s,
                                      int start,
                                      int before,
                                      int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(s.length() == 4) {
                    String ID = s.toString();
                    if (db.checkID(ID)) {

                        iddb.addUser(ID);
                        session.setLogin(true);

                        Intent intent = new Intent(IDActivity.this, LoginActivity.class);
                        startActivity(intent);
                        finish();
                    } else {

                        idET.setError(Html.fromHtml("<font color='red'>Invalid Employee ID</font>"));


                        Snackbar.make(getWindow().getDecorView(), "Invalid Employee ID", Snackbar.LENGTH_LONG).show();

                        idET.requestFocus();
                        final Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                idET.setText(null);

                            }
                        }, 1000);


                    }
                }
            }
        });




    }

    @Override
    public void onBackPressed() {

        if (back_pressed + 2000 > System.currentTimeMillis()) {
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
            System.exit(0);
        } else {
            Toast.makeText(getBaseContext(), "Press once again to exit", Toast.LENGTH_SHORT).show();
            back_pressed = System.currentTimeMillis();
        }
    }

}
