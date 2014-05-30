package com.groupa.dotdash.dotdash;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

import javax.xml.parsers.FactoryConfigurationError;

/**
 * Created by adamsr on 5/10/14.
 */
public class Conversation {

    private Contact contact;
    private ArrayList<Message> messages;

    public Conversation(Contact contact) {
        this.contact = contact;
        messages = new ArrayList<Message>();
    }

    public Contact getContact() {
        return contact;
    }

    public ArrayList<Message> getMessages() {
        Collections.sort(messages);
        return messages;
    }

    public void addMessage(Message message) {
        messages.add(message);
    }

    public boolean isDuplicate(Message message) {
        if (messages.size() == 0) {
            return false;
        }
        Message lastMessage = messages.get(messages.size() - 1);
        if (message.getTimestamp() < lastMessage.getTimestamp() - DotDash.MESSAGE_DUPLICATE_WINDOW || !message.getText().equals(lastMessage.getText())) {
            return false;
        }
        return true;
    }

    public int size() {
        return messages.size();
    }
}
