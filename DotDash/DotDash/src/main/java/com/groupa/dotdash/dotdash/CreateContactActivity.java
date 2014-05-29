package com.groupa.dotdash.dotdash;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class CreateContactActivity extends DotDash {

    private TextView nameField;
    private TextView numberField;
    private TextView idField;
    private Button saveButton;
    private Contact contactToEdit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_contact);
        getActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();

        nameField = (TextView)findViewById(R.id.createContactNameField);

        numberField = (TextView)findViewById(R.id.createContactNumberField);

        idField = (TextView)findViewById(R.id.createContactIDField);

        if (intent.hasExtra(CONTACT_NAME)){
            contactToEdit = DataManager.getInstance().getAddressBookNamesMap().get(intent.getStringExtra(CONTACT_NAME));
            getActionBar().setTitle(contactToEdit.getName());
            nameField.setText(contactToEdit.getName());
            numberField.setText(contactToEdit.getNumber());
            idField.setText(contactToEdit.getMorseID());
        }

        saveButton = (Button)findViewById(R.id.createContactSaveButton);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(contactToEdit != null){
                    DataManager.getInstance().removeContact(contactToEdit);
                }
                Contact contact = new Contact(-1, nameField.getText().toString(),
                        numberField.getText().toString(),
                        idField.getText().toString());
                DataManager.getInstance().addContactToDb(contact);

                startActivity(new Intent(getApplicationContext(), ContactsActivity.class));
                overridePendingTransition(0, 0);
                finish();
            }
        });
    }
}
