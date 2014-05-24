package com.groupa.dotdash.dotdash;

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
    private static final DataManager dm = new DataManager();

    public DataManager(){
        me = new Contact("Me", "0");
        //populate address book
        addressBookNames = new HashMap<String, Contact>();
        addressBookNumbers = new HashMap<String, Contact>();
        populateAddressBooks();
    }

    private void populateAddressBooks() {
        // load things from file.
        Contact alby = new Contact("Alby", "7067654085", "a");
        Contact daniel = new Contact("Daniel", "6177770723", "d");
        Contact rachel = new Contact("Rachel", "2052422946", "r");

        addressBookNames.put(alby.getName(), alby);
        addressBookNames.put(daniel.getName(), daniel);
        addressBookNames.put(rachel.getName(), rachel);

        addressBookNumbers.put(alby.getNumber(), alby);
        addressBookNumbers.put(daniel.getNumber(), daniel);
        addressBookNumbers.put(rachel.getNumber(), rachel);
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
