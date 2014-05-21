package com.groupa.dotdash.dotdash;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.SeekBar;
import android.widget.TextView;

public class SettingsActivity extends DotDash{

    private SeekBar wpmSlider;
    private TextView wpmNumber;
    private CheckBox receiveAsTextCheckBox;
    private CheckBox receiveAsVibrateCheckBox;
    private CheckBox receiveAsLightCheckBox;
    private CheckBox receiveAsBeepCheckBox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        currentScreen = R.id.action_settings;

        wpmSlider = (SeekBar)findViewById(R.id.wpmSlider);
        wpmSlider.setProgress(wpm);
        wpmSlider.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                wpmNumber.setText(Integer.toString(i));
                wpm = i;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        wpmNumber = (TextView)findViewById(R.id.wpmNumber);
        wpmNumber.setText(Integer.toString(wpm));

        receiveAsTextCheckBox = (CheckBox)findViewById(R.id.receiveAsTextCheckBox);
        receiveAsTextCheckBox.setChecked(receiveAsText);
        receiveAsTextCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                receiveAsText = b;
            }
        });

        receiveAsVibrateCheckBox = (CheckBox)findViewById(R.id.receiveAsVibrateCheckBox);
        receiveAsVibrateCheckBox.setChecked(receiveAsVibrate);
        receiveAsVibrateCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                receiveAsVibrate = b;
            }
        });

        receiveAsLightCheckBox = (CheckBox)findViewById(R.id.receiveAsLightCheckBox);
        receiveAsLightCheckBox.setChecked(receiveAsLight);
        receiveAsLightCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                receiveAsLight = b;
            }
        });

        receiveAsBeepCheckBox = (CheckBox)findViewById(R.id.receiveAsBeepCheckBox);
        receiveAsBeepCheckBox.setChecked(receiveAsBeep);
        receiveAsBeepCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                receiveAsBeep = b;
            }
        });
    }

}
