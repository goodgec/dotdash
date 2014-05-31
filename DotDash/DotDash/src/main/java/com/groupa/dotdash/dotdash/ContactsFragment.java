package com.groupa.dotdash.dotdash;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;


public class ContactsFragment extends Fragment {
    private ArrayList<Contact> contactsList;
    private ListView contactsListView;
    private Button newContactButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View fragmentView = inflater.inflate(R.layout.activity_contacts, container, false);

        contactsList = DataManager.getInstance().getAddressBookList();
        contactsListView = (ListView)fragmentView.findViewById(R.id.contactsListView);
        ArrayAdapter<Contact> arrayAdapter = new ArrayAdapter<Contact>(getActivity(), android.R.layout.simple_list_item_1, contactsList);
        contactsListView.setAdapter(arrayAdapter);

        contactsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int pos, long id) {
                Intent nameIntent = new Intent(view.getContext(), SingleContactActivity.class);
                nameIntent.putExtra(DotDash.CONTACT_NAME, ((Contact)adapterView.getItemAtPosition(pos)).getName());
                getActivity().startActivityForResult(nameIntent, DotDash.REQUEST_CODE_DELETE_CONTACT);
            }
        });

        newContactButton = (Button)fragmentView.findViewById(R.id.newContactButton);
        newContactButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().startActivityForResult(new Intent(view.getContext(), CreateContactActivity.class), DotDash.REQUEST_CODE_CREATE_CONTACT);
            }
        });

        return fragmentView;
    }
}
