package in.optho.opthoremedies;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class IDActivity extends AppCompatActivity {

    private Button nextBtn;
    private EditText idET;

    private SharedPreferences sharedpreferences;

    private DatabaseHelper db;


    void initialise(){
        nextBtn = (Button) findViewById(R.id.nextBtn);
        idET = (EditText) findViewById(R.id.idET);
        sharedpreferences = getSharedPreferences("EMPLOYEE_ID", Context.MODE_PRIVATE);
        db = new DatabaseHelper(this);
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
                    idET.setError("Enter the Employee ID");
                    Toast.makeText(IDActivity.this, "Enter the Employee ID", Toast.LENGTH_SHORT).show();
                    idET.requestFocus();
                    return;
                }
                else if(db.checkId(ID)){
                    SharedPreferences.Editor editor = sharedpreferences.edit();
                    editor.putString("ID",ID);
                    editor.commit();

                    Intent intent = new Intent(IDActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                }
                else{
                    idET.setError("Invalid Employee ID");
                    Toast.makeText(IDActivity.this, "Invalid Employee ID", Toast.LENGTH_SHORT).show();
                    idET.requestFocus();
                    return;
                }




            }
        });


    }

}
