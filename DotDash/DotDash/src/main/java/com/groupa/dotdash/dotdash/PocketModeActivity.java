package com.groupa.dotdash.dotdash;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class PocketModeActivity extends Activity {

    private ImageButton playButton;
    private ImageButton newMessageButton;

    private String morseID;
    private long lastDown;
    private long lastDuration;
    private ArrayList<Long> pressTimes;
    private Timer charTimer;
    private Timer spaceTimer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pocket_mode);

        getActionBar().setDisplayHomeAsUpEnabled(true);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        playButton = (ImageButton)findViewById(R.id.pocketPlayButton);
        newMessageButton = (ImageButton)findViewById(R.id.pocketNewMessageButton);
        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Message unheardMessage = DataManager.getInstance().getNextMessage();
                if (unheardMessage != null) {
                    Translator.outputMessage(view.getContext(), Translator.convertTextToMorse(unheardMessage.getContact().getMorseID() + "  " + unheardMessage.getText(), DotDash.wpm));
                }
            }
        });

        pressTimes = new ArrayList<Long>();
        charTimer = new Timer(true);
        spaceTimer = new Timer(true);
        morseID = "";

        newMessageButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    newMessageButton.setBackground(getResources().getDrawable(R.drawable.touch_down_morse_button));
                    lastDown = System.currentTimeMillis();
                    charTimer.cancel();
                    spaceTimer.cancel();
                } else if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    newMessageButton.setBackground(getResources().getDrawable(R.drawable.blue_button));
                    lastDuration = System.currentTimeMillis() - lastDown;
                    pressTimes.add(lastDuration);

                    charTimer = new Timer(true);
                    charTimer.schedule(new TimerTask() {
                        @Override
                        public void run() {
                            morseID += Translator.convertMorseToText(pressTimes);
                            pressTimes.clear();
                        }
                    }, Translator.LETTER_BREAK_DURATION);

                    spaceTimer = new Timer(true);
                    spaceTimer.schedule(new TimerTask() {
                        @Override
                        public void run() {
                            Contact contact = DataManager.getInstance().getAddressBookMorseIDs().get(morseID);
                            if (contact != null) {
                                ((Vibrator)getSystemService(VIBRATOR_SERVICE)).vibrate(100);
                                Intent intent = new Intent(DotDash.appContext, PocketModeWriterActivity.class);
                                intent.putExtra(DotDash.CONTACT_NAME, contact.getName());
                                startActivity(intent);
                            }
                            morseID = "";
                        }
                    }, Translator.SPACE_DURATION);
                }

                return true;
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        onBackPressed();
        return true;
    }

}
