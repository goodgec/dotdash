package com.groupa.dotdash.dotdash;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

/**
 * Created by barterd on conversations/8/14.
 */
public class DotDash extends Activity {
    protected int currentScreen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_contacts);
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
