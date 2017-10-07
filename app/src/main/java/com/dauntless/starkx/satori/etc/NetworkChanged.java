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
		SocketConnection.getInstance().reconnect();
	}
}