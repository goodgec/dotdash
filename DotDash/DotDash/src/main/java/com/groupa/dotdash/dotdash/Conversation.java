package com.groupa.dotdash.dotdash;

import java.util.ArrayList;

/**
 * Created by adamsr on 5/10/14.
 */
public class Conversation {

    private Contact contact;
    private ArrayList<Message> messages;

    public Conversation(Contact contact) {
        this.contact = contact;
    }

    public Contact getContact() {
        return contact;
    }

    public ArrayList<Message> getMessages() {
        return messages;
    }

    public void addMessage(Message message) {
        messages.add(message);
    }
}
