package com.goodgec.smstester.app;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.widget.Toast;

/**
 * Created by alby on 5/12/14.
 */
public class Receiver extends BroadcastReceiver {

    private MainActivity2 activity;
    private String message;

    public Receiver(MainActivity2 activity) {
        super();
        this.activity = activity;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        //---get the SMS message passed in---
        Bundle bundle = intent.getExtras();
        SmsMessage[] msgs = null;
        String message = "";
        if (bundle != null) {
            //---retrieve the SMS message received---
            Object[] pdus = (Object[]) bundle.get("pdus");
            msgs = new SmsMessage[pdus.length];
            for (int i=0; i<msgs.length; i++) {
                msgs[i] = SmsMessage.createFromPdu((byte[])pdus[i]);
                message += msgs[i].getMessageBody().toString();
                message += "\n";
            }
            if (message.contains("ddc")) {
                Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
                activity.setSmsDetails(message);
                abortBroadcast();
            }

//            if (message.contains("ddc")) {
//                Intent broadcastIntent = new Intent();
//                broadcastIntent.setAction("MAGIC");
//                broadcastIntent.putExtra("sms", message);
//                context.sendBroadcast(broadcastIntent);
////                abortBroadcast();
//            }
        }
    }
}
