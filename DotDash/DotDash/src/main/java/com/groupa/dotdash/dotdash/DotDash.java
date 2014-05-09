package com.groupa.dotdash.dotdash;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

/**
 * Created by barterd on 5/8/14.
 */
public class DotDash extends Activity {



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
        switch(item.getItemId()){
            case R.id.action_compose:
                //go to compose activity
                startActivity(new Intent(getApplicationContext(), NewMessage.class));
                break;
            case R.id.action_conversations:
                startActivity(new Intent(getApplicationContext(), Conversations.class));
                break;

        }

        return super.onOptionsItemSelected(item);
    }
}
