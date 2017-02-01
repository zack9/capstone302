package bvgiants.diary3;

        import android.content.Context;
        import android.database.sqlite.SQLiteDatabase;
        import android.support.v7.app.AppCompatActivity;
        import android.net.Uri;
        import android.os.Bundle;
        import android.support.design.widget.FloatingActionButton;
        import android.support.design.widget.Snackbar;
        import android.support.v7.app.AppCompatActivity;
        import android.support.v7.widget.Toolbar;
        import android.util.Log;
        import android.view.View;
        import android.view.Menu;
        import android.view.MenuItem;
        import android.content.Intent;
        import android.widget.Button;
        import android.widget.EditText;
        import android.widget.Toast;

/**
 * Created by Zack on 6/05/2016.
 */
public class ProfileActivity extends AppCompatActivity{

    //Db
    public SQLiteDatabase db;
    public DatabaseHelper databaseHelper;

    //User
    public static Context context;
    private int USERID;
    private User user;
    private boolean found;


    // Initialize EditText variables
    private EditText firstName;
    private EditText lastName;
    private EditText height;
    private EditText weight;
    private EditText age;
    private EditText gender;

    private Button saveButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        Toolbar toolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);

        // Assign variables to content
        saveButton = (Button) findViewById(R.id.save_button);
        firstName = (EditText) findViewById(R.id.first_name);
        lastName = (EditText) findViewById(R.id.lastName);
        height = (EditText) findViewById(R.id.height);
        weight = (EditText) findViewById(R.id.weight);
        age = (EditText) findViewById(R.id.age);
        gender = (EditText) findViewById(R.id.gender);


        //Database
        USERID = getIntent().getIntExtra("UserID", 0);
        context = getApplicationContext();
        databaseHelper = new DatabaseHelper(context);
        db = databaseHelper.getWritableDatabase();
        user = databaseHelper.getUserTraits(USERID);

        if(user.getId() != 0){
            firstName.setText(user.getFirstName());
            lastName.setText(user.getLastName());
            height.setText(String.valueOf(user.getHeight()));
            weight.setText(String.valueOf(user.getWeight()));
            age.setText(String.valueOf(user.getAge()));
            gender.setText(String.valueOf(user.getGender()));
            found = true;
        }
        else {
            found = false;
        }

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveUser();
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

    public void saveUser() {
        if (!validate()) {
            onSaveFailed();
            return;
        } else {
            updateUser();
        }
       saveButton.setEnabled(false);
    } // End saveUser()


    public void onSaveSuccess() {
        saveButton.setEnabled(true);
        setResult(RESULT_OK, null);
        Intent startHome = new Intent(this, MainActivity.class);
        Bundle userCreds = new Bundle();
        userCreds.putInt("UserID", USERID);
        startHome.putExtras(userCreds);
        startActivity(startHome);
    } //End onSaveSuccess


    // Validate ()
    public boolean validate(){

        // Get values for validation
        String fName = firstName.getText().toString();
        String lName = lastName.getText().toString();
        String userGender = gender.getText().toString();
        String heightString = height.getText().toString();
        String weightString = weight.getText().toString();
        String ageString = age.getText().toString();

        boolean valid = true;

        //FirstName
        if (fName.isEmpty() || fName.length() < 2)   {
            firstName.setError("please enter a valid first name");
            valid = false;
        } else {
            firstName.setError(null);
        }

        //LastName
        if (lName.isEmpty() || lName.length() < 3 )  {
            lastName.setError("please enter a valid last name");
            valid = false;
        } else {
            lastName.setError(null);
        }
        // Height
        if (heightString.isEmpty() || heightString.length() < 2 || heightString.length() > 3) {
            height.setError("height must be a realistic number (10cm - 999cm)");
            valid = false;
        } else {
            height.setError(null);
        }

        //Weight
        if (weightString.isEmpty() || weightString.length() < 2 || weightString.length() > 3) {
            weight.setError("weight must be a realistic number (10kg - 999kg)");
            valid = false;
        } else {
           weight.setError(null);
        }

        //Age
        if (ageString.isEmpty() || ageString.length() > 3) {
            age.setError("age must be a realistic number (1 - 999)");
            valid = false;
        } else {
            age.setError(null);
        }

        //Gender
        if (userGender.isEmpty() || !userGender.equalsIgnoreCase("Male") &&
                !userGender.equalsIgnoreCase("female")) {
            gender.setError("Please enter Male or Female");
            valid = false;
        } else {
            gender.setError(null);
        }
        return valid;
    } //End Validate


    public void updateUser() {

        // Set attributes in database

        user.setId(USERID);
        user.setFirstName(firstName.getText().toString());
        user.setLastName(lastName.getText().toString());
        user.setGender(gender.getText().toString());
        user.setHeight(Integer.parseInt(height.getText().toString()));
        user.setWeight(Integer.parseInt(weight.getText().toString()));
        user.setAge(Integer.parseInt(age.getText().toString()));

        if(found == false) {
            //User newUser = new User(USERID, fName, lName, userHeight, userWeight, userAge, userGender);
            databaseHelper.insertUserTraits(user);
            onSaveSuccess();
        }
        else if(found == true){
            databaseHelper.updateUserTraits(user.getId(), user);
            onSaveSuccess();
        }
    } //End UpdateUser

    public void onSaveFailed() {
        Toast.makeText(getBaseContext(), "Profile update failed!", Toast.LENGTH_LONG).show();
        saveButton.setEnabled(true);

    } //End UpdateUser

} //End Class
