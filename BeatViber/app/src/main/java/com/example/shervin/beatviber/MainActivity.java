/**
 * Created by Shervin on 3/20/2017.
 */
package com.example.shervin.beatviber;

import android.content.Context;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import static com.example.shervin.beatviber.R.layout.vibrator;

public class MainActivity extends AppCompatActivity {


    private Vibrator v;
    private RadioGroup radioGroup;

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
            // play sound metronome and cancel haptic metronome
        }
    }

    public void stopMetronome (View view){

        v.cancel();
    }

    public void mainToMetronome(View view){
        setContentView(R.layout.vibrator);
    }
}

