package in.optho.opthoremedies.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import in.optho.opthoremedies.Database.EmployeeDatabaseHelper;
import in.optho.opthoremedies.SessionHelper.SQLiteHandler;
import in.optho.opthoremedies.SessionHelper.SessionManager;
import in.optho.opthoremedies.R;

public class IDActivity extends AppCompatActivity {

    private Button nextBtn;
    private EditText idET;

    EmployeeDatabaseHelper db;

    private SessionManager session;
    private SQLiteHandler iddb;

    void initialise(){
        nextBtn = (Button) findViewById(R.id.nextBtn);
        idET = (EditText) findViewById(R.id.idET);

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

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_id);

        initialise();



        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final String ID=idET.getText().toString();

                if(TextUtils.isEmpty(ID)){
                    idET.setError("Enter the Employee SNO");
                    Toast.makeText(IDActivity.this, "Enter the Employee SNO", Toast.LENGTH_SHORT).show();
                    idET.requestFocus();
                    return;
                }
                else if(db.checkID(ID)){
                    Toast.makeText(IDActivity.this, " Valid UD", Toast.LENGTH_SHORT).show();

                    iddb.addUser(ID);

                    Intent intent = new Intent(IDActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                }
                else{
                    idET.setError("Invalid Employee SNO");
                    Toast.makeText(IDActivity.this, "Invalid Employee SNO", Toast.LENGTH_SHORT).show();
                    idET.requestFocus();
                    return;
                }




            }
        });


    }

}
