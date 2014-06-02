package com.groupa.dotdash.dotdash;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.widget.ArrayAdapter;

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

    private String currentMessageText;
    private ArrayList<Message> newMessages;

    private static final DataManager dm = new DataManager();

    public DataManager(){
        newMessages = new ArrayList<Message>();
        currentMessageText = "";
        me = new Contact(-1, "Me", "0");
        //populate address book
        dbHelper = new DotDashDbHelper(DotDash.appContext);
        populateAddressBooks();
    }

    public void addContactToDb(Contact contact) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(DotDashContract.ContactsTable.COLUMN_NAME_NAME, contact.getName());
        values.put(DotDashContract.ContactsTable.COLUMN_NAME_NUMBER, contact.getNumber());
        values.put(DotDashContract.ContactsTable.COLUMN_NAME_MORSE_ID, contact.getMorseID());

        long newRowId = db.insert(DotDashContract.ContactsTable.TABLE_NAME, null, values);
        contact.setInternalID(newRowId);
        addressBookNames.put(contact.getName(), contact);
        addressBookNumbers.put(contact.getNumber(), contact);
    }

    private Contact createContactFromDb(Cursor c) {
        Contact contact = new Contact(c.getInt(0), c.getString(1), c.getString(2), c.getString(3));
        return contact;
    }

    private void populateAddressBooks() {
        // load things from file.
        addressBookNames = new HashMap<String, Contact>();
        addressBookNumbers = new HashMap<String, Contact>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor c = db.query(DotDashContract.ContactsTable.TABLE_NAME, null, null, null, null, null, null);
        c.moveToFirst();
        while (c.moveToNext()) {
            Contact contact = createContactFromDb(c);
            addressBookNames.put(contact.getName(), contact);
            addressBookNumbers.put(contact.getNumber(), contact);
//            populateConversation(contact, db);
        }
        db.close();

        for (Contact contact : getAddressBookList()) {
            populateConversation(contact);
        }

    }

    public void addMessageToDb(Message message) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(DotDashContract.MessagesTable.COLUMN_NAME_CONTACT_NAME, message.getContact().getName());
        values.put(DotDashContract.MessagesTable.COLUMN_NAME_SENDER, message.isSentMessage() ? 1 : 0);
        values.put(DotDashContract.MessagesTable.COLUMN_NAME_TEXT, message.getText());
        values.put(DotDashContract.MessagesTable.COLUMN_NAME_TIMESTAMP, message.getTimestamp());

        db.insert(DotDashContract.MessagesTable.TABLE_NAME, null, values);

        newMessages.add(message);
    }

    public Message getNextMessage() {

        return newMessages.size() > 0 ? newMessages.remove(0) : null;
    }

    private Message createMessageFromDb(Cursor c) {
        Contact contact = addressBookNames.get(c.getString(2));
        Message message = new Message(c.getString(3), contact, c.getInt(2)==1, c.getLong(4));
        Log.w("string4", String.valueOf(c.getInt(2) == 1));
        return message;
    }

    private void populateConversation(Contact contact) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor c = db.query(DotDashContract.MessagesTable.TABLE_NAME,
                null,
                DotDashContract.MessagesTable.COLUMN_NAME_CONTACT_NAME + " =? ",
                new String[] {contact.getName()},
                null,
                null,
                DotDashContract.MessagesTable.COLUMN_NAME_TIMESTAMP + " ASC");
        c.moveToFirst();
        // add first message
//        contact.getConversation().addMessage(createMessageFromDb(c));
        while (c.moveToNext()) {
            // add the rest
            Message message = createMessageFromDb(c);
            contact.getConversation().addMessage(message);
        }
        db.close();
    }

    public ArrayAdapter<Contact> searchContacts(String query) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ArrayAdapter<Contact> adapter = new ArrayAdapter<Contact>(DotDash.appContext, android.R.layout.simple_list_item_1);

        Cursor c = db.query(DotDashContract.ContactsTable.TABLE_NAME,
                new String[] {DotDashContract.ContactsTable.COLUMN_NAME_NUMBER},
                DotDashContract.ContactsTable.COLUMN_NAME_NAME + " LIKE '%" + query + "%' OR " +
                        DotDashContract.ContactsTable.COLUMN_NAME_NUMBER + " LIKE '%" + query + "%'",
                null,
                null,
                null,
                DotDashContract.ContactsTable.COLUMN_NAME_NAME + " ASC");
        c.moveToFirst();
        while (c.moveToNext()) {
            adapter.add(addressBookNumbers.get(c.getString(0)));
        }
        db.close();

        return adapter;
    }

    public void removeContact(Contact contact){
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.delete(DotDashContract.ContactsTable.TABLE_NAME,
                DotDashContract.ContactsTable.COLUMN_NAME_NAME + " =? ",
                new String[] {contact.getName()});

        db.delete(DotDashContract.MessagesTable.TABLE_NAME,
                DotDashContract.MessagesTable.COLUMN_NAME_CONTACT_NAME + " =? ",
                new String[] {contact.getName()});

        addressBookNumbers.remove(contact.getNumber());
        addressBookNames.remove(contact.getName());
        db.close();
    }
      
    public void editContact(Contact oldContact, Contact newContact) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues cvContact = new ContentValues();
        cvContact.put(DotDashContract.ContactsTable.COLUMN_NAME_NAME, newContact.getName());
        cvContact.put(DotDashContract.ContactsTable.COLUMN_NAME_NUMBER, newContact.getNumber());
        cvContact.put(DotDashContract.ContactsTable.COLUMN_NAME_MORSE_ID, newContact.getMorseID());

        db.update(DotDashContract.ContactsTable.TABLE_NAME, cvContact,
                DotDashContract.ContactsTable.COLUMN_NAME_NAME + " =? ",
                new String[] {oldContact.getName()});

        ContentValues cvMessages = new ContentValues();
        cvMessages.put(DotDashContract.MessagesTable.COLUMN_NAME_CONTACT_NAME, newContact.getName());

        db.update(DotDashContract.MessagesTable.TABLE_NAME, cvMessages,
                DotDashContract.MessagesTable.COLUMN_NAME_CONTACT_NAME + " =? ",
                new String[] {oldContact.getName()});

        addressBookNumbers.remove(oldContact.getNumber());
        addressBookNames.remove(oldContact.getName());

        addressBookNames.put(newContact.getName(), newContact);
        addressBookNumbers.put(newContact.getNumber(), newContact);

        db.close();
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

    public String getCurrentMessageText() {
        return currentMessageText;
    }

    public void setCurrentMessageText(String currentMessageText) {
        this.currentMessageText = currentMessageText;
    }
}
