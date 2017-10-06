package com.dauntless.starkx.satori;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by sonu on 6/10/17.
 */

public class FragmentChat extends Fragment
{

	private static final String ARG_SECTION_NUMBER = "section_number";

	public FragmentChat()
	{
	}

	public static FragmentChat newInstance(int sectionNumber)
	{
		FragmentChat fragment = new FragmentChat();
		Bundle args = new Bundle();
		args.putInt(ARG_SECTION_NUMBER, sectionNumber);
		fragment.setArguments(args);
		return fragment;
	}

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
	                         Bundle savedInstanceState)
	{
		View rootView = inflater.inflate(R.layout.fragment_chat, container, false);
		return rootView;
	}
}
