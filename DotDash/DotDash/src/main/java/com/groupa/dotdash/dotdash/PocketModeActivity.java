package com.groupa.dotdash.dotdash;

import android.app.Activity;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.os.Handler;
import android.os.PowerManager;
import android.util.Log;
import android.view.DragEvent;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class PocketModeActivity extends Activity {

    private ImageButton playButton;
    private ImageButton composeButton;

    PowerManager.WakeLock wakeLock;

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

        PowerManager powerManager = (PowerManager)getSystemService(POWER_SERVICE);
        wakeLock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "My Lock");
        wakeLock.acquire();

        playButton = (ImageButton)findViewById(R.id.pocketPlayButton);
        composeButton = (ImageButton)findViewById(R.id.pocketNewMessageButton);
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

        composeButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if(motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    lastDown = System.currentTimeMillis();
                    charTimer.cancel();
                    spaceTimer.cancel();
                }
                else if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
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
//                            Toast.makeText(DotDash.appContext, morseID, Toast.LENGTH_LONG).show();
                            Log.e("alby", morseID);
                            if (contact != null) {
                                Log.e("alby", contact.getName());
//                                Toast.makeText(DotDash.appContext, contact.getName(), Toast.LENGTH_LONG).show();
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
    public void onPause() {
        super.onPause();
        if (wakeLock.isHeld()) {
            wakeLock.release();
        }
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.pocket_mode, menu);
//        return true;
//    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        onBackPressed();
        return true;
    }

}
