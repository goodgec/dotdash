package com.groupa.dotdash.dotdash;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;

import java.util.HashMap;

/**
 * Created by barterd on conversations/8/14.
 */
public class DotDash extends Activity {

    public static Context appContext;

    protected static final String WPM_SETTING = "wpm";
    protected static final String RECEIVE_AS_TEXT_SETTING = "receiveAsText";
    protected static final String RECEIVE_AS_VIBRATE_SETTING = "receiveAsVibrate";
    protected static final String RECEIVE_AS_LIGHT_SETTING = "receiveAsLight";
    protected static final String RECEIVE_AS_BEEP_SETTING = "receiveAsBeep";
    protected static final String CONTACT_NAME = "contactName";
    public static final String MESSAGE_SENDER = "messageSender";
    public static final String MESSAGE_TEXT = "messageText";

    protected int wpm;
    protected boolean receiveAsText;
    protected boolean receiveAsVibrate;
    protected boolean receiveAsLight;
    protected boolean receiveAsBeep;

    protected int currentScreen;
    protected ArrayAdapter<Message> arrayAdapter;

    protected HashMap<String, Contact> addressBook;

    private SharedPreferences settings;
    protected SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        settings = getSharedPreferences(DotDash.class.getSimpleName(), Activity.MODE_PRIVATE);
        editor = settings.edit();
        wpm = settings.getInt(WPM_SETTING, 15);
        receiveAsText = settings.getBoolean(RECEIVE_AS_TEXT_SETTING, true);
        receiveAsVibrate = settings.getBoolean(RECEIVE_AS_VIBRATE_SETTING, true);
        receiveAsLight = settings.getBoolean(RECEIVE_AS_LIGHT_SETTING, false);
        receiveAsBeep = settings.getBoolean(RECEIVE_AS_BEEP_SETTING, false);

        IntentFilter filter = new IntentFilter(Receiver.DOT_DASH_RECEIVED_MESSAGE);
        this.registerReceiver(newMessageAlertReceiver, filter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.application_actions, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        Log.w("ID:", Integer.toString(id));

        if (currentScreen == id){
            Log.w("if happened:", Integer.toString(currentScreen));

            return true;
            //return super.onOptionsItemSelected(item);
        }
        switch(id){
            case R.id.action_compose:
                //go to compose activity
                currentScreen = R.id.action_compose;
                Log.w("Curr:", Integer.toString(currentScreen));
                finish();
                startActivity(new Intent(getApplicationContext(), NewMessageActivity.class));
                break;
            case R.id.action_conversations:
                currentScreen = R.id.action_conversations;
                Log.w("Curr:", Integer.toString(currentScreen));
                finish();
                startActivity(new Intent(getApplicationContext(), ConversationsActivity.class));
                break;
            case R.id.action_contacts:
                currentScreen = R.id.action_contacts;
                Log.w("Curr:", Integer.toString(currentScreen));
                finish();
                startActivity(new Intent(getApplicationContext(), ContactsActivity.class));
                break;
            case R.id.action_settings:
                currentScreen = R.id.action_settings;
                Log.w("Curr:", Integer.toString(currentScreen));
                finish();
                startActivity(new Intent(getApplicationContext(), SettingsActivity.class));
                break;

        }

        return super.onOptionsItemSelected(item);
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
                Message newMessage = new Message(intent.getStringExtra(MESSAGE_TEXT), sender, false);
                sender.getConversation().addMessage(newMessage);
                if (arrayAdapter != null) {
                    arrayAdapter.add(newMessage);
                }
            }

        }
    };
}
