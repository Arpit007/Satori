package com.dauntless.starkx.satori.etc;

import android.content.Context;
import android.database.Cursor;
import android.provider.ContactsContract;

import com.google.i18n.phonenumbers.PhoneNumberUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Home Laptop on 08-Oct-17.
 */

public class ReadContacts {
	public static synchronized Map<String, String> getContacts(Context context) {
		Map<String, String> ContactsMap = new HashMap<>();

		String name, number;
		Cursor cursor = context.getContentResolver()
				.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, null);
		PhoneNumberUtil pnu = PhoneNumberUtil.getInstance();

		while (cursor.moveToNext()) {
			try {
				name = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
				number = pnu.format(pnu.parse(cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)), "IN"),
						PhoneNumberUtil.PhoneNumberFormat.E164);
				ContactsMap.put(number, name);
			}
			catch (Exception e) {
			}
		}
		cursor.close();
		return ContactsMap;
	}
}
