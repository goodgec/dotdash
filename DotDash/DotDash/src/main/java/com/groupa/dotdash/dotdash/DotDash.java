package com.groupa.dotdash.dotdash;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ArrayAdapter;

import java.util.HashMap;

/**
 * Created by barterd on conversations/8/14.
 */
public class DotDash extends Activity {

    public static Context appContext;

    public static final String WPM_SETTING = "wpm";
    public static final String RECEIVE_AS_TEXT_SETTING = "receiveAsText";
    public static final String RECEIVE_AS_VIBRATE_SETTING = "receiveAsVibrate";
    public static final String RECEIVE_AS_LIGHT_SETTING = "receiveAsLight";
    public static final String RECEIVE_AS_BEEP_SETTING = "receiveAsBeep";
    public static final String CURRENT_TAB_NUMBER = "currentTabNumber";
    public static final String CURRENT_MESSAGE = "currentMessage";
    public static final String CAME_FROM_NOTIFICATION = "cameFromNotification";

    public static int wpm;
    public static boolean receiveAsText;
    public static boolean receiveAsVibrate;
    public static boolean receiveAsLight;
    public static boolean receiveAsBeep;
    public static int currentTabNumber;
    public static String currentMessage;

    public static final String CONTACT_NAME = "contactName";
    //protected static final String CONTACT_NUMBER = "contactNumber";
    //protected static final String CONTACT_ID = "contactID";
    public static final String MESSAGE_SENDER = "messageSender";
    public static final String MESSAGE_TEXT = "messageText";
    public static final String MESSAGE_TIMESTAMP = "messageTimestamp";
    public static final int DEFAULT_WPM = 15;

    public static final int REQUEST_CODE_CREATE_CONTACT = 0;
    public static final int REQUEST_CODE_VIEW_CONTACT = 1;
    public static final int REQUEST_CODE_VIEW_CONVERSATION = 2;

    public static final int RESULT_CODE_DELETED_CONTACT = 0;
    public static final int RESULT_CODE_SENDING_MESSAGE = 1;
    public static final int RESULT_CODE_REPLY_MESSAGE = 2;

    public static final String TARGET_TAB = "targetTab";
    public static final int CONVERSATIONS_TAB_NUMBER = 0;
    public static final int NEW_MESSAGE_TAB_NUMBER = 1;
    public static final int CONTACTS_TAB_NUMBER = 2;
    public static final int SETTINGS_TAB_NUMBER = 3;

    protected ArrayAdapter<Contact> conversationsActivityArrayAdapter;

    protected HashMap<String, Contact> addressBook;

    private SharedPreferences settings;
    protected SharedPreferences.Editor editor;

    private ActionBar actionBar;
    private ConversationsFragment conversationsFragment;
    private NewMessageFragment newMessageFragment;
    private ContactsFragment contactsFragment;
    private SettingsFragment settingsFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dotdash);

        //Log.e("alby", "starting");

        appContext = getApplicationContext();

        actionBar = getActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        Intent intent = getIntent();

        settings = getSharedPreferences(DotDash.class.getSimpleName(), Activity.MODE_PRIVATE);
        editor = settings.edit();
        wpm = settings.getInt(WPM_SETTING, DEFAULT_WPM);
        receiveAsText = settings.getBoolean(RECEIVE_AS_TEXT_SETTING, true);
        receiveAsVibrate = settings.getBoolean(RECEIVE_AS_VIBRATE_SETTING, true);
        receiveAsLight = settings.getBoolean(RECEIVE_AS_LIGHT_SETTING, false);
        receiveAsBeep = settings.getBoolean(RECEIVE_AS_BEEP_SETTING, false);
        currentTabNumber = intent.getIntExtra(TARGET_TAB, settings.getInt(CURRENT_TAB_NUMBER, 0));
        currentMessage = intent.getStringExtra(CURRENT_MESSAGE);

        // set up tabs
        conversationsFragment = (ConversationsFragment)Fragment.instantiate(this, ConversationsFragment.class.getName());
        ActionBar.Tab conversationsTab = actionBar.newTab()
//                .setText("tab1")
                .setTabListener(new TabListener(
                        conversationsFragment, this, "Conversations", CONVERSATIONS_TAB_NUMBER))
                .setIcon(R.drawable.conversations);
        actionBar.addTab(conversationsTab);

        newMessageFragment = (NewMessageFragment)Fragment.instantiate(this, NewMessageFragment.class.getName());
        Bundle bundle = new Bundle();
        bundle.putString(CONTACT_NAME, intent.getStringExtra(CONTACT_NAME));
        bundle.putString(CURRENT_MESSAGE, currentMessage);
        newMessageFragment.setArguments(bundle);
        ActionBar.Tab newMessageTab = actionBar.newTab()
//                .setText("tab1")
                .setTabListener(new TabListener(
                        newMessageFragment, this, "New Message", NEW_MESSAGE_TAB_NUMBER))
                .setIcon(R.drawable.compose);
        actionBar.addTab(newMessageTab);

        contactsFragment = (ContactsFragment)Fragment.instantiate(this, ContactsFragment.class.getName());
        ActionBar.Tab contactsTab = actionBar.newTab()
//                .setText("tab1")
                .setTabListener(new TabListener(
                        contactsFragment, this, "Contacts", CONTACTS_TAB_NUMBER))
                .setIcon(R.drawable.contacts);
        actionBar.addTab(contactsTab);

        settingsFragment = (SettingsFragment)Fragment.instantiate(this, SettingsFragment.class.getName());
        ActionBar.Tab settingTab = actionBar.newTab()
//                .setText("tab1")
                .setTabListener(new TabListener(
                        settingsFragment, this, "Settings", SETTINGS_TAB_NUMBER))
                .setIcon(R.drawable.settings);
        actionBar.addTab(settingTab);

