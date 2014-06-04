package com.groupa.dotdash.dotdash;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
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
    private Button pocketModeButton;
    private Button pocketModeHelpButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View fragmentView = inflater.inflate(R.layout.activity_settings, container, false);

        pocketModeButton = (Button)fragmentView.findViewById(R.id.pocketModeButton);
        pocketModeHelpButton = (Button)fragmentView.findViewById(R.id.pocketModeHelpButton);
        wpmNumber = (TextView)fragmentView.findViewById(R.id.wpmNumber);
        wpmSlider = (SeekBar)fragmentView.findViewById(R.id.wpmSlider);
        receiveAsTextCheckBox = (CheckBox)fragmentView.findViewById(R.id.receiveAsTextCheckBox);
        receiveAsVibrateCheckBox = (CheckBox)fragmentView.findViewById(R.id.receiveAsVibrateCheckBox);

        pocketModeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), PocketModeActivity.class);
                getActivity().startActivity(intent);
            }
        });

        pocketModeHelpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                displayAlert();
            }
        });

        wpmNumber.setText(Integer.toString(DotDash.wpm));

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

        receiveAsTextCheckBox.setChecked(DotDash.receiveAsText);
        receiveAsTextCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                DotDash.receiveAsText = b;
            }
        });

        receiveAsVibrateCheckBox.setChecked(DotDash.receiveAsVibrate);
        receiveAsVibrateCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                DotDash.receiveAsVibrate = b;
            }
        });

        return fragmentView;
    }

    private void displayAlert()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setCancelable(false)
                .setTitle("Pocket Mode Help")
                .setMessage("Pocket Mode can be used to send and receive morse messages from your pocket. " +
                       "In the main screen, the top button will play back any unread messages in the order they " +
                       "were received. Tap a contact's ID into the bottom button to start composing a message to " +
                       "that contact. In the compose screen, simply tap out the message. To send the message, " +
                       "press and hold the screen for about 3 seconds. There will be a warning vibration and two " +
                       "message sent vibrations.\n\nPocket Mode is go!")
                .setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
        AlertDialog alert = builder.create();
        alert.show();
    }
}
