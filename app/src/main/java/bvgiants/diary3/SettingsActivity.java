package bvgiants.diary3;

import android.content.Context;

import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

public class SettingsActivity extends AppCompatActivity {

    // Init variables
    Context mContext;
    Button profileButton;
    Button goalsButton;
    private int USERID;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        Toolbar toolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);

        startBackgroundProcess(this.findViewById(android.R.id.content), mContext);
        USERID = getIntent().getIntExtra("UserID", 0);
        profileButton = (Button) findViewById(R.id.buttonProfile);
        goalsButton = (Button) findViewById(R.id.buttonGoals);

        profileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startProfile();
            }
        }); //End clickListener

        goalsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startGoals();
            }
        }); // End clickListener

    } // End OnCreate


    // Start profile activity
    public void startProfile() {
        Intent startProfile = new Intent(this, ProfileActivity.class);
        Bundle userCreds = new Bundle();
        userCreds.putInt("UserID", USERID);
        startProfile.putExtras(userCreds);
        startActivity(startProfile);

    } //End startProfile();


    // Start Goals activity
    public void startGoals() {
        Intent startGoals = new Intent(this, GoalsActivity.class);
        Bundle userCreds = new Bundle();
        userCreds.putInt("UserID", USERID);
        startGoals.putExtras(userCreds);
        startActivity(startGoals);

    }//end startsGoals();


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



    public void startBackgroundProcess(View view, Context c){
        startService(new Intent(getBaseContext(), BackgroundService.class));
    }

}

