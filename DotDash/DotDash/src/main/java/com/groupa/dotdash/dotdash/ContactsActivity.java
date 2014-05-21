package com.groupa.dotdash.dotdash;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.Menu;
import android.view.MenuItem;


public class ContactsActivity extends DotDash {
    public Vibrator vibrator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts);
        currentScreen = R.id.action_contacts;

//        // Vibrator stuff:
//        vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
//
//        long[] times = {0, 100, 100, 100, 100, 400, 100, 400, 400, 100};
//        vibrator.vibrate(times, -1);


        displayAlert();
    }



}
