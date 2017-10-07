package com.dauntless.starkx.satori.ui;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.dauntless.starkx.satori.Home;
import com.dauntless.starkx.satori.R;
import com.dauntless.starkx.satori.etc.Connection;
import com.google.i18n.phonenumbers.PhoneNumberUtil;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Login extends AppCompatActivity
{
	Button login;
	EditText number;
	ProgressDialog progressDialog;
	JSONArray Numbers = new JSONArray();
	Map<String, String> Contacts = new HashMap<>();
	boolean ContactsLoaded = false;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);

		login = (Button) findViewById(R.id.Login);
		number = (EditText) findViewById(R.id.Number);
		progressDialog = new ProgressDialog(this);
		progressDialog.setCancelable(false);
		progressDialog.setMessage("Please Wait...");

		SetUp();
	}

	void SetUp()
	{
		login.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View view)
			{
				login.setEnabled(false);
				progressDialog.show();

				new Thread()
				{
					@Override
					public void run()
					{
						try
						{
							getContacts();
							JSONObject object = new JSONObject();
							object.put("Number", "+91" + number.getText().toString());
							object.put("newContacts", Numbers);
							object.put("remContacts", new JSONArray());

							Connection.Post(Connection.BaseUrl + "/user/login", object, new Connection.ConnectionResponse()
							{
								@Override
								public void JsonResponse(JSONObject object, boolean Success)
								{
									if (!Success)
									{
										hideDialog("Failed");
										return;
									}
									try
									{
										if (object.getJSONObject("head").getInt("code") != 200)
										{
											hideDialog(object.getJSONObject("head").getString("message"));
											return;
										}
										JSONArray contacts = object.getJSONObject("body").getJSONArray("contacts");
										JSONArray jsonArray = new JSONArray();
										JSONArray otherContracts = new JSONArray();
										for (int x = 0; x < contacts.length(); x++)
										{
											String num = contacts.getString(x);
											JSONObject jsonObject = new JSONObject();
											jsonObject.put("Name", Contacts.get(num));
											jsonObject.put("Number", num);
											Contacts.remove(num);
											jsonArray.put(jsonObject);
										}

										for (String key : Contacts.keySet())
										{
											JSONObject jsonObject = new JSONObject();
											jsonObject.put("Name", Contacts.get(key));
											jsonObject.put("Number", key);
											otherContracts.put(jsonObject);
										}

										SharedPreferences.Editor editor = getSharedPreferences("Contacts", MODE_PRIVATE).edit();

										editor.putString("Contacts", jsonArray.toString());
										editor.putString("Number", number.getText().toString());
										editor.putString("Other", otherContracts.toString());
										editor.apply();
										hideDialog("Success");

										Intent intent = new Intent(Login.this, Home.class);
										Login.this.startActivity(intent);
										finish();
									}
									catch (Exception e)
									{
										e.printStackTrace();
										hideDialog("Failed");
									}
								}
							});
						}
						catch (Exception e)
						{
							e.printStackTrace();
							hideDialog("Failed");
						}
					}
				}.start();
			}
		});
	}

	void hideDialog(final String message)
	{
		new Handler(getMainLooper()).post(new Runnable()
		{
			@Override
			public void run()
			{
				login.setEnabled(true);
				if (!message.isEmpty())
				{
					Toast.makeText(Login.this, message, Toast.LENGTH_SHORT).show();
				}
				progressDialog.dismiss();
			}
		});
	}

	public synchronized void getContacts()
	{
		if (ContactsLoaded)
		{
			return;
		}
		ContactsLoaded = true;
		String name, number;
		Cursor cursor = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, null);

		PhoneNumberUtil pnu = PhoneNumberUtil.getInstance();

		while (cursor.moveToNext())
		{
			try
			{
				name = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
				number = pnu.format(pnu.parse(cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)), "IN"),
						PhoneNumberUtil.PhoneNumberFormat.E164);
				Contacts.put(number, name);
				Numbers.put(number);
			}
			catch (Exception e)
			{
			}
		}
		cursor.close();
	}
}
