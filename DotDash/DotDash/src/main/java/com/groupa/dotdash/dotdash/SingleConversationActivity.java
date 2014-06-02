package com.groupa.dotdash.dotdash;

import android.app.Activity;
import android.content.Intent;
import android.media.AudioManager;
import android.media.ToneGenerator;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;

public class SingleConversationActivity extends Activity {
    private ArrayList<Message> messageList;
    private String converser;
    private Vibrator vibrator;
    private ToneGenerator beeper;

    private ListView conversationListView;
    private Button replyButton;

    protected BubbleArrayAdapter speechBubbleArrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_conversation);
        getActionBar().setDisplayHomeAsUpEnabled(true);

        vibrator = (Vibrator)getSystemService(VIBRATOR_SERVICE);
        beeper = new ToneGenerator(AudioManager.STREAM_ALARM, 50);


        Intent intent = getIntent();
        converser = intent.getStringExtra(DotDash.CONTACT_NAME);
        if (converser != null) {
            getActionBar().setTitle(converser);
        }

        messageList = DataManager.getInstance().getAddressBookNamesMap().get(converser).getConversation().getMessages();
        conversationListView = (ListView)findViewById(R.id.conversationListView);
        //speechBubbleArrayAdapter = new ArrayAdapter<Message>(this, android.R.layout.simple_list_item_1, messageList);
        speechBubbleArrayAdapter = new BubbleArrayAdapter(this, R.layout.speech_bubble, messageList);
        conversationListView.setAdapter(speechBubbleArrayAdapter);
        //addItems();

        conversationListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int pos, long id) {
                Message selectedMessage = (Message)adapterView.getItemAtPosition(pos);
                Translator.outputMessage(view.getContext(), Translator.convertTextToMorse(selectedMessage.getText(), DotDash.wpm));
            }
        });

        replyButton = (Button)findViewById(R.id.singleConversationReplyButton);
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
    }
}
