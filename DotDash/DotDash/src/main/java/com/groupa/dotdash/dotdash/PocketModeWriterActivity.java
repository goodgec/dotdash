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

public class PocketModeWriterActivity extends Activity {

    private ImageButton composeButton;

    private long lastDown;
    private long lastDuration;
    private ArrayList<Long> pressTimes;
    private String messageText;
    private Timer charTimer;
    private Timer spaceTimer;
    private Timer warningTimer;
    private Timer sendTimer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pocket_mode_writer);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        composeButton = (ImageButton)findViewById(R.id.pocketComposeButton);

        pressTimes = new ArrayList<Long>();
        charTimer = new Timer(true);
        spaceTimer = new Timer(true);
        warningTimer = new Timer(true);
        sendTimer = new Timer(true);
        messageText = "";

        Intent intent = getIntent();
        final Contact contact = DataManager.getInstance().getAddressBookNamesMap().get(intent.getStringExtra(DotDash.CONTACT_NAME));

        composeButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if(motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    composeButton.setBackground(getResources().getDrawable(R.drawable.touch_down_morse_button));

                    lastDown = System.currentTimeMillis();
                    charTimer.cancel();
                    spaceTimer.cancel();

                    warningTimer = new Timer(true);
                    warningTimer.schedule(new TimerTask() {
                        @Override
                        public void run() {
                            ((Vibrator)getSystemService(VIBRATOR_SERVICE)).vibrate(100);
                        }
                    }, 1500);

                    sendTimer = new Timer(true);
                    sendTimer.schedule(new TimerTask() {
                        @Override
                        public void run() {
                            if (messageText.length()  == 0) {
                                ((Vibrator)getSystemService(VIBRATOR_SERVICE)).vibrate(100);
                                finish();
                            }
                            else {
                                ((Vibrator)getSystemService(VIBRATOR_SERVICE)).vibrate(new long[] {0, 100, 100, 100}, -1);

                                Translator.sendMessage(contact, messageText);
                                finish();
                            }
                        }
                    }, 2500);
                }
                else if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    composeButton.setBackground(getResources().getDrawable(R.drawable.blue_button));

                    lastDuration = System.currentTimeMillis() - lastDown;
                    pressTimes.add(lastDuration);
                    warningTimer.cancel();
                    sendTimer.cancel();

                    charTimer = new Timer(true);
                    charTimer.schedule(new TimerTask() {
                        @Override
                        public void run() {
                            messageText += Translator.convertMorseToText(pressTimes);
                            pressTimes.clear();
                        }
                    }, Translator.LETTER_BREAK_DURATION);

                    spaceTimer = new Timer(true);
                    spaceTimer.schedule(new TimerTask() {
                        @Override
                        public void run() {
                            messageText += ' ';
                            pressTimes.clear();
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
