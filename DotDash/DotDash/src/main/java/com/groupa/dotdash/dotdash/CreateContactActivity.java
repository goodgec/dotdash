package com.groupa.dotdash.dotdash;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class CreateContactActivity extends Activity {

    private TextView nameField;
    private TextView numberField;
    private TextView idField;
    private Button saveButton;
    private Contact contactToEdit;
    private boolean editingContact;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_contact);
        getActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();

        nameField = (TextView)findViewById(R.id.createContactNameField);
        numberField = (TextView)findViewById(R.id.createContactNumberField);
        idField = (TextView)findViewById(R.id.createContactIDField);

        idField.setInputType(InputType.TYPE_TEXT_FLAG_CAP_CHARACTERS);

        editingContact = false;
        if (intent.hasExtra(DotDash.CONTACT_NAME)){
            editingContact = true;
            contactToEdit = DataManager.getInstance().getAddressBookNamesMap().get(intent.getStringExtra(DotDash.CONTACT_NAME));
            getActionBar().setTitle(contactToEdit.getName());
            nameField.setText(contactToEdit.getName());
            numberField.setText(contactToEdit.getNumber());
            idField.setText(contactToEdit.getMorseID());
        }

        saveButton = (Button)findViewById(R.id.createContactSaveButton);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (nameField.getText().toString().equals("")
                        || numberField.getText().equals("")
                        || numberField.getText().length() != 10
                        || idField.getText().length() == 0) {
                    displayInvalidAlert();
                    return;
                }

                Contact contactFromId = DataManager.getInstance().getAddressBookMorseIDs().get(idField.getText().toString());
                Contact contactFromNumber = DataManager.getInstance().getAddressBookNumbersMap().get(numberField.getText().toString());
                if ((contactFromId == null || (editingContact && contactFromId.equals(contactToEdit)))
                        && (contactFromNumber == null || (editingContact && contactFromNumber.equals(contactToEdit)))) {

                    Contact contact = new Contact(-1, nameField.getText().toString(),
                            numberField.getText().toString(),
                            idField.getText().toString());

                    if(editingContact && !contact.equals(contactToEdit)){
                        DataManager.getInstance().editContact(contactToEdit, contact);
                    }
                    else {
                        DataManager.getInstance().addContactToDb(contact);
                    }

                    Intent intent = new Intent();
                    intent.putExtra(DotDash.CONTACT_NAME, contact.getName());
                    intent.putExtra(DotDash.CONTACT_NUMBER, contact.getNumber());
                    intent.putExtra(DotDash.CONTACT_MORSE_ID, contact.getMorseID());
                    setResult(RESULT_OK, intent);

                    finish();
                }
                else {
                    displayDuplicateAlert(contactFromId != null ? contactFromId.getName() : contactFromNumber.getName());
                }

            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        onBackPressed();
        return true;
    }

    private void displayInvalidAlert()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false)
               .setMessage("Contacts must have a name, a 10-digit phone number, and an ID.")
               .setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
        AlertDialog alert = builder.create();
        alert.show();
    }

    private void displayDuplicateAlert(String name)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false)
                .setMessage("Contacts must have a unique phone number and ID.\n\nConflicting contact: " + name)
                .setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
        AlertDialog alert = builder.create();
        alert.show();
    }
}
