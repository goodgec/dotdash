package com.groupa.dotdash.dotdash;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class SingleContactActivity extends Activity {

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

                setResult(DotDash.RESULT_CODE_DELETED_CONTACT);

                finish();
            }
        });

        editContactButton = (Button) findViewById(R.id.editContactButton);
        editContactButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent editContactIntent = new Intent(view.getContext(), CreateContactActivity.class);
                editContactIntent.putExtra(DotDash.CONTACT_NAME, contact.getName());
                startActivityForResult(editContactIntent, DotDash.RESULT_OK);
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        onBackPressed();
        return true;
    }
}