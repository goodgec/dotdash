package com.groupa.dotdash.dotdash;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.text.InputType;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

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
                        || idField.getText().length() != 1) {
                    displayAlert();
                    return;
                }

                Contact contact = new Contact(-1, nameField.getText().toString(),
                        numberField.getText().toString(),
                        idField.getText().toString().toUpperCase());

                if(contactToEdit != null && !contact.equals(contactToEdit)){
                    DataManager.getInstance().editContact(contactToEdit, contact);
                }
                else {
                    DataManager.getInstance().addContactToDb(contact);
                }
                setResult(Activity.RESULT_OK);

//                if (editingContact) {
//                Intent intent = new Intent(getApplicationContext(), SingleContactActivity.class);
//                intent.putExtra(DotDash.CONTACT_NAME, contact.getName());
//                startActivity(intent);
//                }
//                overridePendingTransition(0, 0);
                finish();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                if (editingContact) {
                    Intent intent = new Intent(this, SingleContactActivity.class);
                    intent.putExtra(DotDash.CONTACT_NAME, contactToEdit.getName());
                    NavUtils.navigateUpTo(this, intent);
//                    startActivity(intent);
//                    finish();
                }
                else {
                    Intent intent = new Intent(this, DotDash.class);
                    NavUtils.navigateUpTo(this, intent);
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void displayAlert()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Cannot save contact").setCancelable(
                false)
                .setMessage("Contacts must have a name, a 10-digit phone number, and a 1-character ID.")
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
