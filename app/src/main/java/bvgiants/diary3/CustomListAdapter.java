package bvgiants.diary3;

import android.widget.ArrayAdapter;

/**
 * Created by kenst on 2/05/2016.
 * Needed this class to help display images in the FoodEntryActivity.  Will create a table of
 * items found in the LookupFood SQLite table.
 */
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class CustomListAdapter extends ArrayAdapter<String> {

    private final Activity context;
    private final ArrayList<String> itemname;
    private final ArrayList<Integer> imgid;

    //List Constructor
    public CustomListAdapter(Activity context, ArrayList<String> itemname, ArrayList<Integer> imgid) {
        super(context, R.layout.content_food_entry, itemname);
        // TODO Auto-generated constructor stub

        this.context=context;
        this.itemname=itemname;
        this.imgid=imgid;
    }

    //Creates required rows for each entry, setting text and images.
    public View getView(int position,View view,ViewGroup parent) {
        LayoutInflater inflater=context.getLayoutInflater();
        View rowView=inflater.inflate(R.layout.list_single, null,true);
        TextView txtTitle = (TextView) rowView.findViewById(R.id.txt);
        if (imgid != null) {
            ImageView imageView = (ImageView) rowView.findViewById(R.id.img);
            imageView.setImageResource(imgid.get(position));
        }
        TextView extratxt = (TextView) rowView.findViewById(R.id.textView1);

        txtTitle.setText(itemname.get(position));
        extratxt.setText("Description "+itemname.get(position));
        return rowView;

    };
}

