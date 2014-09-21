package fr.julienheissat.ui.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.CheckedTextView;
import android.widget.LinearLayout;
import android.widget.TextView;

import fr.julienheissat.modelController.Task;
import fr.julienheissat.taskmanager.R;

/**
 * Created by juju on 15/09/2014.
 */

public class TaskListItem extends LinearLayout {

    private CheckedTextView checkbox;
    private Task task;
    private TextView addressText;

    public TaskListItem(Context context, AttributeSet attrs) {
        super(context, attrs);

    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        checkbox = (CheckedTextView) findViewById(android.R.id.text1);
        addressText = (TextView) findViewById(R.id.address_text_list);
    }

    public Task getTask() {
        return task;
    }

    public void setTask(Task task) {
        this.task = task;
        checkbox.setText(task.getName());
        checkbox.setChecked(task.isComplete());
        if (task.hasAddress())
        {
            addressText.setText(task.getAddress());
            addressText.setVisibility(View.VISIBLE);
        }
        else
        {
            addressText.setVisibility(View.GONE);
        }

    }
}
