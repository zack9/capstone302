package bvgiants.diary3;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


public class SignupActivity extends AppCompatActivity {

    Button signUpButton;
    TextView alreadyMember;


    private EditText mEmailView;
    private EditText mPasswordView;
    private EditText mNameView;
    private EditText aliasView;
    public static Context context;
    private static final String TAG = SignupActivity.class.getSimpleName();

    public DatabaseHelper databaseHelper;
    public SQLiteDatabase db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        Toolbar toolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);

        signUpButton = (Button) findViewById(R.id.signUp);
        alreadyMember = (TextView) findViewById(R.id.alreadymember);
        mPasswordView = (EditText) findViewById(R.id.passwordText);
        aliasView = (EditText) findViewById(R.id.aliasText);
        context = getApplicationContext();
        mEmailView = (EditText) findViewById(R.id.emailText);
        context = getApplicationContext();
        databaseHelper = new DatabaseHelper(context);
        db = databaseHelper.getWritableDatabase();

        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signup();
            }
        });
       alreadyMember.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }


    public void signup() {
        Log.d(TAG, "Signup");
        if (!validate()) {
            onSignupFailed();
            return;
        }
        else {
            createAccount();
        }
        signUpButton.setEnabled(false);
    }

    public void onSignupSuccess() {
        signUpButton.setEnabled(true);
        setResult(RESULT_OK, null);
        finish();
    }

    public void onSignupFailed() {
        Toast.makeText(getBaseContext(), "Login failed", Toast.LENGTH_LONG).show();
        signUpButton.setEnabled(true);
    }


    public void createAccount() {

        //String name = mNameView.getText().toString();
        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();
        String alias = aliasView.getText().toString();

        User newUser = new User (createUserID(),email,password,alias," ");
        databaseHelper.insertUser(newUser);
        onSignupSuccess();

    }

    public int createUserID(){
        int userID = (int) (Math.random() * 9999 + 1);
        return userID;
    }

    public boolean validate() {
        boolean valid = true;

        //String name = mNameView.getText().toString();
        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();
        String alias = aliasView.getText().toString();


        if (alias.isEmpty() || alias.length() < 3) {
            aliasView.setError("at least 3 characters");
            valid = false;
        } else {
            aliasView.setError(null);
        }

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            mEmailView.setError("enter a valid email address");
            valid = false;
        } else {
            mEmailView.setError(null);
        }

        if (password.isEmpty() || password.length() < 4 || password.length() > 10) {
            mPasswordView.setError("between 4 and 10 alphanumeric characters");
            valid = false;
        } else {
            mPasswordView.setError(null);
        }

        return valid;

    }



}
