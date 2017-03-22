/**
 * Created by Shervin on 3/20/2017.
 */
package com.example.shervin.beatviber;

import android.content.Context;
import android.icu.text.LocaleDisplayNames;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.os.CountDownTimer;
import android.media.MediaPlayer;
import java.lang.*;

import java.lang.reflect.Array;
import java.util.ArrayList;

import static com.example.shervin.beatviber.R.layout.vibrator;

class info{
    private String name;
    private String BPM;
    private String rhythm_num;
    private double[] rhythm_data;

    private ArrayList<Long> raw_time_stamp = new ArrayList();
    private ArrayList formated_time_stamp = new ArrayList();
    private ArrayList formated_rhythm_data = new ArrayList();
    private ArrayList error_number = new ArrayList();
    private long offset;
    private int beat_time;


    public info(String name, String BPM, String rhythm_num, double[] rhythm_data){
        this.name = name;
        this.BPM = BPM;
        this.beat_time = beat_time();
        this.rhythm_num = rhythm_num;
        this.rhythm_data = rhythm_data;
        this.formate_rhythms();

    }
    /*different values used for different BPM, check excel*/
    public int beat_time(){
        switch (this.BPM) {
            case ("60 BPM"):
                return 1000;
            case ("80 BPM"):
                return 750;
            case ("100 BPM"):
                return 600;
        }
        return 0;
    }
    //Minus beginning time to get time correct intervals for rhythm
    public void formate_rhythms(){
        for (int i = 0; i < this.rhythm_data.length; i ++){
            double to_add = this.rhythm_data[i] * (double)this.beat_time;
            this.formated_rhythm_data.add((long)to_add);
        }
    }
    //add time stamp, returns false if number of timestamps exceeded.
    public boolean insert_time_stamp(long curr_time){
        if(this.raw_time_stamp.isEmpty()){
            this.offset = curr_time;
        }
        Log.d("Pressed at", Long.toString(curr_time));
        this.raw_time_stamp.add(curr_time);
        if(this.raw_time_stamp.size() == this.rhythm_data.length){
            /*
            * Got all time stamps
            * need to stop
            * */
            this.formate_raw();
            this.calculate_error();
            return false;
        }
        return true;

    }
    //formate raw times stamp to time intervals
    public void formate_raw(){
        for (int i = 0; i < this.raw_time_stamp.size(); i ++){
            long to_add = this.raw_time_stamp.get(i) - this.offset;
            this.formated_time_stamp.add(to_add);
        }
    }
    //calculate error from formated rhythm and formated time stamp
    // press time - rhythm time so negative number mean user is too fast.
    public void calculate_error(){
        for (int i = 0; i < this.formated_rhythm_data.size(); i ++){
            long a = (long)formated_rhythm_data.get(i);
            long b = (long)formated_time_stamp.get(i);
            error_number.add(b - a);
        }
    }
    //Getter functions for infos
    public String get_name(){
        return this.name;
    }

    public String get_BPM(){
        return this.BPM;
    }

    public String get_rhythm_num(){
        return this.rhythm_num;
    }

    public double[] get_rhythm_data(){
        return this.rhythm_data;
    }
    public ArrayList get_formated_rhythm(){
        return this.formated_rhythm_data;
    }

    public ArrayList get_raw_time_stamps(){
        return this.raw_time_stamp;

    }
    public ArrayList get_formated_time_stamp(){
        return this.formated_time_stamp;
    }
    public ArrayList get_error(){
        return this.error_number;
    }
}

public class MainActivity extends AppCompatActivity {

    private Vibrator v;
    private RadioGroup radioGroup;
    private info user;

    /*
* All rhythm we have*/
    /*      Easy Rhythms
        1, 1, 2
        2, 1, 1

        Medium Rhythms
        1, 0.5, 0.5, 1, 1
        1, 1, 0.5, 0.5, 1

        Hard Rhythms
        1, 0.5, 0.5, 2
        0.5, 0.5, 2, 1*/
    public static final double[][] all_rhythms = new double[][]{
            {0, 1, 2, 4, 5, 6, 8, 9, 10},
            {0, 2, 3, 4 ,6, 7, 8, 10,11},
            {0, 1, 1.5, 2, 3, 4, 5, 5.5, 6, 7, 8, 9, 9.5, 10, 11},
            {0, 1, 2, 2.5, 3, 4, 5, 6, 6.5, 7, 8, 9, 10, 10.5, 11},
            {0, 1, 1.5, 2, 4, 5, 5.5, 6, 8, 9, 9.5, 10},
            {0, 0.5, 1, 3, 4, 4.5, 5, 7, 8, 8.5, 9, 11}
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Get instance of Vibrator from current Context
        v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

    }

    // pattern for 60 bpm
    long[] pattern_60 = {0, 200, 800};
    // pattern for 80 bpm
    long[] pattern_80 = {0, 200, 550};
    // pattern for 100 bpm
    long[] pattern_100 = {0, 200, 400};

