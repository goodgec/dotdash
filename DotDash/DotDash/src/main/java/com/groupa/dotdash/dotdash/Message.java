package com.groupa.dotdash.dotdash;

/**
 * Created by adamsr on 5/10/14.
 */
public class Message {

    private long id;
    private String text;
    private Contact contact;
    private boolean sentMessage;
    private long timestamp;

    public Message(String text, Contact contact, boolean sentMessage) {
        this.text = text;
        this.contact = contact;
        this.sentMessage = sentMessage;
        this.timestamp = System.currentTimeMillis();
    }

    public Message(String text, Contact contact, boolean sentMessage, long timestamp) {
        this.text = text;
        this.contact = contact;
        this.sentMessage = sentMessage;
        this.timestamp = timestamp;
    }

    public String getText() {
        return text;
    }

    public Contact getContact() {
        return contact;
    }

    public boolean isSentMessage() {
        return sentMessage;
    }

    public long getTimestamp() {
        return timestamp;
    }

    @Override
    public String toString() {
        return text;
    }
}
