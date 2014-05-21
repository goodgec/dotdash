package com.groupa.dotdash.dotdash;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

/**
 * Created by barterd on conversations/8/14.
 */
public class DotDash extends Activity {

    private final String WPM_SETTING = "wpm";
    private final String RECEIVE_AS_TEXT_SETTING = "receiveAsText";
    private final String RECEIVE_AS_VIBRATE_SETTING = "receiveAsVibrate";
    private final String RECEIVE_AS_LIGHT_SETTING = "receiveAsLight";
    private final String RECEIVE_AS_BEEP_SETTING = "receiveAsBeep";

    protected int wpm;
    protected boolean receiveAsText;
    protected boolean receiveAsVibrate;
    protected boolean receiveAsLight;
    protected boolean receiveAsBeep;

    protected int currentScreen;

    private SharedPreferences settings;
    private SharedPreferences.Editor editor;

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
    }

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
}
