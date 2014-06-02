package com.groupa.dotdash.dotdash;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class SingleContactActivity extends Activity {

//    private DataManager dm;
    private Contact contact;
    private TextView contactNumberTextView;
    private TextView contactIDTextView;
    private Button sendMessageFromSingleContactButton;
    private Button deleteContactButton;
    private Button editContactButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_contact);
        getActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        contact = DataManager.getInstance().getAddressBookNamesMap().get(intent.getStringExtra(DotDash.CONTACT_NAME));

        getActionBar().setTitle(contact.getName());
        contactNumberTextView = (TextView)findViewById(R.id.phoneNumberTextView);
        contactIDTextView = (TextView)findViewById(R.id.contactIDTextView);
        contactIDTextView.setText(contact.getMorseID());
        contactNumberTextView.setText(contact.getNumber());

        sendMessageFromSingleContactButton = (Button) findViewById(R.id.sendMessageFromSingleContactButton);
        sendMessageFromSingleContactButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent finishIntent = new Intent(view.getContext(), DotDash.class);
                finishIntent.putExtra(DotDash.TARGET_TAB, DotDash.NEW_MESSAGE_TAB_NUMBER);
                finishIntent.putExtra(DotDash.CONTACT_NAME, contact.getName());
                setResult(DotDash.RESULT_CODE_SENDING_MESSAGE, finishIntent);
                finish();
            }
        });

        deleteContactButton = (Button) findViewById(R.id.deleteContactButton);
        deleteContactButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DataManager.getInstance().removeContact(contact);
                // remove all messages from that contact from db

                setResult(DotDash.RESULT_CODE_DELETED_CONTACT);

                finish();
//                overridePendingTransition(0, 0);
//                startActivity(new Intent(getApplicationContext(), ContactsFragment.class));
//                overridePendingTransition(0, 0);
            }
        });

        editContactButton = (Button) findViewById(R.id.editContactButton);
        editContactButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent editContactIntent = new Intent(view.getContext(), CreateContactActivity.class);
                editContactIntent.putExtra(DotDash.CONTACT_NAME, contact.getName());
                //editContactIntent.putExtra(CONTACT_NUMBER, contact.getNumber());
                //editContactIntent.putExtra(CONTACT_ID, contact.getMorseID());
                startActivity(editContactIntent);
//                overridePendingTransition(0, 0);
//                finish();
            }
        });
    }


//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.single_contact, menu);
//        return super.onCreateOptionsMenu(menu);
//    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        onBackPressed();
        return true;
    }
//
//    @Override
//    public void onBackPressed() {
//        Intent intent = new Intent(this, DotDash.class);
//        intent.putExtra(DotDash.TARGET_TAB, DotDash.CONTACTS_TAB_NUMBER);
//        NavUtils.navigateUpTo(this, intent);
//    }
}