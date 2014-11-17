package ciafrino.presentationtimer;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

import java.util.List;


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
        List<Presentation> list = ((Values) getApplication()).getPresentations_list();
        for (Presentation presentation : list){
            if(presentation.getId() == presentation_id){
                current_presentation = presentation;
                break;
            }
        }
        databaseHelper = new PresentationDatabaseHelper(this);

        current_presentation.setSteps_list(databaseHelper.getPresentationSteps(presentation_id));
        setupListViewAdapter();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.create_edit_presentation, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
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


    public void addNewStepOnClickHandler(View v) {

        Intent intent = new Intent(this, CreateStep.class);
        intent.putExtra("presentation_id",current_presentation.getId());
        startActivity(intent);
    }
}