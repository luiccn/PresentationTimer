package ciafrino.presentationtimer;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import java.util.Iterator;
import java.util.List;

import static android.os.SystemClock.sleep;


public class PresentationScreen extends Activity {


    Presentation current_presentation;
    List<Step> stepList;
    int full_duration = 0;
    ProgressBar full_progress;
    ProgressBar partial_progress;
    boolean running = true;
    Step step;
    Iterator<Step> iter;
    Timer full_timer;
    Timer partial_timer;
    long dur;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_presentation_screen);
        PresentationDatabaseHelper databaseHelper = new PresentationDatabaseHelper(this);
        Intent intent = getIntent();
        int presentation_id = intent.getIntExtra("presentation_id",-1);
        full_progress = (ProgressBar) findViewById(R.id.progressBar1);
        partial_progress = (ProgressBar) findViewById(R.id.progressBar2);

        current_presentation = databaseHelper.getPresentationbyID(presentation_id);
        Log.d("Presentation screen", current_presentation.getName().toString());
        stepList = current_presentation.getSteps_list();
        for (Step step : stepList){
            Log.d("Presentation screen", "step: " + step.getName().toString() + "color " + step.getText().toString() + " duration: " + String.valueOf(step.getDuration()));
            full_duration = full_duration+step.getDuration();
        }

        full_timer = new Timer(full_duration*1000, 1, false) {
            @Override
            public void onTick(long millisUntilFinished) {
                long dur = 1000*full_duration;
                int mp = (int) (100*(dur-millisUntilFinished)/dur);

                if(mp == 99){mp+=1;};

                full_progress.setProgress(mp);

            }

            @Override
            public void onFinish() {

            }
        };

        full_timer.create();

    }

    public void PausePresentationOnClickHandler(View v) {
        partial_timer.pause();
        full_timer.pause();
    }
    public void setTimer(){

        getWindow().getDecorView().getRootView().setBackgroundColor(step.getColor());

        Log.d("iter", "step: " + step.getName().toString() + "color " + String.valueOf(step.getColor()) + " duration: " + String.valueOf(step.getDuration()));

        partial_timer = new Timer(step.getDuration() * 1000, 1, true) {
            @Override
            public void onTick(long millisUntilFinished) {
                long dur = 1000 * step.getDuration();
                int mp = (int) (100 * (dur - millisUntilFinished) / dur);
                if (mp == 99) {
                    mp += 1;
                }


                partial_progress.setProgress(mp);

            }

            @Override
            public void onFinish() {
                if(iter.hasNext()) {
                    step = iter.next();
                    setTimer();
                }
            }
        };
        if(partial_timer.isPaused()){
            partial_timer.resume();
        }
        else {
            partial_timer.create();
        }
        if(full_timer.totalCountdown() != full_timer.timePassed()) {
            full_timer.resume();
        }
        else{
            full_timer.create();
        }
    }
    public void StartPresentationOnClickHandler(View v) throws InterruptedException {


            iter = stepList.iterator();

            if(iter.hasNext()) {
                step = iter.next();
            }
            setTimer();

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
