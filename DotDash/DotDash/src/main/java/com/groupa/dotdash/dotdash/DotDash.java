package com.groupa.dotdash.dotdash;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Vibrator;
import android.telephony.SmsManager;

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
    public static final String PLAY_MESSAGE = "playMessage";

    public static int wpm;
    public static boolean receiveAsText;
    public static boolean receiveAsVibrate;
    public static boolean receiveAsLight;
    public static boolean receiveAsBeep;
    public static int currentTabNumber;
    public static String currentMessage;

    public static final String CONTACT_NAME = "contactName";
    public static final String CONTACT_NUMBER = "contactNumber";
    public static final String CONTACT_MORSE_ID = "contactMorseID";
    public static final int DEFAULT_WPM = 15;

    public static final int REQUEST_CODE_CREATE_CONTACT = 0;
    public static final int REQUEST_CODE_VIEW_CONTACT = 1;
    public static final int REQUEST_CODE_VIEW_CONVERSATION = 2;
    public static final int REQUEST_CODE_EDIT_CONTACT = 3;

    public static final int RESULT_CODE_DELETED_CONTACT = 0;
    public static final int RESULT_CODE_SENDING_MESSAGE = 1;
    public static final int RESULT_CODE_REPLY_MESSAGE = 2;

    public static final String TARGET_TAB = "targetTab";
    public static final int CONVERSATIONS_TAB_NUMBER = 0;
    public static final int NEW_MESSAGE_TAB_NUMBER = 1;
    public static final int CONTACTS_TAB_NUMBER = 2;
    public static final int SETTINGS_TAB_NUMBER = 3;
    public static final int DEFAULT_TAB = 1;

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

        setDefaultKeyMode(DEFAULT_KEYS_SEARCH_LOCAL);

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
        currentTabNumber = intent.getIntExtra(TARGET_TAB, settings.getInt(CURRENT_TAB_NUMBER, DEFAULT_TAB));
        currentMessage = intent.getStringExtra(CURRENT_MESSAGE);

        conversationsFragment = (ConversationsFragment)Fragment.instantiate(this, ConversationsFragment.class.getName());

        Drawable drConversations = getResources().getDrawable(R.drawable.conversations);
        Bitmap bitmapConversations = ((BitmapDrawable) drConversations).getBitmap();
        Drawable conversationsSmall = new BitmapDrawable(getResources(), Bitmap.createScaledBitmap(bitmapConversations, 80, 80, true));

        ActionBar.Tab conversationsTab = actionBar.newTab()
                .setTabListener(new TabListener(
                        conversationsFragment, this, "Conversations", CONVERSATIONS_TAB_NUMBER))
                .setIcon(conversationsSmall);
        actionBar.addTab(conversationsTab);

        newMessageFragment = (NewMessageFragment)Fragment.instantiate(this, NewMessageFragment.class.getName());

        Drawable drCompose = getResources().getDrawable(R.drawable.compose);
        Bitmap bitmapCompose = ((BitmapDrawable) drCompose).getBitmap();
        Drawable composeSmall = new BitmapDrawable(getResources(), Bitmap.createScaledBitmap(bitmapCompose, 80, 80, true));

        Bundle bundle = new Bundle();
        bundle.putString(CONTACT_NAME, intent.getStringExtra(CONTACT_NAME));
        bundle.putString(CURRENT_MESSAGE, currentMessage);
        newMessageFragment.setArguments(bundle);

        ActionBar.Tab newMessageTab = actionBar.newTab()
                .setTabListener(new TabListener(
                        newMessageFragment, this, "New Message", NEW_MESSAGE_TAB_NUMBER))
                .setIcon(composeSmall);

        actionBar.addTab(newMessageTab);

        contactsFragment = (ContactsFragment)Fragment.instantiate(this, ContactsFragment.class.getName());

        Drawable drContacts = getResources().getDrawable(R.drawable.contacts);
        Bitmap bitmapContacts = ((BitmapDrawable) drContacts).getBitmap();
        Drawable contactsSmall = new BitmapDrawable(getResources(), Bitmap.createScaledBitmap(bitmapContacts, 80, 80, true));

        ActionBar.Tab contactsTab = actionBar.newTab()
                .setTabListener(new TabListener(
                        contactsFragment, this, "Contacts", CONTACTS_TAB_NUMBER))
                .setIcon(contactsSmall);
        actionBar.addTab(contactsTab);

        settingsFragment = (SettingsFragment)Fragment.instantiate(this, SettingsFragment.class.getName());

        Drawable drSettings = getResources().getDrawable(R.drawable.settings);
        Bitmap bitmapSettings = ((BitmapDrawable) drSettings).getBitmap();
        Drawable settingsSmall = new BitmapDrawable(getResources(), Bitmap.createScaledBitmap(bitmapSettings, 80, 80, true));

        ActionBar.Tab settingTab = actionBar.newTab()
                .setTabListener(new TabListener(
                        settingsFragment, this, "Settings", SETTINGS_TAB_NUMBER))
                .setIcon(settingsSmall);
        actionBar.addTab(settingTab);

        actionBar.setSelectedNavigationItem(currentTabNumber);

        if (intent.getStringExtra(CAME_FROM_NOTIFICATION) != null) {
            Intent nameIntent = new Intent(this, SingleConversationActivity.class);
            nameIntent.putExtra(CONTACT_NAME, intent.getStringExtra(CONTACT_NAME));
            nameIntent.putExtra(PLAY_MESSAGE, true);
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
                    currentTabNumber = data.getIntExtra(TARGET_TAB, currentTabNumber);
                    actionBar.setSelectedNavigationItem(currentTabNumber);
                    newMessageFragment.setContactName(data.getStringExtra(CONTACT_NAME));
                    newMessageFragment.setStartedFromContact(true);
                    break;
                default:
                    currentTabNumber = data.getIntExtra(TARGET_TAB, currentTabNumber);
                    actionBar.setSelectedNavigationItem(currentTabNumber);
                    break;
            }
        } else if (requestCode == REQUEST_CODE_VIEW_CONVERSATION) {
            switch (resultCode) {
                case RESULT_CODE_REPLY_MESSAGE:
                    currentTabNumber = data.getIntExtra(TARGET_TAB, currentTabNumber);
                    actionBar.setSelectedNavigationItem(currentTabNumber);
                    newMessageFragment.setContactName(data.getStringExtra(CONTACT_NAME));
                    newMessageFragment.setStartedFromConversation(true);
                    break;
                default:
                    currentTabNumber = data.getIntExtra(TARGET_TAB, currentTabNumber);
                    actionBar.setSelectedNavigationItem(currentTabNumber);
                    break;
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();

        editor.putInt(WPM_SETTING, wpm);
        editor.putBoolean(RECEIVE_AS_TEXT_SETTING, receiveAsText);
        editor.putBoolean(RECEIVE_AS_VIBRATE_SETTING, receiveAsVibrate);
        editor.putBoolean(RECEIVE_AS_LIGHT_SETTING, receiveAsLight);
        editor.putBoolean(RECEIVE_AS_BEEP_SETTING, receiveAsBeep);
        editor.putInt(CURRENT_TAB_NUMBER, currentTabNumber);
        editor.putString(CURRENT_MESSAGE, newMessageFragment.getMessageText());

        editor.commit();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    public void setTabNumber(int tabNumber) {
        currentTabNumber = tabNumber;
    }

    @Override
    public void onBackPressed() {
        if (newMessageFragment.isStartedFromContact() && currentTabNumber == NEW_MESSAGE_TAB_NUMBER) {
            Intent intent = new Intent(this, SingleContactActivity.class);
            intent.putExtra(DotDash.CONTACT_NAME, newMessageFragment.getContactName());
            setTabNumber(DotDash.CONTACTS_TAB_NUMBER);
            newMessageFragment.setStartedFromContact(false);
            startActivityForResult(intent, REQUEST_CODE_VIEW_CONTACT);
        }
        else if (newMessageFragment.isStartedFromConversation() && currentTabNumber == NEW_MESSAGE_TAB_NUMBER) {
            Intent intent = new Intent(this, SingleConversationActivity.class);
            intent.putExtra(DotDash.CONTACT_NAME, newMessageFragment.getContactName());
            setTabNumber(DotDash.CONVERSATIONS_TAB_NUMBER);
            newMessageFragment.setStartedFromConversation(false);
            startActivityForResult(intent, REQUEST_CODE_VIEW_CONVERSATION);
        }
        else {
            finish();
        }
    }

    public static void outputMessage(long[] times) {
        Vibrator vibrator = (Vibrator)appContext.getSystemService(Context.VIBRATOR_SERVICE);

        if (DotDash.receiveAsVibrate) {
            vibrator.vibrate(times, -1);
        }
    }

    public static void sendMessage(Contact contact, String text) {
        SmsManager manager = SmsManager.getDefault();

        Message message = new Message(text, contact, true);
        contact.getConversation().addMessage(message);
        manager.sendTextMessage(message.getContact().getNumber(), null, message.getText(), null, null);

        DataManager.getInstance().addMessageToDb(message);
    }

    public static void sendMessage(String number, String text) {
        Contact contact = DataManager.getInstance().getAddressBookNumbersMap().get(number);
        if (contact != null) {
            sendMessage(contact, text);
        }
        else {
            SmsManager manager = SmsManager.getDefault();
            manager.sendTextMessage(number, null, text, null, null);
        }
    }
}
