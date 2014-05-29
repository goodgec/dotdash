package com.groupa.dotdash.dotdash;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by himelica on 5/25/14.
 */
public class DotDashDbHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 2;
    public static final String DATABASE_NAME = "DotDash.db";

    private static final String TEXT_TYPE = " TEXT";
    private static final String INTEGER_TYPE = " INTEGER";
    private static final String COMMA_SEP = ",";

    private static final String SQL_CREATE_TABLE_CONTACTS =
            "CREATE TABLE " + DotDashContract.ContactsTable.TABLE_NAME + " (" +
                    DotDashContract.ContactsTable._ID + " INTEGER PRIMARY KEY," +
                    DotDashContract.ContactsTable.COLUMN_NAME_NAME + TEXT_TYPE + COMMA_SEP +
                    DotDashContract.ContactsTable.COLUMN_NAME_NUMBER + TEXT_TYPE + COMMA_SEP +
                    DotDashContract.ContactsTable.COLUMN_NAME_MORSE_ID + TEXT_TYPE +
                    " )";
    private static final String SQL_DELETE_TABLE_CONTACTS =
            "DROP TABLE IF EXISTS " + DotDashContract.ContactsTable.TABLE_NAME;

    private static final String SQL_CREATE_TABLE_MESSAGES =
            "CREATE TABLE " + DotDashContract.MessagesTable.TABLE_NAME + " (" +
                    DotDashContract.MessagesTable._ID + " INTEGER PRIMARY KEY," +
                    DotDashContract.MessagesTable.COLUMN_NAME_CONTACT_NAME + INTEGER_TYPE + COMMA_SEP +
                    DotDashContract.MessagesTable.COLUMN_NAME_SENDER + INTEGER_TYPE + COMMA_SEP +
                    DotDashContract.MessagesTable.COLUMN_NAME_TEXT + TEXT_TYPE + COMMA_SEP +
                    DotDashContract.MessagesTable.COLUMN_NAME_TIMESTAMP + INTEGER_TYPE +
                    " )";
    private static final String SQL_DELETE_TABLE_MESSAGES =
            "DROP TABLE IF EXISTS " + DotDashContract.MessagesTable.TABLE_NAME;


    public DotDashDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_TABLE_CONTACTS);
        db.execSQL(SQL_CREATE_TABLE_MESSAGES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i2) {
//        db.execSQL(SQL_DELETE_TABLE_CONTACTS);
//        db.execSQL(SQL_DELETE_TABLE_MESSAGES);
//        onCreate(db);
    }
}
