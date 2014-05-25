package com.groupa.dotdash.dotdash;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.telephony.SmsManager;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;


public class NewMessageActivity extends DotDash {

    private Button morseButton;
    private Button sendButton;
    private TextView newMessageRecipientField;

    private long lastDown;
    private long lastDuration;
    private ArrayList<Long> pressTimes;
    private String messageText;
    private Timer charTimer;
    private Timer spaceTimer;

    private DataManager dm;
    private final Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_message);
        currentScreen = R.id.action_compose;

        newMessageRecipientField = (TextView)findViewById(R.id.newMessageRecipientField);

        Intent intent = getIntent();
        String recipient = intent.getStringExtra(CONTACT_NAME);
        if (recipient != null) {
            newMessageRecipientField.setText(recipient);
        }


        dm = DataManager.getInstance();

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
                Contact recipient = dm.getAddressBookNamesMap().get(newMessageRecipientField.getText().toString());

                if (recipient != null) {
                    Message message = new Message(messageText, recipient, true);
                    recipient.getConversation().addMessage(message);
                    manager.sendTextMessage(message.getContact().getNumber(), null, message.getText(), null, null);
                    morseButton.setText("");
                    messageText = "";
                } else {
                    Toast.makeText(view.getContext(), "Invalid recipient " + newMessageRecipientField.getText(), Toast.LENGTH_LONG).show();
                }
            }
        });


    }
}
