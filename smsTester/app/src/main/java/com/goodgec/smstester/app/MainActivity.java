package com.goodgec.smstester.app;

import android.app.Activity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

public class MainActivity extends Activity implements View.OnClickListener {

    public Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        button = (Button)this.findViewById(R.id.button);
        button.setOnClickListener(this);
    }


    @Override
    public void onClick(View view) {
        // text rachel
        SmsManager manager = SmsManager.getDefault();
        manager.sendTextMessage("2052422946", null, "you are now receiving automatically generated texts courtesy of Alby and Casey", null, null);
    }
}
