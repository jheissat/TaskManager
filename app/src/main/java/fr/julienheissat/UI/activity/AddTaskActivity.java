package fr.julienheissat.ui.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Address;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.Date;

import fr.julienheissat.application.TaskManagerApplication;
import fr.julienheissat.modelController.Task;
import fr.julienheissat.taskmanager.R;
import fr.julienheissat.ui.adapter.PriorityListAdapter;
import fr.julienheissat.ui.adapter.ProjectListAdapter;


public class AddTaskActivity extends ActionBarActivity
{

    private static final int REQUEST_CHOOSE_ADDRESS = 0;
    private final static int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;
    private EditText taskNameEditText;
    private Spinner taskProjectSpinner;
    private Spinner taskPrioritySpinner;
    private Button addButton;
    private Button cancelButton;
    private boolean changesPending;
    private AlertDialog unsavedChangesDialog;
    private Address address;
    private Button addLocationButton;
    private TextView addressText;
    private Task t;

    private String[] priorityTitles;
    private String[] projectTitles;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);
        t = new Task();
        t.setDate(new Date().getTime());
        setUpViews();
    }

    private void addTask() {
        String taskName = taskNameEditText.getText().toString();

        if (taskName.length()==0 || taskName.length()>30 ||t.getPriority() == null || t.getProject() == null)
        {
            return;
        }
        t.setName(taskName);
        t.setAddress(address);
        getTaskManagerApplication().getTaskListController().addTask(t);
        finish();


    }

    protected TaskManagerApplication getTaskManagerApplication() {
        TaskManagerApplication tma = (TaskManagerApplication) getApplication();
        return tma;

    }


    private void cancel() {
        String taskName = taskNameEditText.getText().toString();

        if (changesPending && !taskName.equals("")) {
            unsavedChangesDialog = new AlertDialog.Builder(this)
                .setTitle(R.string.unsaved_changes_title)
                .setMessage(R.string.unsaved_changes_message)
                .setPositiveButton(R.string.add_task, new AlertDialog.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        addTask();
                    }
                })
                .setNeutralButton(R.string.discard, new AlertDialog.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        finish();
                    }
                })
                .setNegativeButton(R.string.cancel, new AlertDialog.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        unsavedChangesDialog.cancel();

                    }
                })
                .create();
                unsavedChangesDialog.show();
        }
        else {

            finish();

        }

    }




    private void setUpViews() {

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        taskNameEditText = (EditText) findViewById(R.id.task_name);
        addButton = (Button) findViewById(R.id.add_button);
        cancelButton = (Button) findViewById(R.id.cancel_button);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addTask();
            }
        });
        addLocationButton = (Button) findViewById(R.id.add_location_button);
        addressText = (TextView) findViewById(R.id.address_text);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cancel();
            }
        });



        taskNameEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                changesPending = true;
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        setupSpinners();

    }


    public void setupSpinners() {

        taskProjectSpinner = (Spinner) findViewById(R.id.project_spinner);
        projectTitles = new String[]{"Personal", "Work", "Family"};

        taskProjectSpinner.setAdapter(new ProjectListAdapter(this,R.layout.item_project_list,projectTitles));

        taskProjectSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l)
            {

                String sProject = taskProjectSpinner.getSelectedItem().toString();
                if (sProject != "Project:")
                {
                    t.setProject(sProject);
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView)
            {

            }
        });


        taskPrioritySpinner = (Spinner) findViewById(R.id.priority_spinner);
        priorityTitles = getResources().getStringArray(R.array.priority_title_list);
        taskPrioritySpinner.setAdapter(new PriorityListAdapter(this,R.layout.item_priority_list,priorityTitles));
        taskPrioritySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l)
            {
                String sPriority = taskPrioritySpinner.getSelectedItem().toString();
                if (sPriority != "Priority:")
                {
                    t.setPriority(sPriority);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView)
            {

            }
        });


    }



    public void addLocationButtonClicked(View view)
    {
        Intent intent = new Intent(this,AddLocationMapActivity.class);
        startActivityForResult(intent,REQUEST_CHOOSE_ADDRESS);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
      if (REQUEST_CHOOSE_ADDRESS==requestCode && RESULT_OK==resultCode)
      {
          address = data.getParcelableExtra(AddLocationMapActivity.ADDRESS_RESULT);
      }
       else
      {

          super.onActivityResult(requestCode,resultCode,data);
      }
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        if (null == address)
        {
            addLocationButton.setVisibility(View.VISIBLE);
            addressText.setVisibility(View.GONE);
        }
        else
        {
            addLocationButton.setVisibility(View.GONE);
            addressText.setVisibility(View.VISIBLE);
            addressText.setText(address.getAddressLine(0));
        }

    }

}
