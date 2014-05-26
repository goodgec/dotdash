package com.groupa.dotdash.dotdash;

import android.app.Activity;
import android.content.Context;
import android.text.Layout;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by barterd on 5/25/14.
 */
public class BubbleArrayAdapter extends ArrayAdapter<Message> {
    private ArrayList<Message> messages;
    private TextView bubbleText;
    private int layoutResource;
    private Context context;

    public BubbleArrayAdapter(Context context, int resource, ArrayList<Message> messages) {
        super(context, resource);
        this.layoutResource = resource;
        this.context = context;
        this.messages = messages;
    }

    public void add(Message message) {
        super.add(message);
    }

    public int getCount() {
        return messages.size();
    }

    public Message getItem (int index) {
        return messages.get(index);
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;

        Message message;

        if (row == null){
            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            row = inflater.inflate(layoutResource, parent, false);

            message = getItem(position);

            row.setTag(message);

        }
        else {
            message = (Message)row.getTag();
        }

        LinearLayout wrapper = (LinearLayout)row.findViewById(R.id.wrapper);

        if (message.getContact().equals(DataManager.getInstance().getMe())) {
            bubbleText = (TextView) row.findViewById(R.id.bubbleText);
            bubbleText.setText(message.getText());
            bubbleText.setBackgroundResource(R.drawable.rightbubble);
            wrapper.setGravity(Gravity.RIGHT);
        }
        else {
            bubbleText = (TextView) row.findViewById(R.id.bubbleText);
            bubbleText.setText(message.getText());
            bubbleText.setBackgroundResource(R.drawable.leftbubble);
            bubbleText.setGravity(Gravity.LEFT);
            wrapper.setGravity(Gravity.LEFT);
        }

        return row;
    }
}