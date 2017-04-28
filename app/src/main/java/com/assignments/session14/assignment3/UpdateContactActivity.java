package com.assignments.session14.assignment3;

import android.content.ContentProviderOperation;
import android.content.OperationApplicationException;
import android.os.RemoteException;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class UpdateContactActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_contact);

        // initialise Activity elements/views
        TextView tvOldName = (TextView) findViewById(R.id.tv_old_name);
        TextView tvOldMobile = (TextView) findViewById(R.id.tv_old_mobile);

        final EditText etNewMobile = (EditText) findViewById(R.id.et_new_mobile);

        Button btnUpdateContact = (Button) findViewById(R.id.bt_update_contact);

        // set text/values
        final String oldName = getIntent().getStringExtra("name");
        final String oldMobile = getIntent().getStringExtra("mobile");
        tvOldName.setText(oldName);
        tvOldMobile.setText(oldMobile);


        // set update contact functionality
        if (btnUpdateContact != null) {
            btnUpdateContact.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String newMobile = etNewMobile.getText().toString().trim().equals("") ? oldMobile : etNewMobile.getText().toString();

                    // create ArrayList ContentProviderOperation
                    ArrayList<ContentProviderOperation> cpoList = new ArrayList<>();

                    // set selection string
                    String selection = ContactsContract.Data.DISPLAY_NAME + " = ? AND "
                            + ContactsContract.Data.MIMETYPE + " = ? AND "
                            + String.valueOf(ContactsContract.CommonDataKinds.Phone.TYPE) + " = ?";

                    // set selectionArgs array
                    String[] selectionArgs = new String[] {oldName,
                            ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE,
                            String.valueOf(ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE)};

                    // update Mobile
                    cpoList.add(ContentProviderOperation
                                    .newUpdate(ContactsContract.Data.CONTENT_URI)
                                    .withSelection(selection, selectionArgs)
                                    .withValue(ContactsContract.CommonDataKinds.Phone.DATA, newMobile)
                                    .build());

                    // execute
                    try {
                        getContentResolver().applyBatch(ContactsContract.AUTHORITY, cpoList);
                        Toast.makeText(getApplicationContext(), "Contact Updated", Toast.LENGTH_SHORT).show();
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    } catch (OperationApplicationException e) {
                        e.printStackTrace();
                    }

                    finish();
                }
            });
        }

    }
}
