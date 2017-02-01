package bvgiants.diary3;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.content.Intent;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.StringTokenizer;

/* Main page the user is placed on after logging into the application.  Holds buttons to enable user
to move through the app, contains 'At A Glance' to see how the user is tracking in key areas etc
 */
public class MainActivity extends AppCompatActivity {


    private Context context;
    static float percentageValue;
    static int tallySteps;
    private int USERID;
    private User loggedinUser;
    private User userGoals;
    private String alias;

    public SQLiteDatabase db;
    public DatabaseHelper databaseHelper;

    private ProgressBar stepCounterProgressBar;
    private ProgressBar calorieCounterProgressBar;
    private ProgressBar kJCounterProgressBar;
    private ProgressBar sugarCounterProgressBar;
    private ArrayList<OrderRow> allFoodOrders = new ArrayList<>();
    private ArrayList<FoodItem> allFoodConsumed = new ArrayList<>();
    private ArrayList<FoodItem> allFoods = new ArrayList<>();

    int userStepGoal = 10000;
    private int calorieCounter;
    private int kJcounter;
    private int sugarCounter;
    private float currentStepsPercent;
    private float currentCalPercent;
    private float currentEnergPercent;
    private float currentSugPercent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);

        context = getApplicationContext();
        databaseHelper = new DatabaseHelper(context);
        db = databaseHelper.getWritableDatabase();

        USERID = getIntent().getIntExtra("UserID", 0);
        loggedinUser = databaseHelper.getUserTraits(USERID);
        userGoals = databaseHelper.getUserGoals(USERID);
        allFoodOrders = databaseHelper.showTodaysFood(USERID);
        alias = databaseHelper.getUserAlias(USERID);
        allFoods = databaseHelper.allFood();



        //Reset Variables each time to ensure they refresh correctly
        calorieCounter = 0;
        kJcounter = 0;
        sugarCounter = 0;
        currentStepsPercent = 0;
        currentCalPercent = 0;
        currentEnergPercent = 0;
        currentSugPercent = 0;

        //Takes the users food orders which are obtained through a database query, then adds up
        //the required totals based on all foods
        for (int i = 0; i < allFoodOrders.size(); i++) {
            for (int k = 0; k < allFoods.size(); k++) {
                if (allFoodOrders.get(i).getFoodId() == allFoods.get(k).getFoodId()) {
                    allFoodConsumed.add(allFoods.get(k));
                    calorieCounter += allFoods.get(k).getCalories();
                    kJcounter += allFoods.get(k).getEnergy();
                    sugarCounter += allFoods.get(k).getSugar();
                }
            }
        }

        startBackgroundProcess(this.findViewById(android.R.id.content), context);

        stepCounterProgressBar = (ProgressBar) findViewById(R.id.progressBarStepsGoal);
        calorieCounterProgressBar = (ProgressBar) findViewById(R.id.progressBarCalories);
        kJCounterProgressBar = (ProgressBar) findViewById(R.id.progressBarKilojoules);
        sugarCounterProgressBar = (ProgressBar) findViewById(R.id.progressBarSugar);

        //Check if user has Goals or Traits added.  If not toast them and advise them to add them.
        if (loggedinUser.getId() == 0 || userGoals.getId() == 0) {
            if (loggedinUser.getAge() == 0 || loggedinUser.getWeight() == 0) {
                    Toast.makeText(getBaseContext(), "Hi " + alias + "!" + "\n" +
                                    "Please enter your personal details through the settings window!",
                            Toast.LENGTH_LONG).show();
            } else if (userGoals.getStepGoal() == 0 || userGoals.getKilojoulesGoal() == 0) {
                    Toast.makeText(getBaseContext(), "Hi " + alias + "!" + "\n" +
                                    "Please enter your GOALS details through the settings window!",
                            Toast.LENGTH_LONG).show();
            }

            }

        //At this point, load up progress bars based on what user has identified as goals.
            if (userGoals.getId() > 0) {

                Thread t = new Thread() {

                    @Override
                    public void run() {
                        try {
                            while (!isInterrupted()) {
                                Thread.sleep(1000);
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Log.e("GOALS", "IN LOOP - Current Step Amount is : " + tallySteps + " | Current step goals is :" + userGoals.getStepGoal());
                                        Log.e("GOALS", "IN LOOP - Current Step Percentage is :" + currentStepsPercent);

                                        currentStepsPercent = ((float) tallySteps / userGoals.getStepGoal()) * 100;
                                        ((TextView) findViewById(R.id.currentSteps)).setText(String.format("%.2f", currentStepsPercent) + "%");
                                        Log.e("GOALS", "IN LOOP - Step Percent is " + (int)currentStepsPercent );
                                        stepCounterProgressBar.setProgress(tallySteps);
                                    }
                                });
                            }
                        } catch (InterruptedException e) {

                        }
                    }
                };

                t.start();

                stepCounterProgressBar.setMax(userGoals.getStepGoal());
                calorieCounterProgressBar.setMax(userGoals.getCalorieGoal());
                kJCounterProgressBar.setMax(userGoals.getKilojoulesGoal());
                sugarCounterProgressBar.setMax(userGoals.getSugarGoal());
                //currentStepsPercent = ((float) tallySteps / userGoals.getStepGoal()) * 100;
                currentCalPercent = ((float) calorieCounter / userGoals.getCalorieGoal()) * 100;
                currentEnergPercent = ((float) kJcounter / userGoals.getKilojoulesGoal()) * 100;
                currentSugPercent = ((float) sugarCounter / userGoals.getSugarGoal()) * 100;



                calorieCounterProgressBar.setProgress(calorieCounter);
                kJCounterProgressBar.setProgress(kJcounter);
                sugarCounterProgressBar.setProgress(sugarCounter);

            }

            // Have to change the percentage in the progress bars
            //((TextView) findViewById(R.id.currentSteps)).setText(String.format("%.2f", currentStepsPercent) + "%");
            ((TextView) findViewById(R.id.currentCalorie)).setText(String.format("%.2f", currentCalPercent) + "%");
            ((TextView) findViewById(R.id.currentKJ)).setText(String.format("%.2f", currentEnergPercent) + "%");
            ((TextView) findViewById(R.id.currentSugar)).setText(String.format("%.2f", currentSugPercent) + "%");

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


        // Takes user to selected screen(Activity)

    public void menuSelect(View v) {
        switch (v.getId()) {
            case (R.id.buttonRun):
                Intent startRun = new Intent(this, runActivity.class);
                Bundle userCreds = new Bundle();
                userCreds.putInt("UserID", USERID);
                startRun.putExtras(userCreds);
                startActivity(startRun);
                break;

            case (R.id.buttonEat):
                Intent startEat = new Intent(this, EatActivity.class);
                Bundle userCreds1 = new Bundle();
                userCreds1.putInt("UserID", USERID);
                startEat.putExtras(userCreds1);
                startActivity(startEat);
                break;

            case (R.id.buttonMaps):
                Intent startMaps = new Intent(this, MapsActivity.class);
                startActivity(startMaps);
                break;

        }
    }

    public void startBackgroundProcess(View view, Context c) {
        startService(new Intent(getBaseContext(), BackgroundService.class));
    }

}
