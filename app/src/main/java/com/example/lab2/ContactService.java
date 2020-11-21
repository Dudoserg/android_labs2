package com.example.lab2;

import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.provider.ContactsContract;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ContactService implements  ActivityCompat.OnRequestPermissionsResultCallback{


    private static final String CONTACT_ID = ContactsContract.Contacts._ID;
    private static final String DISPLAY_NAME = ContactsContract.Contacts.DISPLAY_NAME;
    private static final String HAS_PHONE_NUMBER = ContactsContract.Contacts.HAS_PHONE_NUMBER;
    private static final String PHONE_NUMBER = ContactsContract.CommonDataKinds.Phone.NUMBER;
    private static final String PHONE_CONTACT_ID = ContactsContract.CommonDataKinds.Phone.CONTACT_ID;

    public static final int REQUEST_CODE_PERMISSION_READ_CONTACTS = 228;

    private Context _context ;
    private Activity _activity ;

    public ContactService(Context _context, Activity _activity) {
        this._context = _context;
        this._activity = _activity;
    }

    public ContactService() {
    }

    public ArrayList<Contact> getAll() {
        int permissionStatus = ContextCompat.checkSelfPermission(_context, Manifest.permission.READ_CONTACTS);
        if (permissionStatus == PackageManager.PERMISSION_GRANTED) {
            return readContacts(_context);
        } else {
            ActivityCompat.requestPermissions(
                    _activity,
                    new String[] {Manifest.permission.READ_CONTACTS},
                    REQUEST_CODE_PERMISSION_READ_CONTACTS
                    );
        }
        return readContacts(_context);
    }



    private ArrayList<Contact> readContacts(Context context) {
        ContentResolver cr = context.getContentResolver();

        Cursor pCur = cr.query(
                ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                new String[]{PHONE_NUMBER, PHONE_CONTACT_ID},
                null,
                null,
                null
        );
        if(pCur != null){
            if(pCur.getCount() > 0) {
                HashMap<Integer, ArrayList<String>> phones = new HashMap<>();
                while (pCur.moveToNext()) {
                    Integer contactId = pCur.getInt(pCur.getColumnIndex(PHONE_CONTACT_ID));

                    ArrayList<String> curPhones = new ArrayList<>();

                    if (phones.containsKey(contactId)) {
                        curPhones = phones.get(contactId);

                    }
                    curPhones.add(pCur.getString(0));

                    phones.put(contactId, curPhones);
                }
                Cursor cur = cr.query(
                        ContactsContract.Contacts.CONTENT_URI,
                        new String[]{CONTACT_ID, DISPLAY_NAME, HAS_PHONE_NUMBER},
                        HAS_PHONE_NUMBER + " > 0",
                        null,
                        DISPLAY_NAME + " ASC");
                if (cur != null) {
                    if (cur.getCount() > 0) {
                        ArrayList<Contact> contacts = new ArrayList<>();
                        while (cur.moveToNext()) {
                            int id = cur.getInt(cur.getColumnIndex(CONTACT_ID));
                            if(phones.containsKey(id)) {
                                Contact con = new Contact();
                                con.setMy_id(id);
                                con.setName(cur.getString(cur.getColumnIndex(DISPLAY_NAME)));
                                if(id == 127)
                                    System.out.println();

                                List<String> phonesList = new ArrayList<>();
                                for (Object o : phones.get(id).toArray()) {
                                    phonesList.add((String)o);
                                }
                                con.setPhones(phonesList);
//                                con.setPhone(TextUtils.join(",",));

                                contacts.add(con);
                            }
                        }
                        return contacts;
                    }
                    cur.close();
                }
            }
            pCur.close();
        }
        return null;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case ContactService.REQUEST_CODE_PERMISSION_READ_CONTACTS:
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission granted
                    readContacts(_context);
                } else {
                    // permission denied
                }
                return;
        }
    }
}
