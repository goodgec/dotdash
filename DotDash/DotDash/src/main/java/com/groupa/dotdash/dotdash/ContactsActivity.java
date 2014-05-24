package com.groupa.dotdash.dotdash;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;


public class ContactsActivity extends DotDash {
    private ArrayList<Contact> contactsList;
    private DataManager dm;
    private ListView contactsListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts);
        currentScreen = R.id.action_contacts;

        dm = DataManager.getInstance();
        contactsList = dm.getAddressBookList();
        contactsListView = (ListView)findViewById(R.id.contactsListView);
        ArrayAdapter<Contact> arrayAdapter = new ArrayAdapter<Contact>(this, android.R.layout.simple_list_item_1, contactsList);
        contactsListView.setAdapter(arrayAdapter);

        contactsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int pos, long id) {
                Intent nameIntent = new Intent(getApplicationContext(), SingleContactActivity.class);
                nameIntent.putExtra("contactName", ((Contact)adapterView.getItemAtPosition(pos)).getName());
                startActivity(nameIntent);
//                startActivityForResult(nameIntent, RESULT_OK);
            }
        });

//        // Vibrator stuff:
//        vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
//
//        long[] times = {0, 100, 100, 100, 100, 400, 100, 400, 400, 100};
//        vibrator.vibrate(times, -1);


//        displayAlert();
    }

//    @Override
//    protected void onActivityResult(int requested, int result, Intent intent) {
//        Toast.makeText(this, String.valueOf(result), Toast.LENGTH_LONG).show();
//        finish();
//    }

}
