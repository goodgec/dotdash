package com.goodgec.smstester.app;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends Activity {

    public Button button;
    public Button button2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        button = (Button)findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                PendingIntent sentPI = PendingIntent.getBroadcast(view.getContext(), 0, new Intent("SMS_SENT"), 0);
//                PendingIntent deliveredPI = PendingIntent.getBroadcast(view.getContext(), 0, new Intent("SMS_DELIVERED"), 0);
//                //---when the SMS has been sent---
//                registerReceiver(new BroadcastReceiver() {
//                    @Override
//                    public void onReceive(Context arg0, Intent arg1) {
//                        switch (getResultCode()) {
//                            case Activity.RESULT_OK:
//                                Toast.makeText(getBaseContext(), "SMS sent", Toast.LENGTH_SHORT).show();
//                                break;
//                            case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
//                                Toast.makeText(getBaseContext(), "Generic failure",
//                                        Toast.LENGTH_SHORT).show();
//                                break;
//                            case SmsManager.RESULT_ERROR_NO_SERVICE:
//                                Toast.makeText(getBaseContext(), "No service",
//                                        Toast.LENGTH_SHORT).show();
//                                break;
//                            case SmsManager.RESULT_ERROR_NULL_PDU:
//                                Toast.makeText(getBaseContext(), "Null PDU",
//                                        Toast.LENGTH_SHORT).show();
//                                break;
//                            case SmsManager.RESULT_ERROR_RADIO_OFF:
//                                Toast.makeText(getBaseContext(), "Radio off",
//                                        Toast.LENGTH_SHORT).show();
//                                break;
//                        }
//                    }
//                }, new IntentFilter("SMS_SENT"));
//                //---when the SMS has been delivered---
//                registerReceiver(new BroadcastReceiver(){
//                    @Override
//                    public void onReceive(Context arg0, Intent arg1) {
//                        switch (getResultCode())
//                        {
//                            case Activity.RESULT_OK:
//                                Toast.makeText(getBaseContext(), "SMS delivered",
//                                        Toast.LENGTH_SHORT).show();
//                                break;
//                            case Activity.RESULT_CANCELED:
//                                Toast.makeText(getBaseContext(), "SMS not delivered",
//                                        Toast.LENGTH_SHORT).show();
//                                break;
//                        }
//                    }
//                }, new IntentFilter("SMS_DELIVERED"));

                SmsManager manager = SmsManager.getDefault();
                manager.sendTextMessage("6177770723", null, "ddctest", null, null);
//                manager.sendDataMessage("6177770723", null, (short)8901, test.getBytes(), sentPI, deliveredPI);
            }
        });

        button2 = (Button)findViewById(R.id.button2);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                startActivity(new Intent(view.getContext(), MainActivity2.class));
            }
        });
    }
}
