package com.dauntless.starkx.satori.ui;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;

import com.dauntless.starkx.satori.Home;
import com.dauntless.starkx.satori.R;

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

	public void Init()
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
					intent = new Intent(Splash.this, Home.class);
				}

				startActivity(intent);
				finish();
			}
		}, getResources().getInteger(R.integer.Splash));
	}

	@Override
	public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
	{
		super.onRequestPermissionsResult(requestCode, permissions, grantResults);
		Init();
	}
}
