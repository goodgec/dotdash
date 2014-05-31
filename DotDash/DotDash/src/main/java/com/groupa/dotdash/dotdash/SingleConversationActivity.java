package com.groupa.dotdash.dotdash;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;

public class SingleConversationActivity extends Activity {
    private DataManager dm;
    private ArrayList<Message> messageList;
    private String converser;
    private Vibrator vibrator;

    private ListView conversationListView;
    private Button replyButton;

    protected BubbleArrayAdapter speechBubbleArrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_conversation);
        getActionBar().setDisplayHomeAsUpEnabled(true);

        dm = DataManager.getInstance();

        vibrator = (Vibrator)getSystemService(VIBRATOR_SERVICE);

        Intent intent = getIntent();
        converser = intent.getStringExtra(DotDash.CONTACT_NAME);
        if (converser != null) {
            getActionBar().setTitle(converser);
        }

        messageList = dm.getAddressBookNamesMap().get(converser).getConversation().getMessages();
        conversationListView = (ListView)findViewById(R.id.conversationListView);
        //speechBubbleArrayAdapter = new ArrayAdapter<Message>(this, android.R.layout.simple_list_item_1, messageList);
        speechBubbleArrayAdapter = new BubbleArrayAdapter(this, R.layout.speech_bubble, messageList);
        conversationListView.setAdapter(speechBubbleArrayAdapter);
        //addItems();

        conversationListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int pos, long id) {
                Message selectedMessage = (Message)adapterView.getItemAtPosition(pos);
                vibrator.vibrate(Translator.convertTextToMorse(selectedMessage.getText(), DotDash.wpm), -1);
            }
        });

        replyButton = (Button)findViewById(R.id.singleConversationReplyButton);
        replyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent newMessageIntent = new Intent(view.getContext(), DotDash.class);
                newMessageIntent.putExtra(DotDash.CONTACT_NAME, converser);
//                newMessageIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(newMessageIntent);
//                overridePendingTransition(0, 0);
                finish();
            }
        });
    }
}
