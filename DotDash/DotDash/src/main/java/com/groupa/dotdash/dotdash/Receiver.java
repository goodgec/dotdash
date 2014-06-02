package com.groupa.dotdash.dotdash;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
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
        String phoneNumber = "";
        if (bundle != null) {
            //---retrieve the SMS message received---
            Object[] pdus = (Object[]) bundle.get("pdus");
            msgs = new SmsMessage[pdus.length];
            for (int i=0; i<msgs.length; i++) {
                msgs[i] = SmsMessage.createFromPdu((byte[])pdus[i]);
                message += msgs[i].getMessageBody().toString();
                message += "";//this was adding a newline, it made things look bad.
                phoneNumber = msgs[i].getOriginatingAddress();
            }

//            Toast.makeText(context, message, Toast.LENGTH_LONG).show();
            Contact sender = DataManager.getInstance().getAddressBookNumbersMap().get(phoneNumber);

            if (sender != null) {
//                Toast.makeText(context, "first", Toast.LENGTH_SHORT).show();
                Message newMessage = new Message(message, sender, false);
//                Toast.makeText(context, "second", Toast.LENGTH_SHORT).show();

                if (sender.getConversation().isDuplicate(newMessage)) {
                    return;
                }

                //Build Notification.
                NotificationCompat.Builder notifier = new NotificationCompat.Builder(context);
                notifier.setSmallIcon(R.drawable.ic_launcher);
                notifier.setContentTitle(newMessage.getContact().getName() + ": ");
                notifier.setContentText(newMessage.getText());
                notifier.build();

                Intent notifyIntent = new Intent(context, DotDash.class);
                notifyIntent.putExtra(DotDash.CAME_FROM_NOTIFICATION, DotDash.CAME_FROM_NOTIFICATION);
                notifyIntent.putExtra(DotDash.CONTACT_NAME, sender.getName());

                TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
                stackBuilder.addParentStack(DotDash.class);
                stackBuilder.addNextIntent(notifyIntent);
                PendingIntent resultPendingIntent =
                        stackBuilder.getPendingIntent(
                                0,
                                PendingIntent.FLAG_UPDATE_CURRENT
                        );
                notifier.setContentIntent(resultPendingIntent);
                NotificationManager mNotificationManager =
                        (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
                mNotificationManager.notify(0, notifier.build());





                DataManager.getInstance().addMessageToDb(newMessage);
//                Toast.makeText(context, "fourth", Toast.LENGTH_SHORT).show();

                sender.getConversation().addMessage(newMessage);
//                Toast.makeText(context, "fifth", Toast.LENGTH_SHORT).show();

            }


//            Intent newMessageIntent = new Intent();
//            newMessageIntent.putExtra(DotDash.MESSAGE_SENDER, phoneNumber);
//            newMessageIntent.putExtra(DotDash.MESSAGE_TEXT, message);
//            newMessageIntent.putExtra(DotDash.MESSAGE_TIMESTAMP, msgs[0].getTimestampMillis());
//            newMessageIntent.setAction(DOT_DASH_RECEIVED_MESSAGE);
            //context.sendBroadcast(newMessageIntent);

        }

    }
}
