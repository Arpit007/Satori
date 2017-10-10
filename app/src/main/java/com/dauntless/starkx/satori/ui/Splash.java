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

import com.dauntless.starkx.satori.R;
import com.dauntless.starkx.satori.etc.Connection;
import com.dauntless.starkx.satori.etc.Connectivity;
import com.dauntless.starkx.satori.etc.ReadContacts;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Map;

public class Splash extends AppCompatActivity {
	private boolean OK;
	private static final int PERMS_REQUEST_CODE = 100;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);

		String[] Permissions = new String[]{ Manifest.permission.READ_CONTACTS, Manifest.permission.WRITE_CONTACTS,
				Manifest.permission.INTERNET, Manifest.permission.CAMERA };

		OK = true;
		for (String perms : Permissions) {
			if (checkCallingOrSelfPermission(perms) != PackageManager.PERMISSION_GRANTED) {
				OK = false;
				ActivityCompat.requestPermissions(this, Permissions, PERMS_REQUEST_CODE);
				break;
			}
		}
		if (OK) {
			Init();
		}
	}

	public void MoveToNext() {
		SharedPreferences preferences = getSharedPreferences("Contacts", MODE_PRIVATE);
		final boolean LoggedIn = !preferences.getString("Number", "").isEmpty();
		UpdateData(getApplicationContext(), LoggedIn);

		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				new Handler().postDelayed(new Runnable() {
					@Override
					public void run() {
						Intent intent;
						if (LoggedIn) {
							intent = new Intent(Splash.this, Home.class);
						}
						else {
							intent = new Intent(Splash.this, Login.class);
						}

						startActivity(intent);
						finish();
					}
				}, getResources().getInteger(R.integer.Splash));
			}
		});
	}

	public void UpdateData(final Context context, final boolean LoggedIn) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					Connection.Get(Connection.getUrl() + "/res/all", new JSONObject(), true, new Connection.ConnectionResponse() {
						@Override
						public void JsonResponse(JSONObject object, boolean Success) {
							try {
								if (!Success) {
									return;
								}
								SharedPreferences.Editor editor = context.getSharedPreferences("Resources", MODE_PRIVATE).edit();
								editor.putString("Data", object.toString());
								editor.apply();
							}
							catch (Exception e) {
								e.printStackTrace();
							}
						}
					});

					if (!LoggedIn) {
						return;
					}

					final Map<String, String> ContactsMap = ReadContacts.getContacts(Splash.this.getApplicationContext());
					JSONArray array = new JSONArray();
					for (String x : ContactsMap.keySet()) {
						array.put(x);
					}

					JSONObject object = new JSONObject();
					object.put("Number", context.getSharedPreferences("Contacts", MODE_PRIVATE).getString("Number", ""));
					object.put("Contacts", array);

					Connection.Post(Connection.getUrl() + "/user/getContacts", object, new Connection.ConnectionResponse() {
						@Override
						public void JsonResponse(JSONObject object, boolean Success) {
							try {
								if (!Success || object.getJSONObject("head").getInt("code") != 200) {
									return;
								}

								JSONArray contacts = object.getJSONObject("body").getJSONArray("contacts");
								JSONArray jsonArray = new JSONArray();

								for (int x = 0; x < contacts.length(); x++) {
									String num = contacts.getString(x);
									JSONObject jsonObject = new JSONObject();
									jsonObject.put("Name", ContactsMap.get(num));
									jsonObject.put("Number", num);
									jsonArray.put(jsonObject);
								}

								SharedPreferences.Editor editor = context.getSharedPreferences("Contacts", MODE_PRIVATE).edit();
								editor.putString("Contacts", jsonArray.toString());
								editor.apply();
							}
							catch (Exception e) {
								e.printStackTrace();
							}
						}
					});
				}
				catch (Exception e) {
					e.printStackTrace();
				}
			}
		}).start();
	}

	public void Init() {
		if (!Connectivity.isNetworkAvailable(this) && !Connectivity.isOnline()) {
			Toast.makeText(this, "No Connection", Toast.LENGTH_LONG).show();
			MoveToNext();
		}
		else {
			Connection.TargetUrl = "http://192.168.137.1:3000";
			MoveToNext();
			/*Connection.Get(Connection.BaseUrl + "/url", new JSONObject(), false, new Connection.ConnectionResponse() {
				@Override
				public void StringResponse(String object, boolean Success) {
					if (Success) {
						String[] data = object.split(" ");
						Connection.TargetUrl = data[2];
					}
					MoveToNext();
				}
			});*/
		}
	}

	@Override
	public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
		super.onRequestPermissionsResult(requestCode, permissions, grantResults);
		Init();
	}
}
