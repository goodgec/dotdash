package com.groupa.dotdash.dotdash;

/**
 * Created by adamsr on conversations/10/14.
 */
public class Contact implements Comparable<Contact> {

    private long id;
    private String name;
    private String number;
    private String morseID;
    private Conversation conversation;

    public Contact(long id, String name, String number) {
        this.id = id;
        this.number = number;
        this.name = name;
        this.morseID = "";
        conversation = new Conversation(this);
    }

    public Contact(String name, String number) {
        this(-1, name, number);
    }

    public Contact(long id, String name, String number, String morseID) {
        this(id, name, number);
        this.morseID = morseID;
    }

    public Contact(String name, String number, String morseID) {
        this(-1, name, number, morseID);
    }

    public Conversation getConversation() {
        return conversation;
    }

    public void setConversation(Conversation conversation) {
        this.conversation = conversation;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getMorseID() {
        return morseID;
    }

    public void setMorseID(String morseID) {
        this.morseID = morseID;
    }

    public void setInternalID(long id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return  name;
    }

    @Override
    public int compareTo(Contact contact) {
        return name.compareTo(contact.getName());
    }
}
