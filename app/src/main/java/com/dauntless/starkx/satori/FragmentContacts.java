package com.dauntless.starkx.satori;

import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.dauntless.starkx.satori.Adapter.ContactsAdapter;

import java.util.ArrayList;

/**
 * Created by sonu on 6/10/17.
 */

public class FragmentContacts extends Fragment {

    private ListView contactList;
    private ContactsAdapter contactsAdapter;
    private ArrayList<String> contactName ;
    private ArrayList<String> contactNumber ;

    public FragmentContacts() {
    }

    public static FragmentContacts newInstance() {
        FragmentContacts fragment = new FragmentContacts();
        return fragment;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_contacts, container, false);
        contactList = (ListView) rootView.findViewById(R.id.contactsList);
        contactName = new ArrayList<>();
        contactNumber = new ArrayList<>();

        getContacts();
        return rootView;
    }

    public void getContacts(){
        Cursor cursor = getActivity().getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, null);
        String name , number;
        assert cursor != null;
        while (cursor.moveToNext()) {
            name = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
            number = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
            contactName.add(name);
            contactNumber.add(number);
//            Log.d("FragmentContacs" , name + " \n"  + number);
        }
        cursor.close();
        Log.d("FragmentContacs" , contactName.size() + "narefffffffffffffffffffffffffffffffffffffffffffffffffff");
        contactsAdapter = new ContactsAdapter( getActivity() , contactName , contactNumber);
        contactList.setAdapter(contactsAdapter);
    }
}
