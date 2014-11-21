package ciafrino.presentationtimer;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


public class CreateEditPresentation extends Activity {

    Presentation current_presentation;
    PresentationDatabaseHelper databaseHelper;
    StepListAdapter adapter;
    Step currentStep;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_edit_presentation);
        Intent intent = getIntent();
        int presentation_id = intent.getIntExtra("presentation_id",-1);
        databaseHelper = PresentationDatabaseHelper.getDatabaseHelper(this);
        current_presentation = databaseHelper.getPresentationbyID(presentation_id);
        current_presentation.setSteps_list(databaseHelper.getPresentationSteps(presentation_id));
        setupListViewAdapter();
        TextView name = (TextView) findViewById(R.id.pname);
        name.setText(current_presentation.getName());
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.create_edit_presentation, menu);
        return true;
    }
    public void StepNameOnClickHandler(View v) {
        Toast toast = Toast.makeText(this,"Clicked Step",Toast.LENGTH_SHORT);
        toast.show();
        currentStep = (Step)v.getTag();
        Intent intent = new Intent(this, CreateStep.class);
        intent.putExtra("step_id", currentStep.getId());
        intent.putExtra("presentation_id",current_presentation.getId());
        startActivity(intent);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, PresentationsList.class);
        startActivity(intent);    }

    public void removeStepOnClickHandler(View v){
        currentStep = (Step)v.getTag();

        new AlertDialog.Builder(this)
                .setTitle("Delete entry")
                .setMessage("Are you sure you want to delete this entry?")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        databaseHelper.deleteStep(current_presentation, currentStep);
                        adapter.remove(currentStep);
                        adapter.notifyDataSetChanged();
                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // do nothing
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }
    private void setupListViewAdapter() {

        adapter = new StepListAdapter(CreateEditPresentation.this, R.layout.steps_item,
                current_presentation.getSteps_list());

        ListView stepListView = (ListView)findViewById(R.id.steps_list);
        stepListView.setAdapter(adapter);
    }
    public void onNameClickHandler(View v){
        AlertDialog.Builder alert = new AlertDialog.Builder(this);

        alert.setTitle("Change Name");
        alert.setMessage("Edit the name below");

        final EditText input = new EditText(this);
        input.setText(current_presentation.getName());


        alert.setView(input);

        alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                String value = input.getText().toString();
                current_presentation.setName(value);
                databaseHelper.updatePresentationName(current_presentation);
                TextView name = (TextView) findViewById(R.id.pname);
                name.setText(current_presentation.getName());

            }
        });

        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                // Canceled.
            }
        });

        alert.show();
    }


    public void addNewStepOnClickHandler(View v) {

        Intent intent = new Intent(this, CreateStep.class);
        intent.putExtra("presentation_id",current_presentation.getId());
        intent.putExtra("step_id",-1);
        startActivity(intent);
    }


}