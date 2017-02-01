package bvgiants.diary3;

import android.app.Fragment;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.ArrayList;

/**
 * Created by kenst on 6/05/2016.
 * This fragment holds the EatActivity's displayed food depending on what button is pressed.
 */
public class ExpandableListFragmentEAT extends Fragment {

    private ArrayList<OrderRow> todaysFoodOrders = new ArrayList<>();
    private ArrayList<OrderRow> weeklyFoodOrders = new ArrayList<>();
    private ArrayList<OrderRow> monthlyFoodOrders = new ArrayList<>();
    private ArrayList<FoodItem> allFood = new ArrayList<>();
    private ArrayList<FoodItem> foodDisplayed = new ArrayList<>();
    private ArrayList<Integer> foodDisplayedImages = new ArrayList<Integer>();
    private ArrayList<Integer> imageId = new ArrayList<Integer>();
    private EatActivity activity;
    private LayoutInflater inflater;
    // 0=Today 1=Week 2=Month
    private int SELECTION; //Nessesary to determin what button was pushed from previous activity

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        this.inflater = inflater;

        activity = (EatActivity) getActivity();
        todaysFoodOrders = activity.getTodaysOrders();
        weeklyFoodOrders = activity.getWeeklyOrders();
        monthlyFoodOrders = activity.getMonthlyOrders();
        SELECTION = activity.getSelection();
        allFood = activity.getAllFood();
        addFoodImages(); //Creates all food images manually

