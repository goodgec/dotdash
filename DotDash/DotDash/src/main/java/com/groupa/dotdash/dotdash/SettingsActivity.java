package com.groupa.dotdash.dotdash;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.SeekBar;
import android.widget.TextView;

public class SettingsActivity extends DotDash implements SeekBar.OnSeekBarChangeListener {

    private SeekBar wpmSlider;
    private TextView wpmNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        currentScreen = R.id.action_settings;

        wpmSlider = (SeekBar)findViewById(R.id.wpmSlider);
        wpmSlider.setOnSeekBarChangeListener(this);

        wpmNumber = (TextView)findViewById(R.id.wpmNumber);
        wpmNumber.setText(Integer.toString(wpmSlider.getProgress()));
    }


    @Override
    public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
        wpmNumber.setText(Integer.toString(i));
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }
}
