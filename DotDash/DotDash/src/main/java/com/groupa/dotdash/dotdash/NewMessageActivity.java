package com.groupa.dotdash.dotdash;

import android.media.Image;
import android.os.Bundle;
import android.os.Handler;
import android.telephony.SmsManager;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;


public class NewMessageActivity extends DotDash {

    private Button morseButton;
    private Button sendButton;

    long lastDown;
    long lastUp;
    long lastDuration;

    private ArrayList<Long> pressTimes;
    private String messageText;
    private Timer charTimer;
    private Timer spaceTimer;

    private final Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_message);
        currentScreen = R.id.action_compose;
        morseButton = (Button)findViewById(R.id.morseTapButton);

        pressTimes = new ArrayList<Long>();
        messageText = "";
        charTimer = new Timer(true);
        spaceTimer = new Timer(true);
        morseButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if(motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    lastDown = System.currentTimeMillis();
                    charTimer.cancel();
                    spaceTimer.cancel();
                } else if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    lastDuration = System.currentTimeMillis() - lastDown;
                    pressTimes.add(lastDuration);

                    charTimer = new Timer(true);
                    charTimer.schedule(new TimerTask() {
                        @Override
                        public void run() {
                            messageText += Translator.convertMorseToText(pressTimes);
                            pressTimes.clear();
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    morseButton.setText(messageText);
                                }
                            });
                        }
                    }, Translator.LETTER_BREAK_DURATION);

                    spaceTimer = new Timer(true);
                    spaceTimer.schedule(new TimerTask() {
                        @Override
                        public void run() {
                            messageText += ' ';
                            pressTimes.clear();
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    morseButton.setText(messageText);
                                }
                            });
                        }
                    }, Translator.SPACE_DURATION);

                }
                return true;
            }
        });

        sendButton = (Button)findViewById(R.id.sendButton);
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SmsManager manager = SmsManager.getDefault();
                Contact alby = new Contact("Alby", "7067654085", "a");
                Contact daniel = new Contact("Daniel", "6177770723", "d");
                Message message = new Message(messageText, alby, daniel);

                manager.sendTextMessage(message.getRecipient().getNumber(), null, message.getText(), null, null);

                morseButton.setText("");
                messageText = "";
            }
        });
    }
}
