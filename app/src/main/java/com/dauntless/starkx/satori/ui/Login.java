package com.dauntless.starkx.satori.ui;

import android.app.ProgressDialog;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.dauntless.starkx.satori.R;
import com.dauntless.starkx.satori.etc.Connection;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Map;

public class Login extends AppCompatActivity
{
	Button login;
	EditText number;
	ProgressDialog progressDialog;
	JSONArray Numbers = null;
	Map<String, String> Contacts = null;

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
	}

	void SetUp()
	{
		login.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View view)
			{
				try
				{
					progressDialog.show();
					getContacts();
					JSONObject object = new JSONObject();
					object.put("Number", number.getText().toString());
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
								if (object.getJSONObject("head").getInt("Code") != 200)
								{
									hideDialog(object.getJSONObject("head").getString("message"));
									return;
								}
								JSONArray contacts = object.getJSONObject("body").getJSONArray("contacts");
								for (int x = 0; x < contacts.length(); x++)
								{
									
								}
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
		});
	}

	void hideDialog(String message)
	{
		if (!message.isEmpty())
		{
			Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
		}
		progressDialog.dismiss();
	}

	public void getContacts()
	{
		if (Numbers != null)
		{
			return;
		}
		Numbers = new JSONArray();
		String name, number;
		Cursor cursor = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, null);
		while (cursor.moveToNext())
		{
			name = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
			number = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
			Contacts.put(number, name);
			Numbers.put(number);
		}
		cursor.close();
	}
}
