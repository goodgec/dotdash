package com.groupa.dotdash.dotdash;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class SingleConversationActivity extends DotDash {
    private DataManager dm;
    private ArrayList<Message> messageList;
    private String converser;
    private Vibrator vibrator;


    private ListView conversationListView;
    private Button replyButton;

    private class BubbleArrayAdapter extends ArrayAdapter {
        private ArrayList<Message> messages;
        private LinearLayout wrapper;
        private TextView bubbleText;

        public void add(Message message) {
            super.add(message);
        }

        public BubbleArrayAdapter(Context context, int resource, ArrayList<Message> messages) {
            super(context, resource);
            this.messages = messages;
        }

        public int getCount() {
            return messages.size();
        }

        public Message getItem (int index) {
            return messages.get(index);
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            View row = convertView;
            if (row == null){
                LayoutInflater inflater = (LayoutInflater) this.getContext().getSystemService(LAYOUT_INFLATER_SERVICE);
                row = inflater.inflate(R.layout.activity_single_conversation, parent, false);
            }

            wrapper = (LinearLayout) row.findViewById(R.id.wrapper);

            Message message = getItem(position);

            if (message.isSentMessage()) {
                bubbleText = (TextView) row.findViewById(R.id.rightBubbleText);
                bubbleText.setText(message.getText());
                bubbleText.setBackgroundResource(R.drawable.rightbubble);
                wrapper.setGravity(Gravity.RIGHT);
            }
            else {
                bubbleText = (TextView) row.findViewById(R.id.leftBubbleText);
                bubbleText.setText(message.getText());
                bubbleText.setBackgroundResource(R.drawable.leftbubble);
                wrapper.setGravity(Gravity.LEFT);
            }

            return row;
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_conversation);
        getActionBar().setDisplayHomeAsUpEnabled(true);

        dm = DataManager.getInstance();

        vibrator = (Vibrator)getSystemService(VIBRATOR_SERVICE);

        Intent intent = getIntent();
        converser = intent.getStringExtra(CONTACT_NAME);
        if (converser != null) {
            getActionBar().setTitle(converser);
        }


        messageList = dm.getAddressBookNamesMap().get(converser).getConversation().getMessages();
        conversationListView = (ListView)findViewById(R.id.conversationListView);
        arrayAdapter = new ArrayAdapter<Message>(this, android.R.layout.simple_list_item_1, messageList);
        //arrayAdapter = new BubbleArrayAdapter(this, R.layout.activity_single_conversation, messageList);
        conversationListView.setAdapter(arrayAdapter);
        //addItems();

        conversationListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int pos, long id) {
                Message selectedMessage = (Message)adapterView.getItemAtPosition(pos);
                vibrator.vibrate(Translator.convertTextToMorse(selectedMessage.getText()), -1);
            }
        });

        replyButton = (Button)findViewById(R.id.singleConversationReplyButton);
        replyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent newMessageIntent = new Intent(view.getContext(), NewMessageActivity.class);
                newMessageIntent.putExtra(CONTACT_NAME, converser);
//                newMessageIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(newMessageIntent);
                finish();
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this .s items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.single_conversation, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

//    private void addItems(){
//        for(Message m : messageList){
//            arrayAdapter.add(m);
//        }
//    }

}
