package com.groupa.dotdash.dotdash;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.SearchManager;
import android.app.SearchableInfo;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.NavUtils;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;


public class NewMessageFragment extends Fragment {

    private Button morseButton;
    private Button sendButton;
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
        newMessageRecipientField = (AutoCompleteTextView)fragmentView.findViewById(R.id.newMessageRecipientField);


        messageText = DataManager.getInstance().getCurrentMessageText();
        if (!messageText.equals("")) {
            morseButton.setText(messageText);
        }

        pressTimes = new ArrayList<Long>();
        charTimer = new Timer(true);
        spaceTimer = new Timer(true);

        ArrayAdapter<Contact> suggestionsAdapter = new ArrayAdapter<Contact>(getActivity(), android.R.layout.simple_dropdown_item_1line, DataManager.getInstance().getAddressBookList());
        //newMessageRecipientField.setAdapter(suggestionsAdapter);
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
                                    setText(messageText);
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
                                    setText(messageText);
                                }
                            });
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

//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        switch (item.getItemId()) {
//            case android.R.id.home:
//                if (startedFromContact) {
//                    Intent intent = new Intent(getActivity().getApplicationContext(), SingleContactActivity.class);
//                    intent.putExtra(DotDash.CONTACT_NAME, contactName);
//                    startActivity(intent);
//                    ((DotDash)getActivity()).setTabNumber(DotDash.CONTACTS_TAB_NUMBER);
//                    getActivity().getActionBar().setDisplayHomeAsUpEnabled(false);
//
//                }
//                return true;
//            default:
//                return super.onOptionsItemSelected(item);
//        }
//    }

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
        Log.e("alby", "new message detatch");
    }
}
