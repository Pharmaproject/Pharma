package in.optho.opthoremedies.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.andrognito.pinlockview.IndicatorDots;
import com.andrognito.pinlockview.PinLockListener;
import com.andrognito.pinlockview.PinLockView;

import in.optho.opthoremedies.Database.EmployeeDatabaseHelper;
import in.optho.opthoremedies.R;
import in.optho.opthoremedies.SessionHelper.SQLiteHandler;
import in.optho.opthoremedies.SessionHelper.SessionManager;
import mehdi.sakout.fancybuttons.FancyButton;

public class LoginActivity extends AppCompatActivity {

    private String TAG = "OPTHO";
    private static final String KEY_ID = "id";
    private static long back_pressed;

    private EditText passwordET;
    private FancyButton okButton;
    PinLockView mPinLockView;
    IndicatorDots mIndicatorDots;

    private Button loginBtn;
    private EmployeeDatabaseHelper db;
    private SessionManager session;
    private SQLiteHandler iddb;


    String ID;
    String employeePin;

    private void initialize() {
        /*idET = (EditText) findViewById(R.id.idET);
        passwordET = (EditText) findViewById(R.id.passwordET);
        okButton = (FancyButton) findViewById(R.id.okButton);
*/
        mPinLockView = (PinLockView) findViewById(R.id.pin_lock_view);
        mPinLockView.setPinLockListener(mPinLockListener);

        mIndicatorDots = (IndicatorDots) findViewById(R.id.indicator_dots);
        mPinLockView.attachIndicatorDots(mIndicatorDots);

        loginBtn = (Button) findViewById(R.id.ClearBtn);

        db = new EmployeeDatabaseHelper(this);



        // SQLite database handler
        iddb = new SQLiteHandler(getApplicationContext());

        // Session manager
        session = new SessionManager(getApplicationContext());

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        // Initialise all the variables
        initialize();



        String ID = iddb.getUserDetails();
        employeePin = db.getPin(ID);
        Log.i(TAG, "onCreate: pin: "+employeePin);
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                session.setLogin(false);
                iddb.deleteUsers();
                startActivity(new Intent(LoginActivity.this,IDActivity.class));
                finish();
            }
        });


    }

    private PinLockListener mPinLockListener = new PinLockListener() {
        @Override
        public void onComplete(String pin) {

            Log.d(TAG, "Pin complete: " + pin);

            if(pin.equals(employeePin)){
                startActivity(new Intent(LoginActivity.this,MainListActivity.class));
                finish();
                Snackbar.make(getWindow().getDecorView(), "Logged im", Snackbar.LENGTH_LONG).show();

            }else{
                Snackbar.make(getWindow().getDecorView(), "Wrong Pin", Snackbar.LENGTH_LONG).show();
                mPinLockView.resetPinLockView();

            }



        }

        @Override
        public void onEmpty() {

            Log.d(TAG, "Pin empty");
        }

        @Override
        public void onPinChange(int pinLength, String intermediatePin) {

        }
    };

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
