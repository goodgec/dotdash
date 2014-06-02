package com.groupa.dotdash.dotdash;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.SeekBar;
import android.widget.TextView;

public class SettingsFragment extends Fragment {

    private SeekBar wpmSlider;
    private TextView wpmNumber;
    private CheckBox receiveAsTextCheckBox;
    private CheckBox receiveAsVibrateCheckBox;
    private CheckBox receiveAsLightCheckBox;
    private CheckBox receiveAsBeepCheckBox;
    private Button pocketModeButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View fragmentView = inflater.inflate(R.layout.activity_settings, container, false);
        pocketModeButton = (Button)fragmentView.findViewById(R.id.pocketModeButton);
        pocketModeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), PocketModeActivity.class);
                getActivity().startActivity(intent);
            }
        });
        wpmNumber = (TextView)fragmentView.findViewById(R.id.wpmNumber);
        wpmNumber.setText(Integer.toString(DotDash.wpm));

        wpmSlider = (SeekBar)fragmentView.findViewById(R.id.wpmSlider);
        wpmSlider.setProgress(DotDash.wpm);
        wpmSlider.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                wpmNumber.setText(Integer.toString(i + 1));
                DotDash.wpm = i + 1;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        receiveAsTextCheckBox = (CheckBox)fragmentView.findViewById(R.id.receiveAsTextCheckBox);
        receiveAsTextCheckBox.setChecked(DotDash.receiveAsText);
        receiveAsTextCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                DotDash.receiveAsText = b;
            }
        });

        receiveAsVibrateCheckBox = (CheckBox)fragmentView.findViewById(R.id.receiveAsVibrateCheckBox);
        receiveAsVibrateCheckBox.setChecked(DotDash.receiveAsVibrate);
        receiveAsVibrateCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                DotDash.receiveAsVibrate = b;
            }
        });

        return fragmentView;
    }
}
