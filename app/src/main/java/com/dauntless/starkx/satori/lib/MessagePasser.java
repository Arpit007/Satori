package com.dauntless.starkx.satori.lib;

import android.os.Parcelable;

/**
 * Created by Home Laptop on 08-Oct-17.
 */
public interface MessagePasser {
	public void PassMessage(Parcelable Message, int Code);
}
