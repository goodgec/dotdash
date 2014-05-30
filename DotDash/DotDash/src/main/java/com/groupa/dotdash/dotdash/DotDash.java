package com.groupa.dotdash.dotdash;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
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
    public static final String CONTACT_NAME = "contactName";
    //protected static final String CONTACT_NUMBER = "contactNumber";
    //protected static final String CONTACT_ID = "contactID";
    public static final String MESSAGE_SENDER = "messageSender";
    public static final String MESSAGE_TEXT = "messageText";
    public static final String MESSAGE_TIMESTAMP = "messageTimestamp";
    public static final int DEFAULT_WPM = 15;

    public static final String TARGET_TAB = "targetTab";
    public static final int CONVERSATIONS_TAB_NUMBER = 0;
    public static final int NEW_MESSAGE_TAB_NUMBER = 1;
    public static final int CONTACTS_TAB_NUMBER = 2;
    public static final int SETTINGS_TAB_NUMBER = 3;

    public static int wpm;
    public static boolean receiveAsText;
    public static boolean receiveAsVibrate;
    public static boolean receiveAsLight;
    public static boolean receiveAsBeep;

    protected int currentScreen;

    protected ArrayAdapter<Contact> conversationsActivityArrayAdapter;

    protected HashMap<String, Contact> addressBook;

    private SharedPreferences settings;
    protected SharedPreferences.Editor editor;

    private ActionBar actionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dotdash);

        appContext = getApplicationContext();

        actionBar = getActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

//        String targetTab = intent.getStringExtra(TARGET_TAB);


//        actionBar.setDisplayShowTitleEnabled(false);
//        actionBar.setDisplayShowHomeEnabled(false);
        Intent intent = getIntent();

        // set up tabs
        Fragment conversationsFragment = Fragment.instantiate(this, ConversationsFragment.class.getName());
        ActionBar.Tab conversationsTab = actionBar.newTab()
//                .setText("tab1")
                .setTabListener(new TabListener(
                        conversationsFragment, this, "Conversations"))
                .setIcon(R.drawable.conversations);
        actionBar.addTab(conversationsTab);

        Fragment newMessageFragment = Fragment.instantiate(this, NewMessageFragment.class.getName());
        Bundle bundle = new Bundle();
        bundle.putString(CONTACT_NAME, intent.getStringExtra(CONTACT_NAME));
        newMessageFragment.setArguments(bundle);
        ActionBar.Tab newMessageTab = actionBar.newTab()
//                .setText("tab1")
                .setTabListener(new TabListener(
                        newMessageFragment, this, "New Message"))
                .setIcon(R.drawable.compose);
        actionBar.addTab(newMessageTab);

        Fragment contactsFragment = Fragment.instantiate(this, ContactsFragment.class.getName());
        ActionBar.Tab contactsTab = actionBar.newTab()
//                .setText("tab1")
                .setTabListener(new TabListener(
                        contactsFragment, this, "Contacts"))
                .setIcon(R.drawable.contacts);
        actionBar.addTab(contactsTab);

        Fragment settingsFragment = Fragment.instantiate(this, SettingsFragment.class.getName());
        ActionBar.Tab settingTab = actionBar.newTab()
//                .setText("tab1")
                .setTabListener(new TabListener(
                        settingsFragment, this, "Settings"))
                .setIcon(R.drawable.settings);
        actionBar.addTab(settingTab);

        actionBar.setSelectedNavigationItem(intent.getIntExtra(TARGET_TAB, 0));

        settings = getSharedPreferences(DotDash.class.getSimpleName(), Activity.MODE_PRIVATE);
        editor = settings.edit();
        wpm = settings.getInt(WPM_SETTING, DEFAULT_WPM);
        receiveAsText = settings.getBoolean(RECEIVE_AS_TEXT_SETTING, true);
        receiveAsVibrate = settings.getBoolean(RECEIVE_AS_VIBRATE_SETTING, true);
        receiveAsLight = settings.getBoolean(RECEIVE_AS_LIGHT_SETTING, false);
        receiveAsBeep = settings.getBoolean(RECEIVE_AS_BEEP_SETTING, false);

        IntentFilter filter = new IntentFilter(Receiver.DOT_DASH_RECEIVED_MESSAGE);
        this.registerReceiver(newMessageAlertReceiver, filter);
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.application_actions, menu);
//        return super.onCreateOptionsMenu(menu);
//    }

