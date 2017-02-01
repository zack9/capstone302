package bvgiants.diary3;


import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.*;
import android.util.Log;
import android.util.SparseArray;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import android.support.v4.widget.SimpleCursorAdapter;
import android.app.Activity;
import android.app.SearchManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.provider.ContactsContract;
//import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.menu.MenuItemImpl;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.app.SearchManager;
import android.widget.SearchView;
import android.widget.SearchView.OnQueryTextListener;
import android.util.SparseArray;
import android.widget.ListView;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;

import android.widget.Toast;


import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.GoogleMap;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

/**
 * Created by kenst on 2/05/2016.
 * This class will be used to select food to add to users diary.
 */
public class FoodEntryActivity extends AppCompatActivity implements SearchView.OnQueryTextListener,
        SearchView.OnCloseListener {

    private int USERID;

    private ListView list;
    public CustomListAdapter adapter;
    public static Context context;
    public SQLiteDatabase db;
    public DatabaseHelper databaseHelper;
    public ArrayList<String> foodNames = new ArrayList<String>();

    public ArrayList<Integer> imageId = new ArrayList<Integer>();

    private SearchView searchView;
    private ArrayList<FoodItem> allFood = new ArrayList<FoodItem>();

    private ArrayList<FoodItem> usersFoods = new ArrayList<FoodItem>();
    private ArrayList<FoodItem> delimitedFoods = new ArrayList<FoodItem>();

    private ArrayList<Integer> delimitedImages = new ArrayList<Integer>();
    private ArrayList<String> delimitedNames = new ArrayList<String>();

    private ArrayList<Integer> userFoodImages = new ArrayList<Integer>();
    private ArrayList<String> userFoodNames = new ArrayList<String>();

    private Button addToDiary;

    private Context mContext;
    public GoogleApiClient mGoogleMapsClient;
    private Location mLastLocation;
    private static int instanceCounter = 1;


    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_entry);

        Toolbar toolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);
        addFoodImages();
        //Firstly Load DB
        context = getApplicationContext();
        databaseHelper = new DatabaseHelper(context);
        db = databaseHelper.getWritableDatabase();

        USERID = getIntent().getIntExtra("UserID", 0);

        allFood = databaseHelper.allFood();
        for (int i = 0; i < allFood.size(); i++) {
            foodNames.add(allFood.get(i).name);
        }

        adapter = new CustomListAdapter(this, foodNames, imageId);
        list = (ListView) findViewById(R.id.list);
        list.setAdapter(adapter);
        list.setVisibility(View.GONE);
        list.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                FoodItem selecteditem;
                // selectedItem holds a FoodItem object of the item selected from the list
                if(delimitedFoods.isEmpty()) {
                    selecteditem = allFood.get(position);
                    selecteditem.setLocation(returnFoodLocation());
                } else {
                    selecteditem = delimitedFoods.get(position);
                    selecteditem.setLocation(returnFoodLocation());
                }
                // Check if the selected item is in the list of selected foods.
                // If the item is in the list, take it off the list, and change the background back to white
                // Otherwise, add the item to the list and make background Blue
                if (usersFoods.contains(selecteditem)) {
                    usersFoods.remove(selecteditem);
                    searchView.refreshDrawableState();
                } else {
                    Toast.makeText(getApplicationContext(), selecteditem.getName(), Toast.LENGTH_SHORT).show();
                    usersFoods.add(selecteditem);
                    Log.v("userFoods size = ", String.valueOf(usersFoods.size()));
                    searchView.refreshDrawableState();
                }
            }
        });

        startBackgroundProcess(this.findViewById(android.R.id.content), mContext);

        if (mGoogleMapsClient == null) {
            mGoogleMapsClient = new GoogleApiClient.Builder(this)
                    .addApi(LocationServices.API)
                    .build();
            mGoogleMapsClient.connect();
            Log.e("Google Maps", "Food Pins : Google Maps Connection Started");
        }

        searchView = (SearchView) findViewById(R.id.search);
        searchView.setIconifiedByDefault(false);
        searchView.setOnQueryTextListener(this);
        searchView.setOnCloseListener(this);

        addToDiary();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.eat_menu, menu);
        return true;
    }

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


    public void addToDiary (){
        final Intent loadEat = new Intent(this, EatActivity.class);
        addToDiary = (Button) findViewById(R.id.save_to_diary);
        addToDiary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    databaseHelper.saveDataToFoodConsumedTable(usersFoods, USERID);
                    Bundle userCreds = new Bundle();
                    userCreds.putInt("UserID", USERID);
                    loadEat.putExtras(userCreds);
                    startActivity(loadEat);
                } catch (IOException e){
                    Log.v(e.toString(), " THERE WAS AN ERROR!");
                }
                logFoodPin();
            }

        });
    }

    public String returnFoodLocation(){
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());

        double latitude = LocationServices.FusedLocationApi.getLastLocation(mGoogleMapsClient).getLatitude();
        double longitude = LocationServices.FusedLocationApi.getLastLocation(mGoogleMapsClient).getLongitude();

        String city="";

        try {
            List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);
            Address returnedAddress = addresses.get(0);
            city = returnedAddress.getLocality();
            Log.e("Found address DURING!", city);
            return city;
        } catch (IOException e) {
            e.printStackTrace();
        }
        Log.e("Found Address AFTERWARDS!", city);
        return city;
    }

    public void logFoodPin(){
        // Set the location
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleMapsClient);
        if (mLastLocation != null) {
            String myLat = Double.toString(mLastLocation.getLatitude());
            String myLong = Double.toString(mLastLocation.getLongitude());
        }
        //Create a temp set of the food to bind to the pin
        Set<String> userFoods = new HashSet<String>(getUserFoodNames());

        // Send the food item to the map
        SharedPreferences mapReferences = getSharedPreferences("FoodPins", MODE_PRIVATE);
        SharedPreferences.Editor editor = mapReferences.edit();
            editor.putString("lat" + Integer.toString((instanceCounter-1)), Double.toString(mLastLocation.getLatitude()));
            editor.putString("lng" + Integer.toString((instanceCounter-1)), Double.toString(mLastLocation.getLongitude()));
            editor.putStringSet("foods" + Integer.toString((instanceCounter-1)), userFoods );
            editor.putInt("foodLocationCount", instanceCounter);
            Log.e("Food Item", "Food Pin Logged | Pin No. " + instanceCounter
                    + " | Logged at co-ordinates: "+mLastLocation.getLatitude()+ " , " + mLastLocation.getLongitude()
                    + " | Food Item(s): " + getUserFoodNames());
        editor.apply();
        returnFoodLocation();
        //Increase the counter to keep track of which item is which
        instanceCounter++;
    } // End logFoodPin

    public boolean onClose() {
        list.setVisibility(View.GONE);
        searchView.clearFocus();
        return false;
    }

    public boolean onQueryTextSubmit(String query) {

        searchView.refreshDrawableState();
        list.setVisibility(View.GONE);
        searchView.clearFocus();
        createUsersSelectedFoods();
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {


        delimitedImages = new ArrayList<Integer>();
        delimitedNames = new ArrayList<String>();

        delimitedFoods = databaseHelper.foodSearch(newText);

        for (int i = 0; i < delimitedFoods.size(); i++){
            Log.v("DELIMITED FOODS", delimitedFoods.get(i).getName());
            for(int k = 0; k < allFood.size(); k++){
                if (allFood.get(k).getName().equalsIgnoreCase(delimitedFoods.get(i).getName())){
                    delimitedImages.add(imageId.get(k));
                    delimitedNames.add(foodNames.get(k));
                }
            }

        }
        adapter = new CustomListAdapter(this, delimitedNames, delimitedImages);
        list = (ListView) findViewById(R.id.list);
        list.setAdapter(adapter);
        list.setVisibility(View.VISIBLE);

        return false;
    }


    public ArrayList<FoodItem> foodsToPass(){
        return usersFoods;
    }

    public ArrayList<Integer> getUserFoodImages(){
        if(userFoodImages.isEmpty())
            return imageId;
        else
            return userFoodImages;
    }
    public ArrayList<String> getUserFoodNames(){
        if(userFoodNames.isEmpty())
            return foodNames;
        else
            return userFoodNames;
    }

    public void createUsersSelectedFoods(){

        for (int i = 0; i < usersFoods.size(); i++){
            for(int k = 0; k < allFood.size(); k++){
                if (allFood.get(k).getName().equalsIgnoreCase(usersFoods.get(i).getName())){
                    userFoodImages.add(imageId.get(k));
                    userFoodNames.add(foodNames.get(k));
                }
            }
            usersFoods.get(i).children.add(usersFoods.get(i).toString());
        }

        Fragment fragment = new ExpandableListFragment();
        FragmentManager fm = getFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.replace(R.id.fragment,fragment);
        fragmentTransaction.commit();

        searchView.clearFocus();
        searchView.refreshDrawableState();
        list.setVisibility(View.GONE);
    }

    public void addFoodImages(){

        imageId.add(R.drawable.chickteriyaki);
        imageId.add(R.drawable.subclub);
        imageId.add(R.drawable.chicstrip);
        imageId.add(R.drawable.pizzasub);
        imageId.add(R.drawable.submeatball);
        imageId.add(R.drawable.submelt);
        imageId.add(R.drawable.steakcheese);
        imageId.add(R.drawable.chickbaconr);
        imageId.add(R.drawable.coke);
        imageId.add(R.drawable.cokezero);
        imageId.add(R.drawable.dietcoke);
        imageId.add(R.drawable.mtfrank);
        imageId.add(R.drawable.beefgurr);
        imageId.add(R.drawable.chickgurr);
        imageId.add(R.drawable.porkburrito);
        imageId.add(R.drawable.veggieburrito);
        imageId.add(R.drawable.porkchipburrito);
        imageId.add(R.drawable.steakchipburrito);
        imageId.add(R.drawable.steakchipburrito);
        imageId.add(R.drawable.steakchipburritobowl);
        imageId.add(R.drawable.veggieburritobowl);
        imageId.add(R.drawable.chickguerrburritobowl);
        imageId.add(R.drawable.boostimmunity);
        imageId.add(R.drawable.boostenergise);
        imageId.add(R.drawable.boostwildberry);
        imageId.add(R.drawable.boostenergy);
        imageId.add(R.drawable.boosttropical);
        imageId.add(R.drawable.boostmango);
        imageId.add(R.drawable.boostwildberry);
        imageId.add(R.drawable.urgecheese);
        imageId.add(R.drawable.urgergriller);
        imageId.add(R.drawable.urgenewyork);
        imageId.add(R.drawable.urgeboppa);
        imageId.add(R.drawable.urgepineexp);
        imageId.add(R.drawable.urgeranch);
        imageId.add(R.drawable.urgeholysheep);
        imageId.add(R.drawable.urgezorba);
        imageId.add(R.drawable.urgesouthbord);
        imageId.add(R.drawable.urgeshroom);
        imageId.add(R.drawable.chickenkebab);
        imageId.add(R.drawable.lambkebab);
        imageId.add(R.drawable.bigmac);
        imageId.add(R.drawable.cheeseburger);
        imageId.add(R.drawable.classicangus);
        imageId.add(R.drawable.quarterpounder);
        imageId.add(R.drawable.nuggets);
        imageId.add(R.drawable.chickcheese);
        imageId.add(R.drawable.smallfries);
        imageId.add(R.drawable.mediumfries);
        imageId.add(R.drawable.largefries);
        imageId.add(R.drawable.fanta);
        imageId.add(R.drawable.lift);
        imageId.add(R.drawable.sprite);
    }

}


