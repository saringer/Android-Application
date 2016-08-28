package de.fu_berlin.agdb.adapters;

import android.content.Context;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import de.fu_berlin.agdb.R;
import de.fu_berlin.agdb.data.Constraints;

import java.util.ArrayList;

/**
 * Created by Riva on 03.06.2016.
 */
public class ConstraintsListAdapter extends ArrayAdapter<Constraints> {

    private Context mContext;
    private static ConstraintsListAdapter sInstance;


    // Create a singleton
    public static synchronized ConstraintsListAdapter getInstance(final Context context, ArrayList<Constraints> constraints) {
        if (sInstance == null) {
            sInstance = new ConstraintsListAdapter(context, constraints);
        }
        return sInstance;
    }


    public ConstraintsListAdapter(Context context, ArrayList<Constraints> constraints) {

        super(context, 0, constraints);
        mContext = context;
    }



    // Set false in order to disable the item being clickable at whole
    @Override
    public boolean isEnabled(int position) {
        return false;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        // Get the data item for this position

        final Constraints constraint = getItem(position);

        // Check if an existing view is being reused, otherwise inflate the view

        if (convertView == null) {

            convertView = LayoutInflater.from(getContext()).inflate(R.layout.constraints_list_item, parent, false);

        }

        // Lookup view for data population



        TextView removeButton = (TextView) convertView.findViewById(R.id.cancel);
        removeButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                //do something
                remove(constraint);
                notifyDataSetChanged();
            }
        });

        TextView tvHome = (TextView) convertView.findViewById(R.id.content);
        // this step is mandated for the url and clickable styles.
        tvHome.setMovementMethod(LinkMovementMethod.getInstance());

        // Populate the data into the template view using the data object



        constraint.createConstraintTextView(tvHome, this);




        // Return the completed view to render on screen

        return convertView;

    }
}
