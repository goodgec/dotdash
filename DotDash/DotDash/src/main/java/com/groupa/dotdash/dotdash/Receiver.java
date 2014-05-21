package com.groupa.dotdash.dotdash;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.widget.Toast;

/**
 * Created by himelica on 5/21/14.
 */
public class Receiver extends BroadcastReceiver {

    public static final String DOT_DASH_RECEIVED_MESSAGE = "dotDashReceivedMessage";

    @Override
    public void onReceive(Context context, Intent intent) {
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

            Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
            Intent newMessageIntent = new Intent();
            newMessageIntent.setAction(DOT_DASH_RECEIVED_MESSAGE);
            context.sendBroadcast(newMessageIntent);

        }

    }
}
