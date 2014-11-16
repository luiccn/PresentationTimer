package ciafrino.presentationtimer;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import java.util.List;


public class CreateEditPresentation extends Activity {

    Presentation current_presentation;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_edit_presentation);
        Intent intent = getIntent();
        int presentation_id = intent.getIntExtra("presentation_id",-1);
        List<Presentation> list = ((Values) getApplication()).getPresentations_list();
        for (Presentation presentation : list){
            System.out.println(presentation_id);
            if(presentation.getId() == presentation_id){
                  current_presentation = presentation;
                break;
            }
        }
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

    public void addNewStepOnClickHandler(View v) {
        Toast toast = Toast.makeText(this, "Clicked New Step", Toast.LENGTH_SHORT);
        toast.show();
        Intent intent = new Intent(this, CreateStep.class);
        intent.putExtra("presentation_id",current_presentation.getId());
        startActivity(intent);
    }
}