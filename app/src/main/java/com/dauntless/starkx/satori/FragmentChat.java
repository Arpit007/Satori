package com.dauntless.starkx.satori;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by sonu on 6/10/17.
 */

public class FragmentChat extends Fragment {
    public FragmentChat() {
    }
    public static FragmentChat newInstance() {
        FragmentChat fragment = new FragmentChat();
        return fragment;
    }
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.activity_start_chat, container, false);
        return rootView;
    }
}
