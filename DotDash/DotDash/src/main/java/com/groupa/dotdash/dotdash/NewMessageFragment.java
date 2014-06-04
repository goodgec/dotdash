package com.groupa.dotdash.dotdash;

import android.app.Fragment;
import android.os.Bundle;
import android.os.Handler;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;


public class NewMessageFragment extends Fragment {

    private Button morseButton;
    private Button sendButton;
    private Button clearButton;
    private AutoCompleteTextView newMessageRecipientField;

    private long lastDown;
    private long lastDuration;
    private ArrayList<Long> pressTimes;
    private String messageText;
    private Timer charTimer;
    private Timer spaceTimer;
    private boolean startedFromContact;
    private boolean startedFromConversation;
    private String contactName;

    private final Handler handler = new Handler();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View fragmentView = inflater.inflate(R.layout.activity_new_message, container, false);

        morseButton = (Button)fragmentView.findViewById(R.id.morseTapButton);
        sendButton = (Button)fragmentView.findViewById(R.id.sendButton);
        clearButton = (Button)fragmentView.findViewById(R.id.clearButton);
        newMessageRecipientField = (AutoCompleteTextView)fragmentView.findViewById(R.id.newMessageRecipientField);
        newMessageRecipientField.setInputType(InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);

        messageText = DataManager.getInstance().getCurrentMessageText();
        if (!messageText.equals("")) {
            morseButton.setText(messageText);
        }

        pressTimes = new ArrayList<Long>();
        charTimer = new Timer(true);
        spaceTimer = new Timer(true);

        ArrayAdapter<Contact> suggestionsAdapter = new ArrayAdapter<Contact>(getActivity(), android.R.layout.simple_dropdown_item_1line, DataManager.getInstance().getAddressBookList());
        newMessageRecipientField.setAdapter(suggestionsAdapter);
        newMessageRecipientField.setThreshold(1);

        if (contactName != null) {
            new Handler().post(new Runnable() {
                public void run() {
                    newMessageRecipientField.setText(contactName);
                    newMessageRecipientField.dismissDropDown();
                }
            });
        }

        morseButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if(motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    morseButton.setBackground(getResources().getDrawable(R.drawable.touch_down_morse_button));

                    lastDown = System.currentTimeMillis();
                    charTimer.cancel();
                    spaceTimer.cancel();
                } else if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    morseButton.setBackground(getResources().getDrawable(R.drawable.blue_button));
                    lastDuration = System.currentTimeMillis() - lastDown;
                    pressTimes.add(lastDuration);

                    charTimer = new Timer(true);
                    charTimer.schedule(new TimerTask() {
                        @Override
                        public void run() {
                            messageText += Translator.convertMorseToText(pressTimes);
                            pressTimes.clear();
                            if (DotDash.receiveAsText) {
                                handler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        setText(messageText);
                                    }
                                });
                            }
                        }
                    }, Translator.LETTER_BREAK_DURATION);

                    spaceTimer = new Timer(true);
                    spaceTimer.schedule(new TimerTask() {
                        @Override
                        public void run() {
                            messageText += ' ';
                            pressTimes.clear();
                            if (DotDash.receiveAsText) {
                                handler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        setText(messageText);
                                    }
                                });
                            }
                        }
                    }, Translator.SPACE_DURATION);

                }
                return true;
            }
        });

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                spaceTimer.cancel();
                charTimer.cancel();
                pressTimes.clear();

                Contact recipient = DataManager.getInstance().getAddressBookNamesMap().get(newMessageRecipientField.getText().toString());

                if (recipient != null) {
                    if (messageText.length() == 0) {
                        Toast.makeText(view.getContext(), "Invalid empty text", Toast.LENGTH_LONG).show();
                    } else {
                        Translator.sendMessage(recipient, messageText);
                        Toast.makeText(view.getContext(), "Message sent", Toast.LENGTH_LONG).show();
                        setText("");
                        messageText = "";
                    }
                } else {
                    Toast.makeText(view.getContext(), "Invalid recipient " + newMessageRecipientField.getText(), Toast.LENGTH_LONG).show();
                }
            }
        });

        clearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                spaceTimer.cancel();
                charTimer.cancel();
                pressTimes.clear();

                setText("");
                messageText = "";
            }
        });

        return fragmentView;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public String getContactName() {
        return contactName;
    }

    public String getMessageText() {
        return messageText;
    }

    public boolean isStartedFromContact() {
        return startedFromContact;
    }

    public void setStartedFromContact(boolean startedFromContact) {
        this.startedFromContact = startedFromContact;
    }

    public boolean isStartedFromConversation() {
        return startedFromConversation;
    }

    public void setStartedFromConversation(boolean startedFromConversation) {
        this.startedFromConversation = startedFromConversation;
    }

    private void setText(String s) {
        morseButton.setText(s);
        DataManager.getInstance().setCurrentMessageText(s);
    }

    @Override
    public void onDetach(){
        super.onDetach();
    }
}
