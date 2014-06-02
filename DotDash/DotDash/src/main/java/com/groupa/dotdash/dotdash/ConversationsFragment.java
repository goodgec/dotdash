package com.groupa.dotdash.dotdash;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;


public class ConversationsFragment extends Fragment {

    private ArrayList<Contact> allContactsList;
    private ArrayList<Contact> contactsList;
    private ListView conversationsListView;
    //private ArrayAdapter<Contact> conversationsActivityArrayAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View fragmentView = inflater.inflate(R.layout.activity_conversations, container, false);

        allContactsList = DataManager.getInstance().getAddressBookList();
        contactsList = new ArrayList<Contact>();
        for (Contact c : allContactsList) {
            if (c.getConversation().size() > 0) {
                contactsList.add(c);
            }
        }
        //TODO sort by most recent talking.

        conversationsListView = (ListView)fragmentView.findViewById(R.id.conversationsListView);
        ArrayAdapter<Contact> arrayAdapter = new ArrayAdapter<Contact>(getActivity(), android.R.layout.simple_list_item_1, contactsList);
        conversationsListView.setAdapter(arrayAdapter);

        conversationsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int pos, long id) {
//                Intent nameIntent = new Intent(view.getContext(), SingleConversationActivity.class);
//                nameIntent.putExtra("contactName", ((Contact)adapterView.getItemAtPosition(pos)).getName());
//                startActivity(nameIntent);


                Intent nameIntent = new Intent(view.getContext(), SingleConversationActivity.class);
                nameIntent.putExtra(DotDash.CONTACT_NAME, ((Contact)adapterView.getItemAtPosition(pos)).getName());
                getActivity().startActivityForResult(nameIntent, DotDash.REQUEST_CODE_VIEW_CONVERSATION);
            }
        });

        return fragmentView;
    }
//
//    public void addSender(Contact sender) {
//        conversationsActivityArrayAdapter.add(sender);
//    }
}