    /** Called when the user taps the Send button */
    public void logMessage(View view) {
        // Do something in response to button
        Log.d("test", "hey there homie");
    }

    /*
    *  For playing haptic metronome*/
    MediaPlayer mp;
    public void play_music(){
        if (mp.isPlaying()){
            mp.stop();
        }else{
            mp.start();
        }

    }
    MediaPlayer press;
    public void play_click(){
        if (press.isPlaying()){
            press.stop();
        }else{
            press.start();
        }

    }
    CountDownTimer timer = new CountDownTimer(10, 10){
        public void onTick(long millisUntilFinished) {
        }
        public void onFinish() {
     }
    };
    CountDownTimer call_timer(int minutes,int BPM){
        int tot_time = minutes * 60000;
        int tic = tot_time/(minutes*BPM);
        CountDownTimer timer = new CountDownTimer(tot_time, tic){
            public void onTick(long millisUntilFinished) {
                play_music();
            }
            public void onFinish() {
            }
        }.start();
        return timer;
    }


    public void playMetronome(View view){

        radioGroup = (RadioGroup) findViewById(R.id.rg_bpm);
        int bpmId = radioGroup.getCheckedRadioButtonId();

        radioGroup = (RadioGroup) findViewById(R.id.rg_metronome);
        int metronomeId = radioGroup.getCheckedRadioButtonId();

        // to check bpm selection
        RadioButton bpmButton = (RadioButton) findViewById(bpmId);
        String bpm_string = bpmButton.getText().toString();

        // to check metronome selection
        RadioButton metronomeButton = (RadioButton) findViewById(metronomeId);
        String metronome_string = metronomeButton.getText().toString();

        if (metronome_string.equals("Haptic")){
            timer.cancel();
            // Cancel sound metronome if playing
            switch (bpm_string) {
                case ("60 BPM"):
                    v.vibrate(pattern_60, 0);
                    break;
                case ("80 BPM"):
                    v.vibrate(pattern_80, 0);
                    break;
                case ("100 BPM"):
                    v.vibrate(pattern_100, 0);
                    break;
            }
        } else {
            mp = MediaPlayer.create(this, R.raw.metronome);
            // play sound metronome and cancel haptic metronome
            v.cancel();
            switch (bpm_string) {
                case ("60 BPM"):
                    timer.cancel();
                    timer = call_timer(50, 60);
                    break;
                case ("80 BPM"):
                    timer.cancel();
                    timer = call_timer(50, 80);
                    break;
                case ("100 BPM"):
                    timer.cancel();
                    timer = call_timer(50, 100);
                    break;
            }
        }
    }

    public void stopMetronome (View view){
        v.cancel();
        timer.cancel();
    }

    public void setup_User(View view){;
        /*
         * Get bpm as string
         * */
        radioGroup = (RadioGroup) findViewById(R.id.rg_bpm);
        int bpmId = radioGroup.getCheckedRadioButtonId();

        RadioButton bpmButton = (RadioButton) findViewById(bpmId);
        String bpm_string = bpmButton.getText().toString();

        /*
        * Get rhythm as string
        * */
        radioGroup = (RadioGroup) findViewById(R.id.rhyms_pick);
        int rhymID = radioGroup.getCheckedRadioButtonId();
        RadioButton rhymButton = (RadioButton) findViewById(rhymID);
        String rhym_string = rhymButton.getText().toString();
        //Get name
        EditText name = (EditText)findViewById(R.id.user_name);
        String name_string = name.getText().toString();

        user = new info(name_string, bpm_string, rhym_string, all_rhythms[Character.getNumericValue(rhym_string.charAt(7)) - 1]);
        Log.d("user name         ", user.get_name());
        Log.d("user rhythm number", user.get_rhythm_num());
        Log.d("user BPM          ", user.get_BPM());
    }
    public void process_info(info user){
        //Write information of the user to file
        //############Need to implement######################
    }

    public void mainToMetronome(View view){
        setContentView(R.layout.vibrator);
    }
    public void TO_User_input(View view){
        setContentView(R.layout.user_input);
    }
    public void TO_Tap(View view){
        setup_User(view);
        setContentView(R.layout.tapping);
    }
    public void user_taped(View view){
        //press = MediaPlayer.create(this, R.raw.piano);
        //play_click();
        if(user.insert_time_stamp(System.currentTimeMillis()) == false){
            //all data got
            Log.d("time stamps raw      ", user.get_raw_time_stamps().toString());
            Log.d("Rhythm raw           ", user.get_rhythm_data().toString());
            Log.d("time stamps formate  ", user.get_formated_time_stamp().toString());
            Log.d("Rhythm formated      ", user.get_formated_rhythm().toString());
            Log.d("Time differences     ", user.get_error().toString());
            Log.d("ended", "all data collected");

            /*
            * Need to process user data before  exiting to previous page. everything is in user.
            * just output to a file using the get functions;
            * */
            process_info(user);
            TO_User_input(view);
        }
    }
}

