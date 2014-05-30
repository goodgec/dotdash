package com.groupa.dotdash.dotdash;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.telephony.SmsManager;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;


public class NewMessageFragment extends Fragment {

    private Button morseButton;
    private Button sendButton;
    private TextView newMessageRecipientField;

    private long lastDown;
    private long lastDuration;
    private ArrayList<Long> pressTimes;
    private String messageText;
    private Timer charTimer;
    private Timer spaceTimer;

    private final Handler handler = new Handler();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View fragmentView = inflater.inflate(R.layout.activity_new_message, container, false);

        newMessageRecipientField = (TextView)fragmentView.findViewById(R.id.newMessageRecipientField);

//        Intent intent = getIntent();
//        String recipient = intent.getStringExtra(DotDash.CONTACT_NAME);
//        if (recipient != null) {
//            newMessageRecipientField.setText(recipient);
//        }
        Bundle bundle = getArguments();

        morseButton = (Button)fragmentView.findViewById(R.id.morseTapButton);
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

        sendButton = (Button)fragmentView.findViewById(R.id.sendButton);
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SmsManager manager = SmsManager.getDefault();
                Contact recipient = DataManager.getInstance().getAddressBookNamesMap().get(newMessageRecipientField.getText().toString());

                if (recipient != null) {
                    Message message = new Message(messageText, recipient, true);
                    if (message.getText().length() == 0) {
                        Toast.makeText(view.getContext(), "Invalid empty text", Toast.LENGTH_LONG).show();
                        return;
                    }
                    recipient.getConversation().addMessage(message);
                    manager.sendTextMessage(message.getContact().getNumber(), null, message.getText(), null, null);
                    DataManager.getInstance().addMessageToDb(message);
                    morseButton.setText("");
                    messageText = "";
                } else {
                    Toast.makeText(view.getContext(), "Invalid recipient " + newMessageRecipientField.getText(), Toast.LENGTH_LONG).show();
                }
            }
        });


        return fragmentView;
    }
}
