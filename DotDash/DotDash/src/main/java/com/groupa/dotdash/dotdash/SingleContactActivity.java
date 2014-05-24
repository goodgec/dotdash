package com.groupa.dotdash.dotdash;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class SingleContactActivity extends DotDash {

//    private DataManager dm;
    private Contact contact;
    private TextView contactNumberTextView;
    private TextView contactIDTextView;
    private Button sendMessageFromSingleContactButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_contact);
        getActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        contact = DataManager.getInstance().getAddressBookNamesMap().get(intent.getStringExtra("contactName"));

        getActionBar().setTitle(contact.getName());
        contactNumberTextView = (TextView)findViewById(R.id.phoneNumberTextView);
        contactIDTextView = (TextView)findViewById(R.id.contactIDTextView);
        contactIDTextView.setText(contact.getId());
        contactNumberTextView.setText(contact.getNumber());

        sendMessageFromSingleContactButton = (Button) findViewById(R.id.sendMessageFromSingleContactButton);
        sendMessageFromSingleContactButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent returnIntent = new Intent();
//                setResult(RESULT_OK, returnIntent);

                Intent newMessageIntent = new Intent(view.getContext(), NewMessageActivity.class);
                newMessageIntent.putExtra(CONTACT_NAME, contact.getName());
                newMessageIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(newMessageIntent);
                finish();
            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.single_contact, menu);
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

}
