package ciafrino.presentationtimer;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;

import com.larswerkman.holocolorpicker.ColorPicker;


public class CreateStep extends Activity {
    ColorPicker picker;
    Button finish_button;
    EditText step_name;
    EditText step_text;
    SeekBar step_duration;
    Step current_step;
    Presentation current_presentation;
    PresentationDatabaseHelper databaseHelper;
    TextView step_duration_number;
    int p;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_step);
        databaseHelper = PresentationDatabaseHelper.getDatabaseHelper(this);
        Intent intent = getIntent();
        int presentation_id = intent.getIntExtra("presentation_id",-1);
        int step_id = intent.getIntExtra("step_id",-1);

        Log.d("ids",String.valueOf(step_id));
        current_presentation = databaseHelper.getPresentationbyID(presentation_id);



        picker = (ColorPicker) findViewById(R.id.picker);
        finish_button = (Button) findViewById(R.id.finish_button);
        step_name = (EditText) findViewById(R.id.step_name);
        step_text = (EditText) findViewById(R.id.step_text);
        step_duration = (SeekBar) findViewById(R.id.step_duration);
        step_duration_number = (TextView) findViewById(R.id.step_duration_number);

        current_step =  databaseHelper.getStep(presentation_id,step_id);
        if(current_step != null){
            picker.setColor(current_step.getColor());
            step_name.setText(current_step.getName());
            step_text.setText(current_step.getText());
            step_duration_number.setText(String.valueOf(current_step.getDuration()));
        }

        step_duration.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress,boolean fromUser) {
                p=progress;
                step_duration_number.setText(String.valueOf(p*3));

            }
        });



    }




    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, CreateEditPresentation.class);
        intent.putExtra("presentation_id",current_presentation.getId());
        startActivity(intent);
    }

    public void FinishStepOnClickHandler(View v) {
        int duration = p*3;
        int color = picker.getColor();
        String name = step_name.getText().toString();
        String text = step_text.getText().toString();
        if (current_step == null) {
            Step step = new Step(current_presentation.getStepNumber(), name, text, color, duration);

            databaseHelper.insertNewStep(current_presentation, step);
        }
        else{
            databaseHelper.updateStep(current_presentation.getId(), current_presentation.getName(),
                    name, current_step.getId(), duration, text,
                    color);
        }
        current_presentation.setSteps_list(databaseHelper.getPresentationSteps(current_presentation.getId()));

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
