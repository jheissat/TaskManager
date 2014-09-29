package fr.julienheissat.ui.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import fr.julienheissat.taskmanager.R;

/**
 * Created by juju on 24/09/2014.
 */
public class PriorityListAdapter extends ArrayAdapter<String>
{
    private Context context;
    private LayoutInflater mInflater;
    private String[] priorityTitles;
    private String[] priorityDescriptions;
    private TypedArray priorityIcons;



    public PriorityListAdapter(Context context, int textViewResourceId,   String[] objects) {
        super(context, textViewResourceId, objects);
        this.context=context;
        priorityTitles = context.getResources().getStringArray(R.array.priority_title_list);
        priorityDescriptions = context.getResources().getStringArray(R.array.priority_description_list);
        priorityIcons =  context.getResources().obtainTypedArray(R.array.priority_icon_list);
    }


    @Override
    public View getDropDownView(int position, View convertView,ViewGroup parent) {
        return getCustomView(position, convertView, parent);

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    public View getCustomView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
             mInflater = (LayoutInflater)
                    context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = mInflater.inflate(R.layout.item_priority_list, null);
        }


        TextView label=(TextView)convertView.findViewById(R.id.title_priority_list);
        label.setText(priorityTitles[position]);

        TextView sub=(TextView)convertView.findViewById(R.id.description_priority_list);
        sub.setText(priorityDescriptions[position]);

        ImageView icon=(ImageView)convertView.findViewById(R.id.image_priority_list);
        icon.setImageResource(priorityIcons.getResourceId(position, -1));

        return convertView;
    }
}