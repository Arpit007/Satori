package com.dauntless.starkx.satori.ui;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.dauntless.starkx.satori.Model.Contacts;
import com.dauntless.starkx.satori.R;
import com.dauntless.starkx.satori.etc.Connection;
import com.dauntless.starkx.satori.etc.Connectivity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Splash extends AppCompatActivity
{
	private boolean OK;
	private static final int PERMS_REQUEST_CODE = 100;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);

		String[] Permissions = new String[]{ Manifest.permission.READ_CONTACTS, Manifest.permission.WRITE_CONTACTS,
				Manifest.permission.INTERNET, Manifest.permission.CAMERA };

		OK = true;
		for(String perms : Permissions){
			if(checkCallingOrSelfPermission(perms) != PackageManager.PERMISSION_GRANTED){
				OK = false;
				ActivityCompat.requestPermissions(this, Permissions, PERMS_REQUEST_CODE);
				break;
			}
		}
		if(OK)Init();
	}

	public void MoveToNext(){
		runOnUiThread(new Runnable()
		{
			@Override
			public void run()
			{
				new Handler().postDelayed(new Runnable()
				{
					@Override
					public void run()
					{
						Intent intent;
						SharedPreferences preferences = getSharedPreferences("Contacts", MODE_PRIVATE);
						if (preferences.getString("Number", "").isEmpty())
						{
							intent = new Intent(Splash.this, Login.class);
						}
						else
						{
							UpdateContacts(getApplicationContext());
							intent = new Intent(Splash.this, Home.class);
						}

						startActivity(intent);
						finish();
					}
				}, getResources().getInteger(R.integer.Splash));
			}
		});
	}

	public static void UpdateContacts(final Context context)
	{
		try
		{
			JSONObject object = new JSONObject();
			object.put("Number", context.getSharedPreferences("Contacts", MODE_PRIVATE).getString("Number", ""));

			Connection.Post(Connection.getUrl() + "/user/getContacts", object, new Connection.ConnectionResponse()
			{
				@Override
				public void JsonResponse(JSONObject object, boolean Success)
				{
					if (!Success)
					{
						return;
					}
					try
					{
						if (object.getJSONObject("head").getInt("code") != 200)
						{
							return;
						}
						JSONArray contacts = object.getJSONObject("body").getJSONArray("contacts");
						JSONArray jsonArray = new JSONArray();
						JSONArray otherContracts = new JSONArray();
						Map<String, Contacts> Contacts = readContacts(context);
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

						SharedPreferences.Editor editor = context.getSharedPreferences("Contacts", MODE_PRIVATE).edit();

						editor.putString("Contacts", jsonArray.toString());
						editor.putString("Other", otherContracts.toString());
						editor.apply();
					}
					catch (Exception e)
					{
						e.printStackTrace();
					}
				}
			});
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	public static Map<String, Contacts> readContacts(Context context)
	{
		Map<String, Contacts> contactDetails = new HashMap<>();
		SharedPreferences preferences=context.getSharedPreferences("Contacts", Context.MODE_PRIVATE);
		try
		{
			JSONArray contacts = new JSONArray(preferences.getString("Contacts", "[]"));
			for(int x=0;x<contacts.length();x++){
				JSONObject object = contacts.getJSONObject(x);
				Contacts contact = new Contacts(object.getString("Name"), object.getString("Number"));
				contactDetails.put(contact.number, contact);
			}
			contacts = new JSONArray(preferences.getString("Other", "[]"));
			for(int x=0;x<contacts.length();x++){
				JSONObject object = contacts.getJSONObject(x);
				Contacts contact = new Contacts(object.getString("Name"), object.getString("Number"));
				contactDetails.put(contact.number, contact);
			}
		}
		catch (Exception e){
			e.printStackTrace();
		}
		return contactDetails;
	}

	public void Init()
	{
		if(!Connectivity.isNetworkAvailable(this) && !Connectivity.isOnline())
		{
			Toast.makeText(this, "No Connection", Toast.LENGTH_LONG).show();
			MoveToNext();
		}
		else
		{
			Connection.Get(Connection.BaseUrl + "/url", new JSONObject(), false, new Connection.ConnectionResponse()
			{
				@Override
				public void StringResponse(String object, boolean Success)
				{
					if(Success){
						String[] data = object.split(" ");
						Connection.TargetUrl = data[2];
					}
					MoveToNext();
				}
			});
		}
	}

	@Override
	public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
	{
		super.onRequestPermissionsResult(requestCode, permissions, grantResults);
		Init();
	}
}
