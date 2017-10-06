package com.dauntless.starkx.satori.Adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.dauntless.starkx.satori.Model.Contacts;
import com.dauntless.starkx.satori.R;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by sonu on 6/10/17.
 */

public class ContactsAdapter extends ArrayAdapter<Contacts> {


    private static LayoutInflater inflater = null;
    private Activity activity;
    private ArrayList<Contacts> contacts;
    private Context mContext;

    public ContactsAdapter(Activity activity , ArrayList<Contacts> contacts , Context mContext) {
        super(mContext , R.layout.contacts_list , contacts);
        this.activity = activity;
        this.contacts = contacts;
    }

    @Override
    public int getCount() {
        return 0;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }
    public class Holder {
        TextView name;
        TextView number;
    }

    @SuppressLint({"ViewHolder", "SetTextI18n", "InflateParams"})
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Holder holder=new Holder();
        View vi ;
        vi = inflater.inflate(R.layout.contacts_list, null);

        holder.name = (TextView) vi.findViewById(R.id.name);
        holder.number = (TextView) vi.findViewById(R.id.number);
        Log.d("Adapter " , contactName.get(position));
        // Setting all values in listview
        holder.name.setText(contactName.get(position) + "nk");
        holder.number.setText(contactNumber.get(position) + "nk");
        return vi;
    }


}
