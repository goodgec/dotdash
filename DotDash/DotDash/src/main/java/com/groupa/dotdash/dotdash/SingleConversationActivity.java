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
    private BubbleArrayAdapter arrayAdapter;


    private ListView conversationListView;
    private Button replyButton;

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
        //arrayAdapter = new ArrayAdapter<Message>(this, android.R.layout.simple_list_item_1, messageList);
        arrayAdapter = new BubbleArrayAdapter(this, R.layout.speech_bubble, messageList);
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
        super.onCreateOptionsMenu(menu);
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
