package com.goodgec.smstester.app;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.provider.Telephony;
import android.telephony.SmsMessage;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity2 extends Activity {

    private TextView textView;
    private Button button2;
    private String message = "";
    private BroadcastReceiver intentReceiver;

//    private BroadcastReceiver intentReceiver = new BroadcastReceiver() {
//        @Override
//        public void onReceive(Context context, Intent intent) {
//            //---display the SMS received in the TextView---
//            TextView SMSes = (TextView)findViewById(R.id.textView);
//            SMSes.setText(intent.getExtras().getString("sms"));
//
//        }
//    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        intentReceiver = new Receiver(this);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.provider.Telephony.SMS_RECEIVED");
        intentFilter.setPriority(999);

        registerReceiver(intentReceiver, intentFilter);

        textView = (TextView)findViewById(R.id.textView);
        textView.setText(message);

        button2 = (Button)findViewById(R.id.button2);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                startActivity(new Intent(view.getContext(), MainActivity.class));
            }
        });
    }

    public void setSmsDetails(String SMSBody) {
        message += SMSBody;
        textView.setText(message);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_activity2, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
