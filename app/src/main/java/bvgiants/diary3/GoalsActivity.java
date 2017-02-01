package bvgiants.diary3;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Created by Zack on 2/06/2016.
 */
public class GoalsActivity extends AppCompatActivity {


    //Database init
    private SQLiteDatabase db;
    private DatabaseHelper databaseHelper;
    private static Context context;
    private int USERID;
    private User user;


    // User input strings
    private EditText sugarInput;
    private EditText stepsInput;
    private EditText kjInput;
    private EditText calInput;

    private Button saveButton;
    private boolean found;

    // On Create
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goals);

        Toolbar toolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);

        //Initialise vars
        saveButton = (Button) findViewById(R.id.save_goals);
        sugarInput = (EditText) findViewById(R.id.sugarGoal);
        stepsInput = (EditText) findViewById(R.id.stepsGoal);
        kjInput = (EditText) findViewById(R.id.kJGoal);
        calInput = (EditText) findViewById(R.id.calGoal);
        USERID = getIntent().getIntExtra("UserID", 0);

        //Initialise database
        context = getApplicationContext();
        databaseHelper = new DatabaseHelper(context);
        db = databaseHelper.getWritableDatabase();
        user = databaseHelper.getUserGoals(USERID);
        Log.v("GOAL ACTIVITY USER =", user.userGoals());

        if(user.getId() != 0){
            sugarInput.setText(String.valueOf(user.getSugarGoal()));
            stepsInput.setText(String.valueOf(user.getStepGoal()));
            kjInput.setText(String.valueOf(user.getKilojoulesGoal()));
            calInput.setText(String.valueOf(user.getCalorieGoal()));
            found = true;
        }
        else
            found = false;

        // Set onClick listener for Save button.
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkInput();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.eat_menu, menu);
        return true;
    }

    // Inflate the top menu bar.
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            Intent startSettings = new Intent(this, SettingsActivity.class);
            Bundle userCreds = new Bundle();
            userCreds.putInt("UserID", USERID);
            startSettings.putExtras(userCreds);
            startActivity(startSettings);
            return true;
        }

        if (id == R.id.action_home) {
            Intent startHome = new Intent(this, MainActivity.class);
            Bundle userCreds = new Bundle();
            userCreds.putInt("UserID", USERID);
            startHome.putExtras(userCreds);
            startActivity(startHome);
            return true;
        }

        if (id == R.id.action_about) {
            Intent startAbout = new Intent(this, About_Us.class);
            Bundle userCreds = new Bundle();
            userCreds.putInt("UserID", USERID);
            startAbout.putExtras(userCreds);
            startActivity(startAbout);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    // Validate user input. If no errors, query database (UpdateUser).
    public void checkInput() {
        if (!validate()) {
            saveFailed();
            return;
        } else {
            updateGoals();
        }
        saveButton.setEnabled(false);

    } // End checkInput()

    // Validate the USER GOALS:

    public boolean validate() {

        // Strings to check length of input
        String userSugar = sugarInput.getText().toString();
        String userSteps = stepsInput.getText().toString();
        String userKjs = kjInput.getText().toString();
        String userCals = calInput.getText().toString();

        // Integers for more complex validation
        int sugar = Integer.parseInt(sugarInput.getText().toString());
        int steps = Integer.parseInt(stepsInput.getText().toString());
        int kJs = Integer.parseInt(kjInput.getText().toString());
        int calories = Integer.parseInt(calInput.getText().toString());

        // Validate boolean
        boolean valid = true;

        /// Check users SUGAR input.

        if (userSugar.isEmpty() || userSugar.length() > 6) {
            sugarInput.setError("Please enter a valid number (grams)");
            valid = false;
        }
        //else if (sugar > RECOMMENDED_SUGAR) {
            //warningMessage("daily", "SUGAR", "male", RECOMMENDED_SUGAR);
       // }
        else {
            sugarInput.setError(null);
        } //End If Else


        ///  Check users STEPS input.

        if (userSteps.isEmpty() || userSteps.length() > 10 ) {
            stepsInput.setError("Please enter a valid number of steps");
            valid = false;
        } //else if (steps < RECOMMENDED_STEPS) {
           // Toast.makeText(getBaseContext(), "WARNING: The recommended (daily/weekly) step count " +
             //       "for the average (gender) is " + RECOMMENDED_STEPS, Toast.LENGTH_LONG).show();
        //}
        else {
            stepsInput.setError(null);
        } //End If Else


        ///  Check users KILOJOULES input.

        if (userKjs.isEmpty() || userKjs.length() > 10 ) {
            kjInput.setError("Please enter a valid number");
            valid = false;
        //} else if (kJs < RECOMMENDED_KJS) {
        //   warningMessage("daily", "KILOJOULE", "male", RECOMMENDED_KJS);
        }
        else {
            kjInput.setError(null);
        } //End If Else


        ///  Check users CALORIES input.

        if (userCals.isEmpty() || userCals.length() > 10 ) {
           calInput.setError("Please enter a valid number");
            valid = false;
        }
        //else if (calories > RECOMMENDED_CALORIES || calories < MINIMUM_CALORIES) {

            //warningMessage("daily", "CALORIE", "male", RECOMMENDED_CALORIES);

        //}
        else {
            stepsInput.setError(null);
        } // End If Else

        return valid;

    } /// End validate()


    public void warningMessage(String time, String nutrition, String gender, int warning){

        Toast.makeText(getBaseContext(), "WARNING: The recommended" + time + nutrition
                + "intake for the average" + gender + " is "  + warning + nutrition,
                Toast.LENGTH_LONG).show();
    }
    //End Warning message


    public void saveFailed()  {
        Toast.makeText(getBaseContext(), "Profile update failed!", Toast.LENGTH_LONG).show();
        saveButton.setEnabled(true);
    } // End saveFailed()

    public void updateGoals() {

        user.setId(USERID);
        user.setSugarGoal(Integer.parseInt(sugarInput.getText().toString()));
        user.setStepGoal(Integer.parseInt(stepsInput.getText().toString()));
        user.setKilojoulesGoal(Integer.parseInt(kjInput.getText().toString()));
        user.setCalorieGoal(Integer.parseInt(calInput.getText().toString()));

        if(found == true) {
            databaseHelper.updateUserGoals(user.getId(), user);
            Log.v("USER FOUND ?=", user.userGoals());
            onSaveSuccess();
        }
        else if (found == false) {
            databaseHelper.insertUserGoals(user);
            Log.v("USER NOT FOUND ?= ", user.userGoals());
            onSaveSuccess();
        }


    }
    public void onSaveSuccess() {
        saveButton.setEnabled(true);
        setResult(RESULT_OK, null);
        Intent startHome = new Intent(this, MainActivity.class);
        Bundle userCreds = new Bundle();
        userCreds.putInt("UserID", USERID);
        startHome.putExtras(userCreds);
        startActivity(startHome);
    } //End onSaveSuccess

} // End Class
