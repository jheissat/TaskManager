package fr.julienheissat.application;

import android.app.Application;
import android.database.sqlite.SQLiteDatabase;

import com.google.android.gms.maps.model.LatLng;

import fr.julienheissat.database.TaskSQLiteOpenHelper;
import fr.julienheissat.model.TaskList;

/**
 * Created by juju on 15/09/2014.
 */
public class TaskManagerApplication extends Application {


    private SQLiteDatabase database;
    private LatLng latLng;
    private TaskList taskList;

    @Override
    public void onCreate()
    {
        super.onCreate();
        TaskSQLiteOpenHelper helper= new TaskSQLiteOpenHelper(this);
        database = helper.getWritableDatabase();
        taskList = new TaskList(database);

    }

    public TaskList getTaskList()
    {
        return taskList;
    }


}
