package ciafrino.presentationtimer;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Toast;

import com.larswerkman.holocolorpicker.ColorPicker;


public class CreateStep extends Activity {
    ColorPicker picker;
    Button finish_button;
    EditText step_name;
    EditText step_text;
    SeekBar step_duration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_step);

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


    public void FinishStepOnClickHandler(View v) {
        int duration = step_duration.getProgress();
        int color = picker.getColor();
        String name = step_name.getText().toString();
        String text = step_text.getText().toString();


        Log.d("Finish Step",String.valueOf(duration)+'\t'+name+'\t'+text+'\t'+String.valueOf(color)  );





        Intent intent = new Intent(this, CreateEditPresentation.class);
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
