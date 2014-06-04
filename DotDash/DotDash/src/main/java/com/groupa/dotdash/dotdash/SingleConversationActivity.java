package com.groupa.dotdash.dotdash;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.ActionProvider;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;

public class SingleConversationActivity extends Activity {
    private ArrayList<Message> messageList;
    private String converser;

    private ListView conversationListView;
    private Button replyButton;

    private BubbleArrayAdapter speechBubbleArrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_conversation);
        getActionBar().setDisplayHomeAsUpEnabled(true);

        conversationListView = (ListView)findViewById(R.id.conversationListView);
        replyButton = (Button)findViewById(R.id.singleConversationReplyButton);

        Intent intent = getIntent();
        converser = intent.getStringExtra(DotDash.CONTACT_NAME);
        if (converser != null) {
            getActionBar().setTitle(converser);
        }

        messageList = DataManager.getInstance().getAddressBookNamesMap().get(converser).getConversation().getMessages();
        speechBubbleArrayAdapter = new BubbleArrayAdapter(this, R.layout.speech_bubble, messageList);
        conversationListView.setAdapter(speechBubbleArrayAdapter);

        conversationListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int pos, long id) {
                Message selectedMessage = (Message)adapterView.getItemAtPosition(pos);
                DotDash.outputMessage(Translator.convertTextToMorse(selectedMessage.getText(), DotDash.wpm));
            }
        });

        replyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent finishIntent = new Intent(view.getContext(), DotDash.class);
                finishIntent.putExtra(DotDash.TARGET_TAB, DotDash.NEW_MESSAGE_TAB_NUMBER);
                finishIntent.putExtra(DotDash.CONTACT_NAME, converser);
                setResult(DotDash.RESULT_CODE_REPLY_MESSAGE, finishIntent);
                finish();
            }
        });

        conversationListView.post(new Runnable(){
            public void run() {
                conversationListView.setSelection(conversationListView.getCount() - 1);
            }});

        if (intent.getBooleanExtra(DotDash.PLAY_MESSAGE, false)) {
            Message lastMessage = messageList.get(messageList.size() - 1);
            DotDash.outputMessage(Translator.convertTextToMorse(lastMessage.getText(), DotDash.wpm));
        }
    }
}
