package fr.julienheissat.ui.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import fr.julienheissat.model.TaskList;
import fr.julienheissat.taskmanager.R;
import fr.julienheissat.ui.views.TaskListItem;

/**
 * Created by juju on 15/09/2014.
 */
public class TaskListAdapter extends BaseAdapter implements TaskList.IModelListener
{

    private TaskList tasks;
    private Context context;

    //Constructor

    public TaskListAdapter(TaskList tasks, Context context)
    {
        this.tasks = tasks;
        this.tasks.register(this);
        this.context = context;
    }

    public void forceReload() {

        notifyDataSetChanged();
    }


    //Tasklist listener overriding method

    @Override
    public void modelChanged(TaskList taskList)
    {
        notifyDataSetChanged();
    }



    //List Adapter overriding method

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        TaskListItem tli;

        if (null == view) {
            tli = (TaskListItem) View.inflate(context, R.layout.task_list_item, null);
        } else {
            tli = (TaskListItem) view;
        }

        tli.setTask(tasks.get(i));
        return tli;
    }

    @Override
    public int getCount() {
        return tasks.getSize();
    }

    @Override
    public Object getItem(int i) {
        return (null == tasks) ? null : tasks.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }


}

