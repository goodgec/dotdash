package com.groupa.dotdash.dotdash;

/**
 * Created by adamsr on 5/10/14.
 */
public class Message {

    private String text;
    private Contact sender;
    private Contact recipient;
    private long timestamp;

    public Message(String text, Contact from, Contact to) {
        this.text = text;
        this.sender = from;
        this.recipient = to;
        this.timestamp = System.currentTimeMillis();
    }

    public String getText() {
        return text;
    }

    public Contact getSender() {
        return sender;
    }

    public Contact getRecipient() {
        return recipient;
    }

    public long getTimestamp() {
        return timestamp;
    }
}
