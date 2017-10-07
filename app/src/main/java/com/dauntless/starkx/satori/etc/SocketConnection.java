package com.dauntless.starkx.satori.etc;

/**
 * Created by Home Laptop on 07-Oct-17.
 */

import android.util.Log;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

public class SocketConnection
{
	private String Number;
	private static SocketConnection socketConnection;
	private Socket socket;

	private SocketConnection()
	{
		socketConnection = this;
	}

	public static synchronized SocketConnection getInstance()
	{
		if (socketConnection == null)
		{
			socketConnection = new SocketConnection();
		}
		return socketConnection;
	}

	public void initialize(final String Number)
	{
		try
		{
			this.Number = Number;
			socket = IO.socket(Connection.getUrl());
			socket.on(Socket.EVENT_CONNECT, new Emitter.Listener()
			{
				@Override
				public void call(Object... args)
				{
					try
					{
						socket.emit("verify", Number);
					}
					catch (Exception e)
					{
						e.printStackTrace();
					}
				}

			}).on(Socket.EVENT_DISCONNECT, new Emitter.Listener()
			{

				@Override
				public void call(Object... args)
				{
					Log.d("Socket", "Disconnected");
				}

			});
			socket.connect();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	public void reconnect()
	{
		if(Number.isEmpty())
			return;

		if (socket == null)
		{
			initialize(Number);
		}
		else if (!socket.connected())
		{
			socket.connect();
		}
	}

	public void registerEvent(String Event, Emitter.Listener Handler){
		socket.on(Event, Handler);
	}

	public Socket getSocket()
	{
		return socket;
	}

}
