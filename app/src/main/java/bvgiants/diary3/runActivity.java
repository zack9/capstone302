package bvgiants.diary3;

import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.service.carrier.CarrierMessagingService;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.fitness.Fitness;
import com.google.android.gms.fitness.data.DataPoint;
import com.google.android.gms.fitness.data.DataSource;
import com.google.android.gms.fitness.data.DataType;
import com.google.android.gms.fitness.data.Field;
import com.google.android.gms.fitness.data.Value;
import com.google.android.gms.fitness.request.DataSourcesRequest;
import com.google.android.gms.fitness.request.OnDataPointListener;
import com.google.android.gms.fitness.request.SensorRequest;
import com.google.android.gms.fitness.result.DataSourcesResult;

import java.lang.Override;
import java.util.concurrent.TimeUnit;

/**
 * Created by Dylan Schumacher on 15/4/2016
 * Establishes listener on window creation
 * Updates output to the user every second
 */

//TODO: Add button to maps page to track activity for the day

public class runActivity extends AppCompatActivity {

    Context mContext;
    private Context context;
    static int totalSteps;
    static float distanceValue;
    static float percentageValue;
    private float currentStepsPercent;

    // DB Variables
    public SQLiteDatabase db;
    public DatabaseHelper databaseHelper;
    private int USERID;
    private User user;
    private User userGoals;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_run);

        Toolbar toolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);

        context = getApplicationContext();
        databaseHelper = new DatabaseHelper(context);
        db = databaseHelper.getWritableDatabase();

        USERID = getIntent().getIntExtra("UserID", 0);
        user = databaseHelper.getUserGoals(USERID);
        int stepGoal = user.getStepGoal();
        startBackgroundProcess(this.findViewById(android.R.id.content), mContext);
        userGoals = databaseHelper.getUserGoals(USERID);
        //Log.e("GOALS", "Step Counter is :" + totalSteps);

        Thread t = new Thread() {

            @Override
            public void run() {
                try {
                    while (!isInterrupted()) {
                        Thread.sleep(1000);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                currentStepsPercent = ((float) totalSteps / userGoals.getStepGoal()) * 100;
                                ((TextView)findViewById(R.id.stepCounterView)).setText(""+totalSteps);
                                ((TextView)findViewById(R.id.distanceCounterView)).setText(""+distanceValue);
                                if (currentStepsPercent == 0) {
                                    ((TextView) findViewById(R.id.percentageCounterView)).setText("None Set");
                                } else {
                                    ((TextView) findViewById(R.id.percentageCounterView)).setText(String.format("%.2f", currentStepsPercent) + "%");
                                }

                            }
                        });
                    }
                } catch (InterruptedException e) {

                }
            }
        };

        t.start();

    } // End onCreate

    public void startBackgroundProcess(View view, Context c){
        startService(new Intent(getBaseContext(), BackgroundService.class));
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.eat_menu, menu);
        return true;
    }
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.action_home) {
            Intent startHome = new Intent(this, MainActivity.class);
            Bundle userCreds = new Bundle();
            userCreds.putInt("UserID", USERID);
            startHome.putExtras(userCreds);
            startActivity(startHome);
            return true;
        }

        if (id == R.id.action_settings) {
            Intent startSettings = new Intent(this, SettingsActivity.class);
            Bundle userCreds = new Bundle();
            userCreds.putInt("UserID", USERID);
            startSettings.putExtras(userCreds);
            startActivity(startSettings);
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

}// End all