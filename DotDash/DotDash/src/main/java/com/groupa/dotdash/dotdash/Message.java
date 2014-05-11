package com.groupa.dotdash.dotdash;

/**
 * Created by adamsr on 5/10/14.
 */
public class Message {

    private String text;
    private Contact from;
    private Contact to;
    private int timestamp;

    public Message(String text, Contact from, Contact to, int timestamp) {
        this.text = text;
        this.from = from;
        this.to = to;
        this.timestamp = timestamp;
    }

    public String getText() {
        return text;
    }

    public Contact getFrom() {
        return from;
    }

    public Contact getTo() {
        return to;
    }

    public int getTimestamp() {
        return timestamp;
    }
}