//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//        Log.w("ID:", Integer.toString(id));
//
//        //Toast.makeText(this, "Selected" + String.valueOf(id), Toast.LENGTH_LONG).show();
//
//        if (currentScreen == id){
//            Log.w("if happened:", Integer.toString(currentScreen));
//
//            return true;
//            //return super.onOptionsItemSelected(item);
//        }
//        switch(id){
//            case R.id.action_compose:
//                //go to compose activity
//                currentScreen = R.id.action_compose;
//                Log.w("Curr:", Integer.toString(currentScreen));
//                startActivity(new Intent(getApplicationContext(), NewMessageFragment.class));
//                finish();
//                overridePendingTransition(0, 0);
//
//                break;
//            case R.id.action_conversations:
//                currentScreen = R.id.action_conversations;
//                Log.w("Curr:", Integer.toString(currentScreen));
//                startActivity(new Intent(getApplicationContext(), ConversationsFragment.class));
//                finish();
//                overridePendingTransition(0, 0);
//
//                break;
//            case R.id.action_contacts:
//                currentScreen = R.id.action_contacts;
//                Log.w("Curr:", Integer.toString(currentScreen));
//                startActivity(new Intent(getApplicationContext(), ContactsFragment.class));
//                finish();
//                overridePendingTransition(0, 0);
//
//                break;
//            case R.id.action_settings:
//                currentScreen = R.id.action_settings;
//                Log.w("Curr:", Integer.toString(currentScreen));
//                startActivity(new Intent(getApplicationContext(), SettingsFragment.class));
//                finish();
//                overridePendingTransition(0, 0);
//
//                break;
//
//        }
//
//        return super.onOptionsItemSelected(item);
//    }

    @Override
    protected void onStop() {
        super.onStop();

        editor.putInt(WPM_SETTING, wpm);
        editor.putBoolean(RECEIVE_AS_TEXT_SETTING, receiveAsText);
        editor.putBoolean(RECEIVE_AS_VIBRATE_SETTING, receiveAsVibrate);
        editor.putBoolean(RECEIVE_AS_LIGHT_SETTING, receiveAsLight);
        editor.putBoolean(RECEIVE_AS_BEEP_SETTING, receiveAsBeep);

        editor.commit();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(newMessageAlertReceiver);
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

    private final BroadcastReceiver newMessageAlertReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();

            if (Receiver.DOT_DASH_RECEIVED_MESSAGE.equals(action))
            {
//                displayAlert();
                Contact sender = DataManager.getInstance().getAddressBookNumbersMap().get(intent.getStringExtra(MESSAGE_SENDER));
                if (sender != null) {
                    Message newMessage = new Message(intent.getStringExtra(MESSAGE_TEXT), sender, false, intent.getLongExtra(MESSAGE_TIMESTAMP,0));
                    if (sender.getConversation().isDuplicate(newMessage)) {
                        return;
                    }
                    sender.getConversation().addMessage(newMessage);
                    DataManager.getInstance().addMessageToDb(newMessage);
//                    if (speechBubbleArrayAdapter != null) {
//                        speechBubbleArrayAdapter.add(newMessage);
//                        speechBubbleArrayAdapter.notifyDataSetChanged();
//                    }

                    if (sender.getConversation().size() == 0 && conversationsActivityArrayAdapter != null) {
                        conversationsActivityArrayAdapter.add(sender);
                        conversationsActivityArrayAdapter.notifyDataSetChanged();
                    }

                }
            }

        }
    };
}
