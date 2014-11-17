package ciafrino.presentationtimer;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;

import com.larswerkman.holocolorpicker.ColorPicker;

import java.util.List;


public class CreateStep extends Activity {
    ColorPicker picker;
    Button finish_button;
    EditText step_name;
    EditText step_text;
    SeekBar step_duration;
    Presentation current_presentation;
    PresentationDatabaseHelper databaseHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_step);
        databaseHelper = new PresentationDatabaseHelper(this);
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
         picker = (ColorPicker) findViewById(R.id.picker);
         finish_button = (Button) findViewById(R.id.finish_button);
         step_name = (EditText) findViewById(R.id.step_name);
         step_text = (EditText) findViewById(R.id.step_text);
         step_duration = (SeekBar) findViewById(R.id.step_duration);

        picker.setOnColorChangedListener(new ColorPicker.OnColorChangedListener() {
            @Override
            public void onColorChanged(int i) {
                getWindow().getDecorView().getRootView().setBackgroundColor(i);
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, CreateEditPresentation.class);
        startActivity(intent);
    }

    public void FinishStepOnClickHandler(View v) {
        int duration = step_duration.getProgress();
        int color = picker.getColor();
        String name = step_name.getText().toString();
        String text = step_text.getText().toString();

        Step step = new Step(current_presentation.getStepNumber(),name, text, color, duration);

        databaseHelper.insertNewStep(current_presentation,step);
        System.out.println("Finish Step" + String.valueOf(duration)+'\t'+name+'\t'+text+'\t'+String.valueOf(color)  );
        current_presentation.setSteps_list(databaseHelper.getPresentationSteps(current_presentation.getId()));
        System.out.println("NUMBER" + current_presentation.getStepNumber());

        Intent intent = new Intent(this, CreateEditPresentation.class);
        intent.putExtra("presentation_id",current_presentation.getId());
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.create_step, menu);
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
}
