package com.dauntless.starkx.satori.ui;

import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.dauntless.starkx.satori.Adapter.ContactsAdapter;
import com.dauntless.starkx.satori.Model.Contacts;
import com.dauntless.starkx.satori.R;

import java.util.ArrayList;

/**
 * Created by sonu on 6/10/17.
 */

public class FragmentContacts extends Fragment {

    private ListView contactList;
    private ContactsAdapter contactsAdapter;
    private ArrayList<Contacts> contactDetails ;

    public FragmentContacts() {
    }
    public static FragmentContacts newInstance() {
        FragmentContacts fragment = new FragmentContacts();
        return fragment;
    }
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_contacts, container, false);
        contactList = (ListView) rootView.findViewById(R.id.contactsList);
        contactDetails = new ArrayList<>();
        getContacts();
        contactsAdapter = new ContactsAdapter( getActivity() , contactDetails , getContext());
        contactList.setAdapter(contactsAdapter);
        contactList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Contacts contacts= contactDetails.get(position);
                Snackbar.make(view, contacts.name+"\n"+contacts.number, Snackbar.LENGTH_LONG)
                        .setAction("No action", null).show();
            }
        });
        return rootView;
    }
    public void getContacts(){
        Cursor cursor = getActivity().getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, null);
        String name , number;
        assert cursor != null;
        while (cursor.moveToNext()) {
            name = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
            number = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
            contactDetails.add(new Contacts(name ,number));
        }
        cursor.close();
    }
}