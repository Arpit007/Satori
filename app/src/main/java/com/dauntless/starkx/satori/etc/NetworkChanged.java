package com.dauntless.starkx.satori.etc;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by Home Laptop on 07-Oct-17.
 */

public class NetworkChanged extends BroadcastReceiver
{
	public NetworkChanged()
	{
	}

	@Override
	public void onReceive(Context context, Intent intent)
	{
		if (Connectivity.isOnline()) {
			if (SocketConnection.getInstance().getSocket() == null || !SocketConnection.getInstance().getSocket().connected()) {
				SocketConnection.getInstance().initialize(context.getSharedPreferences("Contacts", Context.MODE_PRIVATE).getString("Number", ""));
			}
			else {
				SocketConnection.getInstance().reconnect();
			}
		}
	}
}