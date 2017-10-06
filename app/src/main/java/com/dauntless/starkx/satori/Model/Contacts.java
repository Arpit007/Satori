package com.dauntless.starkx.satori.Model;

import java.io.Serializable;

/**
 * Created by sonu on 6/10/17.
 */

public class Contacts implements Serializable
{
	public String name;
	public String number;

	public Contacts() {
	}

	public Contacts(String name, String number)
	{
		this.name = name;
		this.number = number;
	}
}
