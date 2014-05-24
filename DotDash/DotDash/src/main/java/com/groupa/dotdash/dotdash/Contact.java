package com.groupa.dotdash.dotdash;

/**
 * Created by adamsr on conversations/10/14.
 */
public class Contact implements Comparable<Contact> {

    private String name;
    private String number;
    private String id;
    private Conversation conversation;

    public Contact(String name, String number) {
        this.number = number;
        this.name = name;
        conversation = new Conversation(this);
    }

    public Contact(String name, String number, String id) {
        this(name, number);
        this.id = id;
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

    public String getId() {
        return id;
    }

    public void setId(String id) {
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