//        currentTab = intent.getIntExtra(TARGET_TAB, currentTab);
//        actionBar.setSelectedNavigationItem(currentTab);

        actionBar.setSelectedNavigationItem(currentTabNumber);


        IntentFilter filter = new IntentFilter(Receiver.DOT_DASH_RECEIVED_MESSAGE);
        this.registerReceiver(newMessageAlertReceiver, filter);


        if (intent.getStringExtra(DotDash.CAME_FROM_NOTIFICATION) != null) {
            Intent nameIntent = new Intent(this, SingleConversationActivity.class);
            nameIntent.putExtra(DotDash.CONTACT_NAME, intent.getStringExtra(CONTACT_NAME));
            startActivityForResult(nameIntent, DotDash.REQUEST_CODE_VIEW_CONVERSATION);
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_CREATE_CONTACT) {
            if (resultCode == RESULT_OK) {
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.detach(contactsFragment);
                ft.attach(contactsFragment);
                ft.commit();
            }
        } else if (requestCode == REQUEST_CODE_VIEW_CONTACT) {
            switch (resultCode) {
                case RESULT_CODE_DELETED_CONTACT:
                    FragmentTransaction ft = getFragmentManager().beginTransaction();
                    ft.detach(contactsFragment);
                    ft.attach(contactsFragment);
                    ft.commit();
                    break;
                case RESULT_CODE_SENDING_MESSAGE:
                    actionBar.setSelectedNavigationItem(data.getIntExtra(TARGET_TAB, currentTabNumber));
                    newMessageFragment.setContactName(data.getStringExtra(CONTACT_NAME));
                    newMessageFragment.setStartedFromContact(true);
                    break;
            }
        } else if (requestCode == REQUEST_CODE_VIEW_CONVERSATION) {
            switch (resultCode) {
                case RESULT_CODE_REPLY_MESSAGE:
                    actionBar.setSelectedNavigationItem(data.getIntExtra(TARGET_TAB, currentTabNumber));
                    newMessageFragment.setContactName(data.getStringExtra(CONTACT_NAME));
                    newMessageFragment.setStartedFromConversation(true);
                    break;
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        //Log.e("alby", "pausing");
    }

    @Override
    protected void onStop() {
        super.onStop();
        //Log.e("alby", "stopping");

        editor.putInt(WPM_SETTING, wpm);
        editor.putBoolean(RECEIVE_AS_TEXT_SETTING, receiveAsText);
        editor.putBoolean(RECEIVE_AS_VIBRATE_SETTING, receiveAsVibrate);
        editor.putBoolean(RECEIVE_AS_LIGHT_SETTING, receiveAsLight);
        editor.putBoolean(RECEIVE_AS_BEEP_SETTING, receiveAsBeep);
        editor.putInt(CURRENT_TAB_NUMBER, currentTabNumber);
//        Log.e("alby", newMessageFragment.getMessageText());
        editor.putString(CURRENT_MESSAGE, newMessageFragment.getMessageText());

        editor.commit();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(newMessageAlertReceiver);
    }

    public void setTabNumber(int tabNumber) {
        currentTabNumber = tabNumber;
    }

    protected void displayAlert()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("You've Got Mail!").setCancelable(
                false).setPositiveButton("Listen",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                }).setNegativeButton("Ignore",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }

    @Override
    public void onBackPressed() {
        if (newMessageFragment.isStartedFromContact() && currentTabNumber == NEW_MESSAGE_TAB_NUMBER) {
            Intent intent = new Intent(this, SingleContactActivity.class);
            intent.putExtra(DotDash.CONTACT_NAME, newMessageFragment.getContactName());
            startActivity(intent);
            setTabNumber(DotDash.CONTACTS_TAB_NUMBER);
            getActionBar().setDisplayHomeAsUpEnabled(false);
            newMessageFragment.setStartedFromContact(false);
        }
        else if (newMessageFragment.isStartedFromConversation() && currentTabNumber == NEW_MESSAGE_TAB_NUMBER) {
            Intent intent = new Intent(this, SingleConversationActivity.class);
            intent.putExtra(DotDash.CONTACT_NAME, newMessageFragment.getContactName());
            startActivity(intent);
            setTabNumber(DotDash.CONVERSATIONS_TAB_NUMBER);
            getActionBar().setDisplayHomeAsUpEnabled(false);
            newMessageFragment.setStartedFromConversation(false);
        }
        else {
            finish();
        }
    }

    private final BroadcastReceiver newMessageAlertReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();

            if (Receiver.DOT_DASH_RECEIVED_MESSAGE.equals(action))
            {
                Contact sender = DataManager.getInstance().getAddressBookNumbersMap().get(intent.getStringExtra(MESSAGE_SENDER));
                if (sender != null) {
                    Message newMessage = new Message(intent.getStringExtra(MESSAGE_TEXT), sender, false, intent.getLongExtra(MESSAGE_TIMESTAMP,0));

                    if (sender.getConversation().isDuplicate(newMessage)) {
                        return;
                    }

                    sender.getConversation().addMessage(newMessage);
                    DataManager.getInstance().addMessageToDb(newMessage);

                    // add sender to conversations list if they aren't already there
                    if (sender.getConversation().size() == 0) {
                        conversationsFragment.addSender(sender);
                        FragmentTransaction ft = getFragmentManager().beginTransaction();
                        ft.detach(conversationsFragment);
                        ft.attach(conversationsFragment);
                        ft.commit();
                    }

                    // refresh message screen


//                    if (speechBubbleArrayAdapter != null) {
//                        speechBubbleArrayAdapter.add(newMessage);
//                        speechBubbleArrayAdapter.notifyDataSetChanged();
//                    }


                }
            }

        }
    };
}
