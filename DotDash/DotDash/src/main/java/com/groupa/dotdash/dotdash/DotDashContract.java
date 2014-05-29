package com.groupa.dotdash.dotdash;

import android.provider.BaseColumns;

/**
 * Created by himelica on 5/25/14.
 */
public class DotDashContract {

    public DotDashContract() {}

    public static abstract class ContactsTable implements BaseColumns {
        public static final String TABLE_NAME = "contacts";
        public static final String COLUMN_NAME_NAME = "name";
        public static final String COLUMN_NAME_NUMBER = "number";
        public static final String COLUMN_NAME_MORSE_ID = "morse_id";
    }

    public static abstract class MessagesTable implements BaseColumns {
        public static final String TABLE_NAME = "messages";
        public static final String COLUMN_NAME_CONTACT_NAME = "contact";
        public static final String COLUMN_NAME_SENDER = "sender";
        public static final String COLUMN_NAME_TEXT = "text";
        public static final String COLUMN_NAME_TIMESTAMP = "timestamp";
    }
}
