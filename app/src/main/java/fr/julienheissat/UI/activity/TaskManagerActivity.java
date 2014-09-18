package fr.julienheissat.ui.activity;

import android.support.v7.app.ActionBarActivity;

import fr.julienheissat.application.TaskManagerApplication;

/**
 * Created by juju on 15/09/2014.
 */
public class TaskManagerActivity extends ActionBarActivity {


    protected TaskManagerApplication getTaskManagerApplication() {
        TaskManagerApplication tma = (TaskManagerApplication) getApplication();
        return tma;

    }
}
