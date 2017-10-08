package com.dauntless.starkx.satori.ui;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.dauntless.starkx.satori.R;
import com.dauntless.starkx.satori.etc.Connection;
import com.dauntless.starkx.satori.etc.ReadContacts;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Map;

public class Login extends AppCompatActivity {
	Button login;
	EditText number;
	ProgressDialog progressDialog;
	Map<String, String> ContactsMap;
	JSONArray Numbers;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);

		login = (Button) findViewById(R.id.Login);
		number = (EditText) findViewById(R.id.Number);
		progressDialog = new ProgressDialog(this);
		progressDialog.setCancelable(false);
		progressDialog.setMessage("Please Wait...");

		SetUp();
	}

	void SetUp() {
		SetUpContacts();

		login.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				login.setEnabled(false);
				progressDialog.show();

				new Thread() {
					@Override
					public void run() {
						try {
							JSONObject object = new JSONObject();
							object.put("Number", "+91" + number.getText().toString());
							object.put("Contacts", Numbers);

							Connection.Post(Connection.getUrl() + "/user/login", object, new Connection.ConnectionResponse() {
								@Override
								public void JsonResponse(JSONObject object, boolean Success) {
									if (!Success) {
										hideDialog("Failed");
										return;
									}
									try {
										if (object.getJSONObject("head").getInt("code") != 200) {
											hideDialog(object.getJSONObject("head").getString("message"));
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

										SharedPreferences.Editor editor = getSharedPreferences("Contacts", MODE_PRIVATE).edit();

										editor.putString("Contacts", jsonArray.toString());
										editor.putString("Number", "+91" + number.getText().toString());
										editor.apply();
										hideDialog("Success");

										Intent intent = new Intent(Login.this, Home.class);
										Login.this.startActivity(intent);
										finish();
									}
									catch (Exception e) {
										e.printStackTrace();
										hideDialog("Failed");
									}
								}
							});
						}
						catch (Exception e) {
							e.printStackTrace();
							hideDialog("Failed");
						}
					}
				}.start();
			}
		});
	}

	void hideDialog(final String message) {
		new Handler(getMainLooper()).post(new Runnable() {
			@Override
			public void run() {
				login.setEnabled(true);
				if (!message.isEmpty()) {
					Toast.makeText(Login.this, message, Toast.LENGTH_SHORT).show();
				}
				progressDialog.dismiss();
			}
		});
	}

	void SetUpContacts() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				ContactsMap = ReadContacts.getContacts(Login.this.getApplicationContext());
				Numbers = new JSONArray();
				for (String x : ContactsMap.keySet()) {
					Numbers.put(x);
				}
			}
		}).start();
	}
}
