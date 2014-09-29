package fr.julienheissat.ui.views;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.CheckedTextView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.HashMap;

import fr.julienheissat.modelController.Task;
import fr.julienheissat.taskmanager.R;

/**
 * Created by juju on 15/09/2014.
 */

public class TaskListItem extends RelativeLayout {


    private ImageView imagePriority;
    private CheckedTextView checkbox;
    private TextView projectView;
    private TextView dateView;

    private Task task;
//    private TextView addressText;

    public TaskListItem(Context context, AttributeSet attrs) {
        super(context, attrs);

    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        imagePriority = (ImageView) findViewById(R.id.list_image);
        checkbox = (CheckedTextView) findViewById(R.id.list_title);
        projectView = (TextView) findViewById(R.id.list_project);
        dateView = (TextView) findViewById(R.id.list_date);


    }

    public Task getTask() {
        return task;
    }

    public void setTask(Task task) {
        this.task = task;
        Drawable draw;
        String taskPriorityNumber;
        checkbox.setText(task.getName());
        checkbox.setChecked(task.isComplete());

        HashMap<String,Integer> mapPriorityImage= new HashMap<String,Integer>();
        mapPriorityImage.put("P1",R.drawable.ic_p1);
        mapPriorityImage.put("P2",R.drawable.ic_p2);
        mapPriorityImage.put("P3",R.drawable.ic_p3);
        mapPriorityImage.put("P4",R.drawable.ic_p4);
        mapPriorityImage.put("P5",R.drawable.ic_p5);
        mapPriorityImage.put("P6",R.drawable.ic_p6);

        if (task.getPriority()!=null)
        {
            taskPriorityNumber = task.getPriority().substring(0, 2);
            Log.d("TaskListItem - SetTask", "String of task:" + taskPriorityNumber);
            if (mapPriorityImage.containsKey(taskPriorityNumber))
            {
                draw = getResources().getDrawable(mapPriorityImage.get(taskPriorityNumber));
                imagePriority.setImageDrawable(draw);

            }
        }
        projectView.setText(task.getProject());
        dateView.setText(task.getDateString());


    }
}
