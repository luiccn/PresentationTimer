package ciafrino.presentationtimer;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
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
    int final_text_color;
    Vibrator v;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_presentation_screen);
        PresentationDatabaseHelper databaseHelper = PresentationDatabaseHelper.getDatabaseHelper(this);
        Intent intent = getIntent();
        int presentation_id = intent.getIntExtra("presentation_id",-1);
        full_progress = (ProgressBar) findViewById(R.id.totalProgressBar);
        partial_progress = (ProgressBar) findViewById(R.id.stepProgressBar);

        current_presentation = databaseHelper.getPresentationbyID(presentation_id);
        TextView name = (TextView) findViewById(R.id.presentationName);
        name.setText(current_presentation.getName());
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
        TextView text = (TextView) findViewById(R.id.pause_button);
        if(full_timer != null && partial_timer != null && full_timer.isPaused()) {
            partial_timer.resume();
            full_timer.resume();
            text.setText("Pause");
        }
        else if (full_timer != null && partial_timer != null){
            partial_timer.pause();
            full_timer.pause();
            text.setText("Resume");

        }
    }

    public void setTotalTimer(){
        full_timer = new Timer(full_duration*1000, 1, false) {
            TextView totaltime = (TextView) findViewById(R.id.total);
            @Override
            public void onTick(long millisUntilFinished) {
                long dur = 1000*full_duration;
                int mp = (int) (100*(dur-millisUntilFinished)/dur);

                if(mp == 99){mp+=1;};


                full_progress.setProgress(mp);
                totaltime.setText("You have "+String.valueOf(millisUntilFinished/1000)+"s left");
            }

            @Override
            public void onFinish() {

            }
        };
        full_timer.create();





    }
    public void setPartialTimer(){


        getWindow().getDecorView().getRootView().setBackgroundColor(step.getColor());
        final TextView partialtext = (TextView) findViewById(R.id.stepInfo);

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
                    partialtext.setText("You have "+String.valueOf(millisUntilFinished/1000)+"s on this step");
                    partial_progress.setProgress(mp);

                }

                @Override
                public void onFinish() {
                    if (iter.hasNext()) {
                        step = iter.next();
                        v.vibrate(500);
                        TextView pname = (TextView) findViewById(R.id.presentationName);
                        TextView sinfo = (TextView) findViewById(R.id.stepInfo);
                        TextView sname = (TextView) findViewById(R.id.stepName);
                        TextView totaltime = (TextView) findViewById(R.id.total);
                        sname.setText(step.getName());
                        int final_text_color = calculateColor(Color.red(step.getColor()),Color.blue(step.getColor()),Color.green(step.getColor()));

                        TextView description  = (TextView) findViewById(R.id.description);
                        description.setText(step.getText());

                        description.setTextColor(final_text_color);
                        sname.setTextColor(final_text_color);
                        pname.setTextColor(final_text_color);
                        totaltime.setTextColor(final_text_color);
                        sinfo.setTextColor(final_text_color);

                        setPartialTimer();
                    }
                    else{
                        v.vibrate(2000);
                        TextView sname = (TextView) findViewById(R.id.stepName);
                        sname.setText("Your presentation has ended");
                    }
                }
            };
            partial_timer.create();
            full_timer.resume();
        }

    }

    public int calculateColor(int red,int blue, int green){

        int r = 255 - red;
        int b = 255 - blue;
        int g = 255 - green;



        float [] hsv = new float[3];
        Color.colorToHSV(Color.rgb(r,g,b), hsv);
        float h = hsv[0];
        hsv[0] = Math.abs(180 - hsv[0]);
        if( Math.abs(h - hsv[0]) > 150){
            hsv[1] = Math.abs((float)1.0 - hsv[1]);
        }
        int color = Color.HSVToColor(hsv);

        return color;
    }

    public void StartPresentationOnClickHandler(View v) throws InterruptedException {
        TextView pause = (TextView) findViewById(R.id.pause_button);
        TextView start = (TextView) findViewById(R.id.start_button);

        iter = stepList.iterator();
            step = null;
        int final_text_color;
            if(iter.hasNext()) {
                step = iter.next();


                final_text_color = calculateColor(Color.red(step.getColor()),Color.blue(step.getColor()),Color.green(step.getColor()));
                TextView pname = (TextView) findViewById(R.id.presentationName);
                TextView sname = (TextView) findViewById(R.id.stepName);
                TextView totaltime = (TextView) findViewById(R.id.total);
                TextView sinfo = (TextView) findViewById(R.id.stepInfo);
                sname.setText(step.getName());
                TextView description  = (TextView) findViewById(R.id.description);
                description.setText(step.getText());
                description.setTextColor(final_text_color);
                sname.setTextColor(final_text_color);
                pname.setTextColor(final_text_color);
                totaltime.setTextColor(final_text_color);
                sinfo.setTextColor(final_text_color);


            }
            if (partial_timer != null){
                pause.setText("Pause");
                start.setText("Restart");
                partial_timer.cancel();
                partial_timer = null;
            }
            if (full_timer != null){
                pause.setText("Pause");
                start.setText("Restart");
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
