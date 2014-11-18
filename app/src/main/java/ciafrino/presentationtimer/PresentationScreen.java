package ciafrino.presentationtimer;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.List;

//import com.bravebeard.circletimerview.CircleTimerView;


public class PresentationScreen extends Activity {

//    private CircleTimerView timer;
//    boolean isRunning = false;
    Presentation current_presentation;
    List<Step> stepList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_presentation_screen);
        PresentationDatabaseHelper databaseHelper = new PresentationDatabaseHelper(this);
        Intent intent = getIntent();
        int presentation_id = intent.getIntExtra("presentation_id",-1);

        current_presentation = databaseHelper.getPresentationbyID(presentation_id);
        Log.d("Presentation screen", current_presentation.getName().toString());
        stepList = current_presentation.getSteps_list();
        for (Step step : stepList){
            Log.d("Presentation screen", "step: "+step.getName().toString()+"color "+step.getText().toString()+" duration: "+String.valueOf(step.getDuration()));
            getWindow().getDecorView().getRootView().setBackgroundColor(step.getColor());


        }



//        timer = (CircleTimerView) findViewById(R.id.timer);
//        timer.config(android.R.color.holo_orange_dark, android.R.color.holo_orange_light, 3, 5000, false);
//
//        timer.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                if (isRunning){
//                    timer.pause();
//                    isRunning = false;
//                }
//                else{
//                    timer.start();
//                    isRunning = true;
//                }
//            }
//        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.presentation_screen, menu);
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
