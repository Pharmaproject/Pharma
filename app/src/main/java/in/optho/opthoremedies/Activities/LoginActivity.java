package in.optho.opthoremedies.Activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.andrognito.pinlockview.IndicatorDots;
import com.andrognito.pinlockview.PinLockListener;
import com.andrognito.pinlockview.PinLockView;

import in.optho.opthoremedies.Database.DatabaseHelper;
import in.optho.opthoremedies.R;
import mehdi.sakout.fancybuttons.FancyButton;

public class LoginActivity extends AppCompatActivity {

    private EditText idET;
    private String TAG = "OPTHO";

    private EditText passwordET;
    private FancyButton okButton;
    PinLockView mPinLockView;
    IndicatorDots mIndicatorDots;

    private Button loginBtn;
    private SharedPreferences sharedpreferences;
    private DatabaseHelper db;

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

        loginBtn = (Button) findViewById(R.id.loginBtn);

        db = new DatabaseHelper(this);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        // Initialise all the variables
        initialize();

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this,IDActivity.class));
                finish();
            }
        });

        sharedpreferences = getSharedPreferences("EMPLOYEE_ID", Context.MODE_PRIVATE);
        ID= sharedpreferences.getString("SNO", null);
        employeePin = db.getPin(ID);
        Log.i(TAG, "onCreate: pin: "+employeePin);




    }

    private PinLockListener mPinLockListener = new PinLockListener() {
        @Override
        public void onComplete(String pin) {

            Log.d(TAG, "Pin complete: " + pin);

            if(pin.equals(employeePin)){
                startActivity(new Intent(LoginActivity.this,GridViewActivity.class));
                finish();
                Toast.makeText(LoginActivity.this, "successfully loged in "+pin, Toast.LENGTH_SHORT).show();
            }else
                Toast.makeText(LoginActivity.this, "Wrong pin "+pin, Toast.LENGTH_SHORT).show();



        }

        @Override
        public void onEmpty() {
            Toast.makeText(LoginActivity.this, "Pin empty: ", Toast.LENGTH_SHORT).show();

            Log.d(TAG, "Pin empty");
        }

        @Override
        public void onPinChange(int pinLength, String intermediatePin) {
            Toast.makeText(LoginActivity.this, "Pin changed, new length " + pinLength + " with intermediate pin " + intermediatePin, Toast.LENGTH_SHORT).show();
            Log.d(TAG, "Pin changed, new length " + pinLength + " with intermediate pin " + intermediatePin);
        }
    };
}