package com.groupa.dotdash.dotdash;

import android.app.Notification;
import android.content.Context;
import android.support.v4.app.NotificationCompat;

/**
 * Created by barterd on 6/1/14.
 */
public class MessageNotification extends NotificationCompat.Builder {

    /**
     * Constructor.
     * <p/>
     * Automatically sets the when field to {@link System#currentTimeMillis()
     * System.currentTimeMillis()} and the audio stream to the
     * {@link android.app.Notification#STREAM_DEFAULT}.
     *
     * @param context A {@link android.content.Context} that will be used to construct the
     *                RemoteViews. The Context will not be held past the lifetime of this
     *                Builder object.
     */
    public MessageNotification(Context context) {
        super(context);
    }


}
