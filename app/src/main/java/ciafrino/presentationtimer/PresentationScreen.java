package ciafrino.presentationtimer;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.Iterator;
import java.util.List;


public class PresentationScreen extends Activity {


    Presentation current_presentation;
    List<Step> stepList;
    int full_duration = 0;
    ProgressBar full_progress;
    ProgressBar partial_progress;
    boolean running = true;
    Step step;
    Iterator<Step> iter;
    Timer full_timer = null;
    Timer partial_timer = null;
    long dur;
    Vibrator v;

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
        TextView t = (TextView) findViewById(R.id.presentationName);
        t.setText(current_presentation.getName());
        Log.d("Presentation screen", current_presentation.getName().toString());
        stepList = current_presentation.getSteps_list();
        for (Step step : stepList){
            Log.d("Presentation screen", "step: " + step.getName().toString() + "color " + step.getText().toString() + " duration: " + String.valueOf(step.getDuration()));
            full_duration = full_duration+step.getDuration();
        }
        v = (Vibrator) this.getSystemService(Context.VIBRATOR_SERVICE);
        setTotalTimer();


    }

    public void PausePresentationOnClickHandler(View v) {
        if(full_timer != null && partial_timer != null && full_timer.isPaused()) {
            partial_timer.resume();
            full_timer.resume();
        }
        else if (full_timer != null && partial_timer != null){
            partial_timer.pause();
            full_timer.pause();

        }
    }

    public void setTotalTimer(){
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
    public void setPartialTimer(){

        getWindow().getDecorView().getRootView().setBackgroundColor(step.getColor());

        Log.d("iter", "step: " + step.getName().toString() + "color " + String.valueOf(step.getColor()) + " duration: " + String.valueOf(step.getDuration()));

        if(partial_timer == null || partial_progress.getProgress() == partial_progress.getMax()) {
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
                    if (iter.hasNext()) {
                        step = iter.next();
                        v.vibrate(500);

                        setPartialTimer();
                    }
                    else{
                        v.vibrate(2000);

                    }
                }
            };
            partial_timer.create();
            full_timer.resume();
        }

    }
    public void StartPresentationOnClickHandler(View v) throws InterruptedException {

            iter = stepList.iterator();
            step = null;
            if(iter.hasNext()) {
                step = iter.next();
            }
            if (partial_timer != null){
                partial_timer.cancel();
                partial_timer = null;
            }
            if (full_timer != null){
                full_timer.cancel();
                full_timer = null;
            }
            setTotalTimer();
            setPartialTimer();

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
