package com.groupa.dotdash.dotdash;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;


public class ConversationsActivity extends DotDash {

    private ArrayList<Contact> allContactsList;
    private ArrayList<Contact> contactsList;
    private ListView conversationsListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conversations);
        currentScreen = R.id.action_conversations;

        allContactsList = DataManager.getInstance().getAddressBookList();
        contactsList = new ArrayList<Contact>();
        for (Contact c : allContactsList) {
            if (c.getConversation().size() > 0) {
                contactsList.add(c);
            }
        }
        //TODO sort by most recent talking.

        conversationsListView = (ListView)findViewById(R.id.conversationsListView);
        conversationsActivityArrayAdapter = new ArrayAdapter<Contact>(this, android.R.layout.simple_list_item_1, contactsList);
        conversationsListView.setAdapter(conversationsActivityArrayAdapter);

        conversationsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int pos, long id) {
                Intent nameIntent = new Intent(getApplicationContext(), SingleConversationActivity.class);
                nameIntent.putExtra("contactName", ((Contact)adapterView.getItemAtPosition(pos)).getName());
                startActivity(nameIntent);
                overridePendingTransition(0, 0);
            }
        });
    }


}
