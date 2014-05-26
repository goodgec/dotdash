package com.groupa.dotdash.dotdash;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

/**
 * Created by barterd on 5/23/14.
 */
public class DataManager {
    private Contact me;
    private HashMap<String, Contact> addressBookNames;
    private HashMap<String, Contact> addressBookNumbers;
    private DotDashDbHelper dbHelper;

    private static final DataManager dm = new DataManager();

    public DataManager(){
        me = new Contact(-1, "Me", "0");
        //populate address book
        addressBookNames = new HashMap<String, Contact>();
        addressBookNumbers = new HashMap<String, Contact>();
        dbHelper = new DotDashDbHelper(DotDash.appContext);
        populateAddressBooks();
    }

    private void populateAddressBooks() {
        // load things from file.
        SQLiteDatabase db = dbHelper.getReadableDatabase();

//        String[] projection = {
//                DotDashContract.ContactsTable._ID,
//                DotDashContract.ContactsTable.COLUMN_NAME_NAME,
//                DotDashContract.ContactsTable.COLUMN_NAME_NUMBER,
//                DotDashContract.ContactsTable.COLUMN_NAME_MORSE_ID
//        };

        Cursor c = db.query(DotDashContract.ContactsTable.TABLE_NAME, null, null, null, null, null, null);
        c.moveToFirst();
        while (c.moveToNext()) {
            Contact contact = createContact(c);
            addressBookNames.put(contact.getName(), contact);
            addressBookNumbers.put(contact.getNumber(), contact);
        }


//        Contact alby = new Contact("Alby", "7067654085", "a");
//        Contact daniel = new Contact("Daniel", "6177770723", "d");
//        Contact rachel = new Contact("Rachel", "2052422946", "r");
//
//        addressBookNames.put(alby.getName(), alby);
//        addressBookNames.put(daniel.getName(), daniel);
//        addressBookNames.put(rachel.getName(), rachel);
//
//        addressBookNumbers.put(alby.getNumber(), alby);
//        addressBookNumbers.put(daniel.getNumber(), daniel);
//        addressBookNumbers.put(rachel.getNumber(), rachel);
    }

    private Contact createContact(Cursor c) {
        Contact contact = new Contact(0, c.getString(0), c.getString(1), c.getString(2));
        return contact;
    }

    public void addContact(String name, String number, String id) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(DotDashContract.ContactsTable.COLUMN_NAME_NAME, name);
        values.put(DotDashContract.ContactsTable.COLUMN_NAME_NUMBER, number);
        values.put(DotDashContract.ContactsTable.COLUMN_NAME_MORSE_ID, id);

        long newRowId = db.insert(DotDashContract.ContactsTable.TABLE_NAME, null, values);
        Contact contact = new Contact(newRowId, name, number, id);
        addressBookNames.put(contact.getName(), contact);
        addressBookNumbers.put(contact.getNumber(), contact);
    }

    public static DataManager getInstance() {
        return dm;
    }

    public HashMap<String, Contact> getAddressBookNamesMap() {
        return addressBookNames;
    }

    public void setAddressBookNames(HashMap<String, Contact> addressBookNames) {
        this.addressBookNames = addressBookNames;
    }

    public HashMap<String, Contact> getAddressBookNumbersMap() {
        return addressBookNumbers;
    }

    public ArrayList<Contact> getAddressBookList() {
        ArrayList<Contact> sortedContacts = new ArrayList<Contact>(addressBookNames.values());
        Collections.sort(sortedContacts);
        return sortedContacts;
    }

    public Contact getMe() {
        return me;
    }
}
