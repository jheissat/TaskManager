package fr.julienheissat.ui.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Address;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import fr.julienheissat.model.Task;
import fr.julienheissat.taskmanager.R;


public class AddTaskActivity extends TaskManagerActivity
{

    private static final int REQUEST_CHOOSE_ADDRESS = 0;
    private final static int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;
    private EditText taskNameEditText;
    private Button addButton;
    private Button cancelButton;
    private boolean changesPending;
    private AlertDialog unsavedChangesDialog;
    private Address address;
    private Button addLocationButton;
    private TextView addressText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);
        setUpViews();
    }

    private void addTask() {
        String taskName = taskNameEditText.getText().toString();
        Task t = new Task(taskName);
        t.setAddress(address);
        getTaskManagerApplication().getTaskList().addTask(t);
        finish();


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
