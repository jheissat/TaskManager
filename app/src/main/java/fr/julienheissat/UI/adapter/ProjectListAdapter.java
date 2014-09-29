package fr.julienheissat.ui.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import fr.julienheissat.taskmanager.R;

/**
 * Created by juju on 24/09/2014.
 */
public class ProjectListAdapter extends ArrayAdapter<String>
{
    private Context context;
    private LayoutInflater mInflater;
    private String[] projectTitles;

    public ProjectListAdapter(Context context, int textViewResourceId,   String[] objects) {
        super(context, textViewResourceId, objects);
        this.context=context;
        projectTitles = objects;
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
            convertView = mInflater.inflate(R.layout.item_project_list, null);
        }


        TextView label=(TextView)convertView.findViewById(R.id.title_project_list);
        label.setText(projectTitles[position]);


        View rectangle= convertView.findViewById(R.id.rectangle);
        rectangle.setBackgroundColor(0x8adcb300);

        return convertView;
    }
}