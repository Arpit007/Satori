package com.dauntless.starkx.satori.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.dauntless.starkx.satori.Model.Contacts;
import com.dauntless.starkx.satori.R;

import java.util.ArrayList;

/**
 * Created by sonu on 6/10/17.
 */

public class ContactsAdapter extends ArrayAdapter<Contacts> {
	private ArrayList<Contacts> contacts;

	public ContactsAdapter(ArrayList<Contacts> contacts, Context mContext) {
		super(mContext, R.layout.contacts_list, contacts);
		this.contacts = contacts;
	}

	@NonNull
	@Override
	public View getView(int position, View convertView, @NonNull ViewGroup parent) {
		if (convertView == null) {
			LayoutInflater inflater = LayoutInflater.from(getContext());
			convertView = inflater.inflate(R.layout.contacts_list, parent, false);
		}

		Contacts contact = contacts.get(position);
		ViewHolder viewHolder = new ViewHolder(convertView);
		viewHolder.name.setText(contact.name);
		viewHolder.number.setText(contact.number);

		return convertView;
	}

	public class ViewHolder {
		TextView name;
		TextView number;

		ViewHolder(View view) {
			name = (TextView) view.findViewById(R.id.name);
			number = (TextView) view.findViewById(R.id.number);
		}
	}
}