        return workOutWhichTimePeriod();

    }


    public class SavedTabsListAdapter extends BaseExpandableListAdapter {

        @Override
        public int getGroupCount() {
            return foodDisplayed.size();
        }

        @Override
        public int getChildrenCount(int groupPosition) {
            return foodDisplayed.get(groupPosition).children.size();
        }

        @Override
        public FoodItem getGroup(int groupPosition) {
            return foodDisplayed.get(groupPosition);
        }

        @Override
        public Object getChild(int groupPosition, int childPosition){
            return foodDisplayed.get(groupPosition).children.get(childPosition);
        }

        @Override
        public long getGroupId(int i) {
            return i;
        }

        @Override
        public long getChildId(int i, int i1) {
            return i1;
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }

        @Override
        public View getGroupView(int i, boolean b, View view, ViewGroup viewGroup) {
            //Depending on the users button press selection, this will now build the required foods
            //to display
        if(SELECTION == 0){
            View rowView = inflater.inflate(R.layout.consumed_foods_eat_activity, null, true);
            TextView txtTitle = (TextView) rowView.findViewById(R.id.consumed_txt);
            if (foodDisplayedImages != null) {
                ImageView imageView = (ImageView) rowView.findViewById(R.id.consumed_img);
                imageView.setImageResource(foodDisplayedImages.get(i));
            }
            TextView extratxt = (TextView) rowView.findViewById(R.id.consumed_textView1);

            txtTitle.setText("Food\n" + foodDisplayed.get(i).getName());
            extratxt.setText("When: "  + todaysFoodOrders.get(i).getDate() + " " + todaysFoodOrders.get(i).getTime() +
                    "\nWhere: " + todaysFoodOrders.get(i).getLocation());
            return rowView;
        }
        else if (SELECTION == 1){
            View rowView = inflater.inflate(R.layout.consumed_foods_eat_activity, null, true);
            TextView txtTitle = (TextView) rowView.findViewById(R.id.consumed_txt);
            if (foodDisplayedImages != null) {
                ImageView imageView = (ImageView) rowView.findViewById(R.id.consumed_img);
                imageView.setImageResource(foodDisplayedImages.get(i));
            }
            TextView extratxt = (TextView) rowView.findViewById(R.id.consumed_textView1);

            txtTitle.setText("Food\n" + foodDisplayed.get(i).getName());
            extratxt.setText("When: "  + weeklyFoodOrders.get(i).getDate() + " " + weeklyFoodOrders.get(i).getTime() +
                    "\nWhere: " + weeklyFoodOrders.get(i).getLocation());
            return rowView;
        }
        else {
            View rowView = inflater.inflate(R.layout.consumed_foods_eat_activity, null, true);
            TextView txtTitle = (TextView) rowView.findViewById(R.id.consumed_txt);
            if (foodDisplayedImages != null) {
                ImageView imageView = (ImageView) rowView.findViewById(R.id.consumed_img);
                imageView.setImageResource(foodDisplayedImages.get(i));
            }
            TextView extratxt = (TextView) rowView.findViewById(R.id.consumed_textView1);

            txtTitle.setText("Food\n" + foodDisplayed.get(i).getName());
            extratxt.setText("When: "  + monthlyFoodOrders.get(i).getDate() + " " + monthlyFoodOrders.get(i).getTime() +
                    "\nWhere: " + monthlyFoodOrders.get(i).getLocation());
            return rowView;
        }


        }

        @Override
        public View getChildView(int i, int i1, boolean b, View view, ViewGroup viewGroup) {
            TextView textView = new TextView(ExpandableListFragmentEAT.this.getActivity());
            textView.setText(getChild(i, i1).toString());
            textView.setBackgroundColor(Color.parseColor("#B2EBF2"));
            return textView;
        }

        @Override
        public boolean isChildSelectable(int i, int i1) {
            return true;
        }

    }

    //Will inflate the correct food depending on selection
    public View workOutWhichTimePeriod(){

        Log.v("SELECTION =", String.valueOf(SELECTION));
        if (SELECTION == 0) {
            for (int i = 0; i < todaysFoodOrders.size(); i++) {
                for (int k = 0; k < allFood.size(); k++) {
                    if (todaysFoodOrders.get(i).getFoodId() == allFood.get(k).getFoodId()) {
                        foodDisplayed.add(allFood.get(k));
                        foodDisplayedImages.add(imageId.get(k));
                    }
                }
            }
            for (int i = 0; i < foodDisplayed.size(); i++) {
                if(foodDisplayed.get(i).children.size() <=0)
                    foodDisplayed.get(i).children.add(foodDisplayed.get(i).toString());
            }

            if (todaysFoodOrders.isEmpty() == false) {
                View v = inflater.inflate(R.layout.expandable_list, null);
                ExpandableListView listView = (ExpandableListView) v.findViewById(R.id.expandable_list_view);
                listView.setAdapter(new SavedTabsListAdapter());
                return v;
            } else {
                View vv = inflater.inflate(R.layout.expandable_list, null);
                return vv;
            }
        }
        else if (SELECTION == 1) {
            for (int i = 0; i < weeklyFoodOrders.size(); i++) {
                for (int k = 0; k < allFood.size(); k++) {
                    if (weeklyFoodOrders.get(i).getFoodId() == allFood.get(k).getFoodId()) {
                        foodDisplayed.add(allFood.get(k));
                        foodDisplayedImages.add(imageId.get(k));
                    }
                }
            }
            for (int i = 0; i < foodDisplayed.size(); i++) {
                if(foodDisplayed.get(i).children.size() <=0)
                    foodDisplayed.get(i).children.add(foodDisplayed.get(i).toString());
            }

            if (weeklyFoodOrders.isEmpty() == false) {
                View v = inflater.inflate(R.layout.expandable_list, null);
                ExpandableListView listView = (ExpandableListView) v.findViewById(R.id.expandable_list_view);
                listView.setAdapter(new SavedTabsListAdapter());
                return v;
            } else {
                View vv = inflater.inflate(R.layout.expandable_list, null);
                return vv;
            }
        }
        else{
            for (int i = 0; i < monthlyFoodOrders.size(); i++) {
                for (int k = 0; k < allFood.size(); k++) {
                    if (monthlyFoodOrders.get(i).getFoodId() == allFood.get(k).getFoodId()) {
                        foodDisplayed.add(allFood.get(k));
                        foodDisplayedImages.add(imageId.get(k));
                    }
                }
            }
            for (int i = 0; i < foodDisplayed.size(); i++) {
                if(foodDisplayed.get(i).children.size() <=0)
                    foodDisplayed.get(i).children.add(foodDisplayed.get(i).toString());
            }

            if (monthlyFoodOrders.isEmpty() == false) {
                View v = inflater.inflate(R.layout.expandable_list, null);
                ExpandableListView listView = (ExpandableListView) v.findViewById(R.id.expandable_list_view);
                listView.setAdapter(new SavedTabsListAdapter());
                return v;
            } else {
                View vv = inflater.inflate(R.layout.expandable_list, null);
                return vv;
            }
        }

    }

    //Adds all images to the array.  This was nessesary due to not being able to grab these id's
    //automatically at run time.  Ideal world would have these stored on a websever and the app scrapes
    //images from server.
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
