package com.dauntless.starkx.satori.Adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
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

public class ContactsAdapter extends ArrayAdapter<Contacts>
{

	private static LayoutInflater inflater = null;
	private Activity activity;
	private ArrayList<Contacts> contacts;
	private Context mContext;

	public ContactsAdapter(Activity activity, ArrayList<Contacts> contacts, Context mContext)
	{
		super(mContext, R.layout.contacts_list, contacts);
		this.activity = activity;
		this.contacts = contacts;
		this.mContext = mContext;
	}

	@NonNull
	@Override
	public View getView(int position, View convertView, @NonNull ViewGroup parent)
	{

		Contacts C = getItem(position);
		ViewHolder viewHolder;
		View vi;
		if (convertView == null)
		{
			viewHolder = new ViewHolder();
			LayoutInflater inflater = LayoutInflater.from(getContext());
			convertView = inflater.inflate(R.layout.contacts_list, parent, false);
			viewHolder.name = (TextView) convertView.findViewById(R.id.name);
			viewHolder.number = (TextView) convertView.findViewById(R.id.number);
			vi = convertView;
			convertView.setTag(viewHolder);
		}
		else {
			viewHolder = (ViewHolder) convertView.getTag();
			vi = convertView;
		}

		viewHolder.name.setText(C.name);
		viewHolder.number.setText(C.number);

		return vi;
	}

	public class ViewHolder
	{
		TextView name;
		TextView number;
	}


}
